package com.zhongzhou.modules.responsible.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhongzhou.modules.bedpreview.entity.PreviewBed;
import com.zhongzhou.modules.bedpreview.entity.PreviewFloor;
import com.zhongzhou.modules.bedpreview.entity.PreviewRoom;
import com.zhongzhou.modules.bedpreview.mapper.PreviewBedMapper;
import com.zhongzhou.modules.bedpreview.mapper.PreviewFloorMapper;
import com.zhongzhou.modules.bedpreview.mapper.PreviewRoomMapper;
import com.zhongzhou.modules.responsible.dto.CaregiverAssignRequest;
import com.zhongzhou.modules.responsible.entity.BedCaregiverAssign;
import com.zhongzhou.modules.responsible.entity.CaregiverStaff;
import com.zhongzhou.modules.responsible.mapper.BedCaregiverAssignMapper;
import com.zhongzhou.modules.responsible.mapper.CaregiverStaffMapper;
import com.zhongzhou.modules.responsible.vo.ResponsibleBedVO;
import com.zhongzhou.modules.responsible.vo.ResponsibleFloorTabVO;
import com.zhongzhou.modules.responsible.vo.ResponsibleFloorViewVO;
import com.zhongzhou.modules.responsible.vo.ResponsibleRoomVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ResponsibleElderService {
    private final PreviewFloorMapper floorMapper;
    private final PreviewRoomMapper roomMapper;
    private final PreviewBedMapper bedMapper;
    private final CaregiverStaffMapper staffMapper;
    private final BedCaregiverAssignMapper assignMapper;

    public ResponsibleElderService(PreviewFloorMapper floorMapper, PreviewRoomMapper roomMapper, PreviewBedMapper bedMapper, CaregiverStaffMapper staffMapper, BedCaregiverAssignMapper assignMapper) {
        this.floorMapper = floorMapper;
        this.roomMapper = roomMapper;
        this.bedMapper = bedMapper;
        this.staffMapper = staffMapper;
        this.assignMapper = assignMapper;
    }

    public List<ResponsibleFloorTabVO> listFloors() {
        return floorMapper.selectList(new LambdaQueryWrapper<PreviewFloor>()
                        .eq(PreviewFloor::getIsDeleted, 0)
                        .orderByAsc(PreviewFloor::getSortOrder)
                        .orderByAsc(PreviewFloor::getId))
                .stream()
                .map(f -> {
                    ResponsibleFloorTabVO vo = new ResponsibleFloorTabVO();
                    vo.setId(f.getId());
                    vo.setFloorName(f.getFloorName());
                    vo.setSortOrder(f.getSortOrder());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    public ResponsibleFloorViewVO getFloorView(Long floorId) {
        PreviewFloor floor = floorMapper.selectById(floorId);
        if (floor == null || (floor.getIsDeleted() != null && floor.getIsDeleted() == 1)) {
            throw new IllegalArgumentException("楼层不存在");
        }
        List<PreviewRoom> rooms = roomMapper.selectList(new LambdaQueryWrapper<PreviewRoom>()
                .eq(PreviewRoom::getFloorId, floorId)
                .eq(PreviewRoom::getIsDeleted, 0)
                .orderByAsc(PreviewRoom::getSortOrder)
                .orderByAsc(PreviewRoom::getId));
        List<Long> roomIds = rooms.stream().map(PreviewRoom::getId).collect(Collectors.toList());
        List<PreviewBed> beds = bedMapper.selectList(new LambdaQueryWrapper<PreviewBed>()
                .in(!roomIds.isEmpty(), PreviewBed::getRoomId, roomIds)
                .eq(PreviewBed::getIsDeleted, 0)
                .orderByAsc(PreviewBed::getSortOrder)
                .orderByAsc(PreviewBed::getId));
        Map<Long, List<PreviewBed>> bedGroup = beds.stream().collect(Collectors.groupingBy(PreviewBed::getRoomId));
        Map<Long, List<String>> caregiverMap = listCaregiverMap(beds.stream().map(PreviewBed::getId).collect(Collectors.toList()));

        ResponsibleFloorViewVO vo = new ResponsibleFloorViewVO();
        vo.setFloorId(floor.getId());
        vo.setFloorName(floor.getFloorName());
        vo.setRooms(rooms.stream().map(r -> {
            ResponsibleRoomVO rv = new ResponsibleRoomVO();
            rv.setRoomId(r.getId());
            rv.setRoomNo(r.getRoomNo());
            List<PreviewBed> rb = bedGroup.getOrDefault(r.getId(), new ArrayList<>());
            rv.setBeds(rb.stream().map(b -> {
                ResponsibleBedVO bv = new ResponsibleBedVO();
                bv.setBedId(b.getId());
                bv.setBedName(b.getBedName());
                bv.setElderName(b.getElderName() == null ? "" : b.getElderName());
                bv.setCaregiverNames(caregiverMap.getOrDefault(b.getId(), new ArrayList<>()));
                return bv;
            }).collect(Collectors.toList()));
            return rv;
        }).collect(Collectors.toList()));
        return vo;
    }

    public List<String> listCaregiverOptions() {
        return staffMapper.selectList(new LambdaQueryWrapper<CaregiverStaff>()
                        .eq(CaregiverStaff::getIsDeleted, 0)
                        .eq(CaregiverStaff::getStatus, 1)
                        .orderByAsc(CaregiverStaff::getId))
                .stream()
                .map(CaregiverStaff::getStaffName)
                .collect(Collectors.toList());
    }

    @Transactional
    public void assignBed(Long bedId, CaregiverAssignRequest req) {
        PreviewBed bed = bedMapper.selectById(bedId);
        if (bed == null || (bed.getIsDeleted() != null && bed.getIsDeleted() == 1)) {
            throw new IllegalArgumentException("床位不存在");
        }
        List<String> names = normalizeNames(req.getCaregiverNames());
        validateNames(names);
        rewriteBedAssign(bedId, names);
    }

    @Transactional
    public void assignRoom(Long roomId, CaregiverAssignRequest req) {
        PreviewRoom room = roomMapper.selectById(roomId);
        if (room == null || (room.getIsDeleted() != null && room.getIsDeleted() == 1)) {
            throw new IllegalArgumentException("房间不存在");
        }
        List<String> names = normalizeNames(req.getCaregiverNames());
        validateNames(names);
        List<PreviewBed> beds = bedMapper.selectList(new LambdaQueryWrapper<PreviewBed>()
                .eq(PreviewBed::getRoomId, roomId)
                .eq(PreviewBed::getIsDeleted, 0));
        for (PreviewBed b : beds) {
            rewriteBedAssign(b.getId(), names);
        }
    }

    public List<String> getBedCaregivers(Long bedId) {
        return listCaregiverMap(List.of(bedId)).getOrDefault(bedId, new ArrayList<>());
    }

    public List<String> getRoomCaregivers(Long roomId) {
        List<PreviewBed> beds = bedMapper.selectList(new LambdaQueryWrapper<PreviewBed>()
                .eq(PreviewBed::getRoomId, roomId)
                .eq(PreviewBed::getIsDeleted, 0)
                .orderByAsc(PreviewBed::getSortOrder));
        if (beds.isEmpty()) return new ArrayList<>();
        return getBedCaregivers(beds.get(0).getId());
    }

    private void rewriteBedAssign(Long bedId, List<String> names) {
        List<BedCaregiverAssign> oldRows = assignMapper.selectList(new LambdaQueryWrapper<BedCaregiverAssign>()
                .eq(BedCaregiverAssign::getBedId, bedId)
                .eq(BedCaregiverAssign::getIsDeleted, 0));
        for (BedCaregiverAssign r : oldRows) {
            r.setIsDeleted(1);
            assignMapper.updateById(r);
        }
        for (String n : names) {
            BedCaregiverAssign row = new BedCaregiverAssign();
            row.setBedId(bedId);
            row.setCaregiverName(n);
            row.setIsDeleted(0);
            assignMapper.insert(row);
        }
    }

    private Map<Long, List<String>> listCaregiverMap(List<Long> bedIds) {
        if (bedIds == null || bedIds.isEmpty()) return Map.of();
        return assignMapper.selectList(new LambdaQueryWrapper<BedCaregiverAssign>()
                        .in(BedCaregiverAssign::getBedId, bedIds)
                        .eq(BedCaregiverAssign::getIsDeleted, 0))
                .stream()
                .collect(Collectors.groupingBy(BedCaregiverAssign::getBedId,
                        Collectors.mapping(BedCaregiverAssign::getCaregiverName, Collectors.toList())));
    }

    private List<String> normalizeNames(List<String> names) {
        if (names == null) return new ArrayList<>();
        return names.stream().filter(s -> s != null && !s.trim().isEmpty()).map(String::trim).distinct().collect(Collectors.toList());
    }

    private void validateNames(List<String> names) {
        if (names.isEmpty()) {
            throw new IllegalArgumentException("护理员姓名不能为空");
        }
        if (names.size() > 4) {
            throw new IllegalArgumentException("最多选择4个护理员");
        }
        Set<String> valid = new HashSet<>(listCaregiverOptions());
        for (String n : names) {
            if (!valid.contains(n)) {
                throw new IllegalArgumentException("护理员不存在：" + n);
            }
        }
    }
}
