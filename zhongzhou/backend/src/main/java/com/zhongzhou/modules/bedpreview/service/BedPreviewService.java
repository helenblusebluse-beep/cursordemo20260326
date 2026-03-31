package com.zhongzhou.modules.bedpreview.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhongzhou.modules.bedpreview.dto.BedSaveRequest;
import com.zhongzhou.modules.bedpreview.dto.FloorSaveRequest;
import com.zhongzhou.modules.bedpreview.dto.PreviewRoomSaveRequest;
import com.zhongzhou.modules.bedpreview.entity.PreviewBed;
import com.zhongzhou.modules.bedpreview.entity.PreviewFloor;
import com.zhongzhou.modules.bedpreview.entity.PreviewRoom;
import com.zhongzhou.modules.bedpreview.mapper.PreviewBedMapper;
import com.zhongzhou.modules.bedpreview.mapper.PreviewFloorMapper;
import com.zhongzhou.modules.bedpreview.mapper.PreviewRoomMapper;
import com.zhongzhou.modules.bedpreview.vo.BedItemVO;
import com.zhongzhou.modules.bedpreview.vo.FloorDetailVO;
import com.zhongzhou.modules.bedpreview.vo.FloorTabVO;
import com.zhongzhou.modules.bedpreview.vo.RoomCardVO;
import com.zhongzhou.modules.room.entity.RoomType;
import com.zhongzhou.modules.room.mapper.RoomTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BedPreviewService {
    private final PreviewFloorMapper floorMapper;
    private final PreviewRoomMapper roomMapper;
    private final PreviewBedMapper bedMapper;
    private final RoomTypeMapper roomTypeMapper;

    public BedPreviewService(PreviewFloorMapper floorMapper, PreviewRoomMapper roomMapper,
                             PreviewBedMapper bedMapper, RoomTypeMapper roomTypeMapper) {
        this.floorMapper = floorMapper;
        this.roomMapper = roomMapper;
        this.bedMapper = bedMapper;
        this.roomTypeMapper = roomTypeMapper;
    }

    public List<FloorTabVO> listFloors() {
        List<PreviewFloor> list = floorMapper.selectList(new LambdaQueryWrapper<PreviewFloor>()
                .eq(PreviewFloor::getIsDeleted, 0)
                .orderByAsc(PreviewFloor::getSortOrder)
                .orderByAsc(PreviewFloor::getId));
        return list.stream().map(f -> {
            FloorTabVO vo = new FloorTabVO();
            vo.setId(f.getId());
            vo.setFloorName(f.getFloorName());
            vo.setSortOrder(f.getSortOrder());
            vo.setHasRooms(countRoomsOnFloor(f.getId()) > 0);
            return vo;
        }).collect(Collectors.toList());
    }

    public FloorDetailVO getFloorDetail(Long floorId) {
        PreviewFloor f = floorMapper.selectById(floorId);
        if (f == null || f.getIsDeleted() != null && f.getIsDeleted() != 0) {
            throw new IllegalArgumentException("楼层不存在");
        }
        FloorDetailVO vo = new FloorDetailVO();
        vo.setFloorId(f.getId());
        vo.setFloorName(f.getFloorName());
        List<PreviewRoom> rooms = roomMapper.selectList(new LambdaQueryWrapper<PreviewRoom>()
                .eq(PreviewRoom::getFloorId, floorId)
                .eq(PreviewRoom::getIsDeleted, 0)
                .orderByAsc(PreviewRoom::getSortOrder)
                .orderByAsc(PreviewRoom::getId));
        vo.setRooms(rooms.stream().map(this::toRoomCard).collect(Collectors.toList()));
        return vo;
    }

    private RoomCardVO toRoomCard(PreviewRoom r) {
        RoomCardVO vo = new RoomCardVO();
        vo.setId(r.getId());
        vo.setRoomNo(r.getRoomNo());
        vo.setRoomTypeId(r.getRoomTypeId());
        vo.setSortOrder(r.getSortOrder());
        RoomType rt = roomTypeMapper.selectById(r.getRoomTypeId());
        vo.setRoomTypeName(rt == null ? "" : rt.getRoomName());
        long bc = countBedsInRoom(r.getId());
        vo.setCanDeleteRoom(bc == 0);
        List<PreviewBed> beds = bedMapper.selectList(new LambdaQueryWrapper<PreviewBed>()
                .eq(PreviewBed::getRoomId, r.getId())
                .eq(PreviewBed::getIsDeleted, 0)
                .orderByAsc(PreviewBed::getSortOrder)
                .orderByAsc(PreviewBed::getId));
        vo.setBeds(beds.stream().map(this::toBedItem).collect(Collectors.toList()));
        return vo;
    }

    private BedItemVO toBedItem(PreviewBed b) {
        BedItemVO vo = new BedItemVO();
        vo.setId(b.getId());
        vo.setBedName(b.getBedName());
        vo.setSortOrder(b.getSortOrder());
        vo.setStatus(b.getStatus());
        vo.setElderName(b.getElderName());
        vo.setCanDelete(canDeleteBedEntity(b));
        return vo;
    }

    private boolean canDeleteBedEntity(PreviewBed b) {
        if (b.getStatus() == null || b.getStatus() != 1) {
            return false;
        }
        String n = b.getElderName();
        return n == null || n.trim().isEmpty();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createFloor(FloorSaveRequest req) {
        String name = req.getFloorName().trim();
        validateFloorOrRoomName(name, "楼层名称");
        ensureFloorNameUnique(name, null);
        PreviewFloor e = new PreviewFloor();
        e.setFloorName(name);
        e.setSortOrder(req.getSortOrder());
        e.setIsDeleted(0);
        floorMapper.insert(e);
        return e.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateFloor(Long id, FloorSaveRequest req) {
        PreviewFloor e = loadFloor(id);
        String name = req.getFloorName().trim();
        validateFloorOrRoomName(name, "楼层名称");
        ensureFloorNameUnique(name, id);
        e.setFloorName(name);
        e.setSortOrder(req.getSortOrder());
        floorMapper.updateById(e);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteFloor(Long id) {
        PreviewFloor e = loadFloor(id);
        if (countRoomsOnFloor(id) > 0) {
            throw new IllegalArgumentException("该楼层下已有房间，无法删除");
        }
        e.setIsDeleted(1);
        floorMapper.updateById(e);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createRoom(Long floorId, PreviewRoomSaveRequest req) {
        loadFloor(floorId);
        String roomNo = req.getRoomNo().trim();
        validateFloorOrRoomName(roomNo, "房间号");
        ensureRoomTypeEnabled(req.getRoomTypeId());
        ensureRoomNoUnique(floorId, roomNo, null);
        PreviewRoom r = new PreviewRoom();
        r.setFloorId(floorId);
        r.setRoomNo(roomNo);
        r.setRoomTypeId(req.getRoomTypeId());
        r.setSortOrder(req.getSortOrder());
        r.setIsDeleted(0);
        roomMapper.insert(r);
        return r.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRoom(Long roomId, PreviewRoomSaveRequest req) {
        PreviewRoom r = loadRoom(roomId);
        String roomNo = req.getRoomNo().trim();
        validateFloorOrRoomName(roomNo, "房间号");
        ensureRoomTypeEnabled(req.getRoomTypeId());
        ensureRoomNoUnique(r.getFloorId(), roomNo, roomId);
        r.setRoomNo(roomNo);
        r.setRoomTypeId(req.getRoomTypeId());
        r.setSortOrder(req.getSortOrder());
        roomMapper.updateById(r);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRoom(Long roomId) {
        PreviewRoom r = loadRoom(roomId);
        if (countBedsInRoom(roomId) > 0) {
            throw new IllegalArgumentException("该房间下已有床位，无法删除");
        }
        r.setIsDeleted(1);
        roomMapper.updateById(r);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createBed(Long roomId, BedSaveRequest req) {
        loadRoom(roomId);
        String bedName = req.getBedName().trim();
        validateBedName(bedName);
        ensureBedNameUnique(roomId, bedName, null);
        PreviewBed b = new PreviewBed();
        b.setRoomId(roomId);
        b.setBedName(bedName);
        b.setSortOrder(req.getSortOrder());
        b.setStatus(1);
        b.setElderName(null);
        b.setIsDeleted(0);
        bedMapper.insert(b);
        return b.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateBed(Long bedId, BedSaveRequest req) {
        PreviewBed b = loadBed(bedId);
        String bedName = req.getBedName().trim();
        validateBedName(bedName);
        ensureBedNameUnique(b.getRoomId(), bedName, bedId);
        b.setBedName(bedName);
        b.setSortOrder(req.getSortOrder());
        bedMapper.updateById(b);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBed(Long bedId) {
        PreviewBed b = loadBed(bedId);
        if (!canDeleteBedEntity(b)) {
            throw new IllegalArgumentException("该床位已绑定老人，无法删除");
        }
        b.setIsDeleted(1);
        bedMapper.updateById(b);
    }

    private PreviewFloor loadFloor(Long id) {
        PreviewFloor e = floorMapper.selectById(id);
        if (e == null || e.getIsDeleted() != null && e.getIsDeleted() != 0) {
            throw new IllegalArgumentException("楼层不存在");
        }
        return e;
    }

    private PreviewRoom loadRoom(Long id) {
        PreviewRoom r = roomMapper.selectById(id);
        if (r == null || r.getIsDeleted() != null && r.getIsDeleted() != 0) {
            throw new IllegalArgumentException("房间不存在");
        }
        return r;
    }

    private PreviewBed loadBed(Long id) {
        PreviewBed b = bedMapper.selectById(id);
        if (b == null || b.getIsDeleted() != null && b.getIsDeleted() != 0) {
            throw new IllegalArgumentException("床位不存在");
        }
        return b;
    }

    private long countRoomsOnFloor(Long floorId) {
        return roomMapper.selectCount(new LambdaQueryWrapper<PreviewRoom>()
                .eq(PreviewRoom::getFloorId, floorId)
                .eq(PreviewRoom::getIsDeleted, 0));
    }

    private long countBedsInRoom(Long roomId) {
        return bedMapper.selectCount(new LambdaQueryWrapper<PreviewBed>()
                .eq(PreviewBed::getRoomId, roomId)
                .eq(PreviewBed::getIsDeleted, 0));
    }

    private void ensureFloorNameUnique(String name, Long excludeId) {
        LambdaQueryWrapper<PreviewFloor> w = new LambdaQueryWrapper<PreviewFloor>()
                .eq(PreviewFloor::getIsDeleted, 0)
                .eq(PreviewFloor::getFloorName, name);
        if (excludeId != null) {
            w.ne(PreviewFloor::getId, excludeId);
        }
        if (floorMapper.selectCount(w) > 0) {
            throw new IllegalArgumentException("楼层名称已存在");
        }
    }

    private void ensureRoomNoUnique(Long floorId, String roomNo, Long excludeRoomId) {
        LambdaQueryWrapper<PreviewRoom> w = new LambdaQueryWrapper<PreviewRoom>()
                .eq(PreviewRoom::getIsDeleted, 0)
                .eq(PreviewRoom::getFloorId, floorId)
                .eq(PreviewRoom::getRoomNo, roomNo);
        if (excludeRoomId != null) {
            w.ne(PreviewRoom::getId, excludeRoomId);
        }
        if (roomMapper.selectCount(w) > 0) {
            throw new IllegalArgumentException("该楼层下房间号已存在");
        }
    }

    private void ensureBedNameUnique(Long roomId, String bedName, Long excludeBedId) {
        LambdaQueryWrapper<PreviewBed> w = new LambdaQueryWrapper<PreviewBed>()
                .eq(PreviewBed::getIsDeleted, 0)
                .eq(PreviewBed::getRoomId, roomId)
                .eq(PreviewBed::getBedName, bedName);
        if (excludeBedId != null) {
            w.ne(PreviewBed::getId, excludeBedId);
        }
        if (bedMapper.selectCount(w) > 0) {
            throw new IllegalArgumentException("该房间下床位名称已存在");
        }
    }

    private void ensureRoomTypeEnabled(Long roomTypeId) {
        RoomType rt = roomTypeMapper.selectById(roomTypeId);
        if (rt == null || rt.getIsDeleted() != null && rt.getIsDeleted() != 0) {
            throw new IllegalArgumentException("房型不存在");
        }
        if (rt.getStatus() == null || rt.getStatus() != 1) {
            throw new IllegalArgumentException("只能选择启用状态的房型");
        }
    }

    private void validateFloorOrRoomName(String s, String label) {
        if (s.isEmpty()) {
            throw new IllegalArgumentException(label + "不能为空");
        }
        validateNoEmoji(s, label);
    }

    private void validateBedName(String s) {
        if (s.isEmpty()) {
            throw new IllegalArgumentException("床位名称不能为空");
        }
        validateNoEmoji(s, "床位名称");
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
