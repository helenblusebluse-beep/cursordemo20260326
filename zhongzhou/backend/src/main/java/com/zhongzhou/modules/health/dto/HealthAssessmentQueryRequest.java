package com.zhongzhou.modules.health.dto;

public class HealthAssessmentQueryRequest {
    private String elderName;
    private String idCard;
    private Integer livingStatus;
    private int page = 1;
    private int pageSize = 10;

    public String getElderName() { return elderName; }
    public void setElderName(String elderName) { this.elderName = elderName; }
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public Integer getLivingStatus() { return livingStatus; }
    public void setLivingStatus(Integer livingStatus) { this.livingStatus = livingStatus; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}
