package com.zhongzhou.modules.prepay.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.balance.entity.BalanceQuery;
import com.zhongzhou.modules.balance.mapper.BalanceQueryMapper;
import com.zhongzhou.modules.bill.entity.BillManage;
import com.zhongzhou.modules.bill.mapper.BillManageMapper;
import com.zhongzhou.modules.checkin.entity.CheckinApplication;
import com.zhongzhou.modules.checkin.mapper.CheckinApplicationMapper;
import com.zhongzhou.modules.prepay.dto.PrepayRechargeCreateRequest;
import com.zhongzhou.modules.prepay.entity.PrepayRecharge;
import com.zhongzhou.modules.prepay.mapper.PrepayRechargeMapper;
import com.zhongzhou.modules.prepay.vo.PrepayElderOptionVO;
import com.zhongzhou.modules.prepay.vo.PrepayRechargeItemVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrepayRechargeService {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final PrepayRechargeMapper prepayRechargeMapper;
    private final CheckinApplicationMapper checkinApplicationMapper;
    private final BillManageMapper billManageMapper;
    private final BalanceQueryMapper balanceQueryMapper;

    public PrepayRechargeService(PrepayRechargeMapper prepayRechargeMapper,
                                 CheckinApplicationMapper checkinApplicationMapper,
                                 BillManageMapper billManageMapper,
                                 BalanceQueryMapper balanceQueryMapper) {
        this.prepayRechargeMapper = prepayRechargeMapper;
        this.checkinApplicationMapper = checkinApplicationMapper;
        this.billManageMapper = billManageMapper;
        this.balanceQueryMapper = balanceQueryMapper;
    }

    public PageResult<PrepayRechargeItemVO> page(String elderName, String bedNo, int page, int pageSize) {
        String elderNameValue = elderName == null ? null : elderName.trim();
        String bedNoValue = bedNo == null ? null : bedNo.trim();
        LambdaQueryWrapper<PrepayRecharge> qw = new LambdaQueryWrapper<PrepayRecharge>()
                .eq(PrepayRecharge::getIsDeleted, 0)
                .eq(elderNameValue != null && !elderNameValue.isBlank(), PrepayRecharge::getElderName, elderNameValue)
                .eq(bedNoValue != null && !bedNoValue.isBlank(), PrepayRecharge::getBedNo, bedNoValue)
                .orderByDesc(PrepayRecharge::getCreatedTime, PrepayRecharge::getId);
        Page<PrepayRecharge> p = prepayRechargeMapper.selectPage(new Page<>(page, pageSize), qw);
        List<PrepayRechargeItemVO> rows = p.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return new PageResult<>(p.getTotal(), rows);
    }

    public List<PrepayElderOptionVO> elderOptions() {
        return checkinApplicationMapper.selectList(new LambdaQueryWrapper<CheckinApplication>()
                        .eq(CheckinApplication::getIsDeleted, 0)
                        .eq(CheckinApplication::getIsRetired, 0)
                        .orderByDesc(CheckinApplication::getCreatedTime, CheckinApplication::getId))
                .stream()
                .map(e -> {
                    PrepayElderOptionVO vo = new PrepayElderOptionVO();
                    vo.setElderId(e.getId());
                    vo.setElderName(defaultStr(e.getElderName()));
                    vo.setElderIdNo(defaultStr(e.getIdCard()));
                    vo.setBedNo(defaultStr(e.getRoomNo()));
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void create(PrepayRechargeCreateRequest req) {
        CheckinApplication elder = checkinApplicationMapper.selectOne(new LambdaQueryWrapper<CheckinApplication>()
                .eq(CheckinApplication::getId, req.getElderId())
                .eq(CheckinApplication::getIsDeleted, 0)
                .eq(CheckinApplication::getIsRetired, 0));
        if (elder == null) {
            throw new IllegalArgumentException("老人不存在或已退住");
        }
        PrepayRecharge rec = new PrepayRecharge();
        rec.setRechargeNo(genRechargeNo());
        rec.setElderName(defaultStr(elder.getElderName()));
        rec.setElderIdNo(defaultStr(elder.getIdCard()));
        rec.setBedNo(defaultStr(elder.getRoomNo()));
        rec.setRechargeMethod(req.getRechargeMethod().trim());
        rec.setRechargeAmount(req.getRechargeAmount());
        rec.setVoucherFileName(req.getVoucherFileName().trim());
        rec.setVoucherFileUrl(req.getVoucherFileUrl().trim());
        rec.setRechargeRemark(req.getRechargeRemark().trim());
        rec.setCreatedTime(LocalDateTime.now());
        rec.setIsDeleted(0);
        prepayRechargeMapper.insert(rec);

        // 按支付截止时间正序自动扣欠费账单（待支付）
        BigDecimal remain = req.getRechargeAmount();
        List<BillManage> bills = billManageMapper.selectList(new LambdaQueryWrapper<BillManage>()
                .eq(BillManage::getIsDeleted, 0)
                .eq(BillManage::getTradeStatus, 1)
                .eq(BillManage::getElderName, elder.getElderName())
                .orderByAsc(BillManage::getPayDeadline, BillManage::getId));
        for (BillManage bill : bills) {
            if (remain.compareTo(BigDecimal.ZERO) <= 0) break;
            BigDecimal payable = bill.getPayableAmount() == null ? BigDecimal.ZERO : bill.getPayableAmount();
            if (payable.compareTo(BigDecimal.ZERO) <= 0) continue;
            if (remain.compareTo(payable) >= 0) {
                remain = remain.subtract(payable);
                BigDecimal oldDeduct = bill.getPrepayDeductAmount() == null ? BigDecimal.ZERO : bill.getPrepayDeductAmount();
                bill.setPrepayDeductAmount(oldDeduct.add(payable));
                bill.setPayableAmount(BigDecimal.ZERO);
                bill.setTradeStatus(2);
                billManageMapper.updateById(bill);
            } else {
                BigDecimal oldDeduct = bill.getPrepayDeductAmount() == null ? BigDecimal.ZERO : bill.getPrepayDeductAmount();
                bill.setPrepayDeductAmount(oldDeduct.add(remain));
                bill.setPayableAmount(payable.subtract(remain));
                billManageMapper.updateById(bill);
                remain = BigDecimal.ZERO;
            }
        }

        // 余额查询列表实时刷新最新金额
        BalanceQuery balance = balanceQueryMapper.selectOne(new LambdaQueryWrapper<BalanceQuery>()
                .eq(BalanceQuery::getElderName, rec.getElderName())
                .eq(BalanceQuery::getBedNo, rec.getBedNo())
                .eq(BalanceQuery::getIsDeleted, 0)
                .last("limit 1"));
        if (balance == null) {
            balance = new BalanceQuery();
            balance.setElderName(rec.getElderName());
            balance.setBedNo(rec.getBedNo());
            balance.setDepositBalance(BigDecimal.ZERO);
            balance.setAccountBalance(req.getRechargeAmount());
            balance.setChangedTime(LocalDateTime.now());
            balance.setIsDeleted(0);
            balanceQueryMapper.insert(balance);
        } else {
            BigDecimal oldAccount = balance.getAccountBalance() == null ? BigDecimal.ZERO : balance.getAccountBalance();
            balance.setAccountBalance(oldAccount.add(req.getRechargeAmount()));
            balance.setChangedTime(LocalDateTime.now());
            balanceQueryMapper.updateById(balance);
        }
    }

    private PrepayRechargeItemVO toVO(PrepayRecharge e) {
        PrepayRechargeItemVO vo = new PrepayRechargeItemVO();
        vo.setId(e.getId());
        vo.setRechargeNo(defaultStr(e.getRechargeNo()));
        vo.setElderName(defaultStr(e.getElderName()));
        vo.setElderIdNo(defaultStr(e.getElderIdNo()));
        vo.setBedNo(defaultStr(e.getBedNo()));
        vo.setRechargeMethod(defaultStr(e.getRechargeMethod()));
        vo.setRechargeAmount(e.getRechargeAmount());
        vo.setVoucherFileName(defaultStr(e.getVoucherFileName()));
        vo.setVoucherFileUrl(defaultStr(e.getVoucherFileUrl()));
        vo.setRechargeRemark(defaultStr(e.getRechargeRemark()));
        vo.setCreatedTime(e.getCreatedTime() == null ? "" : FMT.format(e.getCreatedTime()));
        return vo;
    }

    private String genRechargeNo() {
        return "YJ" + DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now());
    }

    private String defaultStr(String s) {
        return s == null ? "" : s;
    }
}
