package com.zhongzhou.modules.nursing.vo;

import java.util.List;

public class NursingPlanVO {
    private Long id;
    private String planName;
    private Integer sortOrder;
    private Integer status;
    private String statusLabel;
    private String createdTime;
    private Integer bindLevelCount;
    private boolean canEdit;
    private boolean canDelete;
    private boolean canDisable;
    private List<NursingPlanItemVO> items;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getStatusLabel() { return statusLabel; }
    public void setStatusLabel(String statusLabel) { this.statusLabel = statusLabel; }
    public String getCreatedTime() { return createdTime; }
    public void setCreatedTime(String createdTime) { this.createdTime = createdTime; }
    public Integer getBindLevelCount() { return bindLevelCount; }
    public void setBindLevelCount(Integer bindLevelCount) { this.bindLevelCount = bindLevelCount; }
    public boolean isCanEdit() { return canEdit; }
    public void setCanEdit(boolean canEdit) { this.canEdit = canEdit; }
    public boolean isCanDelete() { return canDelete; }
    public void setCanDelete(boolean canDelete) { this.canDelete = canDelete; }
    public boolean isCanDisable() { return canDisable; }
    public void setCanDisable(boolean canDisable) { this.canDisable = canDisable; }
    public List<NursingPlanItemVO> getItems() { return items; }
    public void setItems(List<NursingPlanItemVO> items) { this.items = items; }
}
