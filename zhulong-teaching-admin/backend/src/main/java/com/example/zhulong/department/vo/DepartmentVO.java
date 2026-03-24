package com.example.zhulong.department.vo;

public class DepartmentVO {
    private Long id;
    private String deptName;
    private String lastOperateTime;

    public DepartmentVO() {
    }

    public DepartmentVO(Long id, String deptName, String lastOperateTime) {
        this.id = id;
        this.deptName = deptName;
        this.lastOperateTime = lastOperateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getLastOperateTime() {
        return lastOperateTime;
    }

    public void setLastOperateTime(String lastOperateTime) {
        this.lastOperateTime = lastOperateTime;
    }
}

