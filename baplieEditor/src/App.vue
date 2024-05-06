<script setup>
// import BaplieViewer from "./views/BaplieViewer.vue"
// import HomeView from "./views/HomeView.vue";
import BannerHeader from './components/BannerHeader.vue'
</script>

<template>
  <v-app>
    <banner-header></banner-header>
    <v-main>
      <v-container fluid>
        <router-view></router-view>
      </v-container>
    </v-main>
  </v-app>
</template>