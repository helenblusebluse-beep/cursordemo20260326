import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import OrderSummary from '../components/OrderSummary.vue';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'OrderSummary',
    component: OrderSummary
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;

