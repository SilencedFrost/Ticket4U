import type { Config } from 'prettier';

const config: Config = {
  semi: true,
  trailingComma: 'all',
  singleQuote: true,
  printWidth: 100,
  tabWidth: 2,
  useTabs: false,

  overrides: [
    {
      files: '*.vue',
      options: {
        htmlWhitespaceSensitivity: 'css',
      },
    },
    {
      files: '*.json',
      options: {
        printWidth: 80,
      },
    },
  ],
};

export default config;
