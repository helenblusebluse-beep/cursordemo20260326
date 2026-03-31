package com.zhongzhou.modules.checkout.vo;

import java.util.List;

public class CheckoutDetailVO {
    private Long id;
    private Long checkinId;
    private String elderName;
    private String idCard;
    private String roomNo;
    private String careLevel;
    private String checkinPeriod;
    private String feePeriod;
    private String contactPhone;
    private String checkoutDate;
    private String checkoutReason;
    private String agreementDate;
    private String agreementFileName;
    private String homeAddress;
    private String caregiverNames;
    private String refundMethod;
    private String refundVoucherName;
    private String refundVoucherUrl;
    private String refundRemark;
    private String voucherSubmitter;
    private String voucherSubmitTime;
    private Double shouldRefundSubtotal;
    private Double debtSubtotal;
    private Double balanceSubtotal;
    private Double unpaidSubtotal;
    private Double finalRefundAmount;
    private Integer status;
    private List<FeeItemVO> shouldRefundItems;
    private List<FeeItemVO> debtItems;
    private List<FeeItemVO> balanceItems;
    private List<FeeItemVO> unpaidItems;

    public static class FeeItemVO {
        private Long id;
        private String billNo;
        private String billMonth;
        private String itemName;
        private Double amount;
        private Integer actualDays;
        private Integer refundDays;
        private Integer status;
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
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
    }

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
    public String getCheckinPeriod() { return checkinPeriod; }
    public void setCheckinPeriod(String checkinPeriod) { this.checkinPeriod = checkinPeriod; }
    public String getFeePeriod() { return feePeriod; }
    public void setFeePeriod(String feePeriod) { this.feePeriod = feePeriod; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getCheckoutDate() { return checkoutDate; }
    public void setCheckoutDate(String checkoutDate) { this.checkoutDate = checkoutDate; }
    public String getCheckoutReason() { return checkoutReason; }
    public void setCheckoutReason(String checkoutReason) { this.checkoutReason = checkoutReason; }
    public String getAgreementDate() { return agreementDate; }
    public void setAgreementDate(String agreementDate) { this.agreementDate = agreementDate; }
    public String getAgreementFileName() { return agreementFileName; }
    public void setAgreementFileName(String agreementFileName) { this.agreementFileName = agreementFileName; }
    public String getHomeAddress() { return homeAddress; }
    public void setHomeAddress(String homeAddress) { this.homeAddress = homeAddress; }
    public String getCaregiverNames() { return caregiverNames; }
    public void setCaregiverNames(String caregiverNames) { this.caregiverNames = caregiverNames; }
    public String getRefundMethod() { return refundMethod; }
    public void setRefundMethod(String refundMethod) { this.refundMethod = refundMethod; }
    public String getRefundVoucherName() { return refundVoucherName; }
    public void setRefundVoucherName(String refundVoucherName) { this.refundVoucherName = refundVoucherName; }
    public String getRefundVoucherUrl() { return refundVoucherUrl; }
    public void setRefundVoucherUrl(String refundVoucherUrl) { this.refundVoucherUrl = refundVoucherUrl; }
    public String getRefundRemark() { return refundRemark; }
    public void setRefundRemark(String refundRemark) { this.refundRemark = refundRemark; }
    public String getVoucherSubmitter() { return voucherSubmitter; }
    public void setVoucherSubmitter(String voucherSubmitter) { this.voucherSubmitter = voucherSubmitter; }
    public String getVoucherSubmitTime() { return voucherSubmitTime; }
    public void setVoucherSubmitTime(String voucherSubmitTime) { this.voucherSubmitTime = voucherSubmitTime; }
    public Double getShouldRefundSubtotal() { return shouldRefundSubtotal; }
    public void setShouldRefundSubtotal(Double shouldRefundSubtotal) { this.shouldRefundSubtotal = shouldRefundSubtotal; }
    public Double getDebtSubtotal() { return debtSubtotal; }
    public void setDebtSubtotal(Double debtSubtotal) { this.debtSubtotal = debtSubtotal; }
    public Double getBalanceSubtotal() { return balanceSubtotal; }
    public void setBalanceSubtotal(Double balanceSubtotal) { this.balanceSubtotal = balanceSubtotal; }
    public Double getUnpaidSubtotal() { return unpaidSubtotal; }
    public void setUnpaidSubtotal(Double unpaidSubtotal) { this.unpaidSubtotal = unpaidSubtotal; }
    public Double getFinalRefundAmount() { return finalRefundAmount; }
    public void setFinalRefundAmount(Double finalRefundAmount) { this.finalRefundAmount = finalRefundAmount; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public List<FeeItemVO> getShouldRefundItems() { return shouldRefundItems; }
    public void setShouldRefundItems(List<FeeItemVO> shouldRefundItems) { this.shouldRefundItems = shouldRefundItems; }
    public List<FeeItemVO> getDebtItems() { return debtItems; }
    public void setDebtItems(List<FeeItemVO> debtItems) { this.debtItems = debtItems; }
    public List<FeeItemVO> getBalanceItems() { return balanceItems; }
    public void setBalanceItems(List<FeeItemVO> balanceItems) { this.balanceItems = balanceItems; }
    public List<FeeItemVO> getUnpaidItems() { return unpaidItems; }
    public void setUnpaidItems(List<FeeItemVO> unpaidItems) { this.unpaidItems = unpaidItems; }
}
