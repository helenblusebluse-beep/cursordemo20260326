SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS zz_sys_profile (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  nickname VARCHAR(32) NOT NULL DEFAULT '' COMMENT '昵称/姓名',
  phone VARCHAR(20) NOT NULL DEFAULT '' COMMENT '手机号',
  email VARCHAR(64) NOT NULL DEFAULT '' COMMENT '邮箱',
  department VARCHAR(64) NOT NULL DEFAULT '' COMMENT '所属部门',
  position_title VARCHAR(64) NOT NULL DEFAULT '' COMMENT '所属职位',
  role_name VARCHAR(64) NOT NULL DEFAULT '' COMMENT '角色',
  gender TINYINT NOT NULL DEFAULT 1 COMMENT '1男 2女',
  avatar_url VARCHAR(255) NOT NULL DEFAULT '' COMMENT '头像地址',
  password VARCHAR(64) NOT NULL DEFAULT '' COMMENT '密码(演示明文)',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='个人中心-个人信息';

INSERT INTO zz_sys_profile
(id, nickname, phone, email, department, position_title, role_name, gender, avatar_url, password, created_time, is_deleted)
SELECT 1, 'momoChou', '13787788888', 'ry@163.com', '院长办公室', '护理部主任', '管理员', 1,
       'https://dummyimage.com/120x120/d9c59c/ffffff&text=%E5%A4%B4%E5%83%8F', 'Abcd1234',
       '2042-10-10 10:00:00', 0
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM zz_sys_profile WHERE id = 1);

