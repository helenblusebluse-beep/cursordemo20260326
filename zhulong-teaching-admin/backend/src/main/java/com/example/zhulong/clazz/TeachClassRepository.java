package com.example.zhulong.clazz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TeachClassRepository extends JpaRepository<TeachClass, Long>, JpaSpecificationExecutor<TeachClass> {
    Optional<TeachClass> findByIdAndIsDeletedFalse(Long id);

    boolean existsByClassNameAndIsDeletedFalse(String className);

    boolean existsByClassNameAndIsDeletedFalseAndIdNot(String className, Long id);
}

