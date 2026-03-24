package com.example.zhulong.student;

import com.example.zhulong.clazz.TeachClass;
import com.example.zhulong.clazz.TeachClassRepository;
import com.example.zhulong.common.PageResult;
import com.example.zhulong.student.dto.StudentCreateRequest;
import com.example.zhulong.student.dto.StudentDemeritRequest;
import com.example.zhulong.student.dto.StudentUpdateRequest;
import com.example.zhulong.student.vo.StudentVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class StudentService {
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    private final StudentRepository studentRepository;
    private final StudentDemeritLogRepository demeritLogRepository;
    private final StudentOperateLogRepository studentOperateLogRepository;
    private final TeachClassRepository teachClassRepository;

    public StudentService(
            StudentRepository studentRepository,
            StudentDemeritLogRepository demeritLogRepository,
            StudentOperateLogRepository studentOperateLogRepository,
            TeachClassRepository teachClassRepository) {
        this.studentRepository = studentRepository;
        this.demeritLogRepository = demeritLogRepository;
        this.studentOperateLogRepository = studentOperateLogRepository;
        this.teachClassRepository = teachClassRepository;
    }

    public PageResult<StudentVO> page(String studentName, String education, Long classId, int page, int pageSize) {
        Specification<Student> spec = (root, query, cb) -> {
            List<Predicate> ps = new ArrayList<>();
            ps.add(cb.isFalse(root.get("isDeleted")));
            if (StringUtils.hasText(studentName)) ps.add(cb.like(root.get("studentName"), "%" + studentName.trim() + "%"));
            if (StringUtils.hasText(education)) ps.add(cb.equal(root.get("education"), education.trim()));
            if (classId != null) ps.add(cb.equal(root.get("classId"), classId));
            return cb.and(ps.toArray(new Predicate[0]));
        };
        Page<Student> result = studentRepository.findAll(spec, PageRequest.of(Math.max(page - 1, 0), pageSize));
        List<StudentVO> rows = new ArrayList<>();
        for (Student s : result.getContent()) rows.add(toVO(s));
        return new PageResult<>(result.getTotalElements(), rows);
    }

    public StudentVO getById(Long id) {
        return toVO(getExist(id));
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(StudentCreateRequest req, Long operatorEmpId) {
        validateUnique(req.getStudentNo(), req.getPhone(), req.getIdCardNo(), null);
        Student s = new Student();
        apply(req, s);
        s.setIsDeleted(false);
        s.setDemeritCount(0);
        s.setDemeritScore(0);
        s.setCreatedTime(LocalDateTime.now());
        s.setLastOperateTime(LocalDateTime.now());
        s = studentRepository.save(s);
        insertStudentLog(s.getId(), "ADD", operatorEmpId, null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, StudentUpdateRequest req, Long operatorEmpId) {
        Student s = getExist(id);
        validateUnique(req.getStudentNo(), req.getPhone(), req.getIdCardNo(), id);
        apply(req, s);
        s.setLastOperateTime(LocalDateTime.now());
        studentRepository.save(s);
        insertStudentLog(id, "UPDATE", operatorEmpId, null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id, Long operatorEmpId) {
        Student s = getExist(id);
        s.setIsDeleted(true);
        s.setLastOperateTime(LocalDateTime.now());
        studentRepository.save(s);
        insertStudentLog(id, "DELETE", operatorEmpId, null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> ids, Long operatorEmpId) {
        if (ids == null || ids.isEmpty()) throw new IllegalArgumentException("请选择要删除的学员");
        for (Long id : ids) {
            Student s = getExist(id);
            s.setIsDeleted(true);
            s.setLastOperateTime(LocalDateTime.now());
            studentRepository.save(s);
            insertStudentLog(id, "DELETE", operatorEmpId, "批量删除");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void demerit(Long id, StudentDemeritRequest req, Long operatorEmpId) {
        Student s = getExist(id);
        s.setDemeritCount(s.getDemeritCount() + 1);
        s.setDemeritScore(s.getDemeritScore() + req.getScore());
        s.setLastOperateTime(LocalDateTime.now());
        studentRepository.save(s);

        StudentDemeritLog log = new StudentDemeritLog();
        log.setStudentId(id);
        log.setScore(req.getScore());
        log.setRemark(req.getRemark());
        log.setOperatorEmpId(operatorEmpId);
        log.setOperateTime(LocalDateTime.now());
        demeritLogRepository.save(log);
    }

    private void apply(StudentCreateRequest req, Student s) {
        s.setStudentName(req.getStudentName().trim());
        s.setStudentNo(req.getStudentNo().trim());
        s.setGender(req.getGender());
        s.setPhone(req.getPhone().trim());
        s.setIdCardNo(req.getIdCardNo().trim());
        s.setIsCollegeStudent(req.getIsCollegeStudent());
        s.setAddress(req.getAddress() == null ? null : req.getAddress().trim());
        s.setEducation(req.getEducation() == null ? null : req.getEducation().trim());
        s.setGraduateDate(req.getGraduateDate());
        s.setClassId(req.getClassId());
    }

    private void apply(StudentUpdateRequest req, Student s) {
        s.setStudentName(req.getStudentName().trim());
        s.setStudentNo(req.getStudentNo().trim());
        s.setGender(req.getGender());
        s.setPhone(req.getPhone().trim());
        s.setIdCardNo(req.getIdCardNo().trim());
        s.setIsCollegeStudent(req.getIsCollegeStudent());
        s.setAddress(req.getAddress() == null ? null : req.getAddress().trim());
        s.setEducation(req.getEducation() == null ? null : req.getEducation().trim());
        s.setGraduateDate(req.getGraduateDate());
        s.setClassId(req.getClassId());
    }

    private void validateUnique(String studentNo, String phone, String idCardNo, Long id) {
        if (id == null) {
            if (studentRepository.existsByStudentNoAndIsDeleted(studentNo, false)) throw new IllegalArgumentException("学号已存在");
            if (studentRepository.existsByPhoneAndIsDeleted(phone, false)) throw new IllegalArgumentException("手机号已存在");
            if (studentRepository.existsByIdCardNoAndIsDeleted(idCardNo, false)) throw new IllegalArgumentException("身份证号已存在");
        } else {
            if (studentRepository.existsByStudentNoAndIsDeletedAndIdNot(studentNo, false, id)) throw new IllegalArgumentException("学号已存在");
            if (studentRepository.existsByPhoneAndIsDeletedAndIdNot(phone, false, id)) throw new IllegalArgumentException("手机号已存在");
            if (studentRepository.existsByIdCardNoAndIsDeletedAndIdNot(idCardNo, false, id)) throw new IllegalArgumentException("身份证号已存在");
        }
    }

    private Student getExist(Long id) {
        return studentRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new IllegalArgumentException("学员不存在"));
    }

    private StudentVO toVO(Student s) {
        StudentVO vo = new StudentVO();
        vo.setId(s.getId());
        vo.setStudentName(s.getStudentName());
        vo.setStudentNo(s.getStudentNo());
        vo.setGender(s.getGender());
        vo.setPhone(s.getPhone());
        vo.setIdCardNo(s.getIdCardNo());
        vo.setIsCollegeStudent(s.getIsCollegeStudent());
        vo.setAddress(s.getAddress());
        vo.setEducation(s.getEducation());
        vo.setGraduateDate(s.getGraduateDate() == null ? null : s.getGraduateDate().toString());
        vo.setClassId(s.getClassId());
        vo.setClassName(resolveClassName(s.getClassId()));
        vo.setDemeritCount(s.getDemeritCount());
        vo.setDemeritScore(s.getDemeritScore());
        vo.setLastOperateTime(s.getLastOperateTime() == null ? null : s.getLastOperateTime().format(DT));
        return vo;
    }

    private String resolveClassName(Long classId) {
        if (classId == null) {
            return null;
        }
        return teachClassRepository.findByIdAndIsDeletedFalse(classId).map(TeachClass::getClassName).orElse(null);
    }

    private void insertStudentLog(Long studentId, String operateType, Long operatorEmpId, String remark) {
        StudentOperateLog log = new StudentOperateLog();
        log.setStudentId(studentId);
        log.setOperateType(operateType);
        log.setOperatorEmpId(operatorEmpId);
        log.setRemark(remark);
        studentOperateLogRepository.save(log);
    }
}

