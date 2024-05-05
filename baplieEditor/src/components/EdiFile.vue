<script>
import { ref } from 'vue';
import {useStore} from 'vuex';
export default {
  setup() {
    const ediData = ref(null);
    const store = useStore();
    const preElement = ref(null); // Reference to the pre element
    let originalEdiName = ''; // Variable to store the original filename
    const handleFileChange = (event) => {
      const file = event.target.files[0];
      if (file && file.type === 'text/plain') {
        const reader = new FileReader();
        reader.onload = () => {
            const Edi = reader.result;
            const parsedEdi = splitTextBySingleQuote(Edi)
          ediData.value = { content: parsedEdi};
          store.dispatch('updateEdiContent', parsedEdi); // Dispatch action to update EDI content in store
        };
        console.log('store value:' + store.getters.getEdiContent)
        reader.readAsText(file);
        originalEdiName= file.name;
      } else {
        // Handle error if file is not txt or edi
        console.error('Please select a valid text/edi file.');
      }
    };
    const handleInput =() => {
        if(ediData.value && preElement) {
          const selection = window.getSelection(); // get current selection in the doc
          const selectionStart = selection.anchorOffset; // Get the cursor position before modification
          const selectionEnd = selection.focusOffset;
          const modifiedContent = preElement.value.textContent; // updating the Edi in order to save it

          ediData.value.content = modifiedContent; // Update the content with the modified text
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
         if (ediData.value && preElement) {
        // Update the EdiData.content with the modified text
        const modifiedContent = preElement.value.textContent;
        ediData.value.content = modifiedContent;
        const EdiNameParts = originalEdiName.split('.');
        const modifiedEdidName = EdiNameParts.slice(0,-1).join('.') + '_modified.'+ EdiNameParts.pop();
        const blob = new Blob([modifiedContent], { type: 'text/plain' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = modifiedEdidName;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
      }
    }

    return {
        ediData,
        handleFileChange,
        handleInput,
        saveAsTxt,
        preElement
    };
  }
};
</script>

<template>
  <div class="w-25">
    <v-file-input color="#5865f2" type="file" label="Please insert your file" @change="handleFileChange" accept=".txt">
    </v-file-input>
    <div v-if="ediData !== null">
      <h2>Edi Content</h2>
      <pre ref="preElement" contenteditable="true" @input="handleInput">{{ ediData.content }}</pre>
      <v-btn class="mt-6" color="#5865f2"  @click="saveAsTxt">Save Edi file</v-btn>
    </div>
  </div>
</template>
