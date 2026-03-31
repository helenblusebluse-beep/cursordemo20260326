package com.zhongzhou.modules.room.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.room.dto.RoomTypeSaveRequest;
import com.zhongzhou.modules.room.service.RoomTypeService;
import com.zhongzhou.modules.room.vo.RoomTypeOptionVO;
import com.zhongzhou.modules.room.vo.RoomTypeVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/room-types")
@Validated
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @GetMapping("/enabled-options")
    public ApiResponse<List<RoomTypeOptionVO>> enabledOptions() {
        return ApiResponse.success(roomTypeService.listEnabledOptions());
    }

    @GetMapping
    public ApiResponse<PageResult<RoomTypeVO>> page(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        return ApiResponse.success(roomTypeService.page(page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<RoomTypeVO> detail(@PathVariable @Min(1) Long id) {
        return ApiResponse.success(roomTypeService.detail(id));
    }

    @PostMapping
    public ApiResponse<Long> create(@RequestBody @Valid RoomTypeSaveRequest request) {
        return ApiResponse.success(roomTypeService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable @Min(1) Long id, @RequestBody @Valid RoomTypeSaveRequest request) {
        roomTypeService.update(id, request);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable @Min(1) Long id) {
        roomTypeService.delete(id);
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/toggle-status")
    public ApiResponse<Void> toggle(@PathVariable @Min(1) Long id, @RequestParam boolean enable) {
        roomTypeService.toggleStatus(id, enable);
        return ApiResponse.success(null);
    }
}
