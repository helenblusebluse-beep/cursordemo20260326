package com.zhongzhou.modules.balance.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.balance.service.BalanceQueryService;
import com.zhongzhou.modules.balance.vo.BalanceQueryItemVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/balance-queries")
public class BalanceQueryController {
    private final BalanceQueryService balanceQueryService;

    public BalanceQueryController(BalanceQueryService balanceQueryService) {
        this.balanceQueryService = balanceQueryService;
    }

    @GetMapping
    public ApiResponse<PageResult<BalanceQueryItemVO>> page(
            @RequestParam(required = false) String elderName,
            @RequestParam(required = false) String bedNo,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return ApiResponse.success(balanceQueryService.page(elderName, bedNo, page, pageSize));
    }
}
