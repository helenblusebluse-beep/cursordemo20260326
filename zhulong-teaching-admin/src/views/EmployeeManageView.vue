<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import type { FormInstance, FormRules, UploadProps, UploadRawFile } from 'element-plus'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import {
  pageEmployees,
  createEmployee,
  updateEmployee,
  deleteEmployee,
  deleteEmployees,
  uploadEmployeeAvatar,
  type EmployeeItem,
  type EmployeePayload,
} from '@/api/employee'
import { listDepartments, type DepartmentItem } from '@/api/department'

type WorkHistoryForm = {
  startDate: string
  endDate: string
  companyName: string
  positionName: string
}

const defaultAvatar = 'https://via.placeholder.com/72x52.png?text=avatar'

const departmentList = ref<DepartmentItem[]>([])
const tableRows = ref<EmployeeItem[]>([])
const tableLoading = ref(false)
const addSubmitting = ref(false)
const editSubmitting = ref(false)
const deleteSubmitting = ref(false)
const batchDeleteSubmitting = ref(false)
const avatarUploading = ref(false)

const queryForm = reactive({
  name: '',
  gender: '' as '' | 1 | 2,
  dateRange: ['', ''] as [string, string],
})

const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const selectedIds = ref<number[]>([])

const deptNameById = computed(() => {
  const m = new Map<number, string>()
  for (const d of departmentList.value) {
    m.set(d.id, d.deptName)
  }
  return m
})

function deptLabel(deptId: number | undefined): string {
  if (deptId == null) return '—'
  return deptNameById.value.get(deptId) ?? '—'
}

function genderText(g: number | undefined): string {
  if (g === 1) return '男'
  if (g === 2) return '女'
  return '—'
}

const positionOptions = ['班主任', '讲师', '教务主任', '咨询师', '就业老师']

const addVisible = ref(false)
const editVisible = ref(false)
const deleteVisible = ref(false)
const batchDeleteVisible = ref(false)
const deleteTargetId = ref<number | null>(null)
const editId = ref<number | null>(null)
/** 编辑前服务端头像，用于本地预览超长 base64 提交时回退，避免 PUT 写库失败 */
const editOriginalAvatarUrl = ref<string | null>(null)

const addRef = ref<FormInstance | null>(null)
const editRef = ref<FormInstance | null>(null)

function emptyWorkRow(): WorkHistoryForm {
  return { startDate: '', endDate: '', companyName: '', positionName: '' }
}

const emptyForm = () => ({
  username: '',
  empName: '',
  gender: '' as '' | 1 | 2,
  phone: '',
  deptId: undefined as number | undefined,
  positionName: '',
  salary: '' as number | '',
  entryDate: '',
  avatarUrl: defaultAvatar,
  workHistories: [emptyWorkRow()] as WorkHistoryForm[],
})

const addForm = reactive(emptyForm())
const editForm = reactive(emptyForm())

const usernameRegex = /^[A-Za-z0-9_]{2,20}$/
const nameRegex = /^[A-Za-z\u4e00-\u9fa5]{2,10}$/
const phoneRegex = /^1\d{10}$/

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { validator: (_r, v, cb) => (usernameRegex.test(String(v ?? '').trim()) ? cb() : cb(new Error('用户名2-20位字母/数字/_'))), trigger: 'blur' },
  ],
  empName: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { validator: (_r, v, cb) => (nameRegex.test(String(v ?? '').trim()) ? cb() : cb(new Error('姓名2-10位汉字/字母'))), trigger: 'blur' },
  ],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { validator: (_r, v, cb) => (phoneRegex.test(String(v ?? '').trim()) ? cb() : cb(new Error('手机号格式错误'))), trigger: 'blur' },
  ],
}

async function loadDepartments(): Promise<void> {
  try {
    const result = await listDepartments({ page: 1, pageSize: 500 })
    departmentList.value = Array.isArray(result) ? result : result.rows
  } catch (e) {
    const msg = e instanceof Error ? e.message : '加载部门失败'
    ElMessage.error(msg)
  }
}

async function loadEmployees(): Promise<void> {
  tableLoading.value = true
  try {
    const [start, end] = queryForm.dateRange
    const res = await pageEmployees({
      empName: queryForm.name.trim() || undefined,
      gender: queryForm.gender === '' ? undefined : queryForm.gender,
      startDate: start || undefined,
      endDate: end || undefined,
      page: page.value,
      pageSize: pageSize.value,
    })
    tableRows.value = res.rows
    total.value = Number(res.total)
  } catch (e) {
    const msg = e instanceof Error ? e.message : '加载员工列表失败'
    ElMessage.error(msg)
  } finally {
    tableLoading.value = false
  }
}

