import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type {
  IotAlarmRecordPageResult,
  IotAlarmRuleItem,
  IotAlarmRulePageResult,
  IotDeviceDetail,
  IotDevicePageResult,
  IotProductItem,
  IotTslHistoryRow,
  IotTslModuleItem,
  IotTslRuntimeRow,
  IotTslServiceInvocationRow,
} from '../types/iot'

const http = axios.create({ baseURL: '/api', timeout: 15000 })

export async function fetchIotProducts(): Promise<IotProductItem[]> {
  const res = await http.get<ApiResponse<IotProductItem[]>>('/iot/products')
  if (res.data.code !== 1) throw new Error(res.data.msg || '加载产品失败')
  return res.data.data
}

export async function syncIotProducts(): Promise<void> {
  const res = await http.post<ApiResponse<null>>('/iot/products/sync')
  if (res.data.code !== 1) throw new Error(res.data.msg || '同步失败')
}

export async function fetchIotDeviceOptions(): Promise<{ id: number; deviceName: string; productId: number }[]> {
  const res = await http.get<ApiResponse<{ id: number; deviceName: string; productId: number }[]>>('/iot/devices/options')
  if (res.data.code !== 1) throw new Error(res.data.msg || '加载设备选项失败')
  return res.data.data
}

export async function queryIotDevices(params: {
  deviceName?: string
  productId?: number
  deviceType?: string
  page: number
  pageSize: number
}): Promise<IotDevicePageResult> {
  const res = await http.get<ApiResponse<IotDevicePageResult>>('/iot/devices', { params })
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询设备失败')
  return res.data.data
}

export async function fetchIotDeviceDetail(id: number): Promise<IotDeviceDetail> {
  const res = await http.get<ApiResponse<IotDeviceDetail>>(`/iot/devices/${id}`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '加载设备详情失败')
  return res.data.data
}

export async function fetchIotTslModules(deviceId: number): Promise<IotTslModuleItem[]> {
  const res = await http.get<ApiResponse<IotTslModuleItem[]>>(`/iot/devices/${deviceId}/tsl/modules`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '加载物模型模块失败')
  return res.data.data
}

export async function fetchIotTslRunningStatus(
  deviceId: number,
  moduleId: number,
  page: number,
  pageSize: number,
): Promise<{ total: number; rows: IotTslRuntimeRow[] }> {
  const res = await http.get<ApiResponse<{ total: number; rows: IotTslRuntimeRow[] }>>(
    `/iot/devices/${deviceId}/tsl/running-status`,
    { params: { moduleId, page, pageSize } },
  )
  if (res.data.code !== 1) throw new Error(res.data.msg || '加载运行状态失败')
  return res.data.data
}

export async function fetchIotTslPropertyHistory(params: {
  deviceId: number
  moduleId: number
  identifier: string
  rangeType: string
  start?: string
  end?: string
  page: number
  pageSize: number
}): Promise<{ total: number; rows: IotTslHistoryRow[] }> {
  const res = await http.get<ApiResponse<{ total: number; rows: IotTslHistoryRow[] }>>(
    `/iot/devices/${params.deviceId}/tsl/property-history`,
    {
      params: {
        moduleId: params.moduleId,
        identifier: params.identifier,
        rangeType: params.rangeType,
        start: params.start,
        end: params.end,
        page: params.page,
        pageSize: params.pageSize,
      },
    },
  )
  if (res.data.code !== 1) throw new Error(res.data.msg || '加载上报历史失败')
  return res.data.data
}

export async function fetchIotTslServiceInvocations(params: {
  deviceId: number
  moduleId: number
  rangeType: string
  start?: string
  end?: string
  offset: number
  limit: number
}): Promise<{ total: number; rows: IotTslServiceInvocationRow[] }> {
  const res = await http.get<ApiResponse<{ total: number; rows: IotTslServiceInvocationRow[] }>>(
    `/iot/devices/${params.deviceId}/tsl/service-invocations`,
    {
      params: {
        moduleId: params.moduleId,
        rangeType: params.rangeType,
        start: params.start,
        end: params.end,
        offset: params.offset,
        limit: params.limit,
      },
    },
  )
  if (res.data.code !== 1) throw new Error(res.data.msg || '加载服务调用失败')
  return res.data.data
}

