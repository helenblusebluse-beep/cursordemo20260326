<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCheckinDetail } from '../api/checkin'
import type { CheckinDetail } from '../types/checkin'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const detail = ref<CheckinDetail | null>(null)

async function loadDetail(): Promise<void> {
  loading.value = true
  try {
    detail.value = await getCheckinDetail(Number(route.params.id))
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载详情失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => { void loadDetail() })
</script>

<template>
  <div class="detail-page" v-loading="loading">
    <el-card v-if="detail" class="card">
      <template #header><div class="title">入住详情</div></template>
      <div class="row"><label>老人姓名</label><span>{{ detail.elderName }}</span><label>老人身份证号</label><span>{{ detail.idCard }}</span></div>
      <div class="row"><label>入住床位</label><span>{{ detail.roomNo }}</span><label>护理等级</label><span>{{ detail.careLevel }}</span></div>
      <div class="row"><label>入住期限</label><span>{{ detail.checkinPeriod }}</span><label>创建时间</label><span>{{ detail.createdTime }}</span></div>
      <div class="footer"><el-button type="primary" @click="router.push('/checkins')">返回</el-button></div>
    </el-card>
  </div>
</template>

<style scoped>
.detail-page { padding: 12px; background: #f5f7fa; min-height: 100vh; }
.card { border-radius: 8px; }
.title { font-weight: 700; color: #303133; }
.row { display: grid; grid-template-columns: 100px 1fr 120px 1fr; gap: 10px; margin: 14px 0; align-items: center; }
.row label { color: #909399; }
.footer { display: flex; justify-content: center; margin-top: 12px; }
</style>
