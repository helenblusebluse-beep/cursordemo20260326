package com.zhongzhou.modules.responsible.vo;

import java.util.List;

public class ResponsibleFloorViewVO {
    private Long floorId;
    private String floorName;
    private List<ResponsibleRoomVO> rooms;

    public Long getFloorId() { return floorId; }
    public void setFloorId(Long floorId) { this.floorId = floorId; }
    public String getFloorName() { return floorName; }
    public void setFloorName(String floorName) { this.floorName = floorName; }
    public List<ResponsibleRoomVO> getRooms() { return rooms; }
    public void setRooms(List<ResponsibleRoomVO> rooms) { this.rooms = rooms; }
}
