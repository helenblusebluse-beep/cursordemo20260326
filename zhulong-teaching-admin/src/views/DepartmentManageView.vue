<script setup lang="ts">
import { nextTick, onMounted, reactive, ref, unref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import {
  createDepartment,
  deleteDepartment,
  listDepartments,
  updateDepartment,
  type DepartmentItem,
} from '@/api/department'

const tableData = ref<DepartmentItem[]>([])
const tableLoading = ref(false)
const queryForm = reactive({ deptName: '' })
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

const addRef = ref<FormInstance | null>(null)
const editRef = ref<FormInstance | null>(null)
/** ElInput 暴露的 input 为 Ref<HTMLInputElement>，需 unref 后读原生 value */
const addNameInputRef = ref<{ input?: unknown } | null>(null)
const editNameInputRef = ref<{ input?: unknown } | null>(null)
/** 包住名称输入框，用于直接 querySelector 读真实 DOM（比组件 ref 更可靠，尤其中文 IME） */
const addNameFieldWrapRef = ref<HTMLElement | null>(null)
const editNameFieldWrapRef = ref<HTMLElement | null>(null)

function readElInputNativeValue(inst: { input?: unknown; ref?: unknown } | null): string | undefined {
  if (!inst) return undefined
  const tryRead = (holder: unknown): string | undefined => {
    const el = unref(holder) as unknown
    if (el instanceof HTMLInputElement || el instanceof HTMLTextAreaElement) {
      return el.value
    }
    return undefined
  }
  return tryRead(inst.input) ?? tryRead(inst.ref)
}

function readDomInputFromWrap(wrap: HTMLElement | null | undefined): string {
  if (!wrap) return ''
  const el = wrap.querySelector<HTMLInputElement>('input.el-input__inner')
  return el?.value ?? ''
}

/** 在「原生 input」与「v-model」之间择优：优先取长度合法(2-10)且更长的串，避免 IME 下一边只有拼音、一边已上屏 */
function mergeDeptNameCandidates(a: string, b: string): string {
  const ta = a.trim()
  const tb = b.trim()
  const valid = (s: string) => s.length >= 2 && s.length <= 10
  if (valid(ta) && valid(tb)) {
    return ta.length >= tb.length ? ta : tb
  }
  if (valid(ta)) return ta
  if (valid(tb)) return tb
  return ta.length >= tb.length ? ta : tb
}

const addForm = reactive({
  name: '',
})

const editForm = reactive({
  id: 0,
  name: '',
})

const deleteDialogVisible = ref(false)
const addDialogVisible = ref(false)
const editDialogVisible = ref(false)
const deleteTargetId = ref<number | null>(null)
const addSubmitting = ref(false)
const editSubmitting = ref(false)
const deleteSubmitting = ref(false)

const nameRules: FormRules = {
  name: [
    { required: true, message: '部门名称必填', trigger: 'change' },
    {
      validator: (_rule, value, callback) => {
        const v = String(value ?? '').trim()
        if (!v) {
          callback(new Error('请输入部门名称'))
          return
        }
        if (v.length < 2 || v.length > 10) {
          callback(new Error('部门名称长度为2-10位'))
          return
        }
        callback()
      },
      trigger: 'change',
    },
  ],
}

function onClickAddRow(): void {
  addForm.name = ''
  addDialogVisible.value = true
  void nextTick(() => {
    addRef.value?.clearValidate()
  })
}

async function loadTable(): Promise<void> {
  tableLoading.value = true
  try {
    const result = await listDepartments({
      deptName: queryForm.deptName,
      page: page.value,
      pageSize: pageSize.value,
    })
    if (Array.isArray(result)) {
      tableData.value = result
      total.value = result.length
    } else {
      tableData.value = result.rows
      total.value = Number(result.total)
    }
  } catch (e) {
    const msg = e instanceof Error ? e.message : '查询失败'
    ElMessage.error(msg)
  } finally {
    tableLoading.value = false
  }
}

function onSearch(): void {
  page.value = 1
  void loadTable()
}

function onReset(): void {
  queryForm.deptName = ''
  page.value = 1
  void loadTable()
}

function onPageChange(p: number): void {
  page.value = p
  void loadTable()
}

function onPageSizeChange(s: number): void {
  pageSize.value = s
  page.value = 1
  void loadTable()
}

/** 中文输入法组字过程中若直接点「确定」，焦点会先跑到按钮，compositionend 未触发，v-model 可能仍是空的；先结束焦点并等 DOM 更新再读值 */
async function prepareImeCommit(): Promise<void> {
  const el = document.activeElement
  if (el instanceof HTMLElement) {
    el.blur()
  }
  await nextTick()
  await nextTick()
}

function readAddDeptName(): string {
  const dom = readDomInputFromWrap(addNameFieldWrapRef.value).trim()
  const native = readElInputNativeValue(addNameInputRef.value)?.trim() ?? ''
  const m = addForm.name.trim()
  return mergeDeptNameCandidates(mergeDeptNameCandidates(dom, native), m)
}

function readEditDeptName(): string {
  const dom = readDomInputFromWrap(editNameFieldWrapRef.value).trim()
  const native = readElInputNativeValue(editNameInputRef.value)?.trim() ?? ''
  const m = editForm.name.trim()
  return mergeDeptNameCandidates(mergeDeptNameCandidates(dom, native), m)
}

function onAddNameCompositionEnd(): void {
  const v = readDomInputFromWrap(addNameFieldWrapRef.value).trim()
  if (v) addForm.name = v
}

function onEditNameCompositionEnd(): void {
  const v = readDomInputFromWrap(editNameFieldWrapRef.value).trim()
  if (v) editForm.name = v
}

async function onSubmitAdd(): Promise<void> {
  const beforeBlur = readAddDeptName()
  await prepareImeCommit()
  const afterBlur = readAddDeptName()
  const deptName = mergeDeptNameCandidates(beforeBlur, afterBlur)
  addForm.name = deptName
  if (deptName.length < 2 || deptName.length > 10) {
    ElMessage.warning('部门名称长度为2-10位')
    return
  }
  // 提交时不再调用 el-form validate()：部分环境下 validate 会误失败，导致未发请求且无明确提示
  if (addSubmitting.value) return
  addSubmitting.value = true
  try {
    await createDepartment(deptName)
    ElMessage.success('新增部门成功')
    addDialogVisible.value = false
    addForm.name = ''
    page.value = 1
    queryForm.deptName = ''
    await loadTable()
  } catch (e) {
    const msg = e instanceof Error ? e.message : '新增失败'
    ElMessage.error(msg)
  } finally {
    addSubmitting.value = false
  }
}

function onEditRow(row: DepartmentItem): void {
  editForm.id = row.id
  editForm.name = row.deptName
  editDialogVisible.value = true
}

async function onSubmitEdit(): Promise<void> {
  const beforeBlur = readEditDeptName()
  await prepareImeCommit()
  const afterBlur = readEditDeptName()
  const deptName = mergeDeptNameCandidates(beforeBlur, afterBlur)
  editForm.name = deptName
  if (deptName.length < 2 || deptName.length > 10) {
    ElMessage.warning('部门名称长度为2-10位')
    return
  }
  if (editSubmitting.value) return
  editSubmitting.value = true
  try {
    await updateDepartment(editForm.id, deptName)
    ElMessage.success('编辑部门成功')
    editDialogVisible.value = false
    await loadTable()
  } catch (e) {
    const msg = e instanceof Error ? e.message : '编辑失败'
    ElMessage.error(msg)
  } finally {
    editSubmitting.value = false
  }
}

function onCancelAdd(): void {
  addDialogVisible.value = false
}

function onCancelEdit(): void {
  editDialogVisible.value = false
}

function onDeleteRow(row: DepartmentItem): void {
  deleteTargetId.value = row.id
  deleteDialogVisible.value = true
}

function onCancelDelete(): void {
  deleteDialogVisible.value = false
  deleteTargetId.value = null
}

async function onConfirmDelete(): Promise<void> {
  if (deleteTargetId.value === null) {
    onCancelDelete()
    return
  }
  const id = deleteTargetId.value
  if (deleteSubmitting.value) return
  deleteSubmitting.value = true
  try {
    await deleteDepartment(id)
    if (editForm.id === id) {
      editForm.id = 0
      editForm.name = ''
    }
    ElMessage.success('删除部门成功')
    onCancelDelete()
    await loadTable()
  } catch (e) {
    const msg = e instanceof Error ? e.message : '删除失败'
    ElMessage.error(msg)
  } finally {
    deleteSubmitting.value = false
  }
}

function tableIndex(index: number): number {
  return (page.value - 1) * pageSize.value + index + 1
}

onMounted(() => {
  void loadTable()
})
</script>

<template>
  <div class="department-page">
    <div class="section-title">
      <span class="section-title__bar" />
      <span class="section-title__text">部门管理</span>
    </div>

    <el-card shadow="never" class="panel-card">
      <el-form :model="queryForm" inline class="query-form">
        <el-form-item label="部门名称">
          <el-input v-model="queryForm.deptName" clearable placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSearch">查询</el-button>
          <el-button @click="onReset">清空</el-button>
        </el-form-item>
      </el-form>

      <div class="toolbar">
        <el-button type="primary" @click="onClickAddRow">
          <el-icon class="toolbar__icon"><Plus /></el-icon>
          新增部门
        </el-button>
      </div>

      <el-table :data="tableData" v-loading="tableLoading" border stripe>
        <el-table-column type="index" label="序号" width="80" :index="tableIndex" />
        <el-table-column prop="deptName" label="部门名称" />
        <el-table-column prop="lastOperateTime" label="最后操作时间" width="220" />
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button type="primary" link @click="onEditRow(row)">编辑</el-button>
            <el-button type="danger" link @click="onDeleteRow(row)">删除</el-button>
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

    <el-dialog v-model="addDialogVisible" title="新增部门" width="460px" :close-on-click-modal="false">
      <el-form ref="addRef" :model="addForm" :rules="nameRules" label-width="90px">
        <el-form-item label="部门名称" prop="name">
          <div ref="addNameFieldWrapRef" class="dept-name-field-wrap">
            <el-input
              ref="addNameInputRef"
              v-model="addForm.name"
              :maxlength="10"
              placeholder="请输入部门名称，长度为2-10位"
              @compositionend="onAddNameCompositionEnd"
            />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="onCancelAdd">取消</el-button>
        <el-button type="primary" :loading="addSubmitting" @mousedown.prevent @click="onSubmitAdd">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="editDialogVisible" title="编辑部门" width="460px" :close-on-click-modal="false">
      <el-form ref="editRef" :model="editForm" :rules="nameRules" label-width="90px">
        <el-form-item label="部门名称" prop="name">
          <div ref="editNameFieldWrapRef" class="dept-name-field-wrap">
            <el-input
              ref="editNameInputRef"
              v-model="editForm.name"
              :maxlength="10"
              placeholder="请输入部门名称，长度为2-10位"
              @compositionend="onEditNameCompositionEnd"
            />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="onCancelEdit">取消</el-button>
        <el-button type="primary" :loading="editSubmitting" @mousedown.prevent @click="onSubmitEdit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="deleteDialogVisible" title="提示" width="420px" :close-on-click-modal="false">
      <div class="delete-content">您确定要删除该部门的信息吗？</div>
      <template #footer>
        <el-button @click="onCancelDelete">取消</el-button>
        <el-button type="primary" :loading="deleteSubmitting" @click="onConfirmDelete">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.department-page {
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
  font-size: 24px;
  font-weight: 700;
  color: #303133;
}

.panel-card {
  border-radius: 8px;
  border: 1px solid #e4e7ed;
}

.query-form {
  margin-bottom: 8px;
}

.toolbar {
  margin-bottom: 12px;
}

.toolbar__icon {
  margin-right: 6px;
}

.panel-card :deep(.el-table th) {
  background: #f5f7fa;
  color: #303133;
  font-weight: 700;
}

.delete-content {
  text-align: center;
  padding: 12px 0 10px;
  font-size: 16px;
}

.pagination-wrap {
  display: flex;
  justify-content: flex-end;
  padding-top: 12px;
}
</style>

