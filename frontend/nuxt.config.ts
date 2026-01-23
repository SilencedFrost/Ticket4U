// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2025-07-15',
  devtools: { enabled: true },
  modules: ['@nuxt/eslint', '@nuxtjs/i18n', '@pinia/nuxt'],

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
  },

  runtimeConfig: {
    public: {
      authUrl: process.env.NUXT_PUBLIC_AUTH_URL || 'https://localhost:8080/api/v1/auth',
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
