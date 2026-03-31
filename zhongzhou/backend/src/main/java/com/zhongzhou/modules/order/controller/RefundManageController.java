package com.zhongzhou.modules.order.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.order.service.RefundManageService;
import com.zhongzhou.modules.order.vo.RefundDetailVO;
import com.zhongzhou.modules.order.vo.RefundManageVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/refunds")
public class RefundManageController {
    private final RefundManageService refundManageService;

    public RefundManageController(RefundManageService refundManageService) {
        this.refundManageService = refundManageService;
    }

    @GetMapping
    public ApiResponse<PageResult<RefundManageVO>> page(
            @RequestParam(required = false) String refundNo,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String applicant,
            @RequestParam(required = false) String beginTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(defaultValue = "0") Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return ApiResponse.success(refundManageService.page(refundNo, orderNo, applicant, beginTime, endTime, status, page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<RefundDetailVO> detail(@PathVariable Long id) {
        return ApiResponse.success(refundManageService.detail(id));
    }
}
