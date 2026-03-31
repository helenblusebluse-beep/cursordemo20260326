package com.zhongzhou.modules.nursing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.nursing.dto.NursingPlanItemRequest;
import com.zhongzhou.modules.nursing.dto.NursingPlanSaveRequest;
import com.zhongzhou.modules.nursing.entity.NursingCareLevel;
import com.zhongzhou.modules.nursing.entity.NursingItem;
import com.zhongzhou.modules.nursing.entity.NursingPlan;
import com.zhongzhou.modules.nursing.entity.NursingPlanItem;
import com.zhongzhou.modules.nursing.mapper.NursingCareLevelMapper;
import com.zhongzhou.modules.nursing.mapper.NursingItemMapper;
import com.zhongzhou.modules.nursing.mapper.NursingPlanItemMapper;
import com.zhongzhou.modules.nursing.mapper.NursingPlanMapper;
import com.zhongzhou.modules.nursing.vo.NursingPlanItemVO;
import com.zhongzhou.modules.nursing.vo.NursingPlanVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NursingPlanService {
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private static final Set<String> CYCLES = Set.of("每日", "每周", "每月");
    private final NursingPlanMapper planMapper;
    private final NursingPlanItemMapper itemMapper;
    private final NursingItemMapper nursingItemMapper;
    private final NursingCareLevelMapper careLevelMapper;

    public NursingPlanService(NursingPlanMapper planMapper, NursingPlanItemMapper itemMapper, NursingItemMapper nursingItemMapper, NursingCareLevelMapper careLevelMapper) {
        this.planMapper = planMapper;
        this.itemMapper = itemMapper;
        this.nursingItemMapper = nursingItemMapper;
        this.careLevelMapper = careLevelMapper;
    }

    public PageResult<NursingPlanVO> page(String name, Integer status, int page, int pageSize) {
        List<NursingPlan> plans = planMapper.selectList(new LambdaQueryWrapper<NursingPlan>()
                        .eq(NursingPlan::getIsDeleted, 0)
                        .orderByAsc(NursingPlan::getSortOrder)
                        .orderByAsc(NursingPlan::getId));
        Map<Long, Long> bindCounts = countPlanBindings(plans.stream().map(NursingPlan::getId).collect(Collectors.toList()));
        List<NursingPlanVO> all = plans.stream().map(p -> toListVO(p, bindCounts.getOrDefault(p.getId(), 0L))).collect(Collectors.toList());
        if (StringUtils.hasText(name)) {
            String kw = name.trim();
            all = all.stream().filter(x -> x.getPlanName() != null && x.getPlanName().contains(kw)).collect(Collectors.toList());
        }
        if (status != null) {
            all = all.stream().filter(x -> status.equals(x.getStatus())).collect(Collectors.toList());
        }
        int from = Math.max(0, (page - 1) * pageSize);
        int to = Math.min(all.size(), from + pageSize);
        List<NursingPlanVO> rows = from >= all.size() ? new ArrayList<>() : all.subList(from, to);
        return new PageResult<>(all.size(), rows);
    }

    public NursingPlanVO detail(Long id) {
        NursingPlan p = load(id);
        NursingPlanVO vo = toListVO(p, countPlanBindings(p.getId()));
        vo.setItems(itemMapper.selectList(new LambdaQueryWrapper<NursingPlanItem>()
                        .eq(NursingPlanItem::getPlanId, id)
                        .orderByAsc(NursingPlanItem::getRowNo)
                        .orderByAsc(NursingPlanItem::getId))
                .stream()
                .map(this::toItemVO)
                .collect(Collectors.toList()));
        return vo;
    }

    @Transactional
    public Long create(NursingPlanSaveRequest req) {
        validateCommon(req, null);
        NursingPlan p = new NursingPlan();
        p.setPlanName(req.getPlanName().trim());
        p.setStatus(req.getStatus());
        p.setSortOrder(req.getSortOrder());
        p.setIsDeleted(0);
        p.setCreatedTime(LocalDateTime.now());
        p.setUpdatedTime(LocalDateTime.now());
        planMapper.insert(p);
        saveItems(p.getId(), req.getItems());
        return p.getId();
    }

    @Transactional
    public void update(Long id, NursingPlanSaveRequest req) {
        NursingPlan p = load(id);
        if (isBound(p)) {
            throw new IllegalArgumentException("该护理计划已与护理等级绑定，不能修改");
        }
        validateCommon(req, id);
        p.setPlanName(req.getPlanName().trim());
        p.setStatus(req.getStatus());
        p.setSortOrder(req.getSortOrder());
        p.setUpdatedTime(LocalDateTime.now());
        planMapper.updateById(p);
        itemMapper.delete(new LambdaQueryWrapper<NursingPlanItem>().eq(NursingPlanItem::getPlanId, id));
        saveItems(id, req.getItems());
    }

    public void remove(Long id) {
        NursingPlan p = load(id);
        if (isBound(p)) {
            throw new IllegalArgumentException("该护理计划已与护理等级绑定，不能删除");
        }
        p.setIsDeleted(1);
        p.setUpdatedTime(LocalDateTime.now());
        planMapper.updateById(p);
    }

    public void changeStatus(Long id, Integer status) {
        if (status == null || (status != 1 && status != 2)) {
            throw new IllegalArgumentException("状态值非法");
        }
        NursingPlan p = load(id);
        if (status == 2 && isBound(p)) {
            throw new IllegalArgumentException("该护理计划已与护理等级绑定，不能禁用");
        }
        p.setStatus(status);
        p.setUpdatedTime(LocalDateTime.now());
        planMapper.updateById(p);
    }

    private void saveItems(Long planId, List<NursingPlanItemRequest> reqItems) {
        List<Long> ids = reqItems.stream().map(NursingPlanItemRequest::getNursingItemId).collect(Collectors.toList());
        Map<Long, NursingItem> itemMap = nursingItemMapper.selectBatchIds(ids).stream()
                .filter(x -> x.getStatus() != null && x.getStatus() == 1 && (x.getIsDeleted() == null || x.getIsDeleted() == 0))
                .collect(Collectors.toMap(NursingItem::getId, Function.identity(), (a, b) -> a));
        for (int i = 0; i < reqItems.size(); i++) {
            NursingPlanItemRequest req = reqItems.get(i);
            NursingItem nursingItem = itemMap.get(req.getNursingItemId());
            if (nursingItem == null) {
                throw new IllegalArgumentException("护理项目信息不完整，请重新设置");
            }
            NursingPlanItem row = new NursingPlanItem();
            row.setPlanId(planId);
            row.setRowNo(i + 1);
            row.setNursingItemId(req.getNursingItemId());
            row.setNursingItemName(nursingItem.getItemName());
            row.setExpectedServiceTime(req.getExpectedServiceTime());
            row.setExecuteCycle(req.getExecuteCycle());
            row.setExecuteFrequency(req.getExecuteFrequency());
            row.setCreatedTime(LocalDateTime.now());
            row.setUpdatedTime(LocalDateTime.now());
            itemMapper.insert(row);
        }
    }

    private void validateCommon(NursingPlanSaveRequest req, Long excludeId) {
        validateNoEmoji(req.getPlanName(), "护理计划名称");
        if (req.getStatus() == null || (req.getStatus() != 1 && req.getStatus() != 2)) {
            throw new IllegalArgumentException("状态值非法");
        }
        if (req.getItems() == null || req.getItems().isEmpty()) {
            throw new IllegalArgumentException("护理项目信息不完整，请重新设置");
        }
        for (NursingPlanItemRequest item : req.getItems()) {
            if (item.getNursingItemId() == null) {
                throw new IllegalArgumentException("护理项目信息不完整，请重新设置");
            }
            if (!StringUtils.hasText(item.getExpectedServiceTime())) {
                item.setExpectedServiceTime("08:00");
            }
            if (!StringUtils.hasText(item.getExecuteCycle())) {
                item.setExecuteCycle("每周");
            }
            if (!CYCLES.contains(item.getExecuteCycle())) {
                throw new IllegalArgumentException("执行周期非法");
            }
            if (item.getExecuteFrequency() == null || item.getExecuteFrequency() < 1 || item.getExecuteFrequency() > 7) {
                throw new IllegalArgumentException("请输入1-7的整数");
            }
        }
        LambdaQueryWrapper<NursingPlan> wrapper = new LambdaQueryWrapper<NursingPlan>()
                .eq(NursingPlan::getIsDeleted, 0)
                .eq(NursingPlan::getPlanName, req.getPlanName().trim());
        if (excludeId != null) {
            wrapper.ne(NursingPlan::getId, excludeId);
        }
        if (planMapper.selectCount(wrapper) > 0) {
            throw new IllegalArgumentException("护理计划名称已存在");
        }
    }

    private NursingPlan load(Long id) {
        NursingPlan p = planMapper.selectById(id);
        if (p == null || (p.getIsDeleted() != null && p.getIsDeleted() == 1)) {
            throw new IllegalArgumentException("护理计划不存在");
        }
        return p;
    }

    private NursingPlanVO toListVO(NursingPlan p, long bindCount) {
        NursingPlanVO vo = new NursingPlanVO();
        vo.setId(p.getId());
        vo.setPlanName(p.getPlanName());
        vo.setSortOrder(p.getSortOrder());
        vo.setStatus(p.getStatus());
        vo.setStatusLabel(p.getStatus() != null && p.getStatus() == 1 ? "启用" : "禁用");
        vo.setCreatedTime(p.getCreatedTime() == null ? "-" : p.getCreatedTime().format(DT));
        vo.setBindLevelCount((int) bindCount);
        boolean bound = bindCount > 0;
        vo.setCanEdit(!bound);
        vo.setCanDelete(!bound);
        vo.setCanDisable(!bound);
        return vo;
    }

    private NursingPlanItemVO toItemVO(NursingPlanItem item) {
        NursingPlanItemVO vo = new NursingPlanItemVO();
        vo.setRowNo(item.getRowNo());
        vo.setNursingItemId(item.getNursingItemId());
        vo.setNursingItemName(item.getNursingItemName());
        vo.setExpectedServiceTime(item.getExpectedServiceTime());
        vo.setExecuteCycle(item.getExecuteCycle());
        vo.setExecuteFrequency(item.getExecuteFrequency());
        return vo;
    }

    private boolean isBound(NursingPlan p) {
        return countPlanBindings(p.getId()) > 0;
    }

    private long countPlanBindings(Long planId) {
        return careLevelMapper.selectCount(new LambdaQueryWrapper<NursingCareLevel>()
                .eq(NursingCareLevel::getIsDeleted, 0)
                .eq(NursingCareLevel::getPlanId, planId));
    }

    private Map<Long, Long> countPlanBindings(List<Long> planIds) {
        if (planIds == null || planIds.isEmpty()) {
            return Map.of();
        }
        return careLevelMapper.selectList(new LambdaQueryWrapper<NursingCareLevel>()
                        .eq(NursingCareLevel::getIsDeleted, 0)
                        .in(NursingCareLevel::getPlanId, planIds))
                .stream()
                .collect(Collectors.groupingBy(NursingCareLevel::getPlanId, Collectors.counting()));
    }

    private void validateNoEmoji(String s, String label) {
        for (int i = 0; i < s.length(); ) {
            int cp = s.codePointAt(i);
            i += Character.charCount(cp);
            if (isEmojiCodePoint(cp)) {
                throw new IllegalArgumentException(label + "不能包含表情符号");
            }
        }
    }

    private static boolean isEmojiCodePoint(int cp) {
        return (cp >= 0x1F300 && cp <= 0x1FAFF)
                || (cp >= 0x2600 && cp <= 0x27BF)
                || (cp >= 0xFE00 && cp <= 0xFE0F)
                || cp == 0x200D
                || cp == 0x20E3;
    }
}
