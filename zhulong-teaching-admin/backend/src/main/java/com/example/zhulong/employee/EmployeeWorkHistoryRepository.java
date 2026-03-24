package com.example.zhulong.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeWorkHistoryRepository extends JpaRepository<EmployeeWorkHistory, Long> {
    List<EmployeeWorkHistory> findByEmpIdOrderByIdAsc(Long empId);

    @Modifying
    @Query("DELETE FROM EmployeeWorkHistory h WHERE h.empId = :empId")
    void deleteByEmpId(@Param("empId") Long empId);
}

