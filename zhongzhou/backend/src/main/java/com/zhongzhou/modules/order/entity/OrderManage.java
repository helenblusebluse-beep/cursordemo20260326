package com.zhongzhou.modules.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("zz_order_manage")
public class OrderManage {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private String elderName;
    private String bedNo;
    private String nursingItemName;
    private String nursingItemType;
    private BigDecimal orderAmount;
    private LocalDateTime expectedServiceTime;
    private String orderUserName;
    private String orderUserMobile;
    private LocalDateTime orderTime;
    private String remark;
    private LocalDateTime payTime;
    private LocalDateTime executeTime;
    private LocalDateTime finishTime;
    private LocalDateTime closeTime;
    private String closeType;
    private Integer status;
    private Integer refundStatus;
    private String cancelReason;
    private String cancelBy;
    private String cancelUserType;
    private LocalDateTime cancelTime;
    private String refundReason;
    private LocalDateTime refundTime;
    private Integer isDeleted;

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
    public String getNursingItemType() { return nursingItemType; }
    public void setNursingItemType(String nursingItemType) { this.nursingItemType = nursingItemType; }
    public BigDecimal getOrderAmount() { return orderAmount; }
    public void setOrderAmount(BigDecimal orderAmount) { this.orderAmount = orderAmount; }
    public LocalDateTime getExpectedServiceTime() { return expectedServiceTime; }
    public void setExpectedServiceTime(LocalDateTime expectedServiceTime) { this.expectedServiceTime = expectedServiceTime; }
    public String getOrderUserName() { return orderUserName; }
    public void setOrderUserName(String orderUserName) { this.orderUserName = orderUserName; }
    public String getOrderUserMobile() { return orderUserMobile; }
    public void setOrderUserMobile(String orderUserMobile) { this.orderUserMobile = orderUserMobile; }
    public LocalDateTime getOrderTime() { return orderTime; }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getPayTime() { return payTime; }
    public void setPayTime(LocalDateTime payTime) { this.payTime = payTime; }
    public LocalDateTime getExecuteTime() { return executeTime; }
    public void setExecuteTime(LocalDateTime executeTime) { this.executeTime = executeTime; }
    public LocalDateTime getFinishTime() { return finishTime; }
    public void setFinishTime(LocalDateTime finishTime) { this.finishTime = finishTime; }
    public LocalDateTime getCloseTime() { return closeTime; }
    public void setCloseTime(LocalDateTime closeTime) { this.closeTime = closeTime; }
    public String getCloseType() { return closeType; }
    public void setCloseType(String closeType) { this.closeType = closeType; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getRefundStatus() { return refundStatus; }
    public void setRefundStatus(Integer refundStatus) { this.refundStatus = refundStatus; }
    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }
    public String getCancelBy() { return cancelBy; }
    public void setCancelBy(String cancelBy) { this.cancelBy = cancelBy; }
    public String getCancelUserType() { return cancelUserType; }
    public void setCancelUserType(String cancelUserType) { this.cancelUserType = cancelUserType; }
    public LocalDateTime getCancelTime() { return cancelTime; }
    public void setCancelTime(LocalDateTime cancelTime) { this.cancelTime = cancelTime; }
    public String getRefundReason() { return refundReason; }
    public void setRefundReason(String refundReason) { this.refundReason = refundReason; }
    public LocalDateTime getRefundTime() { return refundTime; }
    public void setRefundTime(LocalDateTime refundTime) { this.refundTime = refundTime; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
}
