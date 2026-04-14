<script setup lang="ts">
import { ref, computed, watch, onBeforeUnmount, nextTick } from 'vue'
import { useEditor, EditorContent } from '@tiptap/vue-3'
import StarterKit from '@tiptap/starter-kit'
import Underline from '@tiptap/extension-underline'
import Link from '@tiptap/extension-link'
import { TextStyle } from '@tiptap/extension-text-style'
import Color from '@tiptap/extension-color'
import Highlight from '@tiptap/extension-highlight'
import Image from '@tiptap/extension-image'

const props = withDefaults(defineProps<{
  modelValue: string
  placeholder?: string
  minHeight?: number
  disabled?: boolean
}>(), {
  placeholder: 'Start writing...',
  minHeight:   200,
  disabled:    false,
})

const emit = defineEmits<{ 'update:modelValue': [value: string] }>()

const isFocused      = ref(false)
const showLinkModal  = ref(false)
const linkUrl        = ref('')
const linkInputRef   = ref<HTMLInputElement | null>(null)
const showImageModal = ref(false)
const imageUrl       = ref('')
const imageAlt       = ref('')
const imageInputRef  = ref<HTMLInputElement | null>(null)

const editor = useEditor({
  content:  props.modelValue,
  editable: !props.disabled,
  extensions: [
    StarterKit,
    Underline,
    Link.configure({ openOnClick: false, autolink: true }),
    TextStyle,
    Color,
    Highlight.configure({ multicolor: true }),
    Image.configure({ inline: false, allowBase64: false }),
  ],
  editorProps: {
    attributes: { class: 'rich-editor-inner', spellcheck: 'true' },
  },
  onUpdate({ editor }) { emit('update:modelValue', editor.getHTML()) },
  onFocus()  { isFocused.value = true  },
  onBlur()   { isFocused.value = false },
})

watch(
  function () { return props.modelValue },
  function (val) {
    if (editor.value && editor.value.getHTML() !== val)
      editor.value.commands.setContent(val || '', false)
  }
)

watch(
  function () { return props.disabled },
  function (val) { editor.value?.setEditable(!val) }
)

const currentTextColor = computed(function () {
  return (editor.value?.getAttributes('textStyle')?.color as string) ?? 'var(--text-reactive-primary)'
})

const currentHighlight = computed(function () {
  return (editor.value?.getAttributes('highlight')?.color as string) ?? '#ffff00'
})

// ── Link ──────────────────────────────────────────────────────

function openLinkModal() {
  linkUrl.value       = editor.value?.getAttributes('link')?.href ?? ''
  showLinkModal.value = true
  nextTick(function () { linkInputRef.value?.focus() })
}

function confirmLink() {
  if (!linkUrl.value.trim()) {
    editor.value?.chain().focus().unsetLink().run()
  } else {
    editor.value?.chain().focus().setLink({ href: linkUrl.value.trim() }).run()
  }
  showLinkModal.value = false
  linkUrl.value       = ''
}

// ── Image ─────────────────────────────────────────────────────

function openImageModal() {
  imageUrl.value       = ''
  imageAlt.value       = ''
  showImageModal.value = true
  nextTick(function () { imageInputRef.value?.focus() })
}

function confirmImage() {
  const url = imageUrl.value.trim()
  if (url) {
    editor.value?.chain().focus().setImage({
      src: url,
      alt: imageAlt.value.trim() || undefined,
    }).run()
  }
  showImageModal.value = false
  imageUrl.value       = ''
  imageAlt.value       = ''
}

// ── Colors ────────────────────────────────────────────────────

function onTextColorInput(e: Event) {
  editor.value?.chain().focus().setColor((e.target as HTMLInputElement).value).run()
}

function onHighlightInput(e: Event) {
  editor.value?.chain().focus().setHighlight({ color: (e.target as HTMLInputElement).value }).run()
}

function clearFormatting() {
  editor.value?.chain().focus().unsetColor().unsetHighlight().run()
}

onBeforeUnmount(function () { editor.value?.destroy() })
</script>

