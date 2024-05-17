import { createStore } from 'vuex'

export default createStore({
  state: {
    ediContent: '',
    ediJson: null,
  },
  mutations: {
    setEdiContent(state, content) {
      state.ediContent = content
    },
    setEdiJson(state, json) {
      state.ediJson = json
    },
  },
  actions: {
    updateEdiContent({ commit }, content) {
      commit('setEdiContent', content)
    },
    updateEdiJson({ commit }, json) {
      commit('setEdiJson', json)
    },
  },
  getters: {
    getEdiContent: (state) => state.ediContent,
    getEdiJson: (state) => state.ediJson,
  },
})
