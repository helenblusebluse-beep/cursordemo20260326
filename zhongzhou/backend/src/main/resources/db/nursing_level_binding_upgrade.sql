SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP TABLE IF EXISTS zz_nursing_level_plan_bind;

CREATE TABLE zz_nursing_level_plan_bind (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  care_level_name VARCHAR(32) NOT NULL COMMENT '护理等级名称(来自入住办理care_level)',
  plan_id BIGINT UNSIGNED NOT NULL COMMENT '护理计划ID',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_level_plan_active (care_level_name, plan_id, is_deleted),
  KEY idx_level_plan_level (care_level_name),
  KEY idx_level_plan_plan (plan_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理等级-护理计划绑定表';

INSERT INTO zz_nursing_level_plan_bind (care_level_name, plan_id, is_deleted, created_time) VALUES
('特级护理等级', 1, 0, '2048-10-10 10:00:00'),
('特级护理等级', 3, 0, '2048-10-10 10:00:00'),
('一级护理等级', 2, 0, '2048-10-10 10:00:00'),
('二级护理等级', 3, 0, '2048-10-10 10:00:00'),
('三级护理等级', 4, 0, '2048-10-10 10:00:00');

UPDATE zz_nursing_plan SET bind_level_count = 0 WHERE 1=1;
