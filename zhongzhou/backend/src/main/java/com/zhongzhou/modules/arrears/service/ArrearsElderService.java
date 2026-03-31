package com.zhongzhou.modules.arrears.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.arrears.vo.ArrearsBillRowVO;
import com.zhongzhou.modules.arrears.vo.ArrearsElderItemVO;
import com.zhongzhou.modules.bill.entity.BillManage;
import com.zhongzhou.modules.bill.mapper.BillManageMapper;
import com.zhongzhou.modules.checkin.entity.CheckinApplication;
import com.zhongzhou.modules.checkin.mapper.CheckinApplicationMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ArrearsElderService {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int PENDING = 1;

    private final BillManageMapper billManageMapper;
    private final CheckinApplicationMapper checkinApplicationMapper;

    public ArrearsElderService(BillManageMapper billManageMapper, CheckinApplicationMapper checkinApplicationMapper) {
        this.billManageMapper = billManageMapper;
        this.checkinApplicationMapper = checkinApplicationMapper;
    }

    public PageResult<ArrearsElderItemVO> page(String elderName, String bedNo, int page, int pageSize) {
        String elderNameValue = normalize(elderName);
        String bedNoValue = normalize(bedNo);
        LocalDateTime now = LocalDateTime.now();

        List<BillManage> overdue = billManageMapper.selectList(new LambdaQueryWrapper<BillManage>()
                .eq(BillManage::getIsDeleted, 0)
                .eq(BillManage::getTradeStatus, PENDING)
                .lt(BillManage::getPayDeadline, now));

        Map<String, List<BillManage>> byElderBed = overdue.stream()
                .collect(Collectors.groupingBy(b -> groupKey(b.getElderName(), b.getBedNo())));

        List<ArrearsAgg> aggs = new ArrayList<>();
        for (Map.Entry<String, List<BillManage>> e : byElderBed.entrySet()) {
            List<BillManage> bills = e.getValue();
            if (bills.isEmpty()) {
                continue;
            }
            BillManage first = bills.get(0);
            String en = defaultStr(first.getElderName());
            String bn = defaultStr(first.getBedNo());
            if (elderNameValue != null && !en.equals(elderNameValue)) {
                continue;
            }
            if (bedNoValue != null && !bn.equals(bedNoValue)) {
                continue;
            }
            BigDecimal sum = bills.stream()
                    .map(BillManage::getPayableAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            LocalDateTime maxDeadline = bills.stream()
                    .map(BillManage::getPayDeadline)
                    .filter(Objects::nonNull)
                    .max(LocalDateTime::compareTo)
                    .orElse(LocalDateTime.MIN);
            LocalDateTime checkin = resolveLatestCheckinStart(en, bn);
            aggs.add(new ArrearsAgg(en, bn, sum, maxDeadline, checkin));
        }

        aggs.sort((a, b) -> {
            int c = b.deadline.compareTo(a.deadline);
            if (c != 0) {
                return c;
            }
            return b.checkinStart.compareTo(a.checkinStart);
        });

        long total = aggs.size();
        int from = Math.max(0, (page - 1) * pageSize);
        List<ArrearsAgg> slice = from >= aggs.size() ? List.of() : aggs.subList(from, Math.min(from + pageSize, aggs.size()));
        List<ArrearsElderItemVO> rows = slice.stream().map(this::toItemVO).collect(Collectors.toList());
        return new PageResult<>(total, rows);
    }

    public List<ArrearsBillRowVO> listOverdueBills(String elderName, String bedNo) {
        String en = normalize(elderName);
        String bn = normalize(bedNo);
        if (en == null || bn == null) {
            throw new IllegalArgumentException("老人姓名与床位号不能为空");
        }
        LocalDateTime now = LocalDateTime.now();
        List<BillManage> list = billManageMapper.selectList(new LambdaQueryWrapper<BillManage>()
                .eq(BillManage::getIsDeleted, 0)
                .eq(BillManage::getTradeStatus, PENDING)
                .eq(BillManage::getElderName, en)
                .eq(BillManage::getBedNo, bn)
                .lt(BillManage::getPayDeadline, now)
                .orderByDesc(BillManage::getPayDeadline)
                .orderByDesc(BillManage::getId));
        return list.stream().map(this::toBillRow).collect(Collectors.toList());
    }

    private ArrearsElderItemVO toItemVO(ArrearsAgg a) {
        ArrearsElderItemVO vo = new ArrearsElderItemVO();
        vo.setElderName(a.elderName);
        vo.setBedNo(a.bedNo);
        vo.setArrearsAmount(a.amount);
        vo.setPayDeadline(a.deadline == null ? "" : FMT.format(a.deadline));
        return vo;
    }

    private ArrearsBillRowVO toBillRow(BillManage b) {
        ArrearsBillRowVO vo = new ArrearsBillRowVO();
        vo.setBillNo(defaultStr(b.getBillNo()));
        vo.setBillMonth(defaultStr(b.getBillMonth()));
        vo.setPayDeadline(b.getPayDeadline() == null ? "" : FMT.format(b.getPayDeadline()));
        vo.setPayableAmount(b.getPayableAmount() == null ? BigDecimal.ZERO : b.getPayableAmount());
        return vo;
    }

    private LocalDateTime resolveLatestCheckinStart(String elderName, String bedNo) {
        CheckinApplication one = checkinApplicationMapper.selectOne(new LambdaQueryWrapper<CheckinApplication>()
                .eq(CheckinApplication::getIsDeleted, 0)
                .eq(CheckinApplication::getIsRetired, 0)
                .eq(CheckinApplication::getElderName, elderName)
                .eq(CheckinApplication::getRoomNo, bedNo)
                .orderByDesc(CheckinApplication::getCheckinStartDate)
                .orderByDesc(CheckinApplication::getId)
                .last("limit 1"));
        return one == null || one.getCheckinStartDate() == null ? LocalDateTime.MIN : one.getCheckinStartDate();
    }

    private static String groupKey(String elderName, String bedNo) {
        return defaultStr(elderName) + "\0" + defaultStr(bedNo);
    }

    private static String defaultStr(String s) {
        return s == null ? "" : s.trim();
    }

    private static String normalize(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static final class ArrearsAgg {
        final String elderName;
        final String bedNo;
        final BigDecimal amount;
        final LocalDateTime deadline;
        final LocalDateTime checkinStart;

        ArrearsAgg(String elderName, String bedNo, BigDecimal amount, LocalDateTime deadline, LocalDateTime checkinStart) {
            this.elderName = elderName;
            this.bedNo = bedNo;
            this.amount = amount;
            this.deadline = deadline;
            this.checkinStart = checkinStart;
        }
    }
}
