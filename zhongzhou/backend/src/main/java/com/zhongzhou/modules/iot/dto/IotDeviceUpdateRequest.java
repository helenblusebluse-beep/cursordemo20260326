package com.zhongzhou.modules.iot.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 编辑设备：仅允许修改备注名称、设备类型、接入位置（绑定）。
 */
public class IotDeviceUpdateRequest {
    @NotBlank
    @Size(max = 15)
    private String remarkName;
    @NotBlank
    private String deviceType;
    private Long bindElderId;
    private Long bindBedId;

    public String getRemarkName() { return remarkName; }
    public void setRemarkName(String remarkName) { this.remarkName = remarkName; }
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    public Long getBindElderId() { return bindElderId; }
    public void setBindElderId(Long bindElderId) { this.bindElderId = bindElderId; }
    public Long getBindBedId() { return bindBedId; }
    public void setBindBedId(Long bindBedId) { this.bindBedId = bindBedId; }
}
