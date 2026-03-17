<template>
  <div class="layout">
    <aside class="sidebar">
      <div class="logo">知识库</div>
      <nav>
        <div class="nav-section-title">空间</div>
        <ul class="nav-list">
          <li class="nav-item">
            <a class="nav-link active" href="#">默认空间</a>
          </li>
        </ul>
        <div class="nav-section-title">内容</div>
        <ul class="nav-list">
          <li class="nav-item">
            <RouterLink class="nav-link" to="/">文档列表</RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink class="nav-link" to="/members">成员管理</RouterLink>
          </li>
        </ul>
      </nav>
    </aside>
    <main class="main">
      <header class="topbar">
        <h1 class="topbar-title">知识库</h1>
      </header>
      <section class="content">
        <router-view />
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import { RouterLink } from 'vue-router';
</script>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
}
.sidebar {
  width: 220px;
  background-color: #111827;
  color: #e5e7eb;
  padding: 18px 14px;
}
.logo {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 24px;
}
.nav-section-title {
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: #6b7280;
  margin: 12px 6px 6px;
}
.nav-list {
  list-style: none;
}
.nav-item {
  margin-bottom: 4px;
}
.nav-link {
  display: block;
  padding: 8px 10px;
  border-radius: 6px;
  font-size: 14px;
  color: #e5e7eb;
  text-decoration: none;
}
.nav-link.router-link-active,
.nav-link:hover {
  background-color: #1f2937;
}
.main {
  flex: 1;
  display: flex;
  flex-direction: column;
}
.topbar {
  height: 56px;
  background-color: #ffffff;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  align-items: center;
  padding: 0 20px;
}
.topbar-title {
  font-size: 18px;
  font-weight: 600;
}
.content {
  padding: 20px 24px 32px;
}
</style>

