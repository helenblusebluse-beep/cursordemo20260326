<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import {
  pageStudents,
  createStudent,
  updateStudent,
  deleteStudent,
  deleteStudents,
  demeritStudent,
  type StudentItem,
  type StudentPayload,
} from '@/api/student'
import { pageClasses, type ClassItem } from '@/api/class'

type Education = '初中' | '高中' | '大专' | '本科' | '硕士' | '博士'

const educationOptions: Education[] = ['初中', '高中', '大专', '本科', '硕士', '博士']

const classList = ref<ClassItem[]>([])

const queryForm = reactive({
  studentName: '',
  education: '' as '' | Education,
  classId: undefined as number | undefined,
})

const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)
const tableRows = ref<StudentItem[]>([])
const selectedIds = ref<number[]>([])

const addDialogVisible = ref(false)
const editDialogVisible = ref(false)
const deleteDialogVisible = ref(false)
const batchDeleteDialogVisible = ref(false)
const demeritDialogVisible = ref(false)

const currentEditId = ref<number | null>(null)
const currentDeleteId = ref<number | null>(null)
const currentDemeritId = ref<number | null>(null)
const demeritScore = ref<number | undefined>(undefined)

const addRef = ref<FormInstance | null>(null)
const editRef = ref<FormInstance | null>(null)

type FormModel = {
  studentName: string
  studentNo: string
  gender: number | undefined
  phone: string
  idCardNo: string
  isCollegeStudent: boolean | undefined
  address: string
  education: '' | Education
  graduateDate: string
  classId: number | undefined
}

function emptyForm(): FormModel {
  return {
    studentName: '',
    studentNo: '',
    gender: undefined,
    phone: '',
    idCardNo: '',
    isCollegeStudent: undefined,
    address: '',
    education: '',
    graduateDate: '',
    classId: undefined,
  }
}

const addForm = reactive(emptyForm())
const editForm = reactive(emptyForm())

const nameRegex = /^[A-Za-z\u4e00-\u9fa5]{2,10}$/
const studentNoRegex = /^[A-Za-z0-9]{10}$/
const phoneRegex = /^1\d{10}$/
const idCardRegex = /^\d{17}[\dXx]$/

const rules: FormRules = {
  studentName: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    {
      validator: (_rule, value, cb) => {
        const v = String(value ?? '').trim()
        if (!nameRegex.test(v)) return cb(new Error('姓名仅支持2-10位汉字或字母'))
        cb()
      },
      trigger: 'blur',
    },
  ],
  studentNo: [
    { required: true, message: '请输入学号', trigger: 'blur' },
    {
      validator: (_rule, value, cb) => {
        const v = String(value ?? '').trim()
        if (!studentNoRegex.test(v)) return cb(new Error('学号必须是固定10位字母或数字'))
        cb()
      },
      trigger: 'blur',
    },
  ],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    {
      validator: (_rule, value, cb) => {
        const v = String(value ?? '').trim()
        if (!phoneRegex.test(v)) return cb(new Error('手机号格式不正确'))
        cb()
      },
      trigger: 'blur',
    },
  ],
  idCardNo: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    {
      validator: (_rule, value, cb) => {
        const v = String(value ?? '').trim()
        if (!idCardRegex.test(v)) return cb(new Error('身份证号格式不正确'))
        cb()
      },
      trigger: 'blur',
    },
  ],
  isCollegeStudent: [{ required: true, message: '请选择是否院校学员', trigger: 'change' }],
  address: [
    {
      validator: (_rule, value, cb) => {
        const v = String(value ?? '')
        if (v.length > 100) return cb(new Error('联系地址最多100位'))
        cb()
      },
      trigger: 'blur',
    },
  ],
}

function genderLabel(g: number): string {
  if (g === 1) return '男'
  if (g === 2) return '女'
  return String(g)
}

function buildPayload(f: FormModel): StudentPayload {
  return {
    studentName: f.studentName.trim(),
    studentNo: f.studentNo.trim(),
    gender: f.gender as number,
    phone: f.phone.trim(),
    idCardNo: f.idCardNo.trim(),
    isCollegeStudent: f.isCollegeStudent as boolean,
    address: f.address?.trim() || undefined,
    education: f.education || undefined,
    graduateDate: f.graduateDate || undefined,
    classId: f.classId ?? null,
  }
}

