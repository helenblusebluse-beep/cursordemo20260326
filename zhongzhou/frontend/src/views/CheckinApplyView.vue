<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { InfoFilled } from '@element-plus/icons-vue'
import { useRoute, useRouter } from 'vue-router'
import { applyCheckin, getCheckinDetail, queryCareLevelOptions, updateCheckinFamily } from '../api/checkin'
import { useLayoutStore } from '../stores/layout'
import type { CareLevelOption } from '../types/checkin'

const router = useRouter()
const route = useRoute()
const layoutStore = useLayoutStore()
const formRef = ref<FormInstance>()
const loading = ref(false)
const submitting = ref(false)
const savingFamily = ref(false)
const previewVisible = ref(false)
const readonly = computed(() => route.path !== '/checkins/apply')
const activeMenu = computed(() => '3-2')

const relationOptions = ['子女', '配偶', '亲属', '朋友', '社工', '其他']
const careOptions = ref<CareLevelOption[]>([])
const bedOptions = ['A区-101', 'A区-102', 'B区-201', 'B区-202']
const bedFeeMap: Record<string, number> = { 'A区-101': 1500, 'A区-102': 1600, 'B区-201': 1700, 'B区-202': 1800 }

const form = reactive({
  elderName: '',
  idCard: '',
  birthDate: '',
  age: '',
  gender: '',
  contactPhone: '',
  homeAddress: '',
  profilePhotoName: '',
  idCardFrontName: '',
  idCardBackName: '',
  checkinStartDate: '',
  checkinEndDate: '',
  feeStartDate: '',
  feeEndDate: '',
  careLevel: '',
  roomNo: '',
  deposit: 3000,
  nursingFee: 0,
  bedFee: 0,
  otherFee: 0,
  medicalFee: 0,
  subsidyFee: 0,
  contractName: '',
  signDate: '',
  payerName: '',
  payerContact: '',
  contractFileName: '',
  familyMembers: [{ familyName: '', familyContact: '', relationType: '' }],
})

const billDays = computed(() => {
  if (!form.feeStartDate || !form.feeEndDate) return 0
  const start = new Date(form.feeStartDate).getTime()
  const end = new Date(form.feeEndDate).getTime()
  if (end < start) return 0
  return Math.floor((end - start) / (24 * 3600 * 1000)) + 1
})
const monthlyAmount = computed(() => Number((form.nursingFee + form.bedFee + form.otherFee).toFixed(2)))
const currentAmount = computed(() => Number(((monthlyAmount.value * billDays.value) / 30).toFixed(2)))
const billAmount = computed(() => Number((currentAmount.value + form.deposit + form.medicalFee - form.subsidyFee).toFixed(2)))

const rules = {
  elderName: [{ required: true, message: '请输入老人姓名', trigger: 'blur' }],
  idCard: [{ required: true, pattern: /^[0-9]{17}[0-9Xx]$/, message: '身份证号格式错误', trigger: 'blur' }],
  contactPhone: [{ required: true, pattern: /^1\d{10}$/, message: '手机号格式错误', trigger: 'blur' }],
  homeAddress: [{ required: true, message: '请输入家庭地址', trigger: 'blur' }],
  checkinStartDate: [
    {
      validator: (
        _rule: unknown,
        _value: unknown,
        callback: (e?: Error | string) => void,
      ) => {
        if (!form.checkinStartDate || !form.checkinEndDate) {
          callback(new Error('请选择入住期限'))
        } else {
          callback()
        }
      },
      trigger: 'change',
    },
  ],
  feeStartDate: [
    {
      validator: (
        _rule: unknown,
        _value: unknown,
        callback: (e?: Error | string) => void,
      ) => {
        if (!form.feeStartDate || !form.feeEndDate) {
          callback(new Error('请选择费用期限'))
        } else {
          callback()
        }
      },
      trigger: 'change',
    },
  ],
  careLevel: [{ required: true, message: '请选择护理等级', trigger: 'change' }],
  roomNo: [{ required: true, message: '请选择入住床位', trigger: 'change' }],
  bedFee: [
    {
      validator: (
        _rule: unknown,
        _value: unknown,
        callback: (e?: Error | string) => void,
      ) => {
        if (form.bedFee == null || Number.isNaN(Number(form.bedFee))) {
          callback(new Error('请输入床位费用'))
        } else {
          callback()
        }
      },
      trigger: 'change',
    },
  ],
  nursingFee: [
    {
      validator: (
        _rule: unknown,
        _value: unknown,
        callback: (e?: Error | string) => void,
      ) => {
        if (form.nursingFee == null || Number.isNaN(Number(form.nursingFee))) {
          callback(new Error('请输入护理费用'))
        } else {
          callback()
        }
      },
      trigger: 'change',
    },
  ],
  contractName: [{ required: true, message: '请输入合同名称', trigger: 'blur' }],
  signDate: [{ required: true, message: '请选择签约日期', trigger: 'change' }],
}

