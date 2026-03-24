package com.example.zhulong.student.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class StudentDemeritRequest {
    @NotNull
    @Min(1)
    private Integer score;
    private String remark;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

