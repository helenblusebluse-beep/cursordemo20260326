package com.example.zhulong.user;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sys_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 64)
    private String password;

    @Column(name = "display_name", nullable = false, length = 20)
    private String displayName;

    @Column(name = "role_name", nullable = false, length = 20)
    private String roleName;

    @Column(nullable = false)
    private Integer status = 1;

    @Column(name = "last_operate_time", nullable = false)
    private LocalDateTime lastOperateTime = LocalDateTime.now();

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
