import { useCallback, useEffect, useMemo, useState } from 'react'
import { api, canManageCustomers, canWriteOrders, type Customer, type Order, type OrderStatus } from '../api'
import { useAuth } from '../auth'
import NewOrderPanel from '../components/NewOrderPanel'
import OrderPanel from '../components/OrderPanel'
import OrderStatusPill from '../components/OrderStatusPill'
import { formatDate, money } from '../format'

const FILTERS: { value: OrderStatus | 'ALL'; label: string }[] = [
  { value: 'ALL', label: 'Todas' },
  { value: 'PENDING', label: 'Pendientes' },
  { value: 'COMPLETED', label: 'Completadas' },
  { value: 'CANCELLED', label: 'Canceladas' },
]

export default function Orders() {
  const { session } = useAuth()
  const editable = session ? canWriteOrders(session.role) : false
  const [rawOrders, setOrders] = useState<Order[] | null>(null)
  const [customers, setCustomers] = useState<Customer[]>([])
  const [error, setError] = useState('')
  const [filter, setFilter] = useState<OrderStatus | 'ALL'>('PENDING')
  const [query, setQuery] = useState('')
  const [selectedId, setSelectedId] = useState<number | null>(null)
  const [creating, setCreating] = useState(false)

  const load = useCallback(() => {
    api.orders().then(setOrders).catch((e: Error) => setError(e.message))
  }, [])
  useEffect(load, [load])

  // Respaldo: el backend guarda el nombre del cliente al crear la orden (y V2 rellena las antiguas);
  // si algún dato viene vacío se resuelve con la lista de clientes (si el rol puede leerla).
  useEffect(() => {
    if (session && canManageCustomers(session.role)) api.customers().then(setCustomers).catch(() => setCustomers([]))
  }, [session])

  const orders = useMemo(() => {
    const byId = new Map(customers.map((c) => [c.id, c]))
    return rawOrders?.map((o) => ({
      ...o,
      customerName: o.customerName ?? byId.get(o.customerId)?.name ?? `Cliente #${o.customerId}`,
      customerCode: o.customerCode ?? byId.get(o.customerId)?.code ?? '',
    })) ?? null
  }, [rawOrders, customers])
  const selected = orders?.find((o) => o.id === selectedId) ?? null

  const counts = useMemo(() => {
    const c: Record<string, number> = { ALL: orders?.length ?? 0, PENDING: 0, COMPLETED: 0, CANCELLED: 0 }
    orders?.forEach((o) => (c[o.status] = (c[o.status] ?? 0) + 1))
    return c
  }, [orders])

  const visible = useMemo(() => {
    const q = query.trim().toLowerCase()
    return (orders ?? [])
      .filter((o) => (filter === 'ALL' || o.status === filter) && (!q || o.orderNumber.toLowerCase().includes(q) || o.customerName.toLowerCase().includes(q)))
      .sort((a, b) => b.orderDate.localeCompare(a.orderDate))
  }, [orders, filter, query])

  function changed(o: Order) {
    setOrders((prev) => prev?.map((x) => (x.id === o.id ? o : x)) ?? prev)
  }

  if (error) return <p className="error-text" role="alert">{error}</p>

  return (
    <>
      <div className="page-head">
        <div>
          <h1>Órdenes</h1>
          <p>{orders ? `${counts.PENDING} pendientes de ${orders.length}` : 'Cargando…'}</p>
        </div>
        {editable && (
          <button className="btn btn-primary" onClick={() => setCreating(true)}>
            Nueva orden
          </button>
        )}
      </div>

      <div className="toolbar">
        <div className="seg" role="group" aria-label="Filtrar por estado">
          {FILTERS.map((f) => (
            <button key={f.value} className="seg-btn" aria-pressed={filter === f.value} onClick={() => setFilter(f.value)}>
              {f.label} <span className="seg-count">{counts[f.value] ?? 0}</span>
            </button>
          ))}
        </div>
        <input
          className="input"
          type="search"
          placeholder="Buscar por número o cliente"
          aria-label="Buscar órdenes"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </div>

      {!orders ? (
        <div className="skeleton" style={{ height: 240 }} role="status" aria-label="Cargando" />
      ) : visible.length === 0 ? (
        <div className="empty">
          <h3>{orders.length === 0 ? 'Aún no hay órdenes' : 'Sin resultados'}</h3>
          <p>
            {orders.length === 0
              ? editable
                ? 'Crea la primera orden para descontar stock y llevar el historial.'
                : 'Cuando se registren órdenes aparecerán aquí.'
              : 'Prueba con otro estado o quita la búsqueda.'}
          </p>
        </div>
      ) : (
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Orden</th>
                <th className="hide-sm">Fecha</th>
                <th className="num">Total</th>
                <th className="num">Estado</th>
              </tr>
            </thead>
            <tbody>
              {visible.map((o) => (
                <tr key={o.id} onClick={() => setSelectedId(o.id)}>
                  <td>
                    <div className="cell-name">
                      <button className="row-link" onClick={(e) => { e.stopPropagation(); setSelectedId(o.id) }}>{o.orderNumber}</button>
                      <span>{o.customerName}</span>
                    </div>
                  </td>
                  <td className="hide-sm">{formatDate(o.orderDate)}</td>
                  <td className="num">{money.format(o.total)}</td>
                  <td className="num"><OrderStatusPill status={o.status} /></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {selected && <OrderPanel order={selected} editable={editable} onClose={() => setSelectedId(null)} onChanged={changed} />}
      {creating && (
        <NewOrderPanel
          onClose={() => setCreating(false)}
          onCreated={(o) => {
            setCreating(false)
            setFilter('PENDING')
            setOrders((prev) => [o, ...(prev ?? [])])
          }}
        />
      )}
    </>
  )
}
