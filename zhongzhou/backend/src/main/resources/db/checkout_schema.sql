DROP TABLE IF EXISTS zz_checkout_application;
DROP TABLE IF EXISTS zz_checkout_fee_item;

CREATE TABLE zz_checkout_application (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  checkin_id BIGINT UNSIGNED NOT NULL COMMENT '入住申请ID',
  elder_name VARCHAR(32) NOT NULL COMMENT '老人姓名',
  id_card VARCHAR(18) NOT NULL COMMENT '老人身份证号',
  room_no VARCHAR(32) DEFAULT NULL COMMENT '入住床位',
  care_level VARCHAR(32) DEFAULT NULL COMMENT '护理等级',
  contact_phone VARCHAR(16) DEFAULT NULL COMMENT '联系方式',
  checkin_period VARCHAR(64) DEFAULT NULL COMMENT '入住期限',
  fee_period VARCHAR(64) DEFAULT NULL COMMENT '费用期限',
  checkout_date DATETIME NOT NULL COMMENT '退住日期',
  checkout_reason VARCHAR(64) DEFAULT NULL COMMENT '退住原因',
  agreement_date DATETIME DEFAULT NULL COMMENT '解除日期',
  agreement_file_name VARCHAR(255) DEFAULT NULL COMMENT '解除协议',
  caregiver_names VARCHAR(200) DEFAULT NULL COMMENT '护理员姓名',
  refund_voucher_name VARCHAR(255) DEFAULT NULL COMMENT '退款凭证文件名',
  refund_voucher_url VARCHAR(500) DEFAULT NULL COMMENT '退款凭证图片URL',
  refund_method VARCHAR(32) DEFAULT NULL COMMENT '退款方式',
  refund_remark VARCHAR(50) DEFAULT NULL COMMENT '退款备注',
  voucher_submitter VARCHAR(64) DEFAULT NULL COMMENT '凭证提交人',
  voucher_submit_time DATETIME DEFAULT NULL COMMENT '凭证提交时间',
  final_refund_amount DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '最终退款金额',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1草稿 2已提交',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY idx_checkout_name (elder_name),
  KEY idx_checkout_idcard (id_card)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退住办理表';

CREATE TABLE zz_checkout_fee_item (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  application_id BIGINT UNSIGNED NOT NULL COMMENT '退住申请ID',
  fee_category TINYINT NOT NULL COMMENT '费用分类:1应退 2欠费 3余额 4未缴',
  bill_no VARCHAR(64) NOT NULL COMMENT '账单编号',
  bill_month VARCHAR(16) DEFAULT NULL COMMENT '账单月份',
  item_name VARCHAR(64) DEFAULT NULL COMMENT '费用项目',
  amount DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '金额',
  actual_days INT DEFAULT NULL COMMENT '实际天数',
  refund_days INT DEFAULT NULL COMMENT '退款天数',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1有效 2已取消',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  KEY idx_checkout_fee_application (application_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退住费用明细表';

INSERT INTO zz_checkout_application
(id, checkin_id, elder_name, id_card, room_no, care_level, contact_phone, checkin_period, fee_period, checkout_date, checkout_reason, agreement_date, agreement_file_name, final_refund_amount, status, is_deleted, created_time, updated_time)
VALUES
(1,1,'安权','230203199701221029','A区-101','特级护理等级','13875568891','2048-10-10~2048-10-31','2048-10-10~2048-10-31','2048-10-10 00:00:00','老人个人原因','2048-10-10 00:00:00','agreement-1.pdf',-6000,2,0,'2048-10-10 15:00:00','2048-10-10 15:00:00'),
(2,2,'高启强','230203199701221029','A区-101','特级护理等级','13875568892','2048-10-10~2048-10-31','2048-10-10~2048-10-31','2048-10-10 00:00:00','服务不高','2048-10-09 00:00:00','agreement-2.pdf',-6000,2,0,'2048-10-09 15:00:00','2048-10-09 15:00:00'),
(3,3,'陈布','230203199701221029','A区-101','特级护理等级','13875568893','2048-10-10~2048-10-31','2048-10-10~2048-10-31','2048-10-10 00:00:00','管理混乱','2048-10-08 00:00:00','agreement-3.pdf',-6000,2,0,'2048-10-08 15:00:00','2048-10-08 15:00:00'),
(4,4,'李有田','230203199701221029','A区-101','特级护理等级','13875568894','2048-10-10~2048-10-31','2048-10-10~2048-10-31','2048-10-10 00:00:00','费用高昂','2048-10-07 00:00:00','agreement-4.pdf',-6000,2,0,'2048-10-07 15:00:00','2048-10-07 15:00:00'),
(5,5,'安长林','230203199701221029','A区-101','特级护理等级','13875568895','2048-10-10~2048-10-31','2048-10-10~2048-10-31','2048-10-10 00:00:00','其他','2048-10-06 00:00:00','agreement-5.pdf',-6000,2,0,'2048-10-06 15:00:00','2048-10-06 15:00:00'),
(6,6,'徐江','230203199701221029','A区-101','特级护理等级','13875568896','2048-10-10~2048-10-31','2048-10-10~2048-10-31','2048-10-10 00:00:00','服务不高','2048-10-05 00:00:00','agreement-6.pdf',-6000,2,0,'2048-10-05 15:00:00','2048-10-05 15:00:00'),
(7,7,'唐小龙','230203199701221029','A区-101','特级护理等级','13875568897','2048-10-10~2048-10-31','2048-10-10~2048-10-31','2048-10-10 00:00:00','其他','2048-10-04 00:00:00','agreement-7.pdf',-6000,2,0,'2048-10-04 15:00:00','2048-10-04 15:00:00'),
(8,8,'唐小虎','230203199701221029','A区-101','特级护理等级','13875568898','2048-10-10~2048-10-31','2048-10-10~2048-10-31','2048-10-10 00:00:00','老人个人原因','2048-10-03 00:00:00','agreement-8.pdf',-6000,2,0,'2048-10-03 15:00:00','2048-10-03 15:00:00'),
(9,9,'陈书婷','230203199701221029','A区-101','特级护理等级','13875568899','2048-10-10~2048-10-31','2048-10-10~2048-10-31','2048-10-10 00:00:00','管理混乱','2048-10-02 00:00:00','agreement-9.pdf',-6000,2,0,'2048-10-02 15:00:00','2048-10-02 15:00:00'),
(10,10,'杨健','230203199701221029','A区-101','特级护理等级','13875568900','2048-10-10~2048-10-31','2048-10-10~2048-10-31','2048-10-10 00:00:00','费用高昂','2048-10-01 00:00:00','agreement-10.pdf',-6000,2,0,'2048-10-01 15:00:00','2048-10-01 15:00:00'),
(11,11,'王建国','230203199701221039','A区-101','特级护理等级','13875568901','2048-09-30~2048-10-31','2048-09-30~2048-10-31','2048-09-30 00:00:00','其他','2048-09-30 00:00:00','agreement-11.pdf',-6000,2,0,'2048-09-30 10:00:00','2048-09-30 10:00:00'),
(12,12,'李慧芳','230203199701221040','A区-101','特级护理等级','13875568902','2048-09-29~2048-10-31','2048-09-29~2048-10-31','2048-09-29 00:00:00','老人个人原因','2048-09-29 00:00:00','agreement-12.pdf',-6000,2,0,'2048-09-29 11:00:00','2048-09-29 11:00:00'),
(13,13,'赵明','230203199701221041','A区-101','特级护理等级','13875568903','2048-09-28~2048-10-31','2048-09-28~2048-10-31','2048-09-28 00:00:00','管理混乱','2048-09-28 00:00:00','agreement-13.pdf',-6000,2,0,'2048-09-28 09:30:00','2048-09-28 09:30:00'),
(14,14,'周婷','230203199701221042','A区-101','特级护理等级','13875568904','2048-09-27~2048-10-31','2048-09-27~2048-10-31','2048-09-27 00:00:00','服务不高','2048-09-27 00:00:00','agreement-14.pdf',-6000,2,0,'2048-09-27 14:20:00','2048-09-27 14:20:00'),
(15,15,'吴浩','230203199701221043','A区-101','特级护理等级','13875568905','2048-09-26~2048-10-31','2048-09-26~2048-10-31','2048-09-26 00:00:00','其他','2048-09-26 00:00:00','agreement-15.pdf',-6000,2,0,'2048-09-26 15:40:00','2048-09-26 15:40:00'),
(16,16,'郑丽','230203199701221044','A区-101','特级护理等级','13875568906','2048-09-25~2048-10-31','2048-09-25~2048-10-31','2048-09-25 00:00:00','老人个人原因','2048-09-25 00:00:00','agreement-16.pdf',-6000,2,0,'2048-09-25 08:15:00','2048-09-25 08:15:00'),
(17,17,'冯涛','230203199701221045','A区-101','特级护理等级','13875568907','2048-09-24~2048-10-31','2048-09-24~2048-10-31','2048-09-24 00:00:00','服务不高','2048-09-24 00:00:00','agreement-17.pdf',-6000,2,0,'2048-09-24 16:10:00','2048-09-24 16:10:00'),
(18,18,'褚敏','230203199701221046','A区-101','特级护理等级','13875568908','2048-09-23~2048-10-31','2048-09-23~2048-10-31','2048-09-23 00:00:00','管理混乱','2048-09-23 00:00:00','agreement-18.pdf',-6000,2,0,'2048-09-23 13:05:00','2048-09-23 13:05:00'),
(19,19,'卫星','230203199701221047','A区-101','特级护理等级','13875568909','2048-09-22~2048-10-31','2048-09-22~2048-10-31','2048-09-22 00:00:00','费用高昂','2048-09-22 00:00:00','agreement-19.pdf',-6000,2,0,'2048-09-22 09:50:00','2048-09-22 09:50:00'),
(20,20,'蒋燕','230203199701221048','A区-101','特级护理等级','13875568910','2048-09-21~2048-10-31','2048-09-21~2048-10-31','2048-09-21 00:00:00','其他','2048-09-21 00:00:00','agreement-20.pdf',-6000,2,0,'2048-09-21 10:25:00','2048-09-21 10:25:00'),
(21,21,'沈洋','230203199701221049','A区-101','特级护理等级','13875568911','2048-09-20~2048-10-31','2048-09-20~2048-10-31','2048-09-20 00:00:00','老人个人原因','2048-09-20 00:00:00','agreement-21.pdf',-6000,2,0,'2048-09-20 14:55:00','2048-09-20 14:55:00'),
(22,22,'韩雪','230203199701221050','A区-101','特级护理等级','13875568912','2048-09-19~2048-10-31','2048-09-19~2048-10-31','2048-09-19 00:00:00','管理混乱','2048-09-19 00:00:00','agreement-22.pdf',-6000,2,0,'2048-09-19 11:45:00','2048-09-19 11:45:00'),
(23,23,'杨凯','230203199701221051','A区-101','特级护理等级','13875568913','2048-09-18~2048-10-31','2048-09-18~2048-10-31','2048-09-18 00:00:00','服务不高','2048-09-18 00:00:00','agreement-23.pdf',-6000,2,0,'2048-09-18 08:30:00','2048-09-18 08:30:00'),
(24,24,'朱红','230203199701221052','A区-101','特级护理等级','13875568914','2048-09-17~2048-10-31','2048-09-17~2048-10-31','2048-09-17 00:00:00','费用高昂','2048-09-17 00:00:00','agreement-24.pdf',-6000,2,0,'2048-09-17 15:15:00','2048-09-17 15:15:00'),
(25,25,'秦峰','230203199701221053','A区-101','特级护理等级','13875568915','2048-09-16~2048-10-31','2048-09-16~2048-10-31','2048-09-16 00:00:00','其他','2048-09-16 00:00:00','agreement-25.pdf',-6000,2,0,'2048-09-16 12:40:00','2048-09-16 12:40:00'),
(26,26,'尤佳','230203199701221054','A区-101','特级护理等级','13875568916','2048-09-15~2048-10-31','2048-09-15~2048-10-31','2048-09-15 00:00:00','老人个人原因','2048-09-15 00:00:00','agreement-26.pdf',-6000,2,0,'2048-09-15 09:20:00','2048-09-15 09:20:00'),
(27,27,'许宁','230203199701221055','A区-101','特级护理等级','13875568917','2048-09-14~2048-10-31','2048-09-14~2048-10-31','2048-09-14 00:00:00','管理混乱','2048-09-14 00:00:00','agreement-27.pdf',-6000,2,0,'2048-09-14 16:00:00','2048-09-14 16:00:00'),
(28,28,'何倩','230203199701221056','A区-101','特级护理等级','13875568918','2048-09-13~2048-10-31','2048-09-13~2048-10-31','2048-09-13 00:00:00','服务不高','2048-09-13 00:00:00','agreement-28.pdf',-6000,2,0,'2048-09-13 10:10:00','2048-09-13 10:10:00'),
(29,29,'吕阳','230203199701221057','A区-101','特级护理等级','13875568919','2048-09-12~2048-10-31','2048-09-12~2048-10-31','2048-09-12 00:00:00','费用高昂','2048-09-12 00:00:00','agreement-29.pdf',-6000,2,0,'2048-09-12 14:35:00','2048-09-12 14:35:00'),
(30,30,'施静','230203199701221058','A区-101','特级护理等级','13875568920','2048-09-11~2048-10-31','2048-09-11~2048-10-31','2048-09-11 00:00:00','其他','2048-09-11 00:00:00','agreement-30.pdf',-6000,2,0,'2048-09-11 09:05:00','2048-09-11 09:05:00');

INSERT INTO zz_checkout_fee_item
(application_id, fee_category, bill_no, bill_month, item_name, amount, actual_days, refund_days, status, is_deleted)
VALUES
(1,1,'ZD2048101015000001','2048-10','月度账单',2000,18,12,1,0),
(1,1,'ZD2048101015000002','2048-10','费用账单',20,0,0,1,0),
(1,2,'QD2048101015000001','2048-10','月度账单',2000,0,0,1,0),
(1,3,'YE2048101015000001','2048-10','押金余额',2000,0,0,1,0),
(1,4,'WJ2048101015000001','2048-10','费用账单',20,0,0,1,0),
(2,1,'ZD2048101015000003','2048-10','月度账单',2000,20,10,1,0),
(2,2,'QD2048101015000002','2048-10','月度账单',2000,0,0,1,0),
(2,3,'YE2048101015000002','2048-10','押金余额',2000,0,0,1,0),
(2,4,'WJ2048101015000002','2048-10','费用账单',20,0,0,1,0),
(3,1,'ZD2048101015000004','2048-10','月度账单',2000,23,7,1,0),
(3,2,'QD2048101015000003','2048-10','月度账单',2000,0,0,1,0),
(3,3,'YE2048101015000003','2048-10','押金余额',2000,0,0,1,0),
(3,4,'WJ2048101015000003','2048-10','费用账单',20,0,0,1,0),
(4,1,'ZD2048101015000005','2048-10','月度账单',2000,19,11,1,0),
(4,2,'QD2048101015000004','2048-10','月度账单',2000,0,0,1,0),
(4,3,'YE2048101015000004','2048-10','押金余额',2000,0,0,1,0),
(4,4,'WJ2048101015000004','2048-10','费用账单',20,0,0,1,0),
(5,1,'ZD2048101015000006','2048-10','月度账单',2000,21,9,1,0),
(5,2,'QD2048101015000005','2048-10','月度账单',2000,0,0,1,0),
(5,3,'YE2048101015000005','2048-10','押金余额',2000,0,0,1,0),
(5,4,'WJ2048101015000005','2048-10','费用账单',20,0,0,1,0);

INSERT INTO zz_checkout_fee_item (application_id, fee_category, bill_no, bill_month, item_name, amount, actual_days, refund_days, status, is_deleted)
SELECT id, 1, CONCAT('ZD', LPAD(id, 16, '0')), '2048-10', '月度账单', 2000, 18, 12, 1, 0
FROM zz_checkout_application WHERE id BETWEEN 6 AND 30;

INSERT INTO zz_checkout_fee_item (application_id, fee_category, bill_no, bill_month, item_name, amount, actual_days, refund_days, status, is_deleted)
SELECT id, 1, CONCAT('ZF', LPAD(id, 16, '0')), '2048-10', '费用账单', 20, 0, 0, 1, 0
FROM zz_checkout_application WHERE id BETWEEN 6 AND 30;

INSERT INTO zz_checkout_fee_item (application_id, fee_category, bill_no, bill_month, item_name, amount, actual_days, refund_days, status, is_deleted)
SELECT id, 2, CONCAT('Q1', LPAD(id, 16, '0')), '2048-10', '月度账单', 2000, 0, 0, 1, 0
FROM zz_checkout_application WHERE id BETWEEN 6 AND 30;

INSERT INTO zz_checkout_fee_item (application_id, fee_category, bill_no, bill_month, item_name, amount, actual_days, refund_days, status, is_deleted)
SELECT id, 2, CONCAT('Q2', LPAD(id, 16, '0')), '2048-10', '费用账单', 2000, 0, 0, 1, 0
FROM zz_checkout_application WHERE id BETWEEN 6 AND 30;

INSERT INTO zz_checkout_fee_item (application_id, fee_category, bill_no, bill_month, item_name, amount, actual_days, refund_days, status, is_deleted)
SELECT id, 2, CONCAT('Q3', LPAD(id, 16, '0')), '2048-10', '费用账单', 2000, 0, 0, 1, 0
FROM zz_checkout_application WHERE id BETWEEN 6 AND 30;

INSERT INTO zz_checkout_fee_item (application_id, fee_category, bill_no, bill_month, item_name, amount, actual_days, refund_days, status, is_deleted)
SELECT id, 3, CONCAT('YJ', LPAD(id, 16, '0')), '2048-10', '押金余额', 2000, 0, 0, 1, 0
FROM zz_checkout_application WHERE id BETWEEN 6 AND 30;

INSERT INTO zz_checkout_fee_item (application_id, fee_category, bill_no, bill_month, item_name, amount, actual_days, refund_days, status, is_deleted)
SELECT id, 3, CONCAT('YY', LPAD(id, 16, '0')), '2048-10', '押金余额', 20, 0, 0, 1, 0
FROM zz_checkout_application WHERE id BETWEEN 6 AND 30;

INSERT INTO zz_checkout_fee_item (application_id, fee_category, bill_no, bill_month, item_name, amount, actual_days, refund_days, status, is_deleted)
SELECT id, 4, CONCAT('W1', LPAD(id, 16, '0')), '2048-10', '费用账单', 2000, 0, 0, 1, 0
FROM zz_checkout_application WHERE id BETWEEN 6 AND 30;

INSERT INTO zz_checkout_fee_item (application_id, fee_category, bill_no, bill_month, item_name, amount, actual_days, refund_days, status, is_deleted)
SELECT id, 4, CONCAT('W2', LPAD(id, 16, '0')), '2048-10', '费用账单', 2000, 0, 0, 1, 0
FROM zz_checkout_application WHERE id BETWEEN 6 AND 30;

INSERT INTO zz_checkout_fee_item (application_id, fee_category, bill_no, bill_month, item_name, amount, actual_days, refund_days, status, is_deleted)
SELECT id, 4, CONCAT('W3', LPAD(id, 16, '0')), '2048-10', '费用账单', 2000, 0, 0, 1, 0
FROM zz_checkout_application WHERE id BETWEEN 6 AND 30;

-- 已退住详情演示：id=1 与原型一致（欠费 3 笔、凭证信息）
UPDATE zz_checkin_application SET home_address='广东江门市蓬江区三十三墟街' WHERE id=1;

INSERT INTO zz_checkout_fee_item (application_id, fee_category, bill_no, bill_month, item_name, amount, actual_days, refund_days, status, is_deleted)
VALUES
(1,2,'QD2048101015000002','2048-10','月度账单',2000,0,0,1,0),
(1,2,'QD2048101015000003','2048-10','费用账单',2000,0,0,1,0);

UPDATE zz_checkout_application SET
  elder_name='高启强',
  id_card='230203197702221029',
  room_no='101床位',
  care_level='特级护理等级',
  contact_phone='13898988888',
  checkin_period='2048-10-10~2049-10-10',
  fee_period='2048-10-10~2049-10-10',
  checkout_date='2048-10-15 00:00:00',
  checkout_reason='服务不周',
  agreement_date='2024-05-05 00:00:00',
  agreement_file_name='高启强解除协议.pdf',
  caregiver_names='盛长柏、盛明兰、盛如兰',
  refund_method='现金',
  refund_voucher_name='voucher.jpg',
  refund_voucher_url='https://picsum.photos/seed/voucher1/240/160',
  refund_remark='共退款5520.00元，现金退款，已结清。',
  voucher_submitter='顾廷烨',
  voucher_submit_time='2022-10-05 15:00:00'
WHERE id=1;