<template>
  <div class="rich-editor" :class="{ 'is-focused': isFocused, 'is-disabled': disabled }">

    <!-- Toolbar -->
    <div class="rich-editor-toolbar d-flex flex-wrap align-items-center gap-1 p-2">

      <!-- History -->
      <div class="btn-group btn-group-sm me-1">
        <button type="button" class="toolbar-btn" title="Undo" :disabled="!editor?.can().undo()" @click="editor?.chain().focus().undo().run()">
          <i class="bi bi-arrow-counterclockwise"/>
        </button>
        <button type="button" class="toolbar-btn" title="Redo" :disabled="!editor?.can().redo()" @click="editor?.chain().focus().redo().run()">
          <i class="bi bi-arrow-clockwise"/>
        </button>
      </div>

      <div class="toolbar-divider"/>

      <!-- Headings -->
      <div class="btn-group btn-group-sm me-1">
        <button
          v-for="level in [1, 2, 3]" :key="level"
          type="button" class="toolbar-btn"
          :class="{ active: editor?.isActive('heading', { level }) }"
          :title="`Heading ${level}`"
          @click="editor?.chain().focus().toggleHeading({ level }).run()"
        >H{{ level }}</button>
        <button
          type="button" class="toolbar-btn"
          :class="{ active: editor?.isActive('paragraph') && !editor?.isActive('heading') }"
          title="Paragraph"
          @click="editor?.chain().focus().setParagraph().run()"
        ><i class="bi bi-text-paragraph"/></button>
      </div>

      <div class="toolbar-divider"/>

      <!-- Inline marks -->
      <div class="btn-group btn-group-sm me-1">
        <button type="button" class="toolbar-btn" :class="{ active: editor?.isActive('bold') }"      title="Bold"          @click="editor?.chain().focus().toggleBold().run()">      <i class="bi bi-type-bold"/>          </button>
        <button type="button" class="toolbar-btn" :class="{ active: editor?.isActive('italic') }"    title="Italic"        @click="editor?.chain().focus().toggleItalic().run()">    <i class="bi bi-type-italic"/>        </button>
        <button type="button" class="toolbar-btn" :class="{ active: editor?.isActive('underline') }" title="Underline"     @click="editor?.chain().focus().toggleUnderline().run()"> <i class="bi bi-type-underline"/>     </button>
        <button type="button" class="toolbar-btn" :class="{ active: editor?.isActive('strike') }"    title="Strikethrough" @click="editor?.chain().focus().toggleStrike().run()">    <i class="bi bi-type-strikethrough"/> </button>
      </div>

      <div class="toolbar-divider"/>

      <!-- Lists -->
      <div class="btn-group btn-group-sm me-1">
        <button type="button" class="toolbar-btn" :class="{ active: editor?.isActive('bulletList') }"  title="Bullet list"   @click="editor?.chain().focus().toggleBulletList().run()">  <i class="bi bi-list-ul"/></button>
        <button type="button" class="toolbar-btn" :class="{ active: editor?.isActive('orderedList') }" title="Numbered list" @click="editor?.chain().focus().toggleOrderedList().run()"> <i class="bi bi-list-ol"/></button>
      </div>

      <div class="toolbar-divider"/>

      <!-- Link -->
      <div class="btn-group btn-group-sm me-1">
        <button type="button" class="toolbar-btn" :class="{ active: editor?.isActive('link') }" title="Add link" @click="openLinkModal">
          <i class="bi bi-link-45deg"/>
        </button>
        <button v-if="editor?.isActive('link')" type="button" class="toolbar-btn" title="Remove link" @click="editor?.chain().focus().unsetLink().run()">
          <i class="bi bi-link-break"/>
        </button>
      </div>

      <div class="toolbar-divider"/>

      <!-- Colors -->
      <div class="d-flex align-items-center gap-1 me-1">
        <div class="position-relative" title="Text color">
          <input type="color" class="color-input" :value="currentTextColor" @input="onTextColorInput"/>
          <button type="button" class="toolbar-btn color-btn" tabindex="-1">
            <i class="bi bi-fonts"/>
            <span class="color-indicator" :style="{ background: currentTextColor }"/>
          </button>
        </div>
        <div class="position-relative" title="Highlight color">
          <input type="color" class="color-input" :value="currentHighlight" @input="onHighlightInput"/>
          <button type="button" class="toolbar-btn color-btn" tabindex="-1">
            <i class="bi bi-highlighter"/>
            <span class="color-indicator" :style="{ background: currentHighlight }"/>
          </button>
        </div>
        <button
          v-if="editor?.isActive('textStyle') || editor?.isActive('highlight')"
          type="button" class="toolbar-btn" title="Clear formatting"
          @click="clearFormatting"
        ><i class="bi bi-eraser"/></button>
      </div>

      <div class="toolbar-divider"/>

      <!-- Image -->
      <button type="button" class="toolbar-btn me-1" title="Insert image" @click="openImageModal">
        <i class="bi bi-image"/>
      </button>

      <!-- Blockquote / HR -->
      <div class="btn-group btn-group-sm">
        <button type="button" class="toolbar-btn" :class="{ active: editor?.isActive('blockquote') }" title="Blockquote" @click="editor?.chain().focus().toggleBlockquote().run()">
          <i class="bi bi-chat-quote"/>
        </button>
        <button type="button" class="toolbar-btn" title="Divider" @click="editor?.chain().focus().setHorizontalRule().run()">
          <i class="bi bi-dash-lg"/>
        </button>
      </div>

    </div>

    <!-- Editor body -->
    <div class="rich-editor-body" :style="{ minHeight: `${minHeight}px` }">
      <editor-content :editor="editor" class="rich-editor-content"/>
    </div>

    <!-- Link modal -->
    <div v-if="showLinkModal" class="editor-modal-backdrop" @click.self="showLinkModal = false">
      <div class="editor-modal card shadow p-4 rounded-3">
        <div class="fw-semibold text-reactive-primary mb-3">
          <i class="bi bi-link-45deg me-2 text-primary"/>Insert Link
        </div>
        <input
          ref="linkInputRef"
          v-model="linkUrl"
          type="url"
          class="form-control mb-3"
          placeholder="https://..."
          @keyup.enter="confirmLink"
        />
        <div class="d-flex gap-2 justify-content-end">
          <button class="btn btn-sm btn-outline-secondary" @click="showLinkModal = false">Cancel</button>
          <button class="btn btn-sm btn-primary" @click="confirmLink">Insert</button>
        </div>
      </div>
    </div>

    <!-- Image modal -->
    <div v-if="showImageModal" class="editor-modal-backdrop" @click.self="showImageModal = false">
      <div class="editor-modal card shadow p-4 rounded-3">
        <div class="fw-semibold text-reactive-primary mb-3">
          <i class="bi bi-image me-2 text-primary"/>Insert Image
        </div>

        <label class="form-label small text-reactive-secondary">Image URL <span class="text-danger">*</span></label>
        <input
          ref="imageInputRef"
          v-model="imageUrl"
          type="url"
          class="form-control mb-3"
          placeholder="https://example.com/image.jpg"
          @keyup.enter="confirmImage"
        />

        <!-- Preview -->
        <div v-if="imageUrl.trim()" class="image-preview-box mb-3 rounded overflow-hidden bg-reactive-primary d-flex align-items-center justify-content-center">
          <img :src="imageUrl" alt="preview" class="image-preview" @error="imageUrl = imageUrl"/>
        </div>

        <label class="form-label small text-reactive-secondary">Alt text <span class="text-reactive-secondary opacity-50">(optional)</span></label>
        <input
          v-model="imageAlt"
          type="text"
          class="form-control mb-3"
          placeholder="Describe the image..."
        />

        <div class="d-flex gap-2 justify-content-end">
          <button class="btn btn-sm btn-outline-secondary" @click="showImageModal = false">Cancel</button>
          <button class="btn btn-sm btn-primary" :disabled="!imageUrl.trim()" @click="confirmImage">
            <i class="bi bi-image me-1"/>Insert
          </button>
        </div>
      </div>
    </div>

  </div>
