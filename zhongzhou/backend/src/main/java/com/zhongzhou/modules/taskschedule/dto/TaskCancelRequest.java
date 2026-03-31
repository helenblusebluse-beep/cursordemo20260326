package com.zhongzhou.modules.taskschedule.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TaskCancelRequest {
    @NotBlank(message = "取消原因不能为空")
    @Size(max = 100, message = "取消原因最多100个字符")
    private String cancelReason;

    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }
}
