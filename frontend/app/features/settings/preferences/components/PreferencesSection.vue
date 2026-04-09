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

const saving = ref(false);
const initialSnapshot = ref<PreferencesFormState | null>(null);

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

function createSnapshotFromCurrentPreferences(): PreferencesFormState {
	return {
		theme: currentTheme.value === 'dark' ? 'dark' : 'light',
		language: locale.value,
	};
}

function applySnapshot(snapshot: PreferencesFormState) {
	formData.theme = snapshot.theme;
	formData.language = snapshot.language;
}

function initializePreferences() {
	const snapshot = createSnapshotFromCurrentPreferences();
	applySnapshot(snapshot);
	initialSnapshot.value = { ...snapshot };
}

watch(currentTheme, (theme) => {
	const normalizedTheme: ThemePreference = theme === 'dark' ? 'dark' : 'light';
	formData.theme = normalizedTheme;

	if (initialSnapshot.value) {
		initialSnapshot.value = {
			...initialSnapshot.value,
			theme: normalizedTheme,
		};
	}
});

const hasChanges = computed(() => {
	if (!initialSnapshot.value) {
		return false;
	}

	return (
		formData.theme !== initialSnapshot.value.theme
		|| formData.language !== initialSnapshot.value.language
	);
});

async function saveChanges() {
	if (!hasChanges.value || saving.value) {
		return;
	}

	saving.value = true;

	try {
		currentTheme.value = formData.theme;

		if (formData.language !== locale.value) {
			await setLocale(formData.language);
		}

		initialSnapshot.value = createSnapshotFromCurrentPreferences();
	} finally {
		saving.value = false;
	}
}

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

		<form class="card shadow-sm p-3" @submit.prevent="saveChanges">
			<div class="d-flex flex-column gap-4">
				<preferences-appearance-section v-model="formData.theme" />
				<preferences-language-section v-model="formData.language" :options="languageOptions" />
			</div>

			<div class="d-flex flex-wrap justify-content-end gap-2 mt-4 pt-3 border-secondary-subtle border-opacity-25">
				<button
					type="submit"
					class="btn btn-primary ms-auto d-inline-flex align-items-center"
					:disabled="!hasChanges || saving"
				>
					<i class="bi bi-floppy-fill text-white"></i>
				</button>
			</div>
		</form>
	</div>
</template>

<style scoped>
.preferences-action-label {
	font-size: 0.75rem;
	letter-spacing: 0.08em;
}
</style>
