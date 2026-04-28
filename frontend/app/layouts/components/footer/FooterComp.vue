<template>
  <footer class="bg-reactive-dark text-white py-5">
    <div class="container-lg">
      <div class="row g-4">
        <div class="col-12 col-md-3">
          <div class="mb-2">
            <img src="/logo-primary.png" alt="Ticket4U" class="mb-3" style="max-height: 70px" />
          </div>

          <div class="d-flex flex-column">
            <h6 class="text-uppercase fw-bold mb-3 small">{{ $t('common.follow_us') }}</h6>
            <div class="d-flex gap-2">
              <a
                href="#"
                class="btn btn-outline-primary rounded-circle d-flex align-items-center justify-content-center"
                style="width: 40px; height: 40px"
                aria-label="Facebook"
              >
                <i class="bi bi-facebook"></i>
              </a>
              <a
                href="#"
                class="btn btn-outline-primary rounded-circle d-flex align-items-center justify-content-center"
                style="width: 40px; height: 40px"
                aria-label="YouTube"
              >
                <i class="bi bi-youtube"></i>
              </a>
              <a
                href="#"
                class="btn btn-outline-primary rounded-circle d-flex align-items-center justify-content-center"
                style="width: 40px; height: 40px"
                aria-label="Instagram"
              >
                <i class="bi bi-instagram"></i>
              </a>
            </div>
          </div>
        </div>

        <div class="col-12 col-md-6">
          <div class="mb-4">
            <h5 class="fw-bold mb-3">{{ $t('common.about_us') }}</h5>
            <p class="text-reactive-secondary small lh-base">
              {{ $t('brand.description') }}
            </p>
          </div>
        </div>

        <div class="col-12 col-md-3">
          <div class="mb-4">
            <h5 class="fw-bold mb-3">{{ $t('common.contact') }}</h5>
            <div class="mb-3">
              <p class="text-reactive-secondary mb-1 small">{{ $t('common.phone') }}:</p>
              <p class="text-white fw-bold mb-0 small">0912345678</p>
            </div>
            <div>
              <p class="text-reactive-secondary mb-1 small">{{ $t('common.email') }}:</p>
              <p class="text-white fw-bold mb-0 small">support@ticket4u.com</p>
            </div>
          </div>
        </div>
      </div>

      <hr class="my-4 text-white-50" />

      <div class="row">
        <div class="col-12 text-center">
          <p class="text-reactive-secondary mb-0 small">{{ $t('brand.copyright') }}</p>
        </div>
      </div>
    </div>
  </footer>
</template>
