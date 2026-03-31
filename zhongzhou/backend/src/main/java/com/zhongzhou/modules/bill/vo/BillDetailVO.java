package com.zhongzhou.modules.bill.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BillDetailVO {
    private Long id;
    private String billNo;
    private String billType;
    private String billMonth;
    private String elderName;
    private String elderIdNo;
    private String relatedOrderNo;
    private BigDecimal billTotalAmount;
    private BigDecimal payableAmount;
    private BigDecimal prepayDeductAmount;
    private String billPeriod;
    private Integer totalDays;
    private String payDeadline;
    private String tradeStatusLabel;
    private String createdBy;
    private String createdTime;
    private List<BillItemVO> items = new ArrayList<>();
    private String refundApplicant;
    private String refundApplicantType;
    private String refundSubmitTime;
    private String refundMethod;
    private Integer refundActualDays;
    private Integer refundDays;
    private BigDecimal refundAmount;
    private String cancelBy;
    private String cancelUserType;
    private String cancelTime;
    private String cancelReason;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBillNo() { return billNo; }
    public void setBillNo(String billNo) { this.billNo = billNo; }
    public String getBillType() { return billType; }
    public void setBillType(String billType) { this.billType = billType; }
    public String getBillMonth() { return billMonth; }
    public void setBillMonth(String billMonth) { this.billMonth = billMonth; }
    public String getElderName() { return elderName; }
    public void setElderName(String elderName) { this.elderName = elderName; }
    public String getElderIdNo() { return elderIdNo; }
    public void setElderIdNo(String elderIdNo) { this.elderIdNo = elderIdNo; }
    public String getRelatedOrderNo() { return relatedOrderNo; }
    public void setRelatedOrderNo(String relatedOrderNo) { this.relatedOrderNo = relatedOrderNo; }
    public BigDecimal getBillTotalAmount() { return billTotalAmount; }
    public void setBillTotalAmount(BigDecimal billTotalAmount) { this.billTotalAmount = billTotalAmount; }
    public BigDecimal getPayableAmount() { return payableAmount; }
    public void setPayableAmount(BigDecimal payableAmount) { this.payableAmount = payableAmount; }
    public BigDecimal getPrepayDeductAmount() { return prepayDeductAmount; }
    public void setPrepayDeductAmount(BigDecimal prepayDeductAmount) { this.prepayDeductAmount = prepayDeductAmount; }
    public String getBillPeriod() { return billPeriod; }
    public void setBillPeriod(String billPeriod) { this.billPeriod = billPeriod; }
    public Integer getTotalDays() { return totalDays; }
    public void setTotalDays(Integer totalDays) { this.totalDays = totalDays; }
    public String getPayDeadline() { return payDeadline; }
    public void setPayDeadline(String payDeadline) { this.payDeadline = payDeadline; }
    public String getTradeStatusLabel() { return tradeStatusLabel; }
    public void setTradeStatusLabel(String tradeStatusLabel) { this.tradeStatusLabel = tradeStatusLabel; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public String getCreatedTime() { return createdTime; }
    public void setCreatedTime(String createdTime) { this.createdTime = createdTime; }
    public List<BillItemVO> getItems() { return items; }
    public void setItems(List<BillItemVO> items) { this.items = items; }
    public String getRefundApplicant() { return refundApplicant; }
    public void setRefundApplicant(String refundApplicant) { this.refundApplicant = refundApplicant; }
    public String getRefundApplicantType() { return refundApplicantType; }
    public void setRefundApplicantType(String refundApplicantType) { this.refundApplicantType = refundApplicantType; }
    public String getRefundSubmitTime() { return refundSubmitTime; }
    public void setRefundSubmitTime(String refundSubmitTime) { this.refundSubmitTime = refundSubmitTime; }
    public String getRefundMethod() { return refundMethod; }
    public void setRefundMethod(String refundMethod) { this.refundMethod = refundMethod; }
    public Integer getRefundActualDays() { return refundActualDays; }
    public void setRefundActualDays(Integer refundActualDays) { this.refundActualDays = refundActualDays; }
    public Integer getRefundDays() { return refundDays; }
    public void setRefundDays(Integer refundDays) { this.refundDays = refundDays; }
    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }
    public String getCancelBy() { return cancelBy; }
    public void setCancelBy(String cancelBy) { this.cancelBy = cancelBy; }
    public String getCancelUserType() { return cancelUserType; }
    public void setCancelUserType(String cancelUserType) { this.cancelUserType = cancelUserType; }
    public String getCancelTime() { return cancelTime; }
    public void setCancelTime(String cancelTime) { this.cancelTime = cancelTime; }
    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }
}
