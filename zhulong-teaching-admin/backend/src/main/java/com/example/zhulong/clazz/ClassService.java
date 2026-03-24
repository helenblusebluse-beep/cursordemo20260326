package com.example.zhulong.clazz;

import com.example.zhulong.clazz.dto.ClassCreateRequest;
import com.example.zhulong.clazz.dto.ClassUpdateRequest;
import com.example.zhulong.clazz.vo.ClassVO;
import com.example.zhulong.common.PageResult;
import com.example.zhulong.employee.Employee;
import com.example.zhulong.employee.EmployeeRepository;
import com.example.zhulong.student.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ClassService {
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    private final TeachClassRepository repository;
    private final ClassOperateLogRepository classOperateLogRepository;
    private final EmployeeRepository employeeRepository;
    private final StudentRepository studentRepository;

    public ClassService(
            TeachClassRepository repository,
            ClassOperateLogRepository classOperateLogRepository,
            EmployeeRepository employeeRepository,
            StudentRepository studentRepository) {
        this.repository = repository;
        this.classOperateLogRepository = classOperateLogRepository;
        this.employeeRepository = employeeRepository;
        this.studentRepository = studentRepository;
    }

    public PageResult<ClassVO> page(String className, LocalDate startDate, LocalDate endDate, int page, int pageSize) {
        Specification<TeachClass> spec = (root, query, cb) -> {
            List<Predicate> ps = new ArrayList<>();
            ps.add(cb.isFalse(root.get("isDeleted")));
            if (StringUtils.hasText(className)) {
                ps.add(cb.like(root.get("className"), "%" + className.trim() + "%"));
            }
            if (startDate != null) {
                ps.add(cb.greaterThanOrEqualTo(root.get("startDate"), startDate));
            }
            if (endDate != null) {
                ps.add(cb.lessThanOrEqualTo(root.get("startDate"), endDate));
            }
            return cb.and(ps.toArray(new Predicate[0]));
        };
        Page<TeachClass> result = repository.findAll(spec, PageRequest.of(Math.max(page - 1, 0), pageSize));
        List<ClassVO> rows = new ArrayList<>();
        for (TeachClass c : result.getContent()) {
            rows.add(toVO(c));
        }
        return new PageResult<>(result.getTotalElements(), rows);
    }

    public ClassVO getById(Long id) {
        TeachClass c = repository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new IllegalArgumentException("班级不存在"));
        return toVO(c);
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(ClassCreateRequest req, Long operatorEmpId) {
        validateDates(req.getStartDate(), req.getEndDate());
        String name = req.getClassName().trim();
        if (repository.existsByClassNameAndIsDeletedFalse(name)) {
            throw new IllegalArgumentException("班级名称已存在");
        }
        TeachClass c = new TeachClass();
        c.setClassName(name);
        c.setClassroom(trim(req.getClassroom()));
        c.setStartDate(req.getStartDate());
        c.setEndDate(req.getEndDate());
        c.setHeadTeacherEmpId(req.getHeadTeacherEmpId());
        c.setSubjectName(req.getSubjectName().trim());
        c.setStatus(calcStatus(req.getStartDate(), req.getEndDate()));
        c.setIsDeleted(false);
        c.setCreatedTime(LocalDateTime.now());
        c.setLastOperateTime(LocalDateTime.now());
        c = repository.save(c);
        insertClassLog(c.getId(), "ADD", operatorEmpId, null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, ClassUpdateRequest req, Long operatorEmpId) {
        TeachClass c = repository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new IllegalArgumentException("班级不存在"));
        validateDates(req.getStartDate(), req.getEndDate());
        String name = req.getClassName().trim();
        if (repository.existsByClassNameAndIsDeletedFalseAndIdNot(name, id)) {
            throw new IllegalArgumentException("班级名称已存在");
        }
        c.setClassName(name);
        c.setClassroom(trim(req.getClassroom()));
        c.setStartDate(req.getStartDate());
        c.setEndDate(req.getEndDate());
        c.setHeadTeacherEmpId(req.getHeadTeacherEmpId());
        c.setSubjectName(req.getSubjectName().trim());
        c.setStatus(calcStatus(req.getStartDate(), req.getEndDate()));
        c.setLastOperateTime(LocalDateTime.now());
        repository.save(c);
        insertClassLog(id, "UPDATE", operatorEmpId, null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id, Long operatorEmpId) {
        TeachClass c = repository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new IllegalArgumentException("班级不存在"));
        c.setIsDeleted(true);
        c.setLastOperateTime(LocalDateTime.now());
        repository.save(c);
        insertClassLog(id, "DELETE", operatorEmpId, null);
    }

    private Integer calcStatus(LocalDate start, LocalDate end) {
        LocalDate now = LocalDate.now();
        if (now.isBefore(start)) return 0;
        if (now.isAfter(end)) return 2;
        return 1;
    }

    private String trim(String s) {
        return s == null ? null : s.trim();
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("结课时间不能早于开课时间");
        }
    }

    private ClassVO toVO(TeachClass c) {
        ClassVO vo = new ClassVO();
        vo.setId(c.getId());
        vo.setClassName(c.getClassName());
        vo.setClassroom(c.getClassroom());
        vo.setStartDate(c.getStartDate() == null ? null : c.getStartDate().toString());
        vo.setEndDate(c.getEndDate() == null ? null : c.getEndDate().toString());
        vo.setHeadTeacherEmpId(c.getHeadTeacherEmpId());
        vo.setHeadTeacherName(resolveTeacherName(c.getHeadTeacherEmpId()));
        vo.setStudentCount(studentRepository.countByClassIdAndIsDeletedFalse(c.getId()));
        vo.setSubjectName(c.getSubjectName());
        vo.setStatus(c.getStatus());
        vo.setLastOperateTime(c.getLastOperateTime() == null ? null : c.getLastOperateTime().format(DT));
        return vo;
    }

    private String resolveTeacherName(Long empId) {
        if (empId == null) {
            return null;
        }
        return employeeRepository.findByIdAndIsDeletedFalse(empId).map(Employee::getEmpName).orElse(null);
    }

    private void insertClassLog(Long classId, String operateType, Long operatorEmpId, String remark) {
        ClassOperateLog log = new ClassOperateLog();
        log.setClassId(classId);
        log.setOperateType(operateType);
        log.setOperatorEmpId(operatorEmpId);
        log.setRemark(remark);
        classOperateLogRepository.save(log);
    }
}

