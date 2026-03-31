<script setup lang="ts">
import { onMounted, ref, nextTick } from 'vue'
import * as echarts from 'echarts'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getHealthAssessmentDetail } from '../api/healthAssessment'
import type { HealthAssessmentDetail } from '../types/healthAssessment'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const detail = ref<HealthAssessmentDetail | null>(null)
const barRef = ref<HTMLDivElement | null>(null)
const radarRef = ref<HTMLDivElement | null>(null)

async function loadDetail(): Promise<void> {
  loading.value = true
  try {
    const id = Number(route.params.id)
    detail.value = await getHealthAssessmentDetail(id)
    await nextTick()
    renderCharts()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载详情失败')
  } finally {
    loading.value = false
  }
}

function renderCharts(): void {
  if (!detail.value) return
  if (barRef.value) {
    const chart = echarts.init(barRef.value)
    const d = detail.value.ageDistributions
    chart.setOption({
      grid: { left: 36, right: 16, top: 24, bottom: 26 },
      legend: { bottom: 0 },
      tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
      xAxis: { type: 'category', data: d.map((x) => x.ageGroup) },
      yAxis: { type: 'value', max: 100, axisLabel: { formatter: '{value}%' } },
      series: [
        { name: '健康', type: 'bar', stack: 't', data: d.map((x) => x.healthy), itemStyle: { color: '#67c23a' } },
        { name: '提示', type: 'bar', stack: 't', data: d.map((x) => x.warning), itemStyle: { color: '#e6a23c' } },
        { name: '风险', type: 'bar', stack: 't', data: d.map((x) => x.risk), itemStyle: { color: '#f59a23' } },
        { name: '危重', type: 'bar', stack: 't', data: d.map((x) => x.severe), itemStyle: { color: '#f56c6c' } },
        { name: '严重危险', type: 'bar', stack: 't', data: d.map((x) => x.critical), itemStyle: { color: '#5470c6' } },
      ],
    })
  }
  if (radarRef.value) {
    const chart = echarts.init(radarRef.value)
    const vals = detail.value.systemScores
    chart.setOption({
      tooltip: {},
      radar: {
        center: ['50%', '55%'],
        radius: 88,
        indicator: vals.map((x) => ({ name: `${x.score}分\n${x.systemName}`, max: 100 })),
      },
      series: [{ type: 'radar', data: [{ value: vals.map((x) => x.score), areaStyle: { color: 'rgba(103,194,58,.35)' } }] }],
      graphic: [{ type: 'text', left: 'center', top: '48%', style: { text: `${detail.value.healthScore}分`, fontSize: 42, fontWeight: 700 } }],
    })
  }
}

onMounted(() => { void loadDetail() })
</script>

<template>
  <div class="detail-page" v-loading="loading">
    <template v-if="detail">
      <el-card class="card">
        <template #header><div class="section-title">基本信息</div></template>
        <div class="grid-2">
          <div class="item"><label>老人姓名</label><span>{{ detail.elderName }}</span></div>
          <div class="item"><label>老人身份证号</label><span>{{ detail.idCard }}</span></div>
          <div class="item"><label>出生日期</label><span>{{ detail.birthDate }}</span></div>
          <div class="item"><label>年龄</label><span>{{ detail.age }}</span></div>
          <div class="item"><label>性别</label><span>{{ detail.gender }}</span></div>
          <div class="item"><label>体检机构</label><span>{{ detail.examOrg }}</span></div>
          <div class="item"><label>总检日期</label><span>{{ detail.summaryDate }}</span></div>
          <div class="item"><label>体检报告</label><span>{{ detail.reportFileName }} <a class="link" :href="detail.reportFileUrl" target="_blank">查看</a></span></div>
        </div>
      </el-card>

      <el-card class="card">
        <template #header><div class="section-title">体检总结</div></template>
        <div class="grid-2">
          <div class="item"><label>健康评分</label><span>{{ detail.healthScore }}</span></div>
          <div class="item"><label>风险等级</label><span>{{ detail.riskLevel }}</span></div>
          <div class="item"><label>建议入住</label><span>{{ detail.suggestion }}</span></div>
          <div class="item"><label>推荐等级</label><span>{{ detail.careLevel }}</span></div>
          <div class="item"><label>入住情况</label><span>{{ detail.livingStatusLabel }}</span></div>
          <div class="item"><label>评估时间</label><span>{{ detail.assessedTime }}</span></div>
          <div class="item"><label>分析报告</label><span>{{ detail.reportFileName }} <a class="link" :href="detail.reportFileUrl" target="_blank">查看</a></span></div>
          <div class="item"><label>报告总结</label><span class="text">{{ detail.reportSummary }}</span></div>
          <div class="item full"><label>AI建议</label><span class="text">{{ detail.aiAdvice }}</span></div>
        </div>
      </el-card>

      <el-card class="card">
        <template #header><div class="section-title">疾病风险</div></template>
        <div class="risk-grid">
          <div class="chart-card">
            <div class="chart-title">不同年龄人群健康指数分布</div>
            <div ref="barRef" class="chart"></div>
          </div>
          <div class="chart-card">
            <div class="chart-title">不同系统健康指数分布</div>
            <div ref="radarRef" class="chart"></div>
          </div>
        </div>
      </el-card>

      <el-card class="card">
        <template #header><div class="section-title">异常分析</div></template>
        <el-table :data="detail.abnormalItems" border table-layout="auto">
          <el-table-column prop="conclusion" label="结论" min-width="120" />
          <el-table-column prop="itemName" label="项目名称" min-width="140" />
          <el-table-column prop="resultValue" label="检查结果" min-width="100" />
          <el-table-column label="" min-width="50">
            <template #default="{ row }"><span :class="row.direction==='up' ? 'up' : 'down'">{{ row.direction==='up' ? '↑' : '↓' }}</span></template>
          </el-table-column>
          <el-table-column prop="refRange" label="参考值" min-width="120" />
          <el-table-column prop="unit" label="单位" min-width="90" />
        </el-table>
        <p class="hint"><strong>异常解读：</strong>体重超标、血糖/血脂异常提示代谢风险增高，建议结合医生意见进行饮食与运动干预。</p>
        <p class="hint"><strong>AI建议：</strong>{{ detail.aiAdvice }}</p>
      </el-card>

      <div class="footer-btn">
        <el-button type="primary" @click="router.push('/health-assessments')">返回</el-button>
      </div>
    </template>
  </div>
</template>

<style scoped>
.detail-page { padding: 12px; background: #f5f7fa; min-height: 100vh; }
.card { border-radius: 8px; margin-bottom: 12px; }
.section-title { font-weight: 700; color: #303133; }
.grid-2 { display: grid; grid-template-columns: 1fr 1fr; gap: 12px 24px; }
.item { display: grid; grid-template-columns: 90px 1fr; align-items: start; font-size: 14px; color: #303133; }
.item label { color: #606266; }
.item.full { grid-column: 1 / span 2; }
.text { line-height: 1.6; }
.link { color: #409eff; margin-left: 8px; text-decoration: none; }
.risk-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
.chart-card { border: 1px solid #ebeef5; border-radius: 6px; padding: 12px; }
.chart-title { text-align: center; color: #303133; margin-bottom: 8px; font-weight: 600; }
.chart { height: 320px; }
.up { color: #f56c6c; font-weight: 700; }
.down { color: #67c23a; font-weight: 700; }
.hint { margin: 12px 0 0; color: #606266; line-height: 1.8; }
.footer-btn { display: flex; justify-content: center; padding: 8px 0 20px; }
</style>
