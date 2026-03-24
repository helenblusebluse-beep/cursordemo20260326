package com.example.zhulong.employee.vo;

import java.util.List;

public class EmployeeVO {
    private Long id;
    private String username;
    private String empName;
    private Integer gender;
    private String phone;
    private Long deptId;
    private String positionName;
    private String salary;
    private String avatarUrl;
    private String entryDate;
    private String lastOperateTime;
    private List<EmployeeWorkHistoryVO> workHistories;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmpName() { return empName; }
    public void setEmpName(String empName) { this.empName = empName; }
    public Integer getGender() { return gender; }
    public void setGender(Integer gender) { this.gender = gender; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public String getPositionName() { return positionName; }
    public void setPositionName(String positionName) { this.positionName = positionName; }
    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getEntryDate() { return entryDate; }
    public void setEntryDate(String entryDate) { this.entryDate = entryDate; }
    public String getLastOperateTime() { return lastOperateTime; }
    public void setLastOperateTime(String lastOperateTime) { this.lastOperateTime = lastOperateTime; }
    public List<EmployeeWorkHistoryVO> getWorkHistories() { return workHistories; }
    public void setWorkHistories(List<EmployeeWorkHistoryVO> workHistories) { this.workHistories = workHistories; }
}

