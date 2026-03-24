# 员工管理

> 统一说明：
> - 响应格式统一为：`{ code, msg, data }`
> - `code=1` 表示成功，`code=0` 表示失败
> - 时间格式：`yyyy-MM-dd` 或 `yyyy-MM-dd HH:mm:ss`

## 员工分页查询

### 基本信息

> 请求路径：/api/employees
>
> 请求方式：GET
>
> 接口描述：员工列表分页查询，支持姓名、性别、入职时间区间筛选。

#### 请求参数

参数格式：queryString

| 参数名称  | 是否必须 | 示例       | 备注 |
| --------- | -------- | ---------- | ---- |
| empName   | 否       | 赵         | 姓名模糊查询 |
| gender    | 否       | 1          | 性别，1男/2女 |
| startDate | 否       | 2023-01-01 | 入职时间开始 |
| endDate   | 否       | 2023-12-31 | 入职时间结束 |
| page      | 是       | 1          | 页码 |
| pageSize  | 是       | 10         | 每页条数 |

请求参数样例：

```shell
/api/employees?page=1&pageSize=10
/api/employees?empName=赵&page=1&pageSize=10
/api/employees?empName=赵&gender=2&startDate=2023-01-01&endDate=2023-12-31&page=1&pageSize=10
```

#### 响应数据

参数格式：application/json

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| code | number | 必须 | 1成功/0失败 |
| msg | string | 非必须 | 提示信息 |
| data | object | 必须 | 分页对象 |
| \|- total | number | 必须 | 总条数 |
| \|- rows | object[] | 必须 | 列表 |
| \|- id | number | 非必须 | 员工ID |
| \|- username | string | 非必须 | 用户名 |
| \|- empName | string | 非必须 | 姓名 |
| \|- gender | number | 非必须 | 性别 |
| \|- phone | string | 非必须 | 手机号 |
| \|- deptId | number | 非必须 | 所属部门ID |
| \|- positionName | string | 非必须 | 职位 |
| \|- salary | string | 非必须 | 薪资 |
| \|- avatarUrl | string | 非必须 | 头像地址 |
| \|- entryDate | string | 非必须 | 入职日期 |
| \|- lastOperateTime | string | 非必须 | 最后操作时间 |

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
        "username": "zhaomin",
        "empName": "赵敏",
        "gender": 2,
        "phone": "15509128904",
        "deptId": 1,
        "positionName": "班主任",
        "salary": "4200.00",
        "avatarUrl": "https://example.com/avatar/1.png",
        "entryDate": "2023-04-05",
        "lastOperateTime": "2026-03-20 14:57:04"
      }
    ]
  }
}
```

## 根据ID查询员工

### 基本信息

> 请求路径：/api/employees/{id}
>
> 请求方式：GET
>
> 接口描述：根据ID查询员工详情（编辑回显，含工作经历）。

#### 请求参数

参数格式：路径参数

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| id | number | 必须 | 员工ID |

#### 响应数据

参数格式：application/json

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| code | number | 必须 | 1成功/0失败 |
| msg | string | 非必须 | 提示信息 |
| data | object | 非必须 | 员工详情（含workHistories） |

## 新增员工

### 基本信息

> 请求路径：/api/employees
>
> 请求方式：POST
>
> 接口描述：新增员工，成功后分配默认密码123456。

#### 请求参数

格式：application/json

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| username | string | 必须 | 用户名，2-20位且唯一 |
| empName | string | 必须 | 姓名，2-10位 |
| gender | number | 必须 | 1男/2女 |
| phone | string | 必须 | 手机号，11位且唯一 |
| deptId | number | 否 | 所属部门ID |
| positionName | string | 否 | 职位 |
| salary | number | 否 | 薪资 |
| avatarUrl | string | 否 | 头像URL |
| entryDate | string | 否 | 入职日期 yyyy-MM-dd |
| workHistories | object[] | 否 | 工作经历列表 |
| \|- startDate | string | 否 | 开始时间 |
| \|- endDate | string | 否 | 结束时间 |
| \|- companyName | string | 否 | 公司 |
| \|- positionName | string | 否 | 职位 |

请求参数样例：

```json
{
  "username": "zhaomin",
  "empName": "赵敏",
  "gender": 2,
  "phone": "15509128904",
  "deptId": 1,
  "positionName": "班主任",
  "salary": 4200,
  "avatarUrl": "https://example.com/avatar/1.png",
  "entryDate": "2023-04-05",
  "workHistories": [
    {
      "startDate": "2019-02-01",
      "endDate": "2023-03-15",
      "companyName": "陕西巨巴影业有限公司",
      "positionName": "影视后勤生产总监"
    }
  ]
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

## 修改员工

### 基本信息

> 请求路径：/api/employees/{id}
>
> 请求方式：PUT
>
> 接口描述：修改员工信息及工作经历。

#### 请求参数

格式：application/json（同新增）

路径参数：

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| id | number | 必须 | 员工ID |

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

## 删除员工

### 基本信息

> 请求路径：/api/employees/{id}
>
> 请求方式：DELETE
>
> 接口描述：删除单个员工（逻辑删除）。

#### 请求参数

参数格式：路径参数

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| id | number | 必须 | 员工ID |

#### 响应数据

参数格式：application/json

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| code | number | 必须 | 1成功/0失败 |
| msg | string | 非必须 | 提示信息 |
| data | object | 非必须 | 返回null |

## 批量删除员工

### 基本信息

> 请求路径：/api/employees?ids=1,2,3
>
> 请求方式：DELETE
>
> 接口描述：批量删除员工（逻辑删除）。

#### 请求参数

参数格式：queryString

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| ids | number[] | 必须 | 员工ID数组，示例：1,2,3 |

#### 响应数据

参数格式：application/json

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| code | number | 必须 | 1成功/0失败 |
| msg | string | 非必须 | 提示信息 |
| data | object | 非必须 | 返回null |

## 通用错误码

| code | msg示例 | 场景 |
| ---- | ------- | ---- |
| 1 | success | 成功 |
| 0 | 员工不存在 | ID无效或已删除 |
| 0 | 用户名已存在 | 唯一约束冲突 |
| 0 | 手机号已存在 | 唯一约束冲突 |
| 0 | 请选择要删除的员工 | 批量删除参数为空 |

