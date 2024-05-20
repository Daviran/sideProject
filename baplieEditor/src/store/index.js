import { createStore } from 'vuex'

export default createStore({
  state: {
    ediContent: '',
    baplieJson: {},
    codecoJson: {}
  },
  getters: {
    getEdiContent: state => state.ediContent,
    getBaplieJson: state => state.baplieJson,
    getCodecoJson: state => state.codecoJson,
  },
  mutations: {
    updateEdiContent(state, ediContent) {
      state.ediContent = ediContent
    },
    updateBaplieJson(state, baplieJson) {
      state.baplieJson = baplieJson
    },
    updateCodecoJson(state, codecoJson) {
      state.codecoJson = codecoJson
    }
  },
  actions: {
    updateEdiContent({ commit }, ediContent) {
      commit('updateEdiContent', ediContent)
    },
    updateBaplieJson({ commit }, baplieJson) {
      commit('updateBaplieJson', baplieJson)
    },
    updateCodecoJson({ commit }, codecoJson) {
      commit('updateCodecoJson', codecoJson)
    }
  },
  modules: {}
})
