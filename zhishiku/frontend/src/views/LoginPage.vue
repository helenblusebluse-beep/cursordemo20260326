<template>
  <div class="login-page">
    <header class="brand">Figma</header>
    <main class="main">
      <section class="card">
        <h1 class="title">知识库</h1>

        <form class="form" @submit.prevent="onSubmit">
          <div class="field">
            <input
              v-model="email"
              type="email"
              required
              class="input"
              placeholder="邮箱输入"
            />
          </div>
          <div class="field">
            <input
              v-model="password"
              type="password"
              required
              class="input"
              placeholder="密码"
            />
          </div>

          <div class="helper-row">
            <label class="remember">
              <input v-model="remember" type="checkbox" />
              <span>记住我</span>
            </label>
            <button class="link" type="button">忘记密码</button>
          </div>

          <button class="btn primary" type="submit">登录</button>

          <div class="sso-row">
            <button class="btn ghost" type="button">企业SSO</button>
            <button class="btn ghost" type="button">第三方登录</button>
          </div>
        </form>

        <footer class="footer">
          © 2024 KnowledgeBase System
        </footer>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const email = ref('');
const password = ref('');
const remember = ref(false);

function onSubmit() {
  if (!email.value || !password.value) return;
  router.push('/');
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  padding: 40px 24px 32px;
  background: #f9fafb;
  color: #111827;
}

.brand {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 40px;
}

.main {
  display: flex;
  justify-content: center;
}

.card {
  width: 360px;
  background: #ffffff;
  border-radius: 16px;
  padding: 32px 30px 26px;
  box-shadow: 0 18px 40px rgba(15, 23, 42, 0.08);
}

.title {
  font-size: 26px;
  font-weight: 700;
  text-align: center;
  margin-bottom: 24px;
}

.form {
  display: flex;
  flex-direction: column;
}

.field {
  margin-bottom: 14px;
}

.input {
  width: 100%;
  padding: 8px 10px;
  border-radius: 999px;
  border: 1px solid #d1d5db;
  font-size: 14px;
  background-color: #ffffff;
}

.helper-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 12px;
  margin-bottom: 16px;
}

.remember {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.link {
  border: none;
  background: transparent;
  font-size: 12px;
  color: #2563eb;
  cursor: pointer;
  padding: 0;
}

.btn {
  width: 100%;
  padding: 9px 12px;
  border-radius: 999px;
  border: 1px solid transparent;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
}

.btn.primary {
  background: linear-gradient(135deg, #2563eb, #1d4ed8);
  color: #ffffff;
  margin-bottom: 10px;
}

.btn.ghost {
  background: #ffffff;
  color: #111827;
  border-color: #d1d5db;
}

.sso-row {
  display: flex;
  gap: 10px;
  margin-top: 4px;
}

.footer {
  margin-top: 18px;
  text-align: center;
  font-size: 12px;
  color: #9ca3af;
}
</style>

