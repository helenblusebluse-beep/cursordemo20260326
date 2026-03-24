import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    port: 5174,
    proxy: {
      '/api': {
        target: 'http://localhost:8092',
        changeOrigin: true,
      },
      '/login': {
        target: 'http://localhost:8092',
        changeOrigin: true,
      },
      // 本机头像静态资源（与 app.avatar.local-url-prefix 一致）
      '/uploads': {
        target: 'http://localhost:8092',
        changeOrigin: true,
      },
    },
  },
})
