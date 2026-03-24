package com.example.zhulong.student;

import com.example.zhulong.common.ApiResponse;
import com.example.zhulong.common.PageResult;
import com.example.zhulong.student.dto.StudentCreateRequest;
import com.example.zhulong.student.dto.StudentDemeritRequest;
import com.example.zhulong.student.dto.StudentUpdateRequest;
import com.example.zhulong.student.vo.StudentVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@Validated
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ApiResponse<PageResult<StudentVO>> page(
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) String education,
            @RequestParam(required = false) Long classId,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        return ApiResponse.success(studentService.page(studentName, education, classId, page, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<StudentVO> getById(@PathVariable @Min(1) Long id) {
        return ApiResponse.success(studentService.getById(id));
    }

    @PostMapping
    public ApiResponse<Void> create(
            @RequestBody @Valid StudentCreateRequest req,
            @RequestAttribute(value = "userId", required = false) Number operatorUserId) {
        studentService.create(req, toOperatorEmpId(operatorUserId));
        return ApiResponse.success("新增成功", null);
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid StudentUpdateRequest req,
            @RequestAttribute(value = "userId", required = false) Number operatorUserId) {
        studentService.update(id, req, toOperatorEmpId(operatorUserId));
        return ApiResponse.success("修改成功", null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @PathVariable @Min(1) Long id,
            @RequestAttribute(value = "userId", required = false) Number operatorUserId) {
        studentService.delete(id, toOperatorEmpId(operatorUserId));
        return ApiResponse.success("删除成功", null);
    }

    @DeleteMapping
    public ApiResponse<Void> deleteBatch(
            @RequestParam List<Long> ids,
            @RequestAttribute(value = "userId", required = false) Number operatorUserId) {
        studentService.deleteBatch(ids, toOperatorEmpId(operatorUserId));
        return ApiResponse.success("删除成功", null);
    }

    @PostMapping("/{id}/demerits")
    public ApiResponse<Void> demerit(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid StudentDemeritRequest req,
            @RequestAttribute(value = "userId", required = false) Number operatorUserId) {
        studentService.demerit(id, req, toOperatorEmpId(operatorUserId));
        return ApiResponse.success("处理成功", null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Void> handle(IllegalArgumentException ex) {
        return ApiResponse.fail(ex.getMessage());
    }

    private static Long toOperatorEmpId(Number userId) {
        return userId == null ? null : userId.longValue();
    }
}

