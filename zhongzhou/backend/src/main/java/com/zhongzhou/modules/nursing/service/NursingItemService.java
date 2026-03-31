package com.zhongzhou.modules.nursing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.nursing.dto.NursingItemSaveRequest;
import com.zhongzhou.modules.nursing.entity.NursingItem;
import com.zhongzhou.modules.nursing.mapper.NursingItemMapper;
import com.zhongzhou.modules.nursing.vo.NursingItemOptionVO;
import com.zhongzhou.modules.nursing.vo.NursingItemVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class NursingItemService {
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private final NursingItemMapper mapper;

    public NursingItemService(NursingItemMapper mapper) {
        this.mapper = mapper;
    }

    public PageResult<NursingItemVO> page(String name, Integer status, int page, int pageSize) {
        LambdaQueryWrapper<NursingItem> wrapper = new LambdaQueryWrapper<NursingItem>()
                .eq(NursingItem::getIsDeleted, 0)
                .orderByAsc(NursingItem::getSortOrder)
                .orderByAsc(NursingItem::getId);
        List<NursingItemVO> allRows = mapper.selectList(wrapper).stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        if (StringUtils.hasText(name)) {
            String kw = name.trim();
            allRows = allRows.stream().filter(x -> x.getItemName() != null && x.getItemName().contains(kw)).collect(Collectors.toList());
        }
        if (status != null) {
            allRows = allRows.stream().filter(x -> status.equals(x.getStatus())).collect(Collectors.toList());
        }
        int from = Math.max(0, (page - 1) * pageSize);
        int to = Math.min(allRows.size(), from + pageSize);
        List<NursingItemVO> rows = from >= allRows.size() ? new ArrayList<>() : allRows.subList(from, to);
        return new PageResult<>(allRows.size(), rows);
    }

    public List<NursingItemOptionVO> listEnabledOptions() {
        return mapper.selectList(new LambdaQueryWrapper<NursingItem>()
                        .eq(NursingItem::getIsDeleted, 0)
                        .eq(NursingItem::getStatus, 1)
                        .orderByAsc(NursingItem::getSortOrder)
                        .orderByAsc(NursingItem::getId))
                .stream()
                .map(e -> {
                    NursingItemOptionVO vo = new NursingItemOptionVO();
                    vo.setId(e.getId());
                    vo.setItemName(e.getItemName());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    public Long create(NursingItemSaveRequest req) {
        validateCommon(req, null);
        NursingItem e = new NursingItem();
        fillEntity(e, req);
        e.setBindPlanCount(0);
        e.setIsDeleted(0);
        e.setCreatedTime(LocalDateTime.now());
        e.setUpdatedTime(LocalDateTime.now());
        mapper.insert(e);
        return e.getId();
    }

    public void update(Long id, NursingItemSaveRequest req) {
        NursingItem e = load(id);
        if (isBound(e)) {
            throw new IllegalArgumentException("该护理项目已绑定护理计划，不能修改");
        }
        validateCommon(req, id);
        fillEntity(e, req);
        e.setUpdatedTime(LocalDateTime.now());
        mapper.updateById(e);
    }

    public void remove(Long id) {
        NursingItem e = load(id);
        if (isBound(e)) {
            throw new IllegalArgumentException("该护理项目已绑定护理计划，不能删除");
        }
        e.setIsDeleted(1);
        e.setUpdatedTime(LocalDateTime.now());
        mapper.updateById(e);
    }

    public void changeStatus(Long id, Integer status) {
        if (status == null || (status != 1 && status != 2)) {
            throw new IllegalArgumentException("状态值非法");
        }
        NursingItem e = load(id);
        if (status == 2 && isBound(e)) {
            throw new IllegalArgumentException("该护理项目已绑定护理计划，不能禁用");
        }
        e.setStatus(status);
        e.setUpdatedTime(LocalDateTime.now());
        mapper.updateById(e);
    }

    private NursingItem load(Long id) {
        NursingItem e = mapper.selectById(id);
        if (e == null || (e.getIsDeleted() != null && e.getIsDeleted() == 1)) {
            throw new IllegalArgumentException("护理项目不存在");
        }
        return e;
    }

    private void fillEntity(NursingItem e, NursingItemSaveRequest req) {
        e.setItemName(req.getItemName().trim());
        e.setSortOrder(req.getSortOrder());
        e.setUnit(StringUtils.hasText(req.getUnit()) ? req.getUnit().trim() : "");
        e.setPrice(req.getPrice());
        e.setImageName(req.getImageName().trim());
        e.setImageUrl(req.getImageUrl());
        e.setNursingRequirement(StringUtils.hasText(req.getNursingRequirement()) ? req.getNursingRequirement().trim() : "");
        e.setStatus(req.getStatus());
    }

    private void validateCommon(NursingItemSaveRequest req, Long excludeId) {
        validateNoEmoji(req.getItemName(), "名称");
        if (StringUtils.hasText(req.getUnit())) {
            validateNoEmoji(req.getUnit(), "单位");
        }
        if (StringUtils.hasText(req.getNursingRequirement())) {
            validateNoEmoji(req.getNursingRequirement(), "护理要求");
        }
        validateImageName(req.getImageName());
        LambdaQueryWrapper<NursingItem> wrapper = new LambdaQueryWrapper<NursingItem>()
                .eq(NursingItem::getIsDeleted, 0)
                .eq(NursingItem::getItemName, req.getItemName().trim());
        if (excludeId != null) {
            wrapper.ne(NursingItem::getId, excludeId);
        }
        if (mapper.selectCount(wrapper) > 0) {
            throw new IllegalArgumentException("名称已存在");
        }
    }

    private void validateImageName(String imageName) {
        String lower = imageName == null ? "" : imageName.trim().toLowerCase(Locale.ROOT);
        if (!(lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg"))) {
            throw new IllegalArgumentException("仅支持上传PNG/JPG/JPEG类型图片");
        }
    }

    private boolean isBound(NursingItem e) {
        return e.getBindPlanCount() != null && e.getBindPlanCount() > 0;
    }

    private NursingItemVO toVO(NursingItem e) {
        NursingItemVO vo = new NursingItemVO();
        vo.setId(e.getId());
        vo.setItemName(e.getItemName());
        vo.setSortOrder(e.getSortOrder());
        vo.setUnit(e.getUnit());
        vo.setPrice(e.getPrice());
        vo.setImageName(e.getImageName());
        vo.setImageUrl(e.getImageUrl());
        vo.setNursingRequirement(e.getNursingRequirement());
        vo.setStatus(e.getStatus());
        vo.setStatusLabel(e.getStatus() != null && e.getStatus() == 1 ? "启用" : "禁用");
        vo.setBindPlanCount(e.getBindPlanCount() == null ? 0 : e.getBindPlanCount());
        vo.setCreatedTime(e.getCreatedTime() == null ? "-" : e.getCreatedTime().format(DT));
        boolean bound = isBound(e);
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
