package com.zhongzhou.modules.nursing.vo;

public class NursingPlanItemVO {
    private Integer rowNo;
    private Long nursingItemId;
    private String nursingItemName;
    private String expectedServiceTime;
    private String executeCycle;
    private Integer executeFrequency;

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
}
