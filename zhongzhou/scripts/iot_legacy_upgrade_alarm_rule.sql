-- 兼容旧库：将 zz_iot_alarm_rule 升级为新增页所需字段
SET NAMES utf8mb4;

SET @has_product_id = (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'zz_iot_alarm_rule' AND column_name = 'product_id');
SET @sql = IF(@has_product_id = 0, 'ALTER TABLE zz_iot_alarm_rule ADD COLUMN product_id BIGINT UNSIGNED NOT NULL DEFAULT 2 COMMENT ''所属产品'' AFTER rule_name', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @has_metric_key = (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'zz_iot_alarm_rule' AND column_name = 'metric_key');
SET @sql = IF(@has_metric_key = 1, 'ALTER TABLE zz_iot_alarm_rule CHANGE COLUMN metric_key function_name VARCHAR(64) NOT NULL COMMENT ''功能名称''', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @has_module_name = (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'zz_iot_alarm_rule' AND column_name = 'module_name');
SET @sql = IF(@has_module_name = 0, 'ALTER TABLE zz_iot_alarm_rule ADD COLUMN module_name VARCHAR(128) NOT NULL DEFAULT ''默认模块'' COMMENT ''模块'' AFTER product_id', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @has_data_type = (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'zz_iot_alarm_rule' AND column_name = 'data_type');
SET @sql = IF(@has_data_type = 0, 'ALTER TABLE zz_iot_alarm_rule ADD COLUMN data_type VARCHAR(16) NOT NULL DEFAULT ''elder'' COMMENT ''elder老人异常数据/device设备异常数据'' AFTER function_name', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @has_persist_cycles = (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'zz_iot_alarm_rule' AND column_name = 'persist_cycles');
SET @sql = IF(@has_persist_cycles = 0, 'ALTER TABLE zz_iot_alarm_rule ADD COLUMN persist_cycles INT NOT NULL DEFAULT 1 COMMENT ''持续周期数'' AFTER threshold_value', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @has_time_start = (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'zz_iot_alarm_rule' AND column_name = 'time_start');
SET @sql = IF(@has_time_start = 0, 'ALTER TABLE zz_iot_alarm_rule ADD COLUMN time_start TIME NOT NULL DEFAULT ''00:00:00'' COMMENT ''生效时段开始'' AFTER persist_cycles', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @has_time_end = (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'zz_iot_alarm_rule' AND column_name = 'time_end');
SET @sql = IF(@has_time_end = 0, 'ALTER TABLE zz_iot_alarm_rule ADD COLUMN time_end TIME NOT NULL DEFAULT ''23:59:59'' COMMENT ''生效时段结束'' AFTER time_start', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @has_mute = (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'zz_iot_alarm_rule' AND column_name = 'mute_cycle_minutes');
SET @sql = IF(@has_mute = 0, 'ALTER TABLE zz_iot_alarm_rule ADD COLUMN mute_cycle_minutes INT NOT NULL DEFAULT 5 COMMENT ''报警沉默周期(分钟)'' AFTER time_end', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @has_remark = (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'zz_iot_alarm_rule' AND column_name = 'remark');
SET @sql = IF(@has_remark = 1, 'ALTER TABLE zz_iot_alarm_rule DROP COLUMN remark', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

