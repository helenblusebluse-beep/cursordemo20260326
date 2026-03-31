package com.zhongzhou.modules.bedpreview.dto;

import javax.validation.constraints.*;

public class FloorSaveRequest {
    @NotBlank(message = "楼层名称不能为空")
    @Size(max = 5, message = "楼层名称最多5个字符")
    private String floorName;

    @NotNull(message = "排序不能为空")
    private Integer sortOrder;

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
}
