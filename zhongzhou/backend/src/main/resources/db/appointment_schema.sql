CREATE TABLE IF NOT EXISTS zz_visit_appointment (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  reserve_type TINYINT NOT NULL COMMENT '预约类型:1参观预约 2探访预约',
  visitor_name VARCHAR(32) NOT NULL COMMENT '预约人姓名',
  visitor_phone VARCHAR(20) NOT NULL COMMENT '预约人手机号',
  elder_name VARCHAR(32) NOT NULL COMMENT '老人姓名',
  scheduled_time DATETIME NOT NULL COMMENT '预约时间',
  confirmed_time DATETIME DEFAULT NULL COMMENT '到访确认时间',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1待上门 2已完成 3已取消',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除:0否 1是',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY idx_visit_status (status),
  KEY idx_visit_scheduled_time (scheduled_time),
  KEY idx_visit_visitor_phone (visitor_phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约登记表';

INSERT INTO zz_visit_appointment
(reserve_type, visitor_name, visitor_phone, elder_name, scheduled_time, confirmed_time, status, is_deleted)
VALUES
(1, '安权',   '13875568891', '安权',   '2026-03-28 10:00:00', NULL, 1, 0),                 -- 待上门（未过期）
(2, '高启强', '13875568892', '高启强', '2026-03-18 09:30:00', NULL, 1, 0),                 -- 已过期（前端按待上门+时间已过展示）
(1, '陈布',   '13875568893', '陈布',   '2026-03-12 15:00:00', '2026-03-12 15:10:00', 2, 0), -- 已完成
(2, '李自强', '13875568894', '李自强', '2026-03-15 14:00:00', '2026-03-15 14:20:00', 2, 0), -- 已完成
(1, '安艾林', '13875568895', '安艾林', '2026-03-16 11:00:00', NULL, 3, 0),                 -- 已取消
(2, '徐江',   '13875568896', '徐江',   '2026-03-17 16:00:00', NULL, 3, 0),                 -- 已取消
(1, '唐小龙', '13875568897', '唐小龙', '2026-03-30 09:00:00', NULL, 1, 0),                 -- 待上门（未过期）
(2, '唐小虎', '13875568898', '唐小虎', '2026-03-10 13:30:00', NULL, 1, 0),                 -- 已过期
(1, '陈书婷', '13875568899', '陈书婷', '2026-03-13 10:20:00', '2026-03-13 10:45:00', 2, 0), -- 已完成
(2, '杨健',   '13875568900', '杨健',   '2026-03-11 08:40:00', NULL, 3, 0)                  -- 已取消
ON DUPLICATE KEY UPDATE updated_time = CURRENT_TIMESTAMP;
