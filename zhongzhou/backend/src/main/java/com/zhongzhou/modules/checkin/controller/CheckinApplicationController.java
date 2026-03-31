package com.zhongzhou.modules.checkin.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.checkin.dto.CheckinApplyRequest;
import com.zhongzhou.modules.checkin.dto.CheckinFamilyUpdateRequest;
import com.zhongzhou.modules.checkin.service.CheckinApplicationService;
import com.zhongzhou.modules.checkin.vo.CheckinApplicationDetailVO;
import com.zhongzhou.modules.checkin.vo.CheckinApplicationVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/checkins")
@Validated
public class CheckinApplicationController {
    private final CheckinApplicationService service;

    public CheckinApplicationController(CheckinApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<PageResult<CheckinApplicationVO>> page(
            @RequestParam(required = false) String elderName,
            @RequestParam(required = false) String idCard,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        return ApiResponse.success(service.page(elderName, idCard, page, pageSize));
    }

    @PostMapping("/apply")
    public ApiResponse<Long> apply(@RequestBody @Valid CheckinApplyRequest request) {
        return ApiResponse.success(service.apply(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<CheckinApplicationDetailVO> detail(@PathVariable @Min(1) Long id) {
        return ApiResponse.success(service.detail(id));
    }

    @PutMapping("/{id}/family")
    public ApiResponse<Void> updateFamily(@PathVariable @Min(1) Long id, @RequestBody CheckinFamilyUpdateRequest request) {
        service.updateFamily(id, request);
        return ApiResponse.success(null);
    }
}
