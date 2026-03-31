package com.zhongzhou.modules.customer.vo;

public class CustomerManageItemVO {
    private Long id;
    private String customerNickname;
    private String customerPhone;
    /** 是 / 否 */
    private String signed;
    private Integer orderTrackCount;
    private String bindElderNames;
    private String firstLoginTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCustomerNickname() { return customerNickname; }
    public void setCustomerNickname(String customerNickname) { this.customerNickname = customerNickname; }
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public String getSigned() { return signed; }
    public void setSigned(String signed) { this.signed = signed; }
    public Integer getOrderTrackCount() { return orderTrackCount; }
    public void setOrderTrackCount(Integer orderTrackCount) { this.orderTrackCount = orderTrackCount; }
    public String getBindElderNames() { return bindElderNames; }
    public void setBindElderNames(String bindElderNames) { this.bindElderNames = bindElderNames; }
    public String getFirstLoginTime() { return firstLoginTime; }
    public void setFirstLoginTime(String firstLoginTime) { this.firstLoginTime = firstLoginTime; }
}
