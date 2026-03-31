export interface RoomTypeItem {
  id: number
  roomName: string
  bedCount?: number
  bedFee: string | number
  introduction: string
  thumbUrl: string | null
  createdTime: string
  status: number
  associatedRoomCount: number
  imageUrlsJson?: string | null
  remarks?: string | null
}

export interface RoomTypePageResult {
  total: number
  rows: RoomTypeItem[]
}
