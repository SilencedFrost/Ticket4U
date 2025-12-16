<template>
    <!-- Footer Section -->
    <footer class="py-4" style="background-color: #5e5e5e">
      <div class="container-xxl">
        <div class="row g-4 mb-4">
          <div class="col-6 col-md-3">
            <h6 class="text-white fw-bold mb-3" style="font-size: 0.95rem">{{ $t('footer.hotline') }}</h6>
            <ul class="list-unstyled">
              <li class="mb-2 text-white" style="font-size: 0.9rem"><i class="bi bi-calendar" style="color: white; margin-right: 0.5rem; font-size: 0.9rem"></i>{{ $t('footer.hotline_hours') }}</li>
              <li class="mb-2"><a href="tel:1900xxxx" class="text-white text-decoration-none" style="font-size: 0.9rem; transition: color 0.3s">
                <i class="bi bi-telephone" style="color: white; margin-right: 0.5rem; font-size: 0.9rem"></i>
                1900 xxxx
              </a></li>
              <h6 class="text-white fw-bold my-2" style="font-size: 0.95rem">Email</h6>
              <li><a href="mailto:support@ticket4u.vn" class="text-white text-decoration-none" style="font-size: 0.9rem; transition: color 0.3s">
                <i class="bi bi-envelope" style="color: white; margin-right: 0.5rem; font-size: 0.9rem"></i>
                support@ticket4u.vn
              </a></li>
              <h6 class="text-white fw-bold my-2" style="font-size: 0.95rem">{{ $t('footer.head_office') }}</h6>
              <li class="mb-2 text-white" style="font-size: 0.9rem">
                <i class="bi bi-building" style="color: white; margin-right: 0.5rem; font-size: 0.9rem"></i>
                {{ $t('footer.address') }}
            </li>
            </ul>
            </div>

            <div class="col-6 col-md-3">
                <h6 class="text-white fw-bold mb-3" style="font-size: 0.95rem">{{ $t('footer.customer_service') }}</h6>
                <ul class="list-unstyled">
                <li class="mb-2"><a href="#" class="text-white text-decoration-none" style="font-size: 0.9rem; transition: color 0.3s">{{ $t('footer.terms') }}</a></li>
                </ul>
            </div>

            <div class="col-6 col-md-3">
                <h6 class="text-white fw-bold mb-3" style="font-size: 0.95rem">{{ $t('footer.organize_service') }}</h6>
                <ul class="list-unstyled">
                <li class="mb-2"><a href="#" class="text-white text-decoration-none" style="font-size: 0.9rem; transition: color 0.3s">{{ $t('footer.create_event') }}</a></li>
                </ul>
            </div>

            <div class="col-6 col-md-3">
                <h6 class="text-white fw-bold mb-3" style="font-size: 0.95rem">{{ $t('footer.about_us') }}</h6>
                <ul class="list-unstyled">
                <li class="mb-2"><a href="#" class="text-white text-decoration-none" style="font-size: 0.9rem; transition: color 0.3s">{{ $t('footer.code_of_conduct') }}</a></li>
                <li class="mb-2"><a href="#" class="text-white text-decoration-none" style="font-size: 0.9rem; transition: color 0.3s">{{ $t('footer.privacy_policy') }}</a></li>
                <li class="mb-2"><a href="#" class="text-white text-decoration-none" style="font-size: 0.9rem; transition: color 0.3s">{{ $t('footer.payment_security') }}</a></li>
                <li class="mb-2"><a href="#" class="text-white text-decoration-none" style="font-size: 0.9rem; transition: color 0.3s">{{ $t('footer.dispute_resolution') }}</a></li>
                <li class="mb-2"><a href="#" class="text-white text-decoration-none" style="font-size: 0.9rem; transition: color 0.3s">{{ $t('footer.return_policy') }}</a></li>
                <li class="mb-2"><a href="#" class="text-white text-decoration-none" style="font-size: 0.9rem; transition: color 0.3s">{{ $t('footer.shipping_terms') }}</a></li>
                <li class="mb-2"><a href="#" class="text-white text-decoration-none" style="font-size: 0.9rem; transition: color 0.3s">{{ $t('footer.payment_methods') }}</a></li>
                </ul>
            </div>
        </div>
      </div>
    </footer>
</template>