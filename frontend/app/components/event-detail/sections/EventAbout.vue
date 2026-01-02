<script setup lang="ts">
defineProps<{
  eventData: any;
  aboutImage: string;
}>();
const expandAbout = ref(false);
</script>
<template>
  <section id="about-section" class="p-3 p-md-4 card m-3 mx-auto mw-100">
    <div class="container-xxl">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h3 class="text-reactive-primary fw-bold mb-0 h4">{{ $t('event-detail.about_event') }}</h3>
      </div>
      <div
        class="border-dark overflow-hidden mb-4 about-expandable rounded-2"
        :class="{ expanded: expandAbout }"
      >
        <img :src="aboutImage" alt="About" class="img-fluid rounded-2" />
        <div class="p-3 p-md-4">
          <h5 class="fw-bold mb-3">1. Giới thiệu chung</h5>
          <p class="text-reactive-primary">
            Thông tin giới thiệu về sự kiện sẽ được hiển thị tại đây với chi tiết đầy đủ về chương
            trình và các điểm nổi bật
          </p>

          <h5 class="fw-bold mb-3">2. Chi tiết sự kiện</h5>
          <p class="text-reactive-primary">
            Chi tiết sự kiện sẽ được hiển thị tại đây bao gồm thông tin về các nghệ sĩ, danh sách
            chương trình
          </p>

          <h5 class="fw-bold mb-3">3. Điều Khoản & Điều kiện</h5>
          <p class="text-reactive-primary">
            Điều khoản sẽ được hiển thị tại đây cho khách hàng tham khảo trước khi mua vé
          </p>
        </div>
      </div>
      <button class="btn btn-outline-primary fw-bold mx-auto" @click="expandAbout = !expandAbout">
        {{ expandAbout ? $t('event-detail.see_less') : $t('event-detail.see_more') }}
      </button>
    </div>
  </section>
</template>

<style scoped>
.about-expandable {
  max-height: 250px;
  transition: max-height 2s;
}

.about-expandable.expanded {
  max-height: 5000px;
}
</style>