async function loadClasses(): Promise<void> {
  try {
    const res = await pageClasses({ page: 1, pageSize: 500 })
    classList.value = res.rows
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载班级列表失败')
  }
}

async function loadData(): Promise<void> {
  loading.value = true
  try {
    const params = {
      studentName: queryForm.studentName.trim() || undefined,
      education: queryForm.education || undefined,
      classId: queryForm.classId,
      page: page.value,
      pageSize: pageSize.value,
    }
    let res = await pageStudents(params)
    total.value = Number(res.total) || 0
    const maxPage = Math.max(1, Math.ceil(total.value / pageSize.value) || 1)
    if (page.value > maxPage) {
      page.value = maxPage
      res = await pageStudents({ ...params, page: page.value })
      total.value = Number(res.total) || 0
    }
    tableRows.value = res.rows
  } catch (e) {
    tableRows.value = []
    total.value = 0
    ElMessage.error(e instanceof Error ? e.message : '加载学员列表失败')
  } finally {
    loading.value = false
  }
}

function onSearch(): void {
  page.value = 1
  void loadData()
}

function onReset(): void {
  queryForm.studentName = ''
  queryForm.education = ''
  queryForm.classId = undefined
  page.value = 1
  void loadData()
}

function onSelectionChange(rows: StudentItem[]): void {
  selectedIds.value = rows.map((r) => r.id)
}

function onPageChange(p: number): void {
  page.value = p
  void loadData()
}

function onPageSizeChange(s: number): void {
  pageSize.value = s
  page.value = 1
  void loadData()
}

function openAddDialog(): void {
  Object.assign(addForm, emptyForm())
  addDialogVisible.value = true
  void nextTick(() => {
    addRef.value?.clearValidate()
  })
}

function openEditDialog(row: StudentItem): void {
  currentEditId.value = row.id
  Object.assign(editForm, {
    studentName: row.studentName,
    studentNo: row.studentNo,
    gender: row.gender,
    phone: row.phone,
    idCardNo: row.idCardNo,
    isCollegeStudent: row.isCollegeStudent,
    address: row.address ?? '',
    education: (row.education || '') as '' | Education,
    graduateDate: row.graduateDate || '',
    classId: row.classId,
  })
  editDialogVisible.value = true
  void nextTick(() => {
    editRef.value?.clearValidate()
  })
}

function openDeleteDialog(row: StudentItem): void {
  currentDeleteId.value = row.id
  deleteDialogVisible.value = true
}

function openBatchDeleteDialog(): void {
  if (!selectedIds.value.length) {
    ElMessage.warning('请先勾选要删除的学员')
    return
  }
  batchDeleteDialogVisible.value = true
}

function openDemeritDialog(row: StudentItem): void {
  currentDemeritId.value = row.id
  demeritScore.value = 1
  demeritDialogVisible.value = true
}

async function submitAdd(): Promise<void> {
  const form = addRef.value
  if (!form) return
  await form.validate()
  try {
    await createStudent(buildPayload(addForm))
    addDialogVisible.value = false
    ElMessage.success('新增学员成功')
    await loadData()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '新增失败')
  }
}

async function submitEdit(): Promise<void> {
  const form = editRef.value
  if (!form) return
  await form.validate()
  if (!currentEditId.value) return
  try {
    await updateStudent(currentEditId.value, buildPayload(editForm))
    editDialogVisible.value = false
    ElMessage.success('编辑学员成功')
    await loadData()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '编辑失败')
  }
}

async function confirmDeleteOne(): Promise<void> {
  const id = currentDeleteId.value
  if (!id) return
  try {
    await deleteStudent(id)
    deleteDialogVisible.value = false
    currentDeleteId.value = null
    selectedIds.value = selectedIds.value.filter((x) => x !== id)
    ElMessage.success('删除学员成功')
    await loadData()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '删除失败')
  }
}

