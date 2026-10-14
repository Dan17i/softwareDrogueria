import { useState, type FormEvent } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../auth'
import { ApiError } from '../api'

export function AuthShell({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <div className="login">
      <section className="login-brand">
        <span className="login-kicker">Droguería · Inventario</span>
        <div>
          <p className="wordmark" aria-hidden="true">Bella&shy;vista</p>
          <h1>Lo que falta en el estante, primero.</h1>
        </div>
      </section>
      <div className="login-side">
        <div className="login-form">
          <h2>{title}</h2>
          {children}
        </div>
      </div>
    </div>
  )
}

export default function Login() {
  const { login } = useAuth()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [show, setShow] = useState(false)
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)

  async function submit(e: FormEvent) {
    e.preventDefault()
    setBusy(true)
    setError('')
    try {
      await login(username.trim(), password)
    } catch (err) {
      if (err instanceof ApiError && err.status === 429) setError('Demasiados intentos. Espera un minuto y vuelve a intentar.')
      else if (err instanceof ApiError && err.status === 0) setError(err.message)
      else setError('Usuario o contraseña incorrectos. Revisa mayúsculas y vuelve a intentar.')
    } finally {
      setBusy(false)
    }
  }

  return (
    <AuthShell title="Ingresar">
      <form className="stack" onSubmit={submit}>
        <div className="field">
          <label htmlFor="username">Usuario</label>
          <input id="username" className="input" autoComplete="username" autoCapitalize="none" value={username} onChange={(e) => setUsername(e.target.value)} required minLength={3} />
        </div>
        <div className="field">
          <label htmlFor="password">Contraseña</label>
          <div className="input-group">
            <input id="password" className="input" type={show ? 'text' : 'password'} autoComplete="current-password" value={password} onChange={(e) => setPassword(e.target.value)} required minLength={8} />
            <button type="button" className="btn btn-ghost" onClick={() => setShow((s) => !s)} aria-pressed={show}>
              {show ? 'Ocultar' : 'Mostrar'}
            </button>
          </div>
        </div>
        {error && <p className="error-text" role="alert">{error}</p>}
        <button className="btn btn-primary" disabled={busy}>
          {busy ? 'Ingresando…' : 'Ingresar'}
        </button>
        <Link to="/recuperar" className="text-link">¿Olvidaste tu contraseña?</Link>
      </form>
    </AuthShell>
  )
}
