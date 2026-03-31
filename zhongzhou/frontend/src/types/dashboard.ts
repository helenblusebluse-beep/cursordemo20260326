export type DashboardData = {
  updateTime: string
  overview: {
    elderCount: number
    bedCount: number
    serviceTotal: string
    employeeCount: number
    incomeTotal: string
  }
  profile: {
    name: string
    title: string
    greeting: string
  }
  serviceTrend: Array<{ date: string; count: number }>
  levelDistribution: Array<{ name: string; value: number }>
  ageDistribution: Array<{ range: string; male: number; female: number }>
  appointments: Array<{ time: string; type: string; elderName: string; contact: string }>
  quickActions: Array<{ key: string; label: string }>
}

export type ApiResponse<T> = {
  code: number
  msg: string
  data: T
}