async function confirmBatchDelete(): Promise<void> {
  const ids = [...selectedIds.value]
  if (!ids.length) return
  try {
    await deleteStudents(ids)
    batchDeleteDialogVisible.value = false
    selectedIds.value = []
    ElMessage.success('批量删除成功')
    await loadData()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '批量删除失败')
  }
}

async function confirmDemerit(): Promise<void> {
  if (!currentDemeritId.value) return
  const score = Number(demeritScore.value)
  if (!Number.isFinite(score) || score <= 0) {
    ElMessage.warning('请输入大于0的违纪扣分')
    return
  }
  try {
    await demeritStudent(currentDemeritId.value, score)
    demeritDialogVisible.value = false
    ElMessage.success('违纪处理成功')
    await loadData()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '违纪处理失败')
  }
}

onMounted(() => {
  void loadClasses()
  void loadData()
})
</script>

<template>
  <div class="student-page">
    <div class="section-title">
      <span class="section-title__bar" />
      <span class="section-title__text">学员管理</span>
    </div>

    <el-card shadow="never" class="panel-card">
      <el-form :model="queryForm" inline class="query-form" label-width="70px">
        <el-form-item label="姓名">
          <el-input v-model="queryForm.studentName" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item label="最高学历">
          <el-select v-model="queryForm.education" placeholder="请选择" clearable style="width: 140px">
            <el-option v-for="e in educationOptions" :key="e" :label="e" :value="e" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属班级">
          <el-select v-model="queryForm.classId" placeholder="请选择" clearable style="width: 170px">
            <el-option v-for="c in classList" :key="c.id" :label="c.className" :value="c.id" />
          </el-select>
        </el-form-item>
        <div class="query-actions">
          <el-button type="primary" @click="onSearch">查询</el-button>
          <el-button @click="onReset">清空</el-button>
        </div>
      </el-form>

      <div class="toolbar">
        <el-button type="primary" @click="openAddDialog">
          <el-icon class="toolbar__icon"><Plus /></el-icon>
          添加学员
        </el-button>
        <el-button type="primary" plain @click="openBatchDeleteDialog">批量删除</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="tableRows"
        row-key="id"
        border
        stripe
        @selection-change="onSelectionChange"
      >
        <el-table-column type="selection" width="48" />
        <el-table-column prop="studentName" label="姓名" width="90" />
        <el-table-column prop="studentNo" label="学号" width="130" />
        <el-table-column label="班级" width="170">
          <template #default="{ row }">
            {{ row.className || '—' }}
          </template>
        </el-table-column>
        <el-table-column label="性别" width="70">
          <template #default="{ row }">
            {{ genderLabel(row.gender) }}
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="education" label="最高学历" width="100" />
        <el-table-column prop="demeritCount" label="违纪次数" width="90" />
        <el-table-column prop="demeritScore" label="违纪扣分" width="90" />
        <el-table-column prop="lastOperateTime" label="最后操作时间" width="170" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link @click="openEditDialog(row)">编辑</el-button>
            <el-button type="warning" link @click="openDemeritDialog(row)">违纪</el-button>
            <el-button type="danger" link @click="openDeleteDialog(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          background
          :current-page="page"
          :page-size="pageSize"
          :page-sizes="[10, 20, 50]"
          layout="sizes, prev, pager, next, jumper, total"
          :total="total"
          @current-change="onPageChange"
          @size-change="onPageSizeChange"
        />
      </div>
    </el-card>

    <el-dialog v-model="addDialogVisible" title="添加学员" width="760px" :close-on-click-modal="false">
      <el-form ref="addRef" :model="addForm" :rules="rules" label-width="90px" class="student-form-grid">
        <el-form-item label="姓名" prop="studentName"><el-input v-model="addForm.studentName" /></el-form-item>
        <el-form-item label="学号" prop="studentNo"><el-input v-model="addForm.studentNo" /></el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="addForm.gender" placeholder="请选择">
            <el-option label="男" :value="1" />
            <el-option label="女" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号" prop="phone"><el-input v-model="addForm.phone" /></el-form-item>
        <el-form-item label="身份证号" prop="idCardNo"><el-input v-model="addForm.idCardNo" /></el-form-item>
        <el-form-item label="是否院校学员" prop="isCollegeStudent">
          <el-select v-model="addForm.isCollegeStudent" placeholder="请选择">
            <el-option label="是" :value="true" />
            <el-option label="否" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系地址" prop="address"><el-input v-model="addForm.address" /></el-form-item>
        <el-form-item label="最高学历" prop="education">
          <el-select v-model="addForm.education" placeholder="请选择" clearable>
            <el-option v-for="e in educationOptions" :key="e" :label="e" :value="e" />
          </el-select>
        </el-form-item>
        <el-form-item label="毕业时间" prop="graduateDate">
          <el-date-picker v-model="addForm.graduateDate" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="所属班级">
          <el-select v-model="addForm.classId" placeholder="请选择" clearable style="width: 100%">
            <el-option v-for="c in classList" :key="c.id" :label="c.className" :value="c.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAdd">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="editDialogVisible" title="编辑学员" width="760px" :close-on-click-modal="false">
      <el-form ref="editRef" :model="editForm" :rules="rules" label-width="90px" class="student-form-grid">
        <el-form-item label="姓名" prop="studentName"><el-input v-model="editForm.studentName" /></el-form-item>
        <el-form-item label="学号" prop="studentNo"><el-input v-model="editForm.studentNo" /></el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="editForm.gender" placeholder="请选择">
            <el-option label="男" :value="1" />
            <el-option label="女" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号" prop="phone"><el-input v-model="editForm.phone" /></el-form-item>
        <el-form-item label="身份证号" prop="idCardNo"><el-input v-model="editForm.idCardNo" /></el-form-item>
        <el-form-item label="是否院校学员" prop="isCollegeStudent">
          <el-select v-model="editForm.isCollegeStudent" placeholder="请选择">
            <el-option label="是" :value="true" />
            <el-option label="否" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系地址" prop="address"><el-input v-model="editForm.address" /></el-form-item>
        <el-form-item label="最高学历" prop="education">
          <el-select v-model="editForm.education" placeholder="请选择" clearable>
            <el-option v-for="e in educationOptions" :key="e" :label="e" :value="e" />
          </el-select>
        </el-form-item>
        <el-form-item label="毕业时间" prop="graduateDate">
          <el-date-picker v-model="editForm.graduateDate" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="所属班级">
          <el-select v-model="editForm.classId" placeholder="请选择" clearable style="width: 100%">
            <el-option v-for="c in classList" :key="c.id" :label="c.className" :value="c.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="demeritDialogVisible" title="学员违纪处理" width="420px" :close-on-click-modal="false">
      <el-form label-width="90px">
        <el-form-item label="违纪扣分">
          <el-input-number v-model="demeritScore" :min="1" :max="100" controls-position="right" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="demeritDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmDemerit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="deleteDialogVisible" title="删除学员" width="420px" :close-on-click-modal="false">
      <div class="confirm-text">您确定要删除该学员的信息吗？</div>
      <template #footer>
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmDeleteOne">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="batchDeleteDialogVisible" title="批量删除" width="420px" :close-on-click-modal="false">
      <div class="confirm-text">您确定要删除已勾选的学员信息吗？</div>
      <template #footer>
        <el-button @click="batchDeleteDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmBatchDelete">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.student-page {
  padding: 14px 12px 18px;
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
  font-size: 22px;
  font-weight: 700;
  color: #303133;
}

.panel-card {
  border-radius: 8px;
  border: 1px solid #e4e7ed;
}

.query-form :deep(.el-form-item__label) {
  font-weight: 600;
}

.query-actions {
  margin-left: 8px;
  display: flex;
  gap: 10px;
}

.toolbar {
  margin: 4px 0 12px;
  display: flex;
  gap: 10px;
}

.toolbar__icon {
  margin-right: 6px;
}

.pagination-wrap {
  display: flex;
  justify-content: flex-end;
  padding-top: 12px;
}

.student-form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px 18px;
}

.student-form-grid :deep(.el-select),
.student-form-grid :deep(.el-date-editor) {
  width: 100%;
}

.confirm-text {
  text-align: center;
  padding: 8px 0;
}
</style>
