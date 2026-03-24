package com.example.zhulong.employee;

import com.example.zhulong.common.ApiResponse;
import com.example.zhulong.common.PageResult;
import com.example.zhulong.employee.dto.EmployeeCreateRequest;
import com.example.zhulong.employee.dto.EmployeeUpdateRequest;
import com.example.zhulong.employee.vo.AvatarUploadVO;
import com.example.zhulong.employee.vo.EmployeeVO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Validated
public class EmployeeController {
    private final EmployeeService employeeService;
    private final AvatarSftpUploadService avatarSftpUploadService;

    public EmployeeController(EmployeeService employeeService, AvatarSftpUploadService avatarSftpUploadService) {
        this.employeeService = employeeService;
        this.avatarSftpUploadService = avatarSftpUploadService;
    }

    /**
     * 须放在 /{id} 之前；上传 multipart，勿加 consumes 限制（带 boundary 的 Content-Type 可能匹配失败导致落到 GET /{id} 而 405）。
     */
    @PostMapping("/avatar")
    public ApiResponse<AvatarUploadVO> uploadAvatar(@RequestParam("file") MultipartFile file) {
        String url = avatarSftpUploadService.upload(file);
        return ApiResponse.success(new AvatarUploadVO(url));
    }

    @GetMapping
    public ApiResponse<PageResult<EmployeeVO>> page(
            @RequestParam(required = false) String empName,
            @RequestParam(required = false) Integer gender,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize
    ) {
        return ApiResponse.success(employeeService.page(empName, gender, startDate, endDate, page, pageSize));
    }

    /** 仅匹配数字 id，避免与 /avatar 等字面路径冲突导致 POST 被误判为 GET /{id} 而 405 */
    @GetMapping("/{id:[0-9]+}")
    public ApiResponse<EmployeeVO> getById(@PathVariable @Min(1) Long id) {
        return ApiResponse.success(employeeService.getById(id));
    }

    @PostMapping
    public ApiResponse<Void> create(
            @RequestBody @Valid EmployeeCreateRequest req,
            @RequestAttribute(value = "userId", required = false) Number operatorUserId) {
        employeeService.create(req, toOperatorEmpId(operatorUserId));
        return ApiResponse.success("新增成功", null);
    }

    @PutMapping("/{id:[0-9]+}")
    public ApiResponse<Void> update(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid EmployeeUpdateRequest req,
            @RequestAttribute(value = "userId", required = false) Number operatorUserId) {
        employeeService.update(id, req, toOperatorEmpId(operatorUserId));
        return ApiResponse.success("修改成功", null);
    }

    @DeleteMapping("/{id:[0-9]+}")
    public ApiResponse<Void> delete(
            @PathVariable @Min(1) Long id,
            @RequestAttribute(value = "userId", required = false) Number operatorUserId) {
        employeeService.delete(id, toOperatorEmpId(operatorUserId));
        return ApiResponse.success("删除成功", null);
    }

    @DeleteMapping
    public ApiResponse<Void> deleteBatch(
            @RequestParam List<Long> ids,
            @RequestAttribute(value = "userId", required = false) Number operatorUserId) {
        employeeService.deleteBatch(ids, toOperatorEmpId(operatorUserId));
        return ApiResponse.success("删除成功", null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Void> handle(IllegalArgumentException ex) {
        return ApiResponse.fail(ex.getMessage());
    }

    private static Long toOperatorEmpId(Number userId) {
        return userId == null ? null : userId.longValue();
    }
}