</template>

<style scoped>
.rich-editor {
  border: 1px solid rgba(var(--bs-secondary-rgb), 0.2);
  border-radius: 8px; overflow: visible;
  background: var(--bg-reactive-primary, var(--bs-body-bg));
  transition: border-color 0.2s;
  position: relative;
}
.rich-editor.is-focused {
  border-color: var(--bs-primary);
  box-shadow: 0 0 0 3px rgba(var(--bs-primary-rgb), 0.12);
}
.rich-editor-toolbar {
  background: rgba(var(--bs-secondary-rgb), 0.08);
  border-bottom: 1px solid rgba(var(--bs-secondary-rgb), 0.15);
  border-radius: 8px 8px 0 0;
}
.toolbar-btn {
  display: inline-flex; align-items: center; justify-content: center;
  min-width: 28px; height: 28px; padding: 0 5px;
  background: transparent; border: 1px solid transparent; border-radius: 5px;
  color: var(--bs-secondary); font-size: 0.8rem; font-weight: 600;
  cursor: pointer; transition: background 0.15s, color 0.15s, border-color 0.15s;
}
.toolbar-btn:hover:not(:disabled) {
  background: rgba(var(--bs-primary-rgb), 0.12);
  color: var(--bs-primary);
  border-color: rgba(var(--bs-primary-rgb), 0.2);
}
.toolbar-btn.active {
  background: rgba(var(--bs-primary-rgb), 0.18);
  color: var(--bs-primary);
  border-color: rgba(var(--bs-primary-rgb), 0.3);
}
.toolbar-btn:disabled { opacity: 0.35; cursor: not-allowed; }
.toolbar-divider { width: 1px; height: 20px; background: rgba(var(--bs-secondary-rgb), 0.25); margin: 0 2px; flex-shrink: 0; }
.color-btn { position: relative; flex-direction: column; gap: 1px; height: 32px; }
.color-indicator { display: block; width: 14px; height: 3px; border-radius: 2px; }
.color-input { position: absolute; inset: 0; opacity: 0; width: 100%; height: 100%; cursor: pointer; border: none; padding: 0; }
.rich-editor-body { padding: 12px 16px; }
.rich-editor-content { outline: none; }
.editor-modal-backdrop {
  position: fixed; inset: 0; background: rgba(0,0,0,0.5);
  z-index: 2000; display: flex; align-items: center; justify-content: center;
}
.editor-modal { width: 100%; max-width: 440px; border-radius: 10px; }
.image-preview-box { height: 160px; }
.image-preview { max-width: 100%; max-height: 160px; object-fit: contain; }
</style>

