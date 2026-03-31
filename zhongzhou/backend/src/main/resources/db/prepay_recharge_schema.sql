SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP TABLE IF EXISTS zz_prepay_recharge;

CREATE TABLE zz_prepay_recharge (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  recharge_no VARCHAR(32) NOT NULL COMMENT '充值流水号',
  elder_name VARCHAR(32) NOT NULL COMMENT '老人姓名',
  elder_id_no VARCHAR(32) NOT NULL COMMENT '老人身份证号',
  bed_no VARCHAR(32) NOT NULL DEFAULT '' COMMENT '床位号',
  recharge_method VARCHAR(20) NOT NULL COMMENT '充值方式',
  recharge_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '充值金额',
  voucher_file_name VARCHAR(120) NOT NULL DEFAULT '' COMMENT '充值凭证文件名',
  voucher_file_url VARCHAR(255) NOT NULL DEFAULT '' COMMENT '充值凭证地址',
  recharge_remark VARCHAR(50) NOT NULL DEFAULT '' COMMENT '充值备注',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (id),
  UNIQUE KEY uk_recharge_no (recharge_no),
  KEY idx_recharge_elder (elder_name, bed_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预缴款充值记录';

INSERT INTO zz_prepay_recharge
(id, recharge_no, elder_name, elder_id_no, bed_no, recharge_method, recharge_amount, voucher_file_name, voucher_file_url, recharge_remark, created_time)
VALUES
(1, 'YJ2048101015000001', '安欣', '230123196610101234', 'A-1201', '现金', 2072.00, 'voucher-1.jpg', '/uploads/nursing-item-images/voucher-1.jpg', '线下充值，现金缴纳', '2048-10-10 15:00:00'),
(2, 'YJ2048101015000002', '高启强', '230123196610101234', 'A-1202', '现金', 2200.22, 'voucher-2.jpg', '/uploads/nursing-item-images/voucher-2.jpg', '线下充值，现金缴纳', '2048-10-09 15:00:00'),
(3, 'YJ2048101015000003', '陈书婷', '230123196610101234', 'A-1203', '微信', 1900.01, 'voucher-3.jpg', '/uploads/nursing-item-images/voucher-3.jpg', '线下充值，微信缴纳', '2048-10-08 15:00:00'),
(4, 'YJ2048101015000004', '李有田', '230123196610101234', 'A-1204', '微信', 1899.00, 'voucher-4.jpg', '/uploads/nursing-item-images/voucher-4.jpg', '线下充值，微信缴纳', '2048-10-07 15:00:00'),
(5, 'YJ2048101015000005', '安长林', '230123196610101234', 'A-1205', '支付宝', 1122.22, 'voucher-5.jpg', '/uploads/nursing-item-images/voucher-5.jpg', '线下充值，支付宝缴纳', '2048-10-06 15:00:00'),
(6, 'YJ2048101015000006', '徐江', '230123196610101234', 'A-1206', '支付宝', 1222.00, 'voucher-6.jpg', '/uploads/nursing-item-images/voucher-6.jpg', '线下充值，支付宝缴纳', '2048-10-05 15:00:00'),
(7, 'YJ2048101015000007', '唐小龙', '230123196610101234', 'A-1207', '现金', 1299.34, 'voucher-7.jpg', '/uploads/nursing-item-images/voucher-7.jpg', '线下充值，现金缴纳', '2048-10-04 15:00:00'),
(8, 'YJ2048101015000008', '唐小虎', '230123196610101234', 'A-1208', '现金', 2343.00, 'voucher-8.jpg', '/uploads/nursing-item-images/voucher-8.jpg', '线下充值，现金缴纳', '2048-10-03 15:00:00'),
(9, 'YJ2048101015000009', '陈书婷', '230123196610101234', 'A-1209', '现金', 1244.00, 'voucher-9.jpg', '/uploads/nursing-item-images/voucher-9.jpg', '线下充值，现金缴纳', '2048-10-02 15:00:00'),
(10, 'YJ2048101015000010', '杨健', '230123196610101234', 'A-1210', '现金', 17890.00, 'voucher-10.jpg', '/uploads/nursing-item-images/voucher-10.jpg', '线下充值，现金缴纳', '2048-10-01 15:00:00');
