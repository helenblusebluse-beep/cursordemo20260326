package com.zhongzhou.modules.taskschedule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("zz_task_schedule")
public class TaskSchedule {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String elderName;
    private String elderGender;
    private Integer elderAge;
    private String bedNo;
    private String careLevelName;
    private String caregiverNames;
    private String elderAvatarUrl;
    private String nursingItemName;
    private String orderNo;
    private String taskType;
    private String caregiverName;
    private LocalDateTime expectedServiceTime;
    private LocalDateTime createdTime;
    private String createdBy;
    private String remark;
    private Integer status;
    private String cancelReason;
    private String cancelBy;
    private LocalDateTime cancelTime;
    private LocalDateTime executeTime;
    private String executeBy;
    private String executeImageName;
    private String executeImageUrl;
    private String executeRecord;
    private Integer isDeleted;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getElderName() { return elderName; }
    public void setElderName(String elderName) { this.elderName = elderName; }
    public String getElderGender() { return elderGender; }
    public void setElderGender(String elderGender) { this.elderGender = elderGender; }
    public Integer getElderAge() { return elderAge; }
    public void setElderAge(Integer elderAge) { this.elderAge = elderAge; }
    public String getBedNo() { return bedNo; }
    public void setBedNo(String bedNo) { this.bedNo = bedNo; }
    public String getCareLevelName() { return careLevelName; }
    public void setCareLevelName(String careLevelName) { this.careLevelName = careLevelName; }
    public String getCaregiverNames() { return caregiverNames; }
    public void setCaregiverNames(String caregiverNames) { this.caregiverNames = caregiverNames; }
    public String getElderAvatarUrl() { return elderAvatarUrl; }
    public void setElderAvatarUrl(String elderAvatarUrl) { this.elderAvatarUrl = elderAvatarUrl; }
    public String getNursingItemName() { return nursingItemName; }
    public void setNursingItemName(String nursingItemName) { this.nursingItemName = nursingItemName; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }
    public String getCaregiverName() { return caregiverName; }
    public void setCaregiverName(String caregiverName) { this.caregiverName = caregiverName; }
    public LocalDateTime getExpectedServiceTime() { return expectedServiceTime; }
    public void setExpectedServiceTime(LocalDateTime expectedServiceTime) { this.expectedServiceTime = expectedServiceTime; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }
    public String getCancelBy() { return cancelBy; }
    public void setCancelBy(String cancelBy) { this.cancelBy = cancelBy; }
    public LocalDateTime getCancelTime() { return cancelTime; }
    public void setCancelTime(LocalDateTime cancelTime) { this.cancelTime = cancelTime; }
    public LocalDateTime getExecuteTime() { return executeTime; }
    public void setExecuteTime(LocalDateTime executeTime) { this.executeTime = executeTime; }
    public String getExecuteBy() { return executeBy; }
    public void setExecuteBy(String executeBy) { this.executeBy = executeBy; }
    public String getExecuteImageName() { return executeImageName; }
    public void setExecuteImageName(String executeImageName) { this.executeImageName = executeImageName; }
    public String getExecuteImageUrl() { return executeImageUrl; }
    public void setExecuteImageUrl(String executeImageUrl) { this.executeImageUrl = executeImageUrl; }
    public String getExecuteRecord() { return executeRecord; }
    public void setExecuteRecord(String executeRecord) { this.executeRecord = executeRecord; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
}
