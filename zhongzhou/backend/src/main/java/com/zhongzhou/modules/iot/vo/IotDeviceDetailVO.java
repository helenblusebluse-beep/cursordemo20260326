package com.zhongzhou.modules.iot.vo;

/**
 * 设备详情页：顶部「设备信息」+ Tab「设备详情」字段（物模型由独立接口加载）。
 */
public class IotDeviceDetailVO {
    private Long id;
    private String deviceName;
    private String remarkName;
    private Long productId;
    private String productName;
    /** 顶部与 Tab 展示：与产品 ProductKey 一致（来自产品表 aliyun_product_key） */
    private String productKey;
    /** 完整 DeviceSecret */
    private String deviceSecret;
    private String deviceType;
    private String accessLocationDisplay;
    private Long bindElderId;
    private Long bindBedId;
    private String protocol;
    private Integer onlineStatus;
    private String onlineStatusLabel;
    private String region;
    private String nodeType;
    private String authMethod;
    private String ipAddress;
    private String firmwareVersion;
    private String creatorName;
    private String createdTime;
    private String activationTime;
    private String updatedTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public String getRemarkName() { return remarkName; }
    public void setRemarkName(String remarkName) { this.remarkName = remarkName; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductKey() { return productKey; }
    public void setProductKey(String productKey) { this.productKey = productKey; }
    public String getDeviceSecret() { return deviceSecret; }
    public void setDeviceSecret(String deviceSecret) { this.deviceSecret = deviceSecret; }
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    public String getAccessLocationDisplay() { return accessLocationDisplay; }
    public void setAccessLocationDisplay(String accessLocationDisplay) { this.accessLocationDisplay = accessLocationDisplay; }
    public Long getBindElderId() { return bindElderId; }
    public void setBindElderId(Long bindElderId) { this.bindElderId = bindElderId; }
    public Long getBindBedId() { return bindBedId; }
    public void setBindBedId(Long bindBedId) { this.bindBedId = bindBedId; }
    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    public Integer getOnlineStatus() { return onlineStatus; }
    public void setOnlineStatus(Integer onlineStatus) { this.onlineStatus = onlineStatus; }
    public String getOnlineStatusLabel() { return onlineStatusLabel; }
    public void setOnlineStatusLabel(String onlineStatusLabel) { this.onlineStatusLabel = onlineStatusLabel; }
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
    public String getCreatedTime() { return createdTime; }
    public void setCreatedTime(String createdTime) { this.createdTime = createdTime; }
    public String getActivationTime() { return activationTime; }
    public void setActivationTime(String activationTime) { this.activationTime = activationTime; }
    public String getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(String updatedTime) { this.updatedTime = updatedTime; }
}
