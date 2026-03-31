<script setup lang="ts">
const props = defineProps<{
  /** hr | br | left-count | left-time */
  kind: string
  size?: number
}>()

const stroke = '#1677FF'
const sw = 1.8
</script>

<template>
  <span class="metric-icon" :style="{ width: `${props.size ?? 24}px`, height: `${props.size ?? 24}px` }" aria-hidden="true">
    <!-- 心率：心电+心形 -->
    <svg v-if="kind === 'hr'" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path
        :stroke="stroke"
        :stroke-width="sw"
        stroke-linecap="round"
        stroke-linejoin="round"
        d="M3 12h3l1.6-2.7L10 15l1.7-3h2.1"
      />
      <path
        :stroke="stroke"
        :stroke-width="sw"
        stroke-linecap="round"
        stroke-linejoin="round"
        d="M15.4 6.8c1.4-1.3 3.7-1.2 5 0.2 1.4 1.4 1.4 3.7 0 5.1L12 20.5 3.6 12.1a3.62 3.62 0 0 1 0-5.1c1.3-1.4 3.6-1.5 5-0.2l0.8 0.8"
      />
    </svg>

    <!-- 呼吸率：肺 -->
    <svg v-else-if="kind === 'br'" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path :stroke="stroke" :stroke-width="sw" stroke-linecap="round" d="M12 3v7" />
      <path
        :stroke="stroke"
        :stroke-width="sw"
        stroke-linecap="round"
        stroke-linejoin="round"
        d="M12 10c-2.8.3-5 2.7-5 5.6V21H4.8C3.8 21 3 20.2 3 19.2v-1.4c0-2.9 1.8-5.4 4.5-6.4"
      />
      <path
        :stroke="stroke"
        :stroke-width="sw"
        stroke-linecap="round"
        stroke-linejoin="round"
        d="M12 10c2.8.3 5 2.7 5 5.6V21h2.2c1 0 1.8-.8 1.8-1.8v-1.4c0-2.9-1.8-5.4-4.5-6.4"
      />
    </svg>

    <!-- 离床次数：圆圈+竖条 -->
    <svg v-else-if="kind === 'left-count'" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <circle :stroke="stroke" :stroke-width="sw" cx="12" cy="12" r="9" />
      <path :stroke="stroke" :stroke-width="sw" stroke-linecap="round" d="M9 9v6M12 8v8M15 10v4" />
    </svg>

    <!-- 离床时间：圆圈+时钟 -->
    <svg v-else viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <circle :stroke="stroke" :stroke-width="sw" cx="12" cy="12" r="9" />
      <path :stroke="stroke" :stroke-width="sw" stroke-linecap="round" d="M12 7v5l3 2" />
    </svg>
  </span>
</template>

<style scoped>
.metric-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.metric-icon svg {
  width: 100%;
  height: 100%;
}
</style>
