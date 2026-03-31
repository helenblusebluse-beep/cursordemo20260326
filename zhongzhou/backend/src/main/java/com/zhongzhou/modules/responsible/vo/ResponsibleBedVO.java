package com.zhongzhou.modules.responsible.vo;

import java.util.List;

public class ResponsibleBedVO {
    private Long bedId;
    private String bedName;
    private String elderName;
    private List<String> caregiverNames;

    public Long getBedId() { return bedId; }
    public void setBedId(Long bedId) { this.bedId = bedId; }
    public String getBedName() { return bedName; }
    public void setBedName(String bedName) { this.bedName = bedName; }
    public String getElderName() { return elderName; }
    public void setElderName(String elderName) { this.elderName = elderName; }
    public List<String> getCaregiverNames() { return caregiverNames; }
    public void setCaregiverNames(List<String> caregiverNames) { this.caregiverNames = caregiverNames; }
}
