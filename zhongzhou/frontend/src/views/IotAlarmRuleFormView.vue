<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useLayoutStore } from '../stores/layout'
import { showToast } from '../utils/ui'
import {
  fetchIotAlarmRuleDetail,
  fetchIotDeviceOptions,
  fetchIotAlarmRuleFunctionOptions,
  fetchIotAlarmRuleModuleOptions,
  fetchIotProducts,
  saveIotAlarmRule,
} from '../api/iot'
import type { IotProductItem } from '../types/iot'

const route = useRoute()
const router = useRouter()
const layoutStore = useLayoutStore()
const loading = ref(false)
const saving = ref(false)
const productOptions = ref<IotProductItem[]>([])
const deviceOptions = ref<{ id: number; deviceName: string; productId: number }[]>([])
const moduleOptions = ref<string[]>([])
const functionOptions = ref<string[]>([])

const ruleId = computed(() => {
  const id = route.params.id
  if (typeof id === 'string' && /^\d+$/.test(id)) return Number(id)
  return null
})
const isEdit = computed(() => ruleId.value != null)

const form = reactive({
  productId: undefined as number | undefined,
  moduleName: '',
  functionName: '',
  deviceId: undefined as number | undefined,
  dataType: 'elder',
  ruleName: '',
  persistCycles: 3,
  compareType: 'LT',
  thresholdValue: 0,
  timeStart: '00:00:00',
  timeEnd: '23:59:00',
  muteCycleMinutes: 5,
  enabled: 1,
})

const compareOptions = [
  { label: '>', value: 'GT' },
  { label: '<', value: 'LT' },
]
const cycleOptions = [
  { label: '持续1个周期（1周期=1分钟）', value: 1 },
  { label: '持续3个周期（1周期=1分钟）', value: 3 },
  { label: '持续5个周期（1周期=1分钟）', value: 5 },
  { label: '持续10个周期（1周期=1分钟）', value: 10 },
  { label: '持续20个周期（1周期=1分钟）', value: 20 },
  { label: '持续30个周期（1周期=1分钟）', value: 30 },
]
const muteCycleOptions = [
  { label: '5分钟', value: 5 },
  { label: '10分钟', value: 10 },
  { label: '15分钟', value: 15 },
  { label: '30分钟', value: 30 },
  { label: '60分钟', value: 60 },
  { label: '3小时', value: 180 },
  { label: '6小时', value: 360 },
  { label: '12小时', value: 720 },
  { label: '24小时', value: 1440 },
]

const filteredDevices = computed(() => {
  if (form.productId == null) return []
  return deviceOptions.value.filter((d) => d.productId === form.productId)
})

function onMenuSelect(index: string): void {
  if (index === '1') return void router.push('/')
  if (index === '9-1') return void router.push('/iot/devices')
  if (index === '9-2') return void router.push('/iot/alarm-rules')
  if (index === '9-3') return void router.push('/iot/alarm-records')
  if (index === '10-1') return void router.push('/profile')
}

async function loadProducts(): Promise<void> {
  try {
    productOptions.value = await fetchIotProducts()
  } catch {
    productOptions.value = []
  }
}

async function loadDevices(): Promise<void> {
  try {
    deviceOptions.value = await fetchIotDeviceOptions()
  } catch {
    deviceOptions.value = []
  }
}

async function loadDetail(): Promise<void> {
  if (ruleId.value == null) return
  loading.value = true
  try {
    const d = await fetchIotAlarmRuleDetail(ruleId.value)
    form.productId = d.productId
    await loadModulesByProduct(d.productId)
    form.moduleName = d.moduleName || ''
    if (form.moduleName) {
      await loadFunctionsByModule(d.productId, form.moduleName)
    }
    form.functionName = d.functionName
    form.deviceId = d.deviceId === null ? -1 : d.deviceId
    form.dataType = d.dataType === 'device' ? 'device' : 'elder'
    form.ruleName = d.ruleName
    form.compareType = d.compareType
    form.thresholdValue = Number(d.thresholdValue)
    form.persistCycles = d.persistCycles
    form.timeStart = d.timeStart || '00:00:00'
    form.timeEnd = d.timeEnd || '23:59:00'
    form.muteCycleMinutes = d.muteCycleMinutes || 5
    form.enabled = d.enabled
  } catch (e) {
    showToast.error(e instanceof Error ? e.message : '加载失败')
    void router.push('/iot/alarm-rules')
  } finally {
    loading.value = false
  }
}

