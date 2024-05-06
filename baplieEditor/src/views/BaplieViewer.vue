<template>
  <div>
    <h1>Baplie Viewer</h1>
    <div v-if="baplieData">
      <h2>Ports</h2>
      <ul>
        <li v-for="port in baplieData.ports" :key="port.code">{{ port.name }} ({{ port.code }})</li>
      </ul>

      <h2>Containers</h2>
      <ul>
        <li v-for="container in baplieData.containers" :key="container.number">{{ container.number }}</li>
      </ul>

    </div>
    <div v-else>
      <p>No Baplie data available.</p>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useStore } from 'vuex';
import { parseBaplieData } from '../utils/baplieParser'; // Assuming the parser function is stored in a separate file

export default {
  setup() {
    const baplieData = ref(null);
    const store = useStore();

    onMounted(() => {
      const ediContent = store.getters.getEdiContent;
      if (ediContent) {
        baplieData.value = parseBaplieData(ediContent);
      }
    });

    return {
      baplieData
    };
  }
};
</script>

<style>
/* Add your custom styles here */
</style>
