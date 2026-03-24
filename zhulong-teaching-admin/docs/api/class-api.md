# 班级管理

> 统一说明：
> - 响应格式统一为：`{ code, msg, data }`
> - `code=1` 表示成功，`code=0` 表示失败
> - 时间格式：`yyyy-MM-dd` 或 `yyyy-MM-dd HH:mm:ss`

## 班级分页查询

### 基本信息

> 请求路径：/api/classes
>
> 请求方式：GET
>
> 接口描述：该接口用于班级列表条件分页查询（按最后操作时间倒序）。

#### 请求参数

参数格式：queryString

参数说明：

| 参数名称  | 是否必须 | 示例       | 备注                             |
| --------- | -------- | ---------- | -------------------------------- |
| className | 否       | JavaEE     | 班级名称，模糊查询               |
| startDate | 否       | 2025-01-01 | 开课时间范围开始（yyyy-MM-dd）   |
| endDate   | 否       | 2025-12-31 | 开课时间范围结束（yyyy-MM-dd）   |
| page      | 是       | 1          | 页码，默认1                      |
| pageSize  | 是       | 10         | 每页条数，默认10                 |

请求参数样例：

```shell
/api/classes?page=1&pageSize=10
/api/classes?className=JavaEE&page=1&pageSize=10
/api/classes?className=JavaEE&startDate=2025-01-01&endDate=2025-12-31&page=1&pageSize=10
```

#### 响应数据

参数格式：application/json

| 参数名            | 类型      | 是否必须 | 备注 |
| ----------------- | --------- | -------- | ---- |
| code              | number    | 必须     | 1成功/0失败 |
| msg               | string    | 非必须   | 提示信息 |
| data              | object    | 必须     | 分页对象 |
| \|- total         | number    | 必须     | 总条数 |
| \|- rows          | object[]  | 必须     | 列表数据 |
| \|- id            | number    | 非必须   | 班级ID |
| \|- className     | string    | 非必须   | 班级名称 |
| \|- classroom     | string    | 非必须   | 班级教室 |
| \|- startDate     | string    | 非必须   | 开课时间 |
| \|- endDate       | string    | 非必须   | 结课时间 |
| \|- headTeacherEmpId | number | 非必须   | 班主任员工ID |
| \|- subjectName   | string    | 非必须   | 学科 |
| \|- status        | number    | 非必须   | 0未开班/1在读/2结课 |
| \|- lastOperateTime | string  | 非必须   | 最后操作时间 |

响应数据样例：

```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "total": 2,
    "rows": [
      {
        "id": 1,
        "className": "JavaEE班级300期",
        "classroom": "A303",
        "startDate": "2022-12-01",
        "endDate": "2024-03-15",
        "headTeacherEmpId": 1,
        "subjectName": "JavaEE",
        "status": 2,
        "lastOperateTime": "2026-03-20 14:57:04"
      },
      {
        "id": 5,
        "className": "JavaSE班级200期",
        "classroom": "C101",
        "startDate": "2026-01-01",
        "endDate": "2026-03-20",
        "headTeacherEmpId": 1,
        "subjectName": "Java",
        "status": 1,
        "lastOperateTime": "2026-03-20 14:57:04"
      }
    ]
  }
}
```

## 根据ID查询班级

### 基本信息

> 请求路径：/api/classes/{id}
>
> 请求方式：GET
>
> 接口描述：根据ID查询班级详情（编辑回显）。

#### 请求参数

参数格式：路径参数

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| id | number | 必须 | 班级ID |

请求参数样例：

```shell
/api/classes/1
```

#### 响应数据

参数格式：application/json

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| code | number | 必须 | 1成功/0失败 |
| msg | string | 非必须 | 提示信息 |
| data | object | 非必须 | 班级详情 |
| \|- id | number | 非必须 | 班级ID |
| \|- className | string | 非必须 | 班级名称 |
| \|- classroom | string | 非必须 | 班级教室 |
| \|- startDate | string | 非必须 | 开课时间 |
| \|- endDate | string | 非必须 | 结课时间 |
| \|- headTeacherEmpId | number | 非必须 | 班主任员工ID |
| \|- subjectName | string | 非必须 | 学科 |
| \|- status | number | 非必须 | 状态 |
| \|- lastOperateTime | string | 非必须 | 最后操作时间 |