export async function createIotDevice(body: {
  deviceName: string
  remarkName: string
  productId: number
  deviceType: string
  bindElderId?: number | null
  bindBedId?: number | null
}): Promise<number> {
  const res = await http.post<ApiResponse<number>>('/iot/devices', body)
  if (res.data.code !== 1) throw new Error(res.data.msg || '创建失败')
  return res.data.data as number
}

export async function updateIotDevice(
  id: number,
  body: {
    remarkName: string
    deviceType: string
    bindElderId?: number | null
    bindBedId?: number | null
  },
): Promise<void> {
  const res = await http.put<ApiResponse<null>>(`/iot/devices/${id}`, body)
  if (res.data.code !== 1) throw new Error(res.data.msg || '保存失败')
}

export async function deleteIotDevice(id: number): Promise<void> {
  const res = await http.delete<ApiResponse<null>>(`/iot/devices/${id}`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '删除失败')
}

export async function queryIotAlarmRules(params: {
  ruleName?: string
  productId?: number
  page: number
  pageSize: number
}): Promise<IotAlarmRulePageResult> {
  const res = await http.get<ApiResponse<IotAlarmRulePageResult>>('/iot/alarm-rules', { params })
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询规则失败')
  return res.data.data
}

export async function fetchIotAlarmRuleDetail(id: number): Promise<IotAlarmRuleItem> {
  const res = await http.get<ApiResponse<IotAlarmRuleItem>>(`/iot/alarm-rules/${id}`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '加载规则失败')
  return res.data.data
}

export async function fetchIotAlarmRuleModuleOptions(productId: number): Promise<string[]> {
  const res = await http.get<ApiResponse<string[]>>('/iot/alarm-rules/options/modules', { params: { productId } })
  if (res.data.code !== 1) throw new Error(res.data.msg || '加载模块失败')
  return res.data.data
}

export async function fetchIotAlarmRuleFunctionOptions(productId: number, moduleName: string): Promise<string[]> {
  const res = await http.get<ApiResponse<string[]>>('/iot/alarm-rules/options/functions', {
    params: { productId, moduleName },
  })
  if (res.data.code !== 1) throw new Error(res.data.msg || '加载功能失败')
  return res.data.data
}

export async function saveIotAlarmRule(
  id: number | undefined,
  body: {
    ruleName: string
    productId: number
    deviceId?: number | null
    moduleName: string
    functionName: string
    dataType: string
    compareType: string
    thresholdValue: number
    persistCycles: number
    timeStart: string
    timeEnd: string
    muteCycleMinutes: number
    enabled: number
  },
): Promise<number | void> {
  if (id) {
    const res = await http.put<ApiResponse<null>>(`/iot/alarm-rules/${id}`, body)
    if (res.data.code !== 1) throw new Error(res.data.msg || '保存失败')
    return
  }
  const res = await http.post<ApiResponse<number>>('/iot/alarm-rules', body)
  if (res.data.code !== 1) throw new Error(res.data.msg || '创建失败')
  return res.data.data
}

export async function setIotAlarmRuleEnabled(id: number, enabled: 0 | 1): Promise<void> {
  const res = await http.put<ApiResponse<null>>(`/iot/alarm-rules/${id}/enabled`, null, { params: { enabled } })
  if (res.data.code !== 1) throw new Error(res.data.msg || '操作失败')
}

export async function deleteIotAlarmRule(id: number): Promise<void> {
  const res = await http.delete<ApiResponse<null>>(`/iot/alarm-rules/${id}`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '删除失败')
}

export async function queryIotAlarmRecords(params: {
  deviceName?: string
  createdDate?: string
  handleStatus?: number
  page: number
  pageSize: number
}): Promise<IotAlarmRecordPageResult> {
  const res = await http.get<ApiResponse<IotAlarmRecordPageResult>>('/iot/alarm-records', { params })
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询报警数据失败')
  return res.data.data
}

export async function handleIotAlarmRecord(id: number, body: { handleTime: string; handleResult: string }): Promise<void> {
  const res = await http.post<ApiResponse<null>>(`/iot/alarm-records/${id}/handle`, body)
  if (res.data.code !== 1) throw new Error(res.data.msg || '处理失败')
}
