package com.zhongzhou.modules.smartbed.vo;

public class SmartBedFloorTabVO {
    private Long id;
    private String floorName;
    private Integer sortOrder;
    /** 楼层内任一房间或床位数据异常 */
    private boolean hasAnomaly;

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

    public boolean isHasAnomaly() {
        return hasAnomaly;
    }

    public void setHasAnomaly(boolean hasAnomaly) {
        this.hasAnomaly = hasAnomaly;
    }
}
