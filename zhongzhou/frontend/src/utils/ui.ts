import { ElMessage, ElMessageBox } from 'element-plus'

/** 全局 Toast（对应全局说明：成功 / 错误 / 加载类提示） */
export const showToast = {
  success: (msg = '操作成功') => {
    ElMessage.success({ message: msg, duration: 3000 })
  },
  error: (msg = '操作失败') => {
    ElMessage.error({ message: msg, duration: 5000 })
  },
  warning: (msg: string) => {
    ElMessage.warning({ message: msg, duration: 4000 })
  },
  info: (msg: string) => {
    ElMessage.info({ message: msg, duration: 3000 })
  },
}

/** 确认删除（规范：此操作将删除该 xxx，是否继续？） */
export function confirmDelete(fieldName: string): Promise<boolean> {
  return ElMessageBox.confirm(
    `此操作将删除该${fieldName}，是否继续？`,
    '确认删除',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning',
      confirmButtonClass: 'el-button--danger',
    },
  )
    .then(() => true)
    .catch(() => false)
}

/** 通用确认（禁用 / 其他需二次确认的操作） */
export function confirmAction(
  title: string,
  message: string,
  type: 'warning' | 'info' = 'warning',
): Promise<boolean> {
  return ElMessageBox.confirm(message, title, {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type,
  })
    .then(() => true)
    .catch(() => false)
}
