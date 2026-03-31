SET NAMES utf8mb4;
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
