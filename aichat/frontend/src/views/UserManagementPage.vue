<template>
  <div class="page">
    <section class="card header-card">
      <h1 class="card-title">成员列表</h1>
    </section>

    <section class="card">
      <table class="table">
        <thead>
          <tr>
            <th>姓名</th>
            <th>邮箱</th>
            <th>角色</th>
            <th>状态</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="u in users" :key="u.id">
            <td>{{ u.name }}</td>
            <td>{{ u.email }}</td>
            <td>{{ u.role }}</td>
            <td>{{ u.status }}</td>
          </tr>
        </tbody>
      </table>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import axios from 'axios';

interface User {
  id: number;
  name: string;
  email: string;
  role: string;
  status: string;
}

const users = ref<User[]>([]);

async function loadUsers() {
  const resp = await axios.get('/api/admin/users', {
    params: { page: 0, size: 50 }
  });
  users.value = resp.data.content ?? [];
}

onMounted(loadUsers);
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 24px;
}
.card {
  background-color: #ffffff;
  border-radius: 12px;
  border: 1px solid #e5e7eb;
  padding: 16px 18px;
}
.card-title {
  font-size: 18px;
  font-weight: 600;
}
.table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}
.table th,
.table td {
  padding: 8px 10px;
  border-bottom: 1px solid #e5e7eb;
  text-align: left;
}
.table thead {
  background-color: #f9fafb;
}
</style>

