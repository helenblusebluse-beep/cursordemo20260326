import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import LoginPage from '../views/LoginPage.vue';
import LayoutPage from '../views/LayoutPage.vue';
import ArticleListPage from '../views/ArticleListPage.vue';
import ArticleDetailPage from '../views/ArticleDetailPage.vue';
import MembersPage from '../views/MembersPage.vue';

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'login',
    component: LoginPage
  },
  {
    path: '/',
    component: LayoutPage,
    children: [
      {
        path: '',
        name: 'articles',
        component: ArticleListPage
      },
      {
        path: 'articles/:id',
        name: 'articleDetail',
        component: ArticleDetailPage,
        props: true
      },
      {
        path: 'members',
        name: 'members',
        component: MembersPage
      }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;

