CREATE DATABASE IF NOT EXISTS `zhulong` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `zhulong`;

CREATE TABLE IF NOT EXISTS `sys_department` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dept_name` VARCHAR(10) NOT NULL COMMENT '部门名称(2-10位)',
  `last_operate_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后操作时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除:0否 1是',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dept_name_deleted` (`dept_name`, `is_deleted`),
  KEY `idx_last_operate_time` (`last_operate_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(20) NOT NULL,
  `password` VARCHAR(64) NOT NULL,
  `display_name` VARCHAR(20) NOT NULL,
  `role_name` VARCHAR(20) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1启用 0禁用',
  `last_operate_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0,
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_username_deleted` (`username`, `is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统登录用户表';

CREATE TABLE IF NOT EXISTS `sys_employee` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(20) NOT NULL COMMENT '用户名(2-20)',
  `emp_name` VARCHAR(10) NOT NULL COMMENT '姓名(2-10)',
  `gender` TINYINT NOT NULL COMMENT '性别:1男 2女',
  `phone` VARCHAR(11) NOT NULL COMMENT '手机号',
  `dept_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '所属部门ID',
  `position_name` VARCHAR(20) DEFAULT NULL COMMENT '职位',
  `salary` DECIMAL(10,2) DEFAULT NULL COMMENT '薪资',
  `avatar_url` VARCHAR(255) DEFAULT NULL COMMENT '头像地址',
  `entry_date` DATE DEFAULT NULL COMMENT '入职日期',
  `init_password` VARCHAR(100) DEFAULT NULL COMMENT '初始密码(可加密存储)',
  `last_operate_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0,
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username_deleted` (`username`, `is_deleted`),
  UNIQUE KEY `uk_phone_deleted` (`phone`, `is_deleted`),
  KEY `idx_emp_dept_id` (`dept_id`),
  KEY `idx_entry_date` (`entry_date`),
  KEY `idx_last_operate_time` (`last_operate_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

CREATE TABLE IF NOT EXISTS `sys_employee_work_history` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `emp_id` BIGINT UNSIGNED NOT NULL COMMENT '员工ID',
  `start_date` DATE DEFAULT NULL COMMENT '开始时间',
  `end_date` DATE DEFAULT NULL COMMENT '结束时间',
  `company_name` VARCHAR(50) DEFAULT NULL COMMENT '公司',
  `position_name` VARCHAR(50) DEFAULT NULL COMMENT '职位',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_work_emp_id` (`emp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工工作经历';

CREATE TABLE IF NOT EXISTS `edu_class` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `class_name` VARCHAR(30) NOT NULL COMMENT '班级名称',
  `classroom` VARCHAR(20) DEFAULT NULL COMMENT '班级教室',
  `start_date` DATE NOT NULL COMMENT '开课时间',
  `end_date` DATE NOT NULL COMMENT '结课时间',
  `head_teacher_emp_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '班主任员工ID',
  `subject_name` VARCHAR(20) NOT NULL COMMENT '学科',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态:0未开班 1在读 2结课',
  `last_operate_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0,
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_class_name_deleted` (`class_name`, `is_deleted`),
  KEY `idx_class_head_teacher` (`head_teacher_emp_id`),
  KEY `idx_start_date` (`start_date`),
  KEY `idx_last_operate_time` (`last_operate_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

CREATE TABLE IF NOT EXISTS `edu_student` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `student_name` VARCHAR(10) NOT NULL COMMENT '姓名',
  `student_no` VARCHAR(10) NOT NULL COMMENT '学号(固定10位)',
  `gender` TINYINT NOT NULL COMMENT '性别:1男 2女',
  `phone` VARCHAR(11) NOT NULL COMMENT '手机号',
  `id_card_no` VARCHAR(18) NOT NULL COMMENT '身份证号',
  `is_college_student` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否院校学员:0否 1是',
  `address` VARCHAR(100) DEFAULT NULL COMMENT '联系地址',
  `education` VARCHAR(10) DEFAULT NULL COMMENT '最高学历',
  `graduate_date` DATE DEFAULT NULL COMMENT '毕业时间',
  `class_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '所属班级ID',
  `demerit_count` INT NOT NULL DEFAULT 0 COMMENT '违纪次数',
  `demerit_score` INT NOT NULL DEFAULT 0 COMMENT '违纪扣分累计',
  `last_operate_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0,
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_no_deleted` (`student_no`, `is_deleted`),
  UNIQUE KEY `uk_student_phone_deleted` (`phone`, `is_deleted`),
  UNIQUE KEY `uk_id_card_deleted` (`id_card_no`, `is_deleted`),
  KEY `idx_student_class_id` (`class_id`),
  KEY `idx_education` (`education`),
  KEY `idx_last_operate_time` (`last_operate_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学员表';

CREATE TABLE IF NOT EXISTS `edu_student_demerit_log` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `student_id` BIGINT UNSIGNED NOT NULL,
  `score` INT NOT NULL COMMENT '本次违纪扣分',
  `remark` VARCHAR(255) DEFAULT NULL,
  `operator_emp_id` BIGINT UNSIGNED DEFAULT NULL,
  `operate_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_demerit_student_id` (`student_id`),
  KEY `idx_operate_time` (`operate_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学员违纪处理日志';

CREATE TABLE IF NOT EXISTS `sys_department_log` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `dept_id` BIGINT UNSIGNED NOT NULL,
  `operate_type` VARCHAR(20) NOT NULL COMMENT 'ADD/UPDATE/DELETE/ENABLE/DISABLE',
  `before_name` VARCHAR(10) DEFAULT NULL,
  `after_name` VARCHAR(10) DEFAULT NULL,
  `operator_emp_id` BIGINT UNSIGNED DEFAULT NULL,
  `operate_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_dept_log_dept_id` (`dept_id`),
  KEY `idx_operate_time` (`operate_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门操作日志';

CREATE TABLE IF NOT EXISTS `edu_class_log` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `class_id` BIGINT UNSIGNED NOT NULL,
  `operate_type` VARCHAR(20) NOT NULL COMMENT 'ADD/UPDATE/DELETE',
  `operator_emp_id` BIGINT UNSIGNED DEFAULT NULL,
  `operate_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `remark` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_class_log_class_id` (`class_id`),
  KEY `idx_operate_time` (`operate_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级操作日志';

CREATE TABLE IF NOT EXISTS `sys_employee_log` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `emp_id` BIGINT UNSIGNED NOT NULL,
  `operate_type` VARCHAR(20) NOT NULL COMMENT 'ADD/UPDATE/DELETE',
  `operator_emp_id` BIGINT UNSIGNED DEFAULT NULL,
  `operate_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `remark` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_emp_log_emp_id` (`emp_id`),
  KEY `idx_operate_time` (`operate_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工操作日志';

CREATE TABLE IF NOT EXISTS `edu_student_log` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `student_id` BIGINT UNSIGNED NOT NULL,
  `operate_type` VARCHAR(20) NOT NULL COMMENT 'ADD/UPDATE/DELETE',
  `operator_emp_id` BIGINT UNSIGNED DEFAULT NULL,
  `operate_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `remark` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_student_log_student_id` (`student_id`),
  KEY `idx_operate_time` (`operate_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学员操作日志';

INSERT INTO `sys_department` (`dept_name`) VALUES
('学工部'), ('教研部'), ('教务部'), ('就业部')
ON DUPLICATE KEY UPDATE `last_operate_time` = CURRENT_TIMESTAMP;

INSERT INTO `sys_user` (`username`, `password`, `display_name`, `role_name`, `status`) VALUES
('admin', 'admin123', '系统管理员', '管理员', 1),
('user01', '123456', '测试用户01', '普通用户', 1),
('user02', '123456', '测试用户02', '普通用户', 1),
('user03', '123456', '测试用户03', '普通用户', 1),
('user04', '123456', '测试用户04', '普通用户', 1),
('user05', '123456', '测试用户05', '普通用户', 1),
('user06', '123456', '测试用户06', '普通用户', 1),
('user07', '123456', '测试用户07', '普通用户', 1),
('user08', '123456', '测试用户08', '普通用户', 1),
('user09', '123456', '测试用户09', '普通用户', 1),
('user10', '123456', '测试用户10', '普通用户', 1)
ON DUPLICATE KEY UPDATE
  `password` = VALUES(`password`),
  `display_name` = VALUES(`display_name`),
  `role_name` = VALUES(`role_name`),
  `status` = VALUES(`status`),
  `last_operate_time` = CURRENT_TIMESTAMP;

