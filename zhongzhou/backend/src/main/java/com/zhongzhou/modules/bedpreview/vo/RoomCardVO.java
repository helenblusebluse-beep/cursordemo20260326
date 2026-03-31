package com.zhongzhou.modules.bedpreview.vo;

import java.util.List;

public class RoomCardVO {
    private Long id;
    private String roomNo;
    private String roomTypeName;
    private Long roomTypeId;
    private Integer sortOrder;
    private boolean canDeleteRoom;
    private List<BedItemVO> beds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isCanDeleteRoom() {
        return canDeleteRoom;
    }

    public void setCanDeleteRoom(boolean canDeleteRoom) {
        this.canDeleteRoom = canDeleteRoom;
    }

    public List<BedItemVO> getBeds() {
        return beds;
    }

    public void setBeds(List<BedItemVO> beds) {
        this.beds = beds;
    }
}
