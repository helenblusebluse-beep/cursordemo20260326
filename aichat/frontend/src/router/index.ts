import { createRouter, createWebHistory } from 'vue-router';
import type { RouteRecordRaw } from 'vue-router';
import LoginPage from '../views/LoginPage.vue';
import ChatPage from '../views/ChatPage.vue';
import UserManagementPage from '../views/UserManagementPage.vue';

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'login',
    component: LoginPage
  },
  {
    path: '/chat',
    name: 'chat',
    component: ChatPage
  },
  {
    path: '/admin/users',
    name: 'adminUsers',
    component: UserManagementPage
  },
  {
    path: '/',
    name: 'root',
    redirect: () => {
      const raw = localStorage.getItem('authUser');
      if (!raw) return '/login';
      try {
        const user = JSON.parse(raw);
        return user.role === 'ADMIN' ? '/admin/users' : '/chat';
      } catch {
        return '/login';
      }
    }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to, from, next) => {
  const raw = localStorage.getItem('authUser');
  const isAuthed = !!raw;

  if (to.path === '/login') {
    if (isAuthed) {
      try {
        const user = JSON.parse(raw!);
        next(user.role === 'ADMIN' ? '/admin/users' : '/chat');
      } catch {
        next();
      }
    } else {
      next();
    }
    return;
  }

  if (!isAuthed && to.path !== '/login') {
    next('/login');
    return;
  }

  next();
});

export default router;

