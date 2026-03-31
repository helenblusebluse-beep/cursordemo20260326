package com.zhongzhou.modules.balance.vo;

import java.math.BigDecimal;

public class BalanceQueryItemVO {
    private Long id;
    private String elderName;
    private String bedNo;
    private BigDecimal accountBalance;
    private BigDecimal depositBalance;
    private String changedTime;

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
    public String getChangedTime() { return changedTime; }
    public void setChangedTime(String changedTime) { this.changedTime = changedTime; }
}
