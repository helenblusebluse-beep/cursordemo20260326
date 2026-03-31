<!-- 对应附件图5：身份识别、智能手环、床垫/床、声光告警 -->
<script setup lang="ts">
import { computed } from 'vue'

const props = withDefaults(
  defineProps<{
    /** id | wear | bed | alarm */
    kind: 'id' | 'wear' | 'bed' | 'alarm'
    size?: number
  }>(),
  { size: 22 },
)

const dim = computed(() => `${props.size}px`)
const stroke = '#409EFF'
const sw = 1.6
</script>

<template>
  <span class="sb-bed-icon" :style="{ width: dim, height: dim }" aria-hidden="true">
    <!-- 图5-1 身份识别 -->
    <svg v-if="kind === 'id'" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path
        :stroke="stroke"
        :stroke-width="sw"
        d="M8 9h16a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2V11a2 2 0 0 1 2-2z"
      />
      <path :stroke="stroke" :stroke-width="sw" stroke-linecap="round" d="M16 7v2" />
      <rect :stroke="stroke" :stroke-width="sw" x="10" y="13" width="5" height="6" rx="0.5" />
      <path :stroke="stroke" :stroke-width="sw" stroke-linecap="round" d="M17.5 14h6M17.5 17h6M17.5 20h4" />
    </svg>
    <!-- 图5-2 智能手环 -->
    <svg v-else-if="kind === 'wear'" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path
        :stroke="stroke"
        :stroke-width="sw"
        d="M11 8h10a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H11a2 2 0 0 1-2-2V10a2 2 0 0 1 2-2z"
      />
      <path
        :stroke="stroke"
        :stroke-width="sw"
        stroke-linecap="round"
        stroke-linejoin="round"
        d="M11 6v2M21 6v2M11 24v2M21 24v2"
      />
      <path
        :stroke="stroke"
        :stroke-width="sw"
        stroke-linecap="round"
        d="M12 16l2-2 2 3 3-5 2 4"
      />
    </svg>
    <!-- 图5-3 床（侧视） -->
    <svg v-else-if="kind === 'bed'" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path :stroke="stroke" :stroke-width="sw" stroke-linecap="round" d="M22 8v16M10 14h18" />
      <path :stroke="stroke" :stroke-width="sw" d="M6 18h20v6H6z" />
      <path :stroke="stroke" :stroke-width="sw" stroke-linecap="round" d="M8 24v3M24 24v3" />
    </svg>
    <!-- 图5-4 声光告警：半球+闪电+放射线 -->
    <svg v-else viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path
        :stroke="stroke"
        :stroke-width="sw"
        stroke-linejoin="round"
        d="M6 24h20c0-8-4-14-10-18-6 4-10 10-10 18z"
      />
      <path :stroke="stroke" :stroke-width="sw" stroke-linejoin="round" d="M16 11l2 5h-4l2 6" />
      <path :stroke="stroke" :stroke-width="sw" stroke-linecap="round" d="M16 4v2M10 6l1.5 1.5M22 6l-1.5 1.5M7 10l2 1M25 10l-2 1" />
    </svg>
  </span>
</template>

<style scoped>
.sb-bed-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.sb-bed-icon svg {
  width: 100%;
  height: 100%;
}
</style>
