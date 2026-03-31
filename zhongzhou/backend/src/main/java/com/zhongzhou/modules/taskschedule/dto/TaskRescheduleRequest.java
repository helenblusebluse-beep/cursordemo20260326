package com.zhongzhou.modules.taskschedule.dto;

import javax.validation.constraints.NotBlank;

public class TaskRescheduleRequest {
    @NotBlank(message = "期望服务时间不能为空")
    private String expectedServiceTime;

    public String getExpectedServiceTime() { return expectedServiceTime; }
    public void setExpectedServiceTime(String expectedServiceTime) { this.expectedServiceTime = expectedServiceTime; }
}
