package com.zhongzhou.modules.iot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/** 物模型-服务调用（下行指令） */
@TableName("zz_iot_device_tsl_service_invocation")
public class IotDeviceTslServiceInvocation {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long deviceId;
    private Long moduleId;
    private String serviceIdentifier;
    private String serviceName;
    private String inputParams;
    private String outputParams;
    private LocalDateTime invokeTime;
    private Integer isDeleted;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    public Long getModuleId() { return moduleId; }
    public void setModuleId(Long moduleId) { this.moduleId = moduleId; }
    public String getServiceIdentifier() { return serviceIdentifier; }
    public void setServiceIdentifier(String serviceIdentifier) { this.serviceIdentifier = serviceIdentifier; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getInputParams() { return inputParams; }
    public void setInputParams(String inputParams) { this.inputParams = inputParams; }
    public String getOutputParams() { return outputParams; }
    public void setOutputParams(String outputParams) { this.outputParams = outputParams; }
    public LocalDateTime getInvokeTime() { return invokeTime; }
    public void setInvokeTime(LocalDateTime invokeTime) { this.invokeTime = invokeTime; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
}
