package com.zhongzhou.modules.nursing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("zz_nursing_level_plan_bind")
public class NursingLevelPlanBind {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String careLevelName;
    private Long planId;
    private Integer isDeleted;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCareLevelName() { return careLevelName; }
    public void setCareLevelName(String careLevelName) { this.careLevelName = careLevelName; }
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public Integer getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }
}