async function loadModulesByProduct(productId: number): Promise<void> {
  try {
    moduleOptions.value = await fetchIotAlarmRuleModuleOptions(productId)
  } catch {
    moduleOptions.value = []
  }
}

async function loadFunctionsByModule(productId: number, moduleName: string): Promise<void> {
  try {
    functionOptions.value = await fetchIotAlarmRuleFunctionOptions(productId, moduleName)
  } catch {
    functionOptions.value = []
  }
}

watch(
  () => form.productId,
  async (v) => {
    form.moduleName = ''
    form.functionName = ''
    moduleOptions.value = []
    functionOptions.value = []
    if (v == null) {
      form.deviceId = undefined
      return
    }
    const ok = form.deviceId === -1 || filteredDevices.value.some((d) => d.id === form.deviceId)
    if (!ok) form.deviceId = undefined
    await loadModulesByProduct(v)
  },
)

watch(
  () => form.moduleName,
  async (v) => {
    form.functionName = ''
    functionOptions.value = []
    if (!v || form.productId == null) return
    await loadFunctionsByModule(form.productId, v)
  },
)

function goBack(): void {
  void router.push('/iot/alarm-rules')
}

async function submit(): Promise<void> {
  if (form.productId == null) return showToast.warning('请选择所属产品')
  if (!form.moduleName) return showToast.warning('请选择模块')
  if (!form.functionName) return showToast.warning('请选择功能名称')
  if (form.deviceId == null) return showToast.warning('请选择关联设备')
  if (!form.ruleName.trim()) return showToast.warning('请填写报警规则名称')
  if (!/^[\u4e00-\u9fa5A-Za-z0-9]+$/.test(form.ruleName.trim())) {
    return showToast.warning('报警规则名称不能包含特殊符号')
  }
  if (form.ruleName.trim().length > 20) {
    return showToast.warning('报警规则名称不能超过20个字符')
  }
  if (form.persistCycles < 1) return showToast.warning('请选择持续周期')
  if (form.thresholdValue < 0) return showToast.warning('阈值不能小于0')
  if (!form.timeStart || !form.timeEnd) return showToast.warning('请选择报警生效时段')
  if (!form.muteCycleMinutes) return showToast.warning('请选择报警沉默周期')
  saving.value = true
  try {
    await saveIotAlarmRule(isEdit.value ? ruleId.value! : undefined, {
      ruleName: form.ruleName.trim(),
      productId: form.productId,
      moduleName: form.moduleName,
      deviceId: form.deviceId === -1 ? null : form.deviceId,
      functionName: form.functionName,
      dataType: form.dataType,
      compareType: form.compareType,
      thresholdValue: Number(form.thresholdValue.toFixed(2)),
      persistCycles: form.persistCycles,
      timeStart: form.timeStart,
      timeEnd: form.timeEnd,
      muteCycleMinutes: form.muteCycleMinutes,
      enabled: form.enabled,
    })
    showToast.success('保存成功')
    void router.push('/iot/alarm-rules')
  } catch (e) {
    showToast.error(e instanceof Error ? e.message : '保存失败')
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  void loadProducts()
  void loadDevices()
  if (isEdit.value) void loadDetail()
})
</script>

