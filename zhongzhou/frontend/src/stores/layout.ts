import { defineStore } from 'pinia'

export const useLayoutStore = defineStore('layout', {
  state: () => ({
    collapsed: false,
  }),
  actions: {
    toggleCollapsed() {
      this.collapsed = !this.collapsed
    },
  },
})