onMounted(() => {
  void loadDepartments()
  void loadEmployees()
})

function onSearch() {
  page.value = 1
  void loadEmployees()
}

function onReset() {
  queryForm.name = ''
  queryForm.gender = ''
  queryForm.dateRange = ['', '']
  page.value = 1
  void loadEmployees()
}

function onSelectionChange(rows: EmployeeItem[]) {
  selectedIds.value = rows.map((r) => r.id)
}

function onPageChange(p: number) {
  page.value = p
  void loadEmployees()
}

function onPageSizeChange(s: number) {
  pageSize.value = s
  page.value = 1
  void loadEmployees()
}

function addWorkHistory(form: typeof addForm | typeof editForm) {
  form.workHistories.push(emptyWorkRow())
}

function removeWorkHistory(form: typeof addForm | typeof editForm, index: number) {
  form.workHistories.splice(index, 1)
  if (!form.workHistories.length) addWorkHistory(form)
}

function openAdd() {
  Object.assign(addForm, emptyForm())
  addVisible.value = true
}

function openEdit(row: EmployeeItem) {
  editId.value = row.id
  editOriginalAvatarUrl.value = row.avatarUrl ?? null
  const sal = row.salary
  Object.assign(editForm, {
    username: row.username,
    empName: row.empName,
    gender: row.gender as 1 | 2,
    phone: row.phone,
    deptId: row.deptId,
    positionName: row.positionName ?? '',
    salary: sal === undefined || sal === null || sal === '' ? '' : Number(sal),
    entryDate: row.entryDate ?? '',
    avatarUrl: row.avatarUrl || defaultAvatar,
    workHistories: row.workHistories?.length
      ? row.workHistories.map((w) => ({
          startDate: w.startDate ?? '',
          endDate: w.endDate ?? '',
          companyName: w.companyName ?? '',
          positionName: w.positionName ?? '',
        }))
      : [emptyWorkRow()],
  })
  editVisible.value = true
}

function openDelete(row: EmployeeItem) {
  deleteTargetId.value = row.id
  deleteVisible.value = true
}

function openBatchDelete() {
  if (!selectedIds.value.length) {
    ElMessage.warning('请先勾选要删除的员工')
    return
  }
  batchDeleteVisible.value = true
}

function buildPayload(
  form: typeof addForm | typeof editForm,
  opts?: { fallbackAvatarUrl?: string | null },
): EmployeePayload {
  const trimmedAvatar = form.avatarUrl?.trim()
  let avatarUrl: string | undefined = trimmedAvatar
  if (trimmedAvatar && trimmedAvatar.length > 255) {
    ElMessage.warning('头像地址超过 255 字符（本地上传 base64 过长），已改为使用原头像或不上传')
    const fb = opts?.fallbackAvatarUrl?.trim()
    avatarUrl = fb || undefined
  }
  if (avatarUrl === defaultAvatar) avatarUrl = undefined

  const wh = form.workHistories
    .filter((w) => {
      const parts = [w.startDate, w.endDate, w.companyName?.trim(), w.positionName?.trim()].filter(Boolean)
      return parts.length > 0
    })
    .map((w) => ({
      startDate: w.startDate || undefined,
      endDate: w.endDate || undefined,
      companyName: w.companyName?.trim() || undefined,
      positionName: w.positionName?.trim() || undefined,
    }))
  const salaryNum = form.salary === '' ? undefined : Number(form.salary)
  return {
    username: form.username.trim(),
    empName: form.empName.trim(),
    gender: form.gender as 1 | 2,
    phone: form.phone.trim(),
    deptId: form.deptId,
    positionName: form.positionName?.trim() || undefined,
    salary: Number.isFinite(salaryNum as number) ? salaryNum : undefined,
    avatarUrl,
    entryDate: form.entryDate || undefined,
    workHistories: wh.length ? wh : undefined,
  }
}

