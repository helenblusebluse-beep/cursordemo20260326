package com.zhongzhou.modules.iot.vo;

public class IotProductVO {
    private Long id;
    private String productName;
    private String aliyunProductKey;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProductName() { return this.productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getAliyunProductKey() { return aliyunProductKey; }
    public void setAliyunProductKey(String aliyunProductKey) { this.aliyunProductKey = aliyunProductKey; }
}
