<template>
  <!--
    PaginationBar: page numbers + prev/next arrows
    Props expected (khi viết script): currentPage, totalPages
  -->
  <div class="am-pagination">
    <button class="am-pagination__btn" disabled>
      <i class="bi bi-chevron-left"></i>
    </button>
    <button class="am-pagination__btn am-pagination__btn--active">1</button>
    <button class="am-pagination__btn">2</button>
    <button class="am-pagination__btn">3</button>
    <button class="am-pagination__btn">
      <i class="bi bi-chevron-right"></i>
    </button>
  </div>
</template>
