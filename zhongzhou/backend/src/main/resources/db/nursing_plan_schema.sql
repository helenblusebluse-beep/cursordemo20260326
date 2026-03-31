SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP TABLE IF EXISTS zz_nursing_plan_item;
DROP TABLE IF EXISTS zz_nursing_plan;

CREATE TABLE zz_nursing_plan (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  plan_name VARCHAR(10) NOT NULL COMMENT '护理计划名称',
  sort_order INT NOT NULL DEFAULT 1 COMMENT '排序',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1启用 2禁用',
  bind_level_count INT NOT NULL DEFAULT 0 COMMENT '绑定护理等级数量',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_nursing_plan_name (plan_name),
  KEY idx_nursing_plan_status (status),
  KEY idx_nursing_plan_sort (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理计划';

CREATE TABLE zz_nursing_plan_item (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  plan_id BIGINT UNSIGNED NOT NULL COMMENT '护理计划ID',
  row_no INT NOT NULL COMMENT '行号',
  nursing_item_id BIGINT UNSIGNED DEFAULT NULL COMMENT '护理项目ID',
  nursing_item_name VARCHAR(10) DEFAULT NULL COMMENT '护理项目名称快照',
  expected_service_time VARCHAR(5) NOT NULL DEFAULT '08:00' COMMENT '期望服务时间',
  execute_cycle VARCHAR(10) NOT NULL DEFAULT '每周' COMMENT '执行周期',
  execute_frequency INT NOT NULL DEFAULT 1 COMMENT '执行频次(1-7)',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_nursing_plan_item_plan (plan_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理计划明细';

INSERT INTO zz_nursing_plan
(id, plan_name, sort_order, status, bind_level_count, is_deleted, created_time)
VALUES
(1,'特级护理等级计划',1,1,0,0,'2048-10-10 15:00:00'),
(2,'一级护理等级计划',2,1,0,0,'2048-10-09 15:00:00'),
(3,'二级护理等级计划',3,1,2,0,'2048-10-08 15:00:00'),
(4,'三级护理等级计划',4,2,1,0,'2048-10-07 15:00:00');

INSERT INTO zz_nursing_plan_item
(plan_id, row_no, nursing_item_id, nursing_item_name, expected_service_time, execute_cycle, execute_frequency)
VALUES
(1,1,1,'剃须','08:00','每日',1),
(1,2,2,'喝浴','08:00','每日',1),
(1,3,3,'餐便运动','08:00','每周',3),
(2,1,4,'公羊空间清洁','08:00','每周',1),
(2,2,5,'洗头','08:00','每周',1),
(3,1,1,'剃须','08:00','每日',1),
(4,1,2,'喝浴','08:00','每月',2);
