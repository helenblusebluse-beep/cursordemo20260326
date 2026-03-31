package com.zhongzhou.modules.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("zz_order_execute_record")
public class OrderExecuteRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private String executeBy;
    private String executeImageUrl;
    private String executeRecord;
    private LocalDateTime createdTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getExecuteBy() { return executeBy; }
    public void setExecuteBy(String executeBy) { this.executeBy = executeBy; }
    public String getExecuteImageUrl() { return executeImageUrl; }
    public void setExecuteImageUrl(String executeImageUrl) { this.executeImageUrl = executeImageUrl; }
    public String getExecuteRecord() { return executeRecord; }
    public void setExecuteRecord(String executeRecord) { this.executeRecord = executeRecord; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
}
