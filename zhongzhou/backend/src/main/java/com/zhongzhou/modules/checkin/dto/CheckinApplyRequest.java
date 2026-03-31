package com.zhongzhou.modules.checkin.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class CheckinApplyRequest {
    @NotBlank(message = "老人姓名不能为空")
    @Size(max = 10, message = "老人姓名最多10个字符")
    private String elderName;

    @NotBlank(message = "老人身份证号不能为空")
    @Pattern(regexp = "^[0-9Xx]{18}$", message = "身份证号格式错误")
    private String idCard;

    @NotBlank(message = "入住床位不能为空")
    private String roomNo;

    @NotBlank(message = "护理等级不能为空")
    private String careLevel;

    @NotBlank(message = "入住周期不能为空")
    private String checkinPeriod;
    @NotBlank(message = "联系方式不能为空")
    @Pattern(regexp = "^1\\d{10}$", message = "联系方式格式错误")
    private String contactPhone;
    @NotBlank(message = "家庭住址不能为空")
    @Size(max = 100, message = "家庭住址最多100个字符")
    private String homeAddress;
    @NotBlank(message = "一寸照片不能为空")
    private String profilePhotoName;
    @NotBlank(message = "身份证人像面不能为空")
    private String idCardFrontName;
    @NotBlank(message = "身份证国徽面不能为空")
    private String idCardBackName;
    @NotBlank(message = "入住期限不能为空")
    private String checkinStartDate;
    @NotBlank(message = "费用期限不能为空")
    private String feeStartDate;
    @NotBlank(message = "护理费用不能为空")
    private String nursingFee;
    @NotBlank(message = "床位费用不能为空")
    private String bedFee;
    @NotBlank(message = "合同名称不能为空")
    @Size(max = 20, message = "合同名称最多20个字符")
    private String contractName;
    @NotBlank(message = "签约日期不能为空")
    private String signDate;
    private String payerName;
    private String payerContact;
    @NotBlank(message = "入住合同不能为空")
    private String contractFileName;
    private List<FamilyMemberDTO> familyMembers;

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
    public String getCheckinStartDate() { return checkinStartDate; }
    public void setCheckinStartDate(String checkinStartDate) { this.checkinStartDate = checkinStartDate; }
    public String getFeeStartDate() { return feeStartDate; }
    public void setFeeStartDate(String feeStartDate) { this.feeStartDate = feeStartDate; }
    public String getNursingFee() { return nursingFee; }
    public void setNursingFee(String nursingFee) { this.nursingFee = nursingFee; }
    public String getBedFee() { return bedFee; }
    public void setBedFee(String bedFee) { this.bedFee = bedFee; }
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
    public List<FamilyMemberDTO> getFamilyMembers() { return familyMembers; }
    public void setFamilyMembers(List<FamilyMemberDTO> familyMembers) { this.familyMembers = familyMembers; }

    public static class FamilyMemberDTO {
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
