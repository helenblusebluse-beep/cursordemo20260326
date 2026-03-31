package com.zhongzhou.modules.bedpreview.dto;

import javax.validation.constraints.*;

public class BedSaveRequest {
    @NotBlank(message = "床位名称不能为空")
    @Size(max = 10, message = "床位名称最多10个字符")
    private String bedName;

    @NotNull(message = "排序不能为空")
    private Integer sortOrder;

    public String getBedName() {
        return bedName;
    }

    public void setBedName(String bedName) {
        this.bedName = bedName;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
