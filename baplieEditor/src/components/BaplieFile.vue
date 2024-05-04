<script>
import { ref } from 'vue';

export default {
  setup() {
    const baplieData = ref(null);
    const preElement = ref(null); // Reference to the pre element
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
    const handleInput =() => {
        if(baplieData.value && preElement.value) {
          const selection = window.getSelection();
          const selectionStart = selection.anchorOffset; // Get the cursor position before modification
            baplieData.value.content = preElement.value.innerText;
            restoreCursorPosition(selection, selectionStart);
        }
    };
    const restoreCursorPosition = (selection, selectionStart) => {
      selection.collapse(selection.anchorNode, selectionStart); // Restore the cursor position
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
        saveAsTxt,
        preElement
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