package com.example.aichat.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/admin/users")
@Validated
public class AdminUserController {

    public static class CreateUserRequest {
        @Email
        @NotBlank
        public String email;
        @NotBlank
        public String name;
        @NotNull
        public UserRole role;
    }

    public static class UpdateUserRequest {
        @NotBlank
        public String name;
        @NotNull
        public UserRole role;
        @NotNull
        public UserStatus status;
    }

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<User>> list(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(userService.listUsers(PageRequest.of(page, size)));
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody @Validated CreateUserRequest req) {
        User user = userService.createUser(req.email, req.name, req.role);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id,
                                       @RequestBody @Validated UpdateUserRequest req) {
        User user = userService.updateUser(id, req.name, req.role, req.status);
        return ResponseEntity.ok(user);
    }
}

