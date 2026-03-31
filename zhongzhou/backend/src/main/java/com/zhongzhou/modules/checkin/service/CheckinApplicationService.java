package com.zhongzhou.modules.checkin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.checkin.dto.CheckinApplyRequest;
import com.zhongzhou.modules.checkin.dto.CheckinFamilyUpdateRequest;
import com.zhongzhou.modules.checkin.entity.CheckinApplication;
import com.zhongzhou.modules.checkin.entity.CheckinFamilyMember;
import com.zhongzhou.modules.checkin.mapper.CheckinApplicationMapper;
import com.zhongzhou.modules.checkin.mapper.CheckinFamilyMemberMapper;
import com.zhongzhou.modules.checkin.vo.CheckinApplicationDetailVO;
import com.zhongzhou.modules.checkin.vo.CheckinApplicationVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class CheckinApplicationService {
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private static final DateTimeFormatter D = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.CHINA);

    private final CheckinApplicationMapper mapper;
    private final CheckinFamilyMemberMapper familyMemberMapper;

    public CheckinApplicationService(CheckinApplicationMapper mapper, CheckinFamilyMemberMapper familyMemberMapper) {
        this.mapper = mapper;
        this.familyMemberMapper = familyMemberMapper;
    }

    public PageResult<CheckinApplicationVO> page(String elderName, String idCard, int page, int pageSize) {
        LambdaQueryWrapper<CheckinApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CheckinApplication::getIsDeleted, 0)
                .eq(CheckinApplication::getIsRetired, 0);
        if (StringUtils.hasText(elderName)) wrapper.like(CheckinApplication::getElderName, elderName.trim());
        if (StringUtils.hasText(idCard)) wrapper.eq(CheckinApplication::getIdCard, idCard.trim().toUpperCase(Locale.ROOT));
        wrapper.orderByDesc(CheckinApplication::getCreatedTime);
        Page<CheckinApplication> p = mapper.selectPage(new Page<>(page, pageSize), wrapper);
        List<CheckinApplicationVO> rows = p.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return new PageResult<>(p.getTotal(), rows);
    }

    public Long apply(CheckinApplyRequest req) {
        Long exists = mapper.selectCount(new LambdaQueryWrapper<CheckinApplication>()
                .eq(CheckinApplication::getIdCard, req.getIdCard().trim().toUpperCase(Locale.ROOT))
                .eq(CheckinApplication::getIsDeleted, 0)
                .eq(CheckinApplication::getIsRetired, 0));
        if (exists != null && exists > 0) {
            throw new IllegalArgumentException("该老人已入住，请重新输入");
        }
        CheckinApplication entity = new CheckinApplication();
        entity.setElderName(req.getElderName().trim());
        entity.setIdCard(req.getIdCard().trim().toUpperCase(Locale.ROOT));
        entity.setBirthDate(parseBirthDate(entity.getIdCard()));
        entity.setAge(calcAge(entity.getBirthDate()));
        entity.setGender(resolveGender(entity.getIdCard()));
        entity.setContactPhone(req.getContactPhone().trim());
        entity.setHomeAddress(req.getHomeAddress().trim());
        entity.setProfilePhotoName(req.getProfilePhotoName());
        entity.setIdCardFrontName(req.getIdCardFrontName());
        entity.setIdCardBackName(req.getIdCardBackName());
        entity.setRoomNo(req.getRoomNo().trim());
        entity.setCareLevel(req.getCareLevel().trim());
        entity.setCheckinStartDate(LocalDate.parse(req.getCheckinStartDate(), D).atStartOfDay());
        entity.setCheckinEndDate(LocalDate.parse(req.getCheckinPeriod().split("~")[1], D).atStartOfDay());
        entity.setNursingFee(Double.parseDouble(req.getNursingFee()));
        entity.setBedFee(Double.parseDouble(req.getBedFee()));
        entity.setOtherFee(0D);
        entity.setMedicalFee(0D);
        entity.setSubsidyFee(0D);
        entity.setDeposit(3000D);
        entity.setContractName(req.getContractName().trim());
        entity.setSignDate(LocalDate.parse(req.getSignDate(), D).atStartOfDay());
        entity.setPayerName(StringUtils.hasText(req.getPayerName()) ? req.getPayerName().trim() : "");
        entity.setPayerContact(StringUtils.hasText(req.getPayerContact()) ? req.getPayerContact().trim() : "");
        entity.setContractFileName(req.getContractFileName());
        LocalDate feeStart = LocalDate.parse(req.getFeeStartDate(), D);
        int days = (int) ChronoUnit.DAYS.between(feeStart, entity.getCheckinEndDate().toLocalDate()) + 1;
        entity.setBillDays(days);
        double monthly = entity.getNursingFee() + entity.getBedFee() + entity.getOtherFee();
        entity.setMonthlyAmount(monthly);
        entity.setCurrentAmount(round(monthly * days / 30D));
        entity.setBillAmount(round(entity.getCurrentAmount() + entity.getDeposit() + entity.getMedicalFee() - entity.getSubsidyFee()));
        entity.setIsRetired(0);
        entity.setIsDeleted(0);
        entity.setCreatedTime(LocalDateTime.now());
        entity.setUpdatedTime(LocalDateTime.now());
        mapper.insert(entity);
        if (req.getFamilyMembers() != null) {
            for (CheckinApplyRequest.FamilyMemberDTO item : req.getFamilyMembers()) {
                if (!StringUtils.hasText(item.getFamilyName()) || !StringUtils.hasText(item.getFamilyContact()) || !StringUtils.hasText(item.getRelationType())) {
                    continue;
                }
                CheckinFamilyMember member = new CheckinFamilyMember();
                member.setApplicationId(entity.getId());
                member.setFamilyName(item.getFamilyName().trim());
                member.setFamilyContact(item.getFamilyContact().trim());
                member.setRelationType(item.getRelationType().trim());
                member.setIsDeleted(0);
                familyMemberMapper.insert(member);
            }
        }
        return entity.getId();
    }

    public CheckinApplicationDetailVO detail(Long id) {
        CheckinApplication e = mapper.selectOne(new LambdaQueryWrapper<CheckinApplication>()
                .eq(CheckinApplication::getId, id)
                .eq(CheckinApplication::getIsDeleted, 0)
                .last("limit 1"));
        if (e == null) throw new IllegalArgumentException("入住申请不存在");
        CheckinApplicationDetailVO vo = new CheckinApplicationDetailVO();
        vo.setId(e.getId());
        vo.setElderName(e.getElderName());
        vo.setIdCard(e.getIdCard());
        vo.setRoomNo(e.getRoomNo());
        vo.setCareLevel(e.getCareLevel());
        vo.setCheckinPeriod(resolvePeriod(e));
        vo.setFeePeriod(resolveFeePeriod(e));
        vo.setBirthDate(e.getBirthDate() == null ? null : e.getBirthDate().format(D));
        vo.setAge(e.getAge());
        vo.setGender(e.getGender());
        vo.setContactPhone(e.getContactPhone());
        vo.setHomeAddress(e.getHomeAddress());
        vo.setProfilePhotoName(e.getProfilePhotoName());
        vo.setIdCardFrontName(e.getIdCardFrontName());
        vo.setIdCardBackName(e.getIdCardBackName());
        vo.setNursingFee(defaultNumber(e.getNursingFee()));
        vo.setBedFee(defaultNumber(e.getBedFee()));
        vo.setOtherFee(defaultNumber(e.getOtherFee()));
        vo.setMedicalFee(defaultNumber(e.getMedicalFee()));
        vo.setSubsidyFee(defaultNumber(e.getSubsidyFee()));
        vo.setDeposit(defaultNumber(e.getDeposit()));
        vo.setContractName(e.getContractName());
        vo.setSignDate(e.getSignDate() == null ? null : e.getSignDate().format(D));
        vo.setPayerName(e.getPayerName());
        vo.setPayerContact(e.getPayerContact());
        vo.setContractFileName(e.getContractFileName());
        vo.setMonthlyAmount(defaultNumber(e.getMonthlyAmount()));
        vo.setCurrentAmount(defaultNumber(e.getCurrentAmount()));
        vo.setBillAmount(defaultNumber(e.getBillAmount()));
        vo.setBillDays(e.getBillDays() == null ? 0 : e.getBillDays());
        List<CheckinFamilyMember> members = familyMemberMapper.selectList(new LambdaQueryWrapper<CheckinFamilyMember>()
                .eq(CheckinFamilyMember::getApplicationId, id)
                .eq(CheckinFamilyMember::getIsDeleted, 0)
                .orderByAsc(CheckinFamilyMember::getId));
        List<CheckinApplicationDetailVO.FamilyMemberVO> rows = new ArrayList<>();
        for (CheckinFamilyMember m : members) {
            CheckinApplicationDetailVO.FamilyMemberVO item = new CheckinApplicationDetailVO.FamilyMemberVO();
            item.setFamilyName(m.getFamilyName());
            item.setFamilyContact(m.getFamilyContact());
            item.setRelationType(m.getRelationType());
            rows.add(item);
        }
        vo.setFamilyMembers(rows);
        vo.setCreatedTime(e.getCreatedTime() == null ? null : e.getCreatedTime().format(DT));
        return vo;
    }

    public void updateFamily(Long id, CheckinFamilyUpdateRequest req) {
        CheckinApplication e = mapper.selectOne(new LambdaQueryWrapper<CheckinApplication>()
                .eq(CheckinApplication::getId, id)
                .eq(CheckinApplication::getIsDeleted, 0)
                .last("limit 1"));
        if (e == null) {
            throw new IllegalArgumentException("入住申请不存在");
        }
        familyMemberMapper.delete(new LambdaQueryWrapper<CheckinFamilyMember>()
                .eq(CheckinFamilyMember::getApplicationId, id));
        if (req == null || req.getFamilyMembers() == null) {
            return;
        }
        for (CheckinFamilyUpdateRequest.FamilyMemberDTO item : req.getFamilyMembers()) {
            if (item == null || !StringUtils.hasText(item.getFamilyName()) || !StringUtils.hasText(item.getFamilyContact()) || !StringUtils.hasText(item.getRelationType())) {
                continue;
            }
            CheckinFamilyMember member = new CheckinFamilyMember();
            member.setApplicationId(id);
            member.setFamilyName(item.getFamilyName().trim());
            member.setFamilyContact(item.getFamilyContact().trim());
            member.setRelationType(item.getRelationType().trim());
            member.setIsDeleted(0);
            familyMemberMapper.insert(member);
        }
    }

    private CheckinApplicationVO toVO(CheckinApplication e) {
        CheckinApplicationVO vo = new CheckinApplicationVO();
        vo.setId(e.getId());
        vo.setElderName(e.getElderName());
        vo.setIdCard(e.getIdCard());
        vo.setRoomNo(e.getRoomNo());
        vo.setCareLevel(e.getCareLevel());
        vo.setCheckinPeriod(resolvePeriod(e));
        vo.setCreatedTime(e.getCreatedTime() == null ? null : e.getCreatedTime().format(DT));
        return vo;
    }

    private String resolvePeriod(CheckinApplication e) {
        String s = e.getCheckinStartDate() == null ? "" : e.getCheckinStartDate().toLocalDate().toString();
        String t = e.getCheckinEndDate() == null ? "" : e.getCheckinEndDate().toLocalDate().toString();
        return s + "~" + t;
    }

    private String resolveFeePeriod(CheckinApplication e) {
        String s = e.getCheckinStartDate() == null ? "" : e.getCheckinStartDate().toLocalDate().toString();
        String t = e.getCheckinEndDate() == null ? "" : e.getCheckinEndDate().toLocalDate().toString();
        return s + "~" + t;
    }

    private LocalDateTime parseBirthDate(String idCard) {
        String s = idCard.substring(6, 14);
        return LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay();
    }

    private Integer calcAge(LocalDateTime birthDate) {
        if (birthDate == null) return null;
        return (int) ChronoUnit.YEARS.between(birthDate.toLocalDate(), LocalDate.now());
    }

    private String resolveGender(String idCard) {
        int genderCode = Integer.parseInt(String.valueOf(idCard.charAt(16)));
        return genderCode % 2 == 0 ? "女" : "男";
    }

    private double round(double v) {
        return Math.round(v * 100D) / 100D;
    }

    private Double defaultNumber(Double value) {
        return value == null ? 0D : value;
    }
}
