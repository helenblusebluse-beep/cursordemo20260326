package com.example.zhulong.employee;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sys_employee_log")
@Data
public class EmployeeOperateLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emp_id", nullable = false)
    private Long empId;

    @Column(name = "operate_type", nullable = false, length = 20)
    private String operateType;

    @Column(name = "operator_emp_id")
    private Long operatorEmpId;

    @Column(name = "operate_time", nullable = false)
    private LocalDateTime operateTime = LocalDateTime.now();

    @Column(length = 255)
    private String remark;
}
