export interface SmartDeviceItem {
  productCategory: string
  deviceName: string
  iconType?: string | null
}

export interface SmartBedCard {
  bedId: number
  bedName: string
  elderName: string
  devices: SmartDeviceItem[]
  presenceLabel: string | null
  heartRate?: number | null
  breathRate?: number | null
  leftBedCount?: number | null
  leftBedTime?: string | null
  message?: string | null
  alarm: boolean
  showHeartBreath: boolean
  showLeftBed: boolean
}

export interface SmartRoom {
  roomId: number
  roomNo: string
  roomDevices: SmartDeviceItem[]
  doorStatus: string
  temperature: string
  humidity: string
  alarmStatus: string
  roomMessage?: string | null
  beds: SmartBedCard[]
}

export interface SmartFloorView {
  floorId: number
  floorName: string
  rooms: SmartRoom[]
}

export interface SmartFloorTab {
  id: number
  floorName: string
  sortOrder: number
  hasAnomaly: boolean
}
