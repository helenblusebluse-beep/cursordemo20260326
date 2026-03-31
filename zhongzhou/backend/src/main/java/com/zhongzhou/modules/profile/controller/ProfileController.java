package com.zhongzhou.modules.profile.controller;

import com.zhongzhou.common.ApiResponse;
import com.zhongzhou.modules.profile.dto.ProfileBasicUpdateRequest;
import com.zhongzhou.modules.profile.dto.ProfilePasswordUpdateRequest;
import com.zhongzhou.modules.profile.service.ProfileService;
import com.zhongzhou.modules.profile.vo.ProfileVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/profile")
@Validated
public class ProfileController {
    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public ApiResponse<ProfileVO> me() {
        return ApiResponse.success(service.me());
    }

    @PutMapping("/me")
    public ApiResponse<ProfileVO> updateBasic(@RequestBody @Valid ProfileBasicUpdateRequest req) {
        return ApiResponse.success(service.updateBasic(req));
    }

    @PutMapping("/password")
    public ApiResponse<Void> updatePassword(@RequestBody @Valid ProfilePasswordUpdateRequest req) {
        service.updatePassword(req);
        return ApiResponse.success(null);
    }
}

