import { useCallback, useEffect, useMemo, useState } from 'react'
import { api, canWriteReceipts, type Receipt, type ReceiptStatus } from '../api'
import { useAuth } from '../auth'
import NewReceiptPanel from '../components/NewReceiptPanel'
import ReceiptPanel from '../components/ReceiptPanel'
import ReceiptStatusPill from '../components/ReceiptStatusPill'
import { formatDate } from '../format'

const FILTERS: { value: ReceiptStatus | 'ALL'; label: string }[] = [
  { value: 'PENDING', label: 'Pendientes' },
  { value: 'RECEIVED', label: 'Recibidas' },
  { value: 'REJECTED', label: 'Rechazadas' },
  { value: 'ALL', label: 'Todas' },
]

export default function Receipts() {
  const { session } = useAuth()
  const editable = session ? canWriteReceipts(session.role) : false
  const [receipts, setReceipts] = useState<Receipt[] | null>(null)
  const [error, setError] = useState('')
  const [filter, setFilter] = useState<ReceiptStatus | 'ALL'>('PENDING')
  const [query, setQuery] = useState('')
  const [selectedId, setSelectedId] = useState<number | null>(null)
  const [creating, setCreating] = useState(false)

  const load = useCallback(() => {
    api.receipts().then(setReceipts).catch((e: Error) => setError(e.message))
  }, [])
  useEffect(load, [load])

  // "Recibida parcial" se cuenta junto a "Recibidas"
  const group = (s: ReceiptStatus): ReceiptStatus => (s === 'PARTIALLY_RECEIVED' ? 'RECEIVED' : s)

  const counts = useMemo(() => {
    const c: Record<string, number> = { ALL: receipts?.length ?? 0, PENDING: 0, RECEIVED: 0, REJECTED: 0 }
    receipts?.forEach((r) => (c[group(r.status)] = (c[group(r.status)] ?? 0) + 1))
    return c
  }, [receipts])

  const visible = useMemo(() => {
    const q = query.trim().toLowerCase()
    return (receipts ?? [])
      .filter(
        (r) =>
          (filter === 'ALL' || group(r.status) === filter) &&
          (!q || r.receiptNumber.toLowerCase().includes(q) || r.orderNumber.toLowerCase().includes(q) || r.supplierName.toLowerCase().includes(q)),
      )
      .sort((a, b) => b.createdAt.localeCompare(a.createdAt))
  }, [receipts, filter, query])

  const selected = receipts?.find((r) => r.id === selectedId) ?? null

  if (error) return <p className="error-text" role="alert">{error}</p>

  return (
    <>
      <div className="page-head">
        <div>
          <h1>Recepciones</h1>
          <p>{receipts ? `${counts.PENDING} por confirmar de ${receipts.length}` : 'Cargando…'}</p>
        </div>
        {editable && (
          <button className="btn btn-primary" onClick={() => setCreating(true)}>
            Nueva recepción
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
          placeholder="Buscar por número, orden o proveedor"
          aria-label="Buscar recepciones"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </div>

      {!receipts ? (
        <div className="skeleton" style={{ height: 240 }} role="status" aria-label="Cargando" />
      ) : visible.length === 0 ? (
        <div className="empty">
          <h3>{receipts.length === 0 ? 'Aún no hay recepciones' : 'Sin resultados'}</h3>
          <p>
            {receipts.length === 0
              ? editable
                ? 'Cuando llegue mercancía de un proveedor, regístrala aquí para sumarla al stock.'
                : 'Cuando se registre mercancía recibida aparecerá aquí.'
              : 'Prueba con otro estado o quita la búsqueda.'}
          </p>
        </div>
      ) : (
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Recepción</th>
                <th className="hide-sm">Orden</th>
                <th className="hide-sm">Fecha</th>
                <th className="num">Estado</th>
              </tr>
            </thead>
            <tbody>
              {visible.map((r) => (
                <tr key={r.id} onClick={() => setSelectedId(r.id)}>
                  <td>
                    <div className="cell-name">
                      <button className="row-link" onClick={(e) => { e.stopPropagation(); setSelectedId(r.id) }}>{r.receiptNumber}</button>
                      <span>{r.supplierName}</span>
                    </div>
                  </td>
                  <td className="hide-sm">{r.orderNumber}</td>
                  <td className="hide-sm">{formatDate(r.createdAt)}</td>
                  <td className="num"><ReceiptStatusPill status={r.status} /></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {selected && (
        <ReceiptPanel
          receipt={selected}
          editable={editable}
          onClose={() => setSelectedId(null)}
          onChanged={(r) => setReceipts((prev) => prev?.map((x) => (x.id === r.id ? r : x)) ?? prev)}
        />
      )}
      {creating && (
        <NewReceiptPanel
          existing={receipts ?? []}
          onClose={() => setCreating(false)}
          onCreated={(r) => {
            setCreating(false)
            setFilter('PENDING')
            setReceipts((prev) => [r, ...(prev ?? [])])
          }}
        />
      )}
    </>
  )
}
