<script setup lang="ts">
import { ref } from 'vue'

const visible = ref(true)

function onCancel() {
  visible.value = false
}

function onConfirm() {
  // 演示：不执行真实删除
  visible.value = false
}
</script>

<template>
  <div class="delete-demo">
    <el-dialog v-model="visible" title="删除班级" width="420px" :close-on-click-modal="false">
      <div class="delete-demo__content">您确定要删除该班级吗？</div>
      <template #footer>
        <el-button @click="onCancel">取消</el-button>
        <el-button type="primary" @click="onConfirm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.delete-demo {
  padding: 18px 16px;
}

.delete-demo__content {
  padding: 8px 0 6px;
  text-align: center;
  color: #303133;
}
</style>

