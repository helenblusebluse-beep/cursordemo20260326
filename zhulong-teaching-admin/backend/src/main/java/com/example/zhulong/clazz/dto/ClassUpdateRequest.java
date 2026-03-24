package com.example.zhulong.clazz.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class ClassUpdateRequest {
    @NotBlank
    private String className;
    private String classroom;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    private Long headTeacherEmpId;
    @NotBlank
    private String subjectName;

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getClassroom() { return classroom; }
    public void setClassroom(String classroom) { this.classroom = classroom; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Long getHeadTeacherEmpId() { return headTeacherEmpId; }
    public void setHeadTeacherEmpId(Long headTeacherEmpId) { this.headTeacherEmpId = headTeacherEmpId; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
}

