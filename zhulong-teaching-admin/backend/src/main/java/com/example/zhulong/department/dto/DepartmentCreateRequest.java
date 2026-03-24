package com.example.zhulong.department.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class DepartmentCreateRequest {
    @NotBlank(message = "部门名称不能为空")
    @Size(min = 2, max = 10, message = "部门名称长度必须为2-10位")
    private String deptName;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}

