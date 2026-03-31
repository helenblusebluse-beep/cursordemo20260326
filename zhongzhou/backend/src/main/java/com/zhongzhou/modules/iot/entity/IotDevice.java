package com.zhongzhou.modules.iot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("zz_iot_device")
public class IotDevice {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String deviceName;
    private String remarkName;
    private Long productId;
    /** 随身设备 | 固定设备 */
    private String deviceType;
    private String accessLocationDisplay;
    private Long bindElderId;
    private Long bindBedId;
    private String productKey;
    private String deviceKey;
    private String deviceSecret;
    private String region;
    private String nodeType;
    private String authMethod;
    private String ipAddress;
    private String firmwareVersion;
    private String creatorName;
    private LocalDateTime activationTime;
    private String protocol;
    private Integer onlineStatus;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private Integer isDeleted;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public String getRemarkName() { return remarkName; }
    public void setRemarkName(String remarkName) { this.remarkName = remarkName; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    public String getAccessLocationDisplay() { return accessLocationDisplay; }
    public void setAccessLocationDisplay(String accessLocationDisplay) { this.accessLocationDisplay = accessLocationDisplay; }
    public Long getBindElderId() { return bindElderId; }
    public void setBindElderId(Long bindElderId) { this.bindElderId = bindElderId; }
    public Long getBindBedId() { return bindBedId; }
    public void setBindBedId(Long bindBedId) { this.bindBedId = bindBedId; }
    public String getProductKey() { return productKey; }
    public void setProductKey(String productKey) { this.productKey = productKey; }
    public String getDeviceKey() { return deviceKey; }
    public void setDeviceKey(String deviceKey) { this.deviceKey = deviceKey; }
    public String getDeviceSecret() { return deviceSecret; }
    public void setDeviceSecret(String deviceSecret) { this.deviceSecret = deviceSecret; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getNodeType() { return nodeType; }
    public void setNodeType(String nodeType) { this.nodeType = nodeType; }
    public String getAuthMethod() { return authMethod; }
    public void setAuthMethod(String authMethod) { this.authMethod = authMethod; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getFirmwareVersion() { return firmwareVersion; }
    public void setFirmwareVersion(String firmwareVersion) { this.firmwareVersion = firmwareVersion; }
    public String getCreatorName() { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }
    public LocalDateTime getActivationTime() { return activationTime; }
    public void setActivationTime(LocalDateTime activationTime) { this.activationTime = activationTime; }
    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    public Integer getOnlineStatus() { return onlineStatus; }
    public void setOnlineStatus(Integer onlineStatus) { this.onlineStatus = onlineStatus; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
}
