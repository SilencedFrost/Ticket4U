<template>
  <!-- 
    Usage: <StatusBadge status="active" />
    Props expected (khi viết script): status: 'active' | 'inactive' | 'pending'
    Hiện tại dùng slot để truyền nội dung
  -->
  <span class="am-status" :class="statusClass">
    <span class="am-status__dot"></span>
    <slot>{{ label }}</slot>
  </span>
</template>
