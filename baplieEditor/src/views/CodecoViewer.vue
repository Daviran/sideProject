<template>
  <div>
    <h2>Codeco Viewer</h2>
    <v-data-table
      v-if="!loading && codecoData.containers.length > 0"
      :headers="headers"
      :items="codecoData.containers"
      class="elevation-1"
    >
      <template v-slot:item="{ item }">
        <tr>
          <td>{{ item.containerNumber }}</td>
          <td>{{ item.dateTime || '-' }}</td>
          <td>{{ item.location || '-' }}</td>
          <td>{{ item.equipmentType || '-' }}</td>
          <td>{{ item.fullEmptyIndicator || '-' }}</td>
          <td>{{ item.sealNumber || '-' }}</td>
          <td>{{ item.weight || '-' }}</td>
        </tr>
      </template>
      <template v-slot:no-data>
        <v-alert type="info">No container data available</v-alert>
      </template>
    </v-data-table>
    <v-alert v-else-if="!loading" type="error">No Codeco data found!</v-alert>
    <v-progress-linear v-else :indeterminate="true"></v-progress-linear>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import axios from 'axios'

export default {
  setup() {
    const codecoData = ref({ containers: [] })
    const store = useStore()
    const loading = ref(true)
    const headers = [
      { title: 'Container Number', key: 'containerNumber' },
      { title: 'Date/Time', key: 'dateTime' },
      { title: 'Location', key: 'location' },
      { title: 'Equipment Type', key: 'equipmentType' },
      { title: 'Full/Empty Indicator', key: 'fullEmptyIndicator' },
      { title: 'Seal Number', key: 'sealNumber' },
      { title: 'Weight', key: 'weight' }
    ]

    onMounted(async () => {
      const ediContent = store.getters.getEdiContent
      if (ediContent) {
        loading.value = true

        // Send the EDI content to the backend to parse
        const formData = new FormData()
        const blob = new Blob([ediContent], { type: 'text/plain' })
        formData.append('file', blob, 'codeco.edi')

        try {
          const response = await axios.post('http://localhost:8080/api/parse/codeco', formData, {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          })
          codecoData.value = response.data
          console.log(codecoData.value)
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
      codecoData,
      loading,
      headers
    }
  }
}
</script>

<style scoped>
.elevation-1 {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>