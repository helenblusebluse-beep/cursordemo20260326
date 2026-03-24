<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import {
  createClass,
  deleteClass,
  pageClasses,
  updateClass,
  type ClassItem,
  type ClassPayload,
} from '@/api/class'
import { pageEmployees, type EmployeeItem } from '@/api/employee'

const SUBJECT_OPTIONS = ['Java', 'JavaEE', 'Python', 'GO', '前端']

const tableLoading = ref(false)
const tableData = ref<ClassItem[]>([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)

const queryForm = reactive({
  className: '',
  dateRange: ['', ''] as [string, string],
})

const employees = ref<EmployeeItem[]>([])

const addDialogVisible = ref(false)
const editDialogVisible = ref(false)
const deleteDialogVisible = ref(false)

const deleteTargetId = ref<number | null>(null)
const editRowId = ref<number | null>(null)

const addFormRef = ref<FormInstance | null>(null)
const editFormRef = ref<FormInstance | null>(null)
const addSubmitting = ref(false)
const editSubmitting = ref(false)
const deleteSubmitting = ref(false)

const emptyForm = () => ({
  className: '',
  classroom: '',
  startDate: '',
  endDate: '',
  headTeacherEmpId: undefined as number | undefined,
  subjectName: '',
})

const addForm = reactive(emptyForm())
const editForm = reactive(emptyForm())

const classNameRegex = /^[A-Za-z0-9\u4e00-\u9fa5]{4,30}$/

const addRules = reactive<FormRules>({
  className: [
    { required: true, message: '请输入班级名称（4-30位）', trigger: 'blur' },
    {
      validator: (_rule, value, cb) => {
        const v = (value ?? '').toString()
        if (!v) return cb(new Error('请输入班级名称'))
        if (!classNameRegex.test(v)) return cb(new Error('班级名称仅支持4-30位：汉字/字母/数字'))
        return cb()
      },
      trigger: 'blur',
    },
  ],
  subjectName: [{ required: true, message: '请选择学科', trigger: 'change' }],
  startDate: [{ required: true, message: '请选择开课时间', trigger: 'change' }],
  endDate: [
    { required: true, message: '请选择结课时间', trigger: 'change' },
    {
      validator: (_rule, value, cb) => {
        const v = value as string
        if (!v) return cb(new Error('请选择结课时间'))
        if (addForm.startDate && v < addForm.startDate) return cb(new Error('结课时间不能早于开课时间'))
        return cb()
      },
      trigger: 'change',
    },
  ],
})

const editRules = reactive<FormRules>({
  className: addRules.className,
  subjectName: addRules.subjectName,
  startDate: addRules.startDate,
  endDate: [
    { required: true, message: '请选择结课时间', trigger: 'change' },
    {
      validator: (_rule, value, cb) => {
        const v = value as string
        if (!v) return cb(new Error('请选择结课时间'))
        if (editForm.startDate && v < editForm.startDate) return cb(new Error('结课时间不能早于开课时间'))
        return cb()
      },
      trigger: 'change',
    },
  ],
})

function statusLabel(status: number): string {
  if (status === 0) return '未开始'
  if (status === 1) return '进行中'
  if (status === 2) return '已结束'
  return String(status)
}

function statusTagType(status: number): 'success' | 'warning' | 'info' {
  if (status === 0) return 'info'
  if (status === 2) return 'success'
  return 'warning'
}

async function loadEmployees(): Promise<void> {
  try {
    const res = await pageEmployees({ page: 1, pageSize: 500 })
    employees.value = res.rows
  } catch (e) {
    const msg = e instanceof Error ? e.message : '加载员工列表失败'
    ElMessage.error(msg)
  }
}

async function loadTable(): Promise<void> {
  tableLoading.value = true
  try {
    const [start, end] = queryForm.dateRange
    const result = await pageClasses({
      className: queryForm.className,
      startDate: start || undefined,
      endDate: end || undefined,
      page: page.value,
      pageSize: pageSize.value,
    })
    tableData.value = result.rows
    total.value = Number(result.total)
  } catch (e) {
    const msg = e instanceof Error ? e.message : '加载失败'
    ElMessage.error(msg)
  } finally {
    tableLoading.value = false
  }
}

function buildPayload(form: typeof addForm): ClassPayload {
  return {
    className: form.className.trim(),
    classroom: form.classroom?.trim() || undefined,
    startDate: form.startDate,
    endDate: form.endDate,
    headTeacherEmpId: form.headTeacherEmpId,
    subjectName: form.subjectName.trim(),
  }
}

function onSearch(): void {
  page.value = 1
  void loadTable()
}

function onReset(): void {
  queryForm.className = ''
  queryForm.dateRange = ['', '']
  page.value = 1
  void loadTable()
}

function openAdd(): void {
  Object.assign(addForm, emptyForm())
  addDialogVisible.value = true
}

function openEdit(row: ClassItem): void {
  editRowId.value = row.id
  Object.assign(editForm, {
    className: row.className,
    classroom: row.classroom ?? '',
    startDate: row.startDate,
    endDate: row.endDate,
    headTeacherEmpId: row.headTeacherEmpId,
    subjectName: row.subjectName,
  })
  editDialogVisible.value = true
}

function openDelete(row: ClassItem): void {
  deleteTargetId.value = row.id
  deleteDialogVisible.value = true
}

async function submitAdd(): Promise<void> {
  const form = addFormRef.value
  if (!form) return
  try {
    await form.validate()
  } catch {
    return
  }
  if (addSubmitting.value) return
  addSubmitting.value = true
  try {
    await createClass(buildPayload(addForm))
    ElMessage.success('新增成功')
    addDialogVisible.value = false
    page.value = 1
    await loadTable()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '新增失败')
  } finally {
    addSubmitting.value = false
  }
}

