<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { createUser, deleteUser, listUsers, updateUser, type UserItem } from '@/api/user'

const loading = ref(false)
const rows = ref<UserItem[]>([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const query = reactive({ keyword: '' })
const dialogVisible = ref(false)
const deleteDialogVisible = ref(false)
const submitting = ref(false)
const deleteSubmitting = ref(false)
const editingId = ref<number | null>(null)
const deleteTargetId = ref<number | null>(null)
const form = reactive({
  username: '',
  password: '',
  displayName: '',
  roleName: '普通用户',
  status: 1,
})

async function loadTable(): Promise<void> {
  loading.value = true
  try {
    const data = await listUsers({
      keyword: query.keyword,
      page: page.value,
      pageSize: pageSize.value,
    })
    rows.value = data.rows
    total.value = Number(data.total)
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '查询失败')
  } finally {
    loading.value = false
  }
}

function onSearch(): void {
  page.value = 1
  void loadTable()
}

function onReset(): void {
  query.keyword = ''
  page.value = 1
  void loadTable()
}

function onClickAdd(): void {
  editingId.value = null
  form.username = ''
  form.password = ''
  form.displayName = ''
  form.roleName = '普通用户'
  form.status = 1
  dialogVisible.value = true
}

function onEditRow(row: UserItem): void {
  editingId.value = row.id
  form.username = row.username
  form.password = ''
  form.displayName = row.displayName
  form.roleName = row.roleName
  form.status = row.status
  dialogVisible.value = true
}

function onDeleteRow(row: UserItem): void {
  deleteTargetId.value = row.id
  deleteDialogVisible.value = true
}

async function onSubmit(): Promise<void> {
  if (!form.username.trim() || !form.displayName.trim() || !form.roleName.trim()) {
    ElMessage.warning('请完整填写用户信息')
    return
  }
  if (editingId.value === null && !form.password.trim()) {
    ElMessage.warning('新增用户必须填写密码')
    return
  }
  if (submitting.value) return
  submitting.value = true
  try {
    if (editingId.value === null) {
      await createUser({
        username: form.username.trim(),
        password: form.password.trim(),
        displayName: form.displayName.trim(),
        roleName: form.roleName.trim(),
        status: form.status,
      })
      ElMessage.success('新增用户成功')
    } else {
      await updateUser(editingId.value, {
        username: form.username.trim(),
        password: form.password.trim() || undefined,
        displayName: form.displayName.trim(),
        roleName: form.roleName.trim(),
        status: form.status,
      })
      ElMessage.success('编辑用户成功')
    }
    dialogVisible.value = false
    await loadTable()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '保存失败')
  } finally {
    submitting.value = false
  }
}

async function onConfirmDelete(): Promise<void> {
  if (deleteTargetId.value === null) {
    deleteDialogVisible.value = false
    return
  }
  if (deleteSubmitting.value) return
  deleteSubmitting.value = true
  try {
    await deleteUser(deleteTargetId.value)
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

onMounted(() => {
  void loadTable()
})
</script>

<template>
  <div class="user-page">
    <div class="section-title">
      <span class="section-title__bar" />
      <span class="section-title__text">用户管理</span>
    </div>

    <el-card shadow="never">
      <el-form :model="query" inline>
        <el-form-item label="关键字">
          <el-input v-model="query.keyword" clearable placeholder="用户名/姓名" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSearch">查询</el-button>
          <el-button @click="onReset">重置</el-button>
        </el-form-item>
      </el-form>

      <div class="toolbar">
        <el-button type="primary" @click="onClickAdd">新增用户</el-button>
      </div>

      <el-table :data="rows" border v-loading="loading">
        <el-table-column prop="id" label="ID" width="90" />
        <el-table-column prop="username" label="用户名" min-width="120" />
        <el-table-column prop="displayName" label="姓名" min-width="120" />
        <el-table-column prop="roleName" label="角色" min-width="120" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button type="primary" link @click="onEditRow(row)">编辑</el-button>
            <el-button type="danger" link @click="onDeleteRow(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pager">
        <el-pagination
          background
          :current-page="page"
          :page-size="pageSize"
          :page-sizes="[10, 20, 50]"
          layout="sizes, prev, pager, next, jumper, total"
          :total="total"
          @current-change="(p:number)=>{page=p; void loadTable()}"
          @size-change="(s:number)=>{pageSize=s; page=1; void loadTable()}"
        />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="editingId === null ? '新增用户' : '编辑用户'" width="520px" :close-on-click-modal="false">
      <el-form label-width="90px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" maxlength="20" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input
            v-model="form.password"
            maxlength="64"
            show-password
            :placeholder="editingId === null ? '请输入密码' : '不修改请留空'"
          />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.displayName" maxlength="20" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="角色">
          <el-input v-model="form.roleName" maxlength="20" placeholder="请输入角色" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="onSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="deleteDialogVisible" title="提示" width="420px" :close-on-click-modal="false">
      <div class="delete-content">您确定要删除该用户吗？</div>
      <template #footer>
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="deleteSubmitting" @click="onConfirmDelete">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.user-page { padding: 14px 12px 18px; }
.section-title { display: flex; align-items: center; gap: 10px; margin: 4px 0 14px; }
.section-title__bar { width: 6px; height: 22px; background: #409eff; border-radius: 2px; }
.section-title__text { font-size: 24px; font-weight: 700; color: #303133; }
.toolbar { margin-bottom: 12px; }
.delete-content { text-align: center; padding: 12px 0 10px; font-size: 16px; }
.pager { display: flex; justify-content: flex-end; padding-top: 12px; }
</style>
