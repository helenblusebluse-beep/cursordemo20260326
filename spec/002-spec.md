# 知识库 AI 系统实现计划（实现路线图）

> 本文档基于 `./spec/001-spec.mc` 的需求与设计文档，给出分阶段、可执行的实现计划，便于团队按任务拆分开发与迭代。  
> 默认仓库结构：  
> - `aichat/frontend`：Vue3 前端工程  
> - `aichat/backend`：Spring Boot 后端工程  

---

## 一、总体实施策略

### 1.1 分阶段目标

- **第 1 阶段（MVP）：**
  - 完成基础登录功能（前端表单 + 后端登录接口 + 简单认证）。
  - 区分管理员 / 普通用户，并根据角色跳转到不同页面。
  - 管理员：能在「用户管理」页面查看用户列表，支持基础增删改。
  - 普通用户：能在「AI 聊天」页面发起对话并获得智谱模型的回复（不强依赖真实知识库检索，可先 mock）。

- **第 2 阶段：**
  - 引入基础知识库文档实体与检索接口。
  - 在聊天接口中引入知识检索结果并传给大模型（简单 RAG）。
  - 丰富用户管理功能（重置密码、状态控制等）。

- **第 3 阶段（增强）：**
  - 优化 UI/UX：会话列表管理、消息流动效、提示词优化。
  - 接入企业 SSO / 第三方登录（如有需要）。
  - 增加操作日志与配置管理等。

### 1.2 角色分工建议

- **后端开发**：
  - 负责 Spring Boot 工程搭建、实体建模、认证授权、业务接口实现、与智谱 API 调用。
- **前端开发**：
  - 负责 Vue3 工程搭建、组件开发、页面布局、路由守卫、与后端接口对接。
- **测试 / 产品**：
  - 负责用例整理、功能验证、联调验收。

---

## 二、后端实现计划（Spring Boot + H2）

### 2.1 工程初始化

**任务 B-1：创建 `aichat/backend` 工程**

- 使用 Spring Initializr 或手动配置：
  - 依赖：
    - `spring-boot-starter-web`
    - `spring-boot-starter-data-jpa`
    - `spring-boot-starter-security`
    - `spring-boot-starter-validation`
    - `h2`
  - `pom.xml`：指定 Java 版本、打包插件配置。
- 创建启动类：`com.example.aichat.AiChatApplication`.
- 配置 `application.yml`：
  - `server.port`：如 8091。
  - H2 数据源：`jdbc:h2:file:./data/aichat`（或内存模式）。
  - JPA 配置：`ddl-auto=update`, `show-sql=true`.

**交付物：**
- 可启动的 Spring Boot 工程，访问 `/actuator/health`（如启用）正常。

---

### 2.2 用户与认证模块

**任务 B-2：建模 User 实体与表结构**

- 创建实体 `User`：
  - 字段：`id, email, password, name, role, status, lastLoginAt, createdAt, updatedAt`.
  - `role` 使用枚举：`ADMIN`, `USER`。
  - `status` 使用枚举：`ENABLED`, `DISABLED`。
- 创建 `UserRepository extends JpaRepository<User, Long>`，并添加：
  - `Optional<User> findByEmail(String email);`

**任务 B-3：配置 Spring Security 基础认证**

- 安全配置类，如 `SecurityConfig`：
  - 禁用 CSRF（先期可简化）。
  - 开放 `/api/auth/login`、静态资源路径。
  - 其余路径要求认证。
  - 配置 `PasswordEncoder`（BCrypt）。
- 实现 `UserDetailsService`：
  - 根据 email 查询用户，封装为 Spring Security 的 `UserDetails`。

**任务 B-4：实现登录接口 `/api/auth/login`**

- 控制器：`AuthController`：
  - `POST /api/auth/login` 参数：`{ email, password }`。
  - 逻辑：
    - 使用 `AuthenticationManager` 认证。
    - 认证通过后，生成 token（简易方案：JWT 或伪 token + 内存 Map）。
    - 返回：`{ token, user: { id, name, email, role } }`。
  - 登录后更新 `lastLoginAt`。
- 简化 token 实现方案（短期）：
  - 使用 JWT（推荐）：
    - 配置签名秘钥，设置过期时间。
    - 所有后端受保护接口从 `Authorization: Bearer xxx` 中解析用户信息。

**任务 B-5：实现基于角色的权限控制**

- 在 Security 配置中：
  - `/api/admin/**` 仅允许 `hasRole('ADMIN')`。
  - `/api/chat/**`、`/api/kb/**` 要求登录用户。
- 提供获取当前登录用户的工具方法（如 `SecurityUtils.getCurrentUserId()`）。

---

