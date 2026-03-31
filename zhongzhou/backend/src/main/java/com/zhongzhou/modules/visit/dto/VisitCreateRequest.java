package com.zhongzhou.modules.visit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class VisitCreateRequest {
    @NotNull(message = "来访类型不能为空")
    private Integer reserveType;

    @NotBlank(message = "来访人姓名不能为空")
    @Size(max = 10, message = "来访人姓名最多10个字符")
    private String visitorName;

    @NotBlank(message = "来访人手机号不能为空")
    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式错误，请重新输入")
    private String visitorPhone;

    @NotBlank(message = "老人姓名不能为空")
    @Size(max = 10, message = "老人姓名最多10个字符")
    private String elderName;

    @NotNull(message = "来访时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkinTime;

    public Integer getReserveType() {
        return reserveType;
    }

    public void setReserveType(Integer reserveType) {
        this.reserveType = reserveType;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getVisitorPhone() {
        return visitorPhone;
    }

    public void setVisitorPhone(String visitorPhone) {
        this.visitorPhone = visitorPhone;
    }

    public String getElderName() {
        return elderName;
    }

    public void setElderName(String elderName) {
        this.elderName = elderName;
    }

    public LocalDateTime getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(LocalDateTime checkinTime) {
        this.checkinTime = checkinTime;
    }
}
