package com.zhongzhou.modules.taskschedule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("zz_task_refund_apply")
public class TaskRefundApply {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long taskId;
    private String elderName;
    private String refundReason;
    private Integer status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public String getElderName() { return elderName; }
    public void setElderName(String elderName) { this.elderName = elderName; }
    public String getRefundReason() { return refundReason; }
    public void setRefundReason(String refundReason) { this.refundReason = refundReason; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
