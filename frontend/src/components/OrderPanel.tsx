import { useEffect, useState } from 'react'
import { toast } from 'sonner'
import { api, type Order } from '../api'
import { formatDate, money } from '../format'
import OrderStatusPill from './OrderStatusPill'
import SidePanel from './SidePanel'

interface Props {
  order: Order
  editable: boolean
  onClose: () => void
  onChanged: (o: Order) => void
}

export default function OrderPanel({ order, editable, onClose, onChanged }: Props) {
  const [busy, setBusy] = useState(false)
  const [confirmCancel, setConfirmCancel] = useState(false)
  const pending = order.status === 'PENDING'

  // La confirmación de cancelar expira sola
  useEffect(() => {
    if (!confirmCancel) return
    const t = setTimeout(() => setConfirmCancel(false), 4000)
    return () => clearTimeout(t)
  }, [confirmCancel])

  async function act(kind: 'complete' | 'cancel') {
    setBusy(true)
    try {
      const o = await (kind === 'complete' ? api.completeOrder(order.id) : api.cancelOrder(order.id))
      onChanged(o)
      toast.success(kind === 'complete' ? 'Orden completada' : 'Orden cancelada: el stock volvió al inventario')
    } catch (err) {
      toast.error((err as Error).message)
    } finally {
      setBusy(false)
      setConfirmCancel(false)
    }
  }

  return (
    <SidePanel title={`Orden ${order.orderNumber}`} onClose={onClose}>
      <dl className="detail">
        <div>
          <dt>Cliente</dt>
          <dd>{order.customerName} <span className="muted">· {order.customerCode}</span></dd>
        </div>
        <div>
          <dt>Estado</dt>
          <dd><OrderStatusPill status={order.status} /></dd>
        </div>
        <div>
          <dt>Fecha</dt>
          <dd>{formatDate(order.orderDate)}</dd>
        </div>
        {order.notes && (
          <div>
            <dt>Notas</dt>
            <dd>{order.notes}</dd>
          </div>
        )}
      </dl>

      <div className="table-wrap">
        <table>
          <thead>
            <tr>
              <th>Producto</th>
              <th className="num">Cant.</th>
              <th className="num">Subtotal</th>
            </tr>
          </thead>
          <tbody>
            {order.items.map((it) => (
              <tr key={it.id} style={{ cursor: 'default' }}>
                <td>
                  <div className="cell-name">
                    <strong style={{ fontWeight: 600 }}>{it.productName}</strong>
                    <span>{money.format(it.unitPrice)} c/u</span>
                  </div>
                </td>
                <td className="num">{it.quantity}</td>
                <td className="num">{money.format(it.subtotal)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <p className="total-line">
        Total <strong>{money.format(order.total)}</strong>
      </p>

      {editable && pending && (
        <div className="form-actions">
          <button
            className={confirmCancel ? 'btn btn-danger' : 'btn btn-ghost'}
            disabled={busy}
            onClick={() => (confirmCancel ? void act('cancel') : setConfirmCancel(true))}
          >
            {confirmCancel ? 'Confirmar cancelación' : 'Cancelar orden'}
          </button>
          <button className="btn btn-primary" disabled={busy} onClick={() => void act('complete')} data-autofocus>
            Completar orden
          </button>
        </div>
      )}
      {!pending && <p className="hint">Esta orden ya está {order.status === 'COMPLETED' ? 'completada' : 'cancelada'} y no se puede modificar.</p>}
    </SidePanel>
  )
}
