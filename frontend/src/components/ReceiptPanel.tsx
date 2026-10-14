import { useEffect, useState } from 'react'
import { toast } from 'sonner'
import { api, type Receipt } from '../api'
import { formatDate } from '../format'
import ReceiptStatusPill from './ReceiptStatusPill'
import SidePanel from './SidePanel'

interface Props {
  receipt: Receipt
  editable: boolean
  onClose: () => void
  onChanged: (r: Receipt) => void
}

export default function ReceiptPanel({ receipt, editable, onClose, onChanged }: Props) {
  const [busy, setBusy] = useState(false)
  const [confirmReject, setConfirmReject] = useState(false)
  const pending = receipt.status === 'PENDING'

  useEffect(() => {
    if (!confirmReject) return
    const t = setTimeout(() => setConfirmReject(false), 4000)
    return () => clearTimeout(t)
  }, [confirmReject])

  async function act(kind: 'receive' | 'reject') {
    setBusy(true)
    try {
      const r = await (kind === 'receive' ? api.receiveReceipt(receipt.id) : api.rejectReceipt(receipt.id))
      onChanged(r)
      toast.success(kind === 'receive' ? 'Mercancía recibida: el stock aumentó' : 'Recepción rechazada: el stock no cambió')
    } catch (err) {
      toast.error((err as Error).message)
    } finally {
      setBusy(false)
      setConfirmReject(false)
    }
  }

  return (
    <SidePanel title={`Recepción ${receipt.receiptNumber}`} onClose={onClose}>
      <dl className="detail">
        <div>
          <dt>Proveedor</dt>
          <dd>{receipt.supplierName}</dd>
        </div>
        <div>
          <dt>Orden</dt>
          <dd>{receipt.orderNumber}</dd>
        </div>
        <div>
          <dt>Estado</dt>
          <dd><ReceiptStatusPill status={receipt.status} /></dd>
        </div>
        <div>
          <dt>Creada</dt>
          <dd>{formatDate(receipt.createdAt)}</dd>
        </div>
        {receipt.notes && (
          <div>
            <dt>Notas</dt>
            <dd>{receipt.notes}</dd>
          </div>
        )}
      </dl>

      <div className="table-wrap">
        <table>
          <thead>
            <tr>
              <th>Producto</th>
              <th className="num">Pedido</th>
              <th className="num">Recibido</th>
            </tr>
          </thead>
          <tbody>
            {receipt.items.map((it) => (
              <tr key={it.id} style={{ cursor: 'default' }}>
                <td>
                  <div className="cell-name">
                    <strong style={{ fontWeight: 600 }}>{it.productName}</strong>
                    <span>{it.productCode}</span>
                  </div>
                </td>
                <td className="num">{it.orderedQuantity}</td>
                <td className="num">
                  {it.receivedQuantity}
                  {it.receivedQuantity !== it.orderedQuantity && (
                    <span className="diff"> ({it.receivedQuantity - it.orderedQuantity > 0 ? '+' : ''}{it.receivedQuantity - it.orderedQuantity})</span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {editable && pending && (
        <>
          <p className="hint">Al marcarla como recibida, el stock de cada producto aumenta por la cantidad recibida.</p>
          <div className="form-actions">
            <button
              className={confirmReject ? 'btn btn-danger' : 'btn btn-ghost'}
              disabled={busy}
              onClick={() => (confirmReject ? void act('reject') : setConfirmReject(true))}
            >
              {confirmReject ? 'Confirmar rechazo' : 'Rechazar'}
            </button>
            <button className="btn btn-primary" disabled={busy} onClick={() => void act('receive')} data-autofocus>
              Marcar como recibida
            </button>
          </div>
        </>
      )}
      {!pending && <p className="hint">Esta recepción ya está cerrada y no se puede modificar.</p>}
    </SidePanel>
  )
}
