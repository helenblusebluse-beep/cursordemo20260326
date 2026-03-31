package com.zhongzhou.modules.nursing.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public class NursingLevelBindRequest {
    @NotNull(message = "绑定计划不能为空")
    private List<Long> planIds;

    public List<Long> getPlanIds() { return planIds; }
    public void setPlanIds(List<Long> planIds) { this.planIds = planIds; }
}
