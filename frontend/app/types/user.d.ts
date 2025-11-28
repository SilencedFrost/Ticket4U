export {}

declare global {
  interface User {
    id: string
    roleId: number
    username: string
    email: string
  }
}