<template>
  <footer class="bg-reactive-dark text-white pt-5 pb-4">
    <div class="container-lg">
      <div class="row g-5">
        <div class="col-6 col-lg-3">
          <h6 class="mb-4 text-uppercase fw-bold">
            <span class="d-inline-block pb-3 border-bottom border-3 border-primary">
              {{ $t('common.events') }}
            </span>
          </h6>
          <ul class="list-unstyled d-flex flex-column gap-3 mb-0">
            <li>
              <a
                href="#"
                class="text-clickable user-select-auto text-decoration-none d-inline-flex align-items-center gap-2"
              >
                <span aria-hidden="true">•</span>
                <span>{{ $t('footer.menu.all_events') }}</span>
              </a>
            </li>
            <li>
              <a
                href="#"
                class="text-clickable user-select-auto text-decoration-none d-inline-flex align-items-center gap-2"
              >
                <span aria-hidden="true">•</span>
                <span>{{ $t('footer.menu.popular_events') }}</span>
              </a>
            </li>
            <li>
              <a
                href="#"
                class="text-clickable user-select-auto text-decoration-none d-inline-flex align-items-center gap-2"
              >
                <span aria-hidden="true">•</span>
                <span>{{ $t('footer.menu.upcoming_events') }}</span>
              </a>
            </li>
            <li>
              <a
                href="#"
                class="text-clickable user-select-auto text-decoration-none d-inline-flex align-items-center gap-2"
              >
                <span aria-hidden="true">•</span>
                <span>{{ $t('footer.menu.nearby_events') }}</span>
              </a>
            </li>
            <li>
              <a
                href="#"
                class="text-clickable user-select-auto text-decoration-none d-inline-flex align-items-center gap-2"
              >
                <span aria-hidden="true">•</span>
                <span>{{ $t('footer.menu.free_events') }}</span>
              </a>
            </li>
          </ul>
        </div>

        <div class="col-6 col-lg-3">
          <h6 class="mb-4 text-uppercase fw-bold">
            <span class="d-inline-block pb-3 border-bottom border-3 border-primary">
              {{ $t('common.about_us') }}
            </span>
          </h6>
          <ul class="list-unstyled d-flex flex-column gap-3 mb-0">
            <li>
              <a
                href="#"
                class="text-clickable user-select-auto text-decoration-none d-inline-flex align-items-center gap-2"
              >
                <span aria-hidden="true">•</span>
                <span>{{ $t('footer.menu.our_story') }}</span>
              </a>
            </li>
            <li>
              <a
                href="#"
                class="text-clickable user-select-auto text-decoration-none d-inline-flex align-items-center gap-2"
              >
                <span aria-hidden="true">•</span>
                <span>{{ $t('footer.menu.team') }}</span>
              </a>
            </li>
            <li>
              <a
                href="#"
                class="text-clickable user-select-auto text-decoration-none d-inline-flex align-items-center gap-2"
              >
                <span aria-hidden="true">•</span>
                <span>{{ $t('footer.menu.careers') }}</span>
              </a>
            </li>
            <li>
              <a
                href="#"
                class="text-clickable user-select-auto text-decoration-none d-inline-flex align-items-center gap-2"
              >
                <span aria-hidden="true">•</span>
                <span>{{ $t('footer.menu.press_media') }}</span>
              </a>
            </li>
            <li>
              <a
                href="#"
                class="text-clickable user-select-auto text-decoration-none d-inline-flex align-items-center gap-2"
              >
                <span aria-hidden="true">•</span>
                <span>{{ $t('footer.menu.contact_us') }}</span>
              </a>
            </li>
          </ul>
        </div>

        <div class="col-6 col-lg-3">
          <h6 class="mb-4 text-uppercase fw-bold">
            <span class="d-inline-block pb-3 border-bottom border-3 border-primary">
              {{ $t('footer.partners') }}
            </span>
          </h6>
          <ul class="list-unstyled d-flex flex-column gap-3 mb-0">
            <li>
              <a
                href="#"
                class="text-clickable user-select-auto text-decoration-none d-inline-flex align-items-center gap-2"
              >
                <span aria-hidden="true">•</span>
                <span>{{ $t('footer.menu.become_partner') }}</span>
              </a>
            </li>
            <li>
              <a
                href="#"
                class="text-clickable user-select-auto text-decoration-none d-inline-flex align-items-center gap-2"
              >
                <span aria-hidden="true">•</span>
                <span>{{ $t('footer.menu.sponsorship') }}</span>
              </a>
            </li>
            <li>
              <a
                href="#"
                class="text-clickable user-select-auto text-decoration-none d-inline-flex align-items-center gap-2"
              >
                <span aria-hidden="true">•</span>
                <span>{{ $t('footer.menu.partner_directory') }}</span>
              </a>
            </li>
            <li>
              <a
                href="#"
                class="text-clickable user-select-auto text-decoration-none d-inline-flex align-items-center gap-2"
              >
                <span aria-hidden="true">•</span>
                <span>{{ $t('footer.menu.partner_login') }}</span>
              </a>
            </li>
          </ul>
        </div>

        <div class="col-6 col-lg-3">
          <h6 class="mb-4 text-uppercase fw-bold">
            <span class="d-inline-block pb-3 border-bottom border-3 border-primary">
              {{ $t('common.contact') }}
            </span>
          </h6>

          <div class="mb-3">
            <div class="small text-uppercase fw-bold text-reactive-secondary mb-1">
              {{ $t('footer.labels.phone') }}
            </div>
            <div class="fw-bold">0912 345 678</div>
          </div>
          <div>
            <div class="small text-uppercase fw-bold text-reactive-secondary mb-1">
              {{ $t('footer.labels.email') }}
            </div>
            <a
              href="mailto:supportagent@gmail.com"
              class="link-primary text-decoration-none fw-bold"
            >
              supportagent@gmail.com
            </a>
          </div>
        </div>
      </div>

      <hr class="text-white-50 opacity-25" />

      <div class="d-flex justify-content-end gap-4">
        <a href="#" class="text-clickable user-select-auto text-decoration-none">
          {{ $t('footer.links.privacy_policy') }}
        </a>
        <a href="#" class="text-clickable user-select-auto text-decoration-none">
          {{ $t('footer.links.terms_of_use') }}
        </a>
      </div>
    </div>
  </footer>
</template>
