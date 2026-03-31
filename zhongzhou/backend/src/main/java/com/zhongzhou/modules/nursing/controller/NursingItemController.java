package com.zhongzhou.modules.nursing.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.nursing.dto.NursingItemSaveRequest;
import com.zhongzhou.modules.nursing.service.NursingItemService;
import com.zhongzhou.modules.nursing.vo.NursingItemOptionVO;
import com.zhongzhou.modules.nursing.vo.NursingItemVO;
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
@RequestMapping("/api/nursing-items")
@Validated
public class NursingItemController {
    private final NursingItemService service;

    public NursingItemController(NursingItemService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<PageResult<NursingItemVO>> page(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        return ApiResponse.success(service.page(name, status, page, pageSize));
    }

    @PostMapping
    public ApiResponse<Long> create(@RequestBody @Valid NursingItemSaveRequest req) {
        return ApiResponse.success(service.create(req));
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable @Min(1) Long id, @RequestBody @Valid NursingItemSaveRequest req) {
        service.update(id, req);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable @Min(1) Long id) {
        service.remove(id);
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/status")
    public ApiResponse<Void> changeStatus(@PathVariable @Min(1) Long id, @RequestParam Integer status) {
        service.changeStatus(id, status);
        return ApiResponse.success(null);
    }

    @GetMapping("/options")
    public ApiResponse<List<NursingItemOptionVO>> options() {
        return ApiResponse.success(service.listEnabledOptions());
    }
}
