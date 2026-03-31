package com.zhongzhou.modules.iot.vo;

/** 设备列表行（对齐原型表字段） */
public class IotDeviceVO {
    private Long id;
    private String deviceName;
    private String remarkName;
    private String productName;
    private String accessLocationDisplay;
    private String deviceType;
    private String createdTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public String getRemarkName() { return remarkName; }
    public void setRemarkName(String remarkName) { this.remarkName = remarkName; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getAccessLocationDisplay() { return accessLocationDisplay; }
    public void setAccessLocationDisplay(String accessLocationDisplay) { this.accessLocationDisplay = accessLocationDisplay; }
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    public String getCreatedTime() { return createdTime; }
    public void setCreatedTime(String createdTime) { this.createdTime = createdTime; }
}
