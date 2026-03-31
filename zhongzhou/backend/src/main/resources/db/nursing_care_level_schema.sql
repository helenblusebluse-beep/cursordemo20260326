SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP TABLE IF EXISTS zz_nursing_care_level;

CREATE TABLE zz_nursing_care_level (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  level_name VARCHAR(10) NOT NULL COMMENT '等级名称',
  plan_id BIGINT UNSIGNED NOT NULL COMMENT '护理计划ID',
  plan_name VARCHAR(32) NOT NULL COMMENT '护理计划名称快照',
  nursing_fee DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '护理费用(元/月)',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1启用 2禁用',
  level_desc VARCHAR(50) NOT NULL DEFAULT '' COMMENT '等级说明',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_nursing_care_level_name (level_name),
  KEY idx_nursing_care_level_plan (plan_id),
  KEY idx_nursing_care_level_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理等级';

INSERT INTO zz_nursing_care_level
(id, level_name, plan_id, plan_name, nursing_fee, status, level_desc, is_deleted, created_time, updated_time)
VALUES
(1, '特级护理等级', 1, '特级护理等级计划', 2000.00, 1, '特级护理等级适用于无法独立完成生活起居的老人', 0, '2048-10-10 15:00:00', '2048-10-10 15:00:00'),
(2, '一级护理等级', 2, '一级护理等级计划', 1500.00, 1, '一级护理等级适用于无法独立完成部分日常活动的老人', 0, '2048-10-09 15:00:00', '2048-10-09 15:00:00'),
(3, '二级护理等级', 3, '二级护理等级计划', 1200.00, 1, '二级护理等级适用于无法独立完成少量活动的老人', 0, '2048-10-08 15:00:00', '2048-10-08 15:00:00'),
(4, '三级护理等级', 4, '三级护理等级计划', 1000.00, 2, '三级护理等级适用于无法独立完成少量家务的老人', 0, '2048-10-07 15:00:00', '2048-10-07 15:00:00');
