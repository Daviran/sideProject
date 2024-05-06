import './assets/main.css'
// Vuetify
import '@mdi/font/css/materialdesignicons.css'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'

// Check if state exists in localStorage and initialize store with persisted state
const persistedState = localStorage.getItem('vuex_state')
if (persistedState) {
  store.replaceState(JSON.parse(persistedState))
}

const app = createApp(App)
app.use(vuetify)
app.use(router)
app.use(store)
app.mount('#app')
