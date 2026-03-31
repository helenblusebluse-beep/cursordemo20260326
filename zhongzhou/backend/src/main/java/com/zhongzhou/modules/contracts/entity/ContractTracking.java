package com.zhongzhou.modules.contracts.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("zz_contract_tracking")
public class ContractTracking {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long checkinId;
    private String contractNo;
    private String contractName;
    private LocalDateTime contractStartTime;
    private LocalDateTime contractEndTime;
    private LocalDateTime invalidatedTime;
    private String invalidatedRemark;
    private Integer isDeleted;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCheckinId() { return checkinId; }
    public void setCheckinId(Long checkinId) { this.checkinId = checkinId; }
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    public String getContractName() { return contractName; }
    public void setContractName(String contractName) { this.contractName = contractName; }
    public LocalDateTime getContractStartTime() { return contractStartTime; }
    public void setContractStartTime(LocalDateTime contractStartTime) { this.contractStartTime = contractStartTime; }
    public LocalDateTime getContractEndTime() { return contractEndTime; }
    public void setContractEndTime(LocalDateTime contractEndTime) { this.contractEndTime = contractEndTime; }
    public LocalDateTime getInvalidatedTime() { return invalidatedTime; }
    public void setInvalidatedTime(LocalDateTime invalidatedTime) { this.invalidatedTime = invalidatedTime; }
    public String getInvalidatedRemark() { return invalidatedRemark; }
    public void setInvalidatedRemark(String invalidatedRemark) { this.invalidatedRemark = invalidatedRemark; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }
}
