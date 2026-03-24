package com.example.zhulong.auth;

import com.example.zhulong.auth.dto.LoginRequest;
import com.example.zhulong.auth.vo.LoginVO;
import com.example.zhulong.user.User;
import com.example.zhulong.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;

    public LoginService(UserRepository userRepository, JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.jwtTokenService = jwtTokenService;
    }

    public LoginVO login(LoginRequest req) {
        String username = req.getUsername().trim();
        String password = req.getPassword();

        Optional<User> opt = userRepository.findByUsernameAndIsDeletedFalse(username);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        User u = opt.get();
        if (u.getStatus() == null || u.getStatus() != 1) {
            throw new IllegalArgumentException("账号已禁用");
        }
        String stored = u.getPassword();
        if (!StringUtils.hasText(stored) || !Objects.equals(stored, password)) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        String token = jwtTokenService.createToken(u.getId(), u.getUsername(), u.getDisplayName());
        return new LoginVO(u.getId(), u.getUsername(), u.getDisplayName(), token);
    }
}
