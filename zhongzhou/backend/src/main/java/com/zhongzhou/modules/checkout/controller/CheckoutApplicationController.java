package com.zhongzhou.modules.checkout.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.checkout.dto.CheckoutApplyRequest;
import com.zhongzhou.modules.checkout.dto.CheckoutVoucherRequest;
import com.zhongzhou.modules.checkout.service.CheckoutApplicationService;
import com.zhongzhou.modules.checkout.vo.CheckoutApplicationVO;
import com.zhongzhou.modules.checkout.vo.CheckoutCandidateVO;
import com.zhongzhou.modules.checkout.vo.CheckoutDetailVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/checkouts")
@Validated
public class CheckoutApplicationController {
    private final CheckoutApplicationService service;

    public CheckoutApplicationController(CheckoutApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<PageResult<CheckoutApplicationVO>> page(
            @RequestParam(required = false) String elderName,
            @RequestParam(required = false) String idCard,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        return ApiResponse.success(service.page(elderName, idCard, page, pageSize));
    }

    @PostMapping("/apply")
    public ApiResponse<Long> apply(@RequestBody @Valid CheckoutApplyRequest request) {
        return ApiResponse.success(service.apply(request));
    }

    @GetMapping("/candidates")
    public ApiResponse<List<CheckoutCandidateVO>> candidates() {
        return ApiResponse.success(service.candidates());
    }

    @GetMapping("/{id}")
    public ApiResponse<CheckoutDetailVO> detail(@PathVariable @Min(1) Long id) {
        return ApiResponse.success(service.detail(id));
    }

    @PostMapping("/{id}/voucher")
    public ApiResponse<Void> uploadVoucher(@PathVariable @Min(1) Long id, @RequestBody @Valid CheckoutVoucherRequest request) {
        service.uploadVoucher(id, request);
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/unpaid/{feeId}/cancel")
    public ApiResponse<Void> cancelUnpaid(@PathVariable @Min(1) Long id, @PathVariable @Min(1) Long feeId) {
        service.cancelUnpaid(id, feeId);
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/submit")
    public ApiResponse<Void> submit(@PathVariable @Min(1) Long id) {
        service.submit(id);
        return ApiResponse.success(null);
    }
}
