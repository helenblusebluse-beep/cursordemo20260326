package com.zhongzhou.modules.profile.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhongzhou.modules.profile.dto.ProfileBasicUpdateRequest;
import com.zhongzhou.modules.profile.dto.ProfilePasswordUpdateRequest;
import com.zhongzhou.modules.profile.entity.SysProfile;
import com.zhongzhou.modules.profile.mapper.SysProfileMapper;
import com.zhongzhou.modules.profile.vo.ProfileVO;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class ProfileService {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final SysProfileMapper mapper;

    public ProfileService(SysProfileMapper mapper) {
        this.mapper = mapper;
    }

    public ProfileVO me() {
        return toVO(getCurrent());
    }

    public ProfileVO updateBasic(ProfileBasicUpdateRequest req) {
        SysProfile p = getCurrent();
        p.setNickname(req.getNickname().trim());
        p.setPhone(req.getPhone().trim());
        p.setGender(req.getGender() == null ? 1 : req.getGender());
        p.setAvatarUrl(req.getAvatarUrl() == null ? p.getAvatarUrl() : req.getAvatarUrl().trim());
        mapper.updateById(p);
        return toVO(p);
    }

    public void updatePassword(ProfilePasswordUpdateRequest req) {
        SysProfile p = getCurrent();
        String oldPwd = req.getOldPassword().trim();
        String newPwd = req.getNewPassword().trim();
        String confirm = req.getConfirmPassword().trim();
        if (!oldPwd.equals(p.getPassword())) {
            throw new IllegalArgumentException("原密码不正确，请重新输入");
        }
        if (newPwd.equals(oldPwd)) {
            throw new IllegalArgumentException("新密码不能与原密码一致，请重新输入");
        }
        if (!newPwd.equals(confirm)) {
            throw new IllegalArgumentException("新密码与确认新密码不一致，请重新输入");
        }
        if (!newPwd.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,20}$")) {
            throw new IllegalArgumentException("密码长度显示8-20位，且必须包含数字、小写字母、大写字母");
        }
        p.setPassword(newPwd);
        mapper.updateById(p);
    }

    private SysProfile getCurrent() {
        SysProfile p = mapper.selectOne(new LambdaQueryWrapper<SysProfile>()
                .eq(SysProfile::getId, 1L)
                .eq(SysProfile::getIsDeleted, 0));
        if (p == null) {
            throw new IllegalArgumentException("个人信息不存在");
        }
        return p;
    }

    private ProfileVO toVO(SysProfile p) {
        ProfileVO vo = new ProfileVO();
        vo.setId(p.getId());
        vo.setNickname(p.getNickname());
        vo.setPhone(p.getPhone());
        vo.setEmail(p.getEmail());
        vo.setDepartment(p.getDepartment());
        vo.setPositionTitle(p.getPositionTitle());
        vo.setRoleName(p.getRoleName());
        vo.setGender(p.getGender());
        vo.setAvatarUrl(p.getAvatarUrl());
        vo.setCreatedTime(p.getCreatedTime() == null ? "" : FMT.format(p.getCreatedTime()));
        return vo;
    }
}

