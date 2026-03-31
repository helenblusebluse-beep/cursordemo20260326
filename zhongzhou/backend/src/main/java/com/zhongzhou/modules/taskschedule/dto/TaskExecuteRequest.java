package com.zhongzhou.modules.taskschedule.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TaskExecuteRequest {
    @NotBlank(message = "执行时间不能为空")
    private String executeTime;
    @NotBlank(message = "执行图片不能为空")
    private String executeImageName;
    @NotBlank(message = "执行图片不能为空")
    private String executeImageUrl;
    @NotBlank(message = "执行人不能为空")
    private String executeBy;
    @NotNull(message = "执行记录不能为空")
    @Size(max = 100, message = "执行记录最多100个字符")
    private String executeRecord;

    public String getExecuteTime() { return executeTime; }
    public void setExecuteTime(String executeTime) { this.executeTime = executeTime; }
    public String getExecuteImageName() { return executeImageName; }
    public void setExecuteImageName(String executeImageName) { this.executeImageName = executeImageName; }
    public String getExecuteImageUrl() { return executeImageUrl; }
    public void setExecuteImageUrl(String executeImageUrl) { this.executeImageUrl = executeImageUrl; }
    public String getExecuteBy() { return executeBy; }
    public void setExecuteBy(String executeBy) { this.executeBy = executeBy; }
    public String getExecuteRecord() { return executeRecord; }
    public void setExecuteRecord(String executeRecord) { this.executeRecord = executeRecord; }
}
