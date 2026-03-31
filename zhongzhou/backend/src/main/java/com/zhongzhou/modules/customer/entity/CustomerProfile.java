package com.zhongzhou.modules.customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("zz_customer_profile")
public class CustomerProfile {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String customerNickname;
    private String customerPhone;
    private Integer orderTrackCount;
    private LocalDateTime firstLoginTime;
    private Integer isDeleted;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCustomerNickname() { return customerNickname; }
    public void setCustomerNickname(String customerNickname) { this.customerNickname = customerNickname; }
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public Integer getOrderTrackCount() { return orderTrackCount; }
    public void setOrderTrackCount(Integer orderTrackCount) { this.orderTrackCount = orderTrackCount; }
    public LocalDateTime getFirstLoginTime() { return firstLoginTime; }
    public void setFirstLoginTime(LocalDateTime firstLoginTime) { this.firstLoginTime = firstLoginTime; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }
}
