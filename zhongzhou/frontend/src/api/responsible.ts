import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type { ResponsibleFloorTab, ResponsibleFloorView } from '../types/responsible'

const http = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

export async function queryResponsibleFloors(): Promise<ResponsibleFloorTab[]> {
  const res = await http.get<ApiResponse<ResponsibleFloorTab[]>>('/responsible-elders/floors')
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询楼层失败')
  return res.data.data
}

export async function queryResponsibleFloorView(floorId: number): Promise<ResponsibleFloorView> {
  const res = await http.get<ApiResponse<ResponsibleFloorView>>(`/responsible-elders/floors/${floorId}/view`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询楼层视图失败')
  return res.data.data
}

export async function queryCaregiverOptions(): Promise<string[]> {
  const res = await http.get<ApiResponse<string[]>>('/responsible-elders/caregivers/options')
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询护理员选项失败')
  return res.data.data
}

export async function queryBedCaregivers(bedId: number): Promise<string[]> {
  const res = await http.get<ApiResponse<string[]>>(`/responsible-elders/beds/${bedId}/caregivers`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询床位护理员失败')
  return res.data.data
}

export async function queryRoomCaregivers(roomId: number): Promise<string[]> {
  const res = await http.get<ApiResponse<string[]>>(`/responsible-elders/rooms/${roomId}/caregivers`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询房间护理员失败')
  return res.data.data
}

export async function saveBedCaregivers(bedId: number, caregiverNames: string[]): Promise<void> {
  const res = await http.post<ApiResponse<null>>(`/responsible-elders/beds/${bedId}/caregivers`, { caregiverNames })
  if (res.data.code !== 1) throw new Error(res.data.msg || '保存床位护理员失败')
}

export async function saveRoomCaregivers(roomId: number, caregiverNames: string[]): Promise<void> {
  const res = await http.post<ApiResponse<null>>(`/responsible-elders/rooms/${roomId}/caregivers`, { caregiverNames })
  if (res.data.code !== 1) throw new Error(res.data.msg || '保存房间护理员失败')
}
