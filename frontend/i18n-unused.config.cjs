// i18n-unused.config.cjs
/** @type {import('i18n-unused').RunOptions} */
module.exports = {
  /** Path to your translation files */
  localesPath: 'i18n/locales',
  
  /** Path to scan for translation usage */
  srcPath: '.',
  
  /** Allowed extensions for locale files */
  localesExtensions: ['json'],
  
  /** Allowed extensions for source files to scan */
  srcExtensions: ['js', 'ts', 'vue', 'jsx', 'tsx'],
  
  /** Paths to ignore during scanning */
  ignorePaths: [
    'node_modules',
    '.nuxt',
    '.output',
    'dist',
    '.git',
    'coverage',
    'public',
  ],
  
  /**
   * Regex to match translation keys in your code
   * Matches: 'key' and "key"
   */
  translationKeyMatcher: /['"]([\w.]+)['"]/g,

  /**
   * Parser to extract the actual key from the match
   */
  missedTranslationParser: (match) => {
    const extracted = match.match(/['"]([\w.]+)['"]/);
    return extracted ? extracted[1] : match;
  },
  
  /** 
   * Enable if you're using flat JSON structure
   */
  flatTranslations: false,
  
  /** 
   * Separator used in translation keys
   */
  translationSeparator: '.',
  
  /** 
   * Marker string to add to unused translations
   */
  marker: '[UNUSED]',
  
  /** 
   * JSON formatting for locale files
   */
  localeJsonStringifyIndent: 2,
  
  /** 
   * Ignore code comments when scanning for translations
   */
  ignoreComments: true,
  
  /** 
   * Show git diff/status after operations
   */
  gitCheck: true,
};