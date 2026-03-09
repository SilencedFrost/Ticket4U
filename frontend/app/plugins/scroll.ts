import { defineNuxtPlugin, useRouter } from '#app'

export default defineNuxtPlugin(() => {
  if (!import.meta.client) return

  const router = useRouter()

  router.afterEach(async () => {
    // Đợi Vue cập nhật DOM của trang mới xong
    await nextTick() 
    
    const c = document.querySelector('.overflow-auto')
    if (c instanceof HTMLElement) {
      c.scrollTo({ top: 0, behavior: 'smooth' })
    }
  })
})