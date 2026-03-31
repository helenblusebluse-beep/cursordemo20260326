package com.zhongzhou.modules.iot.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.iot.dto.IotDeviceCreateRequest;
import com.zhongzhou.modules.iot.dto.IotDeviceUpdateRequest;
import com.zhongzhou.modules.iot.service.IotDeviceService;
import com.zhongzhou.modules.iot.service.IotDeviceTslService;
import com.zhongzhou.modules.iot.vo.IotDeviceDetailVO;
import com.zhongzhou.modules.iot.vo.IotDeviceOptionVO;
import com.zhongzhou.modules.iot.vo.IotDeviceVO;
import com.zhongzhou.modules.iot.vo.IotTslHistoryRowVO;
import com.zhongzhou.modules.iot.vo.IotTslModuleVO;
import com.zhongzhou.modules.iot.vo.IotTslRuntimeRowVO;
import com.zhongzhou.modules.iot.vo.IotTslServiceInvocationRowVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/iot/devices")
@Validated
public class IotDeviceController {
    private final IotDeviceService service;
    private final IotDeviceTslService tslService;

    public IotDeviceController(IotDeviceService service, IotDeviceTslService tslService) {
        this.service = service;
        this.tslService = tslService;
    }

    @GetMapping
    public ApiResponse<PageResult<IotDeviceVO>> page(
            @RequestParam(required = false) String deviceName,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) String deviceType,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        return ApiResponse.success(service.page(deviceName, productId, deviceType, page, pageSize));
    }

    /** 须放在 /{id} 之前，避免被误匹配 */
    @GetMapping("/options")
    public ApiResponse<List<IotDeviceOptionVO>> options() {
        return ApiResponse.success(service.listOptions());
    }

    @GetMapping("/{deviceId}/tsl/modules")
    public ApiResponse<List<IotTslModuleVO>> tslModules(@PathVariable @Min(1) Long deviceId) {
        return ApiResponse.success(tslService.listModules(deviceId));
    }

    @GetMapping("/{deviceId}/tsl/running-status")
    public ApiResponse<PageResult<IotTslRuntimeRowVO>> tslRunningStatus(
            @PathVariable @Min(1) Long deviceId,
            @RequestParam @Min(1) Long moduleId,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        return ApiResponse.success(tslService.pageRunningStatus(deviceId, moduleId, page, pageSize));
    }

    @GetMapping("/{deviceId}/tsl/property-history")
    public ApiResponse<PageResult<IotTslHistoryRowVO>> tslPropertyHistory(
            @PathVariable @Min(1) Long deviceId,
            @RequestParam @Min(1) Long moduleId,
            @RequestParam String identifier,
            @RequestParam(defaultValue = "1h") String rangeType,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "5") @Min(1) int pageSize) {
        return ApiResponse.success(tslService.pagePropertyHistory(deviceId, moduleId, identifier, rangeType, start, end, page, pageSize));
    }

    @GetMapping("/{deviceId}/tsl/service-invocations")
    public ApiResponse<PageResult<IotTslServiceInvocationRowVO>> tslServiceInvocations(
            @PathVariable @Min(1) Long deviceId,
            @RequestParam @Min(1) Long moduleId,
            @RequestParam(defaultValue = "1h") String rangeType,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(defaultValue = "0") @Min(0) int offset,
            @RequestParam(defaultValue = "10") @Min(1) int limit) {
        return ApiResponse.success(tslService.pageServiceInvocations(deviceId, moduleId, rangeType, start, end, offset, limit));
    }

    @GetMapping("/{id}")
    public ApiResponse<IotDeviceDetailVO> detail(@PathVariable @Min(1) Long id) {
        return ApiResponse.success(service.detail(id));
    }

    @PostMapping
    public ApiResponse<Long> create(@RequestBody @Valid IotDeviceCreateRequest req) {
        return ApiResponse.success(service.create(req));
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable @Min(1) Long id, @RequestBody @Valid IotDeviceUpdateRequest req) {
        service.update(id, req);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable @Min(1) Long id) {
        service.remove(id);
        return ApiResponse.success(null);
    }
}