响应数据样例：

```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "id": 1,
    "className": "JavaEE班级300期",
    "classroom": "A303",
    "startDate": "2022-12-01",
    "endDate": "2024-03-15",
    "headTeacherEmpId": 1,
    "subjectName": "JavaEE",
    "status": 2,
    "lastOperateTime": "2026-03-20 14:57:04"
  }
}
```

## 新增班级

### 基本信息

> 请求路径：/api/classes
>
> 请求方式：POST
>
> 接口描述：新增班级。

#### 请求参数

格式：application/json

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| className | string | 必须 | 班级名称 |
| classroom | string | 否 | 班级教室 |
| startDate | string | 必须 | 开课时间 yyyy-MM-dd |
| endDate | string | 必须 | 结课时间 yyyy-MM-dd |
| headTeacherEmpId | number | 否 | 班主任员工ID |
| subjectName | string | 必须 | 学科 |

请求参数样例：

```json
{
  "className": "JavaEE班级301期",
  "classroom": "A305",
  "startDate": "2026-04-01",
  "endDate": "2026-08-31",
  "headTeacherEmpId": 4,
  "subjectName": "JavaEE"
}
```

#### 响应数据

参数格式：application/json

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| code | number | 必须 | 1成功/0失败 |
| msg | string | 非必须 | 提示信息 |
| data | object | 非必须 | 返回null |

响应数据样例：

```json
{
  "code": 1,
  "msg": "新增成功",
  "data": null
}
```

## 修改班级

### 基本信息

> 请求路径：/api/classes/{id}
>
> 请求方式：PUT
>
> 接口描述：修改班级。

#### 请求参数

格式：application/json

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| className | string | 必须 | 班级名称 |
| classroom | string | 否 | 班级教室 |
| startDate | string | 必须 | 开课时间 yyyy-MM-dd |
| endDate | string | 必须 | 结课时间 yyyy-MM-dd |
| headTeacherEmpId | number | 否 | 班主任员工ID |
| subjectName | string | 必须 | 学科 |

路径参数：

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| id | number | 必须 | 班级ID |

请求参数样例：

```json
{
  "className": "JavaEE班级300期",
  "classroom": "A308",
  "startDate": "2022-12-01",
  "endDate": "2024-03-20",
  "headTeacherEmpId": 1,
  "subjectName": "JavaEE"
}
```

#### 响应数据

参数格式：application/json

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| code | number | 必须 | 1成功/0失败 |
| msg | string | 非必须 | 提示信息 |
| data | object | 非必须 | 返回null |

响应数据样例：

```json
{
  "code": 1,
  "msg": "修改成功",
  "data": null
}
```

## 删除班级

### 基本信息

> 请求路径：/api/classes/{id}
>
> 请求方式：DELETE
>
> 接口描述：删除班级（逻辑删除）。

#### 请求参数

参数格式：路径参数

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| id | number | 必须 | 班级ID |

请求参数样例：

```shell
/api/classes/5
```

#### 响应数据

参数格式：application/json

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| code | number | 必须 | 1成功/0失败 |
| msg | string | 非必须 | 提示信息 |
| data | object | 非必须 | 返回null |

响应数据样例：

```json
{
  "code": 1,
  "msg": "删除成功",
  "data": null
}
```

## 通用错误码

| code | msg示例 | 场景 |
| ---- | ------- | ---- |
| 1 | success | 成功 |
| 0 | 班级不存在 | ID无效或已删除 |
| 0 | 结课时间不能早于开课时间 | 时间范围校验失败 |
| 0 | 参数错误 | 入参缺失或格式非法 |

