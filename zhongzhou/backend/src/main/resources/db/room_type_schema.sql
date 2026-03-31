SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP TABLE IF EXISTS zz_room;
DROP TABLE IF EXISTS zz_room_type;

CREATE TABLE zz_room_type (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  room_name VARCHAR(32) NOT NULL COMMENT '房间名称(业务上限10字)',
  bed_count INT NOT NULL COMMENT '床位数量',
  bed_fee DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '床位费用元/月',
  introduction VARCHAR(50) NOT NULL COMMENT '房型介绍',
  image_urls TEXT DEFAULT NULL COMMENT '房间图片URL，JSON数组字符串',
  remarks VARCHAR(200) DEFAULT NULL COMMENT '备注',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '1启用 2禁用',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_room_type_name (room_name),
  KEY idx_room_type_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房型设置';

CREATE TABLE zz_room (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  room_type_id BIGINT UNSIGNED NOT NULL COMMENT '房型ID',
  room_code VARCHAR(32) NOT NULL COMMENT '房间编号',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_room_type_id (room_type_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房间实例（判断房型是否可编辑/删除）';

INSERT INTO zz_room_type
(id, room_name, bed_count, bed_fee, introduction, image_urls, remarks, status, is_deleted)
VALUES
(1,'四人间',4,800.00,'房间内设有24小时cctv监控','["https://dummyimage.com/80x60/409eff/fff.png"]',NULL,1,0),
(2,'三人间',3,1100.00,'房间内设有24小时cctv监控','["https://dummyimage.com/80x60/67c23a/fff.png"]',NULL,1,0),
(3,'双人间',2,1200.00,'房间内设有24小时cctv监控','["https://dummyimage.com/80x60/e6a23c/fff.png"]',NULL,1,0),
(4,'普通单人间',1,1400.00,'房间内设有24小时cctv监控','["https://dummyimage.com/80x60/f56c6c/fff.png"]',NULL,2,0),
(5,'特护房',2,2200.00,'房间内设有24小时cctv监控','["https://dummyimage.com/80x60/909399/fff.png"]',NULL,1,0),
(6,'豪华单人间',1,2600.00,'房间内设有24小时cctv监控','["https://dummyimage.com/80x60/546fc6/fff.png"]',NULL,1,0),
(7,'标准双人间',2,1300.00,'房间内设有24小时cctv监控','["https://dummyimage.com/80x60/8ec96b/fff.png"]',NULL,1,0),
(8,'经济双人间',2,900.00,'房间内设有24小时cctv监控','["https://dummyimage.com/80x60/c0c4cc/fff.png"]',NULL,1,0),
(9,'VIP套间',1,3500.00,'房间内设有24小时cctv监控','["https://dummyimage.com/80x60/dcadc9/fff.png"]',NULL,1,0),
(10,'豪华三人间',3,2700.00,'房间内设有24小时cctv监控','["https://dummyimage.com/80x60/303133/fff.png"]',NULL,2,0);

UPDATE zz_room_type SET created_time='2048-10-10 15:00:00', updated_time='2048-10-10 15:00:00' WHERE id BETWEEN 1 AND 10;

INSERT INTO zz_room (room_type_id, room_code, is_deleted) VALUES
(1,'A101',0),
(2,'B201',0),
(3,'C301',0);
