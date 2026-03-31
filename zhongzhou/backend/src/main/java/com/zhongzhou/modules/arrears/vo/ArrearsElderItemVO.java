package com.zhongzhou.modules.arrears.vo;

import java.math.BigDecimal;

public class ArrearsElderItemVO {
    private String elderName;
    private String bedNo;
    private BigDecimal arrearsAmount;
    private String payDeadline;

    public String getElderName() { return elderName; }
    public void setElderName(String elderName) { this.elderName = elderName; }
    public String getBedNo() { return bedNo; }
    public void setBedNo(String bedNo) { this.bedNo = bedNo; }
    public BigDecimal getArrearsAmount() { return arrearsAmount; }
    public void setArrearsAmount(BigDecimal arrearsAmount) { this.arrearsAmount = arrearsAmount; }
    public String getPayDeadline() { return payDeadline; }
    public void setPayDeadline(String payDeadline) { this.payDeadline = payDeadline; }
}
