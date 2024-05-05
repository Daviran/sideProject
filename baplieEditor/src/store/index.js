import { createStore } from 'vuex';

const store = createStore({
  state: {
    ediContent: null, // Initial state for EDI file content
  },
  mutations: {
    setEdiContent(state, content) {
      state.ediContent = content;
    },
  },
  actions: {
    updateEdiContent({ commit }, content) {
      commit('setEdiContent', content);
    },
  },
  getters: {
    getEdiContent(state) {
      return state.ediContent;
    },
  },
});
store.subscribe((mutation, state) => {
    localStorage.setItem('vuex_state',JSON.stringify(state))
})
export default store;