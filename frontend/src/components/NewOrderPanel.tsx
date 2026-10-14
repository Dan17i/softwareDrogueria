import { useEffect, useMemo, useState, type FormEvent } from 'react'
import { toast } from 'sonner'
import { api, canReadSuppliers, type Customer, type Order, type Product, type Supplier } from '../api'
import { useAuth } from '../auth'
import { money } from '../format'
import SidePanel from './SidePanel'

interface Line {
  key: number
  productId: string
  quantity: string
}

let seq = 0
const newLine = (): Line => ({ key: ++seq, productId: '', quantity: '1' })

export default function NewOrderPanel({ onClose, onCreated }: { onClose: () => void; onCreated: (o: Order) => void }) {
  const { session } = useAuth()
  const [suppliers, setSuppliers] = useState<Supplier[]>([])
  const [supplierId, setSupplierId] = useState('')
  const [customers, setCustomers] = useState<Customer[] | null>(null)
  const [products, setProducts] = useState<Product[]>([])
  const [loadError, setLoadError] = useState('')
  const [customerId, setCustomerId] = useState('')
  const [lines, setLines] = useState<Line[]>([newLine()])
  const [notes, setNotes] = useState('')
  const [busy, setBusy] = useState(false)

  useEffect(() => {
    Promise.all([api.customers(), api.products()])
      .then(([c, p]) => {
        setCustomers(c.filter((x) => x.active !== false))
        setProducts(p.filter((x) => x.active !== false))
      })
      .catch((e: Error) => setLoadError(e.message))
  }, [])

  // El proveedor es opcional, pero sin él la orden no admite recepción de mercancía.
  useEffect(() => {
    if (session && canReadSuppliers(session.role)) api.suppliers().then((l) => setSuppliers(l.filter((x) => x.active !== false))).catch(() => setSuppliers([]))
  }, [session])

  const byId = useMemo(() => new Map(products.map((p) => [String(p.id), p])), [products])
  const total = lines.reduce((sum, l) => sum + (byId.get(l.productId)?.price ?? 0) * (Number(l.quantity) || 0), 0)
  const overStock = lines.some((l) => {
    const p = byId.get(l.productId)
    return p ? Number(l.quantity) > p.stock : false
  })
  const customer = customers?.find((c) => String(c.id) === customerId)
  const available = customer ? (customer.creditLimit ?? 0) - (customer.pendingBalance ?? 0) : null
  const overCredit = available !== null && total > available
  const ready =
    customerId !== '' && !overCredit && lines.length > 0 && lines.every((l) => l.productId !== '' && Number(l.quantity) >= 1) && !overStock

  const setLine = (key: number, patch: Partial<Line>) => setLines((ls) => ls.map((l) => (l.key === key ? { ...l, ...patch } : l)))

  async function submit(e: FormEvent) {
    e.preventDefault()
    if (!ready) return
    setBusy(true)
    try {
      const o = await api.createOrder({
        customerId: Number(customerId),
        supplierId: supplierId ? Number(supplierId) : null,
        items: lines.map((l) => ({ productId: Number(l.productId), quantity: Number(l.quantity) })),
        notes: notes.trim() || null,
      })
      toast.success(`Orden ${o.orderNumber} creada`)
      onCreated(o)
    } catch (err) {
      const msg = (err as Error).message
      toast.error(/cr[eé]dito/i.test(msg) ? 'El cliente no tiene cupo de crédito suficiente. Sube su cupo en Clientes o reduce la orden.' : msg)
    } finally {
      setBusy(false)
    }
  }

  return (
    <SidePanel title="Nueva orden" onClose={onClose}>
      {loadError ? (
        <p className="error-text" role="alert">{loadError}</p>
      ) : customers === null ? (
        <div className="skeleton" style={{ height: 160 }} role="status" aria-label="Cargando" />
      ) : customers.length === 0 ? (
        <div className="empty" style={{ padding: 0 }}>
          <h3>Primero crea un cliente</h3>
          <p>Una orden necesita un cliente. Créalo en la sección Clientes y vuelve aquí.</p>
        </div>
      ) : (
        <form onSubmit={submit}>
          <div className="field">
            <label htmlFor="customer">Cliente</label>
            <select id="customer" className="input" value={customerId} onChange={(e) => setCustomerId(e.target.value)} required>
              <option value="">Elige un cliente…</option>
              {customers.map((c) => (
                <option key={c.id} value={c.id} disabled={!c.creditLimit}>
                  {c.name} · {c.code}{c.creditLimit ? '' : ' · sin cupo de crédito'}
                </option>
              ))}
            </select>
          </div>

          {suppliers.length > 0 && (
            <div className="field">
              <label htmlFor="supplier">Proveedor (opcional)</label>
              <select id="supplier" className="input" value={supplierId} onChange={(e) => setSupplierId(e.target.value)} aria-describedby="supplier-hint">
                <option value="">Sin proveedor</option>
                {suppliers.map((sp) => (
                  <option key={sp.id} value={sp.id}>
                    {sp.name} · {sp.code}
                  </option>
                ))}
              </select>
              <span id="supplier-hint" className="hint">Necesario para registrar después la recepción de mercancía.</span>
            </div>
          )}

          <fieldset className="lines">
            <legend>Productos</legend>
            {lines.map((l, i) => {
              const p = byId.get(l.productId)
              const over = p ? Number(l.quantity) > p.stock : false
              return (
                <div className="line" key={l.key}>
                  <select
                    className="input"
                    aria-label={`Producto ${i + 1}`}
                    value={l.productId}
                    onChange={(e) => setLine(l.key, { productId: e.target.value })}
                    required
                  >
                    <option value="">Elige un producto…</option>
                    {products.map((pr) => (
                      <option key={pr.id} value={pr.id} disabled={pr.stock <= 0}>
                        {pr.name} · {pr.stock > 0 ? `${pr.stock} en stock` : 'agotado'}
                      </option>
                    ))}
                  </select>
                  <input
                    className="input"
                    type="number"
                    inputMode="numeric"
                    min="1"
                    step="1"
                    aria-label={`Cantidad ${i + 1}`}
                    value={l.quantity}
                    onChange={(e) => setLine(l.key, { quantity: e.target.value })}
                  />
                  <button
                    type="button"
                    className="btn btn-ghost"
                    onClick={() => setLines((ls) => ls.filter((x) => x.key !== l.key))}
                    disabled={lines.length === 1}
                    aria-label={`Quitar producto ${i + 1}`}
                  >
                    Quitar
                  </button>
                  {over && <p className="hint line-hint">Solo hay {p?.stock} en stock.</p>}
                </div>
              )
            })}
            <button type="button" className="btn" onClick={() => setLines((ls) => [...ls, newLine()])}>
              Agregar otro producto
            </button>
          </fieldset>

          <div className="field">
            <label htmlFor="notes">Notas (opcional)</label>
            <textarea id="notes" className="input" value={notes} onChange={(e) => setNotes(e.target.value)} maxLength={500} />
          </div>

          {overCredit && customer && (
            <p className="hint" role="alert" style={{ color: 'var(--danger)' }}>
              El cupo disponible de {customer.name} es {money.format(Math.max(available ?? 0, 0))}. Sube el cupo en Clientes o reduce la orden.
            </p>
          )}
          <p className="total-line">
            Total <strong>{money.format(total)}</strong>
          </p>
          <div className="form-actions">
            <button type="button" className="btn btn-ghost" onClick={onClose}>
              Cancelar
            </button>
            <button className="btn btn-primary" disabled={!ready || busy}>
              {busy ? 'Creando…' : 'Crear orden'}
            </button>
          </div>
        </form>
      )}
    </SidePanel>
  )
}
