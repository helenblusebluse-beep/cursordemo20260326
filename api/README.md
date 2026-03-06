# User API（用户模块）

RESTful 用户接口，Node.js + Express，router/controller/service 三层，内存存储。

## 启动

```bash
cd api
npm install
npm start
```

默认端口 `3000`，可通过环境变量 `PORT` 修改。

## 接口

| 方法   | 路径           | 说明     |
|--------|----------------|----------|
| POST   | /api/users     | 创建用户 |
| GET    | /api/users     | 列表分页 |
| GET    | /api/users/:id | 按 ID 查 |
| PATCH  | /api/users/:id | 更新     |
| DELETE | /api/users/:id | 删除     |

创建示例：`POST /api/users` Body `{ "name": "张三" }`  
列表查询：`GET /api/users?page=1&pageSize=10`
