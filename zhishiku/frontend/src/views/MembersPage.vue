<template>
  <div class="members-page">
    <section class="card header-card">
      <h1 class="card-title">成员列表</h1>
    </section>

    <section class="card table-card">
      <table class="member-table">
        <thead>
          <tr>
            <th>头像</th>
            <th>姓名</th>
            <th>角色</th>
            <th>部门</th>
            <th>加入时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="m in members" :key="m.id">
            <td>
              <div class="avatar" :style="{ backgroundImage: `url(${m.avatar})` }"></div>
            </td>
            <td>{{ m.name }}</td>
            <td>{{ m.role }}</td>
            <td>{{ m.department }}</td>
            <td>{{ m.joinedAt }}</td>
          </tr>
        </tbody>
      </table>
    </section>

    <section class="card invite-card">
      <h2 class="section-title">邀请成员</h2>
      <div class="invite-row">
        <input
          v-model="inviteEmail"
          type="email"
          class="input"
          placeholder="输入邮箱"
        />
        <select v-model="inviteRole" class="select">
          <option value="">设置角色</option>
          <option value="管理员">管理员</option>
          <option value="编辑">编辑</option>
          <option value="只读">只读</option>
        </select>
        <button class="btn primary" @click="onInvite">发送邀请</button>
      </div>
      <p class="hint">当前仅为界面演示，点击按钮不会真正发送邮件。</p>
    </section>

    <section class="card roles-card">
      <h2 class="section-title">角色</h2>
      <div class="role-buttons">
        <button
          v-for="r in roles"
          :key="r.key"
          class="btn role-btn"
          :class="{ active: activeRole === r.key }"
          @click="activeRole = r.key"
        >
          {{ r.label }}
        </button>
      </div>
      <p class="role-desc">
        {{ activeRoleDesc }}
      </p>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';

interface Member {
  id: number;
  name: string;
  role: string;
  department: string;
  joinedAt: string;
  avatar: string;
}

const members = ref<Member[]>([
  {
    id: 1,
    name: '姓名',
    role: '管理员',
    department: '产品部',
    joinedAt: '2024-03-01',
    avatar: 'https://via.placeholder.com/40x40.png?text=A'
  }
]);

const inviteEmail = ref('');
const inviteRole = ref('');

function onInvite() {
  // 仅前端提示，不做真实提交
  if (!inviteEmail.value) {
    alert('请输入邮箱');
    return;
  }
  if (!inviteRole.value) {
    alert('请选择角色');
    return;
  }
  alert(`已模拟邀请 ${inviteEmail.value}，角色：${inviteRole.value}`);
  inviteEmail.value = '';
  inviteRole.value = '';
}

const roles = [
  { key: 'admin', label: '管理员', desc: '可管理成员与角色，拥有全部权限。' },
  { key: 'editor', label: '编辑', desc: '可新建和编辑文档，不能管理成员。' },
  { key: 'reader', label: '只读', desc: '只能查看文档，不能修改内容。' }
];

const activeRole = ref<'admin' | 'editor' | 'reader'>('admin');

const activeRoleDesc = computed(() => {
  const found = roles.find(r => r.key === activeRole.value);
  return found ? found.desc : '';
});
</script>

<style scoped>
.members-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.card {
  background-color: #ffffff;
  border-radius: 12px;
  border: 1px solid #e5e7eb;
  padding: 16px 18px;
}

.header-card {
  padding-bottom: 10px;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
}

.member-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
}

.member-table th,
.member-table td {
  padding: 10px 8px;
  text-align: left;
  border-bottom: 1px solid #f3f4f6;
}

.member-table thead {
  background-color: #f9fafb;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 999px;
  background-size: cover;
  background-position: center;
}

.invite-card .invite-row {
  display: flex;
  gap: 10px;
  align-items: center;
  margin-top: 8px;
}

.section-title {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 8px;
}

.input,
.select {
  padding: 8px 10px;
  border-radius: 8px;
  border: 1px solid #d1d5db;
  font-size: 14px;
}

.input {
  flex: 1;
}

.select {
  min-width: 140px;
}

.btn {
  padding: 8px 14px;
  border-radius: 999px;
  border: none;
  font-size: 13px;
  cursor: pointer;
  background-color: #e5e7eb;
  color: #111827;
}

.btn.primary {
  background-color: #2563eb;
  color: #ffffff;
}

.hint {
  margin-top: 6px;
  font-size: 12px;
  color: #9ca3af;
}

.roles-card .role-buttons {
  display: flex;
  gap: 10px;
  margin-bottom: 8px;
}

.role-btn {
  background-color: #e5e7eb;
}

.role-btn.active {
  background-color: #2563eb;
  color: #ffffff;
}

.role-desc {
  font-size: 13px;
  color: #4b5563;
}
</style>

