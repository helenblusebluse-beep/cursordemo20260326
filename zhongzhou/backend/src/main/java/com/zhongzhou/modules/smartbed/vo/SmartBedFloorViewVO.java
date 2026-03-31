package com.zhongzhou.modules.smartbed.vo;

import java.util.ArrayList;
import java.util.List;

public class SmartBedFloorViewVO {
    private Long floorId;
    private String floorName;
    private List<SmartRoomVO> rooms = new ArrayList<>();

    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public List<SmartRoomVO> getRooms() {
        return rooms;
    }

    public void setRooms(List<SmartRoomVO> rooms) {
        this.rooms = rooms;
    }
}