const checkinDateRangeModel = computed({
  get(): [string, string] | undefined {
    if (form.checkinStartDate && form.checkinEndDate) {
      return [form.checkinStartDate, form.checkinEndDate]
    }
    return undefined
  },
  set(v: [string, string] | undefined | null) {
    if (!v || v.length < 2) {
      form.checkinStartDate = ''
      form.checkinEndDate = ''
      return
    }
    form.checkinStartDate = v[0]
    form.checkinEndDate = v[1]
  },
})

const feeDateRangeModel = computed({
  get(): [string, string] | undefined {
    if (form.feeStartDate && form.feeEndDate) {
      return [form.feeStartDate, form.feeEndDate]
    }
    return undefined
  },
  set(v: [string, string] | undefined | null) {
    if (!v || v.length < 2) {
      form.feeStartDate = ''
      form.feeEndDate = ''
      return
    }
    form.feeStartDate = v[0]
    form.feeEndDate = v[1]
  },
})

function parseIdCard(): void {
  if (!/^[0-9]{17}[0-9Xx]$/.test(form.idCard)) return
  const b = form.idCard.substring(6, 14)
  form.birthDate = `${b.substring(0, 4)}-${b.substring(4, 6)}-${b.substring(6, 8)}`
  const nowYear = new Date().getFullYear()
  form.age = String(nowYear - Number(b.substring(0, 4)))
  form.gender = Number(form.idCard[16]) % 2 === 0 ? '女' : '男'
}
function onCareOrBedChange(): void {
  if (form.careLevel) {
    const selected = careOptions.value.find((x) => x.levelName === form.careLevel)
    form.nursingFee = selected?.nursingFee ?? 0
  }
  if (form.roomNo) form.bedFee = bedFeeMap[form.roomNo] ?? 0
}
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
}
function addFamilyRow(): void { form.familyMembers.push({ familyName: '', familyContact: '', relationType: '' }) }
function removeFamilyRow(index: number): void { if (form.familyMembers.length > 1) form.familyMembers.splice(index, 1) }
function disabledPast(date: Date): boolean { return date.getTime() < Date.now() - 24 * 3600 * 1000 }
async function saveFamily(): Promise<void> {
  if (!readonly.value) return
  const id = Number(route.params.id)
  const invalidFamily = form.familyMembers.some((v) => !v.familyName || !/^1\d{10}$/.test(v.familyContact) || !v.relationType)
  if (invalidFamily) {
    ElMessage.error('家属信息不完整，请完善后保存')
    return
  }
  savingFamily.value = true
  try {
    await updateCheckinFamily(id, form.familyMembers)
    ElMessage.success('家属信息保存成功')
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '保存失败')
  } finally {
    savingFamily.value = false
  }
}

