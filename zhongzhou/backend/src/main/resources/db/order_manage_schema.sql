SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP TABLE IF EXISTS zz_order_execute_record;
DROP TABLE IF EXISTS zz_order_payment_record;
DROP TABLE IF EXISTS zz_order_refund_record;
DROP TABLE IF EXISTS zz_order_manage;

CREATE TABLE zz_order_manage (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  order_no VARCHAR(32) NOT NULL COMMENT '订单编号',
  elder_name VARCHAR(32) NOT NULL COMMENT '老人姓名',
  bed_no VARCHAR(10) NOT NULL COMMENT '床位号',
  nursing_item_name VARCHAR(32) NOT NULL COMMENT '护理项目',
  nursing_item_type VARCHAR(20) NOT NULL DEFAULT '护理计划外' COMMENT '项目类型',
  order_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '订单金额',
  expected_service_time DATETIME NOT NULL COMMENT '期望服务时间',
  order_user_name VARCHAR(20) NOT NULL COMMENT '下单人',
  order_user_mobile VARCHAR(20) NOT NULL DEFAULT '' COMMENT '下单人手机号',
  order_time DATETIME NOT NULL COMMENT '下单时间',
  remark VARCHAR(255) NOT NULL DEFAULT '' COMMENT '备注',
  pay_time DATETIME DEFAULT NULL COMMENT '支付时间',
  execute_time DATETIME DEFAULT NULL COMMENT '执行时间',
  finish_time DATETIME DEFAULT NULL COMMENT '完成时间',
  close_time DATETIME DEFAULT NULL COMMENT '关闭时间',
  close_type VARCHAR(20) NOT NULL DEFAULT '' COMMENT '关闭类型(超时/手动关闭)',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '1待支付 2待执行 3已执行 4已完成 5已关闭 6已退款',
  refund_status TINYINT NOT NULL DEFAULT 0 COMMENT '0无 1处理中 2成功 3失败',
  cancel_reason VARCHAR(100) NOT NULL DEFAULT '' COMMENT '取消原因',
  cancel_by VARCHAR(20) NOT NULL DEFAULT '' COMMENT '取消人',
  cancel_user_type VARCHAR(20) NOT NULL DEFAULT '' COMMENT '取消人类型(前台客户/后台用户)',
  cancel_time DATETIME DEFAULT NULL COMMENT '取消时间',
  refund_reason VARCHAR(100) NOT NULL DEFAULT '' COMMENT '退款原因',
  refund_time DATETIME DEFAULT NULL COMMENT '退款时间',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (id),
  UNIQUE KEY uk_order_no (order_no),
  KEY idx_order_status (status),
  KEY idx_order_time (order_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单管理';

CREATE TABLE zz_order_refund_record (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  order_id BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
  order_no VARCHAR(32) NOT NULL COMMENT '订单编号',
  refund_status_label VARCHAR(20) NOT NULL DEFAULT '' COMMENT '退款状态文案',
  applicant VARCHAR(20) NOT NULL DEFAULT '' COMMENT '申请人',
  applicant_type VARCHAR(20) NOT NULL DEFAULT '' COMMENT '申请人类型',
  apply_time DATETIME DEFAULT NULL COMMENT '申请时间',
  refund_reason VARCHAR(100) NOT NULL COMMENT '退款原因',
  refund_channel VARCHAR(20) NOT NULL DEFAULT '原路返回' COMMENT '退款渠道',
  refund_method VARCHAR(20) NOT NULL DEFAULT '微信' COMMENT '退款方式',
  refund_no VARCHAR(50) NOT NULL DEFAULT '' COMMENT '退款编号',
  refund_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '退款金额',
  fail_code VARCHAR(20) NOT NULL DEFAULT '' COMMENT '失败状态码',
  fail_reason VARCHAR(100) NOT NULL DEFAULT '' COMMENT '失败原因',
  status TINYINT NOT NULL DEFAULT 2 COMMENT '1处理中 2成功 3失败',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  KEY idx_refund_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单退款记录';

CREATE TABLE zz_order_payment_record (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  order_id BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
  trade_status VARCHAR(20) NOT NULL COMMENT '交易状态(待支付/已支付/已关闭)',
  pay_channel VARCHAR(20) NOT NULL DEFAULT '' COMMENT '支付渠道',
  pay_method VARCHAR(20) NOT NULL DEFAULT '' COMMENT '支付方式',
  wx_order_no VARCHAR(64) NOT NULL DEFAULT '' COMMENT '微信支付订单号',
  pay_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '支付金额',
  pay_time DATETIME DEFAULT NULL COMMENT '支付时间',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  KEY idx_pay_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单支付记录';

CREATE TABLE zz_order_execute_record (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  order_id BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
  execute_by VARCHAR(20) NOT NULL COMMENT '执行人',
  execute_image_url VARCHAR(500) NOT NULL DEFAULT '' COMMENT '执行图片',
  execute_record VARCHAR(200) NOT NULL DEFAULT '' COMMENT '执行记录',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  KEY idx_exec_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单执行记录';

INSERT INTO zz_order_manage
(id, order_no, elder_name, bed_no, nursing_item_name, order_amount, expected_service_time, order_user_name, order_time, status, refund_status)
VALUES
(1, 'DD2048101015000001', '安欣', '1011', '口腔清洁', 2022.00, '2048-10-05 00:00:00', '顾廷烨', '2048-10-01 00:00:00', 2, 0),
(2, 'DD2048101015000002', '高启强', '1012', '面部清洁', 2200.22, '2048-10-05 00:00:00', '顾廷烨', '2048-10-01 00:00:00', 2, 0),
(3, 'DD2048101015000003', '孟钰', '1013', '床上洗头', 1900.01, '2048-10-05 00:00:00', '顾雁开', '2048-10-01 00:00:00', 3, 0),
(4, 'DD2048101015000004', '李响', '1014', '协助更衣', 1899.00, '2048-10-05 00:00:00', '顾雁开', '2048-10-01 00:00:00', 3, 0),
(5, 'DD2048101015000005', '安长林', '1015', '指甲护理', 1122.22, '2048-10-05 00:00:00', '盛堂兰', '2048-10-01 00:00:00', 1, 0),
(6, 'DD2048101015000006', '徐江', '1021', '足部清洁', 1272.00, '2048-10-05 00:00:00', '盛如兰', '2048-10-01 00:00:00', 4, 0),
(7, 'DD2048101015000007', '唐小龙', '1022', '足部清洁', 1299.34, '2048-10-05 00:00:00', '盛华兰', '2048-10-01 00:00:00', 6, 2),
(8, 'DD2048101015000008', '唐小虎', '1021', '协助更衣', 2341.00, '2048-10-05 00:00:00', '王若弗', '2048-10-01 00:00:00', 5, 0),
(9, 'DD2048101015000009', '陈书婷', '1024', '协助更衣', 1244.00, '2048-10-05 00:00:00', '林添源', '2048-10-01 00:00:00', 2, 0),
(10, 'DD2048101015000010', '杨健', '1035', '协助更衣', 1780.00, '2048-10-05 00:00:00', '盛长柏', '2048-10-01 00:00:00', 1, 0);

UPDATE zz_order_manage
SET refund_reason='家属申请退款', refund_time='2048-10-06 09:20:00'
WHERE id=7;

UPDATE zz_order_manage
SET nursing_item_type='护理计划外', order_user_mobile='15998888889', remark='老人头上有一块疤，洗头的时候需要注意一下。',
    pay_time='2048-10-01 00:10:00'
WHERE id in (1,2,3,4,6,7,8,9);

UPDATE zz_order_manage
SET execute_time='2048-10-01 00:20:00'
WHERE id in (3,4,7);

UPDATE zz_order_manage
SET finish_time='2048-10-01 01:00:00'
WHERE id in (6);

UPDATE zz_order_manage
SET close_time='2048-10-01 00:15:00', close_type='手动关闭', cancel_reason='费用有点贵', cancel_by='顾廷烨', cancel_user_type='后台用户', cancel_time='2048-10-01 00:15:00'
WHERE id in (8);

UPDATE zz_order_manage
SET close_time='2048-10-01 00:15:00', close_type='超时', cancel_reason='超过支付时间自动关闭', cancel_by='系统', cancel_user_type='后台用户', cancel_time='2048-10-01 00:15:00'
WHERE id in (5);

UPDATE zz_order_manage
SET refund_reason='高先生觉得服务不满意，护理人员指甲太长'
WHERE id in (7);

INSERT INTO zz_order_refund_record(order_id, order_no, refund_status_label, applicant, applicant_type, apply_time, refund_reason, refund_channel, refund_method, refund_no, refund_amount, fail_code, fail_reason, status)
VALUES
(7, 'DD2048101015000007', '退款成功', '顾廷烨', '后台用户', '2048-10-23 19:00:00', '高先生觉得服务不满意，护理人员指甲太长', '原路返回', '微信', '50000000382019052709732678859', 20.00, '', '', 2),
(9, 'DD2048101015000009', '退款处理中', '顾廷烨', '后台用户', '2048-10-23 19:00:00', '高先生觉得服务不满意，护理人员指甲太长', '原路返回', '微信', '', 1244.00, '', '', 1),
(4, 'DD2048101015000004', '退款失败', '顾廷烨', '后台用户', '2048-10-23 19:00:00', '高先生觉得服务不满意，护理人员指甲太长', '原路返回', '微信', '', 1899.00, '403', '余额不足', 3);

INSERT INTO zz_order_payment_record(order_id, trade_status, pay_channel, pay_method, wx_order_no, pay_amount, pay_time)
VALUES
(1, '已支付', '线上支付', '微信', '1217752501201407033233368018', 2022.00, '2048-10-01 00:10:00'),
(2, '已支付', '线上支付', '微信', '1217752501201407033233368019', 2200.22, '2048-10-01 00:10:00'),
(3, '已支付', '线上支付', '微信', '1217752501201407033233368020', 1900.01, '2048-10-01 00:10:00'),
(4, '已支付', '线上支付', '微信', '1217752501201407033233368021', 1899.00, '2048-10-01 00:10:00'),
(5, '待支付', '', '', '', 1122.22, NULL),
(6, '已支付', '线上支付', '微信', '1217752501201407033233368022', 1272.00, '2048-10-01 00:10:00'),
(7, '已支付', '线上支付', '微信', '1217752501201407033233368023', 1299.34, '2048-10-01 00:10:00'),
(8, '已关闭', '', '', '', 2341.00, NULL),
(9, '已支付', '线上支付', '微信', '1217752501201407033233368024', 1244.00, '2048-10-01 00:10:00'),
(10, '待支付', '', '', '', 1780.00, NULL);

INSERT INTO zz_order_execute_record(order_id, execute_by, execute_image_url, execute_record)
VALUES
(3, '顾廷烨', 'https://picsum.photos/seed/order-exec-3/220/220', '今日已完成高先生的洗头服务，头发发质一靓，出油情况较为明显，已使用控油洗发水，根据高先生指示，并未使用护发素。'),
(4, '顾雁开', 'https://picsum.photos/seed/order-exec-4/220/220', '今日已完成协助更衣服务，过程平稳，老人反馈良好。'),
(6, '盛堂兰', 'https://picsum.photos/seed/order-exec-6/220/220', '足部清洁已完成，已做基础护理。'),
(7, '盛华兰', 'https://picsum.photos/seed/order-exec-7/220/220', '足部清洁执行完成，后续进入退款流程。');
