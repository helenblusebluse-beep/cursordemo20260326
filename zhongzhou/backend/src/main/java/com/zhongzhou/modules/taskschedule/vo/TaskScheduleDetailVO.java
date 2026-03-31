package com.zhongzhou.modules.taskschedule.vo;

public class TaskScheduleDetailVO extends TaskScheduleItemVO {
    private String elderGender;
    private Integer elderAge;
    private String careLevelName;
    private String caregiverNames;
    private String elderAvatarUrl;
    private String orderNo;
    private String createdBy;
    private String remark;
    private String cancelReason;
    private String executeTime;
    private String executeImageName;
    private String executeImageUrl;
    private String executeRecord;

    public String getElderGender() { return elderGender; }
    public void setElderGender(String elderGender) { this.elderGender = elderGender; }
    public Integer getElderAge() { return elderAge; }
    public void setElderAge(Integer elderAge) { this.elderAge = elderAge; }
    public String getCareLevelName() { return careLevelName; }
    public void setCareLevelName(String careLevelName) { this.careLevelName = careLevelName; }
    public String getCaregiverNames() { return caregiverNames; }
    public void setCaregiverNames(String caregiverNames) { this.caregiverNames = caregiverNames; }
    public String getElderAvatarUrl() { return elderAvatarUrl; }
    public void setElderAvatarUrl(String elderAvatarUrl) { this.elderAvatarUrl = elderAvatarUrl; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }
    public String getExecuteTime() { return executeTime; }
    public void setExecuteTime(String executeTime) { this.executeTime = executeTime; }
    public String getExecuteImageName() { return executeImageName; }
    public void setExecuteImageName(String executeImageName) { this.executeImageName = executeImageName; }
    public String getExecuteImageUrl() { return executeImageUrl; }
    public void setExecuteImageUrl(String executeImageUrl) { this.executeImageUrl = executeImageUrl; }
    public String getExecuteRecord() { return executeRecord; }
    public void setExecuteRecord(String executeRecord) { this.executeRecord = executeRecord; }
}
