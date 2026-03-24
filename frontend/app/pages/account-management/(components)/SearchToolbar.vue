<template>
  <!--
    SearchToolbar: search input + slot bên phải cho action buttons
    Props expected (khi viết script): placeholder: string
    Slots: #actions - cho buttons bên phải
  -->
  <div class="am-toolbar">
    <div class="am-toolbar__search">
      <i class="bi bi-search am-toolbar__search-icon"></i>
      <input type="text" class="am-toolbar__search-input" placeholder="Tìm kiếm..." />
    </div>
    <div class="am-toolbar__spacer"></div>
    <slot name="actions"></slot>
  </div>
</template>
