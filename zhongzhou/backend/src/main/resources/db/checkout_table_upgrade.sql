-- 补齐 zz_checkout_application 与 Java 实体字段（若列已存在会报错，可忽略该条）
SET NAMES utf8mb4;

ALTER TABLE zz_checkout_application ADD COLUMN caregiver_names VARCHAR(200) DEFAULT NULL COMMENT '护理员姓名' AFTER agreement_file_name;
ALTER TABLE zz_checkout_application ADD COLUMN refund_voucher_name VARCHAR(255) DEFAULT NULL COMMENT '退款凭证文件名' AFTER caregiver_names;
ALTER TABLE zz_checkout_application ADD COLUMN refund_voucher_url VARCHAR(500) DEFAULT NULL COMMENT '退款凭证图片URL' AFTER refund_voucher_name;
ALTER TABLE zz_checkout_application ADD COLUMN refund_method VARCHAR(32) DEFAULT NULL COMMENT '退款方式' AFTER refund_voucher_url;
ALTER TABLE zz_checkout_application ADD COLUMN refund_remark VARCHAR(50) DEFAULT NULL COMMENT '退款备注' AFTER refund_method;
ALTER TABLE zz_checkout_application ADD COLUMN voucher_submitter VARCHAR(64) DEFAULT NULL COMMENT '凭证提交人' AFTER refund_remark;
ALTER TABLE zz_checkout_application ADD COLUMN voucher_submit_time DATETIME DEFAULT NULL COMMENT '凭证提交时间' AFTER voucher_submitter;
