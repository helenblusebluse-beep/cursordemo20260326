package com.zhongzhou.modules.health.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.health.dto.HealthAssessmentQueryRequest;
import com.zhongzhou.modules.health.service.HealthAssessmentService;
import com.zhongzhou.modules.health.vo.HealthAssessmentDetailVO;
import com.zhongzhou.modules.health.vo.HealthAssessmentVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/health-assessments")
@Validated
public class HealthAssessmentController {
    private final HealthAssessmentService healthAssessmentService;

    public HealthAssessmentController(HealthAssessmentService healthAssessmentService) {
        this.healthAssessmentService = healthAssessmentService;
    }

    @GetMapping
    public ApiResponse<PageResult<HealthAssessmentVO>> page(
            @RequestParam(required = false) String elderName,
            @RequestParam(required = false) String idCard,
            @RequestParam(required = false) Integer livingStatus,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        HealthAssessmentQueryRequest query = new HealthAssessmentQueryRequest();
        query.setElderName(elderName);
        query.setIdCard(idCard);
        query.setLivingStatus(livingStatus);
        query.setPage(page);
        query.setPageSize(pageSize);
        return ApiResponse.success(healthAssessmentService.page(query));
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ApiResponse<Long> upload(
            @RequestParam String elderName,
            @RequestParam String idCard,
            @RequestParam String examOrg,
            @RequestPart("reportFile") MultipartFile reportFile) {
        return ApiResponse.success(healthAssessmentService.upload(elderName, idCard, examOrg, reportFile));
    }

    @GetMapping("/{id}")
    public ApiResponse<HealthAssessmentDetailVO> detail(@PathVariable @Min(1) Long id) {
        return ApiResponse.success(healthAssessmentService.detail(id));
    }
}
