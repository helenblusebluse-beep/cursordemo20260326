import axios from 'axios'
import type { ApiResponse, DashboardData } from '../types/dashboard'

const http = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

export async function fetchDashboard(): Promise<DashboardData> {
  const res = await http.get<ApiResponse<DashboardData>>('/home/dashboard')
  if (res.data.code !== 1) {
    throw new Error(res.data.msg || '获取首页数据失败')
  }
  return res.data.data
}
