package com.zhongzhou.modules.appointment.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.appointment.dto.AppointmentConfirmRequest;
import com.zhongzhou.modules.appointment.dto.AppointmentCreateRequest;
import com.zhongzhou.modules.appointment.service.AppointmentService;
import com.zhongzhou.modules.appointment.vo.AppointmentVO;
import com.zhongzhou.modules.appointment.vo.PendingAppointmentVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@Validated
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ApiResponse<PageResult<AppointmentVO>> page(
            @RequestParam(required = false) String visitorName,
            @RequestParam(required = false) String visitorPhone,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer reserveType,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        return ApiResponse.success(appointmentService.page(visitorName, visitorPhone, status, reserveType, page, pageSize));
    }

    @GetMapping("/pending")
    public ApiResponse<List<PendingAppointmentVO>> pending() {
        return ApiResponse.success(appointmentService.pendingList());
    }

    @PostMapping
    public ApiResponse<Void> create(@RequestBody @Valid AppointmentCreateRequest request) {
        appointmentService.create(request);
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/confirm")
    public ApiResponse<Void> confirm(@PathVariable @Min(1) Long id, @RequestBody @Valid AppointmentConfirmRequest request) {
        appointmentService.confirm(id, request);
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancel(@PathVariable @Min(1) Long id) {
        appointmentService.cancel(id);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable @Min(1) Long id) {
        appointmentService.delete(id);
        return ApiResponse.success(null);
    }
}
