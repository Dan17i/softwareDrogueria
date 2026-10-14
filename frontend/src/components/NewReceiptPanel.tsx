import { useEffect, useMemo, useState, type FormEvent } from 'react'
import { toast } from 'sonner'
import { api, type Order, type Receipt } from '../api'
import SidePanel from './SidePanel'

interface Props {
  existing: Receipt[]
  onClose: () => void
  onCreated: (r: Receipt) => void
}

export default function NewReceiptPanel({ existing, onClose, onCreated }: Props) {
  const [orders, setOrders] = useState<Order[] | null>(null)
  const [loadError, setLoadError] = useState('')
  const [orderId, setOrderId] = useState('')
  const [received, setReceived] = useState<Record<number, string>>({})
  const [notes, setNotes] = useState('')
  const [busy, setBusy] = useState(false)

  useEffect(() => {
    api.orders().then(setOrders).catch((e: Error) => setLoadError(e.message))
  }, [])

  // El backend solo admite recepciones de órdenes completadas, con proveedor y sin otra recepción ya recibida.
  const eligible = useMemo(() => {
    const done = new Set(existing.filter((r) => r.status === 'RECEIVED').map((r) => r.orderId))
    return (orders ?? []).filter((o) => o.status === 'COMPLETED' && o.supplierId != null && !done.has(o.id))
  }, [orders, existing])

  const partial = useMemo(() => new Set(existing.filter((r) => r.status === 'PARTIALLY_RECEIVED').map((r) => r.orderId)), [existing])
  const order = eligible.find((o) => String(o.id) === orderId)

  function pick(id: string) {
    setOrderId(id)
    const o = eligible.find((x) => String(x.id) === id)
    // Lo más común es que llegue todo lo pedido: se precarga y solo se corrige la diferencia.
    setReceived(Object.fromEntries((o?.items ?? []).map((it) => [it.id, String(it.quantity)])))
  }

  const valid = order && order.items.every((it) => received[it.id] !== '' && Number(received[it.id]) >= 0)

  async function submit(e: FormEvent) {
    e.preventDefault()
    if (!order || !valid) return
    setBusy(true)
    try {
      const r = await api.createReceipt({
        orderId: order.id,
        items: order.items.map((it) => ({
          productId: it.productId,
          productCode: it.productCode,
          productName: it.productName,
          orderedQuantity: it.quantity,
          receivedQuantity: Number(received[it.id]),
        })),
        notes: notes.trim() || null,
      })
      toast.success(`Recepción ${r.receiptNumber} creada`)
      onCreated(r)
    } catch (err) {
      toast.error((err as Error).message)
    } finally {
      setBusy(false)
    }
  }

  return (
    <SidePanel title="Nueva recepción" onClose={onClose}>
      {loadError ? (
        <p className="error-text" role="alert">{loadError}</p>
      ) : orders === null ? (
        <div className="skeleton" style={{ height: 160 }} role="status" aria-label="Cargando" />
      ) : eligible.length === 0 ? (
        <div className="empty" style={{ padding: 0 }}>
          <h3>No hay órdenes por recibir</h3>
          <p>Una recepción parte de una orden completada que tenga proveedor. Crea la orden con proveedor y complétala primero.</p>
        </div>
      ) : (
        <form onSubmit={submit}>
          <div className="field">
            <label htmlFor="r-order">Orden</label>
            <select id="r-order" className="input" value={orderId} onChange={(e) => pick(e.target.value)} required>
              <option value="">Elige una orden…</option>
              {eligible.map((o) => (
                <option key={o.id} value={o.id}>
                  {o.orderNumber}{partial.has(o.id) ? ' · ya tiene una recepción parcial' : ''}
                </option>
              ))}
            </select>
          </div>

          {order && (
            <>
              <fieldset className="lines">
                <legend>Lo que llegó</legend>
                {order.items.map((it) => {
                  const n = Number(received[it.id])
                  return (
                    <div className="receipt-line" key={it.id}>
                      <div className="cell-name">
                        <strong style={{ fontWeight: 600 }}>{it.productName}</strong>
                        <span>Pedido: {it.quantity}</span>
                      </div>
                      <input
                        className="input"
                        type="number"
                        inputMode="numeric"
                        min="0"
                        step="1"
                        aria-label={`Recibido de ${it.productName}`}
                        value={received[it.id] ?? ''}
                        onChange={(e) => setReceived((r) => ({ ...r, [it.id]: e.target.value }))}
                      />
                      {n !== it.quantity && received[it.id] !== '' && (
                        <p className="hint line-hint" style={{ color: 'var(--warn)' }}>
                          {n > it.quantity ? `Llegaron ${n - it.quantity} de más.` : `Faltan ${it.quantity - n}.`}
                        </p>
                      )}
                    </div>
                  )
                })}
              </fieldset>
              <div className="field">
                <label htmlFor="r-notes">Notas (opcional)</label>
                <textarea id="r-notes" className="input" value={notes} onChange={(e) => setNotes(e.target.value)} maxLength={1000} />
              </div>
            </>
          )}

          <div className="form-actions">
            <button type="button" className="btn btn-ghost" onClick={onClose}>
              Cancelar
            </button>
            <button className="btn btn-primary" disabled={!valid || busy}>
              {busy ? 'Creando…' : 'Crear recepción'}
            </button>
          </div>
          <p className="hint">La recepción queda pendiente: el stock solo sube cuando la marcas como recibida.</p>
        </form>
      )}
    </SidePanel>
  )
}
