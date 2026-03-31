package com.zhongzhou.modules.profile.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ProfileBasicUpdateRequest {
    @NotBlank
    @Size(max = 10)
    private String nickname;
    @NotBlank
    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式错误")
    private String phone;
    private Integer gender;
    private String avatarUrl;

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Integer getGender() { return gender; }
    public void setGender(Integer gender) { this.gender = gender; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}

