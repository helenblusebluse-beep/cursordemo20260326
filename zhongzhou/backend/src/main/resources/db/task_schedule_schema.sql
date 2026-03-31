SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP TABLE IF EXISTS zz_task_refund_apply;
DROP TABLE IF EXISTS zz_task_execute_record;
DROP TABLE IF EXISTS zz_task_schedule;

CREATE TABLE zz_task_schedule (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  elder_name VARCHAR(32) NOT NULL COMMENT '老人姓名',
  elder_gender VARCHAR(8) NOT NULL DEFAULT '' COMMENT '性别',
  elder_age INT NOT NULL DEFAULT 0 COMMENT '年龄',
  bed_no VARCHAR(10) NOT NULL COMMENT '床位号',
  care_level_name VARCHAR(20) NOT NULL DEFAULT '' COMMENT '护理等级',
  caregiver_names VARCHAR(128) NOT NULL DEFAULT '' COMMENT '护理员姓名(多名)',
  elder_avatar_url VARCHAR(500) NOT NULL DEFAULT '' COMMENT '老人头像',
  nursing_item_name VARCHAR(32) NOT NULL COMMENT '护理项目名称',
  order_no VARCHAR(40) NOT NULL DEFAULT '' COMMENT '关联单据',
  task_type VARCHAR(20) NOT NULL COMMENT '项目类型:护理计划内/护理计划外',
  caregiver_name VARCHAR(20) NOT NULL COMMENT '护理员姓名',
  expected_service_time DATETIME NOT NULL COMMENT '期望服务时间',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  created_by VARCHAR(20) NOT NULL DEFAULT '' COMMENT '创建人',
  remark VARCHAR(255) NOT NULL DEFAULT '' COMMENT '备注',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '1待执行 2已执行 3已取消',
  cancel_reason VARCHAR(100) DEFAULT '' COMMENT '取消原因',
  cancel_by VARCHAR(20) NOT NULL DEFAULT '' COMMENT '取消人',
  cancel_time DATETIME DEFAULT NULL COMMENT '取消时间',
  execute_time DATETIME DEFAULT NULL COMMENT '执行时间',
  execute_by VARCHAR(20) NOT NULL DEFAULT '' COMMENT '执行人',
  execute_image_name VARCHAR(255) DEFAULT '' COMMENT '执行图片名',
  execute_image_url VARCHAR(500) DEFAULT '' COMMENT '执行图片URL',
  execute_record VARCHAR(100) DEFAULT '' COMMENT '执行记录',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (id),
  KEY idx_task_status_time (status, expected_service_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务安排';

CREATE TABLE zz_task_execute_record (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  task_id BIGINT UNSIGNED NOT NULL COMMENT '任务ID',
  execute_time DATETIME NOT NULL COMMENT '执行时间',
  execute_by VARCHAR(20) NOT NULL DEFAULT '' COMMENT '执行人',
  execute_image_name VARCHAR(255) NOT NULL COMMENT '执行图片名',
  execute_image_url VARCHAR(500) NOT NULL COMMENT '执行图片URL',
  execute_record VARCHAR(100) DEFAULT '' COMMENT '执行记录',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_task_execute_record_task (task_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务执行记录';

CREATE TABLE zz_task_refund_apply (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  task_id BIGINT UNSIGNED NOT NULL COMMENT '任务ID',
  elder_name VARCHAR(32) NOT NULL COMMENT '老人姓名',
  refund_reason VARCHAR(100) NOT NULL COMMENT '退款原因',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '1待处理',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_task_refund_task (task_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务取消退款申请';

INSERT INTO zz_task_schedule
(id, elder_name, elder_gender, elder_age, bed_no, care_level_name, caregiver_names, elder_avatar_url, nursing_item_name, order_no, task_type, caregiver_name, expected_service_time, created_time, created_by, remark, status, is_deleted)
VALUES
(1, '高启强', '男', 43, '1011', '一级护理等级', '顾廷烨、盛明兰、盛如兰', 'https://picsum.photos/seed/elder1011/140/180', '洗头', 'ZD20481010150011.001', '护理计划外', '顾廷烨', '2048-10-10 15:00:00', '2048-10-10 15:00:00', '顾廷烨', '给老人洗澡的时候注意一下，使用老人自带的沐浴液', 2, 0),
(2, '安欣', '男', 42, '1012', '二级护理等级', '顾廷烨、盛明兰', 'https://picsum.photos/seed/elder1012/140/180', '口腔清洁', 'ZD20481010150012.001', '护理计划外', '顾廷烨', '2048-10-10 15:00:00', '2048-10-10 15:00:00', '顾廷烨', '按标准流程执行，注意口腔黏膜状态', 2, 0),
(3, '孟钰', '女', 38, '1014', '一级护理等级', '顾廷烨', 'https://picsum.photos/seed/elder1014/140/180', '床上洗头', 'ZD20481010150014.001', '护理计划内', '顾廷烨', '2048-10-10 15:00:00', '2048-10-10 15:00:00', '顾廷烨', '保持水温，防止受凉', 2, 0),
(4, '孟德海', '男', 70, '1041', '三级护理等级', '顾廷烨', 'https://picsum.photos/seed/elder1041/140/180', '协助更衣', 'ZD20481010150401.001', '护理计划内', '顾廷烨', '2048-10-10 15:00:00', '2048-10-10 15:00:00', '顾廷烨', '更衣前确认皮肤状态', 1, 0),
(5, '徐忠', '男', 55, '1042', '二级护理等级', '盛堂兰', 'https://picsum.photos/seed/elder1042/140/180', '排痰护理', 'ZD20481010150402.001', '护理计划内', '盛堂兰', '2048-10-10 15:00:00', '2048-10-10 15:00:00', '盛堂兰', '观察痰液颜色并记录', 1, 0),
(6, '李响', '男', 41, '1013', '二级护理等级', '盛如兰', 'https://picsum.photos/seed/elder1013/140/180', '足部清洁', 'ZD20481010150013.001', '护理计划外', '盛如兰', '2048-10-10 15:00:00', '2048-10-10 15:00:00', '盛如兰', '清洁后涂抹保湿霜', 1, 0),
(7, '陈书婷', '女', 39, '1044', '一级护理等级', '盛华兰', 'https://picsum.photos/seed/elder1044/140/180', '足部清洁', 'ZD20481010150404.001', '护理计划内', '盛华兰', '2048-10-10 15:00:00', '2048-10-10 15:00:00', '盛华兰', '检查足部皮肤破损', 1, 0),
(8, '杨健', '男', 45, '1043', '二级护理等级', '王子希', 'https://picsum.photos/seed/elder1043/140/180', '协助更衣', 'ZD20481010150403.001', '护理计划内', '王子希', '2048-10-10 15:00:00', '2048-10-10 15:00:00', '王子希', '协助动作轻柔', 1, 0),
(9, '卢启盛', '男', 36, '1022', '一级护理等级', '林诗源', 'https://picsum.photos/seed/elder1022/140/180', '协助更衣', 'ZD20481010150202.001', '护理计划内', '林诗源', '2048-10-10 15:00:00', '2048-10-10 15:00:00', '林诗源', '先沟通后操作', 3, 0),
(10, '徐江', '男', 50, '1024', '三级护理等级', '盛长柏', 'https://picsum.photos/seed/elder1024/140/180', '协助更衣', 'ZD20481010150204.001', '护理计划内', '盛长柏', '2048-10-10 15:00:00', '2048-10-10 15:00:00', '盛长柏', '关注生命体征变化', 3, 0);

UPDATE zz_task_schedule
SET execute_by = '顾廷烨',
    execute_time = '2048-10-10 15:00:00',
    execute_image_name = 'done-1.png',
    execute_image_url = 'https://picsum.photos/seed/task-exec-1/220/220',
    execute_record = '今日已完成洗发业务，头发光亮一如，出油情况较为明显，已使用控油洗发水，根部养生精示，并未发现掉发现象。'
WHERE id = 1;

UPDATE zz_task_schedule
SET execute_by = '顾廷烨',
    execute_time = '2048-10-10 15:00:00',
    execute_image_name = 'done-2.png',
    execute_image_url = 'https://picsum.photos/seed/task-exec-2/220/220',
    execute_record = '护理过程平稳，老人配合度良好，已完成口腔清洁并做基本检查。'
WHERE id = 2;

UPDATE zz_task_schedule
SET execute_by = '顾雁开',
    execute_time = '2048-10-10 15:00:00',
    execute_image_name = 'done-3.png',
    execute_image_url = 'https://picsum.photos/seed/task-exec-3/220/220',
    execute_record = '按标准完成床上洗头，过程无异常，已提醒后续保暖。'
WHERE id = 3;

UPDATE zz_task_schedule
SET cancel_reason = '家属临时请假，任务取消',
    cancel_by = '管理员',
    cancel_time = '2048-10-10 15:00:00'
WHERE id = 9;

UPDATE zz_task_schedule
SET cancel_reason = '老人外出就医，任务取消',
    cancel_by = '管理员',
    cancel_time = '2048-10-10 15:00:00'
WHERE id = 10;

INSERT INTO zz_task_execute_record(task_id, execute_time, execute_by, execute_image_name, execute_image_url, execute_record)
VALUES
(1, '2048-10-10 15:00:00', '顾廷烨', 'done-1.png', 'https://picsum.photos/seed/task-exec-1/220/220', '今日已完成洗发业务，头发光亮一如，出油情况较为明显，已使用控油洗发水，根部养生精示，并未发现掉发现象。'),
(2, '2048-10-10 15:00:00', '顾廷烨', 'done-2.png', 'https://picsum.photos/seed/task-exec-2/220/220', '护理过程平稳，老人配合度良好，已完成口腔清洁并做基本检查。'),
(3, '2048-10-10 15:00:00', '顾雁开', 'done-3.png', 'https://picsum.photos/seed/task-exec-3/220/220', '按标准完成床上洗头，过程无异常，已提醒后续保暖。');
