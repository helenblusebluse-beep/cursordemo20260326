import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type { ProfileInfo } from '../types/profile'

const http = axios.create({ baseURL: '/api', timeout: 15000 })

export async function fetchMyProfile(): Promise<ProfileInfo> {
  const res = await http.get<ApiResponse<ProfileInfo>>('/profile/me')
  if (res.data.code !== 1) throw new Error(res.data.msg || '加载个人信息失败')
  return res.data.data
}

export async function saveMyProfile(body: {
  nickname: string
  phone: string
  gender: number
  avatarUrl?: string
}): Promise<ProfileInfo> {
  const res = await http.put<ApiResponse<ProfileInfo>>('/profile/me', body)
  if (res.data.code !== 1) throw new Error(res.data.msg || '保存失败')
  return res.data.data
}

export async function changeMyPassword(body: {
  oldPassword: string
  newPassword: string
  confirmPassword: string
}): Promise<void> {
  const res = await http.put<ApiResponse<null>>('/profile/password', body)
  if (res.data.code !== 1) throw new Error(res.data.msg || '修改密码失败')
}

