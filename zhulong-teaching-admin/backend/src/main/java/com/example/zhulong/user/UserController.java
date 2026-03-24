package com.example.zhulong.user;

import com.example.zhulong.common.ApiResponse;
import com.example.zhulong.common.PageResult;
import com.example.zhulong.user.dto.UserCreateRequest;
import com.example.zhulong.user.dto.UserUpdateRequest;
import com.example.zhulong.user.vo.UserVO;
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

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ApiResponse<PageResult<UserVO>> page(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        return ApiResponse.success(userService.page(keyword, page, pageSize));
    }

    @PostMapping
    public ApiResponse<Void> create(@RequestBody @Valid UserCreateRequest req) {
        userService.create(req);
        return ApiResponse.success("新增成功", null);
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable @Min(1) Long id, @RequestBody @Valid UserUpdateRequest req) {
        userService.update(id, req);
        return ApiResponse.success("修改成功", null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable @Min(1) Long id) {
        userService.delete(id);
        return ApiResponse.success("删除成功", null);
    }
}
