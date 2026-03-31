SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

DROP TABLE IF EXISTS zz_balance_query;

CREATE TABLE zz_balance_query (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  elder_name VARCHAR(32) NOT NULL COMMENT '老人姓名',
  bed_no VARCHAR(32) NOT NULL DEFAULT '' COMMENT '床位号',
  account_balance DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '预缴款余额',
  deposit_balance DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '押金余额',
  changed_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '变动时间',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (id),
  UNIQUE KEY uk_balance_elder_bed (elder_name, bed_no),
  KEY idx_balance_changed_time (changed_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='余额查询';

INSERT INTO zz_balance_query
(id, elder_name, bed_no, account_balance, deposit_balance, changed_time)
VALUES
(1, '安欣', '1011', 5500.00, 5500.00, '2048-10-10 15:00:00'),
(2, '高启强', '1012', 5000.00, 5000.00, '2048-10-09 15:00:00'),
(3, '陈泰', '1013', 4500.00, 4500.00, '2048-10-08 15:00:00'),
(4, '李有田', '1014', 4400.00, 4400.00, '2048-10-07 15:00:00'),
(5, '安长林', '1015', 4300.00, 4300.00, '2048-10-06 15:00:00'),
(6, '徐江', '1021', 4200.00, 4200.00, '2048-10-05 15:00:00'),
(7, '唐小龙', '1022', 4100.00, 4100.00, '2048-10-04 15:00:00'),
(8, '唐小虎', '1021', 3900.00, 3900.00, '2048-10-03 15:00:00'),
(9, '陈书婷', '1024', 3500.00, 3500.00, '2048-10-02 15:00:00'),
(10, '杨健', '1035', 2500.00, 2500.00, '2048-10-01 15:00:00');
