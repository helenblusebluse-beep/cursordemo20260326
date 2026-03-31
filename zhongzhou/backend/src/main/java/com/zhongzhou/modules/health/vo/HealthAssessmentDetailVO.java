package com.zhongzhou.modules.health.vo;

import java.util.List;

public class HealthAssessmentDetailVO {
    private Long id;
    private String elderName;
    private String idCard;
    private String examOrg;
    private String reportFileName;
    private Long reportFileSize;
    private String reportFileUrl;
    private Double healthScore;
    private String suggestion;
    private String careLevel;
    private String riskLevel;
    private String reportSummary;
    private String aiAdvice;
    private Integer livingStatus;
    private String livingStatusLabel;
    private String assessedTime;
    private String birthDate;
    private Integer age;
    private String gender;
    private String summaryDate;
    private List<SystemScoreVO> systemScores;
    private List<AgeDistributionVO> ageDistributions;
    private List<AbnormalItemVO> abnormalItems;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getElderName() { return elderName; }
    public void setElderName(String elderName) { this.elderName = elderName; }
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public String getExamOrg() { return examOrg; }
    public void setExamOrg(String examOrg) { this.examOrg = examOrg; }
    public String getReportFileName() { return reportFileName; }
    public void setReportFileName(String reportFileName) { this.reportFileName = reportFileName; }
    public Long getReportFileSize() { return reportFileSize; }
    public void setReportFileSize(Long reportFileSize) { this.reportFileSize = reportFileSize; }
    public String getReportFileUrl() { return reportFileUrl; }
    public void setReportFileUrl(String reportFileUrl) { this.reportFileUrl = reportFileUrl; }
    public Double getHealthScore() { return healthScore; }
    public void setHealthScore(Double healthScore) { this.healthScore = healthScore; }
    public String getSuggestion() { return suggestion; }
    public void setSuggestion(String suggestion) { this.suggestion = suggestion; }
    public String getCareLevel() { return careLevel; }
    public void setCareLevel(String careLevel) { this.careLevel = careLevel; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getReportSummary() { return reportSummary; }
    public void setReportSummary(String reportSummary) { this.reportSummary = reportSummary; }
    public String getAiAdvice() { return aiAdvice; }
    public void setAiAdvice(String aiAdvice) { this.aiAdvice = aiAdvice; }
    public Integer getLivingStatus() { return livingStatus; }
    public void setLivingStatus(Integer livingStatus) { this.livingStatus = livingStatus; }
    public String getLivingStatusLabel() { return livingStatusLabel; }
    public void setLivingStatusLabel(String livingStatusLabel) { this.livingStatusLabel = livingStatusLabel; }
    public String getAssessedTime() { return assessedTime; }
    public void setAssessedTime(String assessedTime) { this.assessedTime = assessedTime; }
    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getSummaryDate() { return summaryDate; }
    public void setSummaryDate(String summaryDate) { this.summaryDate = summaryDate; }
    public List<SystemScoreVO> getSystemScores() { return systemScores; }
    public void setSystemScores(List<SystemScoreVO> systemScores) { this.systemScores = systemScores; }
    public List<AgeDistributionVO> getAgeDistributions() { return ageDistributions; }
    public void setAgeDistributions(List<AgeDistributionVO> ageDistributions) { this.ageDistributions = ageDistributions; }
    public List<AbnormalItemVO> getAbnormalItems() { return abnormalItems; }
    public void setAbnormalItems(List<AbnormalItemVO> abnormalItems) { this.abnormalItems = abnormalItems; }

    public static class SystemScoreVO {
        private String systemName;
        private Double score;
        public String getSystemName() { return systemName; }
        public void setSystemName(String systemName) { this.systemName = systemName; }
        public Double getScore() { return score; }
        public void setScore(Double score) { this.score = score; }
    }

    public static class AgeDistributionVO {
        private String ageGroup;
        private Double healthy;
        private Double warning;
        private Double risk;
        private Double severe;
        private Double critical;
        public String getAgeGroup() { return ageGroup; }
        public void setAgeGroup(String ageGroup) { this.ageGroup = ageGroup; }
        public Double getHealthy() { return healthy; }
        public void setHealthy(Double healthy) { this.healthy = healthy; }
        public Double getWarning() { return warning; }
        public void setWarning(Double warning) { this.warning = warning; }
        public Double getRisk() { return risk; }
        public void setRisk(Double risk) { this.risk = risk; }
        public Double getSevere() { return severe; }
        public void setSevere(Double severe) { this.severe = severe; }
        public Double getCritical() { return critical; }
        public void setCritical(Double critical) { this.critical = critical; }
    }

    public static class AbnormalItemVO {
        private String conclusion;
        private String itemName;
        private String resultValue;
        private String direction;
        private String refRange;
        private String unit;
        public String getConclusion() { return conclusion; }
        public void setConclusion(String conclusion) { this.conclusion = conclusion; }
        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
        public String getResultValue() { return resultValue; }
        public void setResultValue(String resultValue) { this.resultValue = resultValue; }
        public String getDirection() { return direction; }
        public void setDirection(String direction) { this.direction = direction; }
        public String getRefRange() { return refRange; }
        public void setRefRange(String refRange) { this.refRange = refRange; }
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
    }
}
