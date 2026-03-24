<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { login } from '@/api/login'

const route = useRoute()
const router = useRouter()

const formRef = ref<FormInstance | null>(null)
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
})

const rules: FormRules = {
  username: [{ required: true, message: '请输入员工用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

function onCancel() {
  form.username = ''
  form.password = ''
  formRef.value?.clearValidate()
}

async function onSubmit() {
  const fr = formRef.value
  if (!fr) return
  await fr.validate()
  loading.value = true
  try {
    await login(form.username.trim(), form.password)
    const redirect = typeof route.query.redirect === 'string' && route.query.redirect ? route.query.redirect : '/home'
    await router.replace(redirect)
  } catch (e) {
    const msg = e instanceof Error ? e.message : '登录失败'
    ElMessage.error(msg)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <div class="login-shell">
      <aside class="login-hero" aria-hidden="true">
        <div class="login-hero__bg" />
        <div class="login-hero__content">
          <div class="float-tag float-tag--1">务实</div>
          <div class="float-tag float-tag--2">育人</div>
          <div class="float-tag float-tag--3">创新</div>
          <div class="float-tag float-tag--4">责任</div>
          <div class="iso-scene">
            <div class="iso-block iso-block--a" />
            <div class="iso-block iso-block--b" />
            <div class="iso-figure iso-figure--1" />
            <div class="iso-figure iso-figure--2" />
            <div class="iso-figure iso-figure--3" />
          </div>
        </div>
      </aside>

      <section class="login-panel">
        <div class="login-panel__inner">
          <h1 class="login-title">烛龙平台-教学管理系统</h1>
          <el-form ref="formRef" :model="form" :rules="rules" label-width="72px" class="login-form" @submit.prevent="onSubmit">
            <el-form-item label="用户名" prop="username" required>
              <el-input v-model="form.username" placeholder="请输入员工用户名" autocomplete="username" clearable />
            </el-form-item>
            <el-form-item label="密码" prop="password" required>
              <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password autocomplete="current-password" />
            </el-form-item>
            <el-form-item class="login-form__actions">
              <el-button type="primary" :loading="loading" native-type="submit">登录</el-button>
              <el-button @click="onCancel">取消</el-button>
            </el-form-item>
          </el-form>
        </div>
      </section>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  padding: 24px;
  box-sizing: border-box;
}

.login-shell {
  display: flex;
  width: min(960px, 100%);
  min-height: 420px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.06);
}

.login-hero {
  flex: 1.1;
  position: relative;
  background: linear-gradient(160deg, #e8f4ff 0%, #f0f9ff 45%, #e0f2fe 100%);
  min-height: 360px;
}

.login-hero__bg {
  position: absolute;
  inset: 0;
  opacity: 0.35;
  background:
    radial-gradient(circle at 20% 30%, rgba(64, 158, 255, 0.25) 0%, transparent 45%),
    radial-gradient(circle at 80% 70%, rgba(64, 158, 255, 0.2) 0%, transparent 40%);
}

.login-hero__content {
  position: relative;
  height: 100%;
  min-height: 360px;
}

.float-tag {
  position: absolute;
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 13px;
  color: #409eff;
  background: rgba(255, 255, 255, 0.85);
  border: 1px solid rgba(64, 158, 255, 0.35);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.12);
}
.float-tag--1 { top: 18%; left: 12%; }
.float-tag--2 { top: 28%; right: 14%; }
.float-tag--3 { bottom: 32%; left: 16%; }
.float-tag--4 { bottom: 22%; right: 12%; }

.iso-scene {
  position: absolute;
  left: 50%;
  top: 52%;
  transform: translate(-50%, -50%) rotateX(12deg) rotateZ(-8deg);
  width: 220px;
  height: 200px;
}

.iso-block {
  position: absolute;
  border-radius: 6px;
  opacity: 0.9;
}
.iso-block--a {
  width: 120px;
  height: 72px;
  left: 40px;
  top: 80px;
  background: linear-gradient(135deg, #93c5fd 0%, #60a5fa 100%);
  transform: skewY(-8deg);
  box-shadow: 8px 12px 0 rgba(37, 99, 235, 0.15);
}
.iso-block--b {
  width: 100px;
  height: 56px;
  left: 90px;
  top: 120px;
  background: linear-gradient(135deg, #bfdbfe 0%, #93c5fd 100%);
  transform: skewY(-8deg);
  opacity: 0.85;
}

.iso-figure {
  position: absolute;
  width: 36px;
  height: 48px;
  border-radius: 8px 8px 4px 4px;
  background: linear-gradient(180deg, #38bdf8 0%, #2563eb 100%);
  box-shadow: 4px 4px 0 rgba(37, 99, 235, 0.2);
}
.iso-figure--1 { left: 70px; top: 40px; transform: skewY(-6deg); }
.iso-figure--2 { left: 120px; top: 56px; transform: skewY(-6deg); height: 44px; width: 32px; background: linear-gradient(180deg, #7dd3fc 0%, #3b82f6 100%); }
.iso-figure--3 { left: 48px; top: 100px; transform: skewY(-6deg); width: 30px; height: 40px; background: linear-gradient(180deg, #bae6fd 0%, #0ea5e9 100%); }

.login-panel {
  flex: 0.95;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(180deg, #f7fbff 0%, #ffffff 100%);
  padding: 32px 28px;
}

.login-panel__inner {
  width: 100%;
  max-width: 360px;
}

.login-title {
  margin: 0 0 28px;
  font-size: 22px;
  font-weight: 700;
  color: #303133;
  text-align: center;
  letter-spacing: 0.02em;
}

.login-form :deep(.el-input) {
  width: 100%;
}

.login-form__actions :deep(.el-form-item__content) {
  display: flex;
  gap: 12px;
  justify-content: flex-start;
}

.login-form__actions .el-button--primary {
  min-width: 96px;
}

.login-form__actions .el-button:not(.el-button--primary) {
  min-width: 96px;
  background: #e4e7ed;
  border-color: #e4e7ed;
  color: #606266;
}

.login-form__actions .el-button:not(.el-button--primary):hover {
  background: #dcdfe6;
  border-color: #dcdfe6;
  color: #303133;
}
</style>
