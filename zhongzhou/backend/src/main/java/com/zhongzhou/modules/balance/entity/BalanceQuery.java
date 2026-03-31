package com.zhongzhou.modules.balance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("zz_balance_query")
public class BalanceQuery {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String elderName;
    private String bedNo;
    private BigDecimal accountBalance;
    private BigDecimal depositBalance;
    private LocalDateTime changedTime;
    private Integer isDeleted;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getElderName() { return elderName; }
    public void setElderName(String elderName) { this.elderName = elderName; }
    public String getBedNo() { return bedNo; }
    public void setBedNo(String bedNo) { this.bedNo = bedNo; }
    public BigDecimal getAccountBalance() { return accountBalance; }
    public void setAccountBalance(BigDecimal accountBalance) { this.accountBalance = accountBalance; }
    public BigDecimal getDepositBalance() { return depositBalance; }
    public void setDepositBalance(BigDecimal depositBalance) { this.depositBalance = depositBalance; }
    public LocalDateTime getChangedTime() { return changedTime; }
    public void setChangedTime(LocalDateTime changedTime) { this.changedTime = changedTime; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
}
