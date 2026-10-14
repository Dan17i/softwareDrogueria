import { useState, type FormEvent } from 'react'
import { Link, useNavigate, useSearchParams } from 'react-router-dom'
import { toast } from 'sonner'
import { api } from '../api'
import AuthShell from '../components/AuthShell'

export function Recover() {
  const [email, setEmail] = useState('')
  const [busy, setBusy] = useState(false)
  const [sent, setSent] = useState(false)
  const [error, setError] = useState('')

  async function submit(e: FormEvent) {
    e.preventDefault()
    setBusy(true)
    setError('')
    try {
      await api.forgotPassword(email.trim())
      setSent(true)
    } catch (err) {
      setError((err as Error).message)
    } finally {
      setBusy(false)
    }
  }

  return (
    <AuthShell title="Recuperar contraseña">
      {sent ? (
        <div className="stack">
          <p>Si ese correo está registrado, te enviamos las instrucciones. Revisa también la carpeta de spam.</p>
          <Link to="/login" className="btn">Volver a ingresar</Link>
        </div>
      ) : (
        <form className="stack" onSubmit={submit}>
          <div className="field">
            <label htmlFor="email">Correo de tu cuenta</label>
            <input id="email" className="input" type="email" autoComplete="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
          </div>
          {error && <p className="error-text" role="alert">{error}</p>}
          <button className="btn btn-primary" disabled={busy}>{busy ? 'Enviando…' : 'Enviar instrucciones'}</button>
          <Link to="/login" className="text-link">Volver a ingresar</Link>
        </form>
      )}
    </AuthShell>
  )
}

export function ResetPassword() {
  const [params] = useSearchParams()
  const token = params.get('token') ?? ''
  const navigate = useNavigate()
  const [password, setPassword] = useState('')
  const [busy, setBusy] = useState(false)
  const [error, setError] = useState('')

  async function submit(e: FormEvent) {
    e.preventDefault()
    setBusy(true)
    setError('')
    try {
      await api.resetPassword(token, password)
      toast.success('Contraseña cambiada. Ya puedes ingresar.')
      navigate('/login', { replace: true })
    } catch (err) {
      setError((err as Error).message || 'El enlace venció. Pide uno nuevo.')
    } finally {
      setBusy(false)
    }
  }

  if (!token) {
    return (
      <AuthShell title="Enlace inválido">
        <div className="stack">
          <p>Este enlace está incompleto. Pide uno nuevo para cambiar tu contraseña.</p>
          <Link to="/recuperar" className="btn btn-primary">Pedir enlace nuevo</Link>
        </div>
      </AuthShell>
    )
  }

  return (
    <AuthShell title="Nueva contraseña">
      <form className="stack" onSubmit={submit}>
        <div className="field">
          <label htmlFor="new-password">Nueva contraseña</label>
          <input id="new-password" className="input" type="password" autoComplete="new-password" value={password} onChange={(e) => setPassword(e.target.value)} required minLength={8} />
          <span className="hint">Mínimo 8 caracteres.</span>
        </div>
        {error && <p className="error-text" role="alert">{error}</p>}
        <button className="btn btn-primary" disabled={busy}>{busy ? 'Guardando…' : 'Guardar contraseña'}</button>
      </form>
    </AuthShell>
  )
}
