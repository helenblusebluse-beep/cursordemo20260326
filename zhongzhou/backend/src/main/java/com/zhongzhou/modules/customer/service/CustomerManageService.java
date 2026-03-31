package com.zhongzhou.modules.customer.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.checkin.entity.CheckinApplication;
import com.zhongzhou.modules.checkin.mapper.CheckinApplicationMapper;
import com.zhongzhou.modules.customer.entity.CustomerProfile;
import com.zhongzhou.modules.customer.mapper.CustomerProfileMapper;
import com.zhongzhou.modules.customer.vo.CustomerManageItemVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class CustomerManageService {
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    private final CustomerProfileMapper mapper;
    private final CheckinApplicationMapper checkinMapper;

    public CustomerManageService(CustomerProfileMapper mapper, CheckinApplicationMapper checkinMapper) {
        this.mapper = mapper;
        this.checkinMapper = checkinMapper;
    }

    public PageResult<CustomerManageItemVO> page(String customerNickname, String customerPhone, String signed, int page, int pageSize) {
        List<CustomerProfile> list = mapper.selectList(new LambdaQueryWrapper<CustomerProfile>()
                .eq(CustomerProfile::getIsDeleted, 0)
                .orderByDesc(CustomerProfile::getFirstLoginTime)
                .orderByDesc(CustomerProfile::getId));

        List<CustomerManageItemVO> allRows = list.stream().map(this::toItem).collect(Collectors.toList());
        if (StringUtils.hasText(customerNickname)) {
            String kw = customerNickname.trim();
            allRows = allRows.stream().filter(x -> x.getCustomerNickname() != null && x.getCustomerNickname().contains(kw)).collect(Collectors.toList());
        }
        if (StringUtils.hasText(customerPhone)) {
            String phone = customerPhone.trim();
            allRows = allRows.stream().filter(x -> phone.equals(x.getCustomerPhone())).collect(Collectors.toList());
        }
        if (StringUtils.hasText(signed)) {
            String s = signed.trim();
            allRows = allRows.stream().filter(x -> s.equals(x.getSigned())).collect(Collectors.toList());
        }
        int from = Math.max(0, (page - 1) * pageSize);
        int to = Math.min(allRows.size(), from + pageSize);
        List<CustomerManageItemVO> rows = from >= allRows.size() ? new ArrayList<>() : allRows.subList(from, to);
        return new PageResult<>(allRows.size(), rows);
    }

    private CustomerManageItemVO toItem(CustomerProfile e) {
        CustomerManageItemVO vo = new CustomerManageItemVO();
        vo.setId(e.getId());
        vo.setCustomerNickname(e.getCustomerNickname());
        vo.setCustomerPhone(e.getCustomerPhone());
        vo.setOrderTrackCount(e.getOrderTrackCount() == null ? 0 : e.getOrderTrackCount());
        vo.setFirstLoginTime(e.getFirstLoginTime() == null ? "-" : e.getFirstLoginTime().format(DT));

        List<CheckinApplication> binds = checkinMapper.selectList(new LambdaQueryWrapper<CheckinApplication>()
                .eq(CheckinApplication::getIsDeleted, 0)
                .eq(CheckinApplication::getPayerContact, e.getCustomerPhone())
                .orderByDesc(CheckinApplication::getCreatedTime));

        if (binds.isEmpty()) {
            vo.setSigned("否");
            vo.setBindElderNames("—");
        } else {
            vo.setSigned("是");
            String elderNames = binds.stream().map(CheckinApplication::getElderName).distinct().collect(Collectors.joining("、"));
            vo.setBindElderNames(elderNames);
        }
        return vo;
    }
}
