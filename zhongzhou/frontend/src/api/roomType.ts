import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type { RoomTypeItem, RoomTypePageResult } from '../types/roomType'

const http = axios.create({
  baseURL: '/api',
  timeout: 30000,
})

export async function listEnabledRoomTypeOptions(): Promise<Array<{ id: number; roomName: string }>> {
  const res = await http.get<ApiResponse<Array<{ id: number; roomName: string }>>>('/room-types/enabled-options')
  if (res.data.code !== 1) throw new Error(res.data.msg || '加载房型选项失败')
  return res.data.data
}

export async function queryRoomTypes(params: { page: number; pageSize: number }): Promise<RoomTypePageResult> {
  const res = await http.get<ApiResponse<RoomTypePageResult>>('/room-types', { params })
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询房型失败')
  return res.data.data
}

export async function createRoomType(payload: {
  roomName: string
  bedCount: number
  bedFee: number
  introduction: string
  imageUrls: string[]
  remarks?: string
}): Promise<number> {
  const res = await http.post<ApiResponse<number>>('/room-types', payload)
  if (res.data.code !== 1) throw new Error(res.data.msg || '新增房型失败')
  return res.data.data
}

export async function updateRoomType(
  id: number,
  payload: {
    roomName: string
    bedCount: number
    bedFee: number
    introduction: string
    imageUrls: string[]
    remarks?: string
  },
): Promise<void> {
  const res = await http.put<ApiResponse<null>>(`/room-types/${id}`, payload)
  if (res.data.code !== 1) throw new Error(res.data.msg || '修改房型失败')
}

export async function deleteRoomType(id: number): Promise<void> {
  const res = await http.delete<ApiResponse<null>>(`/room-types/${id}`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '删除房型失败')
}

export async function toggleRoomTypeStatus(id: number, enable: boolean): Promise<void> {
  const res = await http.post<ApiResponse<null>>(`/room-types/${id}/toggle-status`, null, { params: { enable } })
  if (res.data.code !== 1) throw new Error(res.data.msg || '状态变更失败')
}

export async function getRoomTypeDetail(id: number): Promise<RoomTypeItem> {
  const res = await http.get<ApiResponse<RoomTypeItem>>(`/room-types/${id}`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '获取房型失败')
  return res.data.data
}
