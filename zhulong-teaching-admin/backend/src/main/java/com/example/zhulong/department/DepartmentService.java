package com.example.zhulong.department;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.page.PageMethod;
import com.github.pagehelper.Page;
import com.example.zhulong.department.mapper.DepartmentMapper;
import com.example.zhulong.department.model.DepartmentDO;
import com.example.zhulong.department.vo.DepartmentVO;
import com.example.zhulong.common.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    public PageResult<DepartmentVO> page(String deptName, int page, int pageSize) {
        try {
            Page<DepartmentDO> p = PageMethod.startPage(page, pageSize);
            PageMethod.orderBy("last_operate_time desc");
            List<DepartmentDO> list = departmentMapper.selectList(StringUtils.hasText(deptName) ? deptName.trim() : null);
            List<DepartmentVO> rows = list.stream().map(this::toVO).collect(Collectors.toList());
            return new PageResult<>(p.getTotal(), rows);
        } finally {
            // 避免 ThreadLocal 残留导致同一线程后续非分页 SQL（如 selectById）被错误分页
            PageHelper.clearPage();
        }
    }

    public DepartmentVO getById(Long id) {
        PageHelper.clearPage();
        DepartmentDO d = departmentMapper.selectById(id);
        if (d == null) {
            throw new IllegalArgumentException("部门不存在");
        }
        return toVO(d);
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(String deptName, Long operatorEmpId) {
        PageHelper.clearPage();
        String name = normalizeName(deptName);
        if (departmentMapper.countByDeptName(name) > 0) {
            throw new IllegalArgumentException("部门名称已存在");
        }
        DepartmentDO d = new DepartmentDO();
        d.setDeptName(name);
        int effect = departmentMapper.insertDepartment(d);
        if (effect != 1 || d.getId() == null) {
            throw new IllegalArgumentException("新增失败");
        }
        insertDepartmentLog(d.getId(), "ADD", null, name, operatorEmpId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, String deptName, Long operatorEmpId) {
        PageHelper.clearPage();
        DepartmentDO d = departmentMapper.selectById(id);
        if (d == null) {
            throw new IllegalArgumentException("部门不存在");
        }
        String oldName = d.getDeptName();
        String name = normalizeName(deptName);
        if (departmentMapper.countByDeptNameExcludeId(name, id) > 0) {
            throw new IllegalArgumentException("部门名称已存在");
        }
        if (oldName.equals(name)) {
            return;
        }
        int effect = departmentMapper.updateDeptNameById(id, name);
        if (effect != 1) {
            throw new IllegalArgumentException("修改失败");
        }
        insertDepartmentLog(id, "UPDATE", oldName, name, operatorEmpId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id, Long operatorEmpId) {
        PageHelper.clearPage();
        DepartmentDO d = departmentMapper.selectById(id);
        if (d == null) {
            throw new IllegalArgumentException("部门不存在");
        }
        String oldName = d.getDeptName();
        int effect = departmentMapper.logicDeleteById(id);
        if (effect != 1) {
            throw new IllegalArgumentException("删除失败");
        }
        insertDepartmentLog(id, "DELETE", oldName, null, operatorEmpId);
    }

    private void insertDepartmentLog(Long deptId, String operateType, String beforeName, String afterName, Long operatorEmpId) {
        int rows = departmentMapper.insertDepartmentLog(deptId, operateType, beforeName, afterName, operatorEmpId);
        if (rows != 1) {
            throw new IllegalStateException("部门操作日志写入失败，影响行数=" + rows);
        }
    }

    private String normalizeName(String deptName) {
        if (deptName == null) {
            throw new IllegalArgumentException("部门名称不能为空");
        }
        String name = deptName.trim();
        if (name.length() < 2 || name.length() > 10) {
            throw new IllegalArgumentException("部门名称长度必须为2-10位");
        }
        return name;
    }

    private DepartmentVO toVO(DepartmentDO d) {
        return new DepartmentVO(d.getId(), d.getDeptName(), d.getLastOperateTime().format(TIME_FORMATTER));
    }
}
