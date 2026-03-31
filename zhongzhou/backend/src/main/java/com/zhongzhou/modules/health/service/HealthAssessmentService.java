package com.zhongzhou.modules.health.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.health.dto.HealthAssessmentQueryRequest;
import com.zhongzhou.modules.health.entity.HealthAssessmentAbnormal;
import com.zhongzhou.modules.health.entity.HealthAssessment;
import com.zhongzhou.modules.health.mapper.HealthAssessmentAbnormalMapper;
import com.zhongzhou.modules.health.mapper.HealthAssessmentMapper;
import com.zhongzhou.modules.health.vo.HealthAssessmentDetailVO;
import com.zhongzhou.modules.health.vo.HealthAssessmentVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class HealthAssessmentService {
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private static final DateTimeFormatter D = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.CHINA);

    private final HealthAssessmentMapper healthAssessmentMapper;
    private final HealthAssessmentAbnormalMapper abnormalMapper;

    public HealthAssessmentService(HealthAssessmentMapper healthAssessmentMapper, HealthAssessmentAbnormalMapper abnormalMapper) {
        this.healthAssessmentMapper = healthAssessmentMapper;
        this.abnormalMapper = abnormalMapper;
    }

    public PageResult<HealthAssessmentVO> page(HealthAssessmentQueryRequest query) {
        LambdaQueryWrapper<HealthAssessment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HealthAssessment::getIsDeleted, 0);
        if (StringUtils.hasText(query.getElderName())) {
            wrapper.like(HealthAssessment::getElderName, query.getElderName().trim());
        }
        if (StringUtils.hasText(query.getIdCard())) {
            wrapper.eq(HealthAssessment::getIdCard, query.getIdCard().trim());
        }
        if (query.getLivingStatus() != null) {
            wrapper.eq(HealthAssessment::getLivingStatus, query.getLivingStatus());
        }
        wrapper.orderByDesc(HealthAssessment::getAssessedTime);
        Page<HealthAssessment> page = healthAssessmentMapper.selectPage(new Page<>(query.getPage(), query.getPageSize()), wrapper);
        List<HealthAssessmentVO> rows = page.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), rows);
    }

    public Long upload(String elderName, String idCard, String examOrg, MultipartFile reportFile) {
        if (!StringUtils.hasText(elderName) || elderName.trim().length() > 10) {
            throw new IllegalArgumentException("老人姓名不能为空且最多10个字符");
        }
        if (!StringUtils.hasText(idCard) || !idCard.trim().matches("^[0-9Xx]{18}$")) {
            throw new IllegalArgumentException("身份证格式错误，请重新输入");
        }
        if (!StringUtils.hasText(examOrg) || examOrg.trim().length() > 10) {
            throw new IllegalArgumentException("体检单位不能为空且最多10个字符");
        }
        if (reportFile == null || reportFile.isEmpty()) {
            throw new IllegalArgumentException("体检报告不能为空");
        }
        String name = reportFile.getOriginalFilename() == null ? "" : reportFile.getOriginalFilename().toLowerCase(Locale.ROOT);
        if (!name.endsWith(".pdf")) {
            throw new IllegalArgumentException("仅支持上传pdf文件");
        }
        if (reportFile.getSize() > 60L * 1024 * 1024) {
            throw new IllegalArgumentException("文件大小不能超过60M，请重新上传");
        }

        HealthAssessment entity = new HealthAssessment();
        entity.setElderName(elderName.trim());
        entity.setIdCard(idCard.trim().toUpperCase(Locale.ROOT));
        entity.setExamOrg(examOrg.trim());
        entity.setReportFileName(reportFile.getOriginalFilename());
        entity.setReportFileSize(reportFile.getSize());
        entity.setReportFileUrl("/mock/reports/" + System.currentTimeMillis() + "_" + reportFile.getOriginalFilename());
        entity.setHealthScore(mockScore(entity.getIdCard()));
        entity.setSuggestion(entity.getHealthScore() >= 60 ? "建议" : "不建议");
        entity.setCareLevel(mockCareLevel(entity.getHealthScore()));
        entity.setLivingStatus(1);
        entity.setAssessedTime(LocalDateTime.now());
        entity.setIsDeleted(0);
        entity.setCreatedTime(LocalDateTime.now());
        entity.setUpdatedTime(LocalDateTime.now());
        healthAssessmentMapper.insert(entity);
        return entity.getId();
    }

    public HealthAssessmentDetailVO detail(Long id) {
        HealthAssessment item = healthAssessmentMapper.selectOne(new LambdaQueryWrapper<HealthAssessment>()
                .eq(HealthAssessment::getId, id)
                .eq(HealthAssessment::getIsDeleted, 0)
                .last("limit 1"));
        if (item == null) {
            throw new IllegalArgumentException("健康评估记录不存在");
        }
        return toDetailVO(item);
    }

    private HealthAssessmentVO toVO(HealthAssessment e) {
        HealthAssessmentVO vo = new HealthAssessmentVO();
        vo.setId(e.getId());
        vo.setElderName(e.getElderName());
        vo.setIdCard(e.getIdCard());
        vo.setHealthScore(e.getHealthScore());
        vo.setSuggestion(e.getSuggestion());
        vo.setCareLevel(e.getCareLevel());
        vo.setLivingStatus(e.getLivingStatus());
        vo.setLivingStatusLabel(resolveLivingStatusLabel(e.getLivingStatus()));
        vo.setAssessedTime(e.getAssessedTime() == null ? null : e.getAssessedTime().format(DT));
        return vo;
    }

    private HealthAssessmentDetailVO toDetailVO(HealthAssessment e) {
        HealthAssessmentDetailVO vo = new HealthAssessmentDetailVO();
        vo.setId(e.getId());
        vo.setElderName(e.getElderName());
        vo.setIdCard(e.getIdCard());
        vo.setExamOrg(e.getExamOrg());
        vo.setReportFileName(e.getReportFileName());
        vo.setReportFileSize(e.getReportFileSize());
        vo.setReportFileUrl(e.getReportFileUrl());
        vo.setHealthScore(e.getHealthScore());
        vo.setSuggestion(e.getSuggestion());
        vo.setCareLevel(e.getCareLevel());
        vo.setRiskLevel(resolveRiskLevel(e.getHealthScore()));
        vo.setReportSummary(buildReportSummary(e));
        vo.setAiAdvice(buildAiAdvice());
        vo.setLivingStatus(e.getLivingStatus());
        vo.setLivingStatusLabel(resolveLivingStatusLabel(e.getLivingStatus()));
        vo.setAssessedTime(e.getAssessedTime() == null ? null : e.getAssessedTime().format(DT));
        LocalDateTime birth = parseBirthDate(e.getIdCard());
        if (birth != null) {
            vo.setBirthDate(birth.format(D));
            vo.setAge(Period.between(birth.toLocalDate(), LocalDateTime.now().toLocalDate()).getYears());
        }
        vo.setGender(resolveGender(e.getIdCard()));
        vo.setSummaryDate(e.getAssessedTime() == null ? null : e.getAssessedTime().format(D));
        vo.setSystemScores(buildSystemScores(e.getHealthScore()));
        vo.setAgeDistributions(buildAgeDistributions());
        vo.setAbnormalItems(buildAbnormalItems(e.getId()));
        return vo;
    }

    private String resolveLivingStatusLabel(Integer status) {
        if (status == null) return "未知";
        if (status == 1) return "已入住";
        if (status == 2) return "未入住";
        if (status == 3) return "已退住";
        return "未知";
    }

    private double mockScore(String idCard) {
        int hash = Math.abs(idCard.hashCode());
        return Math.round((60 + (hash % 4000) / 100.0) * 10) / 10.0;
    }

    private String mockCareLevel(double score) {
        if (score >= 90) return "一级护理等级";
        if (score >= 80) return "二级护理等级";
        if (score >= 70) return "三级护理等级";
        return "特级护理等级";
    }

    private String resolveRiskLevel(Double score) {
        if (score == null) return "提示";
        if (score >= 85) return "低风险";
        if (score >= 75) return "中风险";
        if (score >= 65) return "偏高风险";
        return "高风险";
    }

    private String buildReportSummary(HealthAssessment e) {
        return "血脂及代谢指标提示存在波动，结合体检报告建议重点关注血糖、血脂与体重管理。"
                + "总体评估为" + resolveRiskLevel(e.getHealthScore()) + "，建议按医嘱定期复查并配合护理干预。";
    }

    private String buildAiAdvice() {
        return "建议保持规律作息和控制饮食结构，适度增加步行与抗阻训练。"
                + "如有心血管、血压、血糖异常史，建议按月随访并复查关键指标。";
    }

    private LocalDateTime parseBirthDate(String idCard) {
        try {
            if (idCard == null || idCard.length() < 14) return null;
            String s = idCard.substring(6, 14);
            return LocalDateTime.parse(s + " 00:00:00", DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));
        } catch (Exception ignore) {
            return null;
        }
    }

    private String resolveGender(String idCard) {
        if (idCard == null || idCard.length() < 17) return "未知";
        char c = idCard.charAt(16);
        if (!Character.isDigit(c)) return "未知";
        return ((c - '0') % 2 == 1) ? "男" : "女";
    }

    private List<HealthAssessmentDetailVO.SystemScoreVO> buildSystemScores(Double total) {
        double base = total == null ? 78.6 : total;
        List<String> names = Arrays.asList("消化系统", "内分泌系统", "免疫系统", "循环系统", "泌尿系统", "运动系统", "神经系统", "呼吸系统");
        List<Double> vals = Arrays.asList(base - 16, base - 13, base - 6, base - 3, base - 1, base + 0.4, base + 2, 100d);
        List<HealthAssessmentDetailVO.SystemScoreVO> list = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            HealthAssessmentDetailVO.SystemScoreVO vo = new HealthAssessmentDetailVO.SystemScoreVO();
            vo.setSystemName(names.get(i));
            vo.setScore(Math.round(vals.get(i) * 10) / 10.0);
            list.add(vo);
        }
        return list;
    }

    private List<HealthAssessmentDetailVO.AgeDistributionVO> buildAgeDistributions() {
        String[] groups = {"20岁", "30岁", "40岁", "50岁", "60岁", "70岁", "80岁", "90岁+"};
        double[][] vals = {
                {92, 5, 2, 1, 0},
                {85, 10, 3, 1, 1},
                {84, 9, 5, 1, 1},
                {66, 20, 9, 3, 2},
                {53, 24, 14, 5, 4},
                {34, 31, 24, 7, 4},
                {16, 33, 29, 12, 10},
                {6, 20, 40, 19, 15}
        };
        List<HealthAssessmentDetailVO.AgeDistributionVO> list = new ArrayList<>();
        for (int i = 0; i < groups.length; i++) {
            HealthAssessmentDetailVO.AgeDistributionVO vo = new HealthAssessmentDetailVO.AgeDistributionVO();
            vo.setAgeGroup(groups[i]);
            vo.setHealthy(vals[i][0]);
            vo.setWarning(vals[i][1]);
            vo.setRisk(vals[i][2]);
            vo.setSevere(vals[i][3]);
            vo.setCritical(vals[i][4]);
            list.add(vo);
        }
        return list;
    }

    private List<HealthAssessmentDetailVO.AbnormalItemVO> buildAbnormalItems(Long assessmentId) {
        List<HealthAssessmentAbnormal> items = abnormalMapper.selectList(new LambdaQueryWrapper<HealthAssessmentAbnormal>()
                .eq(HealthAssessmentAbnormal::getAssessmentId, assessmentId)
                .eq(HealthAssessmentAbnormal::getIsDeleted, 0)
                .orderByAsc(HealthAssessmentAbnormal::getId));
        if (items.isEmpty()) {
            return buildDefaultAbnormalItems();
        }
        return items.stream().map(e -> {
            HealthAssessmentDetailVO.AbnormalItemVO vo = new HealthAssessmentDetailVO.AbnormalItemVO();
            vo.setConclusion(e.getConclusion());
            vo.setItemName(e.getItemName());
            vo.setResultValue(e.getResultValue());
            vo.setDirection(e.getDirection());
            vo.setRefRange(e.getRefRange());
            vo.setUnit(e.getUnit());
            return vo;
        }).collect(Collectors.toList());
    }

    private List<HealthAssessmentDetailVO.AbnormalItemVO> buildDefaultAbnormalItems() {
        List<HealthAssessmentDetailVO.AbnormalItemVO> list = new ArrayList<>();
        list.add(buildAbnormal("代谢性疾病", "空腹血糖", "14.01", "up", "3.9-6.1", "mmol/L"));
        list.add(buildAbnormal("高血脂", "低密度脂蛋白胆固醇", "4.19", "up", "0-3.36", "mmol/L"));
        list.add(buildAbnormal("心脑血管疾病", "血清载脂蛋白B", "0.50", "down", "0.64-1.14", "g/L"));
        return list;
    }

    private HealthAssessmentDetailVO.AbnormalItemVO buildAbnormal(String c, String n, String v, String d, String r, String u) {
        HealthAssessmentDetailVO.AbnormalItemVO vo = new HealthAssessmentDetailVO.AbnormalItemVO();
        vo.setConclusion(c);
        vo.setItemName(n);
        vo.setResultValue(v);
        vo.setDirection(d);
        vo.setRefRange(r);
        vo.setUnit(u);
        return vo;
    }
}
