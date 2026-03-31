-- 在已有 zhongzhou 库上追加/补齐测试数据（可重复执行）
-- 用法: mysql -h127.0.0.1 -uroot -p1234 zhongzhou < scripts/iot_append_test_data_min30.sql

SET NAMES utf8mb4;

SET @has_handle_result = (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'zz_iot_alarm_record' AND column_name = 'handle_result');
SET @sql = IF(@has_handle_result = 0, 'ALTER TABLE zz_iot_alarm_record ADD COLUMN handle_result VARCHAR(100) NOT NULL DEFAULT '''' COMMENT ''处理结果'' AFTER handled_time', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 1) 报警规则：删除旧批量再插入 31 条（与全量脚本一致）
DELETE FROM zz_iot_alarm_rule WHERE rule_name LIKE '批量测试规则-%';

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

-- 3) 报警数据：重建 34 条（与报警数据页面联调）
DELETE FROM zz_iot_alarm_record;

INSERT INTO zz_iot_alarm_record
(rule_id, device_id, metric_value, alarm_content, alarm_time, handle_status, handled_time, handle_result, is_deleted)
VALUES
(1, 2, 96.00, '报警示例-001', '2048-10-10 15:00:00', 1, '2048-10-10 15:10:00', '已回访护理员并记录', 0),
(2, 2, 88.00, '报警示例-002', '2048-10-10 15:00:00', 1, '2048-10-10 15:20:00', '已完成现场处置', 0),
(3, 3, 170.00, '报警示例-003', '2048-10-08 15:00:00', 0, NULL, '', 0);

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

-- 2) 设备4 服务调用：重建为 35 条（≥30）
DELETE FROM zz_iot_device_tsl_service_invocation WHERE device_id = 4;

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
