import { createRouter, createWebHistory } from 'vue-router'
import store from '../store'
import BaplieViewer from '../views/BaplieViewer.vue'
import HomeView from '../views/HomeView.vue'
import CodecoViewer from '@/views/CodecoViewer.vue'

const routes = [
  { path: '/', component: HomeView },
  { path: '/codeco', component: CodecoViewer },
  { path: '/baplie', component: BaplieViewer }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const ediContent = store.getters.getEdiContent
  const ediType = store.getters.getEdiType

  if (!ediContent) {
    if (to.path !== '/') {
      // If no EDI content is present, redirect to home
      return next('/')
    }
  } else {
    if (to.path === '/codeco' && ediType !== 'CODECO') {
      // If trying to access /codeco but EDI is not CODECO, redirect to home
      return next('/')
    } else if (to.path === '/baplie' && ediType !== 'BAPLIE') {
      // If trying to access /baplie but EDI is not BAPLIE, redirect to home
      return next('/')
    }
  }

  // Allow the navigation
  next()
})

export default router