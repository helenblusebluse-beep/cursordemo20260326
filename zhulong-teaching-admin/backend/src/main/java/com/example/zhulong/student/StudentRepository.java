package com.example.zhulong.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    Optional<Student> findByIdAndIsDeletedFalse(Long id);

    long countByClassIdAndIsDeletedFalse(Long classId);

    boolean existsByStudentNoAndIsDeleted(String studentNo, Boolean isDeleted);
    boolean existsByStudentNoAndIsDeletedAndIdNot(String studentNo, Boolean isDeleted, Long id);

    boolean existsByPhoneAndIsDeleted(String phone, Boolean isDeleted);
    boolean existsByPhoneAndIsDeletedAndIdNot(String phone, Boolean isDeleted, Long id);

    boolean existsByIdCardNoAndIsDeleted(String idCardNo, Boolean isDeleted);
    boolean existsByIdCardNoAndIsDeletedAndIdNot(String idCardNo, Boolean isDeleted, Long id);
}

