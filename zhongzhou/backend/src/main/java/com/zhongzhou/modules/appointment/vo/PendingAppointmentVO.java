package com.zhongzhou.modules.appointment.vo;

public class PendingAppointmentVO {
    private Long id;
    private String visitorName;
    private String visitorPhone;
    private String elderName;
    private String scheduledTime;
    private String reserveTypeLabel;
    private Integer reserveType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getVisitorPhone() {
        return visitorPhone;
    }

    public void setVisitorPhone(String visitorPhone) {
        this.visitorPhone = visitorPhone;
    }

    public String getElderName() {
        return elderName;
    }

    public void setElderName(String elderName) {
        this.elderName = elderName;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getReserveTypeLabel() {
        return reserveTypeLabel;
    }

    public void setReserveTypeLabel(String reserveTypeLabel) {
        this.reserveTypeLabel = reserveTypeLabel;
    }

    public Integer getReserveType() {
        return reserveType;
    }

    public void setReserveType(Integer reserveType) {
        this.reserveType = reserveType;
    }
}
