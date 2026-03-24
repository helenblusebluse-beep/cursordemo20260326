package com.example.zhulong.department.model;

import java.time.LocalDateTime;

public class DepartmentDO {
    private Long id;
    private String deptName;
    private LocalDateTime lastOperateTime;
    private Boolean isDeleted;
    private LocalDateTime createdTime;

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

    public LocalDateTime getLastOperateTime() {
        return lastOperateTime;
    }

    public void setLastOperateTime(LocalDateTime lastOperateTime) {
        this.lastOperateTime = lastOperateTime;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}

