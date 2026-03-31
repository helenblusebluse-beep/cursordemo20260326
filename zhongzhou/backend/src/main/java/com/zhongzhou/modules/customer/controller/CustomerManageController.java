package com.zhongzhou.modules.customer.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.customer.service.CustomerManageService;
import com.zhongzhou.modules.customer.vo.CustomerManageItemVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/customers")
@Validated
public class CustomerManageController {
    private final CustomerManageService service;

    public CustomerManageController(CustomerManageService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<PageResult<CustomerManageItemVO>> page(
            @RequestParam(required = false) String customerNickname,
            @RequestParam(required = false) String customerPhone,
            @RequestParam(required = false) String signed,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        return ApiResponse.success(service.page(customerNickname, customerPhone, signed, page, pageSize));
    }
}
