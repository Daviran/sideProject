<template>
  <div>
    <h2>Baplie Viewer</h2>
    <v-text-field
      v-model="search"
      label="Search a container"
      class="mb-4"
    ></v-text-field>
    <v-data-table
      v-if="!loading && baplieData.containers.length > 0"
      :headers="headers"
      :items="baplieData.containers"
      :search="search"
      class="elevation-1"
    >
      <template v-slot:item="{ item }">
        <tr>
          <td>{{ item.number }}</td>
          <td>{{ item.portOfLoading || '-' }}</td>
          <td>{{ item.portOfDischarge || '-' }}</td>
          <td>{{ item.portOfDestination || '-' }}</td>
          <td>{{ item.grossWeight || '-' }}</td>
          <td>{{ item.vgmWeight || '-' }}</td>
          <td>{{ item.position || '-' }}</td>
        </tr>
      </template>
      <template v-slot:no-data>
        <v-alert type="info">No container data available</v-alert>
      </template>
    </v-data-table>
    <v-alert v-else-if="!loading" type="error">No Baplie data found!</v-alert>
    <v-progress-linear v-else :indeterminate="true"></v-progress-linear>
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
    const search = ref('') // Add the search ref
    const headers = [
      { title: 'Container', key: 'number' },
      { title: 'Port of Loading', key: 'portOfLoading' },
      { title: 'Port of Discharge', key: 'portOfDischarge' },
      { title: 'Port of Destination', key: 'portOfDestination' },
      { title: 'Gross Weight', key: 'grossWeight' },
      { title: 'VGM Weight', key: 'vgmWeight' },
      { title: 'Position', key: 'position' }
    ]

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
          console.log(baplieData.value)
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
      loading,
      headers,
      search // Add search to the returned values
    }
  }
}
</script>

<style scoped>
.mb-4 {
  margin-bottom: 16px;
}
</style>
