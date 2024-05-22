import { createStore } from 'vuex'

export default createStore({
  state: {
    ediContent: '',
    ediType:'',
    baplieJson: {},
    codecoJson: {}
  },
  getters: {
    getEdiContent: state => state.ediContent,
    getEdiType: state => state.ediType,
    getBaplieJson: state => state.baplieJson,
    getCodecoJson: state => state.codecoJson,
  },
  mutations: {
    updateEdiContent(state, ediContent) {
      state.ediContent = ediContent
      state.ediType = detectEdiType(ediContent);
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
function detectEdiType(ediContent) {
  const lines = ediContent.split('\n');
  const unhSegment = lines.find(line => line.startsWith('UNH'));
  if (unhSegment) {
    if (unhSegment.includes('BAPLIE')) {
      return 'BAPLIE';
    } else if (unhSegment.includes('CODECO')) {
      return 'CODECO';
    } else if (unhSegment.includes('PRESTOW')) {
      return 'PRESTOW';
    } else if (unhSegment.includes('COARRI')) {
      return 'COARRI';
    } else {
      return 'UNKNOWN';
    }
  }
  return 'UNKNOWN';
}