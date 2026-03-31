package com.zhongzhou.modules.checkout.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CheckoutApplyRequest {
    @NotNull(message = "老人不能为空")
    @Min(value = 1, message = "老人不能为空")
    private Long checkinId;
    @NotNull(message = "退住日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkoutDate;
    @NotBlank(message = "退住原因不能为空")
    private String checkoutReason;
    @NotNull(message = "解除日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime agreementDate;
    @NotBlank(message = "解除协议不能为空")
    private String agreementFileName;

    public Long getCheckinId() { return checkinId; }
    public void setCheckinId(Long checkinId) { this.checkinId = checkinId; }
    public LocalDateTime getCheckoutDate() { return checkoutDate; }
    public void setCheckoutDate(LocalDateTime checkoutDate) { this.checkoutDate = checkoutDate; }
    public String getCheckoutReason() { return checkoutReason; }
    public void setCheckoutReason(String checkoutReason) { this.checkoutReason = checkoutReason; }
    public LocalDateTime getAgreementDate() { return agreementDate; }
    public void setAgreementDate(LocalDateTime agreementDate) { this.agreementDate = agreementDate; }
    public String getAgreementFileName() { return agreementFileName; }
    public void setAgreementFileName(String agreementFileName) { this.agreementFileName = agreementFileName; }
}
