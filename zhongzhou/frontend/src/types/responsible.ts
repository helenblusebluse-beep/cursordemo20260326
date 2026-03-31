export type ResponsibleFloorTab = {
  id: number
  floorName: string
  sortOrder: number
}

export type ResponsibleBed = {
  bedId: number
  bedName: string
  elderName: string
  caregiverNames: string[]
}

export type ResponsibleRoom = {
  roomId: number
  roomNo: string
  beds: ResponsibleBed[]
}

export type ResponsibleFloorView = {
  floorId: number
  floorName: string
  rooms: ResponsibleRoom[]
}
