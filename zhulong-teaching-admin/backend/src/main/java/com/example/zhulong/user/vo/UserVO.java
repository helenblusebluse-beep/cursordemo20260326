package com.example.zhulong.user.vo;

public class UserVO {
    private Long id;
    private String username;
    private String displayName;
    private String roleName;
    private Integer status;

    public UserVO(Long id, String username, String displayName, String roleName, Integer status) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.roleName = roleName;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRoleName() {
        return roleName;
    }

    public Integer getStatus() {
        return status;
    }
}
