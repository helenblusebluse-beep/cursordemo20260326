package com.zhongzhou.modules.health.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("zz_health_assessment")
public class HealthAssessment {
    @TableId(type = IdType.AUTO)
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
    private Integer livingStatus;
    private LocalDateTime assessedTime;
    private Integer isDeleted;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

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
    public Integer getLivingStatus() { return livingStatus; }
    public void setLivingStatus(Integer livingStatus) { this.livingStatus = livingStatus; }
    public LocalDateTime getAssessedTime() { return assessedTime; }
    public void setAssessedTime(LocalDateTime assessedTime) { this.assessedTime = assessedTime; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }
}
