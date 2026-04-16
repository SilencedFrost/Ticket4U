<script setup lang="ts">
const { locales } = useI18n();
const switchLocalePath = useSwitchLocalePath();
const router = useRouter();

function switchLang(code: string) {
  const path = switchLocalePath(code);
  if (path) router.push(path);
}
</script>
<template>
  <div class="card bg-reactive-primary overflow-hidden border" style="width: 70px">
    <div
      v-for="item in locales"
      :key="item.code"
      class="menu-item-right"
      @click="switchLang(item.code)"
    >
      <span>{{ item.code.toUpperCase() }}</span>
    </div>
  </div>
</template>
