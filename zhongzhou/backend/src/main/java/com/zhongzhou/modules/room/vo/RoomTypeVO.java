package com.zhongzhou.modules.room.vo;

import java.math.BigDecimal;

public class RoomTypeVO {
    private Long id;
    private String roomName;
    private Integer bedCount;
    private BigDecimal bedFee;
    private String introduction;
    private String imageUrlsJson;
    private String thumbUrl;
    private String createdTime;
    private Integer status;
    private Long associatedRoomCount;
    private String remarks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getBedCount() {
        return bedCount;
    }

    public void setBedCount(Integer bedCount) {
        this.bedCount = bedCount;
    }

    public String getImageUrlsJson() {
        return imageUrlsJson;
    }

    public void setImageUrlsJson(String imageUrlsJson) {
        this.imageUrlsJson = imageUrlsJson;
    }

    public BigDecimal getBedFee() {
        return bedFee;
    }

    public void setBedFee(BigDecimal bedFee) {
        this.bedFee = bedFee;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getAssociatedRoomCount() {
        return associatedRoomCount;
    }

    public void setAssociatedRoomCount(Long associatedRoomCount) {
        this.associatedRoomCount = associatedRoomCount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
