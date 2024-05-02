<script>
import { ref } from 'vue';

export default {
  setup() {
    const fileContent = ref(null);

    const handleFileChange = (event) => {
      const file = event.target.files[0];
      if (file && file.type === 'text/plain') {
        const reader = new FileReader();
        reader.onload = () => {
          fileContent.value = reader.result;
        };
        reader.readAsText(file);
      } else {
        // Handle error if file is not txt
        console.error('Please select a valid text file.');
      }
    };

    return {
      fileContent,
      handleFileChange
    };
  }
};
</script>

<template>
  <div>
    <input type="file" @change="handleFileChange" accept=".txt">
    <div v-if="fileContent !== null">
      <h2>Baplie Content</h2>
      <pre>{{ fileContent }}</pre>
    </div>
  </div>
</template>
