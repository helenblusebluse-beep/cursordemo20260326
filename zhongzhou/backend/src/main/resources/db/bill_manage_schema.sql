SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP TABLE IF EXISTS zz_bill_cancel_record;
DROP TABLE IF EXISTS zz_bill_refund_record;
DROP TABLE IF EXISTS zz_bill_item;
DROP TABLE IF EXISTS zz_bill_manage;

CREATE TABLE zz_bill_manage (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  bill_no VARCHAR(32) NOT NULL COMMENT '账单编号',
  bill_type VARCHAR(20) NOT NULL DEFAULT '月度账单' COMMENT '账单类型',
  bill_month VARCHAR(7) NOT NULL COMMENT '账单月份',
  elder_name VARCHAR(32) NOT NULL COMMENT '老人姓名',
  elder_id_no VARCHAR(32) NOT NULL DEFAULT '' COMMENT '老人身份证号',
  bed_no VARCHAR(32) NOT NULL DEFAULT '' COMMENT '床位号',
  related_order_no VARCHAR(32) NOT NULL DEFAULT '' COMMENT '关联订单',
  bill_total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '账单金额',
  payable_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '应付金额',
  prepay_deduct_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '预缴款抵扣',
  bill_period_start DATE NOT NULL COMMENT '账单周期开始',
  bill_period_end DATE NOT NULL COMMENT '账单周期结束',
  total_days INT NOT NULL DEFAULT 0 COMMENT '共计天数',
  pay_deadline DATETIME NOT NULL COMMENT '支付截止时间',
  trade_status TINYINT NOT NULL DEFAULT 1 COMMENT '1待支付 2已支付 3已关闭 4已关闭(已支付退款成功)',
  created_by VARCHAR(20) NOT NULL DEFAULT '' COMMENT '创建人',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (id),
  UNIQUE KEY uk_bill_no (bill_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单管理';

CREATE TABLE zz_bill_item (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  bill_id BIGINT UNSIGNED NOT NULL COMMENT '账单ID',
  item_group VARCHAR(20) NOT NULL COMMENT '类型(添加项/扣减项/汇总)',
  fee_item_name VARCHAR(32) NOT NULL COMMENT '费用项目',
  service_content VARCHAR(64) NOT NULL DEFAULT '' COMMENT '服务内容',
  amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '金额',
  sort_no INT NOT NULL DEFAULT 1 COMMENT '排序',
  PRIMARY KEY (id),
  KEY idx_bill_item_bill (bill_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单明细';

CREATE TABLE zz_bill_refund_record (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  bill_id BIGINT UNSIGNED NOT NULL COMMENT '账单ID',
  applicant VARCHAR(20) NOT NULL COMMENT '申请人',
  applicant_type VARCHAR(20) NOT NULL DEFAULT '后台用户' COMMENT '申请人类型',
  submit_time DATETIME NOT NULL COMMENT '提交时间',
  refund_method VARCHAR(20) NOT NULL DEFAULT '预缴款' COMMENT '退款方式',
  actual_days INT NOT NULL DEFAULT 0 COMMENT '实际天数',
  refund_days INT NOT NULL DEFAULT 0 COMMENT '退款天数',
  refund_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '实退金额',
  PRIMARY KEY (id),
  KEY idx_bill_refund_bill (bill_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单退款记录';

CREATE TABLE zz_bill_cancel_record (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  bill_id BIGINT UNSIGNED NOT NULL COMMENT '账单ID',
  cancel_by VARCHAR(20) NOT NULL COMMENT '取消人',
  cancel_user_type VARCHAR(20) NOT NULL DEFAULT '后台用户' COMMENT '取消人类型',
  cancel_time DATETIME NOT NULL COMMENT '取消时间',
  cancel_reason VARCHAR(100) NOT NULL COMMENT '取消原因',
  PRIMARY KEY (id),
  KEY idx_bill_cancel_bill (bill_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单取消记录';

INSERT INTO zz_bill_manage
(id, bill_no, bill_type, bill_month, elder_name, elder_id_no, bed_no, related_order_no, bill_total_amount, payable_amount, prepay_deduct_amount, bill_period_start, bill_period_end, total_days, pay_deadline, trade_status, created_by, created_time)
VALUES
(1, 'ZD2048101015000001', '月度账单', '2048-10', '高启强', '230203197701291029', '201', 'DD2048101015000001', 3590.00, 2590.00, 1000.00, '2048-10-01', '2048-10-21', 21, '2023-10-06 00:00:00', 2, '顾廷烨', '2022-10-01 00:00:00'),
(2, 'ZF2048101015000002', '费用账单', '2048-10', '安欣', '230203197701291028', '1011', 'DD2048101015000002', 20.00, 20.00, 0.00, '2048-10-01', '2048-10-01', 1, '2023-10-01 00:15:00', 1, '系统', '2022-10-01 00:00:00'),
(3, 'ZD2048101015000003', '月度账单', '2048-10', '陈书婷', '230203197701291030', '1024', 'DD2048101015000009', 1200.00, 1200.00, 0.00, '2048-10-01', '2048-10-12', 12, '2023-10-02 00:00:00', 4, '顾廷烨', '2022-10-01 00:00:00'),
(4, 'ZF2048101015000004', '费用账单', '2048-10', '徐江', '230203197701291031', '1021', 'DD2048101015000010', 50.00, 50.00, 0.00, '2048-10-05', '2048-10-05', 1, '2023-10-05 12:00:00', 3, '系统', '2022-10-01 00:00:00');

INSERT INTO zz_bill_item (bill_id, item_group, fee_item_name, service_content, amount, sort_no) VALUES
(1, '添加项', '护理费用', '特级护理等级', 2000.00, 1),
(1, '添加项', '床位费用', '特护房', 1700.00, 2),
(1, '添加项', '其他费用', '其他', 0.00, 3),
(1, '汇总', '小计', '', 3700.00, 4),
(1, '扣减项', '医保支付', '', 0.00, 5),
(1, '扣减项', '政府补贴', '', 0.00, 6),
(1, '汇总', '小计', '', 0.00, 7),
(1, '汇总', '每月应付', '', 3700.00, 8),
(1, '汇总', '本期应付', '', 2590.00, 9),
(1, '汇总', '押金', '', 1000.00, 10),
(1, '汇总', '账单金额', '', 3590.00, 11),
(1, '汇总', '预缴款支付', '', 1000.00, 12),
(1, '汇总', '应付金额', '', 2590.00, 13),
(2, '添加项', '服务下单', '助浴', 20.00, 1),
(2, '汇总', '应付金额', '', 20.00, 2),
(3, '添加项', '护理费用', '一级护理', 1200.00, 1),
(3, '汇总', '应付金额', '', 1200.00, 2),
(4, '添加项', '服务下单', '理发', 50.00, 1),
(4, '汇总', '应付金额', '', 50.00, 2);

INSERT INTO zz_bill_refund_record
(bill_id, applicant, applicant_type, submit_time, refund_method, actual_days, refund_days, refund_amount)
VALUES
(3, '顾廷烨', '后台用户', '2048-10-05 13:00:00', '预缴款', 18, 12, 1800.00);

INSERT INTO zz_bill_cancel_record
(bill_id, cancel_by, cancel_user_type, cancel_time, cancel_reason)
VALUES
(4, '顾廷烨', '后台用户', '2048-10-05 13:00:00', '老人没有经济来源，无法支付账单，现已离院。');
