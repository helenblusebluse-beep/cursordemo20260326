package com.zhongzhou.modules.nursing.vo;

import java.util.List;

public class NursingCareLevelVO {
    private String levelName;
    private List<Long> planIds;

    public String getLevelName() { return levelName; }
    public void setLevelName(String levelName) { this.levelName = levelName; }
    public List<Long> getPlanIds() { return planIds; }
    public void setPlanIds(List<Long> planIds) { this.planIds = planIds; }
}
