-- 若库中无下行指令表则创建，并插入设备4模块 + 35 条服务调用测试数据
SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS zz_iot_device_tsl_service_invocation (
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

INSERT INTO zz_iot_tsl_module (device_id, module_name, module_key, sort_order, is_deleted)
SELECT 4, '模块1-远程开门', 'DoorOpen', 1, 0
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM zz_iot_tsl_module WHERE device_id = 4 AND module_key = 'DoorOpen');

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
