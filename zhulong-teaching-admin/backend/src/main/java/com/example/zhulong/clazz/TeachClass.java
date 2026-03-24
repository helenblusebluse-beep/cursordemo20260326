package com.example.zhulong.clazz;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "edu_class")
@Data
public class TeachClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_name", nullable = false, length = 30)
    private String className;

    @Column(name = "classroom", length = 20)
    private String classroom;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "head_teacher_emp_id")
    private Long headTeacherEmpId;

    @Column(name = "subject_name", nullable = false, length = 20)
    private String subjectName;

    @Column(nullable = false)
    private Integer status;

    @Column(name = "last_operate_time", nullable = false)
    private LocalDateTime lastOperateTime = LocalDateTime.now();

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime = LocalDateTime.now();
}

