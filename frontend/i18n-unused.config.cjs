// i18n-unused.config.cjs
/** @type {import('i18n-unused').RunOptions} */
module.exports = {
  /** Path to your translation files */
  localesPath: 'i18n/locales',

  /** Path to scan for translation usage */
  srcPath: '.',

  /** Allowed extensions for source files to scan */
  srcExtensions: ['js', 'ts', 'vue', 'java'],

  /** Paths to ignore during scanning */
  ignorePaths: ['node_modules', '.nuxt', '.output', 'dist', '.git', 'coverage', 'public'],

  /**
   * Regex to match translation keys with at least one dot
   * Matches: 'key.second', "auth.login.title"
   * Does NOT match: 'key', "title"
   */
  translationKeyMatcher: /['"](\w+\.[\w.]+)['"]/g,

  // Does not support display-missed
};
