package com.zhongzhou.modules.bill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("zz_bill_cancel_record")
public class BillCancelRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long billId;
    private String cancelBy;
    private String cancelUserType;
    private LocalDateTime cancelTime;
    private String cancelReason;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }
    public String getCancelBy() { return cancelBy; }
    public void setCancelBy(String cancelBy) { this.cancelBy = cancelBy; }
    public String getCancelUserType() { return cancelUserType; }
    public void setCancelUserType(String cancelUserType) { this.cancelUserType = cancelUserType; }
    public LocalDateTime getCancelTime() { return cancelTime; }
    public void setCancelTime(LocalDateTime cancelTime) { this.cancelTime = cancelTime; }
    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }
}
