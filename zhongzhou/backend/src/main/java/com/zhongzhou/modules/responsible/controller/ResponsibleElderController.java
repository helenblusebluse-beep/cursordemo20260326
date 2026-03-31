package com.zhongzhou.modules.responsible.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.modules.responsible.dto.CaregiverAssignRequest;
import com.zhongzhou.modules.responsible.service.ResponsibleElderService;
import com.zhongzhou.modules.responsible.vo.ResponsibleFloorTabVO;
import com.zhongzhou.modules.responsible.vo.ResponsibleFloorViewVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/responsible-elders")
@Validated
public class ResponsibleElderController {
    private final ResponsibleElderService service;

    public ResponsibleElderController(ResponsibleElderService service) {
        this.service = service;
    }

    @GetMapping("/floors")
    public ApiResponse<List<ResponsibleFloorTabVO>> floors() {
        return ApiResponse.success(service.listFloors());
    }

    @GetMapping("/floors/{floorId}/view")
    public ApiResponse<ResponsibleFloorViewVO> floorView(@PathVariable @Min(1) Long floorId) {
        return ApiResponse.success(service.getFloorView(floorId));
    }

    @GetMapping("/caregivers/options")
    public ApiResponse<List<String>> caregiverOptions() {
        return ApiResponse.success(service.listCaregiverOptions());
    }

    @GetMapping("/beds/{bedId}/caregivers")
    public ApiResponse<List<String>> bedCaregivers(@PathVariable @Min(1) Long bedId) {
        return ApiResponse.success(service.getBedCaregivers(bedId));
    }

    @GetMapping("/rooms/{roomId}/caregivers")
    public ApiResponse<List<String>> roomCaregivers(@PathVariable @Min(1) Long roomId) {
        return ApiResponse.success(service.getRoomCaregivers(roomId));
    }

    @PostMapping("/beds/{bedId}/caregivers")
    public ApiResponse<Void> setBedCaregivers(@PathVariable @Min(1) Long bedId, @RequestBody @Valid CaregiverAssignRequest req) {
        service.assignBed(bedId, req);
        return ApiResponse.success(null);
    }

    @PostMapping("/rooms/{roomId}/caregivers")
    public ApiResponse<Void> setRoomCaregivers(@PathVariable @Min(1) Long roomId, @RequestBody @Valid CaregiverAssignRequest req) {
        service.assignRoom(roomId, req);
        return ApiResponse.success(null);
    }
}
