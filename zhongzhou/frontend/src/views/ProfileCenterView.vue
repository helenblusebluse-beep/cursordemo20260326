<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useLayoutStore } from '../stores/layout'
import { changeMyPassword, fetchMyProfile, saveMyProfile } from '../api/profile'
import type { ProfileInfo } from '../types/profile'

const router = useRouter()
const layoutStore = useLayoutStore()
const loading = ref(false)
const activeTab = ref<'basic' | 'password'>('basic')
const showPwdOld = ref(false)
const showPwdNew = ref(false)
const showPwdConfirm = ref(false)

const profile = ref<ProfileInfo | null>(null)
const basicForm = reactive({
  nickname: '',
  phone: '',
  email: '',
  gender: 1,
})
const pwdForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
})

function onMenuSelect(index: string): void {
  if (index === '1') return void router.push('/')
  if (index === '2-1') return void router.push('/appointments/register')
  if (index === '2-2') return void router.push('/visits/register')
  if (index === '3-1') return void router.push('/health-assessments')
  if (index === '3-2') return void router.push('/checkins')
  if (index === '3-3') return void router.push('/checkouts')
  if (index === '4-1') return void router.push('/room-types')
  if (index === '4-2') return void router.push('/bed-preview')
  if (index === '4-3') return void router.push('/smart-beds')
  if (index === '4-4') return void router.push('/contracts')
  if (index === '4-5') return void router.push('/customers')
  if (index === '6-1') return void router.push('/nursing-items')
  if (index === '6-2') return void router.push('/nursing-plans')
  if (index === '6-3') return void router.push('/nursing-care-levels')
  if (index === '6-4') return void router.push('/responsible-elders')
  if (index === '6-5') return void router.push('/task-schedules')
  if (index === '7-1') return void router.push('/orders')
  if (index === '7-2') return void router.push('/refunds')
  if (index === '8-1') return void router.push('/bills/1')
  if (index === '8-2') return void router.push('/prepay-recharges')
  if (index === '8-3') return void router.push('/balance-queries')
  if (index === '8-4') return void router.push('/arrears-elders')
  if (index === '9-1') return void router.push('/iot/devices')
  if (index === '9-2') return void router.push('/iot/alarm-rules')
  if (index === '9-3') return void router.push('/iot/alarm-records')
  if (index === '10-1') return void router.push('/profile')
}

async function loadProfile(): Promise<void> {
  loading.value = true
  try {
    const p = await fetchMyProfile()
    profile.value = p
    basicForm.nickname = p.nickname
    basicForm.phone = p.phone
    basicForm.email = p.email
    basicForm.gender = p.gender ?? 1
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  } finally {
    loading.value = false
  }
}

async function onSaveBasic(): Promise<void> {
  if (!basicForm.nickname.trim()) {
    ElMessage.warning('姓名不能为空')
    return
  }
  if (!/^1\d{10}$/.test(basicForm.phone.trim())) {
    ElMessage.warning('手机号格式错误')
    return
  }
  try {
    const p = await saveMyProfile({
      nickname: basicForm.nickname.trim(),
      phone: basicForm.phone.trim(),
      gender: basicForm.gender,
      avatarUrl: profile.value?.avatarUrl,
    })
    profile.value = p
    ElMessage.success('保存成功')
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '保存失败')
  }
}

function onCloseBasic(): void {
  if (!profile.value) return
  basicForm.nickname = profile.value.nickname
  basicForm.phone = profile.value.phone
  basicForm.email = profile.value.email
  basicForm.gender = profile.value.gender ?? 1
}

