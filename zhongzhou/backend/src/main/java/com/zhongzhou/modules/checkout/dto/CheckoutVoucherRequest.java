package com.zhongzhou.modules.checkout.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CheckoutVoucherRequest {
    @NotBlank(message = "退款方式不能为空")
    private String refundMethod;
    @NotBlank(message = "退款凭证不能为空")
    private String refundVoucherName;
    @NotBlank(message = "退款备注不能为空")
    @Size(max = 50, message = "退款备注最多50字符")
    private String refundRemark;
    /** 凭证图片地址（可选，未传则前端使用占位图） */
    private String refundVoucherUrl;
    /** 提交人（可选，默认管理员） */
    private String voucherSubmitter;

    public String getRefundMethod() { return refundMethod; }
    public void setRefundMethod(String refundMethod) { this.refundMethod = refundMethod; }
    public String getRefundVoucherName() { return refundVoucherName; }
    public void setRefundVoucherName(String refundVoucherName) { this.refundVoucherName = refundVoucherName; }
    public String getRefundRemark() { return refundRemark; }
    public void setRefundRemark(String refundRemark) { this.refundRemark = refundRemark; }
    public String getRefundVoucherUrl() { return refundVoucherUrl; }
    public void setRefundVoucherUrl(String refundVoucherUrl) { this.refundVoucherUrl = refundVoucherUrl; }
    public String getVoucherSubmitter() { return voucherSubmitter; }
    public void setVoucherSubmitter(String voucherSubmitter) { this.voucherSubmitter = voucherSubmitter; }
}
