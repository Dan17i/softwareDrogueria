import { useEffect, useState, type ReactNode } from 'react'
import { NavLink } from 'react-router-dom'
import { canManageCustomers, canReadOrders } from '../api'
import { useAuth } from '../auth'

export default function Layout({ children }: { children: ReactNode }) {
  const { session, logout } = useAuth()
  const [confirming, setConfirming] = useState(false)

  // La confirmación expira sola: un toque accidental no deja la sesión "armada".
  useEffect(() => {
    if (!confirming) return
    const t = setTimeout(() => setConfirming(false), 4000)
    return () => clearTimeout(t)
  }, [confirming])

  return (
    <div className="shell">
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
      <main className="main">{children}</main>
    </div>
  )
}
