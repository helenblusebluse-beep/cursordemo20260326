import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type { SmartFloorTab, SmartFloorView } from '../types/smartBed'

const http = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

function unwrap<T>(res: { data: ApiResponse<T> }, err: string): T {
  if (res.data.code !== 1) throw new Error(res.data.msg || err)
  return res.data.data as T
}

export async function listSmartFloors(): Promise<SmartFloorTab[]> {
  const res = await http.get<ApiResponse<SmartFloorTab[]>>('/smart-beds/floors')
  return unwrap(res, '加载楼层失败')
}

export async function getSmartFloorView(floorId: number): Promise<SmartFloorView> {
  const res = await http.get<ApiResponse<SmartFloorView>>(`/smart-beds/floors/${floorId}/view`)
  return unwrap(res, '加载智能床位数据失败')
}
