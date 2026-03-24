package com.example.zhulong.user;

import com.example.zhulong.common.PageResult;
import com.example.zhulong.user.dto.UserCreateRequest;
import com.example.zhulong.user.dto.UserUpdateRequest;
import com.example.zhulong.user.vo.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PageResult<UserVO> page(String keyword, int page, int pageSize) {
        Specification<User> spec = (root, query, cb) -> {
            List<Predicate> ps = new ArrayList<>();
            ps.add(cb.isFalse(root.get("isDeleted")));
            if (StringUtils.hasText(keyword)) {
                String k = "%" + keyword.trim() + "%";
                ps.add(cb.or(cb.like(root.get("username"), k), cb.like(root.get("displayName"), k)));
            }
            return cb.and(ps.toArray(new Predicate[0]));
        };
        Page<User> result = userRepository.findAll(spec, PageRequest.of(Math.max(page - 1, 0), pageSize));
        List<UserVO> rows = result.getContent().stream()
                .map(u -> new UserVO(u.getId(), u.getUsername(), u.getDisplayName(), u.getRoleName(), u.getStatus()))
                .collect(Collectors.toList());
        return new PageResult<>(result.getTotalElements(), rows);
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(UserCreateRequest req) {
        String username = req.getUsername().trim();
        if (userRepository.existsByUsernameAndIsDeletedFalse(username)) {
            throw new IllegalArgumentException("用户名已存在");
        }
        User u = new User();
        u.setUsername(username);
        u.setPassword(req.getPassword().trim());
        u.setDisplayName(req.getDisplayName().trim());
        u.setRoleName(req.getRoleName().trim());
        u.setStatus(req.getStatus());
        u.setIsDeleted(false);
        u.setLastOperateTime(LocalDateTime.now());
        userRepository.save(u);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, UserUpdateRequest req) {
        User u = getExist(id);
        String username = req.getUsername().trim();
        if (userRepository.existsByUsernameAndIsDeletedFalseAndIdNot(username, id)) {
            throw new IllegalArgumentException("用户名已存在");
        }
        u.setUsername(username);
        if (StringUtils.hasText(req.getPassword())) {
            u.setPassword(req.getPassword().trim());
        }
        u.setDisplayName(req.getDisplayName().trim());
        u.setRoleName(req.getRoleName().trim());
        u.setStatus(req.getStatus());
        u.setLastOperateTime(LocalDateTime.now());
        userRepository.save(u);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        User u = getExist(id);
        u.setIsDeleted(true);
        u.setLastOperateTime(LocalDateTime.now());
        userRepository.save(u);
    }

    private User getExist(Long id) {
        return userRepository.findById(id)
                .filter(u -> !Boolean.TRUE.equals(u.getIsDeleted()))
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }
}
