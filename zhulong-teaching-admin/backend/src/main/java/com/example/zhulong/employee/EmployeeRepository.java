package com.example.zhulong.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    Optional<Employee> findByUsernameAndIsDeletedFalse(String username);

    Optional<Employee> findByIdAndIsDeletedFalse(Long id);

    boolean existsByUsernameAndIsDeleted(String username, Boolean isDeleted);
    boolean existsByUsernameAndIsDeletedAndIdNot(String username, Boolean isDeleted, Long id);

    boolean existsByPhoneAndIsDeleted(String phone, Boolean isDeleted);
    boolean existsByPhoneAndIsDeletedAndIdNot(String phone, Boolean isDeleted, Long id);
}

