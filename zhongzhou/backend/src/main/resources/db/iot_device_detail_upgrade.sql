-- 设备详情页扩展：字段 + 物模型数据表（在已有 zz_iot_device 上执行）
SET NAMES utf8mb4;

ALTER TABLE zz_iot_device
  ADD COLUMN region VARCHAR(64) NOT NULL DEFAULT '' COMMENT '地域' AFTER device_secret,
  ADD COLUMN node_type VARCHAR(32) NOT NULL DEFAULT '设备' COMMENT '节点类型' AFTER region,
  ADD COLUMN auth_method VARCHAR(32) NOT NULL DEFAULT '设备密钥' COMMENT '认证方式' AFTER node_type,
  ADD COLUMN ip_address VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'IP地址' AFTER auth_method,
  ADD COLUMN firmware_version VARCHAR(64) NOT NULL DEFAULT '' COMMENT '固件版本' AFTER ip_address,
  ADD COLUMN creator_name VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建人' AFTER firmware_version,
  ADD COLUMN activation_time DATETIME NULL COMMENT '激活时间' AFTER creator_name;

-- 若列已存在会报错，可忽略后手工检查

CREATE TABLE IF NOT EXISTS zz_iot_device_thing_property (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  device_id BIGINT UNSIGNED NOT NULL COMMENT '设备ID',
  prop_key VARCHAR(64) NOT NULL COMMENT '属性标识',
  prop_name VARCHAR(64) NOT NULL COMMENT '属性名称',
  prop_value VARCHAR(256) NOT NULL DEFAULT '' COMMENT '当前值',
  report_time DATETIME NOT NULL COMMENT '上报时间',
  sort_order INT NOT NULL DEFAULT 0,
  is_deleted TINYINT NOT NULL DEFAULT 0,
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_iot_tsl_device (device_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='智能检测-设备物模型数据';
