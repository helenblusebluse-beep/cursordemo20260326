package com.example.zhulong.clazz;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "edu_class_log")
@Data
public class ClassOperateLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_id", nullable = false)
    private Long classId;

    @Column(name = "operate_type", nullable = false, length = 20)
    private String operateType;

    @Column(name = "operator_emp_id")
    private Long operatorEmpId;

    @Column(name = "operate_time", nullable = false)
    private LocalDateTime operateTime = LocalDateTime.now();

    @Column(length = 255)
    private String remark;
}
