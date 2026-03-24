package com.example.zhulong.employee.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class EmployeeCreateRequest {
    @NotBlank @Size(min = 2, max = 20)
    private String username;
    @NotBlank @Size(min = 2, max = 10)
    private String empName;
    @NotNull
    private Integer gender;
    @NotBlank @Size(min = 11, max = 11)
    private String phone;
    private Long deptId;
    private String positionName;
    private BigDecimal salary;
    private String avatarUrl;
    private LocalDate entryDate;
    private List<EmployeeWorkHistoryItem> workHistories;

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
    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }
    public List<EmployeeWorkHistoryItem> getWorkHistories() { return workHistories; }
    public void setWorkHistories(List<EmployeeWorkHistoryItem> workHistories) { this.workHistories = workHistories; }
}

