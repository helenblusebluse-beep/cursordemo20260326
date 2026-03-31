# 中州后台管理系统（已落地首页骨架）

已按你给的原型图与需求，完成一个可运行的首页工程骨架：  
前端 **Vue3 + Element Plus + ECharts + Pinia + Vue Router**，后端 **Spring Boot + Spring Security + MyBatis-Plus + Redis + Swagger**，并预留 **Nginx** 与 **MySQL/Redis Docker** 配置。

## 目录结构

```text
zhongzhou/
├── frontend/                 # Vue3 首页项目（已实现首页原型布局）
├── backend/                  # SpringBoot 后端（已提供 /api/home/dashboard）
├── docker/
│   └── docker-compose.dev.yml
├── nginx/
│   └── nginx.conf.example
└── docs/
```

## 快速启动

### 1) 启动基础依赖（可选）

```bash
cd zhongzhou/docker
docker compose -f docker-compose.dev.yml up -d
```

### 2) 启动后端

```bash
cd zhongzhou/backend
mvn spring-boot:run -DskipTests
```

- 后端地址：`http://localhost:8080`
- Swagger：`http://localhost:8080/swagger-ui.html`
- 首页接口：`http://localhost:8080/api/home/dashboard`

### 3) 启动前端

```bash
cd zhongzhou/frontend
npm install
npm run dev
```

- 前端地址：`http://localhost:5175`
- 已配置 `/api -> http://localhost:8080` 代理

## 当前实现范围

- 侧栏 + 顶栏 + 面包屑 + 首页卡片布局（贴近你提供的图1）
- 数据概览环形指标
- 数据统计折线图（ECharts）
- 老人等级分布饼图（ECharts）
- 老人年龄分布柱状图（ECharts）
- 预约总览时间轴
- 常用功能宫格
- 后端接口返回首页所需结构化数据（并集成 Redis 写入示例）

## 下一步建议

1. 先将首页改为真实数据库查询（替换 `DashboardService` 中示例数据）。  
2. 按若依风格继续拆分模块：用户管理、订单管理、来访管理等。  
3. 补充 JWT 鉴权、角色权限、菜单动态加载。  
4. 配置 Nginx 生产部署与 CI/CD。

## 技术栈一览

| 层级 | 技术 |
|------|------|
| 前端 | Vue 3、Vite、Element Plus、Pinia、Vue Router、Axios |
| 后端 | Spring Boot、Spring Security、JWT/会话（以所选若依版本为准）、MyBatis 或 MyBatis-Plus |
| 数据 | MySQL 8.x、Redis（会话/缓存/限流等，以若依配置为准） |
| 运维 | Nginx（反向代理 + 静态资源）、可选 Docker Compose 本地依赖 |

## 推荐仓库与目录约定

官方常见组合（任选其一，与团队习惯一致即可）：

- **RuoYi-Vue3**（前后端分离，Vue3）：后端 `ruoyi-admin` + 前端 `ruoyi-ui` 类结构。
- 将官方项目克隆或解压到本目录下，例如：

```text
zhongzhou/
├── README.md                 # 本说明
├── docs/                     # 架构与原型说明
├── docker/                   # 本地 MySQL/Redis（可选）
├── nginx/                    # Nginx 配置示例
└── ruoyi-xxx/                # 若依官方工程（由你 git clone 后放入）
```

克隆示例（版本以官网/Gitee 为准）：

```bash
cd zhongzhou
git clone https://gitee.com/y_project/RuoYi-Vue.git ruoyi-vue
# 或选用 Vue3 分支/若依分离版，以官方文档为准
```

## 与原型图对应的模块

- **布局**：顶栏标题、面包屑、页签（Tab）、左侧菜单分组（如「系统管理」）。
- **列表页**：查询区（关键词、状态下拉）+「搜索」「重置」；工具栏「新增」等；表格「操作」列「修改 / 删除」等（部门管理可为树表，用户管理一般为平铺列表）。
- **用户管理**：建议在侧栏「系统管理」下配置路由 **用户管理**，表格字段常见为：用户名、昵称、部门、手机号、状态、创建时间、操作。

具体菜单与权限在若依后台「菜单管理 / 角色管理」中配置即可。

## 本地开发简要步骤

1. 安装 **JDK 17**（或若依文档要求版本）、**Maven**、**Node.js LTS**。
2. 创建数据库并导入若依提供的 **SQL 脚本**。
3. 修改后端 `application.yml` 中 MySQL、Redis 连接。
4. 后端：`mvn clean package`，运行主类或 `java -jar ruoyi-admin.jar`。
5. 前端：`npm install && npm run dev`（以实际前端工程为准）。
6. 访问 Swagger：一般为 `http://localhost:端口/swagger-ui.html` 或 Knife4j 文档地址（见若依版本说明）。

## Nginx 与部署

生产环境建议：**Nginx** 托管前端 `dist`，`/prod-api` 或 `/dev-api` 反向代理到后端；HTTPS、静态缓存、gzip 等按运维规范配置。参见 `nginx/nginx.conf.example`。

## 文档索引

- [架构与依赖说明](docs/architecture.md)
- [UI 原型与页面约定](docs/ui-prototype.md)

## 许可证

若依框架遵循其官方开源协议；本目录说明文档与示例配置可随业务仓库自行约定。
