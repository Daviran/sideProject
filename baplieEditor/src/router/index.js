import { createRouter, createWebHistory } from 'vue-router'
import BaplieViewer from '../views/BaplieViewer.vue'
import HomeView from '../views/HomeView.vue'
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
    }
    // {
    //   path: '/editor',
    //   name: 'editor',
    //   component: EditorView
    // },
  ]
})

export default router
