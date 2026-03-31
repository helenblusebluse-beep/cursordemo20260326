import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

/** 本地后端端口被占用时可设环境变量，例如 ZHONGZHOU_API_TARGET=http://localhost:8081 */
const apiTarget = process.env.ZHONGZHOU_API_TARGET ?? 'http://localhost:8080'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5175,
    proxy: {
      '/api': {
        target: apiTarget,
        changeOrigin: true,
      },
    },
  },
})
