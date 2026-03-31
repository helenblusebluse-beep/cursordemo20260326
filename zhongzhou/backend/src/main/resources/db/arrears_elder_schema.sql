SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 欠费老人：为账单增加床位号；已存在列则跳过（可重复执行）
SET @c := (SELECT COUNT(*) FROM information_schema.COLUMNS
           WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'zz_bill_manage' AND COLUMN_NAME = 'bed_no');
SET @sql := IF(@c = 0,
  'ALTER TABLE zz_bill_manage ADD COLUMN bed_no VARCHAR(32) NOT NULL DEFAULT '''' COMMENT ''床位号'' AFTER elder_id_no',
  'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 历史账单补床位号（与余额查询原型一致）
UPDATE zz_bill_manage SET bed_no = '1011' WHERE id = 2;
UPDATE zz_bill_manage SET bed_no = '201' WHERE id = 1;
UPDATE zz_bill_manage SET bed_no = '1024' WHERE id = 3;
UPDATE zz_bill_manage SET bed_no = '1021' WHERE id = 4;

-- 安欣：多张逾期待支付，应付合计与最晚截止日用于列表聚合演示
UPDATE zz_bill_manage SET pay_deadline = '2024-01-01 00:00:00', payable_amount = 1500.00 WHERE id = 2;

INSERT IGNORE INTO zz_bill_manage
(id, bill_no, bill_type, bill_month, elder_name, elder_id_no, bed_no, related_order_no, bill_total_amount, payable_amount, prepay_deduct_amount, bill_period_start, bill_period_end, total_days, pay_deadline, trade_status, created_by, created_time, is_deleted)
VALUES
(5, 'ZD2048041015000005', '月度账单', '2048-04', '安欣', '230203197701291028', '1011', '', 3111.00, 2000.00, 0.00, '2048-04-01', '2048-04-30', 30, '2024-06-01 00:00:00', 1, '系统', '2022-10-01 00:00:00', 0),
(6, 'ZD2033061015000006', '月度账单', '2033-06', '安欣', '230203197701291028', '1011', '', 2000.00, 2000.00, 0.00, '2033-06-01', '2033-06-30', 30, '2024-05-01 00:00:00', 1, '系统', '2022-10-01 00:00:00', 0),
(7, 'ZD2048051015000007', '月度账单', '2048-05', '高启强', '230203197701291029', '1012', '', 3000.00, 2500.00, 0.00, '2048-05-01', '2048-05-31', 31, '2024-03-15 12:00:00', 1, '系统', '2022-10-01 00:00:00', 0),
(8, 'ZD2048061015000008', '月度账单', '2048-06', '陈泰', '110101196001011234', '1013', '', 2800.00, 2400.00, 0.00, '2048-06-01', '2048-06-30', 30, '2024-04-10 00:00:00', 1, '系统', '2022-10-01 00:00:00', 0),
(9, 'ZD2048071015000009', '月度账单', '2048-07', '李有田', '110101196002021234', '1014', '', 2600.00, 2300.00, 0.00, '2048-07-01', '2048-07-31', 31, '2024-02-20 00:00:00', 1, '系统', '2022-10-01 00:00:00', 0),
(10, 'ZD2048081015000010', '月度账单', '2048-08', '安长林', '110101196003031234', '1015', '', 2500.00, 2200.00, 0.00, '2048-08-01', '2048-08-31', 31, '2024-01-05 00:00:00', 1, '系统', '2022-10-01 00:00:00', 0),
(11, 'ZD2048091015000011', '月度账单', '2048-09', '徐江', '230203197701291031', '1021', '', 2400.00, 2100.00, 0.00, '2048-09-01', '2048-09-30', 30, '2024-07-01 00:00:00', 1, '系统', '2022-10-01 00:00:00', 0),
(12, 'ZD2048101015000012', '月度账单', '2048-10', '唐小龙', '110101196004041234', '1022', '', 2300.00, 2000.00, 0.00, '2048-10-01', '2048-10-31', 31, '2024-08-20 00:00:00', 1, '系统', '2022-10-01 00:00:00', 0),
(13, 'ZD2048111015000013', '月度账单', '2048-11', '唐小虎', '110101196005051234', '1021', '', 2200.00, 1900.00, 0.00, '2048-11-01', '2048-11-30', 30, '2024-09-01 00:00:00', 1, '系统', '2022-10-01 00:00:00', 0),
(14, 'ZD2048121015000014', '月度账单', '2048-12', '陈书婷', '230203197701291030', '1024', '', 2100.00, 1800.00, 0.00, '2048-12-01', '2048-12-31', 31, '2024-10-05 00:00:00', 1, '系统', '2022-10-01 00:00:00', 0),
(15, 'ZD2049011015000015', '月度账单', '2049-01', '杨健', '110101196006061234', '1035', '', 2000.00, 2500.00, 0.00, '2049-01-01', '2049-01-31', 31, '2024-11-01 00:00:00', 1, '系统', '2022-10-01 00:00:00', 0);

-- 若通过部分 Windows 终端管道执行 INSERT 时中文变为 ??，下面语句可纠正（幂等）
UPDATE zz_bill_manage SET elder_name = '安欣' WHERE id IN (5, 6);
UPDATE zz_bill_manage SET elder_name = '高启强' WHERE id = 7;
UPDATE zz_bill_manage SET elder_name = '陈泰' WHERE id = 8;
UPDATE zz_bill_manage SET elder_name = '李有田' WHERE id = 9;
UPDATE zz_bill_manage SET elder_name = '安长林' WHERE id = 10;
UPDATE zz_bill_manage SET elder_name = '徐江' WHERE id = 11;
UPDATE zz_bill_manage SET elder_name = '唐小龙' WHERE id = 12;
UPDATE zz_bill_manage SET elder_name = '唐小虎' WHERE id = 13;
UPDATE zz_bill_manage SET elder_name = '陈书婷' WHERE id = 14;
UPDATE zz_bill_manage SET elder_name = '杨健' WHERE id = 15;
