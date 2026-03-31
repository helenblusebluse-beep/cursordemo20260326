package com.zhongzhou.modules.smartbed.vo;

import java.util.ArrayList;
import java.util.List;

public class SmartRoomVO {
    private Long roomId;
    private String roomNo;
    private List<SmartDeviceItemVO> roomDevices = new ArrayList<>();
    private String doorStatus;
    private String temperature;
    private String humidity;
    private String alarmStatus;
    /** 房间级占位：无床位或床位无设备 */
    private String roomMessage;
    private List<SmartBedCardVO> beds = new ArrayList<>();

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public List<SmartDeviceItemVO> getRoomDevices() {
        return roomDevices;
    }

    public void setRoomDevices(List<SmartDeviceItemVO> roomDevices) {
        this.roomDevices = roomDevices;
    }

    public String getDoorStatus() {
        return doorStatus;
    }

    public void setDoorStatus(String doorStatus) {
        this.doorStatus = doorStatus;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(String alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public String getRoomMessage() {
        return roomMessage;
    }

    public void setRoomMessage(String roomMessage) {
        this.roomMessage = roomMessage;
    }

    public List<SmartBedCardVO> getBeds() {
        return beds;
    }

    public void setBeds(List<SmartBedCardVO> beds) {
        this.beds = beds;
    }
}
