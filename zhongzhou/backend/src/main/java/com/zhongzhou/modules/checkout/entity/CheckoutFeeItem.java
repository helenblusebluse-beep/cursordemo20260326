package com.zhongzhou.modules.checkout.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("zz_checkout_fee_item")
public class CheckoutFeeItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long applicationId;
    private Integer feeCategory;
    private String billNo;
    private String billMonth;
    private String itemName;
    private Double amount;
    private Integer actualDays;
    private Integer refundDays;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createdTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public Integer getFeeCategory() { return feeCategory; }
    public void setFeeCategory(Integer feeCategory) { this.feeCategory = feeCategory; }
    public String getBillNo() { return billNo; }
    public void setBillNo(String billNo) { this.billNo = billNo; }
    public String getBillMonth() { return billMonth; }
    public void setBillMonth(String billMonth) { this.billMonth = billMonth; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public Integer getActualDays() { return actualDays; }
    public void setActualDays(Integer actualDays) { this.actualDays = actualDays; }
    public Integer getRefundDays() { return refundDays; }
    public void setRefundDays(Integer refundDays) { this.refundDays = refundDays; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
}
