package com.zhongzhou.modules.checkin.vo;

public class CheckinApplicationVO {
    private Long id;
    private String elderName;
    private String idCard;
    private String roomNo;
    private String careLevel;
    private String checkinPeriod;
    private String createdTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getElderName() { return elderName; }
    public void setElderName(String elderName) { this.elderName = elderName; }
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
    public String getCareLevel() { return careLevel; }
    public void setCareLevel(String careLevel) { this.careLevel = careLevel; }
    public String getCheckinPeriod() { return checkinPeriod; }
    public void setCheckinPeriod(String checkinPeriod) { this.checkinPeriod = checkinPeriod; }
    public String getCreatedTime() { return createdTime; }
    public void setCreatedTime(String createdTime) { this.createdTime = createdTime; }
}
