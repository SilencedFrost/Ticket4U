<template>
    <!-- Footer Section -->
    <footer class="py-4 bg-reactive-gray">
      <div class="container-xxl">
        <div class="row g-4 mb-4">
          <div class="col-6 col-md-3">
            <h6 class="text-white fw-bold mb-3 small">{{ $t('footer.hotline') }}</h6>
            <ul class="list-unstyled">
              <li class="mb-2 text-white small"><i class="bi bi-calendar text-white me-2 small"></i>{{ $t('footer.hotline_hours') }}</li>
              <li class="mb-2"><a href="tel:1900xxxx" class="text-white text-decoration-none small footer-link">
                <i class="bi bi-telephone text-white me-2 small"></i>
                1900 xxxx
              </a></li>
              <h6 class="text-white fw-bold my-2 small">Email</h6>
              <li><a href="mailto:support@ticket4u.vn" class="text-white text-decoration-none small footer-link">
                <i class="bi bi-envelope text-white me-2 small"></i>
                support@ticket4u.vn
              </a></li>
              <h6 class="text-white fw-bold my-2 small">{{ $t('footer.head_office') }}</h6>
              <li class="mb-2 text-white small">
                <i class="bi bi-building text-white me-2 small"></i>
                {{ $t('footer.address') }}
            </li>
            </ul>
            </div>

            <div class="col-6 col-md-3">
                <h6 class="text-white fw-bold mb-3 small">{{ $t('footer.customer_service') }}</h6>
                <ul class="list-unstyled">
                <li class="mb-2"><a href="#" class="text-white text-decoration-none small footer-link">{{ $t('footer.terms') }}</a></li>
                </ul>
            </div>

            <div class="col-6 col-md-3">
                <h6 class="text-white fw-bold mb-3 small">{{ $t('footer.organize_service') }}</h6>
                <ul class="list-unstyled">
                <li class="mb-2"><a href="#" class="text-white text-decoration-none small footer-link">{{ $t('footer.create_event') }}</a></li>
                </ul>
            </div>

            <div class="col-6 col-md-3">
                <h6 class="text-white fw-bold mb-3 small">{{ $t('footer.about_us') }}</h6>
                <ul class="list-unstyled">
                <li class="mb-2"><a href="#" class="text-white text-decoration-none small footer-link">{{ $t('footer.code_of_conduct') }}</a></li>
                <li class="mb-2"><a href="#" class="text-white text-decoration-none small footer-link">{{ $t('footer.privacy_policy') }}</a></li>
                <li class="mb-2"><a href="#" class="text-white text-decoration-none small footer-link">{{ $t('footer.payment_security') }}</a></li>
                <li class="mb-2"><a href="#" class="text-white text-decoration-none small footer-link">{{ $t('footer.dispute_resolution') }}</a></li>
                <li class="mb-2"><a href="#" class="text-white text-decoration-none small footer-link">{{ $t('footer.return_policy') }}</a></li>
                <li class="mb-2"><a href="#" class="text-white text-decoration-none small footer-link">{{ $t('footer.shipping_terms') }}</a></li>
                <li class="mb-2"><a href="#" class="text-white text-decoration-none small footer-link">{{ $t('footer.payment_methods') }}</a></li>
                </ul>
            </div>
        </div>
      </div>
    </footer>
</template>

<style scoped>
/* Footer link hover effect */
.footer-link {
  transition: opacity 0.3s;
}

.footer-link:hover {
  opacity: 0.8;
}
</style>