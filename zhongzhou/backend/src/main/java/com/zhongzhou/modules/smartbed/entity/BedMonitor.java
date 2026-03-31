package com.zhongzhou.modules.smartbed.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("zz_bed_monitor")
public class BedMonitor {
    @TableId
    private Long bedId;
    private String elderName;
    private Integer checkedOut;
    /** AWAKE SLEEP LEFT NONE */
    private String presence;
    private Integer heartRate;
    private Integer breathRate;
    private Integer leftBedCount;
    private String leftBedTime;
    private Integer noTslData;
    private Integer alarm;
    private Integer dataAnomaly;
    private Integer isDeleted;
    private LocalDateTime updatedTime;

    public Long getBedId() {
        return bedId;
    }

    public void setBedId(Long bedId) {
        this.bedId = bedId;
    }

    public String getElderName() {
        return elderName;
    }

    public void setElderName(String elderName) {
        this.elderName = elderName;
    }

    public Integer getCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(Integer checkedOut) {
        this.checkedOut = checkedOut;
    }

    public String getPresence() {
        return presence;
    }

    public void setPresence(String presence) {
        this.presence = presence;
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

    public Integer getNoTslData() {
        return noTslData;
    }

    public void setNoTslData(Integer noTslData) {
        this.noTslData = noTslData;
    }

    public Integer getAlarm() {
        return alarm;
    }

    public void setAlarm(Integer alarm) {
        this.alarm = alarm;
    }

    public Integer getDataAnomaly() {
        return dataAnomaly;
    }

    public void setDataAnomaly(Integer dataAnomaly) {
        this.dataAnomaly = dataAnomaly;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
