package com.zhongzhou.modules.iot.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.iot.dto.IotAlarmRecordHandleRequest;
import com.zhongzhou.modules.iot.service.IotAlarmRecordService;
import com.zhongzhou.modules.iot.vo.IotAlarmRecordVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/iot/alarm-records")
@Validated
public class IotAlarmRecordController {
    private final IotAlarmRecordService service;

    public IotAlarmRecordController(IotAlarmRecordService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<PageResult<IotAlarmRecordVO>> page(
            @RequestParam(required = false) String deviceName,
            @RequestParam(required = false) String createdDate,
            @RequestParam(required = false) Integer handleStatus,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        return ApiResponse.success(service.page(deviceName, createdDate, handleStatus, page, pageSize));
    }

    @PostMapping("/{id}/handle")
    public ApiResponse<Void> handle(@PathVariable @Min(1) Long id, @RequestBody @Valid IotAlarmRecordHandleRequest req) {
        service.handle(id, req);
        return ApiResponse.success(null);
    }
}
