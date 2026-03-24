package com.example.zhulong.student.vo;

public class StudentVO {
    private Long id;
    private String studentName;
    private String studentNo;
    private Integer gender;
    private String phone;
    private String idCardNo;
    private Boolean isCollegeStudent;
    private String address;
    private String education;
    private String graduateDate;
    private Long classId;
    /** 班级名称（由 classId 关联查询，仅展示用） */
    private String className;
    private Integer demeritCount;
    private Integer demeritScore;
    private String lastOperateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }
    public Integer getGender() { return gender; }
    public void setGender(Integer gender) { this.gender = gender; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getIdCardNo() { return idCardNo; }
    public void setIdCardNo(String idCardNo) { this.idCardNo = idCardNo; }
    public Boolean getIsCollegeStudent() { return isCollegeStudent; }
    public void setIsCollegeStudent(Boolean collegeStudent) { isCollegeStudent = collegeStudent; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
    public String getGraduateDate() { return graduateDate; }
    public void setGraduateDate(String graduateDate) { this.graduateDate = graduateDate; }
    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public Integer getDemeritCount() { return demeritCount; }
    public void setDemeritCount(Integer demeritCount) { this.demeritCount = demeritCount; }
    public Integer getDemeritScore() { return demeritScore; }
    public void setDemeritScore(Integer demeritScore) { this.demeritScore = demeritScore; }
    public String getLastOperateTime() { return lastOperateTime; }
    public void setLastOperateTime(String lastOperateTime) { this.lastOperateTime = lastOperateTime; }
}

