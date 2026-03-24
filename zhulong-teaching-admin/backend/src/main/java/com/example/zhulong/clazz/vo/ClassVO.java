package com.example.zhulong.clazz.vo;

public class ClassVO {
    private Long id;
    private String className;
    private String classroom;
    private String startDate;
    private String endDate;
    private Long headTeacherEmpId;
    /** 班主任姓名（关联 sys_employee.emp_name） */
    private String headTeacherName;
    /** 在读学员人数（edu_student 中 class_id 且未删除） */
    private Long studentCount;
    private String subjectName;
    private Integer status;
    private String lastOperateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getClassroom() { return classroom; }
    public void setClassroom(String classroom) { this.classroom = classroom; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public Long getHeadTeacherEmpId() { return headTeacherEmpId; }
    public void setHeadTeacherEmpId(Long headTeacherEmpId) { this.headTeacherEmpId = headTeacherEmpId; }
    public String getHeadTeacherName() { return headTeacherName; }
    public void setHeadTeacherName(String headTeacherName) { this.headTeacherName = headTeacherName; }
    public Long getStudentCount() { return studentCount; }
    public void setStudentCount(Long studentCount) { this.studentCount = studentCount; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getLastOperateTime() { return lastOperateTime; }
    public void setLastOperateTime(String lastOperateTime) { this.lastOperateTime = lastOperateTime; }
}

