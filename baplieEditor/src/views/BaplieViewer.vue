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
            <th>Gross Weight</th>
            <th>VGM Weight</th>
          </tr>
        </thead>
        <tbody v-if="baplieData.containers && baplieData.containers.length > 0">
          <tr v-for="container in baplieData.containers" :key="container.number">
            <td>{{ container.number }}</td>
            <td>{{ container.portOfLoading || '-' }}</td>
            <td>{{ container.portOfDischarge || '-' }}</td>
            <td>{{ container.portOfDestination || '-' }}</td>
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
import axios from 'axios'

export default {
  setup() {
    const baplieData = ref({ ports: [], containers: [] })
    const store = useStore()
    const loading = ref(true)

    onMounted(async () => {
      const ediContent = store.getters.getEdiContent
      if (ediContent) {
        loading.value = true

        // Send the EDI content to the backend to parse
        const formData = new FormData()
        const blob = new Blob([ediContent], { type: 'text/plain' })
        formData.append('file', blob, 'baplie.edi')

        try {
          const response = await axios.post('http://localhost:8080/api/parse/baplie', formData, {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          })
          baplieData.value = response.data
        } catch (error) {
          console.error('Error parsing EDI content:', error)
        } finally {
          loading.value = false
        }
      } else {
        loading.value = false
      }
    })

    return {
      baplieData,
      loading
    }
  }
}
</script>