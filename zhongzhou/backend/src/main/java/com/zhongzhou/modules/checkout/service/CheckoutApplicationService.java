package com.zhongzhou.modules.checkout.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.checkin.entity.CheckinApplication;
import com.zhongzhou.modules.checkin.mapper.CheckinApplicationMapper;
import com.zhongzhou.modules.checkout.dto.CheckoutApplyRequest;
import com.zhongzhou.modules.checkout.dto.CheckoutVoucherRequest;
import com.zhongzhou.modules.checkout.entity.CheckoutApplication;
import com.zhongzhou.modules.checkout.entity.CheckoutFeeItem;
import com.zhongzhou.modules.checkout.mapper.CheckoutApplicationMapper;
import com.zhongzhou.modules.checkout.mapper.CheckoutFeeItemMapper;
import com.zhongzhou.modules.checkout.vo.CheckoutCandidateVO;
import com.zhongzhou.modules.checkout.vo.CheckoutDetailVO;
import com.zhongzhou.modules.checkout.vo.CheckoutApplicationVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class CheckoutApplicationService {
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private static final DateTimeFormatter D = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.CHINA);
    private final CheckoutApplicationMapper mapper;
    private final CheckoutFeeItemMapper feeItemMapper;
    private final CheckinApplicationMapper checkinMapper;

    public CheckoutApplicationService(CheckoutApplicationMapper mapper, CheckoutFeeItemMapper feeItemMapper, CheckinApplicationMapper checkinMapper) {
        this.mapper = mapper;
        this.feeItemMapper = feeItemMapper;
        this.checkinMapper = checkinMapper;
    }

    public PageResult<CheckoutApplicationVO> page(String elderName, String idCard, int page, int pageSize) {
        LambdaQueryWrapper<CheckoutApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CheckoutApplication::getIsDeleted, 0).eq(CheckoutApplication::getStatus, 2);
        if (StringUtils.hasText(elderName)) {
            wrapper.like(CheckoutApplication::getElderName, elderName.trim());
        }
        if (StringUtils.hasText(idCard)) {
            wrapper.eq(CheckoutApplication::getIdCard, idCard.trim().toUpperCase(Locale.ROOT));
        }
        wrapper.orderByDesc(CheckoutApplication::getCreatedTime);
        Page<CheckoutApplication> p = mapper.selectPage(new Page<>(page, pageSize), wrapper);
        List<CheckoutApplicationVO> rows = p.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return new PageResult<>(p.getTotal(), rows);
    }

    public Long apply(CheckoutApplyRequest req) {
        CheckinApplication checkin = checkinMapper.selectById(req.getCheckinId());
        if (checkin == null || checkin.getIsDeleted() == 1 || checkin.getIsRetired() == 1) throw new IllegalArgumentException("老人数据不存在或已退住");
        CheckoutApplication entity = new CheckoutApplication();
        entity.setCheckinId(checkin.getId());
        entity.setElderName(checkin.getElderName());
        entity.setIdCard(checkin.getIdCard());
        entity.setRoomNo(checkin.getRoomNo());
        entity.setCareLevel(checkin.getCareLevel());
        entity.setContactPhone(checkin.getContactPhone());
        String checkinPeriod = (checkin.getCheckinStartDate() == null ? "" : checkin.getCheckinStartDate().format(D))
                + "~" + (checkin.getCheckinEndDate() == null ? "" : checkin.getCheckinEndDate().format(D));
        entity.setCheckinPeriod(checkinPeriod);
        entity.setFeePeriod(checkinPeriod);
        entity.setCheckoutDate(req.getCheckoutDate());
        entity.setCheckoutReason(req.getCheckoutReason());
        entity.setAgreementDate(req.getAgreementDate());
        entity.setAgreementFileName(req.getAgreementFileName());
        entity.setStatus(1);
        entity.setIsDeleted(0);
        entity.setCreatedTime(LocalDateTime.now());
        entity.setUpdatedTime(LocalDateTime.now());
        mapper.insert(entity);
        seedFeeItems(entity.getId());
        return entity.getId();
    }

    public List<CheckoutCandidateVO> candidates() {
        List<CheckinApplication> list = checkinMapper.selectList(new LambdaQueryWrapper<CheckinApplication>()
                .eq(CheckinApplication::getIsDeleted, 0)
                .eq(CheckinApplication::getIsRetired, 0)
                .orderByDesc(CheckinApplication::getCreatedTime));
        return list.stream().map(e -> {
            CheckoutCandidateVO vo = new CheckoutCandidateVO();
            vo.setCheckinId(e.getId());
            vo.setElderName(e.getElderName());
            vo.setIdCard(e.getIdCard());
            return vo;
        }).collect(Collectors.toList());
    }

    public CheckoutDetailVO detail(Long id) {
        CheckoutApplication e = mapper.selectById(id);
        if (e == null || e.getIsDeleted() == 1) throw new IllegalArgumentException("退住申请不存在");
        List<CheckoutFeeItem> items = feeItemMapper.selectList(new LambdaQueryWrapper<CheckoutFeeItem>()
                .eq(CheckoutFeeItem::getApplicationId, id)
                .eq(CheckoutFeeItem::getIsDeleted, 0)
                .orderByDesc(CheckoutFeeItem::getCreatedTime));
        CheckoutDetailVO vo = new CheckoutDetailVO();
        vo.setId(e.getId());
        vo.setCheckinId(e.getCheckinId());
        vo.setElderName(e.getElderName());
        vo.setIdCard(e.getIdCard());
        vo.setRoomNo(e.getRoomNo());
        vo.setCareLevel(e.getCareLevel());
        vo.setCheckinPeriod(e.getCheckinPeriod());
        vo.setFeePeriod(e.getFeePeriod());
        vo.setContactPhone(e.getContactPhone());
        vo.setCheckoutDate(e.getCheckoutDate() == null ? null : e.getCheckoutDate().format(D));
        vo.setCheckoutReason(e.getCheckoutReason());
        vo.setAgreementDate(e.getAgreementDate() == null ? null : e.getAgreementDate().format(D));
        vo.setAgreementFileName(e.getAgreementFileName());
        vo.setCaregiverNames(e.getCaregiverNames());
        CheckinApplication checkin = checkinMapper.selectById(e.getCheckinId());
        if (checkin != null && StringUtils.hasText(checkin.getHomeAddress())) {
            vo.setHomeAddress(checkin.getHomeAddress());
        }
        vo.setRefundMethod(e.getRefundMethod());
        vo.setRefundVoucherName(e.getRefundVoucherName());
        vo.setRefundVoucherUrl(e.getRefundVoucherUrl());
        vo.setRefundRemark(e.getRefundRemark());
        vo.setVoucherSubmitter(e.getVoucherSubmitter());
        vo.setVoucherSubmitTime(e.getVoucherSubmitTime() == null ? null : e.getVoucherSubmitTime().format(DT));
        vo.setStatus(e.getStatus());
        vo.setShouldRefundItems(mapCategory(items, 1));
        vo.setDebtItems(mapCategory(items, 2));
        vo.setBalanceItems(mapCategory(items, 3));
        vo.setUnpaidItems(mapCategory(items, 4));
        vo.setShouldRefundSubtotal(sum(vo.getShouldRefundItems()));
        vo.setDebtSubtotal(sum(vo.getDebtItems()));
        vo.setBalanceSubtotal(sum(vo.getBalanceItems()));
        vo.setUnpaidSubtotal(sumActiveUnpaid(vo.getUnpaidItems()));
        double finalAmount = vo.getShouldRefundSubtotal() - vo.getDebtSubtotal() + vo.getBalanceSubtotal() - vo.getUnpaidSubtotal();
        vo.setFinalRefundAmount(round(finalAmount));
        return vo;
    }

    public void uploadVoucher(Long id, CheckoutVoucherRequest req) {
        CheckoutApplication e = mapper.selectById(id);
        if (e == null || e.getIsDeleted() == 1) throw new IllegalArgumentException("退住申请不存在");
        e.setRefundMethod(req.getRefundMethod());
        e.setRefundVoucherName(req.getRefundVoucherName());
        e.setRefundRemark(req.getRefundRemark());
        if (StringUtils.hasText(req.getRefundVoucherUrl())) {
            e.setRefundVoucherUrl(req.getRefundVoucherUrl().trim());
        }
        if (StringUtils.hasText(req.getVoucherSubmitter())) {
            e.setVoucherSubmitter(req.getVoucherSubmitter().trim());
        } else {
            e.setVoucherSubmitter("管理员");
        }
        e.setVoucherSubmitTime(LocalDateTime.now());
        e.setUpdatedTime(LocalDateTime.now());
        mapper.updateById(e);
    }

    public void cancelUnpaid(Long id, Long feeId) {
        CheckoutFeeItem item = feeItemMapper.selectById(feeId);
        if (item == null || !id.equals(item.getApplicationId()) || item.getIsDeleted() == 1 || item.getFeeCategory() != 4) {
            throw new IllegalArgumentException("未缴账单不存在");
        }
        item.setStatus(2);
        feeItemMapper.updateById(item);
    }

    public void submit(Long id) {
        CheckoutApplication e = mapper.selectById(id);
        if (e == null || e.getIsDeleted() == 1) throw new IllegalArgumentException("退住申请不存在");
        CheckoutDetailVO detail = detail(id);
        long activeUnpaid = detail.getUnpaidItems().stream().filter(x -> x.getStatus() == 1).count();
        if (activeUnpaid > 0) throw new IllegalArgumentException("请先取消/支付未缴账单");
        if (detail.getFinalRefundAmount() < 0 && !StringUtils.hasText(e.getRefundVoucherName())) {
            throw new IllegalArgumentException("请上传退款凭证");
        }
        e.setFinalRefundAmount(detail.getFinalRefundAmount());
        e.setStatus(2);
        e.setUpdatedTime(LocalDateTime.now());
        mapper.updateById(e);
        CheckinApplication checkin = checkinMapper.selectById(e.getCheckinId());
        if (checkin != null && checkin.getIsDeleted() == 0) {
            checkin.setIsRetired(1);
            checkin.setUpdatedTime(LocalDateTime.now());
            checkinMapper.updateById(checkin);
        }
    }

    private CheckoutApplicationVO toVO(CheckoutApplication e) {
        CheckoutApplicationVO vo = new CheckoutApplicationVO();
        vo.setId(e.getId());
        vo.setCheckinId(e.getCheckinId());
        vo.setElderName(e.getElderName());
        vo.setIdCard(e.getIdCard());
        vo.setCheckoutDate(e.getCheckoutDate() == null ? null : e.getCheckoutDate().format(D));
        vo.setCreatedTime(e.getCreatedTime() == null ? null : e.getCreatedTime().format(DT));
        return vo;
    }

    private void seedFeeItems(Long applicationId) {
        // 应退（2项）
        insertFee(applicationId, 1, "ZD2048101015000001", "2048-10", "月度账单", 2000D, 18, 12);
        insertFee(applicationId, 1, "ZD2048101015000002", "2048-10", "费用账单", 20D, 0, 0);

        // 欠费（3项）
        insertFee(applicationId, 2, "QD2048101015000001", "2048-10", "月度账单", 2000D, 0, 0);
        insertFee(applicationId, 2, "QD2048101015000002", "2048-10", "费用账单", 2000D, 0, 0);
        insertFee(applicationId, 2, "QD2048101015000003", "2048-10", "费用账单", 2000D, 0, 0);

        // 余额（2项）
        insertFee(applicationId, 3, "YE2048101015000001", "2048-10", "押金余额", 2000D, 0, 0);
        insertFee(applicationId, 3, "YE2048101015000002", "2048-10", "押金余额", 20D, 0, 0);

        // 未缴（3项）
        insertFee(applicationId, 4, "WJ2048101015000001", "2048-10", "费用账单", 2000D, 0, 0);
        insertFee(applicationId, 4, "WJ2048101015000002", "2048-10", "费用账单", 2000D, 0, 0);
        insertFee(applicationId, 4, "WJ2048101015000003", "2048-10", "费用账单", 2000D, 0, 0);
    }

    private void insertFee(Long appId, Integer category, String billNo, String billMonth, String itemName, Double amount, Integer actualDays, Integer refundDays) {
        CheckoutFeeItem item = new CheckoutFeeItem();
        item.setApplicationId(appId);
        item.setFeeCategory(category);
        item.setBillNo(billNo);
        item.setBillMonth(billMonth);
        item.setItemName(itemName);
        item.setAmount(amount);
        item.setActualDays(actualDays);
        item.setRefundDays(refundDays);
        item.setStatus(1);
        item.setIsDeleted(0);
        item.setCreatedTime(LocalDateTime.now());
        feeItemMapper.insert(item);
    }

    private List<CheckoutDetailVO.FeeItemVO> mapCategory(List<CheckoutFeeItem> items, int category) {
        List<CheckoutDetailVO.FeeItemVO> list = new ArrayList<>();
        for (CheckoutFeeItem item : items) {
            if (item.getFeeCategory() == null || item.getFeeCategory() != category) continue;
            CheckoutDetailVO.FeeItemVO vo = new CheckoutDetailVO.FeeItemVO();
            vo.setId(item.getId());
            vo.setBillNo(item.getBillNo());
            vo.setBillMonth(item.getBillMonth());
            vo.setItemName(item.getItemName());
            vo.setAmount(item.getAmount() == null ? 0D : item.getAmount());
            vo.setActualDays(item.getActualDays());
            vo.setRefundDays(item.getRefundDays());
            vo.setStatus(item.getStatus() == null ? 1 : item.getStatus());
            list.add(vo);
        }
        return list;
    }

    private Double sum(List<CheckoutDetailVO.FeeItemVO> items) {
        double total = 0D;
        for (CheckoutDetailVO.FeeItemVO x : items) total += x.getAmount() == null ? 0D : x.getAmount();
        return round(total);
    }

    private Double sumActiveUnpaid(List<CheckoutDetailVO.FeeItemVO> items) {
        double total = 0D;
        for (CheckoutDetailVO.FeeItemVO x : items) if (x.getStatus() != null && x.getStatus() == 1) total += x.getAmount() == null ? 0D : x.getAmount();
        return round(total);
    }

    private Double round(double v) { return Math.round(v * 100D) / 100D; }
}