async function submitAdd() {
  const form = addRef.value
  if (!form) return
  await form.validate()
  addSubmitting.value = true
  try {
    await createEmployee(buildPayload(addForm))
    addVisible.value = false
    ElMessage.success('新增员工成功，初始密码为 123456')
    await loadEmployees()
  } catch (e) {
    const msg = e instanceof Error ? e.message : '新增失败'
    ElMessage.error(msg)
  } finally {
    addSubmitting.value = false
  }
}

async function submitEdit() {
  const form = editRef.value
  if (!form) return
  await form.validate()
  if (!editId.value) return
  editSubmitting.value = true
  try {
    await updateEmployee(editId.value, buildPayload(editForm, { fallbackAvatarUrl: editOriginalAvatarUrl.value }))
    editVisible.value = false
    ElMessage.success('编辑员工成功')
    await loadEmployees()
  } catch (e) {
    const msg = e instanceof Error ? e.message : '保存失败'
    ElMessage.error(msg)
  } finally {
    editSubmitting.value = false
  }
}

async function confirmDeleteOne() {
  if (!deleteTargetId.value) return
  deleteSubmitting.value = true
  try {
    await deleteEmployee(deleteTargetId.value)
    selectedIds.value = selectedIds.value.filter((id) => id !== deleteTargetId.value)
    deleteVisible.value = false
    deleteTargetId.value = null
    ElMessage.success('删除员工成功')
    await loadEmployees()
  } catch (e) {
    const msg = e instanceof Error ? e.message : '删除失败'
    ElMessage.error(msg)
  } finally {
    deleteSubmitting.value = false
  }
}

async function confirmBatchDelete() {
  if (!selectedIds.value.length) return
  batchDeleteSubmitting.value = true
  try {
    await deleteEmployees(selectedIds.value)
    selectedIds.value = []
    batchDeleteVisible.value = false
    ElMessage.success('批量删除成功')
    await loadEmployees()
  } catch (e) {
    const msg = e instanceof Error ? e.message : '批量删除失败'
    ElMessage.error(msg)
  } finally {
    batchDeleteSubmitting.value = false
  }
}

function avatarBeforeUpload(file: UploadRawFile): boolean {
  const allowed = ['image/png', 'image/jpeg', 'image/jpg']
  if (!allowed.includes(file.type)) {
    ElMessage.error('仅支持 PNG/JPG/JPEG 图片')
    return false
  }
  if (file.size > 2 * 1024 * 1024) {
    ElMessage.error('图片大小不能超过2M')
    return false
  }
  return true
}

function createAvatarHandler(target: 'add' | 'edit'): UploadProps['onChange'] {
  return (file) => {
    if (!file.raw || !avatarBeforeUpload(file.raw)) return
    avatarUploading.value = true
    void uploadEmployeeAvatar(file.raw)
      .then((url) => {
        if (target === 'add') addForm.avatarUrl = url
        else editForm.avatarUrl = url
        ElMessage.success('头像已上传')
      })
      .catch((e) => {
        const msg = e instanceof Error ? e.message : '上传失败'
        ElMessage.error(msg)
      })
      .finally(() => {
        avatarUploading.value = false
      })
  }
}
</script>

