SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP TABLE IF EXISTS zz_smart_device;
DROP TABLE IF EXISTS zz_room_env;
DROP TABLE IF EXISTS zz_bed_monitor;

-- 设备绑定：接入位置关联 zz_floor_room / zz_floor_bed（依赖 bed_preview 已建表且有数据）
CREATE TABLE zz_smart_device (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  product_category VARCHAR(64) NOT NULL COMMENT '产品类别',
  device_name VARCHAR(128) NOT NULL COMMENT '设备名称',
  bind_type VARCHAR(8) NOT NULL COMMENT 'ROOM 房间级 BED 床位级',
  room_id BIGINT UNSIGNED NOT NULL COMMENT '房间ID zz_floor_room.id',
  bed_id BIGINT UNSIGNED DEFAULT NULL COMMENT '床位ID zz_floor_bed.id，房间级为NULL',
  icon_type VARCHAR(32) DEFAULT NULL COMMENT '前端图标类型',
  sort_order INT NOT NULL DEFAULT 0,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_dev_room (room_id),
  KEY idx_dev_bed (bed_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='智能监测-设备绑定';

CREATE TABLE zz_room_env (
  room_id BIGINT UNSIGNED NOT NULL COMMENT '房间ID',
  door_open TINYINT NOT NULL DEFAULT 1 COMMENT '1开启 0关闭',
  temperature_c DECIMAL(4,1) DEFAULT NULL COMMENT '温度℃',
  humidity_pct INT DEFAULT NULL COMMENT '湿度%',
  alarm_normal TINYINT NOT NULL DEFAULT 1 COMMENT '1正常 0异常',
  data_anomaly TINYINT NOT NULL DEFAULT 0 COMMENT '1数据异常(楼层红点)',
  is_deleted TINYINT NOT NULL DEFAULT 0,
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='智能监测-房间环境';

CREATE TABLE zz_bed_monitor (
  bed_id BIGINT UNSIGNED NOT NULL COMMENT '床位ID',
  elder_name VARCHAR(32) DEFAULT NULL COMMENT '老人姓名',
  checked_out TINYINT NOT NULL DEFAULT 0 COMMENT '1已退住不展示',
  presence VARCHAR(16) NOT NULL DEFAULT 'NONE' COMMENT 'AWAKE清醒 SLEEP睡眠 LEFT离床 NONE无老人',
  heart_rate INT DEFAULT NULL COMMENT '心率次/分',
  breath_rate INT DEFAULT NULL COMMENT '呼吸率次/分',
  left_bed_count INT DEFAULT NULL COMMENT '离床次数',
  left_bed_time VARCHAR(32) DEFAULT NULL COMMENT '离床时间',
  no_tsl_data TINYINT NOT NULL DEFAULT 0 COMMENT '1无物模型数据',
  alarm TINYINT NOT NULL DEFAULT 0 COMMENT '1告警红卡',
  data_anomaly TINYINT NOT NULL DEFAULT 0 COMMENT '1数据异常',
  is_deleted TINYINT NOT NULL DEFAULT 0,
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (bed_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='智能监测-床位实时';

-- 依赖 bed_preview_schema 已执行：zz_floor_room id 1=101,2=102,3=107,4=108,5=109,6=201,7=301；zz_floor_bed id 1-7 对应 1011/1012/1021/1071/1081/2011/3011
-- 本脚本追加床位 1014（空床演示）

INSERT INTO zz_floor_bed (room_id, bed_name, sort_order, status, elder_name, is_deleted) VALUES
(1, '1014', 3, 1, NULL, 0);

-- 房间 101：4 个房间级设备（原型左上设备图标）
INSERT INTO zz_smart_device (product_category, device_name, bind_type, room_id, bed_id, icon_type, sort_order, is_deleted) VALUES
('智能烟感', 'smoke alarm-01', 'ROOM', 1, NULL, 'smoke', 1, 0),
('智能门磁', 'door sensor-01', 'ROOM', 1, NULL, 'door', 2, 0),
('温湿度', 'temp-humi-01', 'ROOM', 1, NULL, 'temp', 3, 0),
('声光报警', 'alarm-01', 'ROOM', 1, NULL, 'alarm', 4, 0);

INSERT INTO zz_room_env (room_id, door_open, temperature_c, humidity_pct, alarm_normal, data_anomaly, is_deleted) VALUES
(1, 1, 26.0, 40, 1, 0, 0);

-- 床位 1011、1012：床位级设备（卡片右上四图标示意）
INSERT INTO zz_smart_device (product_category, device_name, bind_type, room_id, bed_id, icon_type, sort_order, is_deleted) VALUES
('身份识别', 'id-reader-1011', 'BED', 1, 1, 'id', 1, 0),
('智能手环', 'watch-1011', 'BED', 1, 1, 'wear', 2, 0),
('床垫传感器', 'mat-1011', 'BED', 1, 1, 'bed', 3, 0),
('跌倒告警', 'fall-1011', 'BED', 1, 1, 'alarm', 4, 0);

INSERT INTO zz_smart_device (product_category, device_name, bind_type, room_id, bed_id, icon_type, sort_order, is_deleted) VALUES
('身份识别', 'id-reader-1012', 'BED', 1, 2, 'id', 1, 0),
('智能手环', 'watch-1012', 'BED', 1, 2, 'wear', 2, 0),
('床垫传感器', 'mat-1012', 'BED', 1, 2, 'bed', 3, 0),
('跌倒告警', 'fall-1012', 'BED', 1, 2, 'alarm', 4, 0);

-- 1011 高启强 清醒 心率呼吸；1012 睡眠
INSERT INTO zz_bed_monitor (bed_id, elder_name, checked_out, presence, heart_rate, breath_rate, left_bed_count, left_bed_time, no_tsl_data, alarm, data_anomaly, is_deleted) VALUES
(1, '高启强', 0, 'AWAKE', 80, 20, NULL, NULL, 0, 0, 0, 0),
(2, '陈书婷', 0, 'SLEEP', 72, 18, NULL, NULL, 0, 0, 0, 0);

-- 1014：有床位设备、无老人（空床文案）
INSERT INTO zz_smart_device (product_category, device_name, bind_type, room_id, bed_id, icon_type, sort_order, is_deleted) VALUES
('身份识别', 'id-1014', 'BED', 1, 8, 'id', 1, 0),
('智能手环', 'watch-1014', 'BED', 1, 8, 'wear', 2, 0),
('床垫传感器', 'mat-1014', 'BED', 1, 8, 'bed', 3, 0),
('跌倒告警', 'fall-1014', 'BED', 1, 8, 'alarm', 4, 0);
INSERT INTO zz_bed_monitor (bed_id, elder_name, checked_out, presence, heart_rate, breath_rate, left_bed_count, left_bed_time, no_tsl_data, alarm, data_anomaly, is_deleted) VALUES
(8, NULL, 0, 'NONE', NULL, NULL, NULL, NULL, 0, 0, 0, 0);

-- 102 房：一间床 1021 告警红卡（心率呼吸为0）
INSERT INTO zz_smart_device (product_category, device_name, bind_type, room_id, bed_id, icon_type, sort_order, is_deleted) VALUES
('智能烟感', 'smoke-102', 'ROOM', 2, NULL, 'smoke', 1, 0),
('温湿度', 'temp-102', 'ROOM', 2, NULL, 'temp', 2, 0);

INSERT INTO zz_room_env (room_id, door_open, temperature_c, humidity_pct, alarm_normal, data_anomaly, is_deleted) VALUES
(2, 0, 25.0, 38, 1, 1, 0);

INSERT INTO zz_smart_device (product_category, device_name, bind_type, room_id, bed_id, icon_type, sort_order, is_deleted) VALUES
('床垫传感器', 'mat-1021', 'BED', 2, 3, 'bed', 1, 0),
('跌倒告警', 'fall-1021', 'BED', 2, 3, 'alarm', 2, 0);

INSERT INTO zz_bed_monitor (bed_id, elder_name, checked_out, presence, heart_rate, breath_rate, left_bed_count, left_bed_time, no_tsl_data, alarm, data_anomaly, is_deleted) VALUES
(3, '测试老人', 0, 'SLEEP', 0, 0, NULL, NULL, 0, 1, 1, 0);

-- 107：仅床位设备+空老人文案
INSERT INTO zz_smart_device (product_category, device_name, bind_type, room_id, bed_id, icon_type, sort_order, is_deleted) VALUES
('智能烟感', 'smoke-107', 'ROOM', 3, NULL, 'smoke', 1, 0);
INSERT INTO zz_room_env (room_id, door_open, temperature_c, humidity_pct, alarm_normal, data_anomaly, is_deleted) VALUES
(3, 1, NULL, NULL, 1, 0, 0);

INSERT INTO zz_smart_device (product_category, device_name, bind_type, room_id, bed_id, icon_type, sort_order, is_deleted) VALUES
('床垫传感器', 'mat-1071', 'BED', 3, 4, 'bed', 1, 0);
INSERT INTO zz_bed_monitor (bed_id, elder_name, checked_out, presence, heart_rate, breath_rate, left_bed_count, left_bed_time, no_tsl_data, alarm, data_anomaly, is_deleted) VALUES
(4, NULL, 0, 'NONE', NULL, NULL, NULL, NULL, 0, 0, 0, 0);

-- 108：请假老人+离床数据
INSERT INTO zz_smart_device (product_category, device_name, bind_type, room_id, bed_id, icon_type, sort_order, is_deleted) VALUES
('温湿度', 'temp-108', 'ROOM', 4, NULL, 'temp', 1, 0);
INSERT INTO zz_room_env (room_id, door_open, temperature_c, humidity_pct, alarm_normal, data_anomaly, is_deleted) VALUES
(4, 1, 24.5, 42, 1, 0, 0);
INSERT INTO zz_smart_device (product_category, device_name, bind_type, room_id, bed_id, icon_type, sort_order, is_deleted) VALUES
('床垫传感器', 'mat-1081', 'BED', 4, 5, 'bed', 1, 0);
INSERT INTO zz_bed_monitor (bed_id, elder_name, checked_out, presence, heart_rate, breath_rate, left_bed_count, left_bed_time, no_tsl_data, alarm, data_anomaly, is_deleted) VALUES
(5, '李有田', 0, 'LEFT', NULL, NULL, 6, '20:00:00', 0, 0, 0, 0);

-- 2楼 201：简单监测（用于多楼层 Tab）
INSERT INTO zz_smart_device (product_category, device_name, bind_type, room_id, bed_id, icon_type, sort_order, is_deleted) VALUES
('智能烟感', 'smoke-201', 'ROOM', 6, NULL, 'smoke', 1, 0);
INSERT INTO zz_room_env (room_id, door_open, temperature_c, humidity_pct, alarm_normal, data_anomaly, is_deleted) VALUES
(6, 1, 23.0, 45, 1, 0, 0);
INSERT INTO zz_smart_device (product_category, device_name, bind_type, room_id, bed_id, icon_type, sort_order, is_deleted) VALUES
('床垫传感器', 'mat-2011', 'BED', 6, 6, 'bed', 1, 0);
INSERT INTO zz_bed_monitor (bed_id, elder_name, checked_out, presence, heart_rate, breath_rate, left_bed_count, left_bed_time, no_tsl_data, alarm, data_anomaly, is_deleted) VALUES
(6, NULL, 0, 'NONE', NULL, NULL, NULL, NULL, 1, 0, 0, 0);

-- 3楼 301：设备+老人无数据
INSERT INTO zz_smart_device (product_category, device_name, bind_type, room_id, bed_id, icon_type, sort_order, is_deleted) VALUES
('智能烟感', 'smoke-301', 'ROOM', 7, NULL, 'smoke', 1, 0);
INSERT INTO zz_room_env (room_id, door_open, temperature_c, humidity_pct, alarm_normal, data_anomaly, is_deleted) VALUES
(7, 1, 25.0, 39, 1, 0, 0);
INSERT INTO zz_smart_device (product_category, device_name, bind_type, room_id, bed_id, icon_type, sort_order, is_deleted) VALUES
('床垫传感器', 'mat-3011', 'BED', 7, 7, 'bed', 1, 0);
INSERT INTO zz_bed_monitor (bed_id, elder_name, checked_out, presence, heart_rate, breath_rate, left_bed_count, left_bed_time, no_tsl_data, alarm, data_anomaly, is_deleted) VALUES
(7, '王伯', 0, 'SLEEP', NULL, NULL, NULL, NULL, 1, 0, 0, 0);

-- 109：仅有房间级设备、无床位（房间占位文案）
INSERT INTO zz_smart_device (product_category, device_name, bind_type, room_id, bed_id, icon_type, sort_order, is_deleted) VALUES
('智能烟感', 'smoke-109', 'ROOM', 5, NULL, 'smoke', 1, 0);
INSERT INTO zz_room_env (room_id, door_open, temperature_c, humidity_pct, alarm_normal, data_anomaly, is_deleted) VALUES
(5, 1, 22.0, 50, 1, 0, 0);
