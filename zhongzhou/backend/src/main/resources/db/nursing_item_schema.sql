SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP TABLE IF EXISTS zz_nursing_item;

CREATE TABLE zz_nursing_item (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  item_name VARCHAR(10) NOT NULL COMMENT '名称(唯一,最多10字)',
  sort_order INT NOT NULL DEFAULT 1 COMMENT '排序号',
  unit VARCHAR(5) DEFAULT NULL COMMENT '单位(最多5字)',
  price DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '价格',
  image_name VARCHAR(128) NOT NULL COMMENT '图片名',
  image_url VARCHAR(500) DEFAULT NULL COMMENT '图片URL',
  nursing_requirement VARCHAR(50) DEFAULT NULL COMMENT '护理要求(最多50字)',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1启用 2禁用',
  bind_plan_count INT NOT NULL DEFAULT 0 COMMENT '已绑定护理计划数量',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_nursing_item_name (item_name),
  KEY idx_nursing_item_status (status),
  KEY idx_nursing_item_sort (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理项目';

-- 测试数据：覆盖启用/禁用、已绑定不可操作
INSERT INTO zz_nursing_item
(id,item_name,sort_order,unit,price,image_name,image_url,nursing_requirement,status,bind_plan_count,is_deleted,created_time)
VALUES
(1,'剃须',1,'次',20.00,'nursing-1.jpg','https://picsum.photos/seed/nursing-1/80/60','要时刻观察老人状态',1,0,0,'2048-10-10 15:00:00'),
(2,'喝浴',2,'次',50.00,'nursing-2.jpg','https://picsum.photos/seed/nursing-2/80/60','要时刻观察老人状态',1,0,0,'2048-10-08 15:00:00'),
(3,'餐便运动',3,'40分钟',100.00,'nursing-3.jpg','https://picsum.photos/seed/nursing-3/80/60','要时刻观察老人状态',2,2,0,'2048-10-07 15:00:00'),
(4,'公羊空间清洁',4,'次',40.00,'nursing-4.jpg','https://picsum.photos/seed/nursing-4/80/60','要时刻观察老人状态',1,0,0,'2048-10-07 15:00:00'),
(5,'洗头',5,'次',35.50,'nursing-5.jpg','https://picsum.photos/seed/nursing-5/80/60','要时刻观察老人状态',1,1,0,'2048-10-06 15:00:00');
