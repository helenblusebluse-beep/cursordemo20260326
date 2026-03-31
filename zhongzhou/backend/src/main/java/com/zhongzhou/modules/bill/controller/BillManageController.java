package com.zhongzhou.modules.bill.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.modules.bill.service.BillManageService;
import com.zhongzhou.modules.bill.vo.BillDetailVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bills")
public class BillManageController {
    private final BillManageService billManageService;

    public BillManageController(BillManageService billManageService) {
        this.billManageService = billManageService;
    }

    @GetMapping("/{id}")
    public ApiResponse<BillDetailVO> detail(@PathVariable Long id) {
        return ApiResponse.success(billManageService.detail(id));
    }
}
