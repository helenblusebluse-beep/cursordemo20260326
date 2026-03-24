package com.example.zhulong.auth;

import com.example.zhulong.auth.dto.LoginRequest;
import com.example.zhulong.auth.vo.LoginVO;
import com.example.zhulong.common.ApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginVO> login(@RequestBody @Valid LoginRequest req) {
        LoginVO vo = loginService.login(req);
        return ApiResponse.success(vo);
    }
}
