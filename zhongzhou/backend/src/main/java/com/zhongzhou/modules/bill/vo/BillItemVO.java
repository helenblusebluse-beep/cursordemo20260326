package com.zhongzhou.modules.bill.vo;

import java.math.BigDecimal;

public class BillItemVO {
    private String type;
    private String feeItemName;
    private String serviceContent;
    private BigDecimal amount;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getFeeItemName() { return feeItemName; }
    public void setFeeItemName(String feeItemName) { this.feeItemName = feeItemName; }
    public String getServiceContent() { return serviceContent; }
    public void setServiceContent(String serviceContent) { this.serviceContent = serviceContent; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
