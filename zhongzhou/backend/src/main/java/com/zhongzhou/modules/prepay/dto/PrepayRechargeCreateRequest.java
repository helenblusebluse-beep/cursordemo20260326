package com.zhongzhou.modules.prepay.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class PrepayRechargeCreateRequest {
    @NotNull(message = "请选择老人")
    private Long elderId;
    @NotBlank(message = "请选择充值方式")
    private String rechargeMethod;
    @NotNull(message = "请输入充值金额")
    @DecimalMin(value = "0.01", message = "充值金额必须大于0")
    private BigDecimal rechargeAmount;
    @NotBlank(message = "请上传充值凭证")
    private String voucherFileName;
    @NotBlank(message = "请上传充值凭证")
    private String voucherFileUrl;
    @NotBlank(message = "请输入充值备注")
    @Size(max = 50, message = "充值备注最多50个字符")
    private String rechargeRemark;

    public Long getElderId() { return elderId; }
    public void setElderId(Long elderId) { this.elderId = elderId; }
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
}
