<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useLayoutStore } from '../stores/layout'
import { queryHealthAssessments, uploadHealthReport } from '../api/healthAssessment'
import type { HealthAssessmentItem } from '../types/healthAssessment'

const router = useRouter()
const layoutStore = useLayoutStore()
const loading = ref(false)
const rows = ref<HealthAssessmentItem[]>([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const uploadVisible = ref(false)
const uploading = ref(false)
const reportFile = ref<File | null>(null)

const query = reactive({
  elderName: '',
  idCard: '',
  livingStatus: undefined as number | undefined,
})

const uploadForm = reactive({
  elderName: '',
  idCard: '',
  examOrg: '',
})

function onMenuSelect(index: string): void {
  if (index === '1') return void router.push('/')
  if (index === '2-1') return void router.push('/appointments/register')
  if (index === '2-2') return void router.push('/visits/register')
  if (index === '3-1') return
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

async function loadTable(): Promise<void> {
  loading.value = true
  try {
    const data = await queryHealthAssessments({
      elderName: query.elderName || undefined,
      idCard: query.idCard || undefined,
      livingStatus: query.livingStatus,
      page: page.value,
      pageSize: pageSize.value,
    })
    rows.value = data.rows
    total.value = data.total
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '查询失败')
  } finally {
    loading.value = false
  }
}

function onSearch(): void {
  page.value = 1
  void loadTable()
}

function onReset(): void {
  query.elderName = ''
  query.idCard = ''
  query.livingStatus = undefined
  page.value = 1
  void loadTable()
}

function openUploadDialog(): void {
  uploadForm.elderName = ''
  uploadForm.idCard = ''
  uploadForm.examOrg = ''
  reportFile.value = null
  uploadVisible.value = true
}

function onFileChange(file: { raw?: File }): boolean {
  if (!file.raw) return false
  const name = file.raw.name.toLowerCase()
  if (!name.endsWith('.pdf')) {
    ElMessage.error('仅支持上传pdf文件')
    return false
  }
  if (file.raw.size > 60 * 1024 * 1024) {
    ElMessage.error('文件大小不能超过60m，请重新上传')
    return false
  }
  reportFile.value = file.raw
  return false
}

async function submitUpload(): Promise<void> {
  if (!uploadForm.elderName || !uploadForm.idCard || !uploadForm.examOrg || !reportFile.value) {
    ElMessage.warning('请完整填写并上传体检报告')
    return
  }
  if (uploadForm.elderName.length > 10 || uploadForm.examOrg.length > 10) {
    ElMessage.warning('字符长度超限，请重新输入')
    return
  }
  if (!/^[0-9Xx]{18}$/.test(uploadForm.idCard)) {
    ElMessage.warning('身份证格式错误，请重新输入')
    return
  }
  uploading.value = true
  try {
    const fd = new FormData()
    fd.append('elderName', uploadForm.elderName)
    fd.append('idCard', uploadForm.idCard)
    fd.append('examOrg', uploadForm.examOrg)
    fd.append('reportFile', reportFile.value)
    const id = await uploadHealthReport(fd)
    uploadVisible.value = false
    ElMessage.success('上传成功')
    await loadTable()
    void router.push(`/health-assessments/${id}`)
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '上传失败')
  } finally {
    uploading.value = false
  }
}

function goDetail(row: HealthAssessmentItem): void {
  void router.push(`/health-assessments/${row.id}`)
}

onMounted(() => { void loadTable() })
</script>

<template>
  <el-container class="page-root">
    <el-aside :width="layoutStore.collapsed ? '72px' : '220px'" class="sider">
      <div class="brand">
        <img src="https://dummyimage.com/36x36/8ec96b/ffffff&text=中" alt="logo" />
        <div v-if="!layoutStore.collapsed" class="brand-text">中州养老</div>
      </div>
      <el-menu :collapse="layoutStore.collapsed" default-active="3-1" class="menu" @select="onMenuSelect">
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
        <div class="crumb">首页 / 页面一 / 页面二 / 页面三</div>
        <div class="admin">管理员</div>
      </el-header>
      <el-main class="main">
        <el-card class="page-card">
          <el-form :model="query" inline>
            <el-form-item label="老人姓名">
              <el-input v-model="query.elderName" placeholder="请输入" clearable />
            </el-form-item>
            <el-form-item label="身份证号">
              <el-input v-model="query.idCard" placeholder="请输入" clearable />
            </el-form-item>
            <el-form-item label="入住情况">
              <el-select v-model="query.livingStatus" placeholder="请选择" clearable style="width: 160px">
                <el-option label="已入住" :value="1" />
                <el-option label="未入住" :value="2" />
                <el-option label="已退住" :value="3" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button @click="onReset">重置</el-button>
              <el-button type="primary" @click="onSearch">搜索</el-button>
            </el-form-item>
          </el-form>

          <div class="toolbar">
            <el-button type="primary" @click="openUploadDialog">上传体检报告</el-button>
          </div>

          <el-table :data="rows" border stripe v-loading="loading" table-layout="auto">
            <el-table-column type="index" label="序号" min-width="70" />
            <el-table-column prop="elderName" label="老人姓名" min-width="110" />
            <el-table-column prop="idCard" label="身份证号" min-width="160" />
            <el-table-column prop="healthScore" label="健康评分（分）" min-width="120" />
            <el-table-column prop="suggestion" label="建议入住" min-width="90" />
            <el-table-column prop="careLevel" label="推荐护理等级" min-width="120" />
            <el-table-column prop="livingStatusLabel" label="入住情况" min-width="90" />
            <el-table-column prop="assessedTime" label="评估时间" min-width="170" />
            <el-table-column label="操作" min-width="80">
              <template #default="{ row }">
                <el-button link type="primary" @click="goDetail(row)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="pager-wrap">
            <el-pagination
              background
              :current-page="page"
              :page-size="pageSize"
              :page-sizes="[10, 20, 50]"
              layout="sizes, prev, pager, next, jumper, total"
              :total="total"
              @current-change="(p:number)=>{page=p; void loadTable()}"
              @size-change="(s:number)=>{pageSize=s; page=1; void loadTable()}"
            />
          </div>
        </el-card>
      </el-main>
    </el-container>
  </el-container>

  <el-dialog v-model="uploadVisible" title="上传体检报告" width="640px" :close-on-click-modal="false">
    <el-form label-width="120px">
      <el-form-item label="*老人姓名">
        <el-input v-model="uploadForm.elderName" maxlength="10" show-word-limit placeholder="请输入" />
      </el-form-item>
      <el-form-item label="*老人身份证号">
        <el-input v-model="uploadForm.idCard" maxlength="18" show-word-limit placeholder="请输入" />
      </el-form-item>
      <el-form-item label="*体检单位">
        <el-input v-model="uploadForm.examOrg" maxlength="10" show-word-limit placeholder="请输入" />
      </el-form-item>
      <el-form-item label="*体检报告">
        <el-upload action="#" :auto-upload="false" :show-file-list="true" :on-change="onFileChange" :limit="1">
          <el-button>点击上传</el-button>
          <template #tip><span style="color:#909399;margin-left:8px">请上传pdf文件，大小在60M以内</span></template>
        </el-upload>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="uploadVisible = false">取消</el-button>
      <el-button type="primary" :loading="uploading" @click="submitUpload">确定</el-button>
    </template>
  </el-dialog>
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
.main { padding: 12px; }
.page-card { border-radius: 8px; }
.toolbar { margin-bottom: 12px; display: flex; justify-content: flex-end; }
.pager-wrap { display: flex; justify-content: flex-end; padding-top: 12px; }
</style>
