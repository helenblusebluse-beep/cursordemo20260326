package com.zhongzhou.modules.prepay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("zz_prepay_recharge")
public class PrepayRecharge {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String rechargeNo;
    private String elderName;
    private String elderIdNo;
    private String bedNo;
    private String rechargeMethod;
    private BigDecimal rechargeAmount;
    private String voucherFileName;
    private String voucherFileUrl;
    private String rechargeRemark;
    private LocalDateTime createdTime;
    private Integer isDeleted;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRechargeNo() { return rechargeNo; }
    public void setRechargeNo(String rechargeNo) { this.rechargeNo = rechargeNo; }
    public String getElderName() { return elderName; }
    public void setElderName(String elderName) { this.elderName = elderName; }
    public String getElderIdNo() { return elderIdNo; }
    public void setElderIdNo(String elderIdNo) { this.elderIdNo = elderIdNo; }
    public String getBedNo() { return bedNo; }
    public void setBedNo(String bedNo) { this.bedNo = bedNo; }
    public String getRechargeMethod() { return rechargeMethod; }
    public void setRechargeMethod(String rechargeMethod) { this.rechargeMethod = rechargeMethod; }
    public BigDecimal getRechargeAmount() { return rechargeAmount; }
    public void setRechargeAmount(BigDecimal rechargeAmount) { this.rechargeAmount = rechargeAmount; }
    public String getVoucherFileName() { return voucherFileName; }
    public void setVoucherFileName(String voucherFileName) { this.voucherFileName = voucherFileName; }
    public String getVoucherFileUrl() { return voucherFileUrl; }
    public void setVoucherFileUrl(String voucherFileUrl) { this.voucherFileUrl = voucherFileUrl; }
    public String getRechargeRemark() { return rechargeRemark; }
    public void setRechargeRemark(String rechargeRemark) { this.rechargeRemark = rechargeRemark; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
}