### 2.3 用户管理接口（管理员）

**任务 B-6：实现 User 管理服务与 Controller**

- Service：`UserService`：
  - `Page<User> listUsers(Pageable, keyword?)`。
  - `User createUser(UserCreateDto dto)`：
    - 校验 email 唯一。
    - 生成初始密码（可固定如 `ChangeMe123!` 或随机）。
    - 加密存储。
  - `User updateUser(id, UserUpdateDto dto)`：
    - 可修改 name、role、status 等。
  - `void resetPassword(id)`：
    - 重置密码为默认或随机，并返回/记录。
- Controller：`AdminUserController`：
  - `GET /api/admin/users`
  - `POST /api/admin/users`
  - `PUT /api/admin/users/{id}`
  - `POST /api/admin/users/{id}/reset-password`

**任务 B-7：初始化管理员账号**

- 在应用启动时（`CommandLineRunner`）：
  - 若数据库中没有用户，则创建一个默认管理员：
    - `email=admin@example.com, password=Admin123!, role=ADMIN`。

---

### 2.4 聊天与会话模块

**任务 B-8：建模 ChatSession 与 ChatMessage**

- 实体 `ChatSession`：
  - `id, userId, title, createdAt, updatedAt`.
- 实体 `ChatMessage`：
  - `id, sessionId, sender (enum USER/AI), content, createdAt`.
- 仓库接口：
  - `ChatSessionRepository`: 按 userId 查询会话列表。
  - `ChatMessageRepository`: 按 sessionId 查询消息列表。

**任务 B-9：聊天服务 ChatService**

- `List<ChatSession> listSessions(userId)`。
- `ChatSession createSession(userId, optionalTitle)`：
  - 如果未提供标题，使用第一条用户消息的前若干字。
- `List<ChatMessage> listMessages(sessionId, userId)`：
  - 确保 session 归属当前用户。
- `ChatMessage sendMessage(sessionId, userId, userContent)`：
  - 校验 session 归属。
  - 保存用户消息。
  - 调用 `AiModelService` 获取回复文本。
  - 保存 AI 消息并返回。

**任务 B-10：对接智谱 API（AiModelService）**

- 配置：
  - 在 `application.yml` 中配置：`zhipu.apiKey`, `zhipu.model` 等。
- Service：`AiModelService`：
  - 方法：`String chat(String prompt, List<String> kbSnippets)`：
    - 组装 prompt（加入系统角色、知识片段）。
    - 调用智谱 HTTP API（如 `/chat/completions`）。
    - 解析返回文本。
- 首期 KB 片段可为 mock 文本，RAG 逻辑第 2 阶段实现。

**任务 B-11：聊天 Controller**

- `ChatController`（需要登录）：
  - `GET /api/chat/sessions` → 当前用户会话列表。
  - `POST /api/chat/sessions` → 新建会话。
  - `GET /api/chat/sessions/{id}/messages` → 某会话消息。
  - `POST /api/chat/sessions/{id}/messages` → 发送消息并返回 AI 回复。

---

### 2.5 知识库基础接口（第 2 阶段）

**任务 B-12：建模 KbDocument 与简单检索**

- 实体 `KbDocument`：
  - `id, title, content, tags, updatedAt`.
- 仓库：
  - `List<KbDocument> findTopNByTitleContainingOrContentContaining(...)`。
- Controller：
  - `GET /api/kb/search?keyword=...`（仅聊天服务内部或管理端使用）。

**任务 B-13：在 ChatService 中集成知识检索**

- 在调用 `AiModelService` 前：
  - 调用 `KbDocumentRepository` 按关键词检索若干文档。
  - 提取标题 + 部分内容组成 `kbSnippets`。
  - 传给 `AiModelService.chat`。

---

## 三、前端实现计划（Vue3 + Vite）

### 3.1 工程初始化与路由

**任务 F-1：创建 `aichat/frontend` 工程**

- 使用 Vite + Vue3 + TypeScript：
  - 安装依赖：`vue`, `vue-router`, `axios`, `pinia`（如需要状态管理）。
  - `vite.config.ts`：配置：
    - 开发端口（如 5175）。
    - `/api` 代理到后端端口。

**任务 F-2：路由与基础布局**

- 配置路由结构：
  - `/login` → `LoginPage.vue`
  - `/admin/users` → `UserManagementPage.vue`
  - `/chat` → `ChatPage.vue`
  - `/` → 根路由，登录后根据角色重定向。
- 建立布局组件：
  - 如 `MainLayout.vue`：包含顶部导航、侧边栏（聊天/管理入口），中间路由视图。

### 3.2 登录页实现

**任务 F-3：LoginPage.vue UI 与交互**

