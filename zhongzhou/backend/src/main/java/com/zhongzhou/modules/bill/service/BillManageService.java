package com.zhongzhou.modules.bill.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhongzhou.modules.bill.entity.BillCancelRecord;
import com.zhongzhou.modules.bill.entity.BillItem;
import com.zhongzhou.modules.bill.entity.BillManage;
import com.zhongzhou.modules.bill.entity.BillRefundRecord;
import com.zhongzhou.modules.bill.mapper.BillCancelRecordMapper;
import com.zhongzhou.modules.bill.mapper.BillItemMapper;
import com.zhongzhou.modules.bill.mapper.BillManageMapper;
import com.zhongzhou.modules.bill.mapper.BillRefundRecordMapper;
import com.zhongzhou.modules.bill.vo.BillDetailVO;
import com.zhongzhou.modules.bill.vo.BillItemVO;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BillManageService {
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final BillManageMapper billManageMapper;
    private final BillItemMapper billItemMapper;
    private final BillRefundRecordMapper billRefundRecordMapper;
    private final BillCancelRecordMapper billCancelRecordMapper;

    public BillManageService(BillManageMapper billManageMapper, BillItemMapper billItemMapper,
                             BillRefundRecordMapper billRefundRecordMapper, BillCancelRecordMapper billCancelRecordMapper) {
        this.billManageMapper = billManageMapper;
        this.billItemMapper = billItemMapper;
        this.billRefundRecordMapper = billRefundRecordMapper;
        this.billCancelRecordMapper = billCancelRecordMapper;
    }

    public BillDetailVO detail(Long id) {
        BillManage bill = billManageMapper.selectOne(new LambdaQueryWrapper<BillManage>()
                .eq(BillManage::getId, id)
                .eq(BillManage::getIsDeleted, 0));
        if (bill == null) {
            throw new IllegalArgumentException("账单不存在");
        }
        BillDetailVO vo = new BillDetailVO();
        vo.setId(bill.getId());
        vo.setBillNo(defaultStr(bill.getBillNo()));
        vo.setBillType(defaultStr(bill.getBillType()));
        vo.setBillMonth(defaultStr(bill.getBillMonth()));
        vo.setElderName(defaultStr(bill.getElderName()));
        vo.setElderIdNo(defaultStr(bill.getElderIdNo()));
        vo.setRelatedOrderNo(defaultStr(bill.getRelatedOrderNo()));
        vo.setBillTotalAmount(bill.getBillTotalAmount());
        vo.setPayableAmount(bill.getPayableAmount());
        vo.setPrepayDeductAmount(bill.getPrepayDeductAmount());
        vo.setBillPeriod((bill.getBillPeriodStart() == null ? "" : bill.getBillPeriodStart().toString()) +
                " — " + (bill.getBillPeriodEnd() == null ? "" : bill.getBillPeriodEnd().toString()));
        vo.setTotalDays(bill.getTotalDays());
        vo.setPayDeadline(bill.getPayDeadline() == null ? "" : DT.format(bill.getPayDeadline()));
        vo.setTradeStatusLabel(statusLabel(bill.getTradeStatus()));
        vo.setCreatedBy(defaultStr(bill.getCreatedBy()));
        vo.setCreatedTime(bill.getCreatedTime() == null ? "" : DT.format(bill.getCreatedTime()));

        List<BillItem> rows = billItemMapper.selectList(new LambdaQueryWrapper<BillItem>()
                .eq(BillItem::getBillId, id)
                .orderByAsc(BillItem::getSortNo, BillItem::getId));
        vo.setItems(rows.stream().map(e -> {
            BillItemVO item = new BillItemVO();
            item.setType(defaultStr(e.getItemGroup()));
            item.setFeeItemName(defaultStr(e.getFeeItemName()));
            item.setServiceContent(defaultStr(e.getServiceContent()));
            item.setAmount(e.getAmount());
            return item;
        }).collect(Collectors.toList()));

        BillRefundRecord refund = billRefundRecordMapper.selectOne(new LambdaQueryWrapper<BillRefundRecord>()
                .eq(BillRefundRecord::getBillId, id)
                .orderByDesc(BillRefundRecord::getId)
                .last("limit 1"));
        if (refund != null) {
            vo.setRefundApplicant(defaultStr(refund.getApplicant()));
            vo.setRefundApplicantType(defaultStr(refund.getApplicantType()));
            vo.setRefundSubmitTime(refund.getSubmitTime() == null ? "" : DT.format(refund.getSubmitTime()));
            vo.setRefundMethod(defaultStr(refund.getRefundMethod()));
            vo.setRefundActualDays(refund.getActualDays());
            vo.setRefundDays(refund.getRefundDays());
            vo.setRefundAmount(refund.getRefundAmount());
        }

        BillCancelRecord cancel = billCancelRecordMapper.selectOne(new LambdaQueryWrapper<BillCancelRecord>()
                .eq(BillCancelRecord::getBillId, id)
                .orderByDesc(BillCancelRecord::getId)
                .last("limit 1"));
        if (cancel != null) {
            vo.setCancelBy(defaultStr(cancel.getCancelBy()));
            vo.setCancelUserType(defaultStr(cancel.getCancelUserType()));
            vo.setCancelTime(cancel.getCancelTime() == null ? "" : DT.format(cancel.getCancelTime()));
            vo.setCancelReason(defaultStr(cancel.getCancelReason()));
        }
        return vo;
    }

    private String statusLabel(Integer status) {
        if (Objects.equals(status, 1)) return "待支付";
        if (Objects.equals(status, 2)) return "已支付";
        if (Objects.equals(status, 3)) return "已关闭";
        if (Objects.equals(status, 4)) return "已关闭";
        return "-";
    }

    private String defaultStr(String s) {
        return s == null ? "" : s;
    }
}
