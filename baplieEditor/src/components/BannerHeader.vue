<template>
  <div>
    <v-app-bar color="primary" prominent>
      <v-app-bar-nav-icon variant="text" @click.stop="drawer = !drawer"></v-app-bar-nav-icon>

      <v-toolbar-title
        ><h4 @click="backToHome" class="pointer w-25">EDI Editor</h4></v-toolbar-title
      >
    </v-app-bar>

    <v-navigation-drawer
      v-model="drawer"
      :location="$vuetify.display.mobile ? 'bottom' : undefined"
      temporary
    >
      <v-list-item-group>
        <v-list-item
          v-for="(item, index) in items"
          :key="index"
          @click="handleItemClick(item.value)"
        >
          <v-list-item-title>{{ item.title }}</v-list-item-title>
        </v-list-item>
      </v-list-item-group>
    </v-navigation-drawer>
  </div>
</template>
  <script>
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router' // Import useRouter from Vue Router
export default {
  setup() {
    const router = useRouter() // Get the router instance
    // Define reactive variables using ref

    const drawer = ref(false)
    const group = ref(null)
    const items = [
      {
        title: 'Baplie Viewer',
        value: 'BaplieViewer'
      },
      {
        title: 'Prestow Viewer',
        value: 'PrestowViewer'
      },
      {
        title: 'Codeco Viewer',
        value: 'CodecoViewer'
      },
      {
        title: 'Coarri Viewer',
        value: 'CoarriViewer'
      }
    ]

    // Watch for changes in the 'group' variable
    watch(group, () => {
      drawer.value = false // Set 'drawer' to false when 'group' changes
    })
    const handleItemClick = (value) => {
      console.log('clicked' + value)
      switch (value) {
        case 'BaplieViewer':
          router.push('/baplie')
          break
        case 'PrestowViewer':
          router.push('/prestow')
          break
        default:
          break
      }
    }
    const backToHome = () => {
      router.push('/')
    }
    // Return the reactive variables and methods
    return {
      drawer,
      group,
      items,
      handleItemClick,
      backToHome
    }
  }
}
</script>
  <style>
.pointer {
  cursor: pointer;
}
</style>