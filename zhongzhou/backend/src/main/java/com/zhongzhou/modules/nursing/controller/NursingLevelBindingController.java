package com.zhongzhou.modules.nursing.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.modules.nursing.dto.NursingLevelBindRequest;
import com.zhongzhou.modules.nursing.service.NursingLevelBindingService;
import com.zhongzhou.modules.nursing.vo.NursingCareLevelVO;
import com.zhongzhou.modules.nursing.vo.NursingPlanSimpleVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/nursing-levels")
@Validated
public class NursingLevelBindingController {
    private final NursingLevelBindingService service;

    public NursingLevelBindingController(NursingLevelBindingService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<List<NursingCareLevelVO>> listLevels() {
        return ApiResponse.success(service.listLevels());
    }

    @GetMapping("/available-plans")
    public ApiResponse<List<NursingPlanSimpleVO>> availablePlans() {
        return ApiResponse.success(service.listAvailablePlans());
    }

    @PostMapping("/bindings")
    public ApiResponse<Void> rebind(@RequestParam String careLevel, @RequestBody @Valid NursingLevelBindRequest req) {
        service.rebindLevel(careLevel, req);
        return ApiResponse.success(null);
    }
}
