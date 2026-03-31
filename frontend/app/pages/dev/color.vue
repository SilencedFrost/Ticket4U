<script setup lang="ts">
import { useTheme } from '#imports';

const { currentTheme, toggleTheme } = useTheme();

const nextTheme = computed(() => (currentTheme.value === 'light' ? 'dark' : 'light'));
</script>

<template>
  <div class="container-fluid d-flex flex-column p-2">
    <button class="btn btn-secondary d-flex align-items-center mb-2" @click="toggleTheme">
      Switch to {{ nextTheme }} Mode
    </button>
    <div class="card p-2 mb-2">
      <h4 class="text-reactive-primary mb-0">Header thẻ</h4>
      <div class="text-reactive-secondary text-small">subtitle thẻ</div>
      <hr class="m-0 my-2 p-0 text-reactive-secondary" />
      <div class="text-reactive-primary">Đây là chữ màu trắng</div>
      <div class="text-primary">Đây là chữ màu primary</div>
      <div class="text-reactive-secondary">Đây là chữ màu secondary</div>
    </div>
    <div class="card p-2 mb-2">
      <div class="mb-2">
        <div class="text-reactive-primary">Đây là chữ màu trắng</div>
        <div class="text-primary">Đây là chữ màu primary</div>
        <div class="text-reactive-secondary">Đây là chữ màu secondary</div>
      </div>
      <div class="d-flex flex-column">
        <button class="btn btn-primary mb-2">Đây là nút màu primary</button>
        <button class="btn btn-secondary mb-2">Đây là nút màu secondary</button>
        <button class="btn btn-danger mb-2">Đây là nút màu danger</button>
        <button class="btn btn-success mb-2">Đây là nút màu success</button>
        <button class="btn btn-warning mb-2">Đây là nút màu warning</button>
      </div>
      <div class="card card-info p-2 border-0">
        <a href="#"> Đây là thẻ a, chứa url </a>
      </div>
    </div>
  </div>
</template>
