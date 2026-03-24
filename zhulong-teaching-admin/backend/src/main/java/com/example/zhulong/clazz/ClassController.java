package com.example.zhulong.clazz;

import com.example.zhulong.clazz.dto.ClassCreateRequest;
import com.example.zhulong.clazz.dto.ClassUpdateRequest;
import com.example.zhulong.clazz.vo.ClassVO;
import com.example.zhulong.common.ApiResponse;
import com.example.zhulong.common.PageResult;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/classes")
@Validated
public class ClassController {
    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping
    public ApiResponse<PageResult<ClassVO>> page(
            @RequestParam(required = false) String className,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        return ApiResponse.success(classService.page(className, startDate, endDate, page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<ClassVO> getById(@PathVariable @Min(1) Long id) {
        return ApiResponse.success(classService.getById(id));
    }

    @PostMapping
    public ApiResponse<Void> create(
            @RequestBody @Valid ClassCreateRequest req,
            @RequestAttribute(value = "userId", required = false) Number operatorUserId) {
        classService.create(req, toOperatorEmpId(operatorUserId));
        return ApiResponse.success("新增成功", null);
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid ClassUpdateRequest req,
            @RequestAttribute(value = "userId", required = false) Number operatorUserId) {
        classService.update(id, req, toOperatorEmpId(operatorUserId));
        return ApiResponse.success("修改成功", null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @PathVariable @Min(1) Long id,
            @RequestAttribute(value = "userId", required = false) Number operatorUserId) {
        classService.delete(id, toOperatorEmpId(operatorUserId));
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

