package com.zhongzhou.modules.smartbed.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.modules.smartbed.service.SmartBedService;
import com.zhongzhou.modules.smartbed.vo.SmartBedFloorTabVO;
import com.zhongzhou.modules.smartbed.vo.SmartBedFloorViewVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/smart-beds")
@Validated
public class SmartBedController {
    private final SmartBedService smartBedService;

    public SmartBedController(SmartBedService smartBedService) {
        this.smartBedService = smartBedService;
    }

    @GetMapping("/floors")
    public ApiResponse<List<SmartBedFloorTabVO>> listFloors() {
        return ApiResponse.success(smartBedService.listFloors());
    }

    @GetMapping("/floors/{floorId}/view")
    public ApiResponse<SmartBedFloorViewVO> floorView(@PathVariable @Min(1) Long floorId) {
        return ApiResponse.success(smartBedService.getFloorView(floorId));
    }
}
