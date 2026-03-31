package com.zhongzhou.modules.iot.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class IotAlarmRuleSaveRequest {
    @NotBlank
    @Size(max = 64)
    private String ruleName;
    @NotNull
    private Long productId;
    @NotBlank
    @Size(max = 64)
    private String moduleName;
    private Long deviceId;
    @NotBlank
    @Size(max = 64)
    private String functionName;
    @NotBlank
    private String dataType;
    @NotBlank
    @Size(max = 8)
    private String compareType;
    @NotNull
    private BigDecimal thresholdValue;
    @NotNull
    @Min(1)
    private Integer persistCycles;
    @NotBlank
    private String timeStart;
    @NotBlank
    private String timeEnd;
    @NotNull
    @Min(1)
    private Integer muteCycleMinutes;
    @NotNull
    private Integer enabled;

    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    public String getFunctionName() { return functionName; }
    public void setFunctionName(String functionName) { this.functionName = functionName; }
    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
    public String getCompareType() { return compareType; }
    public void setCompareType(String compareType) { this.compareType = compareType; }
    public BigDecimal getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(BigDecimal thresholdValue) { this.thresholdValue = thresholdValue; }
    public Integer getPersistCycles() { return persistCycles; }
    public void setPersistCycles(Integer persistCycles) { this.persistCycles = persistCycles; }
    public String getTimeStart() { return timeStart; }
    public void setTimeStart(String timeStart) { this.timeStart = timeStart; }
    public String getTimeEnd() { return timeEnd; }
    public void setTimeEnd(String timeEnd) { this.timeEnd = timeEnd; }
    public Integer getMuteCycleMinutes() { return muteCycleMinutes; }
    public void setMuteCycleMinutes(Integer muteCycleMinutes) { this.muteCycleMinutes = muteCycleMinutes; }
    public Integer getEnabled() { return enabled; }
    public void setEnabled(Integer enabled) { this.enabled = enabled; }
}
