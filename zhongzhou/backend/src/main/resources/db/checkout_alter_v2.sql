-- 已有库升级：为 zz_checkout_application 增加已退住详情展示字段（若列已存在请跳过对应语句）
ALTER TABLE zz_checkout_application ADD COLUMN caregiver_names VARCHAR(200) NULL COMMENT '护理员姓名' AFTER agreement_file_name;
ALTER TABLE zz_checkout_application ADD COLUMN refund_voucher_url VARCHAR(500) NULL COMMENT '退款凭证图片URL' AFTER refund_voucher_name;
ALTER TABLE zz_checkout_application ADD COLUMN voucher_submitter VARCHAR(64) NULL COMMENT '凭证提交人' AFTER refund_remark;
ALTER TABLE zz_checkout_application ADD COLUMN voucher_submit_time DATETIME NULL COMMENT '凭证提交时间' AFTER voucher_submitter;
