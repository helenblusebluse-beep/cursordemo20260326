package com.zhongzhou.modules.arrears.vo;

import java.math.BigDecimal;

public class ArrearsBillRowVO {
    private String billNo;
    private String billMonth;
    private String payDeadline;
    private BigDecimal payableAmount;

    public String getBillNo() { return billNo; }
    public void setBillNo(String billNo) { this.billNo = billNo; }
    public String getBillMonth() { return billMonth; }
    public void setBillMonth(String billMonth) { this.billMonth = billMonth; }
    public String getPayDeadline() { return payDeadline; }
    public void setPayDeadline(String payDeadline) { this.payDeadline = payDeadline; }
    public BigDecimal getPayableAmount() { return payableAmount; }
    public void setPayableAmount(BigDecimal payableAmount) { this.payableAmount = payableAmount; }
}
