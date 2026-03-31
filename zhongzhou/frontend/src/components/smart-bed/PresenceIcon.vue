<!-- 对应附件图2-4：清醒中、已离床、睡眠中（圆+白线稿，下配文案由父级展示） -->
<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  /** 清醒中 | 已离床 | 睡眠中 */
  label: string
  size?: number
}>()

const kind = computed(() => {
  if (props.label === '已离床') return 'left'
  if (props.label === '睡眠中') return 'sleep'
  if (props.label === '清醒中') return 'awake'
  return 'awake'
})

const dim = computed(() => `${props.size ?? 56}px`)
const fill = '#8EC5FF'
const line = '#FFFFFF'
const sw = 1.8
</script>

<template>
  <span class="presence-wrap" :style="{ width: dim, height: dim }" aria-hidden="true">
    <!-- 图2 清醒中：坐姿 -->
    <svg v-if="kind === 'awake'" viewBox="0 0 56 56" xmlns="http://www.w3.org/2000/svg">
      <circle :fill="fill" cx="28" cy="28" r="26" />
      <g :stroke="line" :stroke-width="sw" fill="none" stroke-linecap="round" stroke-linejoin="round">
        <path d="M14 38h28" />
        <path d="M18 38V24h20v14" />
        <path d="M22 24V18h12v6" />
        <circle cx="28" cy="12" r="4" />
        <path d="M24 20l4 6 4-6" />
        <path d="M26 30v8M30 30v8" />
      </g>
    </svg>
    <!-- 图3 已离床：空床 -->
    <svg v-else-if="kind === 'left'" viewBox="0 0 56 56" xmlns="http://www.w3.org/2000/svg">
      <circle :fill="fill" cx="28" cy="28" r="26" />
      <g :stroke="line" :stroke-width="sw" fill="none" stroke-linecap="round" stroke-linejoin="round">
        <path d="M12 36h32" />
        <path d="M16 36V26h24v10" />
        <path d="M20 26V20h16v6" />
      </g>
    </svg>
    <!-- 图4 睡眠中：卧姿 -->
    <svg v-else viewBox="0 0 56 56" xmlns="http://www.w3.org/2000/svg">
      <circle :fill="fill" cx="28" cy="28" r="26" />
      <g :stroke="line" :stroke-width="sw" fill="none" stroke-linecap="round" stroke-linejoin="round">
        <path d="M10 34h36" />
        <path d="M14 34V28h28v6" />
        <ellipse cx="22" cy="22" rx="6" ry="5" />
        <path d="M28 22h12" />
        <path d="M18 30h20" />
      </g>
    </svg>
  </span>
</template>

<style scoped>
.presence-wrap {
  display: inline-flex;
  align-items: center;
  justify-content: center;
}
.presence-wrap svg {
  width: 100%;
  height: 100%;
}
</style>
