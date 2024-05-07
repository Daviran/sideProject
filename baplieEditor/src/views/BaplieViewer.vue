<template>
  <div>
    <h2>Baplie Viewer</h2>
    <div v-if="baplieData">
      <table v-if="!loading">
        <thead>
          <tr>
            <th>Container Number</th>
            <th>Port of Loading</th>
            <th>Port of Discharge</th>
            <th>Port of Destination</th>
            <th>Container Carrier</th>
            <th>Gross Weight</th>
            <th>VGM Weight</th>
          </tr>
        </thead>
        <tbody v-if="baplieData.containers && baplieData.containers.length > 0">
          <tr v-for="container in baplieData.containers" :key="container.number">
            <td>{{ container.number }}</td>
            <td>{{ container.portOfLoading ? container.portOfLoading.name : '-' }}</td>
            <td>{{ container.portOfDischarge ? container.portOfDischarge.name : '-' }}</td>
            <td>{{ container.portOfDestination ? container.portOfDestination.name : '-' }}</td>
            <td>{{ container.containerCarrier || '-' }}</td>
            <td>{{ container.grossWeight || '-' }}</td>
            <td>{{ container.vgmWeight || '-' }}</td>
          </tr>
        </tbody>
        <tbody v-else>
          <tr>
            <td colspan="7">No container data available</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-else>
      <h5>No Baplie data found!</h5>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import { parseBaplieData } from '../utils/baplieParser'

export default {
  setup() {
    const baplieData = { ports: [], containers: [] }
    const store = useStore()
    const loading = ref(true)

    onMounted(() => {
      const ediContent = store.getters.getEdiContent
      if (ediContent) {
        loading.value = true
        setTimeout(() => {
            
          const parsedData = parseBaplieData(ediContent)
          console.log(parsedData.containers)
          Object.assign(baplieData, parsedData)
          loading.value = false
        }, 1000)
      }
    })

    return {
      baplieData,
      loading
    }
  }
}
</script>