async function submitEdit(): Promise<void> {
  const form = editFormRef.value
  if (!form) return
  try {
    await form.validate()
  } catch {
    return
  }
  const id = editRowId.value
  if (id == null) return
  if (editSubmitting.value) return
  editSubmitting.value = true
  try {
    await updateClass(id, buildPayload(editForm))
    ElMessage.success('保存成功')
    editDialogVisible.value = false
    editRowId.value = null
    await loadTable()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '保存失败')
  } finally {
    editSubmitting.value = false
  }
}

async function confirmDelete(): Promise<void> {
  const id = deleteTargetId.value
  if (id == null) {
    deleteDialogVisible.value = false
    return
  }
  if (deleteSubmitting.value) return
  deleteSubmitting.value = true
  try {
    await deleteClass(id)
    ElMessage.success('删除成功')
    deleteDialogVisible.value = false
    deleteTargetId.value = null
    await loadTable()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '删除失败')
  } finally {
    deleteSubmitting.value = false
  }
}

function cancelAdd(): void {
  addDialogVisible.value = false
}

function cancelEdit(): void {
  editDialogVisible.value = false
  editRowId.value = null
}

function cancelDelete(): void {
  deleteDialogVisible.value = false
  deleteTargetId.value = null
}

function onPageSizeChange(s: number): void {
  pageSize.value = s
  page.value = 1
  void loadTable()
}

function onPageChange(p: number): void {
  page.value = p
  void loadTable()
}

function tableIndex(index: number): number {
  return (page.value - 1) * pageSize.value + index + 1
}

onMounted(() => {
  void loadEmployees()
  void loadTable()
})
</script>

