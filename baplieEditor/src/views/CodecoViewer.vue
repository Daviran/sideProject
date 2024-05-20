<template>
    <div>
      <h2>CODECO Viewer</h2>
      <div v-if="codecoData">
        <table v-if="!loading">
          <thead>
            <tr>
              <th>Container Number</th>
              <th>Port of Loading</th>
              <th>Port of Discharge</th>
              <th>Container Carrier</th>
              <th>Gross Weight</th>
              <th>VGM Weight</th>
            </tr>
          </thead>
          <tbody v-if="codecoData.containers && codecoData.containers.length > 0">
            <tr v-for="container in codecoData.containers" :key="container.number">
              <td>{{ container.number }}</td>
              <td>{{ container.portOfLoading || '-' }}</td>
              <td>{{ container.portOfDischarge || '-' }}</td>
              <td>{{ container.containerCarrier || '-' }}</td>
              <td>{{ container.grossWeight || '-' }}</td>
              <td>{{ container.vgmWeight || '-' }}</td>
            </tr>
          </tbody>
          <tbody v-else>
            <tr>
              <td colspan="6">No container data available</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div v-else>
        <h5>No CODECO data found!</h5>
      </div>
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
          } catch (error) {
            console.error('Error parsing CODECO content:', error)
          } finally {
            loading.value = false
          }
        } else {
          loading.value = false
        }
      })
  
      return {
        codecoData,
        loading
      }
    }
  }
  </script>
  