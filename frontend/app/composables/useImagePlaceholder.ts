import type { Directive, DirectiveBinding } from 'vue';

export const useImagePlaceholder = () => {
  const getPlaceholder = (width: number = 500, height: number = 500): string => {
    return `https://placehold.co/${width}x${height}/webp?text=${width}x${height}`;
  };

  const vFallback: Directive<HTMLImageElement, [number, number] | undefined> = {
    mounted(el: HTMLImageElement, binding: DirectiveBinding<[number, number] | undefined>) {
      const width = binding.value?.[0] ?? 500;
      const height = binding.value?.[1] ?? 500;

      const setPlaceholder = () => {
        const placeholder = getPlaceholder(width, height);
        if (el.src !== placeholder) {
          el.src = placeholder;
        }
      };

      if (!el.src || el.src === window.location.href) {
        setPlaceholder();
      }

      el.addEventListener('error', setPlaceholder);
    },
  };

  return {
    getPlaceholder,
    vFallback,
  };
};
