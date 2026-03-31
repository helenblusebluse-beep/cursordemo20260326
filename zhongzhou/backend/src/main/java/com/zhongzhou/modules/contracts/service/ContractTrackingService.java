package com.zhongzhou.modules.contracts.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.checkin.entity.CheckinApplication;
import com.zhongzhou.modules.checkin.mapper.CheckinApplicationMapper;
import com.zhongzhou.modules.contracts.entity.ContractTracking;
import com.zhongzhou.modules.contracts.mapper.ContractTrackingMapper;
import com.zhongzhou.modules.contracts.vo.ContractTrackingDetailVO;
import com.zhongzhou.modules.contracts.vo.ContractTrackingItemVO;
import com.zhongzhou.modules.checkout.entity.CheckoutApplication;
import com.zhongzhou.modules.checkout.mapper.CheckoutApplicationMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class ContractTrackingService {
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private static final DateTimeFormatter D = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.CHINA);

    private final ContractTrackingMapper mapper;
    private final CheckinApplicationMapper checkinMapper;
    private final CheckoutApplicationMapper checkoutMapper;

    public ContractTrackingService(ContractTrackingMapper mapper, CheckinApplicationMapper checkinMapper,
                                   CheckoutApplicationMapper checkoutMapper) {
        this.mapper = mapper;
        this.checkinMapper = checkinMapper;
        this.checkoutMapper = checkoutMapper;
    }

    public PageResult<ContractTrackingItemVO> page(String contractNo, String elderName, String contractStatus, int page, int pageSize) {
        LambdaQueryWrapper<ContractTracking> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContractTracking::getIsDeleted, 0);
        if (StringUtils.hasText(contractNo)) {
            wrapper.eq(ContractTracking::getContractNo, contractNo.trim());
        }
        wrapper.orderByDesc(ContractTracking::getCreatedTime).orderByDesc(ContractTracking::getId);
        List<ContractTrackingItemVO> allRows = mapper.selectList(wrapper).stream()
                .map(this::toItemVO)
                .collect(Collectors.toList());

        if (StringUtils.hasText(elderName)) {
            String kw = elderName.trim();
            allRows = allRows.stream()
                    .filter(x -> x.getElderName() != null && x.getElderName().contains(kw))
                    .collect(Collectors.toList());
        }
        if (StringUtils.hasText(contractStatus)) {
            String s = contractStatus.trim();
            allRows = allRows.stream()
                    .filter(x -> s.equals(x.getContractStatus()))
                    .collect(Collectors.toList());
        }
        int from = Math.max(0, (page - 1) * pageSize);
        int to = Math.min(allRows.size(), from + pageSize);
        List<ContractTrackingItemVO> rows = from >= allRows.size() ? new ArrayList<>() : allRows.subList(from, to);
        return new PageResult<>(allRows.size(), rows);
    }

    public ContractTrackingDetailVO detail(Long id) {
        ContractTracking e = mapper.selectById(id);
        if (e == null || (e.getIsDeleted() != null && e.getIsDeleted() == 1)) {
            throw new IllegalArgumentException("合同不存在");
        }
        CheckinApplication c = checkinMapper.selectById(e.getCheckinId());

        ContractTrackingDetailVO vo = new ContractTrackingDetailVO();
        vo.setId(e.getId());
        vo.setCheckinId(e.getCheckinId());
        vo.setContractNo(e.getContractNo());
        vo.setContractName(e.getContractName());
        vo.setElderName(c == null ? "" : c.getElderName());
        vo.setIdCard(c == null ? "" : c.getIdCard());
        vo.setContractStartTime(formatDateTime(e.getContractStartTime()));
        vo.setContractEndTime(formatDateTime(e.getContractEndTime()));
        vo.setContractPeriod(formatDate(e.getContractStartTime()) + "  ~  " + formatDate(e.getContractEndTime()));
        vo.setContractStatus(resolveStatus(e));
        vo.setSignDate(c == null ? "-" : formatDate(c.getSignDate()));
        vo.setPayerName(c == null ? "" : nullToEmpty(c.getPayerName()));
        vo.setPayerContact(c == null ? "" : nullToEmpty(c.getPayerContact()));
        vo.setCreatedTime(formatDateTime(e.getCreatedTime()));
        vo.setContractFileName(c == null ? "" : c.getContractFileName());
        vo.setContractFileUrl(c == null ? "" : nullToEmpty(c.getContractFileUrl()));

        if ("已失效".equals(vo.getContractStatus())) {
            CheckoutApplication checkout = checkoutMapper.selectOne(new LambdaQueryWrapper<CheckoutApplication>()
                    .eq(CheckoutApplication::getCheckinId, e.getCheckinId())
                    .eq(CheckoutApplication::getIsDeleted, 0)
                    .eq(CheckoutApplication::getStatus, 2)
                    .orderByDesc(CheckoutApplication::getUpdatedTime)
                    .last("limit 1"));
            if (checkout != null) {
                vo.setDissolveSubmitter(nullToEmpty(checkout.getVoucherSubmitter()));
                vo.setDissolveDate(formatDate(checkout.getAgreementDate()));
                vo.setDissolveAgreementFileName(nullToEmpty(checkout.getAgreementFileName()));
                vo.setDissolveAgreementFileUrl("");
            }
        }
        return vo;
    }

    private ContractTrackingItemVO toItemVO(ContractTracking e) {
        CheckinApplication c = checkinMapper.selectById(e.getCheckinId());
        ContractTrackingItemVO vo = new ContractTrackingItemVO();
        vo.setId(e.getId());
        vo.setContractNo(e.getContractNo());
        vo.setContractName(e.getContractName());
        vo.setElderName(c == null ? "" : c.getElderName());
        vo.setIdCard(c == null ? "" : c.getIdCard());
        vo.setContractPeriod(formatDate(e.getContractStartTime()) + "  ~  " + formatDate(e.getContractEndTime()));
        vo.setContractStatus(resolveStatus(e));
        vo.setCreatedTime(formatDateTime(e.getCreatedTime()));
        return vo;
    }

    private String resolveStatus(ContractTracking e) {
        if (e.getInvalidatedTime() != null) {
            return "已失效";
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(e.getContractStartTime())) {
            return "未生效";
        }
        if (!now.isAfter(e.getContractEndTime())) {
            return "生效中";
        }
        return "已过期";
    }

    private String formatDateTime(LocalDateTime t) {
        return t == null ? "-" : t.format(DT);
    }

    private String formatDate(LocalDateTime t) {
        return t == null ? "-" : t.format(D);
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

}
