package com.example.zhulong.department.mapper;

import com.example.zhulong.department.model.DepartmentDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepartmentMapper {
    List<DepartmentDO> selectList(@Param("deptName") String deptName);

    DepartmentDO selectById(@Param("id") Long id);

    int insertDepartment(DepartmentDO departmentDO);

    int updateDeptNameById(@Param("id") Long id, @Param("deptName") String deptName);

    int logicDeleteById(@Param("id") Long id);

    int countByDeptName(@Param("deptName") String deptName);

    int countByDeptNameExcludeId(@Param("deptName") String deptName, @Param("id") Long id);

    /**
     * 部门操作日志（与 sys_department 写操作同一 SqlSession/事务，避免 JdbcTemplate 与 JPA 事务连接不一致）
     */
    int insertDepartmentLog(
            @Param("deptId") Long deptId,
            @Param("operateType") String operateType,
            @Param("beforeName") String beforeName,
            @Param("afterName") String afterName,
            @Param("operatorEmpId") Long operatorEmpId);
}

