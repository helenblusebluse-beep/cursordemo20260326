package com.zhongzhou.modules.iot.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class IotDeviceCreateRequest {
    @NotBlank
    @Size(max = 15)
    private String deviceName;
    @NotBlank
    @Size(max = 15)
    private String remarkName;
    @NotNull
    private Long productId;
    /** 随身设备 | 固定设备 */
    @NotBlank
    private String deviceType;
    /** 随身设备必填 */
    private Long bindElderId;
    /** 固定设备必填 */
    private Long bindBedId;

    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public String getRemarkName() { return remarkName; }
    public void setRemarkName(String remarkName) { this.remarkName = remarkName; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    public Long getBindElderId() { return bindElderId; }
    public void setBindElderId(Long bindElderId) { this.bindElderId = bindElderId; }
    public Long getBindBedId() { return bindBedId; }
    public void setBindBedId(Long bindBedId) { this.bindBedId = bindBedId; }
}