async function onSubmit(): Promise<void> {
  if (readonly.value) return
  if (!formRef.value) return
  await formRef.value.validate()
  const invalidFamily = form.familyMembers.some((v) => !v.familyName || !/^1\d{10}$/.test(v.familyContact) || !v.relationType)
  if (invalidFamily) {
    ElMessage.error('家属信息不完整，请输入家属信息')
    return
  }
  submitting.value = true
  try {
    const id = await applyCheckin({
      elderName: form.elderName,
      idCard: form.idCard,
      contactPhone: form.contactPhone,
      homeAddress: form.homeAddress,
      profilePhotoName: form.profilePhotoName || 'profile.jpg',
      idCardFrontName: form.idCardFrontName || 'id-front.jpg',
      idCardBackName: form.idCardBackName || 'id-back.jpg',
      checkinStartDate: form.checkinStartDate,
      feeStartDate: form.feeStartDate,
      roomNo: form.roomNo,
      careLevel: form.careLevel,
      nursingFee: String(form.nursingFee),
      bedFee: String(form.bedFee),
      contractName: form.contractName,
      signDate: form.signDate,
      payerName: form.payerName,
      payerContact: form.payerContact,
      contractFileName: form.contractFileName || 'contract.pdf',
      familyMembers: form.familyMembers,
      checkinPeriod: `${form.checkinStartDate}~${form.checkinEndDate}`,
    })
    ElMessage.success('入住成功')
    void router.push(`/checkins/${id}`)
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '提交失败')
  } finally {
    submitting.value = false
  }
}
async function loadDetail(): Promise<void> {
  if (!readonly.value) return
  loading.value = true
  try {
    const id = Number(route.params.id)
    const d = await getCheckinDetail(id)
    form.elderName = d.elderName
    form.idCard = d.idCard
    form.birthDate = d.birthDate
    form.age = String(d.age)
    form.gender = d.gender
    form.contactPhone = d.contactPhone
    form.homeAddress = d.homeAddress
    form.profilePhotoName = d.profilePhotoName
    form.idCardFrontName = d.idCardFrontName
    form.idCardBackName = d.idCardBackName
    const period = d.checkinPeriod.split('~')
    form.checkinStartDate = period[0] || ''
    form.checkinEndDate = period[1] || ''
    form.feeStartDate = (d.feePeriod || d.checkinPeriod).split('~')[0] || ''
    form.feeEndDate = (d.feePeriod || d.checkinPeriod).split('~')[1] || ''
    form.careLevel = d.careLevel
    form.roomNo = d.roomNo
    form.nursingFee = d.nursingFee
    form.bedFee = d.bedFee
    form.otherFee = d.otherFee
    form.medicalFee = d.medicalFee
    form.subsidyFee = d.subsidyFee
    form.deposit = d.deposit
    form.contractName = d.contractName
    form.signDate = d.signDate
    form.payerName = d.payerName
    form.payerContact = d.payerContact
    form.contractFileName = d.contractFileName
    form.familyMembers = d.familyMembers?.length ? d.familyMembers : [{ familyName: '', familyContact: '', relationType: '' }]
  } finally {
    loading.value = false
  }
}
async function loadCareOptions(): Promise<void> {
  careOptions.value = await queryCareLevelOptions()
}
onMounted(async () => {
  await loadCareOptions()
  await loadDetail()
})
</script>

