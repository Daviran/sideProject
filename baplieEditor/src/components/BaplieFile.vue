<script>
import { ref } from 'vue';

export default {
  setup() {
    const baplieData = ref(null);
    const preElement = ref(null); // Reference to the pre element
    let originalBaplieName = ''; // Variable to store the original filename
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
        originalBaplieName= file.name;
      } else {
        // Handle error if file is not txt or edi
        console.error('Please select a valid text/edi file.');
      }
    };
    const handleInput =() => {
        if(baplieData.value && preElement) {
          const selection = window.getSelection(); // get current selection in the doc
          const selectionStart = selection.anchorOffset; // Get the cursor position before modification
          const selectionEnd = selection.focusOffset;
          const modifiedContent = preElement.value.textContent; // updating the baplie in order to save it

          baplieData.value.content = modifiedContent; // Update the content with the modified text
          // we needed to set a timeout in order to move the cursor back to the selection. The issue
          // with no timeout is that the DOM has not yet finished its update
          // therefore putting the cursor at the beginning of the txt..

          setTimeout(() => {
             // Restore the cursor position after modification
              const range = document.createRange(); // the range object is used to select a portion of txt in order to perform operations
              const textNode = preElement.value.childNodes[0]; // textNode = first thing in the preElement 'box'
              range.setStart(textNode, selectionStart);
              range.setEnd(textNode, selectionEnd);
               selection.removeAllRanges();
              selection.addRange(range);
               }, 0);
    };
  }
    const splitTextBySingleQuote = (text) => {
  return text.split("'").join("\n");
};
    const saveAsTxt = () => {
         if (baplieData.value && preElement) {
        // Update the baplieData.content with the modified text
        const modifiedContent = preElement.value.textContent;
        baplieData.value.content = modifiedContent;
        const baplieNameParts = originalBaplieName.split('.');
        const modifiedBapliedName = baplieNameParts.slice(0,-1).join('.') + '_modified.'+ baplieNameParts.pop();
        const blob = new Blob([modifiedContent], { type: 'text/plain' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = modifiedBapliedName;
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
      <pre ref="preElement" contenteditable="true" @input="handleInput">{{ baplieData.content }}</pre>
      <button @click="saveAsTxt">Save modified Baplie file</button>
    </div>
  </div>
</template>
<style>
  .container {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    background-color: #f8f8f8;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  }

  input[type="file"] {
    margin-bottom: 20px;
  }

  button {
    padding: 10px 20px;
    background-color: #007bff;
    color: #fff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }

  button:hover {
    background-color: #0056b3;
  }
</style>

