package com.zhongzhou.modules.visit.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.appointment.vo.PendingAppointmentVO;
import com.zhongzhou.modules.visit.dto.VisitCheckinRequest;
import com.zhongzhou.modules.visit.dto.VisitCreateRequest;
import com.zhongzhou.modules.visit.dto.VisitCheckoutRequest;
import com.zhongzhou.modules.visit.service.VisitService;
import com.zhongzhou.modules.visit.vo.VisitRecordVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/visits")
@Validated
public class VisitController {
    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping
    public ApiResponse<PageResult<VisitRecordVO>> page(
            @RequestParam(required = false) String visitorName,
            @RequestParam(required = false) String visitorPhone,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false) Integer reserveType,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        return ApiResponse.success(visitService.page(visitorName, visitorPhone, startTime, endTime, reserveType, page, pageSize));
    }

    @PostMapping
    public ApiResponse<Void> create(@RequestBody @Valid VisitCreateRequest request) {
        visitService.create(request);
        return ApiResponse.success(null);
    }

    @GetMapping("/pending-appointments")
    public ApiResponse<List<PendingAppointmentVO>> pendingAppointments() {
        return ApiResponse.success(visitService.pendingAppointments());
    }

    @PostMapping("/checkin")
    public ApiResponse<Void> checkin(@RequestBody @Valid VisitCheckinRequest request) {
        visitService.checkin(request);
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/checkout")
    public ApiResponse<Void> checkout(@PathVariable @Min(1) Long id, @RequestBody @Valid VisitCheckoutRequest request) {
        visitService.checkout(id, request);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable @Min(1) Long id) {
        visitService.delete(id);
        return ApiResponse.success(null);
    }
}
