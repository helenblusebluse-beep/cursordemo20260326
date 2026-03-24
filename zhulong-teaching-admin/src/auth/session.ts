const TOKEN_KEY = 'zhulong_token'
const USER_NAME_KEY = 'zhulong_user_name'
const USER_ID_KEY = 'zhulong_user_id'
const USER_USERNAME_KEY = 'zhulong_username'

export function getToken(): string | null {
  return localStorage.getItem(TOKEN_KEY)
}

export function setSession(token: string, displayName: string, userId: number, username: string): void {
  localStorage.setItem(TOKEN_KEY, token)
  localStorage.setItem(USER_NAME_KEY, displayName)
  localStorage.setItem(USER_ID_KEY, String(userId))
  localStorage.setItem(USER_USERNAME_KEY, username)
}

export function getDisplayName(): string | null {
  return localStorage.getItem(USER_NAME_KEY)
}

export function getUsername(): string | null {
  return localStorage.getItem(USER_USERNAME_KEY)
}

export function clearAuth(): void {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_NAME_KEY)
  localStorage.removeItem(USER_ID_KEY)
  localStorage.removeItem(USER_USERNAME_KEY)
}

export function isLoggedIn(): boolean {
  return Boolean(getToken())
}
