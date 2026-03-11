import type { Directive } from 'vue';

type PlaceholderSize = [number, number];

export function useImagePlaceholder() {
  function getPlaceholder(width: number = 500, height: number = 500): string {
    return `https://placehold.co/${width}x${height}/webp?text=${width}x${height}`;
  }

  const vFallback: Directive<HTMLImageElement, PlaceholderSize | undefined> = {
    mounted(el, binding) {
      const [width, height] = binding.value ?? [500, 500];

      function setPlaceholder() {
        const placeholder = getPlaceholder(width, height);
        if (el.src !== placeholder) {
          el.src = placeholder;
        }
      }

      if (!el.src || el.src === window.location.href) {
        setPlaceholder();
      }

      el.addEventListener('error', setPlaceholder);
    },
    unmounted(el) {
      el.removeEventListener('error', () => {});
    },
  };

  return {
    getPlaceholder,
    vFallback,
  };
}
