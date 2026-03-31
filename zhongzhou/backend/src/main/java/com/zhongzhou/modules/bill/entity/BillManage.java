package com.zhongzhou.modules.bill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("zz_bill_manage")
public class BillManage {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String billNo;
    private String billType;
    private String billMonth;
    private String elderName;
    private String elderIdNo;
    private String bedNo;
    private String relatedOrderNo;
    private BigDecimal billTotalAmount;
    private BigDecimal payableAmount;
    private BigDecimal prepayDeductAmount;
    private LocalDate billPeriodStart;
    private LocalDate billPeriodEnd;
    private Integer totalDays;
    private LocalDateTime payDeadline;
    private Integer tradeStatus;
    private String createdBy;
    private LocalDateTime createdTime;
    private Integer isDeleted;

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
    public String getBedNo() { return bedNo; }
    public void setBedNo(String bedNo) { this.bedNo = bedNo; }
    public String getRelatedOrderNo() { return relatedOrderNo; }
    public void setRelatedOrderNo(String relatedOrderNo) { this.relatedOrderNo = relatedOrderNo; }
    public BigDecimal getBillTotalAmount() { return billTotalAmount; }
    public void setBillTotalAmount(BigDecimal billTotalAmount) { this.billTotalAmount = billTotalAmount; }
    public BigDecimal getPayableAmount() { return payableAmount; }
    public void setPayableAmount(BigDecimal payableAmount) { this.payableAmount = payableAmount; }
    public BigDecimal getPrepayDeductAmount() { return prepayDeductAmount; }
    public void setPrepayDeductAmount(BigDecimal prepayDeductAmount) { this.prepayDeductAmount = prepayDeductAmount; }
    public LocalDate getBillPeriodStart() { return billPeriodStart; }
    public void setBillPeriodStart(LocalDate billPeriodStart) { this.billPeriodStart = billPeriodStart; }
    public LocalDate getBillPeriodEnd() { return billPeriodEnd; }
    public void setBillPeriodEnd(LocalDate billPeriodEnd) { this.billPeriodEnd = billPeriodEnd; }
    public Integer getTotalDays() { return totalDays; }
    public void setTotalDays(Integer totalDays) { this.totalDays = totalDays; }
    public LocalDateTime getPayDeadline() { return payDeadline; }
    public void setPayDeadline(LocalDateTime payDeadline) { this.payDeadline = payDeadline; }
    public Integer getTradeStatus() { return tradeStatus; }
    public void setTradeStatus(Integer tradeStatus) { this.tradeStatus = tradeStatus; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
}
