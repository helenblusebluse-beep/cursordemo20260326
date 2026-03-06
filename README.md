# cursorDemo

本仓库用于演示/练习在 Cursor 中创建项目文件。

## 运行与预览

### 企业官网（静态站）

- **入口**：`site/index.html`
- **建议方式（本地起服务）**：

```bash
cd site
python -m http.server 8000
```

然后在浏览器访问 `http://localhost:8000`。

### 冒泡排序示例

```bash
python bubble_sort.py
```

## 会话总结（累积追加）

> 说明：本章节内容为**累积增加**，每次会话结束都会在这里追加一段总结。

### 2026-03-05：生成企业官网与示例代码

- **会话的主要目的**
  - 在项目中生成一个可直接预览的企业官网模板，并补齐基础工程文件。
- **完成的主要任务**
  - 创建企业官网静态站页面（首页/关于/服务/案例/新闻/联系/隐私/404）。
  - 添加全站样式与基础交互（移动端导航、回到顶部、表单校验与提交演示、页脚年份）。
  - 添加基础 SEO 文件（`robots.txt`、`sitemap.xml`、`manifest.webmanifest`）。
  - 提供冒泡排序示例 `bubble_sort.py`（字符串列表示例）。
- **关键决策和解决方案**
  - 默认采用**纯静态 HTML/CSS/JS**，零依赖即可打开/起服务预览，后续可按需再接入后端接口。
  - 联系页表单当前为“演示提交”，如需真实提交可在后端实现接口（例如 `/api/lead`）再对接。
- **使用的技术栈**
  - 前端：HTML + CSS + 原生 JavaScript（静态站）
  - 其他：Python（用于本地起静态服务器与冒泡排序示例）
- **修改了哪些文件**
  - 新增：`site/` 目录下的页面与资源文件、`README.md`
  - 修改：`bubble_sort.py`（示例数据改为 `["aa","bb","ccc"]`）

### 2026-03-05：创建 REST API 代码生成 Skill

- **会话的主要目的**
  - 在项目内创建一个可复用的 Cursor Skill：根据“业务模块名称”自动生成 Express RESTful API 三层结构 CRUD 代码。
- **完成的主要任务**
  - 新增 Skill：`rest-api-generator`（项目级），定义输入理解规则、输出格式约束、以及必须生成的代码结构清单。
- **关键决策和解决方案**
  - 默认生成 **Node.js + Express + CommonJS(require)** 的可运行最小工程结构（包含 router/controller/service 与统一错误处理约定）。
  - 对中文模块名提供常见映射（如“用户模块”-> `user`）；未命中时允许使用拼音作为资源名，保证可落盘生成。
- **使用的技术栈**
  - Node.js + Express（Skill 生成目标）
  - Cursor Skills（项目级：`.cursor/skills/`）
- **修改了哪些文件**
  - 新增：`.cursor/skills/rest-api-generator/SKILL.md`
  - 新增：`.cursor/skills/rest-api-generator/examples.md`

### 2026-03-05：配置 Git 并关联 GitHub 仓库

- **会话的主要目的**
  - 为本项目配置 Git 提交身份和远程仓库，准备推送到 GitHub。
- **完成的主要任务**
  - 在本机全局设置 Git 用户名和邮箱：`helenblusebluse-beep` / `helenblusebluse@gmail.com`。
  - 在项目中完成第一次提交（`first commit`）。
  - 为当前仓库添加远程 `origin`：`https://github.com/helenblusebluse-beep/cursordemo20260326.git`。
- **关键决策和解决方案**
  - 不在远程 URL 中写入明文 token，避免泄露；推送时使用系统凭据/浏览器登录或 SSH。
- **使用的技术栈**
  - Git（本地版本控制与远程配置）
  - GitHub（远程托管仓库）
- **修改了哪些文件**
  - Git 配置：设置全局 `user.name` / `user.email`（本机）。
  - 项目 Git：新增远程 `origin` 指向 GitHub 仓库。

