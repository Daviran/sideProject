<script>
import { ref } from 'vue';

export default {
  setup() {
    const baplieData = ref(null);
    const handleFileChange = (event) => {
      const file = event.target.files[0];
      if (file && file.type === 'text/plain') {
        const reader = new FileReader();
        reader.onload = () => {
            const baplie = reader.result;
            const parsedBaplie = splitTextBySingleQuote(baplie)
          baplieData.value = { content: parsedBaplie};
        };
        reader.readAsText(file);
      } else {
        // Handle error if file is not txt or edi
        console.error('Please select a valid text/edi file.');
      }
    };
    const handleInput =(e) => {
        if(baplieData.value) {
            baplieData.value.content = e.target.innerText;
        }
    };
    const splitTextBySingleQuote = (text) => {
  return text.split("'").join("\n");
};
    const saveAsTxt = () => {
         if (baplieData.value) {
        const blob = new Blob([baplieData.value.content], { type: 'text/plain' });
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
        baplieData,
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
    <div v-if="baplieData !== null">
      <h2>Baplie Content</h2>
      <pre contenteditable="true" @input="handleInput">{{ baplieData.content }}</pre>
      <button @click="saveAsTxt">Save modified Baplie file</button>
    </div>
  </div>
</template>