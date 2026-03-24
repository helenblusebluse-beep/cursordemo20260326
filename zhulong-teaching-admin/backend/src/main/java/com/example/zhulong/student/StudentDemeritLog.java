package com.example.zhulong.student;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "edu_student_demerit_log")
@Data
public class StudentDemeritLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private Integer score;

    @Column(length = 255)
    private String remark;

    @Column(name = "operator_emp_id")
    private Long operatorEmpId;

    @Column(name = "operate_time", nullable = false)
    private LocalDateTime operateTime = LocalDateTime.now();
}

