package com.zhongzhou.modules.bedpreview.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.modules.bedpreview.dto.BedSaveRequest;
import com.zhongzhou.modules.bedpreview.dto.FloorSaveRequest;
import com.zhongzhou.modules.bedpreview.dto.PreviewRoomSaveRequest;
import com.zhongzhou.modules.bedpreview.service.BedPreviewService;
import com.zhongzhou.modules.bedpreview.vo.FloorDetailVO;
import com.zhongzhou.modules.bedpreview.vo.FloorTabVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/bed-preview")
@Validated
public class BedPreviewController {
    private final BedPreviewService bedPreviewService;

    public BedPreviewController(BedPreviewService bedPreviewService) {
        this.bedPreviewService = bedPreviewService;
    }

    @GetMapping("/floors")
    public ApiResponse<List<FloorTabVO>> listFloors() {
        return ApiResponse.success(bedPreviewService.listFloors());
    }

    @GetMapping("/floors/{floorId}")
    public ApiResponse<FloorDetailVO> floorDetail(@PathVariable @Min(1) Long floorId) {
        return ApiResponse.success(bedPreviewService.getFloorDetail(floorId));
    }

    @PostMapping("/floors")
    public ApiResponse<Long> createFloor(@RequestBody @Valid FloorSaveRequest request) {
        return ApiResponse.success(bedPreviewService.createFloor(request));
    }

    @PutMapping("/floors/{id}")
    public ApiResponse<Void> updateFloor(@PathVariable @Min(1) Long id, @RequestBody @Valid FloorSaveRequest request) {
        bedPreviewService.updateFloor(id, request);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/floors/{id}")
    public ApiResponse<Void> deleteFloor(@PathVariable @Min(1) Long id) {
        bedPreviewService.deleteFloor(id);
        return ApiResponse.success(null);
    }

    @PostMapping("/floors/{floorId}/rooms")
    public ApiResponse<Long> createRoom(@PathVariable @Min(1) Long floorId, @RequestBody @Valid PreviewRoomSaveRequest request) {
        return ApiResponse.success(bedPreviewService.createRoom(floorId, request));
    }

    @PutMapping("/rooms/{roomId}")
    public ApiResponse<Void> updateRoom(@PathVariable @Min(1) Long roomId, @RequestBody @Valid PreviewRoomSaveRequest request) {
        bedPreviewService.updateRoom(roomId, request);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/rooms/{roomId}")
    public ApiResponse<Void> deleteRoom(@PathVariable @Min(1) Long roomId) {
        bedPreviewService.deleteRoom(roomId);
        return ApiResponse.success(null);
    }

    @PostMapping("/rooms/{roomId}/beds")
    public ApiResponse<Long> createBed(@PathVariable @Min(1) Long roomId, @RequestBody @Valid BedSaveRequest request) {
        return ApiResponse.success(bedPreviewService.createBed(roomId, request));
    }

    @PutMapping("/beds/{bedId}")
    public ApiResponse<Void> updateBed(@PathVariable @Min(1) Long bedId, @RequestBody @Valid BedSaveRequest request) {
        bedPreviewService.updateBed(bedId, request);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/beds/{bedId}")
    public ApiResponse<Void> deleteBed(@PathVariable @Min(1) Long bedId) {
        bedPreviewService.deleteBed(bedId);
        return ApiResponse.success(null);
    }
}
