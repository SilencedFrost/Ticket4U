<script setup lang="ts">
import NavBar from './components/navigation/NavBar.vue';
import FooterComp from './components/footer/FooterComp.vue';
const { currentTheme } = useTheme();
</script>

<template>
    <div class="d-flex flex-column min-vh-100">
        <header class="sticky-top z-3">
            <nav-bar />
        </header>
        <div class="flex-fill">
            <main :class="[
                { 'bg-reactive-primary': currentTheme == 'dark' },
                { 'bg-reactive-secondary': currentTheme == 'light' },
            ]" style="min-height: 100%">
                <nuxt-page />
            </main>
            <footer><footer-comp /></footer>
        </div>
    </div>
</template>
