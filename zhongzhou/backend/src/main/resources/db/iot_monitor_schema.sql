SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 智能检测模块（执行前请确保 zz_customer_profile、zz_floor_bed 已存在）

DROP TABLE IF EXISTS zz_iot_alarm_record;
DROP TABLE IF EXISTS zz_iot_alarm_rule;
DROP TABLE IF EXISTS zz_iot_device_tsl_property_history;
DROP TABLE IF EXISTS zz_iot_device_tsl_service_invocation;
DROP TABLE IF EXISTS zz_iot_device_tsl_runtime;
DROP TABLE IF EXISTS zz_iot_tsl_module;
DROP TABLE IF EXISTS zz_iot_device;
DROP TABLE IF EXISTS zz_iot_product;

CREATE TABLE zz_iot_product (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  product_name VARCHAR(64) NOT NULL COMMENT '产品名称',
  aliyun_product_key VARCHAR(64) NOT NULL DEFAULT '' COMMENT '阿里云 ProductKey',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  KEY idx_iot_product_name (product_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='智能检测-物联网产品';

CREATE TABLE zz_iot_device (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  device_name VARCHAR(64) NOT NULL,
  remark_name VARCHAR(32) NOT NULL DEFAULT '',
  product_id BIGINT UNSIGNED NOT NULL,
  device_type VARCHAR(16) NOT NULL,
  access_location_display VARCHAR(128) NOT NULL DEFAULT '',
  bind_elder_id BIGINT UNSIGNED NULL,
  bind_bed_id BIGINT UNSIGNED NULL,
  product_key VARCHAR(64) NOT NULL DEFAULT '',
  device_key VARCHAR(64) NOT NULL DEFAULT '',
  device_secret VARCHAR(128) NOT NULL DEFAULT '',
  region VARCHAR(64) NOT NULL DEFAULT '',
  node_type VARCHAR(32) NOT NULL DEFAULT '设备',
  auth_method VARCHAR(32) NOT NULL DEFAULT '设备密钥',
  ip_address VARCHAR(64) NOT NULL DEFAULT '',
  firmware_version VARCHAR(64) NOT NULL DEFAULT '',
  creator_name VARCHAR(64) NOT NULL DEFAULT '',
  activation_time DATETIME NULL,
  protocol VARCHAR(16) NOT NULL DEFAULT 'MQTT',
  online_status TINYINT NOT NULL DEFAULT 0,
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  KEY idx_iot_device_name (device_name),
  KEY idx_iot_device_product (product_id),
  KEY idx_iot_device_elder (bind_elder_id),
  KEY idx_iot_device_bed (bind_bed_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='智能检测-设备';

-- 物模型：默认模块（按设备）
CREATE TABLE zz_iot_tsl_module (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  device_id BIGINT UNSIGNED NOT NULL COMMENT '设备ID',
  module_name VARCHAR(128) NOT NULL COMMENT '模块名称',
  module_key VARCHAR(64) NOT NULL DEFAULT '' COMMENT '模块标识符',
  sort_order INT NOT NULL DEFAULT 0,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_iot_mod_device (device_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='智能检测-物模型模块';

-- 运行状态：属性上报（上行）列表
CREATE TABLE zz_iot_device_tsl_runtime (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  device_id BIGINT UNSIGNED NOT NULL,
  module_id BIGINT UNSIGNED NOT NULL,
  prop_identifier VARCHAR(64) NOT NULL COMMENT '标识符',
  function_name VARCHAR(64) NOT NULL COMMENT '功能名称',
  data_value VARCHAR(128) NOT NULL DEFAULT '' COMMENT '数据值',
  update_time DATETIME NOT NULL COMMENT '更新时间',
  sort_order INT NOT NULL DEFAULT 0,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_iot_rt_dev_mod (device_id, module_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='智能检测-物模型运行状态';

-- 属性上报历史（查看数据弹窗）
CREATE TABLE zz_iot_device_tsl_property_history (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  device_id BIGINT UNSIGNED NOT NULL,
  module_id BIGINT UNSIGNED NOT NULL,
  prop_identifier VARCHAR(64) NOT NULL,
  raw_value VARCHAR(128) NOT NULL COMMENT '原始值',
  report_time DATETIME NOT NULL,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_iot_hist_dev_prop_time (device_id, prop_identifier, report_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='智能检测-属性上报历史';

-- 物模型-服务调用（下行指令）
CREATE TABLE zz_iot_device_tsl_service_invocation (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  device_id BIGINT UNSIGNED NOT NULL,
  module_id BIGINT UNSIGNED NOT NULL,
  service_identifier VARCHAR(64) NOT NULL COMMENT '标识符',
  service_name VARCHAR(128) NOT NULL COMMENT '服务名称',
  input_params VARCHAR(512) NOT NULL DEFAULT '' COMMENT '输入参数',
  output_params VARCHAR(1024) NOT NULL DEFAULT '' COMMENT '输出参数',
  invoke_time DATETIME NOT NULL COMMENT '调用时间',
  is_deleted TINYINT NOT NULL DEFAULT 0,
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_iot_svc_dev_mod_time (device_id, module_id, invoke_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='智能检测-物模型服务调用(下行)';

CREATE TABLE zz_iot_alarm_rule (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  rule_name VARCHAR(64) NOT NULL COMMENT '报警规则名称',
  product_id BIGINT UNSIGNED NOT NULL COMMENT '所属产品',
  module_name VARCHAR(128) NOT NULL DEFAULT '' COMMENT '模块',
  device_id BIGINT UNSIGNED NULL COMMENT '关联设备，空为全部设备',
  function_name VARCHAR(64) NOT NULL COMMENT '功能名称',
  data_type VARCHAR(16) NOT NULL DEFAULT 'elder' COMMENT 'elder老人异常数据/device设备异常数据',
  compare_type VARCHAR(8) NOT NULL DEFAULT 'GTE' COMMENT 'GT/LT/GTE/LTE/EQ',
  threshold_value DECIMAL(16,4) NOT NULL DEFAULT 0 COMMENT '阈值',
  persist_cycles INT NOT NULL DEFAULT 1 COMMENT '持续周期数',
  time_start TIME NOT NULL DEFAULT '00:00:00' COMMENT '生效时段开始',
  time_end TIME NOT NULL DEFAULT '23:59:59' COMMENT '生效时段结束',
  mute_cycle_minutes INT NOT NULL DEFAULT 5 COMMENT '报警沉默周期(分钟)',
  enabled TINYINT NOT NULL DEFAULT 1 COMMENT '1启用 0禁用',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  KEY idx_iot_rule_product (product_id),
  KEY idx_iot_rule_device (device_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='智能检测-报警规则';

CREATE TABLE zz_iot_alarm_record (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  rule_id BIGINT UNSIGNED NOT NULL COMMENT '规则ID',
  device_id BIGINT UNSIGNED NOT NULL COMMENT '设备ID',
  metric_value DECIMAL(16,4) NOT NULL DEFAULT 0 COMMENT '触发时指标值',
  alarm_content VARCHAR(512) NOT NULL DEFAULT '' COMMENT '告警内容',
  alarm_time DATETIME NOT NULL COMMENT '告警时间',
  handle_status TINYINT NOT NULL DEFAULT 0 COMMENT '0待处理 1已处理',
  handled_time DATETIME NULL COMMENT '处理时间',
  handle_result VARCHAR(100) NOT NULL DEFAULT '' COMMENT '处理结果',
  is_deleted TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  KEY idx_iot_alarm_time (alarm_time),
  KEY idx_iot_alarm_rule (rule_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='智能检测-报警数据';

INSERT INTO zz_iot_product (id, product_name, aliyun_product_key, is_deleted) VALUES
(1, '智能手表', 'PK_smartwatch', 0),
(2, '智能烟感', 'j0dz5GFiqte', 0),
(3, '智能门禁', 'PK_door', 0);

INSERT INTO zz_customer_profile (customer_nickname, customer_phone, order_track_count, first_login_time, is_deleted)
SELECT '安欣', '13900000000', 0, '2048-10-09 15:00:00', 0
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM zz_customer_profile WHERE customer_phone = '13900000000');

INSERT INTO zz_iot_device
(device_name, remark_name, product_id, device_type, access_location_display, bind_elder_id, bind_bed_id, product_key, device_key, device_secret,
 region, node_type, auth_method, ip_address, firmware_version, creator_name, activation_time,
 protocol, online_status, is_deleted, created_time)
SELECT 'SmartWatch-01', '智能手表01', 1, '随身设备', '安欣', cp.id, NULL, '', '', '',
 '华东2 (上海)', '设备', '设备密钥', '', '', '顾廷烨', '2048-10-09 15:00:00',
 'MQTT', 1, 0, '2048-10-09 15:00:00'
FROM zz_customer_profile cp WHERE cp.customer_phone = '13900000000' LIMIT 1;

-- 原型：DeviceSecret / ProductKey / 1011 床位 / 在线
INSERT INTO zz_iot_device
(device_name, remark_name, product_id, device_type, access_location_display, bind_elder_id, bind_bed_id, product_key, device_key, device_secret,
 region, node_type, auth_method, ip_address, firmware_version, creator_name, activation_time,
 protocol, online_status, is_deleted, created_time)
VALUES
('smoke alarm-01', '烟雾报警器', 2, '固定设备', '1011 床位', NULL, 1, 'j0dz5GFiqte', 'smoke_alarm_01', 'b61673980cb67410e2493645ca8ad5ed',
 '华东2 (上海)', '设备', '设备密钥', '', '', '顾廷烨', '2048-10-15 15:00:00',
 'MQTT', 1, 0, '2048-10-15 15:00:00'),
('smoke alarm-02', '烟雾报警器02', 2, '固定设备', '101房间/1012床位', NULL, 2, '', '', '',
 '华东2 (上海)', '设备', '设备密钥', '', '', '顾廷烨', '2048-10-08 15:00:00',
 'MQTT', 1, 0, '2048-10-08 15:00:00'),
('Intelligent access control', '智能门禁', 3, '固定设备', '1楼', NULL, 4, 'j0dz5GFiqte', 'door_ac_01', 'b61673980cb67410e2493645ca8ad5ed',
 '华东2 (上海)', '设备', '设备密钥', '', '', '顾廷烨', '2048-10-07 15:00:00',
 'MQTT', 1, 0, '2048-10-07 15:00:00');

INSERT INTO zz_iot_tsl_module (id, device_id, module_name, module_key, sort_order, is_deleted) VALUES
(1, 2, '模块1-商务房烟雾功能', 'swfywgn1', 1, 0);

-- 智能门禁设备：默认模块（原型：模块1-远程开门 / DoorOpen）
INSERT INTO zz_iot_tsl_module (device_id, module_name, module_key, sort_order, is_deleted) VALUES
(4, '模块1-远程开门', 'DoorOpen', 1, 0);

-- 658 条运行状态（首行 PowerConsumption 对齐原型）
SET SESSION cte_max_recursion_depth = 1000;
INSERT INTO zz_iot_device_tsl_runtime (device_id, module_id, prop_identifier, function_name, data_value, update_time, sort_order, is_deleted)
WITH RECURSIVE seq(n) AS (
  SELECT 1
  UNION ALL
  SELECT n + 1 FROM seq WHERE n < 658
)
SELECT
  2, 1,
  IF(n = 1, 'PowerConsumption', CONCAT('PowerConsumption', LPAD(n, 3, '0'))),
  '用电量',
  CONCAT(10 + (n MOD 20), '.0kW·h'),
  DATE_SUB('2048-10-15 15:00:00', INTERVAL (n - 1) MINUTE),
  n,
  0
FROM seq;

-- PowerConsumption 属性上报历史 15 条（1 小时内，查看数据默认 5 条/页共 3 页）
INSERT INTO zz_iot_device_tsl_property_history (device_id, module_id, prop_identifier, raw_value, report_time, is_deleted) VALUES
(2, 1, 'PowerConsumption', '92.5kW·h', '2048-10-15 15:00:00', 0),
(2, 1, 'PowerConsumption', '91.0kW·h', '2048-10-15 14:55:00', 0),
(2, 1, 'PowerConsumption', '90.2kW·h', '2048-10-15 14:50:00', 0),
(2, 1, 'PowerConsumption', '89.8kW·h', '2048-10-15 14:45:00', 0),
(2, 1, 'PowerConsumption', '88.1kW·h', '2048-10-15 14:40:00', 0),
(2, 1, 'PowerConsumption', '87.5kW·h', '2048-10-15 14:35:00', 0),
(2, 1, 'PowerConsumption', '86.0kW·h', '2048-10-15 14:30:00', 0),
(2, 1, 'PowerConsumption', '85.3kW·h', '2048-10-15 14:25:00', 0),
(2, 1, 'PowerConsumption', '84.7kW·h', '2048-10-15 14:20:00', 0),
(2, 1, 'PowerConsumption', '83.2kW·h', '2048-10-15 14:15:00', 0),
(2, 1, 'PowerConsumption', '82.0kW·h', '2048-10-15 14:10:00', 0),
(2, 1, 'PowerConsumption', '81.4kW·h', '2048-10-15 14:05:00', 0),
(2, 1, 'PowerConsumption', '80.9kW·h', '2048-10-15 14:02:00', 0),
(2, 1, 'PowerConsumption', '80.1kW·h', '2048-10-15 14:01:00', 0),
(2, 1, 'PowerConsumption', '79.5kW·h', '2048-10-15 14:00:00', 0);

-- 下行指令：35 条（≥30 条测试数据；前 4 条时间同原型 2048-10-15 15:00:00）
SET SESSION cte_max_recursion_depth = 1000;
INSERT INTO zz_iot_device_tsl_service_invocation
(device_id, module_id, service_identifier, service_name, input_params, output_params, invoke_time, is_deleted)
WITH RECURSIVE seq(n) AS (
  SELECT 1
  UNION ALL
  SELECT n + 1 FROM seq WHERE n < 35
)
SELECT
  4,
  (SELECT id FROM zz_iot_tsl_module WHERE device_id = 4 AND module_key = 'DoorOpen' LIMIT 1),
  'DoorOpen',
  '远程开门',
  '{开门}',
  '{"code":9201,"data":{},"id":"20201123123456789"}',
  IF(n <= 4, '2048-10-15 15:00:00', DATE_SUB('2048-10-15 15:00:00', INTERVAL (n - 4) MINUTE)),
  0
FROM seq;

INSERT INTO zz_iot_alarm_rule
(rule_name, product_id, module_name, device_id, function_name, data_type, compare_type, threshold_value, persist_cycles, time_start, time_end, mute_cycle_minutes, enabled, created_time, is_deleted)
VALUES
('智能烟感烟雾量', 2, '模块1-商务房烟雾功能', 2, '烟雾量', 'elder', 'GTE', 3.0000, 1, '00:00:00', '23:59:00', 5, 1, '2048-10-10 15:00:00', 0),
('智能烟感烟雾量', 2, '模块1-商务房烟雾功能', NULL, '烟雾量', 'elder', 'GTE', 6.0000, 3, '00:00:00', '23:59:00', 10, 1, '2048-10-10 15:00:00', 0),
('智能烟感烟雾量', 2, '模块1-商务房烟雾功能', NULL, '烟雾量', 'device', 'LT', 10.0000, 5, '00:00:00', '23:59:00', 15, 0, '2048-10-10 15:00:00', 0),
('智能烟感烟雾量', 2, '模块1-商务房烟雾功能', 2, '烟雾量', 'device', 'GTE', 1.0000, 10, '00:00:00', '23:59:00', 30, 1, '2048-10-10 15:00:00', 0);

-- 报警规则：再追加 31 条（共 35 条，≥30）
SET SESSION cte_max_recursion_depth = 1000;
INSERT INTO zz_iot_alarm_rule
(rule_name, product_id, module_name, device_id, function_name, data_type, compare_type, threshold_value, persist_cycles, time_start, time_end, mute_cycle_minutes, enabled, created_time, is_deleted)
WITH RECURSIVE seq(n) AS (
  SELECT 1
  UNION ALL
  SELECT n + 1 FROM seq WHERE n < 31
)
SELECT
  CONCAT('批量测试规则-', LPAD(n, 3, '0')),
  2,
  '模块1-商务房烟雾功能',
  IF(n MOD 3 = 0, NULL, 2),
  IF(n MOD 4 = 0, '电量', '烟雾量'),
  IF(n MOD 2 = 0, 'elder', 'device'),
  IF(n MOD 5 = 0, 'LT', 'GTE'),
  IF(n MOD 5 = 0, 8.0000, 5.0000 + (n MOD 10)),
  1 + (n MOD 7),
  '00:00:00',
  '23:59:00',
  ELT((n MOD 9) + 1, 5, 10, 15, 30, 60, 180, 360, 720, 1440),
  IF(n MOD 11 = 0, 0, 1),
  DATE_SUB('2048-10-10 15:00:00', INTERVAL n MINUTE),
  0
FROM seq;

INSERT INTO zz_iot_alarm_record
(rule_id, device_id, metric_value, alarm_content, alarm_time, handle_status, handled_time, handle_result)
VALUES
(1, 2, 4.00, '设备「smoke alarm-01」烟雾量 4.0 触发规则 智能烟感烟雾量', '2048-10-10 15:00:00', 0, NULL, ''),
(2, 2, 7.00, '设备「smoke alarm-01」烟雾量 7.0 触发规则 智能烟感烟雾量', '2048-10-10 15:00:00', 1, '2048-10-10 15:10:00', '已通知护理员，持续观察'),
(3, 3, 0.00, '设备「smoke alarm-02」上报异常', '2048-10-08 15:00:00', 1, '2048-10-08 15:20:00', '已联系维修工检修');

-- 报警数据追加 31 条（共 34 条）
SET SESSION cte_max_recursion_depth = 1000;
INSERT INTO zz_iot_alarm_record
(rule_id, device_id, metric_value, alarm_content, alarm_time, handle_status, handled_time, handle_result, is_deleted)
WITH RECURSIVE seq(n) AS (
  SELECT 1
  UNION ALL
  SELECT n + 1 FROM seq WHERE n < 31
)
SELECT
  IF(n MOD 3 = 0, 4, IF(n MOD 2 = 0, 2, 1)),
  IF(n MOD 2 = 0, 2, 3),
  60 + (n MOD 120),
  CONCAT('自动测试报警数据-', LPAD(n, 3, '0')),
  DATE_SUB('2048-10-10 15:00:00', INTERVAL n HOUR),
  IF(n MOD 4 = 0, 1, 0),
  IF(n MOD 4 = 0, DATE_SUB('2048-10-10 16:00:00', INTERVAL n HOUR), NULL),
  IF(n MOD 4 = 0, '已电话通知并记录', ''),
  0
FROM seq;
