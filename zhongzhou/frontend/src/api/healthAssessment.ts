import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type { HealthAssessmentDetail, HealthAssessmentPageResult } from '../types/healthAssessment'

const http = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

export async function queryHealthAssessments(params: {
  elderName?: string
  idCard?: string
  livingStatus?: number
  page: number
  pageSize: number
}): Promise<HealthAssessmentPageResult> {
  const res = await http.get<ApiResponse<HealthAssessmentPageResult>>('/health-assessments', { params })
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询健康评估失败')
  return res.data.data
}

export async function uploadHealthReport(formData: FormData): Promise<number> {
  const res = await http.post<ApiResponse<number>>('/health-assessments/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
  if (res.data.code !== 1) throw new Error(res.data.msg || '上传体检报告失败')
  return res.data.data
}

export async function getHealthAssessmentDetail(id: number): Promise<HealthAssessmentDetail> {
  const res = await http.get<ApiResponse<HealthAssessmentDetail>>(`/health-assessments/${id}`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '获取健康评估详情失败')
  return res.data.data
}
