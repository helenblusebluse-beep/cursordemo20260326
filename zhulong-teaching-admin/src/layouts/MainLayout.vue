<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { SwitchButton } from '@element-plus/icons-vue'
import { clearAuth, getDisplayName, getUsername } from '@/auth/session'

const route = useRoute()
const router = useRouter()

const activeMenu = computed(() => route.path)

const currentUserName = computed(() => getDisplayName() ?? '用户')
const isAdmin = computed(() => getUsername() === 'admin')

function onLogoutClick(e: MouseEvent) {
  e.preventDefault()
  clearAuth()
  void router.replace({ path: '/login' })
}
</script>

<template>
  <el-container class="layout-root">
    <el-header class="layout-header">
      <div class="layout-header__title">烛龙云平台-教学管理系统</div>
      <a href="#" class="layout-header__logout" @click="onLogoutClick">
        <el-icon class="layout-header__logout-icon" :size="16">
          <SwitchButton />
        </el-icon>
        退出登录【{{ currentUserName }}】
      </a>
    </el-header>

    <el-container class="layout-body">
      <el-aside width="240px" class="layout-aside">
        <el-menu
          :default-active="activeMenu"
          class="layout-menu"
          router
          :ellipsis="false"
        >
          <el-menu-item index="/home">
            <span class="menu-dot" />
            <span>首页</span>
          </el-menu-item>

          <el-sub-menu index="group-class-student">
            <template #title>
              <span class="menu-dot" />
              <span class="menu-title-text">班级学员管理</span>
              <span class="menu-double-arrow">>></span>
            </template>
            <el-menu-item index="/class">
              <span class="menu-dot menu-dot--sub" />
              <span>班级管理</span>
            </el-menu-item>
            <el-menu-item index="/student">
              <span class="menu-dot menu-dot--sub" />
              <span>学员管理</span>
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="group-system">
            <template #title>
              <span class="menu-dot" />
              <span class="menu-title-text">系统信息管理</span>
              <span class="menu-double-arrow">>></span>
            </template>
            <el-menu-item index="/department">
              <span class="menu-dot menu-dot--sub" />
              <span>部门管理</span>
            </el-menu-item>
            <el-menu-item index="/employee">
              <span class="menu-dot menu-dot--sub" />
              <span>员工管理</span>
            </el-menu-item>
            <el-menu-item v-if="isAdmin" index="/user">
              <span class="menu-dot menu-dot--sub" />
              <span>用户管理</span>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>

      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout-root {
  height: 100%;
  flex-direction: column;
}

.layout-header {
  --header-bg: #545c64;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 60px;
  padding: 0 20px;
  background-color: var(--header-bg);
  color: #fff;
}

.layout-header__title {
  font-family: 'KaiTi', 'STKaiti', 'SimKai', '楷体', serif;
  font-size: 30px;
  line-height: 1.2;
  font-weight: 600;
}

.layout-header__logout {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: #fff;
  text-decoration: none;
  font-size: 14px;
  padding: 6px 4px;
  border-radius: 4px;
}

.layout-header__logout:hover {
  text-decoration: underline;
  background-color: rgba(255, 255, 255, 0.08);
}

.layout-header__logout-icon {
  flex-shrink: 0;
}

.layout-body {
  flex: 1;
  min-height: 0;
}

.layout-aside {
  background-color: #f2f3f5;
  border-right: 1px solid #e4e7ed;
  overflow-x: hidden;
}

.layout-main {
  background-color: #fff;
  padding: 0;
  overflow: auto;
}

.layout-menu {
  border-right: none;
  background-color: transparent;
  --el-menu-item-height: 48px;
  --el-menu-sub-item-height: 44px;
  --el-menu-active-color: #e6a23c;
  --el-menu-hover-bg-color: rgba(0, 0, 0, 0.04);
}

.layout-menu :deep(.el-sub-menu__title) {
  padding-right: 12px !important;
}

.layout-menu :deep(.el-sub-menu__icon-arrow) {
  display: none;
}

.layout-menu :deep(.el-menu-item.is-active) {
  color: var(--el-menu-active-color);
  font-weight: 600;
  background-color: rgba(230, 162, 60, 0.12);
}

.menu-dot {
  display: inline-block;
  width: 6px;
  height: 6px;
  margin-right: 10px;
  border-radius: 50%;
  background-color: #909399;
  flex-shrink: 0;
}

.menu-dot--sub {
  width: 5px;
  height: 5px;
  margin-right: 8px;
  margin-left: 4px;
  background-color: #c0c4cc;
}

.menu-title-text {
  flex: 1;
}

.menu-double-arrow {
  margin-left: auto;
  padding-left: 8px;
  font-size: 12px;
  color: #909399;
  letter-spacing: -2px;
}

.layout-menu :deep(.el-menu-item),
.layout-menu :deep(.el-sub-menu__title) {
  display: flex;
  align-items: center;
}
</style>