<template>
  <div class="employee-page">
    <div class="section-title"><span class="section-title__bar" /><span class="section-title__text">员工管理</span></div>
    <el-card v-loading="tableLoading" shadow="never" class="panel-card">
      <el-form :model="queryForm" inline label-width="70px" class="query-form">
        <el-form-item label="姓名"><el-input v-model="queryForm.name" placeholder="请输入员工姓名" clearable /></el-form-item>
        <el-form-item label="性别">
          <el-select v-model="queryForm.gender" placeholder="请选择" clearable style="width: 140px">
            <el-option label="男" :value="1" /><el-option label="女" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="入职时间">
          <el-date-picker v-model="queryForm.dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <div class="query-actions"><el-button type="primary" @click="onSearch">查询</el-button><el-button @click="onReset">清空</el-button></div>
      </el-form>

      <div class="toolbar">
        <el-button type="primary" @click="openAdd"><el-icon class="toolbar__icon"><Plus /></el-icon>新增员工</el-button>
        <el-button type="primary" plain @click="openBatchDelete">批量删除</el-button>
      </div>

      <el-table :data="tableRows" border stripe style="width: 100%" @selection-change="onSelectionChange">
        <el-table-column type="selection" width="48" />
        <el-table-column prop="empName" label="姓名" min-width="90" />
        <el-table-column label="性别" min-width="70">
          <template #default="{ row }">{{ genderText(row.gender) }}</template>
        </el-table-column>
        <el-table-column label="头像" min-width="90">
          <template #default="{ row }"><img :src="row.avatarUrl || defaultAvatar" class="avatar" alt="avatar" /></template>
        </el-table-column>
        <el-table-column label="所属部门" min-width="120">
          <template #default="{ row }">{{ deptLabel(row.deptId) }}</template>
        </el-table-column>
        <el-table-column prop="positionName" label="职位" min-width="120" />
        <el-table-column prop="entryDate" label="入职日期" min-width="120" />
        <el-table-column label="最后操作时间" min-width="170">
          <template #default="{ row }">{{ row.lastOperateTime ?? '—' }}</template>
        </el-table-column>
        <el-table-column label="操作" min-width="130">
          <template #default="{ row }">
            <el-button type="primary" link @click="openEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="openDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination background :current-page="page" :page-size="pageSize" :page-sizes="[10,20,50]" layout="sizes, prev, pager, next, jumper, total" :total="total" @current-change="onPageChange" @size-change="onPageSizeChange" />
      </div>
    </el-card>

    <el-dialog v-model="addVisible" title="新增员工" width="860px" :close-on-click-modal="false">
      <el-form ref="addRef" :model="addForm" :rules="rules" label-width="80px" class="emp-form-grid">
        <el-form-item label="用户名" prop="username"><el-input v-model="addForm.username" placeholder="2-20位字符" /></el-form-item>
        <el-form-item label="姓名" prop="empName"><el-input v-model="addForm.empName" placeholder="2-10位字符" /></el-form-item>
        <el-form-item label="性别" prop="gender"><el-select v-model="addForm.gender" placeholder="请选择"><el-option label="男" :value="1" /><el-option label="女" :value="2" /></el-select></el-form-item>
        <el-form-item label="手机号" prop="phone"><el-input v-model="addForm.phone" placeholder="请输入员工手机号" /></el-form-item>
        <el-form-item label="职位"><el-select v-model="addForm.positionName" placeholder="请选择" clearable><el-option v-for="p in positionOptions" :key="p" :label="p" :value="p" /></el-select></el-form-item>
        <el-form-item label="薪资"><el-input v-model.number="addForm.salary" type="number" placeholder="请输入员工薪资" /></el-form-item>
        <el-form-item label="所属部门"><el-select v-model="addForm.deptId" placeholder="请选择" clearable><el-option v-for="d in departmentList" :key="d.id" :label="d.deptName" :value="d.id" /></el-select></el-form-item>
        <el-form-item label="入职日期"><el-date-picker v-model="addForm.entryDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="头像" v-loading="avatarUploading">
          <el-upload :show-file-list="false" :auto-upload="false" accept=".png,.jpg,.jpeg" :on-change="createAvatarHandler('add')">
            <img :src="addForm.avatarUrl || defaultAvatar" class="avatar avatar--large" alt="avatar" />
          </el-upload>
        </el-form-item>
      </el-form>
      <div class="history-title">工作经历</div>
      <div v-for="(w, index) in addForm.workHistories" :key="`add-${index}`" class="history-row">
        <el-date-picker v-model="w.startDate" type="date" value-format="YYYY-MM-DD" placeholder="开始日期" />
        <el-date-picker v-model="w.endDate" type="date" value-format="YYYY-MM-DD" placeholder="结束日期" />
        <el-input v-model="w.companyName" placeholder="请输入公司名称" />
        <el-input v-model="w.positionName" placeholder="请输入职位" />
        <el-button type="danger" plain @click="removeWorkHistory(addForm, index)">删除</el-button>
      </div>
      <el-button type="primary" plain size="small" @click="addWorkHistory(addForm)">新增工作经历</el-button>
      <template #footer><el-button @click="addVisible = false">取消</el-button><el-button type="primary" :loading="addSubmitting" @click="submitAdd">保存</el-button></template>
    </el-dialog>

    <el-dialog v-model="editVisible" title="修改员工" width="860px" :close-on-click-modal="false">
      <el-form ref="editRef" :model="editForm" :rules="rules" label-width="80px" class="emp-form-grid">
        <el-form-item label="用户名" prop="username"><el-input v-model="editForm.username" /></el-form-item>
        <el-form-item label="姓名" prop="empName"><el-input v-model="editForm.empName" /></el-form-item>
        <el-form-item label="性别" prop="gender"><el-select v-model="editForm.gender"><el-option label="男" :value="1" /><el-option label="女" :value="2" /></el-select></el-form-item>
        <el-form-item label="手机号" prop="phone"><el-input v-model="editForm.phone" /></el-form-item>
        <el-form-item label="职位"><el-select v-model="editForm.positionName" clearable><el-option v-for="p in positionOptions" :key="p" :label="p" :value="p" /></el-select></el-form-item>
        <el-form-item label="薪资"><el-input v-model.number="editForm.salary" type="number" /></el-form-item>
        <el-form-item label="所属部门"><el-select v-model="editForm.deptId" clearable><el-option v-for="d in departmentList" :key="d.id" :label="d.deptName" :value="d.id" /></el-select></el-form-item>
        <el-form-item label="入职日期"><el-date-picker v-model="editForm.entryDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
        <el-form-item label="头像" v-loading="avatarUploading">
          <el-upload :show-file-list="false" :auto-upload="false" accept=".png,.jpg,.jpeg" :on-change="createAvatarHandler('edit')">
            <img :src="editForm.avatarUrl || defaultAvatar" class="avatar avatar--large" alt="avatar" />
          </el-upload>
        </el-form-item>
      </el-form>
      <div class="history-title">工作经历</div>
      <div v-for="(w, index) in editForm.workHistories" :key="`edit-${index}`" class="history-row">
        <el-date-picker v-model="w.startDate" type="date" value-format="YYYY-MM-DD" placeholder="开始日期" />
        <el-date-picker v-model="w.endDate" type="date" value-format="YYYY-MM-DD" placeholder="结束日期" />
        <el-input v-model="w.companyName" placeholder="请输入公司名称" />
        <el-input v-model="w.positionName" placeholder="请输入职位" />
        <el-button type="danger" plain @click="removeWorkHistory(editForm, index)">删除</el-button>
      </div>
      <el-button type="primary" plain size="small" @click="addWorkHistory(editForm)">新增工作经历</el-button>
      <template #footer><el-button @click="editVisible = false">取消</el-button><el-button type="primary" :loading="editSubmitting" @click="submitEdit">保存</el-button></template>
    </el-dialog>

    <el-dialog v-model="deleteVisible" title="删除员工" width="420px" :close-on-click-modal="false">
      <div class="confirm-text">您确定要删除该员工的信息吗？</div>
      <template #footer><el-button @click="deleteVisible = false">取消</el-button><el-button type="primary" :loading="deleteSubmitting" @click="confirmDeleteOne">确定</el-button></template>
    </el-dialog>

    <el-dialog v-model="batchDeleteVisible" title="批量删除" width="420px" :close-on-click-modal="false">
      <div class="confirm-text">您确定要删除已勾选的员工信息吗？</div>
      <template #footer><el-button @click="batchDeleteVisible = false">取消</el-button><el-button type="primary" :loading="batchDeleteSubmitting" @click="confirmBatchDelete">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<style scoped>
