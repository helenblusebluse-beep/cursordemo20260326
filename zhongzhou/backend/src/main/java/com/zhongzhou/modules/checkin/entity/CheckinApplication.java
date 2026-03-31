package com.zhongzhou.modules.checkin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("zz_checkin_application")
public class CheckinApplication {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String elderName;
    private String idCard;
    private LocalDateTime birthDate;
    private Integer age;
    private String gender;
    private String contactPhone;
    private String homeAddress;
    private String profilePhotoName;
    private String profilePhotoUrl;
    private String idCardFrontName;
    private String idCardFrontUrl;
    private String idCardBackName;
    private String idCardBackUrl;
    private String roomNo;
    private String careLevel;
    private LocalDateTime checkinStartDate;
    private LocalDateTime checkinEndDate;
    private Double nursingFee;
    private Double bedFee;
    private Double otherFee;
    private Double medicalFee;
    private Double subsidyFee;
    private Double deposit;
    private String contractName;
    private LocalDateTime signDate;
    private String payerName;
    private String payerContact;
    private String contractFileName;
    private String contractFileUrl;
    private Double billAmount;
    private Double monthlyAmount;
    private Double currentAmount;
    private Integer billDays;
    private Integer isRetired;
    private Integer isDeleted;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getElderName() { return elderName; }
    public void setElderName(String elderName) { this.elderName = elderName; }
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public LocalDateTime getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDateTime birthDate) { this.birthDate = birthDate; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getHomeAddress() { return homeAddress; }
    public void setHomeAddress(String homeAddress) { this.homeAddress = homeAddress; }
    public String getProfilePhotoName() { return profilePhotoName; }
    public void setProfilePhotoName(String profilePhotoName) { this.profilePhotoName = profilePhotoName; }
    public String getProfilePhotoUrl() { return profilePhotoUrl; }
    public void setProfilePhotoUrl(String profilePhotoUrl) { this.profilePhotoUrl = profilePhotoUrl; }
    public String getIdCardFrontName() { return idCardFrontName; }
    public void setIdCardFrontName(String idCardFrontName) { this.idCardFrontName = idCardFrontName; }
    public String getIdCardFrontUrl() { return idCardFrontUrl; }
    public void setIdCardFrontUrl(String idCardFrontUrl) { this.idCardFrontUrl = idCardFrontUrl; }
    public String getIdCardBackName() { return idCardBackName; }
    public void setIdCardBackName(String idCardBackName) { this.idCardBackName = idCardBackName; }
    public String getIdCardBackUrl() { return idCardBackUrl; }
    public void setIdCardBackUrl(String idCardBackUrl) { this.idCardBackUrl = idCardBackUrl; }
    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
    public String getCareLevel() { return careLevel; }
    public void setCareLevel(String careLevel) { this.careLevel = careLevel; }
    public LocalDateTime getCheckinStartDate() { return checkinStartDate; }
    public void setCheckinStartDate(LocalDateTime checkinStartDate) { this.checkinStartDate = checkinStartDate; }
    public LocalDateTime getCheckinEndDate() { return checkinEndDate; }
    public void setCheckinEndDate(LocalDateTime checkinEndDate) { this.checkinEndDate = checkinEndDate; }
    public Double getNursingFee() { return nursingFee; }
    public void setNursingFee(Double nursingFee) { this.nursingFee = nursingFee; }
    public Double getBedFee() { return bedFee; }
    public void setBedFee(Double bedFee) { this.bedFee = bedFee; }
    public Double getOtherFee() { return otherFee; }
    public void setOtherFee(Double otherFee) { this.otherFee = otherFee; }
    public Double getMedicalFee() { return medicalFee; }
    public void setMedicalFee(Double medicalFee) { this.medicalFee = medicalFee; }
    public Double getSubsidyFee() { return subsidyFee; }
    public void setSubsidyFee(Double subsidyFee) { this.subsidyFee = subsidyFee; }
    public Double getDeposit() { return deposit; }
    public void setDeposit(Double deposit) { this.deposit = deposit; }
    public String getContractName() { return contractName; }
    public void setContractName(String contractName) { this.contractName = contractName; }
    public LocalDateTime getSignDate() { return signDate; }
    public void setSignDate(LocalDateTime signDate) { this.signDate = signDate; }
    public String getPayerName() { return payerName; }
    public void setPayerName(String payerName) { this.payerName = payerName; }
    public String getPayerContact() { return payerContact; }
    public void setPayerContact(String payerContact) { this.payerContact = payerContact; }
    public String getContractFileName() { return contractFileName; }
    public void setContractFileName(String contractFileName) { this.contractFileName = contractFileName; }
    public String getContractFileUrl() { return contractFileUrl; }
    public void setContractFileUrl(String contractFileUrl) { this.contractFileUrl = contractFileUrl; }
    public Double getBillAmount() { return billAmount; }
    public void setBillAmount(Double billAmount) { this.billAmount = billAmount; }
    public Double getMonthlyAmount() { return monthlyAmount; }
    public void setMonthlyAmount(Double monthlyAmount) { this.monthlyAmount = monthlyAmount; }
    public Double getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(Double currentAmount) { this.currentAmount = currentAmount; }
    public Integer getBillDays() { return billDays; }
    public void setBillDays(Integer billDays) { this.billDays = billDays; }
    public Integer getIsRetired() { return isRetired; }
    public void setIsRetired(Integer isRetired) { this.isRetired = isRetired; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }
}
