// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2025-07-15',
  devtools: { enabled: true },
  modules: ['@nuxt/eslint', '@nuxtjs/i18n', '@pinia/nuxt', '@nuxt/image'],

  hooks: {
    'pages:extend'(pages) {
      if (process.env.NODE_ENV === 'production') {
        const devPages = pages.filter((page) => page.file?.includes('pages/dev'));
        devPages.forEach((page) => {
          const index = pages.indexOf(page);
          if (index !== -1) pages.splice(index, 1);
        });
      }
    },
  },

  $development: {
    hooks: {
      'nitro:build:before'(_nitro) {
        process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';
      },
    },
  },

  i18n: {
    locales: [
      { code: 'en', name: 'English', file: 'en.json' },
      { code: 'vi', name: 'Tiếng Việt', file: 'vi.json' },
    ],
    defaultLocale: 'en',
    langDir: './locales/',
    strategy: 'prefix_except_default',
    vueI18n: './i18n.config.ts',
  },

  vite: {
    css: {
      preprocessorOptions: {
        scss: {
          quietDeps: true,
        },
      },
    },
    optimizeDeps: {
      include: [
        '@vueuse/core',
        '@vueuse/gesture',
      ]
    },
  },

  runtimeConfig: {
    public: {
      userServiceUrl: 'https://localhost:8080/api/v1',
      userHealthUrl: 'https://localhost:8080/health',
      ticketServiceUrl: 'https://localhost:8081/api/v1',
      ticketHealthUrl: 'https://localhost:8081/health',
      eventServiceUrl: 'https://localhost:8083/api/v1',
      eventHealthUrl: 'https://localhost:8083/health',
      googleClientId: '',
    },
  },

  devServer: {
    https: {
      key: './keys/localhost+2-key.pem',
      cert: './keys/localhost+2.pem',
    },
  },

  css: ['~/assets/styles/bootstrap-custom.scss', '~/assets/styles/other-custom.css'],

  app: {
    head: {
      link: [
        {
          rel: 'stylesheet',
          href: 'https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;500;700&display=swap',
        },
      ],
    },
  },
});
