<script setup lang="ts">
import type { FormInstance } from 'element-plus'
import { ref } from 'vue'

const teachers = ['赵敏', '周老师', '刘老师', '陈老师']
const subjects = ['Java', 'JavaEE', 'Python', 'GO', '前端']

const addForm = ref({
  className: '',
  classQuantity: undefined as number | undefined,
  startDate: '',
  endDate: '',
  teacher: '',
  subject: '',
})

const editForm = ref({
  className: 'JavaEE班级100期',
  classQuantity: 308,
  startDate: '2022-08-01',
  endDate: '2023-02-15',
  teacher: '赵敏',
  subject: 'Java',
})

const addRef = ref<FormInstance | null>(null)
const editRef = ref<FormInstance | null>(null)
</script>

<template>
  <div class="form-demo">
    <div class="section-title">
      <span class="section-title__bar" />
      <span class="section-title__text">班级管理 - 表单对照</span>
    </div>

    <el-row :gutter="28">
      <el-col :xs="24" :md="12">
        <el-card shadow="never" class="form-demo__card">
          <div class="card-title">
            <span class="card-title__bar" />
            <span>新增班级</span>
          </div>

          <el-form ref="addRef" :model="addForm" label-width="110px" class="demo-form" :disabled="true">
            <el-form-item label="班级名称">
              <el-input v-model="addForm.className" placeholder="请输入班级名称，如：JavaEE班级100期" />
            </el-form-item>

            <el-form-item label="班级数量">
              <el-input v-model.number="addForm.classQuantity" type="number" min="1" placeholder="请输入班级数量" />
            </el-form-item>

            <el-form-item label="开课时间">
              <el-date-picker v-model="addForm.startDate" type="date" placeholder="选择开课时间" value-format="YYYY-MM-DD" />
            </el-form-item>

            <el-form-item label="结课时间">
              <el-date-picker v-model="addForm.endDate" type="date" placeholder="选择结课时间" value-format="YYYY-MM-DD" />
            </el-form-item>

            <el-form-item label="班主任">
              <el-select v-model="addForm.teacher" placeholder="请选择" style="width: 100%">
                <el-option v-for="t in teachers" :key="t" :label="t" :value="t" />
              </el-select>
            </el-form-item>

            <el-form-item label="学科">
              <el-select v-model="addForm.subject" placeholder="请选择" style="width: 100%">
                <el-option v-for="s in subjects" :key="s" :label="s" :value="s" />
              </el-select>
            </el-form-item>

            <div class="demo-form__buttons">
              <el-button type="primary">保存</el-button>
              <el-button>取消</el-button>
            </div>
          </el-form>
        </el-card>
      </el-col>

      <el-col :xs="24" :md="12">
        <el-card shadow="never" class="form-demo__card">
          <div class="card-title">
            <span class="card-title__bar" />
            <span>修改班级</span>
          </div>

          <el-form ref="editRef" :model="editForm" label-width="110px" class="demo-form" :disabled="true">
            <el-form-item label="班级名称">
              <el-input v-model="editForm.className" />
            </el-form-item>

            <el-form-item label="班级数量">
              <el-input v-model.number="editForm.classQuantity" type="number" min="1" />
            </el-form-item>

            <el-form-item label="开课时间">
              <el-date-picker v-model="editForm.startDate" type="date" value-format="YYYY-MM-DD" />
            </el-form-item>

            <el-form-item label="结课时间">
              <el-date-picker v-model="editForm.endDate" type="date" value-format="YYYY-MM-DD" />
            </el-form-item>

            <el-form-item label="班主任">
              <el-select v-model="editForm.teacher" style="width: 100%">
                <el-option v-for="t in teachers" :key="t" :label="t" :value="t" />
              </el-select>
            </el-form-item>

            <el-form-item label="学科">
              <el-select v-model="editForm.subject" style="width: 100%">
                <el-option v-for="s in subjects" :key="s" :label="s" :value="s" />
              </el-select>
            </el-form-item>

            <div class="demo-form__buttons">
              <el-button type="primary">保存</el-button>
              <el-button>取消</el-button>
            </div>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.form-demo {
  padding: 18px 16px;
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

.form-demo__card {
  border-radius: 8px;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 16px;
  font-weight: 800;
  color: #303133;
  margin-bottom: 10px;
}

.card-title__bar {
  width: 4px;
  height: 14px;
  background: #409eff;
  border-radius: 1px;
}

.demo-form :deep(.el-form-item__label) {
  font-weight: 600;
}

.demo-form__buttons {
  display: flex;
  justify-content: center;
  gap: 28px;
  padding-top: 18px;
}
</style>

