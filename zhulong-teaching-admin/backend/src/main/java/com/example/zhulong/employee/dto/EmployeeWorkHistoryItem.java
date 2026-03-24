package com.example.zhulong.employee.dto;

import java.time.LocalDate;

public class EmployeeWorkHistoryItem {
    private LocalDate startDate;
    private LocalDate endDate;
    private String companyName;
    private String positionName;

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getPositionName() { return positionName; }
    public void setPositionName(String positionName) { this.positionName = positionName; }
}

