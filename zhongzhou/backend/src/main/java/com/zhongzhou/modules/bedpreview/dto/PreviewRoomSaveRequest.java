package com.zhongzhou.modules.bedpreview.dto;

import javax.validation.constraints.*;

public class PreviewRoomSaveRequest {
    @NotBlank(message = "房间号不能为空")
    @Size(max = 5, message = "房间号最多5个字符")
    private String roomNo;

    @NotNull(message = "房间类型不能为空")
    @Min(1)
    private Long roomTypeId;

    @NotNull(message = "排序不能为空")
    private Integer sortOrder;

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
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
}
