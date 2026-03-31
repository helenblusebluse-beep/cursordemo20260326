export type HealthAssessmentItem = {
  id: number
  elderName: string
  idCard: string
  healthScore: number
  suggestion: string
  careLevel: string
  livingStatus: number
  livingStatusLabel: string
  assessedTime: string
}

export type HealthAssessmentPageResult = {
  total: number
  rows: HealthAssessmentItem[]
}

export type HealthAssessmentDetail = {
  id: number
  elderName: string
  idCard: string
  examOrg: string
  reportFileName: string
  reportFileSize: number
  reportFileUrl: string
  healthScore: number
  suggestion: string
  careLevel: string
  riskLevel: string
  reportSummary: string
  aiAdvice: string
  livingStatus: number
  livingStatusLabel: string
  assessedTime: string
  birthDate: string
  age: number
  gender: string
  summaryDate: string
  systemScores: Array<{ systemName: string; score: number }>
  ageDistributions: Array<{ ageGroup: string; healthy: number; warning: number; risk: number; severe: number; critical: number }>
  abnormalItems: Array<{ conclusion: string; itemName: string; resultValue: string; direction: string; refRange: string; unit: string }>
}
