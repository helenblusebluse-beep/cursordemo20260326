package com.zhongzhou.modules.checkout.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("zz_checkout_application")
public class CheckoutApplication {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long checkinId;
    private String elderName;
    private String idCard;
    private String roomNo;
    private String careLevel;
    private String contactPhone;
    private String checkinPeriod;
    private String feePeriod;
    private LocalDateTime checkoutDate;
    private String checkoutReason;
    private LocalDateTime agreementDate;
    private String agreementFileName;
    private String caregiverNames;
    private String refundVoucherName;
    private String refundVoucherUrl;
    private String refundMethod;
    private String refundRemark;
    private String voucherSubmitter;
    private LocalDateTime voucherSubmitTime;
    private Double finalRefundAmount;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCheckinId() { return checkinId; }
    public void setCheckinId(Long checkinId) { this.checkinId = checkinId; }
    public String getElderName() { return elderName; }
    public void setElderName(String elderName) { this.elderName = elderName; }
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
    public String getCareLevel() { return careLevel; }
    public void setCareLevel(String careLevel) { this.careLevel = careLevel; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getCheckinPeriod() { return checkinPeriod; }
    public void setCheckinPeriod(String checkinPeriod) { this.checkinPeriod = checkinPeriod; }
    public String getFeePeriod() { return feePeriod; }
    public void setFeePeriod(String feePeriod) { this.feePeriod = feePeriod; }
    public LocalDateTime getCheckoutDate() { return checkoutDate; }
    public void setCheckoutDate(LocalDateTime checkoutDate) { this.checkoutDate = checkoutDate; }
    public String getCheckoutReason() { return checkoutReason; }
    public void setCheckoutReason(String checkoutReason) { this.checkoutReason = checkoutReason; }
    public LocalDateTime getAgreementDate() { return agreementDate; }
    public void setAgreementDate(LocalDateTime agreementDate) { this.agreementDate = agreementDate; }
    public String getAgreementFileName() { return agreementFileName; }
    public void setAgreementFileName(String agreementFileName) { this.agreementFileName = agreementFileName; }
    public String getCaregiverNames() { return caregiverNames; }
    public void setCaregiverNames(String caregiverNames) { this.caregiverNames = caregiverNames; }
    public String getRefundVoucherName() { return refundVoucherName; }
    public void setRefundVoucherName(String refundVoucherName) { this.refundVoucherName = refundVoucherName; }
    public String getRefundVoucherUrl() { return refundVoucherUrl; }
    public void setRefundVoucherUrl(String refundVoucherUrl) { this.refundVoucherUrl = refundVoucherUrl; }
    public String getRefundMethod() { return refundMethod; }
    public void setRefundMethod(String refundMethod) { this.refundMethod = refundMethod; }
    public String getRefundRemark() { return refundRemark; }
    public void setRefundRemark(String refundRemark) { this.refundRemark = refundRemark; }
    public String getVoucherSubmitter() { return voucherSubmitter; }
    public void setVoucherSubmitter(String voucherSubmitter) { this.voucherSubmitter = voucherSubmitter; }
    public LocalDateTime getVoucherSubmitTime() { return voucherSubmitTime; }
    public void setVoucherSubmitTime(LocalDateTime voucherSubmitTime) { this.voucherSubmitTime = voucherSubmitTime; }
    public Double getFinalRefundAmount() { return finalRefundAmount; }
    public void setFinalRefundAmount(Double finalRefundAmount) { this.finalRefundAmount = finalRefundAmount; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }
}
