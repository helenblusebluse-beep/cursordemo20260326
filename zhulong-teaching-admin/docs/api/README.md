# 接口文档总览

## 文档列表

- `department-api.md`：部门管理接口文档
- `class-api.md`：班级管理接口文档
- `student-api.md`：学员管理接口文档
- `employee-api.md`：员工管理接口文档

## 统一约定

- 响应结构统一：
  - `code`：业务码，`1` 成功，`0` 失败
  - `msg`：提示信息
  - `data`：业务数据（对象/数组/分页对象/null）
- 时间格式：
  - 日期：`yyyy-MM-dd`
  - 日期时间：`yyyy-MM-dd HH:mm:ss`
- 删除策略：
  - 管理类删除接口统一为逻辑删除（`is_deleted=1`）

## 分页对象约定

```json
{
  "total": 100,
  "rows": []
}
```

## RESTful 路径规范

- 部门：`/api/departments`
- 班级：`/api/classes`
- 学员：`/api/students`
- 员工：`/api/employees`

