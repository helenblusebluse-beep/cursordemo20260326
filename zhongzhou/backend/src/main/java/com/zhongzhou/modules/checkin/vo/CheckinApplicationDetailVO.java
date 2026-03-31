package com.zhongzhou.modules.checkin.vo;

import java.util.List;

public class CheckinApplicationDetailVO {
    private Long id;
    private String elderName;
    private String idCard;
    private String birthDate;
    private Integer age;
    private String gender;
    private String contactPhone;
    private String homeAddress;
    private String profilePhotoName;
    private String idCardFrontName;
    private String idCardBackName;
    private String roomNo;
    private String careLevel;
    private String checkinPeriod;
    private String feePeriod;
    private Double nursingFee;
    private Double bedFee;
    private Double otherFee;
    private Double medicalFee;
    private Double subsidyFee;
    private Double deposit;
    private String contractName;
    private String signDate;
    private String payerName;
    private String payerContact;
    private String contractFileName;
    private Double monthlyAmount;
    private Double currentAmount;
    private Double billAmount;
    private Integer billDays;
    private List<FamilyMemberVO> familyMembers;
    private String createdTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getElderName() { return elderName; }
    public void setElderName(String elderName) { this.elderName = elderName; }
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
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
    public String getIdCardFrontName() { return idCardFrontName; }
    public void setIdCardFrontName(String idCardFrontName) { this.idCardFrontName = idCardFrontName; }
    public String getIdCardBackName() { return idCardBackName; }
    public void setIdCardBackName(String idCardBackName) { this.idCardBackName = idCardBackName; }
    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
    public String getCareLevel() { return careLevel; }
    public void setCareLevel(String careLevel) { this.careLevel = careLevel; }
    public String getCheckinPeriod() { return checkinPeriod; }
    public void setCheckinPeriod(String checkinPeriod) { this.checkinPeriod = checkinPeriod; }
    public String getFeePeriod() { return feePeriod; }
    public void setFeePeriod(String feePeriod) { this.feePeriod = feePeriod; }
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
    public String getSignDate() { return signDate; }
    public void setSignDate(String signDate) { this.signDate = signDate; }
    public String getPayerName() { return payerName; }
    public void setPayerName(String payerName) { this.payerName = payerName; }
    public String getPayerContact() { return payerContact; }
    public void setPayerContact(String payerContact) { this.payerContact = payerContact; }
    public String getContractFileName() { return contractFileName; }
    public void setContractFileName(String contractFileName) { this.contractFileName = contractFileName; }
    public Double getMonthlyAmount() { return monthlyAmount; }
    public void setMonthlyAmount(Double monthlyAmount) { this.monthlyAmount = monthlyAmount; }
    public Double getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(Double currentAmount) { this.currentAmount = currentAmount; }
    public Double getBillAmount() { return billAmount; }
    public void setBillAmount(Double billAmount) { this.billAmount = billAmount; }
    public Integer getBillDays() { return billDays; }
    public void setBillDays(Integer billDays) { this.billDays = billDays; }
    public List<FamilyMemberVO> getFamilyMembers() { return familyMembers; }
    public void setFamilyMembers(List<FamilyMemberVO> familyMembers) { this.familyMembers = familyMembers; }
    public String getCreatedTime() { return createdTime; }
    public void setCreatedTime(String createdTime) { this.createdTime = createdTime; }

    public static class FamilyMemberVO {
        private String familyName;
        private String familyContact;
        private String relationType;
        public String getFamilyName() { return familyName; }
        public void setFamilyName(String familyName) { this.familyName = familyName; }
        public String getFamilyContact() { return familyContact; }
        public void setFamilyContact(String familyContact) { this.familyContact = familyContact; }
        public String getRelationType() { return relationType; }
        public void setRelationType(String relationType) { this.relationType = relationType; }
    }
}
