-- 部分环境缺少下列列会导致退住列表 500，若报错 Duplicate column 可忽略
SET NAMES utf8mb4;

ALTER TABLE zz_checkout_application ADD COLUMN refund_voucher_url VARCHAR(500) DEFAULT NULL COMMENT '退款凭证图片URL' AFTER refund_voucher_name;
ALTER TABLE zz_checkout_application ADD COLUMN voucher_submitter VARCHAR(64) DEFAULT NULL COMMENT '凭证提交人' AFTER refund_remark;
ALTER TABLE zz_checkout_application ADD COLUMN voucher_submit_time DATETIME DEFAULT NULL COMMENT '凭证提交时间' AFTER voucher_submitter;
