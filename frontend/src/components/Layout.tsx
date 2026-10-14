import type { ReactNode } from 'react'
import { NavLink } from 'react-router-dom'
import { useAuth } from '../auth'

export default function Layout({ children }: { children: ReactNode }) {
  const { session, logout } = useAuth()
  return (
    <div className="shell">
      <nav className="nav" aria-label="Principal">
        <div className="brand">
          Bellavista
          <small>Droguería · Inventario</small>
        </div>
        <div className="nav-links">
          <NavLink to="/" end className="nav-link">
            Resumen
          </NavLink>
          <NavLink to="/productos" className="nav-link">
            Productos
          </NavLink>
        </div>
        <div className="nav-user">
          <span>
            <strong>{session?.username}</strong> · {session?.role}
          </span>
          <button className="btn btn-ghost" onClick={logout}>
            Salir
          </button>
        </div>
      </nav>
      <main className="main">{children}</main>
    </div>
  )
}