- 按最新截图风格实现：
  - 顶部品牌文字「Figma」。
  - 中间卡片含「知识库」标题、邮箱/密码输入、记住我复选框、忘记密码、登录按钮、SSO 按钮等。
- 逻辑：
  - 使用 `axios` 调用 `POST /api/auth/login`。
  - 成功后保存 token（localStorage 或 pinia store），保存用户信息与角色。
  - 根据角色跳转：
    - `ADMIN` → `/admin/users`
    - `USER` → `/chat`
  - 失败时在页面展示错误提示（可用简单的 alert 或消息组件）。

**任务 F-4：前端路由守卫**

- 在 `router` 中添加 `beforeEach`：
  - 未登录访问受保护路由 → 跳转 `/login`。
  - 已登录访问 `/login` → 根据角色跳转相应首页。

---

### 3.3 用户管理页面（管理员）

**任务 F-5：UserManagementPage.vue 布局**

- 大致区域：
  - 顶部：标题「成员列表」。
  - 中间：用户表格。
  - 下方或侧边：新建/邀请用户区域、角色说明区域。

**任务 F-6：用户列表表格**

- 使用 `axios` 调用 `GET /api/admin/users`：
  - 支持简单分页或一次性拉取后前端分页。
- 展示字段：邮箱、姓名、角色、状态、最近登录时间、操作。
- 操作列按钮：
  - 「编辑」按钮：弹出对话框编辑用户。
  - 「重置密码」按钮：调用接口并给出提示。

**任务 F-7：创建/编辑用户**

- 创建用户表单：
  - 输入：邮箱、姓名、角色（下拉）、状态。
  - 调用 `POST /api/admin/users`。
- 编辑用户功能：
  - 选中用户后弹出对话框，修改角色/状态等。
  - 调用 `PUT /api/admin/users/{id}`。

---

### 3.4 聊天页面

**任务 F-8：ChatPage.vue 基本布局**

- 参考 `aichat/原型图/src/app/App.tsx` 中的聊天布局：
  - 左侧：会话列表 + 新建会话按钮。
  - 中部：对话消息列表。
  - 底部：输入框 + 发送按钮。

**任务 F-9：会话列表与状态管理**

- 使用 `axios` 调用：
  - `GET /api/chat/sessions` 获取列表。
  - `POST /api/chat/sessions` 创建会话。
- 在前端状态中保存当前选中的 `sessionId`。

**任务 F-10：消息流与发送消息**

- 加载消息：
  - 切换会话时调用 `GET /api/chat/sessions/{id}/messages` 填充消息列表。
- 发送消息：
  - 用户输入内容 → 在前端先追加用户消息到列表。
  - 调用 `POST /api/chat/sessions/{id}/messages`，拿到 AI 回复后追加。
  - 发送过程中展示「正在生成…」的加载状态。
- UI 细节：
  - 用户消息右对齐，AI 消息左对齐。
  - 对代码块可以使用简单的 `<pre><code>` 渲染（后续可引入高亮库）。

---

## 四、测试与验收计划

### 4.1 后端测试

- 单元测试：
  - `UserService`、`AuthController`：登录成功/失败、权限控制。
  - `ChatService`：创建会话、发送消息（可对智谱调用做 mock）。
- 接口测试：
  - 使用 Postman/Apifox 覆盖所有 `/api/auth/**`、`/api/admin/**`、`/api/chat/**`。

### 4.2 前端测试

- 手工回归：
  - 登录流程：正确凭证登录、错误凭证提示、根据不同角色跳转不同页面。
  - 用户管理：列表展示、创建/编辑/重置密码流程。
  - 聊天：新建会话、切换会话、发送消息并展示模型回复。

---

## 五、里程碑与时间预估（示例）

> 以 2～3 人小组为例，时间仅供参考。

- **第 1 周**：
  - 后端：完成工程搭建 + 用户实体 + 登录接口 + 简单 Security。
  - 前端：完成工程搭建 + 登录页 UI + 调通登录接口与路由守卫。

- **第 2 周**：
  - 后端：完成用户管理接口与 Chat 实体/接口骨架（可先 mock 模型回复）。
  - 前端：完成用户管理页面（列表 + 基本编辑）、聊天页基础 UI。

- **第 3 周**：
  - 后端：接入智谱 API，完善 ChatService 与错误处理。
  - 前端：联调聊天接口、优化用户体验、修复问题。

- **第 4 周**：
  - 加入初步知识库文档与检索接口，将其纳入聊天的 prompt。
  - 完成测试与文档整理，准备演示版本。

---

以上计划生成在 `./spec/002-spec.md`，后续若需求有调整，可以在该文件基础上继续补充或标记完成情况。**
