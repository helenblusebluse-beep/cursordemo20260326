package com.zhongzhou.modules.taskschedule.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.taskschedule.dto.TaskCancelRequest;
import com.zhongzhou.modules.taskschedule.dto.TaskExecuteRequest;
import com.zhongzhou.modules.taskschedule.dto.TaskRescheduleRequest;
import com.zhongzhou.modules.taskschedule.service.TaskScheduleService;
import com.zhongzhou.modules.taskschedule.vo.TaskScheduleDetailVO;
import com.zhongzhou.modules.taskschedule.vo.TaskScheduleItemVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/task-schedules")
public class TaskScheduleController {
    private final TaskScheduleService service;

    public TaskScheduleController(TaskScheduleService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<PageResult<TaskScheduleItemVO>> page(
            @RequestParam(required = false) String elderName,
            @RequestParam(required = false) String caregiverName,
            @RequestParam(required = false) String nursingItemName,
            @RequestParam(required = false) String beginTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(defaultValue = "1") Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return ApiResponse.success(service.page(elderName, caregiverName, nursingItemName, beginTime, endTime, status, page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<TaskScheduleDetailVO> detail(@PathVariable Long id) {
        return ApiResponse.success(service.detail(id));
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancel(@PathVariable Long id, @Valid @RequestBody TaskCancelRequest req) {
        service.cancel(id, req);
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/execute")
    public ApiResponse<Void> execute(@PathVariable Long id, @Valid @RequestBody TaskExecuteRequest req) {
        service.execute(id, req);
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/reschedule")
    public ApiResponse<Void> reschedule(@PathVariable Long id, @Valid @RequestBody TaskRescheduleRequest req) {
        service.reschedule(id, req);
        return ApiResponse.success(null);
    }
}
