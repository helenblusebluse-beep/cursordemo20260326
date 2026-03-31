package com.zhongzhou.modules.nursing.vo;

public class NursingCareLevelItemVO {
    private Long id;
    private String levelName;
    private Long planId;
    private String planName;
    private Double nursingFee;
    private Integer status;
    private String statusLabel;
    private String levelDesc;
    private String createdTime;
    private boolean canEdit;
    private boolean canDelete;
    private boolean canDisable;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLevelName() { return levelName; }
    public void setLevelName(String levelName) { this.levelName = levelName; }
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public Double getNursingFee() { return nursingFee; }
    public void setNursingFee(Double nursingFee) { this.nursingFee = nursingFee; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getStatusLabel() { return statusLabel; }
    public void setStatusLabel(String statusLabel) { this.statusLabel = statusLabel; }
    public String getLevelDesc() { return levelDesc; }
    public void setLevelDesc(String levelDesc) { this.levelDesc = levelDesc; }
    public String getCreatedTime() { return createdTime; }
    public void setCreatedTime(String createdTime) { this.createdTime = createdTime; }
    public boolean isCanEdit() { return canEdit; }
    public void setCanEdit(boolean canEdit) { this.canEdit = canEdit; }
    public boolean isCanDelete() { return canDelete; }
    public void setCanDelete(boolean canDelete) { this.canDelete = canDelete; }
    public boolean isCanDisable() { return canDisable; }
    public void setCanDisable(boolean canDisable) { this.canDisable = canDisable; }
}
