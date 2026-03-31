package com.zhongzhou.modules.taskschedule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("zz_task_execute_record")
public class TaskExecuteRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long taskId;
    private LocalDateTime executeTime;
    private String executeBy;
    private String executeImageName;
    private String executeImageUrl;
    private String executeRecord;
    private LocalDateTime createdTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
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
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
}
