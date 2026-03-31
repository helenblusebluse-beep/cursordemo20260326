package com.zhongzhou.modules.room.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.room.dto.RoomTypeSaveRequest;
import com.zhongzhou.modules.room.entity.Room;
import com.zhongzhou.modules.room.entity.RoomType;
import com.zhongzhou.modules.room.mapper.RoomMapper;
import com.zhongzhou.modules.room.mapper.RoomTypeMapper;
import com.zhongzhou.modules.room.vo.RoomTypeOptionVO;
import com.zhongzhou.modules.room.vo.RoomTypeVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomTypeService {
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RoomTypeMapper roomTypeMapper;
    private final RoomMapper roomMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RoomTypeService(RoomTypeMapper roomTypeMapper, RoomMapper roomMapper) {
        this.roomTypeMapper = roomTypeMapper;
        this.roomMapper = roomMapper;
    }

    public List<RoomTypeOptionVO> listEnabledOptions() {
        LambdaQueryWrapper<RoomType> w = new LambdaQueryWrapper<>();
        w.eq(RoomType::getIsDeleted, 0).eq(RoomType::getStatus, 1).orderByAsc(RoomType::getId);
        return roomTypeMapper.selectList(w).stream().map(e -> {
            RoomTypeOptionVO o = new RoomTypeOptionVO();
            o.setId(e.getId());
            o.setRoomName(e.getRoomName());
            return o;
        }).collect(Collectors.toList());
    }

    public PageResult<RoomTypeVO> page(int page, int pageSize) {
        LambdaQueryWrapper<RoomType> w = new LambdaQueryWrapper<>();
        w.eq(RoomType::getIsDeleted, 0).orderByDesc(RoomType::getCreatedTime);
        Page<RoomType> p = roomTypeMapper.selectPage(new Page<>(page, pageSize), w);
        List<RoomTypeVO> rows = p.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return new PageResult<>(p.getTotal(), rows);
    }

    public RoomTypeVO detail(Long id) {
        RoomType e = roomTypeMapper.selectById(id);
        if (e == null || e.getIsDeleted() != null && e.getIsDeleted() != 0) {
            throw new IllegalArgumentException("房型不存在");
        }
        return toVO(e);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long create(RoomTypeSaveRequest req) {
        validateRequest(req);
        String name = req.getRoomName().trim();
        ensureUniqueName(name, null);
        RoomType e = new RoomType();
        e.setRoomName(name);
        e.setBedCount(req.getBedCount());
        e.setBedFee(normalizeFee(req.getBedFee()));
        e.setIntroduction(req.getIntroduction().trim());
        e.setImageUrls(writeJson(req.getImageUrls()));
        e.setRemarks(trimToNull(req.getRemarks()));
        e.setStatus(1);
        e.setIsDeleted(0);
        roomTypeMapper.insert(e);
        return e.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, RoomTypeSaveRequest req) {
        RoomType e = roomTypeMapper.selectById(id);
        if (e == null || e.getIsDeleted() != null && e.getIsDeleted() != 0) {
            throw new IllegalArgumentException("房型不存在");
        }
        if (countRooms(id) > 0) {
            throw new IllegalArgumentException("该房型下已创建房间，无法修改");
        }
        validateRequest(req);
        String name = req.getRoomName().trim();
        ensureUniqueName(name, id);
        e.setRoomName(name);
        e.setBedCount(req.getBedCount());
        e.setBedFee(normalizeFee(req.getBedFee()));
        e.setIntroduction(req.getIntroduction().trim());
        e.setImageUrls(writeJson(req.getImageUrls()));
        e.setRemarks(trimToNull(req.getRemarks()));
        roomTypeMapper.updateById(e);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        RoomType e = roomTypeMapper.selectById(id);
        if (e == null || e.getIsDeleted() != null && e.getIsDeleted() != 0) {
            throw new IllegalArgumentException("房型不存在");
        }
        if (countRooms(id) > 0) {
            throw new IllegalArgumentException("该房型下已创建房间，无法删除");
        }
        e.setIsDeleted(1);
        roomTypeMapper.updateById(e);
    }

    @Transactional(rollbackFor = Exception.class)
    public void toggleStatus(Long id, boolean enable) {
        RoomType e = roomTypeMapper.selectById(id);
        if (e == null || e.getIsDeleted() != null && e.getIsDeleted() != 0) {
            throw new IllegalArgumentException("房型不存在");
        }
        if (countRooms(id) > 0) {
            throw new IllegalArgumentException("该房型下已创建房间，无法变更状态");
        }
        e.setStatus(enable ? 1 : 2);
        roomTypeMapper.updateById(e);
    }

    private long countRooms(Long roomTypeId) {
        return roomMapper.selectCount(new LambdaQueryWrapper<Room>()
                .eq(Room::getRoomTypeId, roomTypeId)
                .eq(Room::getIsDeleted, 0));
    }

    private void ensureUniqueName(String name, Long excludeId) {
        LambdaQueryWrapper<RoomType> w = new LambdaQueryWrapper<>();
        w.eq(RoomType::getIsDeleted, 0).eq(RoomType::getRoomName, name);
        if (excludeId != null) {
            w.ne(RoomType::getId, excludeId);
        }
        if (roomTypeMapper.selectCount(w) > 0) {
            throw new IllegalArgumentException("房间名称已存在");
        }
    }

    private void validateRequest(RoomTypeSaveRequest req) {
        validateNoEmoji(req.getRoomName().trim(), "房间名称");
        validateNoEmoji(req.getIntroduction().trim(), "房型介绍");
        for (String url : req.getImageUrls()) {
            if (url == null || url.trim().isEmpty()) {
                throw new IllegalArgumentException("图片地址不能为空");
            }
            validateNoEmoji(url.trim(), "图片地址");
        }
    }

    private void validateNoEmoji(String s, String label) {
        if (s == null || s.isEmpty()) {
            return;
        }
        for (int i = 0; i < s.length(); ) {
            int cp = s.codePointAt(i);
            i += Character.charCount(cp);
            if (isEmojiCodePoint(cp)) {
                throw new IllegalArgumentException(label + "不能包含表情符号");
            }
        }
    }

    /** 常见表情/符号区段（JDK11 正则不支持 Extended_Pictographic 属性名） */
    private static boolean isEmojiCodePoint(int cp) {
        return (cp >= 0x1F300 && cp <= 0x1FAFF)
                || (cp >= 0x2600 && cp <= 0x27BF)
                || (cp >= 0xFE00 && cp <= 0xFE0F)
                || cp == 0x200D
                || cp == 0x20E3;
    }

    private String trimToNull(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private BigDecimal normalizeFee(BigDecimal fee) {
        return fee.setScale(2, RoundingMode.HALF_UP);
    }

    private String writeJson(List<String> urls) {
        try {
            return objectMapper.writeValueAsString(urls);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("图片数据无效");
        }
    }

    private List<String> readJson(String json) {
        if (json == null || json.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {
            });
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }

    private RoomTypeVO toVO(RoomType e) {
        RoomTypeVO vo = new RoomTypeVO();
        vo.setId(e.getId());
        vo.setRoomName(e.getRoomName());
        vo.setBedCount(e.getBedCount());
        vo.setBedFee(e.getBedFee());
        vo.setIntroduction(e.getIntroduction());
        vo.setImageUrlsJson(e.getImageUrls());
        vo.setRemarks(e.getRemarks());
        List<String> urls = readJson(e.getImageUrls());
        vo.setThumbUrl(urls.isEmpty() ? null : urls.get(0));
        vo.setCreatedTime(e.getCreatedTime() == null ? null : e.getCreatedTime().format(DT));
        vo.setStatus(e.getStatus());
        vo.setAssociatedRoomCount(countRooms(e.getId()));
        return vo;
    }
}
