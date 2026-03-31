package com.zhongzhou.modules.balance.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.balance.entity.BalanceQuery;
import com.zhongzhou.modules.balance.mapper.BalanceQueryMapper;
import com.zhongzhou.modules.balance.vo.BalanceQueryItemVO;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BalanceQueryService {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final BalanceQueryMapper balanceQueryMapper;

    public BalanceQueryService(BalanceQueryMapper balanceQueryMapper) {
        this.balanceQueryMapper = balanceQueryMapper;
    }

    public PageResult<BalanceQueryItemVO> page(String elderName, String bedNo, int page, int pageSize) {
        String elderNameValue = elderName == null ? null : elderName.trim();
        String bedNoValue = bedNo == null ? null : bedNo.trim();
        LambdaQueryWrapper<BalanceQuery> qw = new LambdaQueryWrapper<BalanceQuery>()
                .eq(BalanceQuery::getIsDeleted, 0)
                .eq(elderNameValue != null && !elderNameValue.isBlank(), BalanceQuery::getElderName, elderNameValue)
                .eq(bedNoValue != null && !bedNoValue.isBlank(), BalanceQuery::getBedNo, bedNoValue)
                .orderByDesc(BalanceQuery::getChangedTime, BalanceQuery::getId);
        Page<BalanceQuery> p = balanceQueryMapper.selectPage(new Page<>(page, pageSize), qw);
        List<BalanceQueryItemVO> rows = p.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return new PageResult<>(p.getTotal(), rows);
    }

    private BalanceQueryItemVO toVO(BalanceQuery e) {
        BalanceQueryItemVO vo = new BalanceQueryItemVO();
        vo.setId(e.getId());
        vo.setElderName(e.getElderName());
        vo.setBedNo(e.getBedNo());
        vo.setAccountBalance(e.getAccountBalance());
        vo.setDepositBalance(e.getDepositBalance());
        vo.setChangedTime(e.getChangedTime() == null ? "" : FMT.format(e.getChangedTime()));
        return vo;
    }
}
