package com.example.zhulong.employee;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sys_employee")
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(name = "emp_name", nullable = false, length = 10)
    private String empName;

    @Column(nullable = false)
    private Integer gender;

    @Column(nullable = false, length = 11)
    private String phone;

    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "position_name", length = 20)
    private String positionName;

    @Column
    private BigDecimal salary;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Column(name = "entry_date")
    private LocalDate entryDate;

    @Column(name = "init_password", length = 100)
    private String initPassword;

    @Column(name = "last_operate_time", nullable = false)
    private LocalDateTime lastOperateTime = LocalDateTime.now();

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime = LocalDateTime.now();
}

