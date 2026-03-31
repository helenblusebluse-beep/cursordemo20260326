package com.zhongzhou.modules.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.order.dto.OrderCancelRequest;
import com.zhongzhou.modules.order.dto.OrderRefundRequest;
import com.zhongzhou.modules.order.entity.OrderManage;
import com.zhongzhou.modules.order.entity.OrderPaymentRecord;
import com.zhongzhou.modules.order.entity.OrderExecuteRecord;
import com.zhongzhou.modules.order.entity.OrderRefundRecord;
import com.zhongzhou.modules.order.mapper.OrderExecuteRecordMapper;
import com.zhongzhou.modules.order.mapper.OrderManageMapper;
import com.zhongzhou.modules.order.mapper.OrderPaymentRecordMapper;
import com.zhongzhou.modules.order.mapper.OrderRefundRecordMapper;
import com.zhongzhou.modules.order.vo.OrderDetailVO;
import com.zhongzhou.modules.order.vo.OrderManageVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderManageService {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final OrderManageMapper orderManageMapper;
    private final OrderPaymentRecordMapper orderPaymentRecordMapper;
    private final OrderExecuteRecordMapper orderExecuteRecordMapper;
    private final OrderRefundRecordMapper orderRefundRecordMapper;

    public OrderManageService(OrderManageMapper orderManageMapper,
                              OrderPaymentRecordMapper orderPaymentRecordMapper,
                              OrderExecuteRecordMapper orderExecuteRecordMapper,
                              OrderRefundRecordMapper orderRefundRecordMapper) {
        this.orderManageMapper = orderManageMapper;
        this.orderPaymentRecordMapper = orderPaymentRecordMapper;
        this.orderExecuteRecordMapper = orderExecuteRecordMapper;
        this.orderRefundRecordMapper = orderRefundRecordMapper;
    }

    public PageResult<OrderManageVO> page(String orderNo, String elderName, String orderUserName, String beginTime, String endTime,
                                          Integer status, int page, int pageSize) {
        LocalDateTime begin = (beginTime == null || beginTime.isBlank()) ? null : parse(beginTime);
        LocalDateTime end = (endTime == null || endTime.isBlank()) ? null : parse(endTime);
        LambdaQueryWrapper<OrderManage> qw = new LambdaQueryWrapper<OrderManage>()
                .eq(OrderManage::getIsDeleted, 0)
                .eq(orderNo != null && !orderNo.isBlank(), OrderManage::getOrderNo, orderNo)
                .like(elderName != null && !elderName.isBlank(), OrderManage::getElderName, elderName)
                .like(orderUserName != null && !orderUserName.isBlank(), OrderManage::getOrderUserName, orderUserName)
                .ge(begin != null, OrderManage::getExpectedServiceTime, begin)
                .le(end != null, OrderManage::getExpectedServiceTime, end)
                .eq(status != null && status > 0, OrderManage::getStatus, status)
                .orderByDesc(OrderManage::getId);
        Page<OrderManage> p = orderManageMapper.selectPage(new Page<>(page, pageSize), qw);
        List<OrderManageVO> rows = p.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return new PageResult<>(p.getTotal(), rows);
    }

    public OrderDetailVO detail(Long id) {
        OrderManage e = mustGet(id);
        OrderDetailVO vo = new OrderDetailVO();
        copyBase(vo, e);
        vo.setNursingItemType(defaultStr(e.getNursingItemType()));
        vo.setOrderUserMobile(defaultStr(e.getOrderUserMobile()));
        vo.setRemark(defaultStr(e.getRemark()));
        vo.setPayTime(format(e.getPayTime()));
        vo.setExecuteTime(format(e.getExecuteTime()));
        vo.setFinishTime(format(e.getFinishTime()));
        vo.setCloseTime(format(e.getCloseTime()));
        vo.setCloseType(defaultStr(e.getCloseType()));
        vo.setCancelBy(defaultStr(e.getCancelBy()));
        vo.setCancelUserType(defaultStr(e.getCancelUserType()));
        vo.setCancelReason(defaultStr(e.getCancelReason()));
        vo.setCancelTime(format(e.getCancelTime()));
        vo.setRefundReason(defaultStr(e.getRefundReason()));
        vo.setRefundTime(format(e.getRefundTime()));

        OrderPaymentRecord pay = orderPaymentRecordMapper.selectOne(new LambdaQueryWrapper<OrderPaymentRecord>()
                .eq(OrderPaymentRecord::getOrderId, id).orderByDesc(OrderPaymentRecord::getId).last("limit 1"));
        if (pay != null) {
            vo.setTradeStatus(defaultStr(pay.getTradeStatus()));
            vo.setPayChannel(defaultStr(pay.getPayChannel()));
            vo.setPayMethod(defaultStr(pay.getPayMethod()));
            vo.setWxOrderNo(defaultStr(pay.getWxOrderNo()));
            vo.setPayAmountText(pay.getPayAmount() == null ? "" : pay.getPayAmount().toPlainString() + " 元");
        } else {
            vo.setTradeStatus(e.getStatus() == 1 ? "待支付" : (e.getStatus() == 5 ? "已关闭" : "已支付"));
            vo.setPayAmountText(e.getOrderAmount() == null ? "" : e.getOrderAmount().toPlainString() + " 元");
        }

        OrderExecuteRecord exec = orderExecuteRecordMapper.selectOne(new LambdaQueryWrapper<OrderExecuteRecord>()
                .eq(OrderExecuteRecord::getOrderId, id).orderByDesc(OrderExecuteRecord::getId).last("limit 1"));
        if (exec != null) {
            vo.setExecuteBy(defaultStr(exec.getExecuteBy()));
            vo.setExecuteImageUrl(defaultStr(exec.getExecuteImageUrl()));
            vo.setExecuteRecord(defaultStr(exec.getExecuteRecord()));
        }

        OrderRefundRecord ref = orderRefundRecordMapper.selectOne(new LambdaQueryWrapper<OrderRefundRecord>()
                .eq(OrderRefundRecord::getOrderId, id).orderByDesc(OrderRefundRecord::getId).last("limit 1"));
        if (ref != null) {
            vo.setRefundStatusText(defaultStr(ref.getRefundStatusLabel()));
            vo.setRefundApplicant(defaultStr(ref.getApplicant()));
            vo.setRefundApplicantType(defaultStr(ref.getApplicantType()));
            vo.setRefundApplyTime(format(ref.getApplyTime()));
            vo.setRefundChannel(defaultStr(ref.getRefundChannel()));
            vo.setRefundMethod(defaultStr(ref.getRefundMethod()));
            vo.setRefundNo(defaultStr(ref.getRefundNo()));
            vo.setRefundAmountText(ref.getRefundAmount() == null ? "" : ref.getRefundAmount().toPlainString() + " 元");
            vo.setRefundFailCode(defaultStr(ref.getFailCode()));
            vo.setRefundFailReason(defaultStr(ref.getFailReason()));
        }
        return vo;
    }

    @Transactional
    public void cancel(Long id, OrderCancelRequest req) {
        OrderManage e = mustGet(id);
        if (!Objects.equals(e.getStatus(), 1)) throw new IllegalArgumentException("仅待支付订单可取消");
        e.setStatus(5);
        e.setCancelReason(req.getCancelReason().trim());
        e.setCancelBy("顾廷烨");
        e.setCancelUserType("后台用户");
        e.setCancelTime(LocalDateTime.now());
        orderManageMapper.updateById(e);
    }

    @Transactional
    public void refund(Long id, OrderRefundRequest req) {
        OrderManage e = mustGet(id);
        if (!(Objects.equals(e.getStatus(), 2) || Objects.equals(e.getStatus(), 3))) {
            throw new IllegalArgumentException("仅待执行或已执行订单可退款");
        }
        e.setStatus(6);
        e.setRefundStatus(2);
        e.setRefundReason(req.getRefundReason().trim());
        e.setRefundTime(LocalDateTime.now());
        orderManageMapper.updateById(e);

        OrderRefundRecord r = new OrderRefundRecord();
        r.setOrderId(e.getId());
        r.setOrderNo(e.getOrderNo());
        r.setRefundReason(req.getRefundReason().trim());
        r.setRefundAmount(e.getOrderAmount());
        r.setStatus(2);
        r.setRefundStatusLabel("退款成功");
        r.setApplicant("顾廷烨");
        r.setApplicantType("后台用户");
        r.setApplyTime(LocalDateTime.now());
        r.setRefundChannel("原路返回");
        r.setRefundMethod("微信");
        r.setRefundNo("RF" + System.currentTimeMillis());
        orderRefundRecordMapper.insert(r);
    }

    private OrderManage mustGet(Long id) {
        OrderManage e = orderManageMapper.selectOne(new LambdaQueryWrapper<OrderManage>()
                .eq(OrderManage::getId, id)
                .eq(OrderManage::getIsDeleted, 0));
        if (e == null) throw new IllegalArgumentException("订单不存在");
        return e;
    }

    private OrderManageVO toVO(OrderManage e) {
        OrderManageVO vo = new OrderManageVO();
        copyBase(vo, e);
        return vo;
    }

    private void copyBase(OrderManageVO vo, OrderManage e) {
        vo.setId(e.getId());
        vo.setOrderNo(e.getOrderNo());
        vo.setElderName(e.getElderName());
        vo.setBedNo(e.getBedNo());
        vo.setNursingItemName(e.getNursingItemName());
        vo.setOrderAmount(e.getOrderAmount());
        vo.setExpectedServiceTime(format(e.getExpectedServiceTime()));
        vo.setOrderUserName(e.getOrderUserName());
        vo.setOrderTime(format(e.getOrderTime()));
        vo.setStatus(e.getStatus());
        vo.setStatusLabel(statusLabel(e.getStatus()));
        vo.setRefundStatus(e.getRefundStatus());
        vo.setRefundStatusLabel(refundLabel(e.getRefundStatus()));
    }

    private String statusLabel(Integer s) {
        if (Objects.equals(s, 1)) return "待支付";
        if (Objects.equals(s, 2)) return "待执行";
        if (Objects.equals(s, 3)) return "已执行";
        if (Objects.equals(s, 4)) return "已完成";
        if (Objects.equals(s, 5)) return "已关闭";
        if (Objects.equals(s, 6)) return "已退款";
        return "-";
    }

    private String refundLabel(Integer s) {
        if (Objects.equals(s, 1)) return "退款处理中";
        if (Objects.equals(s, 2)) return "已退款";
        if (Objects.equals(s, 3)) return "退款失败";
        return "-";
    }

    private String format(LocalDateTime t) { return t == null ? "" : FMT.format(t); }
    private LocalDateTime parse(String s) { return LocalDateTime.parse(s, FMT); }
    private String defaultStr(String s) { return s == null ? "" : s; }
}
