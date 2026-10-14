import type { ReactNode } from 'react'

export default function AuthShell({ title, children }: { title: string; children: ReactNode }) {
  return (
    <div className="login">
      <header className="login-brand">
        <span className="login-kicker">Droguería · Inventario</span>
        <div>
          <p className="wordmark" aria-hidden="true">Bella&shy;vista</p>
          <h1>Lo que falta en el estante, primero.</h1>
        </div>
      </header>
      <main className="login-side">
        <div className="login-form">
          <h2>{title}</h2>
          {children}
        </div>
      </main>
    </div>
  )
}
