import { createContext, useCallback, useContext, useEffect, useMemo, useState, type ReactNode } from 'react'
import { api, loadSession, saveSession, setUnauthorizedHandler, type Session } from './api'

interface AuthValue {
  session: Session | null
  login: (username: string, password: string) => Promise<void>
  logout: () => void
}

const AuthContext = createContext<AuthValue | null>(null)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [session, setSession] = useState<Session | null>(loadSession)

  const clear = useCallback(() => {
    saveSession(null)
    setSession(null)
  }, [])

  useEffect(() => setUnauthorizedHandler(clear), [clear])

  const value = useMemo<AuthValue>(
    () => ({
      session,
      login: async (username, password) => {
        const r = await api.login(username, password)
        const s = { token: r.token, username: r.username, role: r.role }
        saveSession(s)
        setSession(s)
      },
      logout: () => {
        void api.logout()
        clear()
      },
    }),
    [session, clear],
  )

  return <AuthContext value={value}>{children}</AuthContext>
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth fuera de AuthProvider')
  return ctx
}
