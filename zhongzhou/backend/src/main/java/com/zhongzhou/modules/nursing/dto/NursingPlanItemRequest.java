package com.zhongzhou.modules.nursing.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class NursingPlanItemRequest {
    private Long nursingItemId;
    private String expectedServiceTime;
    private String executeCycle;
    @Min(value = 1, message = "执行频次必须为1-7的整数")
    @Max(value = 7, message = "执行频次必须为1-7的整数")
    private Integer executeFrequency;

    public Long getNursingItemId() { return nursingItemId; }
    public void setNursingItemId(Long nursingItemId) { this.nursingItemId = nursingItemId; }
    public String getExpectedServiceTime() { return expectedServiceTime; }
    public void setExpectedServiceTime(String expectedServiceTime) { this.expectedServiceTime = expectedServiceTime; }
    public String getExecuteCycle() { return executeCycle; }
    public void setExecuteCycle(String executeCycle) { this.executeCycle = executeCycle; }
    public Integer getExecuteFrequency() { return executeFrequency; }
    public void setExecuteFrequency(Integer executeFrequency) { this.executeFrequency = executeFrequency; }
}
