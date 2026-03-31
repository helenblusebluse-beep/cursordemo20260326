package com.zhongzhou.modules.nursing.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NursingItemSaveRequest {
    @NotBlank(message = "名称不能为空")
    @Size(max = 10, message = "名称最多10个字符")
    private String itemName;

    @NotNull(message = "排序号不能为空")
    @Min(value = 1, message = "排序号须为正整数")
    @Max(value = 9999, message = "排序号超出范围")
    private Integer sortOrder;

    @Size(max = 5, message = "单位最多5个字符")
    private String unit;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.00", inclusive = true, message = "价格不能为负")
    private Double price;

    @NotBlank(message = "图片不能为空")
    private String imageName;

    private String imageUrl;

    @Size(max = 50, message = "护理要求最多50个字符")
    private String nursingRequirement;

    @NotNull(message = "状态不能为空")
    private Integer status;

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getNursingRequirement() { return nursingRequirement; }
    public void setNursingRequirement(String nursingRequirement) { this.nursingRequirement = nursingRequirement; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
