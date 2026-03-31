package com.zhongzhou.modules.order.vo;

import java.math.BigDecimal;

public class RefundManageVO {
    private Long id;
    private String refundNo;
    private String orderNo;
    private BigDecimal orderAmount;
    private String applicant;
    private String applyTime;
    private String refundTime;
    private String orderStatusLabel;
    private String refundStatusLabel;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRefundNo() { return refundNo; }
    public void setRefundNo(String refundNo) { this.refundNo = refundNo; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public BigDecimal getOrderAmount() { return orderAmount; }
    public void setOrderAmount(BigDecimal orderAmount) { this.orderAmount = orderAmount; }
    public String getApplicant() { return applicant; }
    public void setApplicant(String applicant) { this.applicant = applicant; }
    public String getApplyTime() { return applyTime; }
    public void setApplyTime(String applyTime) { this.applyTime = applyTime; }
    public String getRefundTime() { return refundTime; }
    public void setRefundTime(String refundTime) { this.refundTime = refundTime; }
    public String getOrderStatusLabel() { return orderStatusLabel; }
    public void setOrderStatusLabel(String orderStatusLabel) { this.orderStatusLabel = orderStatusLabel; }
    public String getRefundStatusLabel() { return refundStatusLabel; }
    public void setRefundStatusLabel(String refundStatusLabel) { this.refundStatusLabel = refundStatusLabel; }
}