async function onSubmitPassword(): Promise<void> {
  if (!pwdForm.oldPassword.trim()) {
    ElMessage.warning('原密码不能为空')
    return
  }
  if (!pwdForm.newPassword.trim()) {
    ElMessage.warning('新密码不能为空')
    return
  }
  if (!pwdForm.confirmPassword.trim()) {
    ElMessage.warning('确认新密码不能为空')
    return
  }
  if (pwdForm.newPassword !== pwdForm.confirmPassword) {
    ElMessage.warning('新密码与确认新密码不一致，请重新输入')
    return
  }
  if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,20}$/.test(pwdForm.newPassword)) {
    ElMessage.warning('密码长度显示8-20位，且必须包含数字、小写字母、大写字母')
    return
  }
  try {
    await ElMessageBox.confirm('密码修改成功后，需重新登录，是否继续？', '确认修改', {
      confirmButtonText: '确定修改',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await changeMyPassword({
      oldPassword: pwdForm.oldPassword,
      newPassword: pwdForm.newPassword,
      confirmPassword: pwdForm.confirmPassword,
    })
    ElMessage.success('修改成功')
    void router.push('/')
  } catch (e) {
    if (e === 'cancel') return
    ElMessage.error(e instanceof Error ? e.message : '修改失败')
  }
}

function onLogout(): void {
  ElMessage.success('已退出登录')
  void router.push('/')
}

onMounted(() => {
  void loadProfile()
})
</script>

<template>
  <el-container class="page-root">
    <el-aside :width="layoutStore.collapsed ? '72px' : '220px'" class="sider">
      <div class="brand"><img src="https://dummyimage.com/36x36/8ec96b/ffffff&text=中" alt="logo" /><div v-if="!layoutStore.collapsed" class="brand-text">中州养老</div></div>
      <el-menu :collapse="layoutStore.collapsed" default-active="10-1" class="menu" @select="onMenuSelect">
        <el-menu-item index="1">首页</el-menu-item>
        <el-sub-menu index="2"><template #title>来访管理</template><el-menu-item index="2-1">预约登记</el-menu-item><el-menu-item index="2-2">来访登记</el-menu-item></el-sub-menu>
        <el-sub-menu index="3"><template #title>入退管理</template><el-menu-item index="3-1">健康评估</el-menu-item><el-menu-item index="3-2">入住办理</el-menu-item><el-menu-item index="3-3">退住办理</el-menu-item></el-sub-menu>
        <el-sub-menu index="4"><template #title>在住管理</template><el-menu-item index="4-1">房型设置</el-menu-item><el-menu-item index="4-2">床位预览</el-menu-item><el-menu-item index="4-3">智能床位</el-menu-item><el-menu-item index="4-4">合同跟踪</el-menu-item><el-menu-item index="4-5">客户管理</el-menu-item></el-sub-menu>
        <el-sub-menu index="6"><template #title>服务管理</template><el-menu-item index="6-1">护理项目</el-menu-item><el-menu-item index="6-2">护理计划</el-menu-item><el-menu-item index="6-3">护理等级</el-menu-item><el-menu-item index="6-4">负责老人</el-menu-item><el-menu-item index="6-5">任务安排</el-menu-item></el-sub-menu>
        <el-sub-menu index="7"><template #title>订单查询</template><el-menu-item index="7-1">订单管理</el-menu-item><el-menu-item index="7-2">退款管理</el-menu-item></el-sub-menu>
        <el-sub-menu index="8"><template #title>财务统计</template><el-menu-item index="8-1">账单管理</el-menu-item><el-menu-item index="8-2">预缴款充值</el-menu-item><el-menu-item index="8-3">余额查询</el-menu-item><el-menu-item index="8-4">欠费老人</el-menu-item></el-sub-menu>
        <el-sub-menu index="9"><template #title>智能检测</template><el-menu-item index="9-1">设备管理</el-menu-item><el-menu-item index="9-2">报警规则</el-menu-item><el-menu-item index="9-3">报警数据</el-menu-item></el-sub-menu>
        <el-sub-menu index="10"><template #title>个人中心</template><el-menu-item index="10-1">个人信息</el-menu-item></el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <el-button text @click="layoutStore.toggleCollapsed">☰</el-button>
        <div class="crumb">首页 &gt; 页面三 &gt; 页面三</div>
        <el-dropdown>
          <span class="admin-dd">管理员</span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="onMenuSelect('10-1')">个人中心</el-dropdown-item>
              <el-dropdown-item @click="onLogout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <el-main class="main" v-loading="loading">
        <div class="profile-wrap" v-if="profile">
          <el-card class="left-card" shadow="never">
            <template #header>个人信息</template>
            <div class="avatar-wrap">
              <el-avatar :size="88" :src="profile.avatarUrl" />
            </div>
            <div class="nickname">{{ profile.nickname }}</div>
            <div class="line">{{ profile.phone }}</div>
            <div class="line">{{ profile.email }}</div>
            <div class="line">{{ profile.department }}/{{ profile.positionTitle }}</div>
            <div class="line">{{ profile.roleName }}</div>
            <div class="line">{{ profile.createdTime }}</div>
          </el-card>

          <el-card class="right-card" shadow="never">
            <template #header>基本资料</template>
            <el-tabs v-model="activeTab">
              <el-tab-pane label="基本资料" name="basic">
                <el-form label-width="90px" class="form">
                  <el-form-item label="姓名">
                    <el-input v-model="basicForm.nickname" maxlength="10" show-word-limit />
                  </el-form-item>
                  <el-form-item label="*手机号">
                    <el-input v-model="basicForm.phone" maxlength="11" show-word-limit />
                  </el-form-item>
                  <el-form-item label="*邮箱">
                    <el-input v-model="basicForm.email" disabled />
                  </el-form-item>
                  <el-form-item label="性别">
                    <el-radio-group v-model="basicForm.gender">
                      <el-radio :label="1">男</el-radio>
                      <el-radio :label="2">女</el-radio>
                    </el-radio-group>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="onSaveBasic">保存</el-button>
                    <el-button type="danger" @click="onCloseBasic">关闭</el-button>
                  </el-form-item>
                </el-form>
              </el-tab-pane>

              <el-tab-pane label="修改密码" name="password">
                <el-form label-width="90px" class="form">
                  <el-form-item label="*原密码">
                    <el-input :type="showPwdOld ? 'text' : 'password'" v-model="pwdForm.oldPassword">
                      <template #suffix>
                        <el-button text @click="showPwdOld = !showPwdOld">{{ showPwdOld ? '隐' : '显' }}</el-button>
                      </template>
                    </el-input>
                  </el-form-item>
                  <el-form-item label="*新密码">
                    <el-input :type="showPwdNew ? 'text' : 'password'" v-model="pwdForm.newPassword">
                      <template #suffix>
                        <el-button text @click="showPwdNew = !showPwdNew">{{ showPwdNew ? '隐' : '显' }}</el-button>
                      </template>
                    </el-input>
                  </el-form-item>
                  <el-form-item label="*确认新密码">
                    <el-input :type="showPwdConfirm ? 'text' : 'password'" v-model="pwdForm.confirmPassword">
                      <template #suffix>
                        <el-button text @click="showPwdConfirm = !showPwdConfirm">{{ showPwdConfirm ? '隐' : '显' }}</el-button>
                      </template>
                    </el-input>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="onSubmitPassword">确定</el-button>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.page-root { min-height: 100vh; background: #f5f7fa; }
.sider { background: #fff; color: #303133; transition: width .2s; border-right: 1px solid #ebeef5; }
.brand { height: 60px; display: flex; align-items: center; gap: 8px; padding: 0 12px; border-bottom: 1px solid #ebeef5; }
.brand img { border-radius: 50%; }
.brand-text { font-weight: 700; }
.menu { border-right: none; background: #fff; color: #303133; }
:deep(.menu .el-menu-item), :deep(.menu .el-sub-menu__title) { color: #303133; }
:deep(.menu .el-menu-item:hover), :deep(.menu .el-sub-menu__title:hover) { background: #f5f7fa; color: #409eff; }
:deep(.menu .el-menu-item.is-active) { background: #ecf5ff; color: #409eff; font-weight: 600; }
.header { height: 56px; background: #fff; display: flex; align-items: center; border-bottom: 1px solid #ebeef5; }
.crumb { margin-left: 12px; color: #606266; flex: 1; }
.admin-dd { color: #606266; cursor: pointer; }
.main { padding: 12px; }
.profile-wrap { display: flex; gap: 12px; }
.left-card { width: 280px; flex-shrink: 0; }
.right-card { flex: 1; min-width: 0; }
.avatar-wrap { display: flex; justify-content: center; margin: 12px 0; }
.nickname { text-align: center; font-size: 20px; margin-bottom: 16px; }
.line { text-align: center; color: #606266; margin: 8px 0; }
.form { max-width: 420px; }
</style>

