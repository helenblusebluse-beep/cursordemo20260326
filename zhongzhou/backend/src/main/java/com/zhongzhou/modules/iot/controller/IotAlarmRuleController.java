package com.zhongzhou.modules.iot.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.iot.dto.IotAlarmRuleSaveRequest;
import com.zhongzhou.modules.iot.service.IotAlarmRuleService;
import com.zhongzhou.modules.iot.vo.IotAlarmRuleVO;
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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/iot/alarm-rules")
@Validated
public class IotAlarmRuleController {
    private final IotAlarmRuleService service;

    public IotAlarmRuleController(IotAlarmRuleService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<PageResult<IotAlarmRuleVO>> page(
            @RequestParam(required = false) String ruleName,
            @RequestParam(required = false) Long productId,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        return ApiResponse.success(service.page(ruleName, productId, page, pageSize));
    }

    @GetMapping("/options/modules")
    public ApiResponse<List<String>> moduleOptions(@RequestParam @Min(1) Long productId) {
        return ApiResponse.success(service.listModulesByProduct(productId));
    }

    @GetMapping("/options/functions")
    public ApiResponse<List<String>> functionOptions(
            @RequestParam @Min(1) Long productId,
            @RequestParam String moduleName) {
        return ApiResponse.success(service.listFunctions(productId, moduleName));
    }

    @GetMapping("/{id}")
    public ApiResponse<IotAlarmRuleVO> detail(@PathVariable @Min(1) Long id) {
        return ApiResponse.success(service.get(id));
    }

    @PostMapping
    public ApiResponse<Long> create(@RequestBody @Valid IotAlarmRuleSaveRequest req) {
        return ApiResponse.success(service.create(req));
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable @Min(1) Long id, @RequestBody @Valid IotAlarmRuleSaveRequest req) {
        service.update(id, req);
        return ApiResponse.success(null);
    }

    @PutMapping("/{id}/enabled")
    public ApiResponse<Void> setEnabled(
            @PathVariable @Min(1) Long id,
            @RequestParam @Min(0) @Max(1) int enabled) {
        service.setEnabled(id, enabled);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable @Min(1) Long id) {
        service.remove(id);
        return ApiResponse.success(null);
    }
}
