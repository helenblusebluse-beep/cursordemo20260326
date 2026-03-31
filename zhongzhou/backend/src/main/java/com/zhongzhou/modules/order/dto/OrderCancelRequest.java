package com.zhongzhou.modules.order.dto;

import javax.validation.constraints.NotBlank;

public class OrderCancelRequest {
    @NotBlank(message = "取消原因不能为空")
    private String cancelReason;

    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }
}
