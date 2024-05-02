<script>
import { ref } from 'vue';

export default {
  setup() {
    const textObject = ref(null);
    const handleFileChange = (event) => {
      const file = event.target.files[0];
      if (file && file.type === 'text/plain') {
        const reader = new FileReader();
        reader.onload = () => {
            const text = reader.result;
          textObject.value = { content: text};
        };
        reader.readAsText(file);
      } else {
        // Handle error if file is not txt
        console.error('Please select a valid text file.');
      }
    };
    const handleInput =(e) => {
        if(textObject.value) {
            textObject.value.content = e.target.innerText;
        }
    };
    const saveAsTxt = () => {
         if (textObject.value) {
        const blob = new Blob([textObject.value.content], { type: 'text/plain' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'modified_baplie.txt';
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
      }
    }

    return {
        textObject,
        handleFileChange,
        handleInput,
        saveAsTxt
    };
  }
};
</script>

<template>
  <div>
    <input type="file" @change="handleFileChange" accept=".txt">
    <div v-if="textObject !== null">
      <h2>Baplie Content</h2>
      <pre contenteditable="true" @input="handleInput">{{ textObject.content }}</pre>
      <button @click="saveAsTxt">Save modified Baplie file</button>
    </div>
  </div>
</template>