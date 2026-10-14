import { useCallback, useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'
import { toast } from 'sonner'
import { api, loadNotifications, notifyChanged, parseLocal, type AppNotification, type NotificationType } from '../api'
import { useAuth } from '../auth'

const TYPE_LABEL: Record<NotificationType, string> = {
  INVENTORY_ALERT: 'Inventario',
  ORDER_ALERT: 'Órdenes',
  SYSTEM_ALERT: 'Sistema',
  USER_ALERT: 'Usuario',
}

const dateFmt = new Intl.DateTimeFormat('es-CO', { dateStyle: 'medium', timeStyle: 'short' })

export default function Notifications() {
  const { session } = useAuth()
  const isAdmin = session?.role === 'ADMIN'
  const [items, setItems] = useState<AppNotification[] | null>(null)
  const [error, setError] = useState('')
  const [onlyUnread, setOnlyUnread] = useState(true)
  const [busy, setBusy] = useState(false)
  const [confirmDelete, setConfirmDelete] = useState<number | null>(null)

  const load = useCallback(() => {
    if (!session) return
    loadNotifications(session.role).then(setItems).catch((e: Error) => setError(e.message))
  }, [session])
  useEffect(load, [load])

  useEffect(() => {
    if (confirmDelete === null) return
    const t = setTimeout(() => setConfirmDelete(null), 4000)
    return () => clearTimeout(t)
  }, [confirmDelete])

  const unread = useMemo(() => (items ?? []).filter((n) => !n.isRead), [items])
  const visible = useMemo(
    () => [...(onlyUnread ? unread : items ?? [])].sort((a, b) => b.createdAt.localeCompare(a.createdAt)),
    [items, unread, onlyUnread],
  )

  async function markRead(ids: number[]) {
    setBusy(true)
    try {
      await Promise.all(ids.map((id) => api.markNotificationRead(id)))
      setItems((prev) => prev?.map((n) => (ids.includes(n.id) ? { ...n, isRead: true } : n)) ?? prev)
      notifyChanged()
    } catch (err) {
      toast.error((err as Error).message)
      load()
    } finally {
      setBusy(false)
    }
  }

  async function generate() {
    setBusy(true)
    try {
      await api.generateInventoryAlerts()
      load()
      notifyChanged()
      toast.success('Alertas de inventario actualizadas')
    } catch (err) {
      toast.error((err as Error).message)
    } finally {
      setBusy(false)
    }
  }

  async function remove(id: number) {
    setBusy(true)
    try {
      await api.deleteNotification(id)
      setItems((prev) => prev?.filter((n) => n.id !== id) ?? prev)
      notifyChanged()
    } catch (err) {
      toast.error((err as Error).message)
    } finally {
      setBusy(false)
      setConfirmDelete(null)
    }
  }

  if (error) return <p className="error-text" role="alert">{error}</p>

  return (
    <>
      <div className="page-head">
        <div>
          <h1>Notificaciones</h1>
          <p>{items ? (unread.length === 0 ? 'Estás al día.' : `${unread.length} sin leer`) : 'Cargando…'}</p>
        </div>
        <div className="toolbar" style={{ margin: 0 }}>
          {isAdmin && (
            <button className="btn" disabled={busy} onClick={() => void generate()}>
              Revisar inventario
            </button>
          )}
          {unread.length > 0 && (
            <button className="btn btn-primary" disabled={busy} onClick={() => void markRead(unread.map((n) => n.id))}>
              Marcar todas como leídas
            </button>
          )}
        </div>
      </div>

      <div className="toolbar">
        <div className="seg" role="group" aria-label="Filtrar notificaciones">
          <button className="seg-btn" aria-pressed={onlyUnread} onClick={() => setOnlyUnread(true)}>
            Sin leer <span className="seg-count">{unread.length}</span>
          </button>
          <button className="seg-btn" aria-pressed={!onlyUnread} onClick={() => setOnlyUnread(false)}>
            Todas <span className="seg-count">{items?.length ?? 0}</span>
          </button>
        </div>
        {isAdmin && <span className="hint">Como administrador ves las de todo el sistema.</span>}
      </div>

      {!items ? (
        <div className="skeleton" style={{ height: 200 }} role="status" aria-label="Cargando" />
      ) : visible.length === 0 ? (
        <div className="empty">
          <h3>{onlyUnread ? 'No hay nada pendiente' : 'Aún no hay notificaciones'}</h3>
          <p>
            {onlyUnread
              ? 'Cuando un producto baje de su mínimo o pase algo que requiera tu atención, te avisamos aquí.'
              : 'Las alertas de inventario y del sistema aparecerán aquí.'}
          </p>
          {isAdmin && (
            <button className="btn" disabled={busy} onClick={() => void generate()}>
              Revisar inventario ahora
            </button>
          )}
        </div>
      ) : (
        <div className="table-wrap">
          <ul className="notif-list">
            {visible.map((n) => (
              <li key={n.id} className={n.isRead ? 'notif read' : 'notif'}>
                <span className="notif-dot" aria-hidden="true" />
                <div className="notif-body">
                  <div className="notif-title">
                    {!n.isRead && <span className="sr-only">Sin leer: </span>}
                    {n.title}
                  </div>
                  <p>{n.message}</p>
                  <div className="notif-meta">
                    <span className="pill">{TYPE_LABEL[n.type] ?? n.type}</span>
                    <time dateTime={n.createdAt.replace(/Z$/, '')}>{dateFmt.format(parseLocal(n.createdAt))}</time>
                    {n.type === 'INVENTORY_ALERT' && (
                      <Link to="/" className="text-link">
                        Ver reposición
                      </Link>
                    )}
                  </div>
                </div>
                <div className="notif-actions">
                  {!n.isRead && (
                    <button className="btn btn-ghost" disabled={busy} onClick={() => void markRead([n.id])}>
                      Marcar como leída
                    </button>
                  )}
                  {isAdmin && (
                    <button
                      className={confirmDelete === n.id ? 'btn btn-danger' : 'btn btn-ghost'}
                      disabled={busy}
                      onClick={() => (confirmDelete === n.id ? void remove(n.id) : setConfirmDelete(n.id))}
                    >
                      {confirmDelete === n.id ? 'Confirmar' : 'Eliminar'}
                    </button>
                  )}
                </div>
              </li>
            ))}
          </ul>
        </div>
      )}
    </>
  )
}
