package com.zhongzhou.modules.nursing.vo;

public class NursingItemVO {
    private Long id;
    private String itemName;
    private Integer sortOrder;
    private String unit;
    private Double price;
    private String imageName;
    private String imageUrl;
    private String nursingRequirement;
    private Integer status;
    private String statusLabel;
    private Integer bindPlanCount;
    private String createdTime;
    private boolean canEdit;
    private boolean canDelete;
    private boolean canDisable;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public String getStatusLabel() { return statusLabel; }
    public void setStatusLabel(String statusLabel) { this.statusLabel = statusLabel; }
    public Integer getBindPlanCount() { return bindPlanCount; }
    public void setBindPlanCount(Integer bindPlanCount) { this.bindPlanCount = bindPlanCount; }
    public String getCreatedTime() { return createdTime; }
    public void setCreatedTime(String createdTime) { this.createdTime = createdTime; }
    public boolean isCanEdit() { return canEdit; }
    public void setCanEdit(boolean canEdit) { this.canEdit = canEdit; }
    public boolean isCanDelete() { return canDelete; }
    public void setCanDelete(boolean canDelete) { this.canDelete = canDelete; }
    public boolean isCanDisable() { return canDisable; }
    public void setCanDisable(boolean canDisable) { this.canDisable = canDisable; }
}
