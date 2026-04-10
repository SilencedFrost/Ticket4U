export interface OrganizerStaff {
  id:          string
  email:       string
  username:    string
  firstName?:  string
  lastName?:   string
  avatarUrl?:  string
  /** 1=Event Manager, 10=Gatekeeper, 11=Support Agent, 12=Finance Manager */
  roleId:      1 | 10 | 11 | 12
  invitedAt:   string
  isActive:    boolean
}

export interface AssignableRole {
  id:    number
  key:   string
  icon:  string
  color: string
}
