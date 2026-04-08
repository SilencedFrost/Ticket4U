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

export interface FieldErrors {
  firstName?: string;
  lastName?: string;
  birthday?: string;
  phoneNumber?: string;
}
