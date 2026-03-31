package com.zhongzhou.modules.bedpreview.vo;

import java.util.List;

public class FloorDetailVO {
    private Long floorId;
    private String floorName;
    private List<RoomCardVO> rooms;

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

    public List<RoomCardVO> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomCardVO> rooms) {
        this.rooms = rooms;
    }
}
