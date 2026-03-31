SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP TABLE IF EXISTS zz_floor_bed;
DROP TABLE IF EXISTS zz_floor_room;
DROP TABLE IF EXISTS zz_floor;

CREATE TABLE zz_floor (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  floor_name VARCHAR(5) NOT NULL COMMENT '楼层名称',
  sort_order INT NOT NULL DEFAULT 1 COMMENT '排序',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_floor_name (floor_name),
  KEY idx_floor_sort (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='床位预览-楼层';

CREATE TABLE zz_floor_room (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  floor_id BIGINT UNSIGNED NOT NULL COMMENT '楼层ID',
  room_no VARCHAR(5) NOT NULL COMMENT '房间号',
  room_type_id BIGINT UNSIGNED NOT NULL COMMENT '房型ID(zz_room_type)',
  sort_order INT NOT NULL DEFAULT 1 COMMENT '排序',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_floor_room_no (floor_id, room_no),
  KEY idx_floor_room_floor (floor_id),
  KEY idx_floor_room_type (room_type_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='床位预览-房间';

CREATE TABLE zz_floor_bed (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  room_id BIGINT UNSIGNED NOT NULL COMMENT '房间ID',
  bed_name VARCHAR(10) NOT NULL COMMENT '床位名称',
  sort_order INT NOT NULL DEFAULT 1 COMMENT '排序',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '1空闲 2已入住 3请假中',
  elder_name VARCHAR(32) DEFAULT NULL COMMENT '老人姓名(已入住/请假)',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_room_bed_name (room_id, bed_name),
  KEY idx_floor_bed_room (room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='床位预览-床位';

-- 测试数据：3 楼层 + 多房间 + 多床位状态（对齐原型示意）
INSERT INTO zz_floor (id, floor_name, sort_order, is_deleted) VALUES
(1,'1楼',1,0),
(2,'2楼',2,0),
(3,'3楼',3,0);

-- 1楼：101 双床(已入住)、107 空闲床位、108 请假、109 无床位
INSERT INTO zz_floor_room (id, floor_id, room_no, room_type_id, sort_order, is_deleted) VALUES
(1,1,'101',1,1,0),
(2,1,'102',2,2,0),
(3,1,'107',4,7,0),
(4,1,'108',4,8,0),
(5,1,'109',1,9,0);

INSERT INTO zz_floor_bed (room_id, bed_name, sort_order, status, elder_name, is_deleted) VALUES
(1,'1011',1,2,'高启强',0),
(1,'1012',2,2,'陈书婷',0),
(2,'1021',1,1,NULL,0),
(3,'1071',1,1,NULL,0),
(4,'1081',1,3,'李有田',0);

-- 2楼、3楼各一间房一张床（空闲）
INSERT INTO zz_floor_room (id, floor_id, room_no, room_type_id, sort_order, is_deleted) VALUES
(6,2,'201',3,1,0),
(7,3,'301',1,1,0);

INSERT INTO zz_floor_bed (room_id, bed_name, sort_order, status, elder_name, is_deleted) VALUES
(6,'2011',1,1,NULL,0),
(7,'3011',1,1,NULL,0);
