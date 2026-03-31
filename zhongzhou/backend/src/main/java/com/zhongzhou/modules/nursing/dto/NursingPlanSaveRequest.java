package com.zhongzhou.modules.nursing.dto;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class NursingPlanSaveRequest {
    @NotBlank(message = "护理计划名称不能为空")
    @Size(max = 10, message = "护理计划名称最多10个字符")
    private String planName;

    @NotNull(message = "状态不能为空")
    private Integer status;

    @NotNull(message = "排序不能为空")
    @Min(value = 1, message = "排序须为正整数")
    @Max(value = 9999, message = "排序超出范围")
    private Integer sortOrder;

    @Valid
    private List<NursingPlanItemRequest> items;

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public List<NursingPlanItemRequest> getItems() { return items; }
    public void setItems(List<NursingPlanItemRequest> items) { this.items = items; }
}
