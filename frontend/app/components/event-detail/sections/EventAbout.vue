<script setup lang="ts">
defineProps<{
  eventData: any,
  aboutImage: string
}>();
const expandAbout = ref(false);
</script>
<template>
    <section id="about-section" class="p-3 p-md-4 card bg-reactive-secondary  m-3 mx-auto mw-100">
        <div class="container-xxl">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h3 class="text-reactive-primary fw-bold mb-0 h4">{{ $t('event-detail.about_event') }}</h3>
            </div>
            <div class="border-dark rounded-3 overflow-hidden mb-4 about-expandable" :class="{'expanded': expandAbout}">
                <img :src="aboutImage" alt="About" class="card-img-top card"/>
                <div class="bg-reactive-secondary p-3 p-md-4">
                    <h5 class="fw-bold mb-3">1. Giới thiệu chung</h5>
                    <p class="text-reactive-primary-50">Thông tin giới thiệu về sự kiện sẽ được hiển thị tại đây với chi tiết đầy đủ về chương trình và các điểm nổi bật</p>
                
                    <h5 class="fw-bold mb-3">2. Chi tiết sự kiện</h5>
                    <p class="text-reactive-primary-50">Chi tiết sự kiện sẽ được hiển thị tại đây bao gồm thông tin về các nghệ sĩ, danh sách chương trình</p>
                
                    <h5 class="fw-bold mb-3">3. Điều Khoản & Điều kiện</h5>
                    <p class="text-reactive-primary-50">Điều khoản sẽ được hiển thị tại đây cho khách hàng tham khảo trước khi mua vé</p>
                </div>
            </div>
            <button @click="expandAbout = !expandAbout" class="btn btn-sm btn-outline-info fw-bold mx-auto d-block">
                {{ expandAbout ? $t('event-detail.see_less') : $t('event-detail.see_more') }}
            </button>
        </div>
    </section>
</template>

<style scoped>
.about-expandable {
  max-height: 250px;
  transition: max-height 2.0s;
}

.about-expandable.expanded {
  max-height: 5000px;
}
</style>