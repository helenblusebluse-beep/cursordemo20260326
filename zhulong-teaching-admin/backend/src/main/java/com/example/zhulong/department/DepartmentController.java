package com.example.zhulong.department;

import com.example.zhulong.common.ApiResponse;
import com.example.zhulong.common.PageResult;
import com.example.zhulong.department.dto.DepartmentCreateRequest;
import com.example.zhulong.department.dto.DepartmentUpdateRequest;
import com.example.zhulong.department.vo.DepartmentVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/departments")
@Validated
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ApiResponse<PageResult<DepartmentVO>> list(
            @RequestParam(required = false) String deptName,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        return ApiResponse.success(departmentService.page(deptName, page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<DepartmentVO> getById(@PathVariable @Min(1) Long id) {
        return ApiResponse.success(departmentService.getById(id));
    }

    @PostMapping
    public ApiResponse<Void> create(
            @RequestBody @Valid DepartmentCreateRequest request,
            @RequestAttribute(value = "userId", required = false) Number operatorUserId) {
        departmentService.create(request.getDeptName(), toOperatorEmpId(operatorUserId));
        return ApiResponse.success("新增成功", null);
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid DepartmentUpdateRequest request,
            @RequestAttribute(value = "userId", required = false) Number operatorUserId) {
        departmentService.update(id, request.getDeptName(), toOperatorEmpId(operatorUserId));
        return ApiResponse.success("修改成功", null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @PathVariable @Min(1) Long id,
            @RequestAttribute(value = "userId", required = false) Number operatorUserId) {
        departmentService.delete(id, toOperatorEmpId(operatorUserId));
        return ApiResponse.success("删除成功", null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Void> handleIllegalArgument(IllegalArgumentException ex) {
        return ApiResponse.fail(ex.getMessage());
    }

    private static Long toOperatorEmpId(Number userId) {
        return userId == null ? null : userId.longValue();
    }
}
