This side project consists of : 
- A front end in Vue 3 Composition API, which has the VueX store implemented. 
Its purpose is to edit the Users EDI and save it, display correctly data relevant to an EDI such as Baplie or CODECO. The parsing is done with the backend.

- A Back end in Java 17, with Maven & Spring Boot. Its purpose is to receive the EDI from the frontend, parse it correctly, and serve it back to the front end as a proper json format.

- WIP : Baplie Parser ( almost done). Must analyze CODECO,PRESTOW,COARRI now.
