package com.zhongzhou.modules.smartbed.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("zz_room_env")
public class RoomEnv {
    @TableId
    private Long roomId;
    private Integer doorOpen;
    private BigDecimal temperatureC;
    private Integer humidityPct;
    private Integer alarmNormal;
    private Integer dataAnomaly;
    private Integer isDeleted;
    private LocalDateTime updatedTime;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Integer getDoorOpen() {
        return doorOpen;
    }

    public void setDoorOpen(Integer doorOpen) {
        this.doorOpen = doorOpen;
    }

    public BigDecimal getTemperatureC() {
        return temperatureC;
    }

    public void setTemperatureC(BigDecimal temperatureC) {
        this.temperatureC = temperatureC;
    }

    public Integer getHumidityPct() {
        return humidityPct;
    }

    public void setHumidityPct(Integer humidityPct) {
        this.humidityPct = humidityPct;
    }

    public Integer getAlarmNormal() {
        return alarmNormal;
    }

    public void setAlarmNormal(Integer alarmNormal) {
        this.alarmNormal = alarmNormal;
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
