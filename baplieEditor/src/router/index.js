import { createRouter, createWebHistory } from 'vue-router'
import BaplieViewer from '../views/BaplieViewer.vue'
import HomeView from '../views/HomeView.vue'
import CodecoViewer from '@/views/CodecoViewer.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/baplie',
      name: 'baplie',
      component: BaplieViewer
    },
    {
      path: '/codeco',
      name: 'codeco',
      component: CodecoViewer
    }
  ]
})

export default router