<template>
  <div class="class-manage">
    <el-row :gutter="20" class="class-manage__layout">
      <el-col :xs="24" :lg="24">
        <div class="section-title">
          <span class="section-title__bar" />
          <span class="section-title__text">班级管理</span>
        </div>

        <el-card shadow="never" class="class-manage__card">
          <el-form :model="queryForm" label-width="90px" class="class-manage__query" inline>
            <el-form-item label="班级名称">
              <el-input v-model="queryForm.className" placeholder="请输入班级名称" clearable />
            </el-form-item>

            <el-form-item label="结果时间">
              <el-date-picker
                v-model="queryForm.dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>

            <div class="class-manage__query-actions">
              <el-button type="primary" @click="onSearch">查询</el-button>
              <el-button @click="onReset">清空</el-button>
            </div>
          </el-form>

          <div class="class-manage__toolbar">
            <el-button type="primary" @click="openAdd">
              <el-icon class="class-manage__toolbar-icon">
                <Plus />
              </el-icon>
              新增班级
            </el-button>
          </div>

          <el-table v-loading="tableLoading" :data="tableData" border stripe class="class-manage__table">
            <el-table-column type="index" label="序号" width="70" :index="tableIndex" />
            <el-table-column prop="className" label="班级名称" min-width="160" />
            <el-table-column label="班级数量" width="110" align="right">
              <template #default="{ row }">
                {{ row.studentCount ?? 0 }}
              </template>
            </el-table-column>
            <el-table-column label="班主任" width="130">
              <template #default="{ row }">
                {{ row.headTeacherName || '—' }}
              </template>
            </el-table-column>
            <el-table-column prop="startDate" label="开课时间" width="140" />
            <el-table-column prop="endDate" label="结课时间" width="140" />
            <el-table-column label="状态" width="110">
              <template #default="{ row }">
                <el-tag :type="statusTagType(row.status)" effect="plain">
                  {{ statusLabel(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="lastOperateTime" label="最后操作时间" width="190" />
            <el-table-column label="操作" width="160" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" link @click="openEdit(row)">编辑</el-button>
                <el-button type="danger" link @click="openDelete(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="class-manage__pagination">
            <el-pagination
              background
              :current-page="page"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="pageSize"
              layout="sizes, prev, pager, next, jumper, total"
              :total="total"
              @size-change="onPageSizeChange"
              @current-change="onPageChange"
            />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="addDialogVisible" title="新增班级" width="620px" :close-on-click-modal="false">
      <el-form ref="addFormRef" :model="addForm" :rules="addRules" label-width="110px" class="demo-form">
        <el-form-item label="班级名称" prop="className">
          <el-input v-model="addForm.className" placeholder="请输入班级名称，如：JavaEE班级100期" maxlength="30" show-word-limit />
        </el-form-item>

        <el-form-item label="教室" prop="classroom">
          <el-input v-model="addForm.classroom" placeholder="选填" maxlength="20" />
        </el-form-item>

        <el-form-item label="开课时间" prop="startDate">
          <el-date-picker v-model="addForm.startDate" type="date" placeholder="选择开课时间" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>

        <el-form-item label="结课时间" prop="endDate">
          <el-date-picker v-model="addForm.endDate" type="date" placeholder="选择结课时间" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>

        <el-form-item label="班主任" prop="headTeacherEmpId">
          <el-select v-model="addForm.headTeacherEmpId" placeholder="选填，请选择班主任" clearable filterable style="width: 100%">
            <el-option v-for="e in employees" :key="e.id" :label="e.empName" :value="e.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="学科" prop="subjectName">
          <el-select v-model="addForm.subjectName" placeholder="请选择" filterable style="width: 100%">
            <el-option v-for="s in SUBJECT_OPTIONS" :key="s" :label="s" :value="s" />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="cancelAdd">取消</el-button>
        <el-button type="primary" :loading="addSubmitting" @click="submitAdd">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="editDialogVisible" title="修改班级" width="620px" :close-on-click-modal="false">
      <el-form ref="editFormRef" :model="editForm" :rules="editRules" label-width="110px" class="demo-form">
        <el-form-item label="班级名称" prop="className">
          <el-input v-model="editForm.className" placeholder="请输入班级名称" maxlength="30" show-word-limit />
        </el-form-item>

        <el-form-item label="教室" prop="classroom">
          <el-input v-model="editForm.classroom" placeholder="选填" maxlength="20" />
        </el-form-item>

        <el-form-item label="开课时间" prop="startDate">
          <el-date-picker v-model="editForm.startDate" type="date" placeholder="选择开课时间" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>

        <el-form-item label="结课时间" prop="endDate">
          <el-date-picker v-model="editForm.endDate" type="date" placeholder="选择结课时间" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>

        <el-form-item label="班主任" prop="headTeacherEmpId">
          <el-select v-model="editForm.headTeacherEmpId" placeholder="选填" clearable filterable style="width: 100%">
            <el-option v-for="e in employees" :key="e.id" :label="e.empName" :value="e.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="学科" prop="subjectName">
          <el-select v-model="editForm.subjectName" placeholder="请选择" filterable style="width: 100%">
            <el-option v-for="s in SUBJECT_OPTIONS" :key="s" :label="s" :value="s" />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="cancelEdit">取消</el-button>
        <el-button type="primary" :loading="editSubmitting" @click="submitEdit">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="deleteDialogVisible" title="删除班级" width="420px" :close-on-click-modal="false">
      <div class="delete-dialog__content">您确定要删除该班级吗？</div>
      <template #footer>
        <el-button @click="cancelDelete">取消</el-button>
        <el-button type="primary" :loading="deleteSubmitting" @click="confirmDelete">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.class-manage {
  padding: 18px 16px;
}

.class-manage__layout {
  margin: 0;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 10px;
  margin: 4px 0 14px;
}

.section-title__bar {
  width: 6px;
  height: 22px;
  background: #409eff;
  border-radius: 2px;
}

.section-title__text {
  font-size: 18px;
  font-weight: 700;
  color: #303133;
}

.class-manage__card {
  border-radius: 8px;
}

.class-manage__query :deep(.el-form-item__label) {
  font-weight: 600;
  color: #606266;
}

.class-manage__query :deep(.el-input),
.class-manage__query :deep(.el-date-editor) {
  width: 220px;
}

.class-manage__query-actions {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-left: 6px;
}

.class-manage__toolbar {
  margin: 12px 0 12px;
}

.class-manage__toolbar-icon {
  margin-right: 6px;
  font-size: 16px;
}

.class-manage__table {
  margin-top: 8px;
}

.class-manage__pagination {
  padding: 14px 0 2px;
  display: flex;
  justify-content: flex-end;
}

.delete-dialog__content {
  padding: 8px 0 6px;
  text-align: center;
  color: #303133;
}
</style>
