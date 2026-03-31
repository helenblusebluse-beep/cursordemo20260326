package com.zhongzhou.modules.iot.vo;

import java.math.BigDecimal;

public class IotAlarmRuleVO {
    private Long id;
    private String ruleName;
    private Long productId;
    private String productName;
    private String moduleName;
    private Long deviceId;
    /** 关联设备展示：设备名或「全部设备」 */
    private String deviceDisplay;
    private String functionName;
    private String dataType;
    private String dataTypeLabel;
    private String compareType;
    private BigDecimal thresholdValue;
    private Integer persistCycles;
    private String timeStart;
    private String timeEnd;
    private Integer muteCycleMinutes;
    private String muteCycleLabel;
    /** 报警规则列：功能名称+运算符+阈值，持续x个周期就报警 */
    private String alarmRuleText;
    /** 报警生效时段 HH:mm:ss-HH:mm:ss */
    private String effectivePeriod;
    private Integer enabled;
    private String enabledLabel;
    private String createdTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    public String getDeviceDisplay() { return deviceDisplay; }
    public void setDeviceDisplay(String deviceDisplay) { this.deviceDisplay = deviceDisplay; }
    public String getFunctionName() { return functionName; }
    public void setFunctionName(String functionName) { this.functionName = functionName; }
    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
    public String getDataTypeLabel() { return dataTypeLabel; }
    public void setDataTypeLabel(String dataTypeLabel) { this.dataTypeLabel = dataTypeLabel; }
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
    public String getMuteCycleLabel() { return muteCycleLabel; }
    public void setMuteCycleLabel(String muteCycleLabel) { this.muteCycleLabel = muteCycleLabel; }
    public String getAlarmRuleText() { return alarmRuleText; }
    public void setAlarmRuleText(String alarmRuleText) { this.alarmRuleText = alarmRuleText; }
    public String getEffectivePeriod() { return effectivePeriod; }
    public void setEffectivePeriod(String effectivePeriod) { this.effectivePeriod = effectivePeriod; }
    public Integer getEnabled() { return enabled; }
    public void setEnabled(Integer enabled) { this.enabled = enabled; }
    public String getEnabledLabel() { return enabledLabel; }
    public void setEnabledLabel(String enabledLabel) { this.enabledLabel = enabledLabel; }
    public String getCreatedTime() { return createdTime; }
    public void setCreatedTime(String createdTime) { this.createdTime = createdTime; }
}
