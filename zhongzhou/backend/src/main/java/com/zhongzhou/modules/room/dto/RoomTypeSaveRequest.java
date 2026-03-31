package com.zhongzhou.modules.room.dto;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class RoomTypeSaveRequest {
    @NotBlank(message = "房间名称不能为空")
    @Size(max = 10, message = "房间名称最多10个字符")
    private String roomName;

    @NotNull(message = "床位数量不能为空")
    @Min(value = 1, message = "床位数量须为正整数")
    @Max(value = 9999, message = "床位数量超出范围")
    private Integer bedCount;

    @NotNull(message = "床位费用不能为空")
    @DecimalMin(value = "0.0", inclusive = true, message = "床位费用不能为负")
    private BigDecimal bedFee;

    @NotBlank(message = "房型介绍不能为空")
    @Size(max = 50, message = "房型介绍最多50个字符")
    private String introduction;

    @NotNull(message = "房间图片不能为空")
    @Size(min = 1, max = 50, message = "房间图片须1～50张")
    private List<String> imageUrls;

    @Size(max = 200, message = "备注过长")
    private String remarks;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getBedCount() {
        return bedCount;
    }

    public void setBedCount(Integer bedCount) {
        this.bedCount = bedCount;
    }

    public BigDecimal getBedFee() {
        return bedFee;
    }

    public void setBedFee(BigDecimal bedFee) {
        this.bedFee = bedFee;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
