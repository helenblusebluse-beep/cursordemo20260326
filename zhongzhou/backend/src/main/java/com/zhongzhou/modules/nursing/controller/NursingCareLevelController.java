package com.zhongzhou.modules.nursing.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.nursing.dto.NursingCareLevelSaveRequest;
import com.zhongzhou.modules.nursing.service.NursingCareLevelService;
import com.zhongzhou.modules.nursing.vo.NursingCareLevelItemVO;
import com.zhongzhou.modules.nursing.vo.NursingCareLevelOptionVO;
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
@RequestMapping("/api/nursing-care-levels")
@Validated
public class NursingCareLevelController {
    private final NursingCareLevelService service;

    public NursingCareLevelController(NursingCareLevelService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<PageResult<NursingCareLevelItemVO>> page(
            @RequestParam(required = false) String levelName,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        return ApiResponse.success(service.page(levelName, status, page, pageSize));
    }

    @GetMapping("/options")
    public ApiResponse<List<NursingCareLevelOptionVO>> options() {
        return ApiResponse.success(service.listEnabledOptions());
    }

    @PostMapping
    public ApiResponse<Long> create(@RequestBody @Valid NursingCareLevelSaveRequest req) {
        return ApiResponse.success(service.create(req));
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable @Min(1) Long id, @RequestBody @Valid NursingCareLevelSaveRequest req) {
        service.update(id, req);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable @Min(1) Long id) {
        service.delete(id);
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/status")
    public ApiResponse<Void> changeStatus(@PathVariable @Min(1) Long id, @RequestParam Integer status) {
        service.changeStatus(id, status);
        return ApiResponse.success(null);
    }
}
