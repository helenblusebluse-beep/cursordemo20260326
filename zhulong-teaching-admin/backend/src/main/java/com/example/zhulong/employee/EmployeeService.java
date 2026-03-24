package com.example.zhulong.employee;

import com.example.zhulong.common.PageResult;
import com.example.zhulong.employee.dto.EmployeeCreateRequest;
import com.example.zhulong.employee.dto.EmployeeUpdateRequest;
import com.example.zhulong.employee.dto.EmployeeWorkHistoryItem;
import com.example.zhulong.employee.vo.EmployeeVO;
import com.example.zhulong.employee.vo.EmployeeWorkHistoryVO;
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
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    private final EmployeeRepository employeeRepository;
    private final EmployeeWorkHistoryRepository workHistoryRepository;
    private final EmployeeOperateLogRepository employeeOperateLogRepository;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            EmployeeWorkHistoryRepository workHistoryRepository,
            EmployeeOperateLogRepository employeeOperateLogRepository) {
        this.employeeRepository = employeeRepository;
        this.workHistoryRepository = workHistoryRepository;
        this.employeeOperateLogRepository = employeeOperateLogRepository;
    }

    public PageResult<EmployeeVO> page(String empName, Integer gender, LocalDate startDate, LocalDate endDate, int page, int pageSize) {
        Specification<Employee> spec = (root, query, cb) -> {
            List<Predicate> ps = new ArrayList<>();
            ps.add(cb.isFalse(root.get("isDeleted")));
            if (StringUtils.hasText(empName)) ps.add(cb.like(root.get("empName"), "%" + empName.trim() + "%"));
            if (gender != null) ps.add(cb.equal(root.get("gender"), gender));
            if (startDate != null) ps.add(cb.greaterThanOrEqualTo(root.get("entryDate"), startDate));
            if (endDate != null) ps.add(cb.lessThanOrEqualTo(root.get("entryDate"), endDate));
            return cb.and(ps.toArray(new Predicate[0]));
        };
        Page<Employee> result = employeeRepository.findAll(spec, PageRequest.of(Math.max(page - 1, 0), pageSize));
        List<EmployeeVO> rows = result.getContent().stream().map(this::toVO).collect(Collectors.toList());
        return new PageResult<>(result.getTotalElements(), rows);
    }

    public EmployeeVO getById(Long id) {
        return toVO(getExist(id));
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(EmployeeCreateRequest req, Long operatorEmpId) {
        validateUnique(req.getUsername(), req.getPhone(), null);
        Employee e = new Employee();
        apply(req, e);
        e.setInitPassword("123456");
        e.setIsDeleted(false);
        e.setCreatedTime(LocalDateTime.now());
        e.setLastOperateTime(LocalDateTime.now());
        e = employeeRepository.save(e);
        replaceWorkHistories(e.getId(), req.getWorkHistories());
        insertEmployeeLog(e.getId(), "ADD", operatorEmpId, null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, EmployeeUpdateRequest req, Long operatorEmpId) {
        Employee e = getExist(id);
        validateUnique(req.getUsername(), req.getPhone(), id);
        apply(req, e);
        e.setLastOperateTime(LocalDateTime.now());
        employeeRepository.save(e);
        replaceWorkHistories(id, req.getWorkHistories());
        insertEmployeeLog(id, "UPDATE", operatorEmpId, null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id, Long operatorEmpId) {
        Employee e = getExist(id);
        e.setIsDeleted(true);
        e.setLastOperateTime(LocalDateTime.now());
        employeeRepository.save(e);
        insertEmployeeLog(id, "DELETE", operatorEmpId, null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> ids, Long operatorEmpId) {
        if (ids == null || ids.isEmpty()) throw new IllegalArgumentException("请选择要删除的员工");
        for (Long id : ids) {
            Employee e = getExist(id);
            e.setIsDeleted(true);
            e.setLastOperateTime(LocalDateTime.now());
            employeeRepository.save(e);
            insertEmployeeLog(id, "DELETE", operatorEmpId, "批量删除");
        }
    }

    private void apply(EmployeeCreateRequest req, Employee e) {
        e.setUsername(req.getUsername().trim());
        e.setEmpName(req.getEmpName().trim());
        e.setGender(req.getGender());
        e.setPhone(req.getPhone().trim());
        e.setDeptId(req.getDeptId());
        e.setPositionName(trim(req.getPositionName()));
        e.setSalary(req.getSalary());
        e.setAvatarUrl(trim(req.getAvatarUrl()));
        e.setEntryDate(req.getEntryDate());
    }

    private void apply(EmployeeUpdateRequest req, Employee e) {
        e.setUsername(req.getUsername().trim());
        e.setEmpName(req.getEmpName().trim());
        e.setGender(req.getGender());
        e.setPhone(req.getPhone().trim());
        e.setDeptId(req.getDeptId());
        e.setPositionName(trim(req.getPositionName()));
        e.setSalary(req.getSalary());
        e.setAvatarUrl(trim(req.getAvatarUrl()));
        e.setEntryDate(req.getEntryDate());
    }

    private void replaceWorkHistories(Long empId, List<EmployeeWorkHistoryItem> items) {
        workHistoryRepository.deleteByEmpId(empId);
        if (items == null || items.isEmpty()) return;
        for (EmployeeWorkHistoryItem item : items) {
            EmployeeWorkHistory h = new EmployeeWorkHistory();
            h.setEmpId(empId);
            h.setStartDate(item.getStartDate());
            h.setEndDate(item.getEndDate());
            h.setCompanyName(trim(item.getCompanyName()));
            h.setPositionName(trim(item.getPositionName()));
            h.setCreatedTime(LocalDateTime.now());
            workHistoryRepository.save(h);
        }
    }

    private Employee getExist(Long id) {
        return employeeRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new IllegalArgumentException("员工不存在"));
    }

    private void validateUnique(String username, String phone, Long id) {
        if (id == null) {
            if (employeeRepository.existsByUsernameAndIsDeleted(username, false)) throw new IllegalArgumentException("用户名已存在");
            if (employeeRepository.existsByPhoneAndIsDeleted(phone, false)) throw new IllegalArgumentException("手机号已存在");
        } else {
            if (employeeRepository.existsByUsernameAndIsDeletedAndIdNot(username, false, id)) throw new IllegalArgumentException("用户名已存在");
            if (employeeRepository.existsByPhoneAndIsDeletedAndIdNot(phone, false, id)) throw new IllegalArgumentException("手机号已存在");
        }
    }

    private String trim(String s) {
        return s == null ? null : s.trim();
    }

    private EmployeeVO toVO(Employee e) {
        EmployeeVO vo = new EmployeeVO();
        vo.setId(e.getId());
        vo.setUsername(e.getUsername());
        vo.setEmpName(e.getEmpName());
        vo.setGender(e.getGender());
        vo.setPhone(e.getPhone());
        vo.setDeptId(e.getDeptId());
        vo.setPositionName(e.getPositionName());
        vo.setSalary(e.getSalary() == null ? null : e.getSalary().toPlainString());
        vo.setAvatarUrl(e.getAvatarUrl());
        vo.setEntryDate(e.getEntryDate() == null ? null : e.getEntryDate().toString());
        vo.setLastOperateTime(e.getLastOperateTime() == null ? null : e.getLastOperateTime().format(DT));

        List<EmployeeWorkHistoryVO> workVos = workHistoryRepository.findByEmpIdOrderByIdAsc(e.getId()).stream().map(h -> {
            EmployeeWorkHistoryVO w = new EmployeeWorkHistoryVO();
            w.setStartDate(h.getStartDate() == null ? null : h.getStartDate().toString());
            w.setEndDate(h.getEndDate() == null ? null : h.getEndDate().toString());
            w.setCompanyName(h.getCompanyName());
            w.setPositionName(h.getPositionName());
            return w;
        }).collect(Collectors.toList());
        vo.setWorkHistories(workVos);
        return vo;
    }

    private void insertEmployeeLog(Long empId, String operateType, Long operatorEmpId, String remark) {
        EmployeeOperateLog log = new EmployeeOperateLog();
        log.setEmpId(empId);
        log.setOperateType(operateType);
        log.setOperatorEmpId(operatorEmpId);
        log.setRemark(remark);
        employeeOperateLogRepository.save(log);
    }
}

