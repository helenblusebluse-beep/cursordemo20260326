package com.zhongzhou.modules.dashboard.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.modules.dashboard.service.DashboardService;
import com.zhongzhou.modules.dashboard.vo.DashboardVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public ApiResponse<DashboardVO> dashboard() {
        return ApiResponse.success(dashboardService.getHomeDashboard());
    }
}
