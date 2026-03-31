package com.zhongzhou.modules.smartbed.vo;

import java.util.ArrayList;
import java.util.List;

public class SmartBedCardVO {
    private Long bedId;
    private String bedName;
    private String elderName;
    private List<SmartDeviceItemVO> devices = new ArrayList<>();
    /** 清醒中 / 睡眠中 / 已离床，空床或无数据时可为 null */
    private String presenceLabel;
    private Integer heartRate;
    private Integer breathRate;
    private Integer leftBedCount;
    private String leftBedTime;
    /** 空态文案 */
    private String message;
    /** 告警时整张卡片红底 */
    private boolean alarm;
    private boolean showHeartBreath;
    private boolean showLeftBed;

    public Long getBedId() {
        return bedId;
    }

    public void setBedId(Long bedId) {
        this.bedId = bedId;
    }

    public String getBedName() {
        return bedName;
    }

    public void setBedName(String bedName) {
        this.bedName = bedName;
    }

    public String getElderName() {
        return elderName;
    }

    public void setElderName(String elderName) {
        this.elderName = elderName;
    }

    public List<SmartDeviceItemVO> getDevices() {
        return devices;
    }

    public void setDevices(List<SmartDeviceItemVO> devices) {
        this.devices = devices;
    }

    public String getPresenceLabel() {
        return presenceLabel;
    }

    public void setPresenceLabel(String presenceLabel) {
        this.presenceLabel = presenceLabel;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public Integer getBreathRate() {
        return breathRate;
    }

    public void setBreathRate(Integer breathRate) {
        this.breathRate = breathRate;
    }

    public Integer getLeftBedCount() {
        return leftBedCount;
    }

    public void setLeftBedCount(Integer leftBedCount) {
        this.leftBedCount = leftBedCount;
    }

    public String getLeftBedTime() {
        return leftBedTime;
    }

    public void setLeftBedTime(String leftBedTime) {
        this.leftBedTime = leftBedTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    public boolean isShowHeartBreath() {
        return showHeartBreath;
    }

    public void setShowHeartBreath(boolean showHeartBreath) {
        this.showHeartBreath = showHeartBreath;
    }

    public boolean isShowLeftBed() {
        return showLeftBed;
    }

    public void setShowLeftBed(boolean showLeftBed) {
        this.showLeftBed = showLeftBed;
    }
}
