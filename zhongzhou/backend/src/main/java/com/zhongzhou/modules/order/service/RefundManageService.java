package com.zhongzhou.modules.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.order.entity.OrderManage;
import com.zhongzhou.modules.order.entity.OrderRefundRecord;
import com.zhongzhou.modules.order.mapper.OrderManageMapper;
import com.zhongzhou.modules.order.mapper.OrderRefundRecordMapper;
import com.zhongzhou.modules.order.vo.RefundDetailVO;
import com.zhongzhou.modules.order.vo.RefundManageVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RefundManageService {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final OrderRefundRecordMapper refundMapper;
    private final OrderManageMapper orderMapper;

    public RefundManageService(OrderRefundRecordMapper refundMapper, OrderManageMapper orderMapper) {
        this.refundMapper = refundMapper;
        this.orderMapper = orderMapper;
    }

    public PageResult<RefundManageVO> page(String refundNo, String orderNo, String applicant, String beginTime, String endTime,
                                           Integer status, int page, int pageSize) {
        LocalDateTime begin = (beginTime == null || beginTime.isBlank()) ? null : LocalDateTime.parse(beginTime, FMT);
        LocalDateTime end = (endTime == null || endTime.isBlank()) ? null : LocalDateTime.parse(endTime, FMT);
        LambdaQueryWrapper<OrderRefundRecord> qw = new LambdaQueryWrapper<OrderRefundRecord>()
                .eq(status != null && status > 0, OrderRefundRecord::getStatus, status)
                .eq(refundNo != null && !refundNo.isBlank(), OrderRefundRecord::getRefundNo, refundNo)
                .eq(orderNo != null && !orderNo.isBlank(), OrderRefundRecord::getOrderNo, orderNo)
                .like(applicant != null && !applicant.isBlank(), OrderRefundRecord::getApplicant, applicant)
                .ge(begin != null, OrderRefundRecord::getApplyTime, begin)
                .le(end != null, OrderRefundRecord::getApplyTime, end)
                .orderByDesc(OrderRefundRecord::getId);
        Page<OrderRefundRecord> p = refundMapper.selectPage(new Page<>(page, pageSize), qw);
        List<RefundManageVO> rows = p.getRecords().stream().map(this::toRow).collect(Collectors.toList());
        return new PageResult<>(p.getTotal(), rows);
    }

    public RefundDetailVO detail(Long id) {
        OrderRefundRecord r = refundMapper.selectById(id);
        if (r == null) throw new IllegalArgumentException("退款记录不存在");
        RefundDetailVO vo = new RefundDetailVO();
        vo.setRefundNo(defaultStr(r.getRefundNo()));
        vo.setOrderNo(defaultStr(r.getOrderNo()));
        vo.setRefundStatusLabel(defaultStr(r.getRefundStatusLabel()));
        vo.setApplicant(defaultStr(r.getApplicant()));
        vo.setApplicantType(defaultStr(r.getApplicantType()));
        vo.setApplyTime(format(r.getApplyTime()));
        vo.setRefundReason(defaultStr(r.getRefundReason()));
        vo.setRefundChannel(defaultStr(r.getRefundChannel()));
        vo.setRefundMethod(defaultStr(r.getRefundMethod()));
        vo.setRefundTime(format(r.getCreatedTime()));
        vo.setRefundAmount(r.getRefundAmount());
        vo.setFailCode(defaultStr(r.getFailCode()));
        vo.setFailReason(defaultStr(r.getFailReason()));
        OrderManage o = orderMapper.selectById(r.getOrderId());
        vo.setOrderStatusLabel(o == null ? "-" : orderStatusLabel(o.getStatus()));
        return vo;
    }

    private RefundManageVO toRow(OrderRefundRecord r) {
        RefundManageVO vo = new RefundManageVO();
        vo.setId(r.getId());
        vo.setRefundNo(defaultStr(r.getRefundNo()));
        vo.setOrderNo(defaultStr(r.getOrderNo()));
        vo.setApplicant(defaultStr(r.getApplicant()));
        vo.setApplyTime(format(r.getApplyTime()));
        vo.setRefundStatusLabel(defaultStr(r.getRefundStatusLabel()));
        if (Objects.equals(r.getStatus(), 2)) vo.setRefundTime(format(r.getCreatedTime()));
        OrderManage o = orderMapper.selectById(r.getOrderId());
        if (o != null) {
            vo.setOrderAmount(o.getOrderAmount());
            vo.setOrderStatusLabel(orderStatusLabel(o.getStatus()));
        } else {
            vo.setOrderStatusLabel("-");
        }
        return vo;
    }

    private String orderStatusLabel(Integer s) {
        if (Objects.equals(s, 1)) return "待支付";
        if (Objects.equals(s, 2)) return "待执行";
        if (Objects.equals(s, 3)) return "已执行";
        if (Objects.equals(s, 4)) return "已完成";
        if (Objects.equals(s, 5)) return "已关闭";
        if (Objects.equals(s, 6)) return "已退款";
        return "-";
    }
    private String format(LocalDateTime t) { return t == null ? "" : FMT.format(t); }
    private String defaultStr(String s) { return s == null ? "" : s; }
}
