<script>
import { ref,onMounted } from 'vue';
import {useStore} from 'vuex';
export default {
  setup() {
    const ediData = ref(null);
    const store = useStore();
    const hiddenInput = ref(null);
    const preElement = ref(null); // Reference to the pre element
    let originalEdiName = ''; // Variable to store the original filename
    const loadEdiContentFromStore = () => {
      // Retrieve EDI content from the Vuex store
      const storedEdiContent = store.getters.getEdiContent;
      ediData.value = { content: storedEdiContent };
      hiddenInput.value.value = storedEdiContent; // Set value of hidden input
    };
    onMounted(loadEdiContentFromStore);

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
    const clearData = () => {
      ediData.value = null;
      store.dispatch('updateEdiContent', ''); // Clear store data
      preElement.value.textContent = ''; // Clear pre content
      hiddenInput.value.value = ''; // Clear hidden input value
    };

    return {
        ediData,
        handleFileChange,
        handleInput,
        saveAsTxt,
        preElement,
        hiddenInput,
        clearData
    };
  }
};
</script>

<template>

  <div class="w-50">
    <input type="hidden" ref="hiddenInput" :value="ediData ? ediData.content : ''" >
    <v-file-input v-if="ediData === null" color="#5865f2" type="file" label="Please insert your file" @change="handleFileChange" accept=".txt"  :disabled="ediData !== null">
    </v-file-input>
    <div v-if="ediData !== null">
      <h2>Edi Content</h2>
      <v-row>
        <v-col cols="8">
           <pre ref="preElement" contenteditable="true" @input="handleInput">{{ ediData.content }}</pre>
        </v-col>
        <v-col cols="4">
          <v-btn class="mb-2" color="#5865f2"  @click="saveAsTxt">Save Edi file</v-btn>
      <v-btn class="mt-2 " color="red" @click="clearData" :disabled="ediData === null">Clear Edi</v-btn>
        </v-col>
      </v-row>
      <div>
        
      </div>
    </div>
  </div>

</template>