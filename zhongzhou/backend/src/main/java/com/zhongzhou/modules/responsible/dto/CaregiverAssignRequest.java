package com.zhongzhou.modules.responsible.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CaregiverAssignRequest {
    @NotNull(message = "护理员姓名不能为空")
    private List<String> caregiverNames;

    public List<String> getCaregiverNames() { return caregiverNames; }
    public void setCaregiverNames(List<String> caregiverNames) { this.caregiverNames = caregiverNames; }
}
