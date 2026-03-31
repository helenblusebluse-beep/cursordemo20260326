package com.zhongzhou.modules.iot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@TableName("zz_iot_alarm_rule")
public class IotAlarmRule {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String ruleName;
    private Long productId;
    private String moduleName;
    private Long deviceId;
    private String functionName;
    /** elder/device */
    private String dataType;
    private String compareType;
    private BigDecimal thresholdValue;
    private Integer persistCycles;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    /** 分钟数，支持 5..1440 */
    private Integer muteCycleMinutes;
    private Integer enabled;
    private LocalDateTime createdTime;
    private Integer isDeleted;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public LocalTime getTimeStart() { return timeStart; }
    public void setTimeStart(LocalTime timeStart) { this.timeStart = timeStart; }
    public LocalTime getTimeEnd() { return timeEnd; }
    public void setTimeEnd(LocalTime timeEnd) { this.timeEnd = timeEnd; }
    public Integer getMuteCycleMinutes() { return muteCycleMinutes; }
    public void setMuteCycleMinutes(Integer muteCycleMinutes) { this.muteCycleMinutes = muteCycleMinutes; }
    public Integer getEnabled() { return enabled; }
    public void setEnabled(Integer enabled) { this.enabled = enabled; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
}
