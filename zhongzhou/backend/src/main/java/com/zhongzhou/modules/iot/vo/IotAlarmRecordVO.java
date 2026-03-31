package com.zhongzhou.modules.iot.vo;

import java.math.BigDecimal;

public class IotAlarmRecordVO {
    private Long id;
    private Long ruleId;
    private String ruleName;
    private Long deviceId;
    private String deviceName;
    private String productName;
    private String dataTypeLabel;
    private String accessLocation;
    private String functionName;
    private BigDecimal metricValue;
    private String alarmRuleText;
    private String triggerTime;
    private String notifyObject;
    private String notifyTime;
    private String alarmContent;
    private String alarmTime;
    private Integer handleStatus;
    private String handleStatusLabel;
    private String handledTime;
    private String handleResult;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRuleId() { return ruleId; }
    public void setRuleId(Long ruleId) { this.ruleId = ruleId; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getDataTypeLabel() { return dataTypeLabel; }
    public void setDataTypeLabel(String dataTypeLabel) { this.dataTypeLabel = dataTypeLabel; }
    public String getAccessLocation() { return accessLocation; }
    public void setAccessLocation(String accessLocation) { this.accessLocation = accessLocation; }
    public String getFunctionName() { return functionName; }
    public void setFunctionName(String functionName) { this.functionName = functionName; }
    public BigDecimal getMetricValue() { return metricValue; }
    public void setMetricValue(BigDecimal metricValue) { this.metricValue = metricValue; }
    public String getAlarmRuleText() { return alarmRuleText; }
    public void setAlarmRuleText(String alarmRuleText) { this.alarmRuleText = alarmRuleText; }
    public String getTriggerTime() { return triggerTime; }
    public void setTriggerTime(String triggerTime) { this.triggerTime = triggerTime; }
    public String getNotifyObject() { return notifyObject; }
    public void setNotifyObject(String notifyObject) { this.notifyObject = notifyObject; }
    public String getNotifyTime() { return notifyTime; }
    public void setNotifyTime(String notifyTime) { this.notifyTime = notifyTime; }
    public String getAlarmContent() { return alarmContent; }
    public void setAlarmContent(String alarmContent) { this.alarmContent = alarmContent; }
    public String getAlarmTime() { return alarmTime; }
    public void setAlarmTime(String alarmTime) { this.alarmTime = alarmTime; }
    public Integer getHandleStatus() { return handleStatus; }
    public void setHandleStatus(Integer handleStatus) { this.handleStatus = handleStatus; }
    public String getHandleStatusLabel() { return handleStatusLabel; }
    public void setHandleStatusLabel(String handleStatusLabel) { this.handleStatusLabel = handleStatusLabel; }
    public String getHandledTime() { return handledTime; }
    public void setHandledTime(String handledTime) { this.handledTime = handledTime; }
    public String getHandleResult() { return handleResult; }
    public void setHandleResult(String handleResult) { this.handleResult = handleResult; }
}
