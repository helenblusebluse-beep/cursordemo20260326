CREATE TABLE IF NOT EXISTS zz_health_assessment (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  elder_name VARCHAR(32) NOT NULL COMMENT '老人姓名',
  id_card VARCHAR(18) NOT NULL COMMENT '老人身份证号',
  exam_org VARCHAR(32) NOT NULL COMMENT '体检单位',
  report_file_name VARCHAR(128) NOT NULL COMMENT '体检报告文件名',
  report_file_size BIGINT NOT NULL COMMENT '体检报告文件大小(字节)',
  report_file_url VARCHAR(255) NOT NULL COMMENT '体检报告地址',
  health_score DECIMAL(5,1) NOT NULL COMMENT '健康评分',
  suggestion VARCHAR(16) NOT NULL COMMENT '建议入住:建议/不建议',
  care_level VARCHAR(32) NOT NULL COMMENT '推荐护理等级',
  living_status TINYINT NOT NULL DEFAULT 1 COMMENT '入住情况:1已入住 2未入住 3已退住',
  assessed_time DATETIME NOT NULL COMMENT '评估时间',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除:0否 1是',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY idx_health_name (elder_name),
  KEY idx_health_id_card (id_card),
  KEY idx_health_assessed_time (assessed_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康评估表';

DELETE FROM zz_health_assessment;
INSERT INTO zz_health_assessment
(id, elder_name, id_card, exam_org, report_file_name, report_file_size, report_file_url, health_score, suggestion, care_level, living_status, assessed_time, is_deleted)
VALUES
(1, '安权', '230203199701221029', '嘉佑体检中心', 'health_report_001.pdf', 12582912, '/mock/reports/health_report_001.pdf', 87.7, '建议', '特级护理等级', 1, '2048-10-10 15:00:00', 0),
(2, '高启强', '230203199701221030', '瑞康体检中心', 'health_report_002.pdf', 9437184, '/mock/reports/health_report_002.pdf', 77.7, '不建议', '二级护理等级', 2, '2048-10-09 15:00:00', 0),
(3, '陈布', '230203199701221031', '市一院体检科', 'health_report_003.pdf', 15728640, '/mock/reports/health_report_003.pdf', 88.8, '建议', '三级护理等级', 1, '2048-10-08 15:00:00', 0),
(4, '李有田', '230203199701221032', '瑞康体检中心', 'health_report_004.pdf', 7340032, '/mock/reports/health_report_004.pdf', 86.8, '建议', '一级护理等级', 1, '2048-10-07 15:00:00', 0),
(5, '安长林', '230203199701221033', '嘉佑体检中心', 'health_report_005.pdf', 9961472, '/mock/reports/health_report_005.pdf', 88.8, '建议', '特级护理等级', 1, '2048-10-06 15:00:00', 0),
(6, '徐江', '230203199701221034', '市一院体检科', 'health_report_006.pdf', 10485760, '/mock/reports/health_report_006.pdf', 88.8, '建议', '一级护理等级', 1, '2048-10-05 15:00:00', 0),
(7, '唐小龙', '230203199701221035', '瑞康体检中心', 'health_report_007.pdf', 13631488, '/mock/reports/health_report_007.pdf', 88.8, '建议', '二级护理等级', 1, '2048-10-04 15:00:00', 0),
(8, '唐小虎', '230203199701221036', '嘉佑体检中心', 'health_report_008.pdf', 8912896, '/mock/reports/health_report_008.pdf', 88.8, '建议', '三级护理等级', 1, '2048-10-03 15:00:00', 0),
(9, '陈书婷', '230203199701221037', '市一院体检科', 'health_report_009.pdf', 7864320, '/mock/reports/health_report_009.pdf', 88.8, '建议', '特级护理等级', 1, '2048-10-02 15:00:00', 0),
(10, '杨健', '230203199701221038', '瑞康体检中心', 'health_report_010.pdf', 11534336, '/mock/reports/health_report_010.pdf', 88.8, '建议', '一级护理等级', 3, '2048-10-01 15:00:00', 0),
(11, '王建国', '230203199701221039', '嘉佑体检中心', 'health_report_011.pdf', 6291456, '/mock/reports/health_report_011.pdf', 91.2, '建议', '一级护理等级', 1, '2048-09-30 10:00:00', 0),
(12, '李慧芳', '230203199701221040', '瑞康体检中心', 'health_report_012.pdf', 7340032, '/mock/reports/health_report_012.pdf', 72.5, '不建议', '三级护理等级', 2, '2048-09-29 11:00:00', 0),
(13, '赵明', '230203199701221041', '市一院体检科', 'health_report_013.pdf', 12582912, '/mock/reports/health_report_013.pdf', 84.0, '建议', '二级护理等级', 1, '2048-09-28 09:30:00', 0),
(14, '周婷', '230203199701221042', '嘉佑体检中心', 'health_report_014.pdf', 10485760, '/mock/reports/health_report_014.pdf', 69.8, '不建议', '特级护理等级', 2, '2048-09-27 14:20:00', 0),
(15, '吴浩', '230203199701221043', '瑞康体检中心', 'health_report_015.pdf', 9437184, '/mock/reports/health_report_015.pdf', 95.1, '建议', '一级护理等级', 1, '2048-09-26 15:40:00', 0),
(16, '郑丽', '230203199701221044', '市一院体检科', 'health_report_016.pdf', 8388608, '/mock/reports/health_report_016.pdf', 78.6, '不建议', '二级护理等级', 3, '2048-09-25 08:15:00', 0),
(17, '冯涛', '230203199701221045', '嘉佑体检中心', 'health_report_017.pdf', 11010048, '/mock/reports/health_report_017.pdf', 88.2, '建议', '三级护理等级', 1, '2048-09-24 16:10:00', 0),
(18, '褚敏', '230203199701221046', '瑞康体检中心', 'health_report_018.pdf', 9961472, '/mock/reports/health_report_018.pdf', 82.9, '建议', '二级护理等级', 1, '2048-09-23 13:05:00', 0),
(19, '卫星', '230203199701221047', '市一院体检科', 'health_report_019.pdf', 8912896, '/mock/reports/health_report_019.pdf', 66.4, '不建议', '特级护理等级', 2, '2048-09-22 09:50:00', 0),
(20, '蒋燕', '230203199701221048', '嘉佑体检中心', 'health_report_020.pdf', 15728640, '/mock/reports/health_report_020.pdf', 89.7, '建议', '一级护理等级', 1, '2048-09-21 10:25:00', 0),
(21, '沈洋', '230203199701221049', '瑞康体检中心', 'health_report_021.pdf', 9437184, '/mock/reports/health_report_021.pdf', 73.1, '不建议', '三级护理等级', 2, '2048-09-20 14:55:00', 0),
(22, '韩雪', '230203199701221050', '市一院体检科', 'health_report_022.pdf', 6291456, '/mock/reports/health_report_022.pdf', 92.5, '建议', '一级护理等级', 1, '2048-09-19 11:45:00', 0),
(23, '杨凯', '230203199701221051', '嘉佑体检中心', 'health_report_023.pdf', 7340032, '/mock/reports/health_report_023.pdf', 80.4, '建议', '二级护理等级', 1, '2048-09-18 08:30:00', 0),
(24, '朱红', '230203199701221052', '瑞康体检中心', 'health_report_024.pdf', 10485760, '/mock/reports/health_report_024.pdf', 74.6, '不建议', '三级护理等级', 3, '2048-09-17 15:15:00', 0),
(25, '秦峰', '230203199701221053', '市一院体检科', 'health_report_025.pdf', 8388608, '/mock/reports/health_report_025.pdf', 85.8, '建议', '二级护理等级', 1, '2048-09-16 12:40:00', 0),
(26, '尤佳', '230203199701221054', '嘉佑体检中心', 'health_report_026.pdf', 11534336, '/mock/reports/health_report_026.pdf', 68.2, '不建议', '特级护理等级', 2, '2048-09-15 09:20:00', 0),
(27, '许宁', '230203199701221055', '瑞康体检中心', 'health_report_027.pdf', 9437184, '/mock/reports/health_report_027.pdf', 90.3, '建议', '一级护理等级', 1, '2048-09-14 16:00:00', 0),
(28, '何倩', '230203199701221056', '市一院体检科', 'health_report_028.pdf', 7340032, '/mock/reports/health_report_028.pdf', 79.5, '不建议', '二级护理等级', 3, '2048-09-13 10:10:00', 0),
(29, '吕阳', '230203199701221057', '嘉佑体检中心', 'health_report_029.pdf', 6291456, '/mock/reports/health_report_029.pdf', 87.1, '建议', '三级护理等级', 1, '2048-09-12 14:35:00', 0),
(30, '施静', '230203199701221058', '瑞康体检中心', 'health_report_030.pdf', 9961472, '/mock/reports/health_report_030.pdf', 83.7, '建议', '二级护理等级', 1, '2048-09-11 09:05:00', 0);

CREATE TABLE IF NOT EXISTS zz_health_assessment_abnormal (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  assessment_id BIGINT UNSIGNED NOT NULL COMMENT '健康评估ID',
  conclusion VARCHAR(64) NOT NULL COMMENT '结论',
  item_name VARCHAR(64) NOT NULL COMMENT '项目名称',
  result_value VARCHAR(32) NOT NULL COMMENT '检查结果',
  direction VARCHAR(8) NOT NULL COMMENT '方向:up/down',
  ref_range VARCHAR(32) NOT NULL COMMENT '参考值',
  unit VARCHAR(16) NOT NULL COMMENT '单位',
  is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  KEY idx_health_abnormal_assessment (assessment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康评估异常分析项';

DELETE FROM zz_health_assessment_abnormal;
INSERT INTO zz_health_assessment_abnormal
(assessment_id, conclusion, item_name, result_value, direction, ref_range, unit, is_deleted)
VALUES
(1, '代谢性疾病', '空腹血糖', '14.01', 'up', '3.9-6.1', 'mmol/L', 0),
(1, '脂蛋白代谢异常', '总胆固醇', '7.07', 'up', '2.9-5.86', 'mmol/L', 0),
(1, '高血脂', '低密度脂蛋白胆固醇', '4.19', 'up', '0-3.36', 'mmol/L', 0),
(1, '心脑血管疾病', '血清载脂蛋白B', '0.50', 'down', '0.64-1.14', 'g/L', 0),
(1, '血液系统疾病', '白细胞', '11.49*10^9', 'up', '3.5-9.5*10^9', 'L', 0),
(1, '肥胖', '体重指数BMI', '29.2', 'up', '>24', '-', 0),
(2, '代谢性疾病', '空腹血糖', '8.31', 'up', '3.9-6.1', 'mmol/L', 0),
(2, '高血脂', '甘油三酯', '2.87', 'up', '0.56-1.7', 'mmol/L', 0),
(2, '心脑血管疾病', '高密度脂蛋白胆固醇', '0.80', 'down', '1.04-1.55', 'mmol/L', 0),
(3, '肝功能异常', '谷丙转氨酶', '81', 'up', '0-40', 'U/L', 0),
(3, '代谢性疾病', '糖化血红蛋白', '7.2', 'up', '4.0-6.0', '%', 0),
(4, '血液系统疾病', '中性粒细胞', '7.8', 'up', '1.8-6.3', '10^9/L', 0);
