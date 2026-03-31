export interface FloorTab {
  id: number
  floorName: string
  sortOrder: number
  hasRooms: boolean
}

export interface BedItem {
  id: number
  bedName: string
  sortOrder: number
  status: number
  elderName: string | null
  canDelete: boolean
}

export interface RoomCard {
  id: number
  roomNo: string
  roomTypeName: string
  roomTypeId: number
  sortOrder: number
  canDeleteRoom: boolean
  beds: BedItem[]
}

export interface FloorDetail {
  floorId: number
  floorName: string
  rooms: RoomCard[]
}
