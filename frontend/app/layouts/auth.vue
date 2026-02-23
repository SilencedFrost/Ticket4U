<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';

const imageUrl: string = '/logo-gray.png';
const opacity: number = 0.1;
const repeat: number = 150;

const width = ref(0);
const height = ref(0);

onMounted(() => {
    if (typeof window !== 'undefined') {
        width.value = window.innerWidth;
        height.value = window.innerHeight;

        const handleResize = () => {
            width.value = window.innerWidth;
            height.value = window.innerHeight;
        };

        window.addEventListener('resize', handleResize);
        onBeforeUnmount(() => window.removeEventListener('resize', handleResize));
    }
});

const horizontalImageCount = computed<number>(() =>
    width.value ? Math.ceil(width.value / repeat) + 1 : 0,
);

const verticalImageCount = computed<number>(() =>
    height.value ? Math.ceil(height.value / repeat / 2) : 0,
);
</script>

<template>
    <div class="d-flex">
        <client-only>
            <template v-if="width && height">
                <div v-for="verticalIndex in verticalImageCount" :key="`v-${verticalIndex}`">
                    <div v-for="horizontalIndex in horizontalImageCount" :key="`h-${horizontalIndex}`">
                        <img :src="imageUrl" :style="{
                            maxWidth: `${repeat}px`,
                            left: `${repeat * (horizontalIndex - 1)}px`,
                            top: `${repeat * (verticalIndex * 2 - 2)}px`,
                            opacity: opacity,
                        }" class="position-fixed p-3" loading="lazy" />
                        <img :src="imageUrl" :style="{
                            maxWidth: `${repeat}px`,
                            left: `${repeat * (horizontalIndex - 1.5)}px`,
                            top: `${repeat * (verticalIndex * 2 - 1)}px`,
                            opacity: opacity,
                        }" class="position-fixed p-3" loading="lazy" />
                    </div>
                </div>
            </template>
        </client-only>
        <div class="row g-0 p-0 flex-fill z-1">
            <div class="col-lg-6 col-12">
                <div class="d-flex justify-content-center align-items-center h-100">
                    <div id="smallscreen-layout" class="d-lg-none d-block card p-3 bg-reactive-primary border">
                        <nuxt-page />
                    </div>
                    <div id="radial-background" class="justify-content-center align-items-center d-none d-lg-flex flex-fill h-100 flex-column" style="
              background: radial-gradient(
                circle,
                rgb(from var(--bg-reactive-primary) r g b / 0.7) 20%,
                transparent 100%
              );
            ">
                        <div id="motto">
                            <h1 class="text-reactive-primary fw-bold">{{ $t('brand.name') }}</h1>
                            {{ $t('brand.motto') }}
                        </div>
                    </div>
                </div>
            </div>
            <div id="fullscreen-layout" class="col-lg-6 d-none d-lg-block border-start">
                <div class="d-flex justify-content-center align-items-center h-100 bg-reactive-primary">
                    <nuxt-page />
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.auth-gradient {
    background: radial-gradient(circle,
            rgb(from var(--bg-reactive-primary) r g b / 0.7) 20%,
            transparent 100%);
}
</style>
