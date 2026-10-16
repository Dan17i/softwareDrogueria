import { useEffect, useState, type ReactNode } from 'react'
import { NavLink, useLocation } from 'react-router-dom'
import { canManageCustomers, canReadOrders, canReadReceipts } from '../api'
import { useAuth } from '../auth'
import LegalLinks from './LegalLinks'
import useUnreadCount from './useUnreadCount'

export default function Layout({ children }: { children: ReactNode }) {
  const { session, logout } = useAuth()
  const [confirming, setConfirming] = useState(false)
  const { pathname } = useLocation()
  const unread = useUnreadCount(session?.role)

  // En móvil la barra se desplaza: mantiene a la vista la sección activa
  useEffect(() => {
    document.querySelector('.nav-link.active')?.scrollIntoView({ inline: 'center', block: 'nearest' })
  }, [pathname])

  // La confirmación expira sola: un toque accidental no deja la sesión "armada".
  useEffect(() => {
    if (!confirming) return
    const t = setTimeout(() => setConfirming(false), 4000)
    return () => clearTimeout(t)
  }, [confirming])

  return (
    <div className="shell">
      <a className="skip-link" href="#contenido">Saltar al contenido</a>
      <nav className="nav" aria-label="Principal">
        <div className="brand">
          Bellavista
          <small>Droguería · Inventario</small>
        </div>
        <div className="nav-links">
          <NavLink to="/" end className="nav-link">
            Reposición
          </NavLink>
          <NavLink to="/productos" className="nav-link">
            Productos
          </NavLink>
          {session && canReadOrders(session.role) && (
            <NavLink to="/ordenes" className="nav-link">
              Órdenes
            </NavLink>
          )}
          {session && canManageCustomers(session.role) && (
            <NavLink to="/clientes" className="nav-link">
              Clientes
            </NavLink>
          )}
          {session && canReadReceipts(session.role) && (
            <NavLink to="/recepciones" className="nav-link">
              Recepciones
            </NavLink>
          )}
          {session && canReadReceipts(session.role) && (
            <NavLink to="/proveedores" className="nav-link">
              Proveedores
            </NavLink>
          )}
          <NavLink to="/notificaciones" className="nav-link">
            Notificaciones
            {unread > 0 && (
              <span className="badge">
                {unread > 99 ? '99+' : unread}
                <span className="sr-only"> sin leer</span>
              </span>
            )}
          </NavLink>
        </div>
        <div className="nav-user">
          <span>
            <strong>{session?.username}</strong> · {session?.role}
          </span>
          <button
            className={confirming ? 'btn btn-danger' : 'btn btn-ghost'}
            onClick={() => (confirming ? logout() : setConfirming(true))}
            aria-live="polite"
          >
            {confirming ? 'Confirmar salida' : 'Salir'}
          </button>
        </div>
      </nav>
      <main className="main" id="contenido" tabIndex={-1}>
        {children}
        <LegalLinks />
      </main>
    </div>
  )
}
