package com.zhongzhou.modules.nursing.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NursingCareLevelSaveRequest {
    @NotBlank(message = "等级名称不能为空")
    @Size(max = 10, message = "等级名称最多10个字符")
    private String levelName;

    @NotNull(message = "护理计划不能为空")
    private Long planId;

    @NotNull(message = "护理费用不能为空")
    @DecimalMin(value = "0.00", inclusive = true, message = "护理费用不能为负")
    private Double nursingFee;

    @NotNull(message = "状态不能为空")
    private Integer status;

    @NotBlank(message = "等级说明不能为空")
    @Size(max = 50, message = "等级说明最多50个字符")
    private String levelDesc;

    public String getLevelName() { return levelName; }
    public void setLevelName(String levelName) { this.levelName = levelName; }
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public Double getNursingFee() { return nursingFee; }
    public void setNursingFee(Double nursingFee) { this.nursingFee = nursingFee; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getLevelDesc() { return levelDesc; }
    public void setLevelDesc(String levelDesc) { this.levelDesc = levelDesc; }
}
