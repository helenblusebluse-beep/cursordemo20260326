package com.zhongzhou.modules.iot.dto;

import javax.validation.constraints.NotBlank;

public class IotAlarmRecordHandleRequest {
    @NotBlank
    private String handleTime;
    @NotBlank
    private String handleResult;

    public String getHandleTime() { return handleTime; }
    public void setHandleTime(String handleTime) { this.handleTime = handleTime; }
    public String getHandleResult() { return handleResult; }
    public void setHandleResult(String handleResult) { this.handleResult = handleResult; }
}

