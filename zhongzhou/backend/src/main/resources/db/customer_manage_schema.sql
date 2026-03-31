SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP TABLE IF EXISTS zz_customer_profile;

CREATE TABLE zz_customer_profile (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  customer_nickname VARCHAR(32) NOT NULL COMMENT '客户昵称',
  customer_phone VARCHAR(16) NOT NULL COMMENT '客户手机号',
  order_track_count INT NOT NULL DEFAULT 0 COMMENT '跟踪下单次数',
  first_login_time DATETIME NOT NULL COMMENT '首次登录时间',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_customer_phone (customer_phone),
  KEY idx_first_login (first_login_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户管理-客户端用户';

-- 测试数据：同时覆盖签约(手机号命中丙方联系方式)与未签约场景
INSERT INTO zz_customer_profile
(customer_nickname, customer_phone, order_track_count, first_login_time, is_deleted)
VALUES
('赵刚','13899999999',4,'2048-10-10 15:00:00',0),
('范思哲','13788995678',5,'2048-10-09 15:00:00',0),
('苏若荷','13567678543',5,'2048-10-08 15:00:00',0),
('范理','13788995679',0,'2048-10-07 15:00:00',0),
('沈童','13567678544',1,'2048-10-06 15:00:00',0),
('二皇子','13788995680',2,'2048-10-05 15:00:00',0),
('陈平平','13567678545',3,'2048-10-04 15:00:00',0),
('林婉儿','13788995681',4,'2048-10-03 15:00:00',0),
('肖恩','13567678546',4,'2048-10-02 15:00:00',0),
('海棠朵朵','13566788888',4,'2048-10-01 15:00:00',0),
('签约客户A','13820000004',6,'2048-09-30 15:00:00',0),
('签约客户B','13820000001',7,'2048-09-29 15:00:00',0),
('签约客户C','13820000010',2,'2048-09-28 15:00:00',0);
