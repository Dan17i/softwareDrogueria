import { useEffect, useState } from 'react'
import { loadNotifications, NOTIFICATIONS_CHANGED, type Role } from '../api'

// Cuenta de notificaciones sin leer para la insignia del menú: al montar, cada 60 s con la pestaña visible
// y cuando otra parte de la app avisa que cambió algo.
export default function useUnreadCount(role: Role | undefined): number {
  const [count, setCount] = useState(0)

  useEffect(() => {
    if (!role) return
    let alive = true
    const refresh = () => {
      if (document.hidden) return
      loadNotifications(role)
        .then((list) => alive && setCount(list.filter((n) => !n.isRead).length))
        .catch(() => undefined)
    }
    refresh()
    const timer = setInterval(refresh, 60_000)
    window.addEventListener(NOTIFICATIONS_CHANGED, refresh)
    document.addEventListener('visibilitychange', refresh)
    return () => {
      alive = false
      clearInterval(timer)
      window.removeEventListener(NOTIFICATIONS_CHANGED, refresh)
      document.removeEventListener('visibilitychange', refresh)
    }
  }, [role])

  return count
}
