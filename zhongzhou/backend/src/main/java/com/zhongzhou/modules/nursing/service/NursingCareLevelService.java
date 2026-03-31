package com.zhongzhou.modules.nursing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.checkin.entity.CheckinApplication;
import com.zhongzhou.modules.checkin.mapper.CheckinApplicationMapper;
import com.zhongzhou.modules.nursing.dto.NursingCareLevelSaveRequest;
import com.zhongzhou.modules.nursing.entity.NursingCareLevel;
import com.zhongzhou.modules.nursing.entity.NursingPlan;
import com.zhongzhou.modules.nursing.mapper.NursingCareLevelMapper;
import com.zhongzhou.modules.nursing.mapper.NursingPlanMapper;
import com.zhongzhou.modules.nursing.vo.NursingCareLevelItemVO;
import com.zhongzhou.modules.nursing.vo.NursingCareLevelOptionVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NursingCareLevelService {
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private final NursingCareLevelMapper levelMapper;
    private final NursingPlanMapper planMapper;
    private final CheckinApplicationMapper checkinMapper;

    public NursingCareLevelService(NursingCareLevelMapper levelMapper, NursingPlanMapper planMapper, CheckinApplicationMapper checkinMapper) {
        this.levelMapper = levelMapper;
        this.planMapper = planMapper;
        this.checkinMapper = checkinMapper;
    }

    public PageResult<NursingCareLevelItemVO> page(String levelName, Integer status, int page, int pageSize) {
        List<NursingCareLevel> all = levelMapper.selectList(new LambdaQueryWrapper<NursingCareLevel>()
                .eq(NursingCareLevel::getIsDeleted, 0)
                .orderByAsc(NursingCareLevel::getId));
        if (StringUtils.hasText(levelName)) {
            String kw = levelName.trim();
            all = all.stream().filter(x -> x.getLevelName() != null && x.getLevelName().contains(kw)).collect(Collectors.toList());
        }
        if (status != null) {
            all = all.stream().filter(x -> status.equals(x.getStatus())).collect(Collectors.toList());
        }
        Map<String, Long> elderCountMap = checkinMapper.selectList(new LambdaQueryWrapper<CheckinApplication>()
                        .eq(CheckinApplication::getIsDeleted, 0)
                        .eq(CheckinApplication::getIsRetired, 0))
                .stream()
                .collect(Collectors.groupingBy(CheckinApplication::getCareLevel, Collectors.counting()));
        List<NursingCareLevelItemVO> vos = all.stream().map(x -> toItem(x, elderCountMap.getOrDefault(x.getLevelName(), 0L))).collect(Collectors.toList());
        int from = Math.max(0, (page - 1) * pageSize);
        int to = Math.min(vos.size(), from + pageSize);
        List<NursingCareLevelItemVO> rows = from >= vos.size() ? new ArrayList<>() : vos.subList(from, to);
        return new PageResult<>(vos.size(), rows);
    }

    public List<NursingCareLevelOptionVO> listEnabledOptions() {
        return levelMapper.selectList(new LambdaQueryWrapper<NursingCareLevel>()
                        .eq(NursingCareLevel::getIsDeleted, 0)
                        .eq(NursingCareLevel::getStatus, 1)
                        .orderByAsc(NursingCareLevel::getId))
                .stream()
                .map(x -> {
                    NursingCareLevelOptionVO vo = new NursingCareLevelOptionVO();
                    vo.setLevelName(x.getLevelName());
                    vo.setNursingFee(x.getNursingFee());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    public Long create(NursingCareLevelSaveRequest req) {
        validateCommon(req, null);
        NursingPlan plan = loadPlan(req.getPlanId());
        NursingCareLevel level = new NursingCareLevel();
        level.setLevelName(req.getLevelName().trim());
        level.setPlanId(plan.getId());
        level.setPlanName(plan.getPlanName());
        level.setNursingFee(req.getNursingFee());
        level.setStatus(req.getStatus());
        level.setLevelDesc(req.getLevelDesc().trim());
        level.setIsDeleted(0);
        level.setCreatedTime(LocalDateTime.now());
        level.setUpdatedTime(LocalDateTime.now());
        levelMapper.insert(level);
        return level.getId();
    }

    public void update(Long id, NursingCareLevelSaveRequest req) {
        NursingCareLevel level = load(id);
        if (isBoundWithElder(level.getLevelName())) {
            throw new IllegalArgumentException("该护理等级已与老人绑定，不能修改");
        }
        validateCommon(req, id);
        NursingPlan plan = loadPlan(req.getPlanId());
        level.setLevelName(req.getLevelName().trim());
        level.setPlanId(plan.getId());
        level.setPlanName(plan.getPlanName());
        level.setNursingFee(req.getNursingFee());
        level.setStatus(req.getStatus());
        level.setLevelDesc(req.getLevelDesc().trim());
        level.setUpdatedTime(LocalDateTime.now());
        levelMapper.updateById(level);
    }

    public void delete(Long id) {
        NursingCareLevel level = load(id);
        if (isBoundWithElder(level.getLevelName())) {
            throw new IllegalArgumentException("该护理等级已与老人绑定，不能删除");
        }
        level.setIsDeleted(1);
        level.setUpdatedTime(LocalDateTime.now());
        levelMapper.updateById(level);
    }

    public void changeStatus(Long id, Integer status) {
        if (status == null || (status != 1 && status != 2)) {
            throw new IllegalArgumentException("状态值非法");
        }
        NursingCareLevel level = load(id);
        if (status == 2 && isBoundWithElder(level.getLevelName())) {
            throw new IllegalArgumentException("该护理等级已与老人绑定，不能禁用");
        }
        level.setStatus(status);
        level.setUpdatedTime(LocalDateTime.now());
        levelMapper.updateById(level);
    }

    private void validateCommon(NursingCareLevelSaveRequest req, Long excludeId) {
        validateNoEmoji(req.getLevelName(), "等级名称");
        validateNoEmoji(req.getLevelDesc(), "等级说明");
        if (req.getStatus() == null || (req.getStatus() != 1 && req.getStatus() != 2)) {
            throw new IllegalArgumentException("状态值非法");
        }
        LambdaQueryWrapper<NursingCareLevel> uq = new LambdaQueryWrapper<NursingCareLevel>()
                .eq(NursingCareLevel::getIsDeleted, 0)
                .eq(NursingCareLevel::getLevelName, req.getLevelName().trim());
        if (excludeId != null) {
            uq.ne(NursingCareLevel::getId, excludeId);
        }
        if (levelMapper.selectCount(uq) > 0) {
            throw new IllegalArgumentException("等级名称已存在");
        }
    }

    private NursingPlan loadPlan(Long planId) {
        NursingPlan plan = planMapper.selectById(planId);
        if (plan == null || (plan.getIsDeleted() != null && plan.getIsDeleted() == 1)) {
            throw new IllegalArgumentException("护理计划不存在");
        }
        if (plan.getStatus() == null || plan.getStatus() != 1) {
            throw new IllegalArgumentException("护理计划须为启用状态");
        }
        return plan;
    }

    private NursingCareLevel load(Long id) {
        NursingCareLevel level = levelMapper.selectById(id);
        if (level == null || (level.getIsDeleted() != null && level.getIsDeleted() == 1)) {
            throw new IllegalArgumentException("护理等级不存在");
        }
        return level;
    }

    private boolean isBoundWithElder(String levelName) {
        return checkinMapper.selectCount(new LambdaQueryWrapper<CheckinApplication>()
                .eq(CheckinApplication::getIsDeleted, 0)
                .eq(CheckinApplication::getIsRetired, 0)
                .eq(CheckinApplication::getCareLevel, levelName)) > 0;
    }

    private NursingCareLevelItemVO toItem(NursingCareLevel x, long elderCount) {
        NursingCareLevelItemVO vo = new NursingCareLevelItemVO();
        vo.setId(x.getId());
        vo.setLevelName(x.getLevelName());
        vo.setPlanId(x.getPlanId());
        vo.setPlanName(x.getPlanName());
        vo.setNursingFee(x.getNursingFee());
        vo.setStatus(x.getStatus());
        vo.setStatusLabel(x.getStatus() != null && x.getStatus() == 1 ? "启用" : "禁用");
        vo.setLevelDesc(x.getLevelDesc());
        vo.setCreatedTime(x.getCreatedTime() == null ? "-" : x.getCreatedTime().format(DT));
        boolean bound = elderCount > 0;
        vo.setCanEdit(!bound);
        vo.setCanDelete(!bound);
        vo.setCanDisable(!bound);
        return vo;
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
