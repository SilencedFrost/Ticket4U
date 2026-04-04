export interface UserSummary {
  id: string;
  username: string;
  firstName: string | null;
  lastName: string | null;
  email: string;
  birthday: string | null;
  phoneNumber: string | null;
  createdAt: string;
}

export interface ChangeInfo {
  firstName: string | null;
  lastName: string | null;
  birthday: string | null;
  phoneNumber: string | null;
}

export interface ChangePassword {
  currentPassword: string;
  newPassword: string;
}

export interface Session {
  displayId: string;
  userAgent: string | null;
  updatedAt: string;
}

export interface ProfileForm {
  firstName: string;
  lastName: string;
  email: string;
  birthday: string;
  phoneNumber: string;
}
