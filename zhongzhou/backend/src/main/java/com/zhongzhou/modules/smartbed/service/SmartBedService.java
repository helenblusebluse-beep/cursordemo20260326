package com.zhongzhou.modules.smartbed.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhongzhou.modules.bedpreview.entity.PreviewBed;
import com.zhongzhou.modules.bedpreview.entity.PreviewFloor;
import com.zhongzhou.modules.bedpreview.entity.PreviewRoom;
import com.zhongzhou.modules.bedpreview.mapper.PreviewBedMapper;
import com.zhongzhou.modules.bedpreview.mapper.PreviewFloorMapper;
import com.zhongzhou.modules.bedpreview.mapper.PreviewRoomMapper;
import com.zhongzhou.modules.smartbed.entity.BedMonitor;
import com.zhongzhou.modules.smartbed.entity.RoomEnv;
import com.zhongzhou.modules.smartbed.entity.SmartDevice;
import com.zhongzhou.modules.smartbed.mapper.BedMonitorMapper;
import com.zhongzhou.modules.smartbed.mapper.RoomEnvMapper;
import com.zhongzhou.modules.smartbed.mapper.SmartDeviceMapper;
import com.zhongzhou.modules.smartbed.vo.SmartBedCardVO;
import com.zhongzhou.modules.smartbed.vo.SmartBedFloorTabVO;
import com.zhongzhou.modules.smartbed.vo.SmartBedFloorViewVO;
import com.zhongzhou.modules.smartbed.vo.SmartDeviceItemVO;
import com.zhongzhou.modules.smartbed.vo.SmartRoomVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SmartBedService {
    private static final String BIND_ROOM = "ROOM";
    private static final String BIND_BED = "BED";

    private final PreviewFloorMapper floorMapper;
    private final PreviewRoomMapper roomMapper;
    private final PreviewBedMapper bedMapper;
    private final SmartDeviceMapper smartDeviceMapper;
    private final RoomEnvMapper roomEnvMapper;
    private final BedMonitorMapper bedMonitorMapper;

    public SmartBedService(PreviewFloorMapper floorMapper, PreviewRoomMapper roomMapper,
                           PreviewBedMapper bedMapper, SmartDeviceMapper smartDeviceMapper,
                           RoomEnvMapper roomEnvMapper, BedMonitorMapper bedMonitorMapper) {
        this.floorMapper = floorMapper;
        this.roomMapper = roomMapper;
        this.bedMapper = bedMapper;
        this.smartDeviceMapper = smartDeviceMapper;
        this.roomEnvMapper = roomEnvMapper;
        this.bedMonitorMapper = bedMonitorMapper;
    }

    public List<SmartBedFloorTabVO> listFloors() {
        List<PreviewFloor> floors = floorMapper.selectList(new LambdaQueryWrapper<PreviewFloor>()
                .eq(PreviewFloor::getIsDeleted, 0)
                .orderByAsc(PreviewFloor::getSortOrder)
                .orderByAsc(PreviewFloor::getId));
        List<SmartBedFloorTabVO> out = new ArrayList<>();
        for (PreviewFloor f : floors) {
            if (!floorHasAnyBoundDevice(f.getId())) {
                continue;
            }
            SmartBedFloorTabVO t = new SmartBedFloorTabVO();
            t.setId(f.getId());
            t.setFloorName(f.getFloorName());
            t.setSortOrder(f.getSortOrder());
            t.setHasAnomaly(computeFloorAnomaly(f.getId()));
            out.add(t);
        }
        return out;
    }

    public SmartBedFloorViewVO getFloorView(Long floorId) {
        PreviewFloor f = loadFloor(floorId);
        SmartBedFloorViewVO vo = new SmartBedFloorViewVO();
        vo.setFloorId(f.getId());
        vo.setFloorName(f.getFloorName());
        List<PreviewRoom> rooms = roomMapper.selectList(new LambdaQueryWrapper<PreviewRoom>()
                .eq(PreviewRoom::getFloorId, floorId)
                .eq(PreviewRoom::getIsDeleted, 0)
                .orderByAsc(PreviewRoom::getSortOrder)
                .orderByAsc(PreviewRoom::getId));
        List<SmartRoomVO> list = new ArrayList<>();
        for (PreviewRoom r : rooms) {
            if (!isRoomVisible(r.getId())) {
                continue;
            }
            list.add(buildRoom(r));
        }
        vo.setRooms(list);
        return vo;
    }

    private PreviewFloor loadFloor(Long id) {
        PreviewFloor f = floorMapper.selectById(id);
        if (f == null || f.getIsDeleted() != null && f.getIsDeleted() != 0) {
            throw new IllegalArgumentException("楼层不存在");
        }
        return f;
    }

    private boolean floorHasAnyBoundDevice(Long floorId) {
        List<PreviewRoom> rooms = roomMapper.selectList(new LambdaQueryWrapper<PreviewRoom>()
                .eq(PreviewRoom::getFloorId, floorId)
                .eq(PreviewRoom::getIsDeleted, 0));
        for (PreviewRoom r : rooms) {
            if (hasRoomDevices(r.getId())) {
                return true;
            }
            for (PreviewBed b : listBedsInRoom(r.getId())) {
                if (hasBedDevice(b.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean computeFloorAnomaly(Long floorId) {
        List<PreviewRoom> rooms = roomMapper.selectList(new LambdaQueryWrapper<PreviewRoom>()
                .eq(PreviewRoom::getFloorId, floorId)
                .eq(PreviewRoom::getIsDeleted, 0));
        for (PreviewRoom r : rooms) {
            RoomEnv env = roomEnvMapper.selectById(r.getId());
            if (env != null && env.getIsDeleted() != null && env.getIsDeleted() == 0
                    && env.getDataAnomaly() != null && env.getDataAnomaly() == 1) {
                return true;
            }
            for (PreviewBed b : listBedsInRoom(r.getId())) {
                if (!hasBedDevice(b.getId())) {
                    continue;
                }
                BedMonitor m = bedMonitorMapper.selectById(b.getId());
                if (m != null && m.getIsDeleted() != null && m.getIsDeleted() == 0
                        && m.getCheckedOut() != null && m.getCheckedOut() == 0
                        && m.getDataAnomaly() != null && m.getDataAnomaly() == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isRoomVisible(Long roomId) {
        return hasRoomDevices(roomId) || hasAnyBedWithDeviceInRoom(roomId);
    }

    private boolean hasAnyBedWithDeviceInRoom(Long roomId) {
        for (PreviewBed b : listBedsInRoom(roomId)) {
            if (hasBedDevice(b.getId())) {
                return true;
            }
        }
        return false;
    }

    private List<PreviewBed> listBedsInRoom(Long roomId) {
        return bedMapper.selectList(new LambdaQueryWrapper<PreviewBed>()
                .eq(PreviewBed::getRoomId, roomId)
                .eq(PreviewBed::getIsDeleted, 0)
                .orderByAsc(PreviewBed::getSortOrder)
                .orderByAsc(PreviewBed::getId));
    }

    private boolean hasRoomDevices(Long roomId) {
        return smartDeviceMapper.selectCount(new LambdaQueryWrapper<SmartDevice>()
                .eq(SmartDevice::getRoomId, roomId)
                .eq(SmartDevice::getBindType, BIND_ROOM)
                .eq(SmartDevice::getIsDeleted, 0)) > 0;
    }

    private boolean hasBedDevice(Long bedId) {
        return smartDeviceMapper.selectCount(new LambdaQueryWrapper<SmartDevice>()
                .eq(SmartDevice::getBedId, bedId)
                .eq(SmartDevice::getBindType, BIND_BED)
                .eq(SmartDevice::getIsDeleted, 0)) > 0;
    }

    private SmartRoomVO buildRoom(PreviewRoom r) {
        SmartRoomVO vo = new SmartRoomVO();
        vo.setRoomId(r.getId());
        vo.setRoomNo(r.getRoomNo());
        vo.setRoomDevices(listRoomDevices(r.getId()));
        RoomEnv env = roomEnvMapper.selectById(r.getId());
        vo.setDoorStatus(formatDoor(env));
        vo.setTemperature(formatTemperature(env));
        vo.setHumidity(formatHumidity(env));
        vo.setAlarmStatus(formatAlarm(env));

        List<PreviewBed> beds = listBedsInRoom(r.getId());
        boolean anyBedWithDevice = false;
        List<SmartBedCardVO> cards = new ArrayList<>();
        for (PreviewBed b : beds) {
            if (!hasBedDevice(b.getId())) {
                continue;
            }
            anyBedWithDevice = true;
            SmartBedCardVO c = buildBedCard(b);
            if (c != null) {
                cards.add(c);
            }
        }
        boolean hasRoomDev = hasRoomDevices(r.getId());
        if (hasRoomDev && !anyBedWithDevice) {
            vo.setRoomMessage("当前房间没有安排床位或床位没有绑定设备");
        }
        vo.setBeds(cards);
        return vo;
    }

    private SmartBedCardVO buildBedCard(PreviewBed bed) {
        if (!hasBedDevice(bed.getId())) {
            return null;
        }
        BedMonitor m = bedMonitorMapper.selectById(bed.getId());
        if (m != null && m.getIsDeleted() != null && m.getIsDeleted() == 0
                && m.getCheckedOut() != null && m.getCheckedOut() == 1) {
            return null;
        }
        if (m == null || m.getIsDeleted() != null && m.getIsDeleted() != 0) {
            m = new BedMonitor();
            m.setPresence("NONE");
            m.setNoTslData(0);
            m.setAlarm(0);
            m.setCheckedOut(0);
        }

        String elder = resolveElderName(bed, m);
        boolean hasResident = elder != null && !elder.isBlank();
        boolean noTsl = m.getNoTslData() != null && m.getNoTslData() == 1;
        boolean alarm = m.getAlarm() != null && m.getAlarm() == 1;

        SmartBedCardVO c = new SmartBedCardVO();
        c.setBedId(bed.getId());
        c.setBedName(bed.getBedName());
        c.setDevices(listBedDevices(bed.getId()));
        c.setAlarm(alarm);

        if (!hasResident) {
            c.setElderName("-");
            c.setMessage("当前床位没有安排老人");
        } else if (noTsl) {
            c.setElderName(elder);
            c.setMessage("当前床位设备无数据");
        } else {
            c.setElderName(elder);
        }

        if (c.getMessage() != null) {
            c.setPresenceLabel(null);
            c.setShowHeartBreath(false);
            c.setShowLeftBed(false);
            return c;
        }

        String pres = m.getPresence() == null ? "NONE" : m.getPresence();
        switch (pres) {
            case "LEFT":
                c.setPresenceLabel("已离床");
                c.setShowLeftBed(true);
                c.setShowHeartBreath(false);
                c.setLeftBedCount(m.getLeftBedCount());
                c.setLeftBedTime(m.getLeftBedTime() == null ? "-" : m.getLeftBedTime());
                break;
            case "AWAKE":
                c.setPresenceLabel("清醒中");
                c.setShowHeartBreath(true);
                c.setHeartRate(m.getHeartRate());
                c.setBreathRate(m.getBreathRate());
                break;
            case "SLEEP":
                c.setPresenceLabel("睡眠中");
                c.setShowHeartBreath(true);
                c.setHeartRate(m.getHeartRate());
                c.setBreathRate(m.getBreathRate());
                break;
            default:
                c.setPresenceLabel(null);
                c.setShowHeartBreath(false);
                c.setShowLeftBed(false);
                if (!hasResident) {
                    c.setElderName("-");
                    c.setMessage("当前床位没有安排老人");
                } else {
                    c.setElderName(elder);
                    c.setMessage("当前床位设备无数据");
                }
                break;
        }
        if (c.getMessage() != null) {
            c.setPresenceLabel(null);
            c.setShowHeartBreath(false);
            c.setShowLeftBed(false);
        }
        return c;
    }

    private String resolveElderName(PreviewBed bed, BedMonitor m) {
        if (m.getElderName() != null && !m.getElderName().isBlank()) {
            return m.getElderName().trim();
        }
        if (bed.getElderName() != null && !bed.getElderName().isBlank()) {
            return bed.getElderName().trim();
        }
        return null;
    }

    private List<SmartDeviceItemVO> listRoomDevices(Long roomId) {
        List<SmartDevice> list = smartDeviceMapper.selectList(new LambdaQueryWrapper<SmartDevice>()
                .eq(SmartDevice::getRoomId, roomId)
                .eq(SmartDevice::getBindType, BIND_ROOM)
                .eq(SmartDevice::getIsDeleted, 0)
                .orderByAsc(SmartDevice::getSortOrder)
                .orderByAsc(SmartDevice::getId));
        return list.stream().map(this::toItem).collect(Collectors.toList());
    }

    private List<SmartDeviceItemVO> listBedDevices(Long bedId) {
        List<SmartDevice> list = smartDeviceMapper.selectList(new LambdaQueryWrapper<SmartDevice>()
                .eq(SmartDevice::getBedId, bedId)
                .eq(SmartDevice::getBindType, BIND_BED)
                .eq(SmartDevice::getIsDeleted, 0)
                .orderByAsc(SmartDevice::getSortOrder)
                .orderByAsc(SmartDevice::getId));
        return list.stream().map(this::toItem).collect(Collectors.toList());
    }

    private SmartDeviceItemVO toItem(SmartDevice d) {
        SmartDeviceItemVO v = new SmartDeviceItemVO();
        v.setProductCategory(d.getProductCategory());
        v.setDeviceName(d.getDeviceName());
        v.setIconType(d.getIconType());
        return v;
    }

    private String formatDoor(RoomEnv env) {
        if (env == null || env.getIsDeleted() != null && env.getIsDeleted() != 0) {
            return "-";
        }
        if (env.getDoorOpen() == null) {
            return "-";
        }
        return env.getDoorOpen() == 1 ? "开启" : "关闭";
    }

    private String formatTemperature(RoomEnv env) {
        if (env == null || env.getIsDeleted() != null && env.getIsDeleted() != 0) {
            return "-";
        }
        BigDecimal t = env.getTemperatureC();
        if (t == null) {
            return "-";
        }
        return t.stripTrailingZeros().toPlainString() + "°C";
    }

    private String formatHumidity(RoomEnv env) {
        if (env == null || env.getIsDeleted() != null && env.getIsDeleted() != 0) {
            return "-";
        }
        if (env.getHumidityPct() == null) {
            return "-";
        }
        return env.getHumidityPct() + "%";
    }

    private String formatAlarm(RoomEnv env) {
        if (env == null || env.getIsDeleted() != null && env.getIsDeleted() != 0) {
            return "-";
        }
        if (env.getAlarmNormal() == null) {
            return "-";
        }
        return env.getAlarmNormal() == 1 ? "正常" : "异常";
    }
}
