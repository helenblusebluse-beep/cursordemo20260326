package com.zhongzhou.modules.health.vo;

public class HealthAssessmentVO {
    private Long id;
    private String elderName;
    private String idCard;
    private Double healthScore;
    private String suggestion;
    private String careLevel;
    private Integer livingStatus;
    private String livingStatusLabel;
    private String assessedTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getElderName() { return elderName; }
    public void setElderName(String elderName) { this.elderName = elderName; }
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public Double getHealthScore() { return healthScore; }
    public void setHealthScore(Double healthScore) { this.healthScore = healthScore; }
    public String getSuggestion() { return suggestion; }
    public void setSuggestion(String suggestion) { this.suggestion = suggestion; }
    public String getCareLevel() { return careLevel; }
    public void setCareLevel(String careLevel) { this.careLevel = careLevel; }
    public Integer getLivingStatus() { return livingStatus; }
    public void setLivingStatus(Integer livingStatus) { this.livingStatus = livingStatus; }
    public String getLivingStatusLabel() { return livingStatusLabel; }
    public void setLivingStatusLabel(String livingStatusLabel) { this.livingStatusLabel = livingStatusLabel; }
    public String getAssessedTime() { return assessedTime; }
    public void setAssessedTime(String assessedTime) { this.assessedTime = assessedTime; }
}
