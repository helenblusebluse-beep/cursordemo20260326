export interface IotProductItem {
  id: number
  productName: string
  aliyunProductKey: string
}

export interface IotDeviceRow {
  id: number
  deviceName: string
  remarkName: string
  productName: string
  accessLocationDisplay: string
  deviceType: string
  createdTime: string
}

export interface IotDevicePageResult {
  total: number
  rows: IotDeviceRow[]
}

export interface IotDeviceDetail {
  id: number
  deviceName: string
  remarkName: string
  productId: number
  productName: string
  productKey: string
  deviceSecret: string
  deviceType: string
  accessLocationDisplay: string
  bindElderId: number | null
  bindBedId: number | null
  protocol: string
  onlineStatus: number
  onlineStatusLabel: string
  region: string
  nodeType: string
  authMethod: string
  ipAddress: string
  firmwareVersion: string
  creatorName: string
  createdTime: string
  activationTime: string
  updatedTime: string
}

export interface IotTslModuleItem {
  id: number
  moduleName: string
  moduleKey: string
}

export interface IotTslRuntimeRow {
  id: number
  propIdentifier: string
  functionName: string
  updateTime: string
  dataValue: string
}

export interface IotTslHistoryRow {
  rawValue: string
  reportTime: string
}

/** 物模型-服务调用（下行）列表行 */
export interface IotTslServiceInvocationRow {
  invokeTime: string
  serviceIdentifier: string
  serviceName: string
  inputParams: string
  outputParams: string
}

export interface IotAlarmRuleItem {
  id: number
  ruleName: string
  productId: number
  productName: string
  moduleName: string
  deviceId: number | null
  deviceDisplay: string
  functionName: string
  dataType: 'elder' | 'device' | string
  dataTypeLabel: string
  compareType: string
  thresholdValue: number
  persistCycles: number
  timeStart: string
  timeEnd: string
  muteCycleMinutes: number
  muteCycleLabel: string
  alarmRuleText: string
  effectivePeriod: string
  enabled: number
  enabledLabel: string
  createdTime: string
}

export interface IotAlarmRulePageResult {
  total: number
  rows: IotAlarmRuleItem[]
}

export interface IotAlarmRecordItem {
  id: number
  ruleId: number
  ruleName: string
  deviceId: number
  deviceName: string
  productName: string
  dataTypeLabel: string
  accessLocation: string
  functionName: string
  metricValue: number
  alarmRuleText: string
  triggerTime: string
  notifyObject: string
  notifyTime: string
  alarmContent: string
  alarmTime: string
  handleStatus: number
  handleStatusLabel: string
  handledTime: string
  handleResult: string
}

export interface IotAlarmRecordPageResult {
  total: number
  rows: IotAlarmRecordItem[]
}
