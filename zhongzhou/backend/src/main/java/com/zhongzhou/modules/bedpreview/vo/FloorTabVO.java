package com.zhongzhou.modules.bedpreview.vo;

public class FloorTabVO {
    private Long id;
    private String floorName;
    private Integer sortOrder;
    private boolean hasRooms;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isHasRooms() {
        return hasRooms;
    }

    public void setHasRooms(boolean hasRooms) {
        this.hasRooms = hasRooms;
    }
}
