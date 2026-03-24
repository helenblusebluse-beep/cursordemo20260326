# 学员管理

> 统一说明：
> - 响应格式统一为：`{ code, msg, data }`
> - `code=1` 表示成功，`code=0` 表示失败
> - 时间格式：`yyyy-MM-dd` 或 `yyyy-MM-dd HH:mm:ss`

## 学员分页查询

### 基本信息

> 请求路径：/api/students
>
> 请求方式：GET
>
> 接口描述：学员列表分页查询，支持姓名/最高学历/所属班级筛选。

#### 请求参数

参数格式：queryString

| 参数名称    | 是否必须 | 示例  | 备注 |
| ----------- | -------- | ----- | ---- |
| studentName | 否       | 张    | 姓名模糊查询 |
| education   | 否       | 本科  | 最高学历精确查询 |
| classId     | 否       | 1     | 所属班级ID |
| page        | 是       | 1     | 页码 |
| pageSize    | 是       | 10    | 每页条数 |

请求参数样例：

```shell
/api/students?page=1&pageSize=10
/api/students?studentName=李&page=1&pageSize=10
/api/students?studentName=李&education=大专&classId=2&page=1&pageSize=10
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
| \|- id | number | 非必须 | 学员ID |
| \|- studentName | string | 非必须 | 姓名 |
| \|- studentNo | string | 非必须 | 学号 |
| \|- gender | number | 非必须 | 1男/2女 |
| \|- phone | string | 非必须 | 手机号 |
| \|- idCardNo | string | 非必须 | 身份证号 |
| \|- isCollegeStudent | boolean | 非必须 | 是否院校学员 |
| \|- address | string | 非必须 | 联系地址 |
| \|- education | string | 非必须 | 最高学历 |
| \|- graduateDate | string | 非必须 | 毕业时间 |
| \|- classId | number | 非必须 | 班级ID |
| \|- demeritCount | number | 非必须 | 违纪次数 |
| \|- demeritScore | number | 非必须 | 违纪扣分 |
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
        "studentName": "赵亮",
        "studentNo": "A220505001",
        "gender": 1,
        "phone": "18809090001",
        "idCardNo": "110101200001010011",
        "isCollegeStudent": false,
        "address": "北京市昌平区金燕龙",
        "education": "本科",
        "graduateDate": "2019-07-01",
        "classId": 1,
        "demeritCount": 1,
        "demeritScore": 5,
        "lastOperateTime": "2026-03-20 14:57:04"
      }
    ]
  }
}
```

## 根据ID查询学员

### 基本信息

> 请求路径：/api/students/{id}
>
> 请求方式：GET
>
> 接口描述：根据ID查询学员详情（编辑回显）。

#### 请求参数

参数格式：路径参数

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| id | number | 必须 | 学员ID |

#### 响应数据

参数格式：application/json

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| code | number | 必须 | 1成功/0失败 |
| msg | string | 非必须 | 提示信息 |
| data | object | 非必须 | 学员详情 |

## 新增学员

### 基本信息

> 请求路径：/api/students
>
> 请求方式：POST
>
> 接口描述：新增学员。

#### 请求参数

格式：application/json

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| studentName | string | 必须 | 姓名，2-10位 |
| studentNo | string | 必须 | 学号，固定10位且唯一 |
| gender | number | 必须 | 1男/2女 |
| phone | string | 必须 | 手机号，11位且唯一 |
| idCardNo | string | 必须 | 身份证号，18位且唯一 |
| isCollegeStudent | boolean | 必须 | 是否院校学员 |
| address | string | 否 | 联系地址，最多100位 |
| education | string | 否 | 最高学历 |
| graduateDate | string | 否 | 毕业时间 yyyy-MM-dd |
| classId | number | 否 | 所属班级ID |

请求参数样例：

```json
{
  "studentName": "王浩",
  "studentNo": "A220505006",
  "gender": 1,
  "phone": "18809090006",
  "idCardNo": "110101200001010066",
  "isCollegeStudent": false,
  "address": "北京市海淀区西二旗",
  "education": "本科",
  "graduateDate": "2021-07-01",
  "classId": 1
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

## 修改学员

### 基本信息

> 请求路径：/api/students/{id}
>
> 请求方式：PUT
>
> 接口描述：修改学员。

#### 请求参数

格式：application/json（同新增）

路径参数：

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| id | number | 必须 | 学员ID |

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

## 删除学员

### 基本信息

> 请求路径：/api/students/{id}
>
> 请求方式：DELETE
>
> 接口描述：删除单个学员（逻辑删除）。

#### 请求参数

参数格式：路径参数

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| id | number | 必须 | 学员ID |

#### 响应数据

参数格式：application/json

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| code | number | 必须 | 1成功/0失败 |
| msg | string | 非必须 | 提示信息 |
| data | object | 非必须 | 返回null |

## 批量删除学员

### 基本信息

> 请求路径：/api/students?ids=1,2,3
>
> 请求方式：DELETE
>
> 接口描述：批量删除学员（逻辑删除）。

#### 请求参数

参数格式：queryString

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| ids | number[] | 必须 | 学员ID数组，示例：1,2,3 |

#### 响应数据

参数格式：application/json

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| code | number | 必须 | 1成功/0失败 |
| msg | string | 非必须 | 提示信息 |
| data | object | 非必须 | 返回null |

## 学员违纪处理

### 基本信息

> 请求路径：/api/students/{id}/demerits
>
> 请求方式：POST
>
> 接口描述：处理学员违纪，累计违纪次数和扣分并写入日志。

#### 请求参数

格式：application/json

| 参数名 | 类型 | 是否必须 | 备注 |
| ------ | ---- | -------- | ---- |
| score  | number | 必须 | 本次扣分，>=1 |
| remark | string | 否 | 备注 |

请求参数样例：

```json
{
  "score": 5,
  "remark": "迟到"
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
  "msg": "处理成功",
  "data": null
}
```

## 通用错误码

| code | msg示例 | 场景 |
| ---- | ------- | ---- |
| 1 | success | 成功 |
| 0 | 学员不存在 | ID无效或已删除 |
| 0 | 学号已存在 | 唯一约束冲突 |
| 0 | 手机号已存在 | 唯一约束冲突 |
| 0 | 身份证号已存在 | 唯一约束冲突 |
| 0 | 请选择要删除的学员 | 批量删除参数为空 |

