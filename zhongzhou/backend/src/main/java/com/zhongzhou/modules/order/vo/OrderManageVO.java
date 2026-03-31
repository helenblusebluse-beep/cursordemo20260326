package com.zhongzhou.modules.order.vo;

import java.math.BigDecimal;

public class OrderManageVO {
    private Long id;
    private String orderNo;
    private String elderName;
    private String bedNo;
    private String nursingItemName;
    private BigDecimal orderAmount;
    private String expectedServiceTime;
    private String orderUserName;
    private String orderTime;
    private Integer status;
    private String statusLabel;
    private Integer refundStatus;
    private String refundStatusLabel;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getElderName() { return elderName; }
    public void setElderName(String elderName) { this.elderName = elderName; }
    public String getBedNo() { return bedNo; }
    public void setBedNo(String bedNo) { this.bedNo = bedNo; }
    public String getNursingItemName() { return nursingItemName; }
    public void setNursingItemName(String nursingItemName) { this.nursingItemName = nursingItemName; }
    public BigDecimal getOrderAmount() { return orderAmount; }
    public void setOrderAmount(BigDecimal orderAmount) { this.orderAmount = orderAmount; }
    public String getExpectedServiceTime() { return expectedServiceTime; }
    public void setExpectedServiceTime(String expectedServiceTime) { this.expectedServiceTime = expectedServiceTime; }
    public String getOrderUserName() { return orderUserName; }
    public void setOrderUserName(String orderUserName) { this.orderUserName = orderUserName; }
    public String getOrderTime() { return orderTime; }
    public void setOrderTime(String orderTime) { this.orderTime = orderTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getStatusLabel() { return statusLabel; }
    public void setStatusLabel(String statusLabel) { this.statusLabel = statusLabel; }
    public Integer getRefundStatus() { return refundStatus; }
    public void setRefundStatus(Integer refundStatus) { this.refundStatus = refundStatus; }
    public String getRefundStatusLabel() { return refundStatusLabel; }
    public void setRefundStatusLabel(String refundStatusLabel) { this.refundStatusLabel = refundStatusLabel; }
}
