package com.zhongzhou.modules.prepay.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.prepay.dto.PrepayRechargeCreateRequest;
import com.zhongzhou.modules.prepay.service.PrepayRechargeService;
import com.zhongzhou.modules.prepay.vo.PrepayElderOptionVO;
import com.zhongzhou.modules.prepay.vo.PrepayRechargeItemVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/prepay-recharges")
public class PrepayRechargeController {
    private final PrepayRechargeService prepayRechargeService;

    public PrepayRechargeController(PrepayRechargeService prepayRechargeService) {
        this.prepayRechargeService = prepayRechargeService;
    }

    @GetMapping
    public ApiResponse<PageResult<PrepayRechargeItemVO>> page(
            @RequestParam(required = false) String elderName,
            @RequestParam(required = false) String bedNo,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return ApiResponse.success(prepayRechargeService.page(elderName, bedNo, page, pageSize));
    }

    @GetMapping("/elder-options")
    public ApiResponse<List<PrepayElderOptionVO>> elderOptions() {
        return ApiResponse.success(prepayRechargeService.elderOptions());
    }

    @PostMapping
    public ApiResponse<Void> create(@Valid @RequestBody PrepayRechargeCreateRequest req) {
        prepayRechargeService.create(req);
        return ApiResponse.success(null);
    }
}
