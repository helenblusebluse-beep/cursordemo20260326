package com.zhongzhou.modules.arrears.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.arrears.service.ArrearsElderService;
import com.zhongzhou.modules.arrears.vo.ArrearsBillRowVO;
import com.zhongzhou.modules.arrears.vo.ArrearsElderItemVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/arrears-elders")
public class ArrearsElderController {
    private final ArrearsElderService arrearsElderService;

    public ArrearsElderController(ArrearsElderService arrearsElderService) {
        this.arrearsElderService = arrearsElderService;
    }

    @GetMapping
    public ApiResponse<PageResult<ArrearsElderItemVO>> page(
            @RequestParam(required = false) String elderName,
            @RequestParam(required = false) String bedNo,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return ApiResponse.success(arrearsElderService.page(elderName, bedNo, page, pageSize));
    }

    @GetMapping("/bills")
    public ApiResponse<List<ArrearsBillRowVO>> bills(
            @RequestParam String elderName,
            @RequestParam String bedNo
    ) {
        return ApiResponse.success(arrearsElderService.listOverdueBills(elderName, bedNo));
    }
}
