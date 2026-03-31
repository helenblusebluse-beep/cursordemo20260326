import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type { FloorDetail, FloorTab } from '../types/bedPreview'

const http = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

function unwrap<T>(res: { data: ApiResponse<T> }, err: string): T {
  if (res.data.code !== 1) throw new Error(res.data.msg || err)
  return res.data.data as T
}

export async function listFloors(): Promise<FloorTab[]> {
  const res = await http.get<ApiResponse<FloorTab[]>>('/bed-preview/floors')
  return unwrap(res, '加载楼层失败')
}

export async function getFloorDetail(floorId: number): Promise<FloorDetail> {
  const res = await http.get<ApiResponse<FloorDetail>>(`/bed-preview/floors/${floorId}`)
  return unwrap(res, '加载楼层详情失败')
}

export async function createFloor(payload: { floorName: string; sortOrder: number }): Promise<number> {
  const res = await http.post<ApiResponse<number>>('/bed-preview/floors', payload)
  return unwrap(res, '新增楼层失败')
}

export async function updateFloor(id: number, payload: { floorName: string; sortOrder: number }): Promise<void> {
  const res = await http.put<ApiResponse<null>>(`/bed-preview/floors/${id}`, payload)
  unwrap(res, '更新楼层失败')
}

export async function deleteFloor(id: number): Promise<void> {
  const res = await http.delete<ApiResponse<null>>(`/bed-preview/floors/${id}`)
  unwrap(res, '删除楼层失败')
}

export async function createRoom(
  floorId: number,
  payload: { roomNo: string; roomTypeId: number; sortOrder: number },
): Promise<number> {
  const res = await http.post<ApiResponse<number>>(`/bed-preview/floors/${floorId}/rooms`, payload)
  return unwrap(res, '新增房间失败')
}

export async function updateRoom(
  roomId: number,
  payload: { roomNo: string; roomTypeId: number; sortOrder: number },
): Promise<void> {
  const res = await http.put<ApiResponse<null>>(`/bed-preview/rooms/${roomId}`, payload)
  unwrap(res, '更新房间失败')
}

export async function deleteRoom(roomId: number): Promise<void> {
  const res = await http.delete<ApiResponse<null>>(`/bed-preview/rooms/${roomId}`)
  unwrap(res, '删除房间失败')
}

export async function createBed(roomId: number, payload: { bedName: string; sortOrder: number }): Promise<number> {
  const res = await http.post<ApiResponse<number>>(`/bed-preview/rooms/${roomId}/beds`, payload)
  return unwrap(res, '新增床位失败')
}

export async function updateBed(bedId: number, payload: { bedName: string; sortOrder: number }): Promise<void> {
  const res = await http.put<ApiResponse<null>>(`/bed-preview/beds/${bedId}`, payload)
  unwrap(res, '更新床位失败')
}

export async function deleteBed(bedId: number): Promise<void> {
  const res = await http.delete<ApiResponse<null>>(`/bed-preview/beds/${bedId}`)
  unwrap(res, '删除床位失败')
}
