import { createRouter, createWebHistory } from 'vue-router'
import BaplieViewer from '../views/BaplieViewer.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: BaplieViewer
    },
    {
    }
  ]
})

export default router
