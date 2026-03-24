<script setup lang="ts">
type FieldRule = {
  name: string
  required: string
  type: string
  limit: string
  allow: string
  unique: string
  hint: string
}

const fieldRules: FieldRule[] = [
  {
    name: '班级名称',
    required: '是',
    type: '输入框',
    limit: '4-30',
    allow: '汉字/字母/数字',
    unique: '是',
    hint: '班级名称排除法',
  },
  {
    name: '班级数量',
    required: '是',
    type: '输入框',
    limit: '1-20',
    allow: '数字',
    unique: '否',
    hint: '-',
  },
  {
    name: '开课时间',
    required: '是',
    type: '日期选择',
    limit: '-',
    allow: '选择开课时间（如：2025-01-01）',
    unique: '否',
    hint: '-',
  },
  {
    name: '结课时间',
    required: '是',
    type: '日期选择',
    limit: '-',
    allow: '选择结课时间（如：2025-03-01）',
    unique: '否',
    hint: '-',
  },
  {
    name: '班主任',
    required: '否',
    type: '下拉框',
    limit: '-',
    allow: '选择下拉项',
    unique: '否',
    hint: '-',
  },
  {
    name: '学科',
    required: '是',
    type: '下拉框',
    limit: '-',
    allow: '选择 Java/JavaEE/Python/GO/前端',
    unique: '否',
    hint: '-',
  },
]
</script>

<template>
  <div class="req-demo">
    <el-card shadow="never" class="req-demo__card">
      <div class="doc-title">班级管理-需求说明</div>
      <p class="doc-paragraph">班级管理页面，管理员可以对班级信息进行管理，包括新增、修改、删除等操作。</p>

      <h3 class="doc-heading">页面开发规则</h3>
      <ol class="doc-list">
        <li>
          <b>班级查询</b>：
          <div class="doc-list__item">1. 输入班级名称进行搜索，支持模糊查询。</div>
          <div class="doc-list__item">2. 选择结果时间区间进行查询，区间内开课时间将被筛选。</div>
          <div class="doc-list__item">3. 查询结果进行分页展示。</div>
        </li>
        <li>
          <b>新增班级</b>：
          <div class="doc-list__item">1. 点击新增班级，会打开新增班级弹窗。</div>
          <div class="doc-list__item">2. 按字段限制要求完成数据输入。</div>
        </li>
        <li>
          <b>删除班级</b>：
          <div class="doc-list__item">1. 弹出删除确认框，提示“您确定要删除该班级吗？”。</div>
          <div class="doc-list__item">2. 确认后删除成功；取消则不删除。</div>
        </li>
      </ol>

      <div class="doc-subtitle">字段限制说明（示例）</div>
      <el-table :data="fieldRules" border size="small" class="doc-table">
        <el-table-column prop="name" label="字段名称" />
        <el-table-column prop="required" label="必填" width="70" />
        <el-table-column prop="type" label="类型" width="140" />
        <el-table-column prop="limit" label="长度/范围" width="120" />
        <el-table-column prop="allow" label="输入限制" />
        <el-table-column prop="unique" label="是否唯一" width="90" />
        <el-table-column prop="hint" label="提示语句" />
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.req-demo {
  padding: 18px 16px;
}

.req-demo__card {
  border-radius: 8px;
}

.doc-title {
  font-size: 22px;
  font-weight: 900;
  color: #303133;
  margin-bottom: 10px;
}

.doc-paragraph {
  margin: 0 0 14px;
  color: #606266;
  font-size: 13px;
  line-height: 1.7;
}

.doc-heading {
  margin: 0 0 10px;
  font-size: 18px;
  font-weight: 800;
  color: #303133;
}

.doc-list {
  margin: 0;
  padding-left: 18px;
  color: #606266;
  font-size: 13px;
  line-height: 1.8;
}

.doc-list__item {
  margin-top: 6px;
}

.doc-subtitle {
  margin-top: 16px;
  font-size: 15px;
  font-weight: 800;
  color: #303133;
}

.doc-table :deep(th) {
  background: #f5f7fa;
  color: #303133;
  font-weight: 800;
}
</style>

