import { ref, computed } from 'vue'
import type { Event } from '../types'

export function useDateRange() {
  const startDate = ref<string>('')
  const endDate = ref<string>('')
  const selectedPreset = ref<string>('')

  // Helper function to format date to yyyy-MM-dd for input
  const toInputFormat = (date: Date): string => {
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
  }

  // Helper function to parse Vietnamese date format
  const parseEventDate = (dateStr: string): Date | null => {
    // Format: "26 tháng 11,2025" or "25 Tháng 11,2025"
    const match = dateStr.match(/(\d+)\s+[tT]háng\s+(\d+)[,\s]+(\d+)/)
    if (match) {
      const day = parseInt(match[1]!)
      const month = parseInt(match[2]!) - 1 // JavaScript months are 0-indexed
      const year = parseInt(match[3]!)
      return new Date(year, month, day)
    }
    return null
  }

  const selectPreset = (preset: string) => {
    selectedPreset.value = preset
    const today = new Date()

    switch (preset) {
      case 'all':
        startDate.value = ''
        endDate.value = ''
        break
      case 'today':
        startDate.value = toInputFormat(today)
        endDate.value = toInputFormat(today)
        break
      case 'tomorrow': {
        const tomorrow = new Date(today)
        tomorrow.setDate(today.getDate() + 1)
        startDate.value = toInputFormat(tomorrow)
        endDate.value = toInputFormat(tomorrow)
        break
      }
      case 'this-weekend': {
        const dayOfWeek = today.getDay()
        const daysUntilFriday = (5 - dayOfWeek + 7) % 7
        const friday = new Date(today)
        friday.setDate(today.getDate() + daysUntilFriday)
        const sunday = new Date(friday)
        sunday.setDate(friday.getDate() + 2)
        startDate.value = toInputFormat(friday)
        endDate.value = toInputFormat(sunday)
        break
      }
      case 'this-month': {
        const firstDay = new Date(today.getFullYear(), today.getMonth(), 1)
        const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0)
        startDate.value = toInputFormat(firstDay)
        endDate.value = toInputFormat(lastDay)
        break
      }
    }
  }

  const formatDateRange = () => {
    if (!startDate.value || !endDate.value) return 'Tất cả các ngày'

    const formatDate = (dateStr: string) => {
      const parts = dateStr.split('-')
      const year = parts[0] || ''
      const month = parts[1] || ''
      const day = parts[2] || ''
      return `${day}/${month}/${year}`
    }

    return `${formatDate(startDate.value)} - ${formatDate(endDate.value)}`
  }

  const filterEventsByDate = (events: Event[]) => {
    if (!startDate.value || !endDate.value) {
      return events
    }

    const start = new Date(startDate.value)
    const end = new Date(endDate.value)

    return events.filter((event) => {
      if (!event.date) return false
      const eventDate = parseEventDate(event.date)
      if (!eventDate) return false

      return eventDate >= start && eventDate <= end
    })
  }

  const reset = () => {
    startDate.value = ''
    endDate.value = ''
    selectedPreset.value = ''
  }

  return {
    startDate,
    endDate,
    selectedPreset,
    selectPreset,
    formatDateRange,
    filterEventsByDate,
    reset,
  }
}
