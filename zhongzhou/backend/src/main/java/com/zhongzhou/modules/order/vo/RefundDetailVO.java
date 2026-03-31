package com.zhongzhou.modules.order.vo;

import java.math.BigDecimal;

public class RefundDetailVO {
    private String refundNo;
    private String orderNo;
    private String orderStatusLabel;
    private String refundStatusLabel;
    private String applicant;
    private String applicantType;
    private String applyTime;
    private String refundReason;
    private String refundChannel;
    private String refundMethod;
    private String refundTime;
    private BigDecimal refundAmount;
    private String failCode;
    private String failReason;

    public String getRefundNo() { return refundNo; }
    public void setRefundNo(String refundNo) { this.refundNo = refundNo; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getOrderStatusLabel() { return orderStatusLabel; }
    public void setOrderStatusLabel(String orderStatusLabel) { this.orderStatusLabel = orderStatusLabel; }
    public String getRefundStatusLabel() { return refundStatusLabel; }
    public void setRefundStatusLabel(String refundStatusLabel) { this.refundStatusLabel = refundStatusLabel; }
    public String getApplicant() { return applicant; }
    public void setApplicant(String applicant) { this.applicant = applicant; }
    public String getApplicantType() { return applicantType; }
    public void setApplicantType(String applicantType) { this.applicantType = applicantType; }
    public String getApplyTime() { return applyTime; }
    public void setApplyTime(String applyTime) { this.applyTime = applyTime; }
    public String getRefundReason() { return refundReason; }
    public void setRefundReason(String refundReason) { this.refundReason = refundReason; }
    public String getRefundChannel() { return refundChannel; }
    public void setRefundChannel(String refundChannel) { this.refundChannel = refundChannel; }
    public String getRefundMethod() { return refundMethod; }
    public void setRefundMethod(String refundMethod) { this.refundMethod = refundMethod; }
    public String getRefundTime() { return refundTime; }
    public void setRefundTime(String refundTime) { this.refundTime = refundTime; }
    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }
    public String getFailCode() { return failCode; }
    public void setFailCode(String failCode) { this.failCode = failCode; }
    public String getFailReason() { return failReason; }
    public void setFailReason(String failReason) { this.failReason = failReason; }
}