<style>
.rich-editor-inner {
  outline: none;
  color: var(--text-reactive-primary, inherit);
  font-size: 0.95rem; line-height: 1.7;
}
.rich-editor-inner p { margin-bottom: 0.6em; }
.rich-editor-inner p.is-editor-empty:first-child::before {
  content: attr(data-placeholder); color: var(--bs-secondary);
  opacity: 0.5; pointer-events: none; float: left; height: 0;
}
.rich-editor-inner h1 { font-size: 1.6rem; font-weight: 700; margin-bottom: 0.5em; }
.rich-editor-inner h2 { font-size: 1.3rem; font-weight: 700; margin-bottom: 0.4em; }
.rich-editor-inner h3 { font-size: 1.1rem; font-weight: 600; margin-bottom: 0.4em; }
.rich-editor-inner ul,
.rich-editor-inner ol { padding-left: 1.4em; margin-bottom: 0.6em; }
.rich-editor-inner li { margin-bottom: 0.2em; }
.rich-editor-inner a { color: var(--bs-primary); text-decoration: underline; }
.rich-editor-inner blockquote {
  border-left: 3px solid var(--bs-primary); padding-left: 1em;
  color: var(--bs-secondary); margin: 0.8em 0; font-style: italic;
}
.rich-editor-inner hr { border: none; border-top: 1px solid rgba(var(--bs-secondary-rgb), 0.3); margin: 1em 0; }
.rich-editor-inner img { max-width: 100%; border-radius: 6px; margin: 0.5em 0; display: block; }
.rich-editor-inner mark { border-radius: 3px; padding: 0 2px; }
</style>
