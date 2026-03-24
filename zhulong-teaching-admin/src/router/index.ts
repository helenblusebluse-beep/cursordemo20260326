import { createRouter, createWebHistory } from 'vue-router'
import { getUsername, isLoggedIn } from '@/auth/session'
import MainLayout from '@/layouts/MainLayout.vue'
import LoginView from '@/views/LoginView.vue'
import HomeView from '@/views/HomeView.vue'
import DepartmentManageView from '@/views/DepartmentManageView.vue'
import StudentManageView from '@/views/StudentManageView.vue'
import EmployeeManageView from '@/views/EmployeeManageView.vue'
import UserManageView from '@/views/UserManageView.vue'
import ClassManageView from '@/views/class/ClassManageView.vue'
import ClassFormDemo from '@/views/class/ClassFormDemo.vue'
import ClassRequirementDemo from '@/views/class/ClassRequirementDemo.vue'
import ClassDeleteDemo from '@/views/class/ClassDeleteDemo.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: { public: true, title: '登录' },
    },
    {
      path: '/',
      component: MainLayout,
      redirect: '/home',
      children: [
        {
          path: 'home',
          name: 'home',
          component: HomeView,
          meta: { title: '首页' },
        },
        {
          path: 'class',
          name: 'class-manage',
          component: ClassManageView,
          meta: { pageTitle: '班级管理' },
        },
        {
          path: 'class-form-demo',
          name: 'class-form-demo',
          component: ClassFormDemo,
          meta: { pageTitle: '班级管理-表单对照' },
        },
        {
          path: 'class-requirement-demo',
          name: 'class-requirement-demo',
          component: ClassRequirementDemo,
          meta: { pageTitle: '班级管理-需求说明' },
        },
        {
          path: 'class-delete-demo',
          name: 'class-delete-demo',
          component: ClassDeleteDemo,
          meta: { pageTitle: '删除班级-确认弹窗' },
        },
        {
          path: 'student',
          name: 'student-manage',
          component: StudentManageView,
          meta: { pageTitle: '学员管理' },
        },
        {
          path: 'department',
          name: 'department-manage',
          component: DepartmentManageView,
          meta: { pageTitle: '部门管理' },
        },
        {
          path: 'employee',
          name: 'employee-manage',
          component: EmployeeManageView,
          meta: { pageTitle: '员工管理' },
        },
        {
          path: 'user',
          name: 'user-manage',
          component: UserManageView,
          meta: { pageTitle: '用户管理', requiresAdmin: true },
        },
      ],
    },
  ],
})

router.beforeEach((to) => {
  if (to.meta.public) {
    if (to.path === '/login' && isLoggedIn()) {
      return { path: '/home' }
    }
    return true
  }
  if (!isLoggedIn()) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }
  if (to.meta.requiresAdmin && getUsername() !== 'admin') {
    return { path: '/home' }
  }
  return true
})

export default router
