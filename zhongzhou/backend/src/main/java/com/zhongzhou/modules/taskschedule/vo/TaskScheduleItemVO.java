package com.zhongzhou.modules.taskschedule.vo;

public class TaskScheduleItemVO {
    private Long id;
    private String elderName;
    private String bedNo;
    private String nursingItemName;
    private String taskType;
    private String caregiverName;
    private String expectedServiceTime;
    private String createdTime;
    private String executeBy;
    private String executeTime;
    private String cancelBy;
    private String cancelTime;
    private Integer status;
    private String statusLabel;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getElderName() { return elderName; }
    public void setElderName(String elderName) { this.elderName = elderName; }
    public String getBedNo() { return bedNo; }
    public void setBedNo(String bedNo) { this.bedNo = bedNo; }
    public String getNursingItemName() { return nursingItemName; }
    public void setNursingItemName(String nursingItemName) { this.nursingItemName = nursingItemName; }
    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }
    public String getCaregiverName() { return caregiverName; }
    public void setCaregiverName(String caregiverName) { this.caregiverName = caregiverName; }
    public String getExpectedServiceTime() { return expectedServiceTime; }
    public void setExpectedServiceTime(String expectedServiceTime) { this.expectedServiceTime = expectedServiceTime; }
    public String getCreatedTime() { return createdTime; }
    public void setCreatedTime(String createdTime) { this.createdTime = createdTime; }
    public String getExecuteBy() { return executeBy; }
    public void setExecuteBy(String executeBy) { this.executeBy = executeBy; }
    public String getExecuteTime() { return executeTime; }
    public void setExecuteTime(String executeTime) { this.executeTime = executeTime; }
    public String getCancelBy() { return cancelBy; }
    public void setCancelBy(String cancelBy) { this.cancelBy = cancelBy; }
    public String getCancelTime() { return cancelTime; }
    public void setCancelTime(String cancelTime) { this.cancelTime = cancelTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getStatusLabel() { return statusLabel; }
    public void setStatusLabel(String statusLabel) { this.statusLabel = statusLabel; }
}
