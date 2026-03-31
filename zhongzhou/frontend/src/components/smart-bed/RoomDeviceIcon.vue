<!-- 对应附件图1：智能烟感、智能摄像机、门窗传感器（温湿度映射为摄像机位） -->
<script setup lang="ts">
import { computed } from 'vue'

const props = withDefaults(
  defineProps<{
    /** smoke | camera(图1中) | door | alarm(房间声光，样式参考告警灯) */
    kind: 'smoke' | 'camera' | 'door' | 'alarm'
    size?: number
  }>(),
  { size: 32 },
)

const dim = computed(() => `${props.size}px`)
const stroke = '#409EFF'
const sw = 1.75
</script>

<template>
  <span class="sb-room-icon" :style="{ width: dim, height: dim }" aria-hidden="true">
    <!-- 图1-1 智能烟感 -->
    <svg v-if="kind === 'smoke'" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
      <circle :stroke="stroke" :stroke-width="sw" cx="16" cy="14" r="10" />
      <circle :stroke="stroke" :stroke-width="sw" cx="16" cy="14" r="6" />
      <circle :stroke="stroke" :stroke-width="sw" cx="16" cy="14" r="2.5" />
      <path
        :stroke="stroke"
        :stroke-width="sw"
        stroke-linecap="round"
        d="M8 14a8 8 0 0 1 2-5M24 14a8 8 0 0 1-2-5M10 20a8 8 0 0 0 12 0"
      />
      <path :stroke="stroke" :stroke-width="sw" stroke-linecap="round" d="M16 24v4M13 28h6" />
    </svg>
    <!-- 图1-2 智能摄像机 -->
    <svg v-else-if="kind === 'camera'" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
      <circle :stroke="stroke" :stroke-width="sw" cx="16" cy="15" r="11" />
      <circle :stroke="stroke" :stroke-width="sw" cx="16" cy="15" r="5.5" />
      <circle :fill="stroke" cx="16" cy="7" r="1.2" />
      <path
        :stroke="stroke"
        :stroke-width="sw"
        stroke-linecap="round"
        d="M9 25c2-2 4-3 7-3s5 1 7 3"
      />
    </svg>
    <!-- 图1-3 门窗传感器 -->
    <svg v-else-if="kind === 'door'" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
      <rect :stroke="stroke" :stroke-width="sw" x="8" y="6" width="7" height="20" rx="1" />
      <rect :stroke="stroke" :stroke-width="sw" x="17" y="9" width="7" height="17" rx="1" />
    </svg>
    <!-- 房间声光报警：与床位告警同系（图5-4 简化） -->
    <svg v-else viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path
        :stroke="stroke"
        :stroke-width="sw"
        stroke-linejoin="round"
        d="M16 4l-2 6h4l-2-6zM10 12h12l2 14H8l2-14z"
      />
      <path :stroke="stroke" :stroke-width="sw" stroke-linecap="round" d="M16 18v4M14 8l-2 2M18 8l2 2" />
    </svg>
  </span>
</template>

<style scoped>
.sb-room-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.sb-room-icon svg {
  width: 100%;
  height: 100%;
}
</style>