<template>
  <el-container class="page-root">
    <el-aside :width="layoutStore.collapsed ? '72px' : '220px'" class="sider">
      <div class="brand"><img src="https://dummyimage.com/36x36/8ec96b/ffffff&text=中" alt="logo" /><div v-if="!layoutStore.collapsed" class="brand-text">中州养老</div></div>
      <el-menu :collapse="layoutStore.collapsed" default-active="9-2" class="menu" @select="onMenuSelect">
        <el-menu-item index="1">首页</el-menu-item>
        <el-sub-menu index="9"><template #title>智能检测</template><el-menu-item index="9-1">设备管理</el-menu-item><el-menu-item index="9-2">报警规则</el-menu-item><el-menu-item index="9-3">报警数据</el-menu-item></el-sub-menu>
        <el-sub-menu index="10"><template #title>个人中心</template><el-menu-item index="10-1">个人信息</el-menu-item></el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <el-button text @click="layoutStore.toggleCollapsed">☰</el-button>
        <div class="crumb">智能检测 &gt; 报警规则 &gt; {{ isEdit ? '修改报警规则' : '新增报警规则' }}</div>
        <div class="admin">管理员</div>
      </el-header>
      <el-main class="main" v-loading="loading">
        <el-card shadow="never" class="form-card">
          <template #header><span class="card-title">{{ isEdit ? '修改报警规则' : '新增报警规则' }}</span></template>
          <el-form label-width="120px" class="rule-form">
            <el-form-item label="所属产品" required>
              <el-select v-model="form.productId" placeholder="请选择" clearable style="width: 400px">
                <el-option v-for="p in productOptions" :key="p.id" :label="p.productName" :value="p.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="模块" required>
              <el-select v-model="form.moduleName" placeholder="请选择" clearable style="width: 400px" :disabled="form.productId == null">
                <el-option v-for="m in moduleOptions" :key="m" :label="m" :value="m" />
              </el-select>
            </el-form-item>
            <el-form-item label="功能名称" required>
              <el-select v-model="form.functionName" placeholder="请选择" clearable style="width: 400px" :disabled="!form.moduleName">
                <el-option v-for="f in functionOptions" :key="f" :label="f" :value="f" />
              </el-select>
            </el-form-item>
            <el-form-item label="关联设备" required>
              <el-select v-model="form.deviceId" placeholder="请选择" style="width: 400px" :disabled="form.productId == null">
                <el-option :value="-1" label="全部设备" />
                <el-option v-for="d in filteredDevices" :key="d.id" :label="d.deviceName" :value="d.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="报警数据类型" required>
              <el-radio-group v-model="form.dataType">
                <el-radio label="elder">老人异常数据</el-radio>
                <el-radio label="device">设备异常数据</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="报警规则名称" required>
              <el-input v-model="form.ruleName" placeholder="请输入" maxlength="20" show-word-limit style="max-width: 400px" />
            </el-form-item>
            <el-form-item label="持续周期" required>
              <el-select v-model="form.persistCycles" placeholder="请选择" style="width: 400px">
                <el-option v-for="c in cycleOptions" :key="c.value" :label="c.label" :value="c.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="运算符" required>
              <el-select v-model="form.compareType" style="width: 400px">
                <el-option v-for="o in compareOptions" :key="o.value" :label="o.label" :value="o.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="阈值" required>
              <el-input-number v-model="form.thresholdValue" :precision="2" :step="0.01" :min="0" style="width: 400px" />
            </el-form-item>
            <el-form-item label="报警生效时段" required>
              <el-time-picker v-model="form.timeStart" value-format="HH:mm:ss" format="HH:mm:ss" placeholder="开始" style="width: 160px" />
              <span class="time-sep">-</span>
              <el-time-picker v-model="form.timeEnd" value-format="HH:mm:ss" format="HH:mm:ss" placeholder="结束" style="width: 160px" />
            </el-form-item>
            <el-form-item label="报警沉默周期" required>
              <el-select v-model="form.muteCycleMinutes" placeholder="请选择" style="width: 400px">
                <el-option v-for="m in muteCycleOptions" :key="m.value" :label="m.label" :value="m.value" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button @click="goBack">返回</el-button>
              <el-button type="primary" :loading="saving" @click="submit">保存</el-button>
            </el-form-item>
          </el-form>
        </el-card>
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
.header { height: 56px; background: #fff; display: flex; align-items: center; border-bottom: 1px solid #ebeef5; }
.crumb { margin-left: 12px; color: #606266; flex: 1; }
.admin { color: #909399; font-size: 13px; }
.main { padding: 12px; }
.form-card { max-width: 720px; border-radius: 4px; }
.card-title { font-weight: 600; font-size: 15px; }
.time-sep { margin: 0 8px; color: #909399; }
.rule-form { padding-top: 8px; }
</style>
