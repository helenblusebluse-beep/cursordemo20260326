package com.zhongzhou.modules.order.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class OrderRefundRequest {
    @NotBlank(message = "退款原因不能为空")
    @Size(max = 100, message = "退款原因最多100个字符")
    private String refundReason;

    public String getRefundReason() { return refundReason; }
    public void setRefundReason(String refundReason) { this.refundReason = refundReason; }
}
