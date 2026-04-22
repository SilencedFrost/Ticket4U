<script setup lang="ts">
import PreferencesAppearanceSection from './PreferencesAppearanceSection.vue';
import PreferencesLanguageSection from './PreferencesLanguageSection.vue';

type ThemePreference = 'light' | 'dark';
type LocaleCode = Parameters<typeof setLocale>[0];

interface PreferencesFormState {
	theme: ThemePreference;
	language: LocaleCode;
}

interface LanguageOption {
	value: LocaleCode;
	label: string;
}

const { currentTheme } = useTheme();
const { locale, locales, setLocale, t } = useI18n();

const formData = reactive<PreferencesFormState>({
	theme: 'light',
	language: locale.value,
});

function resolveLocaleCode(localeItem: string | { code: string; name?: string }): LocaleCode {
	if (typeof localeItem === 'string') {
		return localeItem as LocaleCode;
	}
	return localeItem.code as LocaleCode;
}

function resolveLocaleName(localeItem: string | { code: string; name?: string }): string | undefined {
	if (typeof localeItem === 'string') {
		return undefined;
	}
	return localeItem.name;
}

function resolveLanguageLabel(code: LocaleCode, fallback?: string): string {
	const translationKey = `settings.preferences.language.options.${code}`;
	const translated = t(translationKey);
	if (translated !== translationKey) {
		return translated;
	}
	return fallback ?? code.toUpperCase();
}

const languageOptions = computed<LanguageOption[]>(() => {
	return locales.value.map((localeItem) => {
		const code = resolveLocaleCode(localeItem);
		const name = resolveLocaleName(localeItem);
		return {
			value: code,
			label: resolveLanguageLabel(code, name),
		};
	});
});

function initializePreferences() {
	formData.theme = currentTheme.value === 'dark' ? 'dark' : 'light';
	formData.language = locale.value;
}

// Auto-save khi theme thay đổi
watch(() => formData.theme, (newTheme) => {
	  /* TODO: Khi useTheme hỗ trợ chọn theme cụ thể (vd: setTheme(theme)),
	   thay thế việc gán trực tiếp bằng method đó.*/
	currentTheme.value = newTheme;
});

// Auto-save khi language thay đổi
watch(() => formData.language, async (newLanguage) => {
	if (newLanguage !== locale.value) {
		await setLocale(newLanguage);
	}
});

// Sync lại nếu currentTheme thay đổi từ bên ngoài
watch(currentTheme, (theme) => {
	formData.theme = theme === 'dark' ? 'dark' : 'light';
});

onMounted(() => {
	initializePreferences();
});
</script>

<template>
	<div>
		<div class="page-header card shadow-sm p-3 mb-2">
			<h1 class="h3 fw-bold text-reactive-primary mb-1">
				{{ $t('settings.preferences.title') }}
			</h1>
			<p class="text-reactive-secondary mb-0">
				{{ $t('settings.preferences.subtitle') }}
			</p>
		</div>

		<div class="card shadow-sm p-3">
			<div class="d-flex flex-column gap-4">
				<preferences-appearance-section v-model="formData.theme" />
				<preferences-language-section v-model="formData.language" :options="languageOptions" />
			</div>
		</div>
	</div>
</template>
