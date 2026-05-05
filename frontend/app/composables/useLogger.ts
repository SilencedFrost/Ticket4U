export function useLogger() {
  const isDev = import.meta.dev;

  //TODO: add functionality based on error types
  function error(...args: any[]) {
    if (isDev) console.error(...args);
  }

  function warn(...args: any[]) {
    if (isDev) console.warn(...args);
  }

  function log(...args: any[]) {
    if (isDev) console.log(...args);
  }

  return {
    error,
    warn,
    log,
  };
}
