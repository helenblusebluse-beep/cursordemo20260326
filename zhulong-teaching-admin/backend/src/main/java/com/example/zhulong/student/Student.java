package com.example.zhulong.student;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "edu_student")
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_name", nullable = false, length = 10)
    private String studentName;

    @Column(name = "student_no", nullable = false, length = 10)
    private String studentNo;

    @Column(nullable = false)
    private Integer gender;

    @Column(nullable = false, length = 11)
    private String phone;

    @Column(name = "id_card_no", nullable = false, length = 18)
    private String idCardNo;

    @Column(name = "is_college_student", nullable = false)
    private Boolean isCollegeStudent = false;

    @Column(length = 100)
    private String address;

    @Column(length = 10)
    private String education;

    @Column(name = "graduate_date")
    private LocalDate graduateDate;

    @Column(name = "class_id")
    private Long classId;

    @Column(name = "demerit_count", nullable = false)
    private Integer demeritCount = 0;

    @Column(name = "demerit_score", nullable = false)
    private Integer demeritScore = 0;

    @Column(name = "last_operate_time", nullable = false)
    private LocalDateTime lastOperateTime = LocalDateTime.now();

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime = LocalDateTime.now();
}

