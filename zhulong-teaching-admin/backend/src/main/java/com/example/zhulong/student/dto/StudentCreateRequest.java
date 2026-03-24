package com.example.zhulong.student.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class StudentCreateRequest {
    @NotBlank @Size(min = 2, max = 10)
    private String studentName;
    @NotBlank @Size(min = 10, max = 10)
    private String studentNo;
    @NotNull
    private Integer gender;
    @NotBlank @Size(min = 11, max = 11)
    private String phone;
    @NotBlank @Size(min = 18, max = 18)
    private String idCardNo;
    @NotNull
    private Boolean isCollegeStudent;
    @Size(max = 100)
    private String address;
    private String education;
    private LocalDate graduateDate;
    private Long classId;

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
    public LocalDate getGraduateDate() { return graduateDate; }
    public void setGraduateDate(LocalDate graduateDate) { this.graduateDate = graduateDate; }
    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }
}

