SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP TABLE IF EXISTS zz_bed_caregiver_assign;
DROP TABLE IF EXISTS zz_caregiver_staff;

CREATE TABLE zz_caregiver_staff (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  staff_name VARCHAR(20) NOT NULL COMMENT '护理员姓名',
  role_type VARCHAR(20) NOT NULL DEFAULT '护理员' COMMENT '岗位',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1启用 2禁用',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_caregiver_name (staff_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理员字典';

CREATE TABLE zz_bed_caregiver_assign (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  bed_id BIGINT UNSIGNED NOT NULL COMMENT '床位ID(zz_floor_bed)',
  caregiver_name VARCHAR(20) NOT NULL COMMENT '护理员姓名',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_bed_caregiver_active (bed_id, caregiver_name, is_deleted),
  KEY idx_bed_caregiver_bed (bed_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='床位护理员分配';

INSERT INTO zz_caregiver_staff (id, staff_name, role_type, status, is_deleted, created_time) VALUES
(1, '盛明兰', '护理员', 1, 0, '2048-10-01 09:00:00'),
(2, '盛如兰', '护理员', 1, 0, '2048-10-01 09:00:00'),
(3, '盛华兰', '护理员', 1, 0, '2048-10-01 09:00:00'),
(4, '顾廷烨', '护理主管', 1, 0, '2048-10-01 09:00:00'),
(5, '盛长柏', '护理员', 1, 0, '2048-10-01 09:00:00');

INSERT INTO zz_bed_caregiver_assign (bed_id, caregiver_name, is_deleted, created_time) VALUES
(1, '顾廷烨', 0, '2048-10-10 10:00:00'),
(1, '盛明兰', 0, '2048-10-10 10:00:00'),
(1, '盛如兰', 0, '2048-10-10 10:00:00'),
(1, '盛华兰', 0, '2048-10-10 10:00:00'),
(2, '顾廷烨', 0, '2048-10-10 10:00:00'),
(2, '盛明兰', 0, '2048-10-10 10:00:00'),
(2, '盛如兰', 0, '2048-10-10 10:00:00'),
(3, '盛明兰', 0, '2048-10-10 10:00:00'),
(4, '盛华兰', 0, '2048-10-10 10:00:00');
