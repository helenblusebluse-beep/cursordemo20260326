package com.zhongzhou.modules.order.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.order.dto.OrderCancelRequest;
import com.zhongzhou.modules.order.dto.OrderRefundRequest;
import com.zhongzhou.modules.order.service.OrderManageService;
import com.zhongzhou.modules.order.vo.OrderDetailVO;
import com.zhongzhou.modules.order.vo.OrderManageVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/orders")
public class OrderManageController {
    private final OrderManageService orderManageService;

    public OrderManageController(OrderManageService orderManageService) {
        this.orderManageService = orderManageService;
    }

    @GetMapping
    public ApiResponse<PageResult<OrderManageVO>> page(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String elderName,
            @RequestParam(required = false) String orderUserName,
            @RequestParam(required = false) String beginTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(defaultValue = "0") Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return ApiResponse.success(orderManageService.page(orderNo, elderName, orderUserName, beginTime, endTime, status, page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderDetailVO> detail(@PathVariable Long id) {
        return ApiResponse.success(orderManageService.detail(id));
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancel(@PathVariable Long id, @Valid @RequestBody OrderCancelRequest req) {
        orderManageService.cancel(id, req);
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/refund")
    public ApiResponse<Void> refund(@PathVariable Long id, @Valid @RequestBody OrderRefundRequest req) {
        orderManageService.refund(id, req);
        return ApiResponse.success(null);
    }
}
