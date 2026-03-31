package com.zhongzhou.modules.nursing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("zz_nursing_plan_item")
public class NursingPlanItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long planId;
    private Integer rowNo;
    private Long nursingItemId;
    private String nursingItemName;
    private String expectedServiceTime;
    private String executeCycle;
    private Integer executeFrequency;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public Integer getRowNo() { return rowNo; }
    public void setRowNo(Integer rowNo) { this.rowNo = rowNo; }
    public Long getNursingItemId() { return nursingItemId; }
    public void setNursingItemId(Long nursingItemId) { this.nursingItemId = nursingItemId; }
    public String getNursingItemName() { return nursingItemName; }
    public void setNursingItemName(String nursingItemName) { this.nursingItemName = nursingItemName; }
    public String getExpectedServiceTime() { return expectedServiceTime; }
    public void setExpectedServiceTime(String expectedServiceTime) { this.expectedServiceTime = expectedServiceTime; }
    public String getExecuteCycle() { return executeCycle; }
    public void setExecuteCycle(String executeCycle) { this.executeCycle = executeCycle; }
    public Integer getExecuteFrequency() { return executeFrequency; }
    public void setExecuteFrequency(Integer executeFrequency) { this.executeFrequency = executeFrequency; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }
}
