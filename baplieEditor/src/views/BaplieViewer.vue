<template>
  <div>
    <h2>Baplie Viewer</h2>
    <table>
      <thead>
        <tr>
          <th>Container Number</th>
          <th>Port of Loading</th>
          <th>Port of Destination</th>
          <th>Port of Call</th>
          <th>Container Carrier</th>
          <th>Gross Weight</th>
          <th>VGM Weight</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="container in baplieData.containers" :key="container.number">
          <td>{{ container.number }}</td>
          <td>{{ container.portOfLoading ? container.portOfLoading.name : '-' }}</td>
          <td>{{ container.portOfDestination ? container.portOfDestination.name : '-' }}</td>
          <td>{{ container.portOfCall ? container.portOfCall.name : '-' }}</td>
          <td>{{ container.containerCarrier || '-' }}</td>
          <td>{{ container.grossWeight || '-' }}</td>
          <td>{{ container.vgmWeight || '-' }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import { parseBaplieData } from '../utils/baplieParser'

export default {
  setup() {
    const baplieData = ref(null)
    const store = useStore()

    onMounted(() => {
      const ediContent = store.getters.getEdiContent
      if (ediContent) {
        baplieData.value = parseBaplieData(ediContent)
        console.log(baplieData.value)
      }
    })

    return {
      baplieData
    }
  }
}
</script>