<template>
  <el-container class="page-root" v-loading="loading">
    <el-aside :width="layoutStore.collapsed ? '72px' : '220px'" class="sider">
      <div class="brand">
        <img src="https://dummyimage.com/36x36/8ec96b/ffffff&text=中" alt="logo" />
        <div v-if="!layoutStore.collapsed" class="brand-text">中州养老</div>
      </div>
      <el-menu :collapse="layoutStore.collapsed" :default-active="activeMenu" class="menu" @select="onMenuSelect">
        <el-menu-item index="1">首页</el-menu-item>
        <el-sub-menu index="2">
          <template #title>来访管理</template>
          <el-menu-item index="2-1">预约登记</el-menu-item>
          <el-menu-item index="2-2">来访登记</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="3">
          <template #title>入退管理</template>
          <el-menu-item index="3-1">健康评估</el-menu-item>
          <el-menu-item index="3-2">入住办理</el-menu-item>
          <el-menu-item index="3-3">退住办理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="4">
          <template #title>在住管理</template>
          <el-menu-item index="4-1">房型设置</el-menu-item>
          <el-menu-item index="4-2">床位预览</el-menu-item>
          <el-menu-item index="4-3">智能床位</el-menu-item>
          <el-menu-item index="4-4">合同跟踪</el-menu-item>
          <el-menu-item index="4-5">客户管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="6"><template #title>服务管理</template><el-menu-item index="6-1">护理项目</el-menu-item><el-menu-item index="6-2">护理计划</el-menu-item><el-menu-item index="6-3">护理等级</el-menu-item><el-menu-item index="6-4">负责老人</el-menu-item><el-menu-item index="6-5">任务安排</el-menu-item></el-sub-menu>
        <el-sub-menu index="7"><template #title>订单查询</template><el-menu-item index="7-1">订单管理</el-menu-item><el-menu-item index="7-2">退款管理</el-menu-item></el-sub-menu>
        <el-sub-menu index="8"><template #title>财务统计</template><el-menu-item index="8-1">账单管理</el-menu-item><el-menu-item index="8-2">预缴款充值</el-menu-item><el-menu-item index="8-3">余额查询</el-menu-item><el-menu-item index="8-4">欠费老人</el-menu-item></el-sub-menu>
        <el-sub-menu index="9"><template #title>智能检测</template><el-menu-item index="9-1">设备管理</el-menu-item><el-menu-item index="9-2">报警规则</el-menu-item><el-menu-item index="9-3">报警数据</el-menu-item></el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <el-button text @click="layoutStore.toggleCollapsed">☰</el-button>
        <div class="crumb">页面一 / 页面二 / 页面三</div>
        <div class="admin">管理员</div>
      </el-header>
      <el-main class="apply-page">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="128px" class="wrap">
      <el-card class="block">
        <template #header><span class="title">基本信息</span></template>
        <div class="grid2">
          <el-form-item label="*老人姓名" prop="elderName"><el-input v-model="form.elderName" maxlength="10" show-word-limit :disabled="readonly" /></el-form-item>
          <el-form-item label="*身份证号" prop="idCard"><el-input v-model="form.idCard" maxlength="18" show-word-limit :disabled="readonly" @blur="parseIdCard" /></el-form-item>
          <el-form-item label="出生日期"><el-input v-model="form.birthDate" disabled /></el-form-item>
          <el-form-item label="年龄"><el-input v-model="form.age" disabled /></el-form-item>
          <el-form-item label="性别"><el-input v-model="form.gender" disabled /></el-form-item>
          <el-form-item label="*联系方式" prop="contactPhone"><el-input v-model="form.contactPhone" maxlength="11" show-word-limit :disabled="readonly" /></el-form-item>
        </div>
        <el-form-item label="*家庭住址" prop="homeAddress"><el-input v-model="form.homeAddress" type="textarea" maxlength="100" show-word-limit :disabled="readonly" /></el-form-item>
        <div class="grid3">
          <el-form-item label="*一寸照片"><el-input v-model="form.profilePhotoName" placeholder="JPG/PNG/JPEG <=2M" :disabled="readonly" /></el-form-item>
          <el-form-item label="*身份证人像面"><el-input v-model="form.idCardFrontName" placeholder="JPG/PNG/JPEG <=2M" :disabled="readonly" /></el-form-item>
          <el-form-item label="*身份证国徽面"><el-input v-model="form.idCardBackName" placeholder="JPG/PNG/JPEG <=2M" :disabled="readonly" /></el-form-item>
        </div>
      </el-card>

      <el-card class="block">
        <template #header><span class="title">家属信息</span></template>
        <el-table :data="form.familyMembers" border>
          <el-table-column label="家属姓名">
            <template #default="{ row }"><el-input v-model="row.familyName" maxlength="10" show-word-limit /></template>
          </el-table-column>
          <el-table-column label="家属联系方式">
            <template #default="{ row }"><el-input v-model="row.familyContact" maxlength="11" show-word-limit /></template>
          </el-table-column>
          <el-table-column label="与老人关系">
            <template #default="{ row }"><el-select v-model="row.relationType"><el-option v-for="it in relationOptions" :key="it" :label="it" :value="it" /></el-select></template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template #default="{ $index }">
              <el-button link type="primary" @click="addFamilyRow">+</el-button>
              <el-button link type="danger" @click="removeFamilyRow($index)">-</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="family-actions" v-if="readonly">
          <el-button type="primary" :loading="savingFamily" @click="saveFamily">保存家属信息</el-button>
        </div>
      </el-card>

      <el-card class="block">
        <template #header>
          <div class="card-header-row">
            <span class="title">入住配置</span>
            <el-tooltip content="入住期限与费用期限请选择起止日期" placement="top">
              <el-icon class="config-info-icon"><InfoFilled /></el-icon>
            </el-tooltip>
          </div>
        </template>
        <el-row :gutter="24" class="config-row">
          <el-col :span="12" class="config-col">
            <el-form-item label="入住期限" prop="checkinStartDate" required>
              <el-date-picker
                v-model="checkinDateRangeModel"
                type="daterange"
                range-separator="-"
                start-placeholder="请选择开始日期"
                end-placeholder="请选择结束日期"
                value-format="YYYY-MM-DD"
                :disabled-date="disabledPast"
                :disabled="readonly"
              />
            </el-form-item>
            <el-form-item label="护理等级" prop="careLevel" required>
              <el-select v-model="form.careLevel" placeholder="请选择" :disabled="readonly" @change="onCareOrBedChange">
                <el-option v-for="it in careOptions" :key="it.levelName" :label="it.levelName" :value="it.levelName" />
              </el-select>
            </el-form-item>
            <el-form-item label="押金" class="money-item">
              <span class="money-prefix">+</span>
              <el-input-number v-model="form.deposit" :min="0" :precision="2" :disabled="readonly" />
              <span class="money-suffix">元</span>
            </el-form-item>
            <el-form-item label="床位费用" prop="bedFee" class="money-item" required>
              <span class="money-prefix">+</span>
              <el-input-number v-model="form.bedFee" :min="0" :precision="2" :disabled="readonly" />
              <span class="money-suffix">元/月</span>
            </el-form-item>
            <el-form-item label="医保费用" class="money-item">
              <span class="money-prefix minus">-</span>
              <el-input-number v-model="form.medicalFee" :min="0" :precision="2" :disabled="readonly" />
              <span class="money-suffix">元/月</span>
            </el-form-item>
          </el-col>
          <el-col :span="12" class="config-col">
            <el-form-item label="费用期限" prop="feeStartDate" required>
              <el-date-picker
                v-model="feeDateRangeModel"
                type="daterange"
                range-separator="-"
                start-placeholder="请选择开始日期"
                end-placeholder="请选择结束日期"
                value-format="YYYY-MM-DD"
                :disabled-date="disabledPast"
                :disabled="readonly"
              />
            </el-form-item>
            <el-form-item label="入住床位" prop="roomNo" required>
              <el-select v-model="form.roomNo" placeholder="请选择" :disabled="readonly" @change="onCareOrBedChange">
                <el-option v-for="it in bedOptions" :key="it" :label="it" :value="it" />
              </el-select>
            </el-form-item>
            <el-form-item label="护理费用" prop="nursingFee" class="money-item" required>
              <span class="money-prefix">+</span>
              <el-input-number v-model="form.nursingFee" :min="0" :precision="2" :disabled="readonly" />
              <span class="money-suffix">元/月</span>
            </el-form-item>
            <el-form-item label="其他费用" class="money-item">
              <span class="money-prefix">+</span>
              <el-input-number v-model="form.otherFee" :min="0" :precision="2" :disabled="readonly" />
              <span class="money-suffix">元/月</span>
            </el-form-item>
            <el-form-item label="政府补贴" class="money-item">
              <span class="money-prefix minus">-</span>
              <el-input-number v-model="form.subsidyFee" :min="0" :precision="2" :disabled="readonly" />
              <span class="money-suffix">元/月</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <el-card class="block">
        <template #header><span class="title">签约办理</span></template>
        <div class="grid2">
          <el-form-item label="*合同名称" prop="contractName"><el-input v-model="form.contractName" maxlength="20" show-word-limit :disabled="readonly" /></el-form-item>
          <el-form-item label="*签约日期" prop="signDate"><el-date-picker v-model="form.signDate" type="date" value-format="YYYY-MM-DD" :disabled="readonly" /></el-form-item>
          <el-form-item label="丙方姓名"><el-input v-model="form.payerName" maxlength="10" show-word-limit :disabled="readonly" /></el-form-item>
          <el-form-item label="丙方联系方式"><el-input v-model="form.payerContact" maxlength="11" show-word-limit :disabled="readonly" /></el-form-item>
          <el-form-item label="*入住合同"><el-input v-model="form.contractFileName" placeholder="pdf <=60M" :disabled="readonly" /></el-form-item>
        </div>
      </el-card>

      <div class="actions">
        <el-button @click="router.push('/checkins')">返回</el-button>
        <el-button type="primary" :loading="submitting" :disabled="readonly" @click="onSubmit">提交</el-button>
        <el-button type="primary" plain @click="previewVisible = true">账单预览</el-button>
      </div>
    </el-form>
      </el-main>
    </el-container>

    <el-dialog v-model="previewVisible" title="账单预览" width="900px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="账单月份">{{ form.feeStartDate?.slice(0, 7) }}</el-descriptions-item>
        <el-descriptions-item label="老人姓名">{{ form.elderName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="账单周期">{{ form.feeStartDate }} - {{ form.feeEndDate }}</el-descriptions-item>
        <el-descriptions-item label="共计天数">{{ billDays }}天</el-descriptions-item>
      </el-descriptions>
      <el-table :data="[
        { type: '添加项', item: '护理费用', amount: form.nursingFee },
        { type: '添加项', item: '床位费用', amount: form.bedFee },
        { type: '添加项', item: '其他费用', amount: form.otherFee },
        { type: '扣减项', item: '医保费用', amount: -form.medicalFee },
        { type: '扣减项', item: '政府补贴', amount: -form.subsidyFee },
      ]" border style="margin-top: 12px">
        <el-table-column prop="type" label="类型" />
        <el-table-column prop="item" label="费用项目" />
        <el-table-column prop="amount" label="金额(元)" />
      </el-table>
      <div class="bill-footer">
        <span>每月应付：{{ monthlyAmount.toFixed(2) }}</span>
        <span>本期应付：{{ currentAmount.toFixed(2) }}</span>
        <span>押金：{{ form.deposit.toFixed(2) }}</span>
        <span>账单金额：{{ billAmount.toFixed(2) }}</span>
      </div>
    </el-dialog>
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
.admin { color: #909399; font-size: 13px; }
.apply-page { padding: 12px; background: #f5f7fa; min-height: 100vh; }
.wrap { max-width: 1400px; margin: 0 auto; }
.block { margin-bottom: 12px; }
.title { font-weight: 700; }
.card-header-row { display: inline-flex; align-items: center; gap: 6px; }
.config-info-icon { color: #409eff; cursor: help; vertical-align: middle; }
.config-row { align-items: flex-start; }
.config-col :deep(.el-select),
.config-col :deep(.el-date-editor.el-input__wrapper) { width: 100%; }
.config-col :deep(.el-date-editor--daterange) { width: 100%; box-sizing: border-box; }
.grid2 { display: grid; grid-template-columns: 1fr 1fr; gap: 8px 16px; }
.grid3 { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 8px 16px; }
.money-item :deep(.el-form-item__content) { display: flex; flex-wrap: wrap; align-items: center; gap: 8px; }
.money-prefix { display: inline-block; min-width: 12px; font-weight: 700; color: #606266; }
.money-prefix.minus { color: #909399; }
.money-suffix { color: #606266; font-size: 13px; flex-shrink: 0; }
.actions { display: flex; justify-content: center; gap: 12px; margin: 16px 0; }
.family-actions { margin-top: 10px; display: flex; justify-content: flex-end; }
.bill-footer { margin-top: 12px; display: flex; gap: 24px; justify-content: space-between; font-weight: 600; }
</style>
