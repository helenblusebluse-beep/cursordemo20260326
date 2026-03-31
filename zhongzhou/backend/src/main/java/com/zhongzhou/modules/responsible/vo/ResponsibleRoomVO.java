package com.zhongzhou.modules.responsible.vo;

import java.util.List;

public class ResponsibleRoomVO {
    private Long roomId;
    private String roomNo;
    private List<ResponsibleBedVO> beds;

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
    public List<ResponsibleBedVO> getBeds() { return beds; }
    public void setBeds(List<ResponsibleBedVO> beds) { this.beds = beds; }
}
