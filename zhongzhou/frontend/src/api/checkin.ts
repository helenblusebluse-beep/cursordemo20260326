import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type { CareLevelOption, CheckinDetail, CheckinPageResult } from '../types/checkin'

const http = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

export async function queryCheckins(params: {
  elderName?: string
  idCard?: string
  page: number
  pageSize: number
}): Promise<CheckinPageResult> {
  const res = await http.get<ApiResponse<CheckinPageResult>>('/checkins', { params })
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询入住办理失败')
  return res.data.data
}

export async function applyCheckin(payload: {
  elderName: string
  idCard: string
  contactPhone: string
  homeAddress: string
  profilePhotoName: string
  idCardFrontName: string
  idCardBackName: string
  checkinStartDate: string
  feeStartDate: string
  roomNo: string
  careLevel: string
  nursingFee: string
  bedFee: string
  contractName: string
  signDate: string
  payerName?: string
  payerContact?: string
  contractFileName: string
  familyMembers: Array<{ familyName: string; familyContact: string; relationType: string }>
  checkinPeriod: string
}): Promise<number> {
  const res = await http.post<ApiResponse<number>>('/checkins/apply', payload)
  if (res.data.code !== 1) throw new Error(res.data.msg || '发起入住申请失败')
  return res.data.data
}

export async function getCheckinDetail(id: number): Promise<CheckinDetail> {
  const res = await http.get<ApiResponse<CheckinDetail>>(`/checkins/${id}`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '获取入住详情失败')
  return res.data.data
}

export async function updateCheckinFamily(id: number, familyMembers: Array<{ familyName: string; familyContact: string; relationType: string }>): Promise<void> {
  const res = await http.put<ApiResponse<null>>(`/checkins/${id}/family`, { familyMembers })
  if (res.data.code !== 1) throw new Error(res.data.msg || '保存家属信息失败')
}

export async function queryCareLevelOptions(): Promise<CareLevelOption[]> {
  const res = await http.get<ApiResponse<CareLevelOption[]>>('/nursing-care-levels/options')
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询护理等级选项失败')
  return res.data.data
}
