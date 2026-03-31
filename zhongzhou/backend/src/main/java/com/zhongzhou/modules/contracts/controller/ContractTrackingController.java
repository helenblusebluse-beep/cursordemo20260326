package com.zhongzhou.modules.contracts.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.contracts.service.ContractTrackingService;
import com.zhongzhou.modules.contracts.vo.ContractTrackingDetailVO;
import com.zhongzhou.modules.contracts.vo.ContractTrackingItemVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/contracts")
@Validated
public class ContractTrackingController {
    private final ContractTrackingService service;

    public ContractTrackingController(ContractTrackingService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<PageResult<ContractTrackingItemVO>> page(
            @RequestParam(required = false) String contractNo,
            @RequestParam(required = false) String elderName,
            @RequestParam(required = false) String contractStatus,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        return ApiResponse.success(service.page(contractNo, elderName, contractStatus, page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<ContractTrackingDetailVO> detail(@PathVariable @Min(1) Long id) {
        return ApiResponse.success(service.detail(id));
    }
}
