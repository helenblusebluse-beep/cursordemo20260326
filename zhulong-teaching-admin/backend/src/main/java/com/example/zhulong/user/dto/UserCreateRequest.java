package com.example.zhulong.user.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserCreateRequest {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度必须为2-20位")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 3, max = 64, message = "密码长度必须为3-64位")
    private String password;

    @NotBlank(message = "姓名不能为空")
    @Size(min = 2, max = 20, message = "姓名长度必须为2-20位")
    private String displayName;

    @NotBlank(message = "角色不能为空")
    @Size(min = 2, max = 20, message = "角色长度必须为2-20位")
    private String roleName;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态仅支持0或1")
    @Max(value = 1, message = "状态仅支持0或1")
    private Integer status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
