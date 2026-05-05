export {};

declare global {
  interface User {
    id: string;
    roleId: number;
    username: string;
    email: string;
  }

  // TODO: refactor auth store để dùng User | UserSummary phù hợp hơn
  interface UserSummary {
    id: string;
    username: string;
    firstName: string | null;
    lastName: string | null;
    email: string;
    birthday: string | null;
    phoneNumber: string | null;
    createdAt: string;
  }
}
