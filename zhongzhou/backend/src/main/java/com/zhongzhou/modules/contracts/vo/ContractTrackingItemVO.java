package com.zhongzhou.modules.contracts.vo;

public class ContractTrackingItemVO {
    private Long id;
    private String contractNo;
    private String contractName;
    private String elderName;
    private String idCard;
    private String contractPeriod;
    /** 未生效 / 生效中 / 已过期 / 已失效 */
    private String contractStatus;
    private String createdTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    public String getContractName() { return contractName; }
    public void setContractName(String contractName) { this.contractName = contractName; }
    public String getElderName() { return elderName; }
    public void setElderName(String elderName) { this.elderName = elderName; }
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public String getContractPeriod() { return contractPeriod; }
    public void setContractPeriod(String contractPeriod) { this.contractPeriod = contractPeriod; }
    public String getContractStatus() { return contractStatus; }
    public void setContractStatus(String contractStatus) { this.contractStatus = contractStatus; }
    public String getCreatedTime() { return createdTime; }
    public void setCreatedTime(String createdTime) { this.createdTime = createdTime; }
}
