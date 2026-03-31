package com.zhongzhou.modules.iot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/** 物模型-属性上报历史（查看数据） */
@TableName("zz_iot_device_tsl_property_history")
public class IotDeviceTslPropertyHistory {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long deviceId;
    private Long moduleId;
    private String propIdentifier;
    private String rawValue;
    private LocalDateTime reportTime;
    private Integer isDeleted;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    public Long getModuleId() { return moduleId; }
    public void setModuleId(Long moduleId) { this.moduleId = moduleId; }
    public String getPropIdentifier() { return propIdentifier; }
    public void setPropIdentifier(String propIdentifier) { this.propIdentifier = propIdentifier; }
    public String getRawValue() { return rawValue; }
    public void setRawValue(String rawValue) { this.rawValue = rawValue; }
    public LocalDateTime getReportTime() { return reportTime; }
    public void setReportTime(LocalDateTime reportTime) { this.reportTime = reportTime; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
}