.employee-page { padding: 14px 12px 18px; }
.section-title { display: flex; align-items: center; gap: 10px; margin: 4px 0 14px; }
.section-title__bar { width: 6px; height: 22px; background: #409eff; border-radius: 2px; }
.section-title__text { font-size: 22px; font-weight: 700; color: #303133; }
.panel-card { border-radius: 8px; border: 1px solid #e4e7ed; }
.panel-card :deep(.el-table__inner-wrapper::before) { height: 0; }
.query-actions { margin-left: 8px; display: flex; gap: 10px; }
.toolbar { margin: 4px 0 12px; display: flex; gap: 10px; }
.toolbar__icon { margin-right: 6px; }
.avatar { width: 42px; height: 30px; object-fit: cover; border: 1px solid #dcdfe6; }
.avatar--large { width: 90px; height: 64px; cursor: pointer; }
.pagination-wrap { display: flex; justify-content: flex-end; padding-top: 12px; }
.emp-form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 8px 18px; }
.emp-form-grid :deep(.el-select), .emp-form-grid :deep(.el-date-editor) { width: 100%; }
.history-title { margin: 8px 0; font-weight: 700; color: #303133; }
.history-row { display: grid; grid-template-columns: 1fr 1fr 1.2fr 1fr auto; gap: 10px; margin-bottom: 8px; }
.confirm-text { text-align: center; padding: 8px 0; }
</style>
