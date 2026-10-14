import { useCallback, useEffect, useMemo, useState } from 'react'
import { api, type Customer } from '../api'
import CustomerPanel from '../components/CustomerPanel'

export default function Customers() {
  const [customers, setCustomers] = useState<Customer[] | null>(null)
  const [error, setError] = useState('')
  const [query, setQuery] = useState('')
  const [selected, setSelected] = useState<Customer | 'new' | null>(null)

  const load = useCallback(() => {
    api.customers().then(setCustomers).catch((e: Error) => setError(e.message))
  }, [])
  useEffect(load, [load])

  const visible = useMemo(() => {
    const q = query.trim().toLowerCase()
    return (customers ?? []).filter(
      (c) => !q || c.name.toLowerCase().includes(q) || c.code.toLowerCase().includes(q) || (c.city ?? '').toLowerCase().includes(q),
    )
  }, [customers, query])

  function saved(c: Customer) {
    setCustomers((prev) => (prev?.some((x) => x.id === c.id) ? prev.map((x) => (x.id === c.id ? c : x)) : [c, ...(prev ?? [])]))
    setSelected(null)
  }

  if (error) return <p className="error-text" role="alert">{error}</p>

  return (
    <>
      <div className="page-head">
        <div>
          <h1>Clientes</h1>
          <p>{customers ? `${customers.length} registrados` : 'Cargando…'}</p>
        </div>
        <button className="btn btn-primary" onClick={() => setSelected('new')}>
          Nuevo cliente
        </button>
      </div>

      <div className="toolbar">
        <input
          className="input"
          type="search"
          placeholder="Buscar por nombre, código o ciudad"
          aria-label="Buscar clientes"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </div>

      {!customers ? (
        <div className="skeleton" style={{ height: 240 }} role="status" aria-label="Cargando" />
      ) : visible.length === 0 ? (
        <div className="empty">
          <h3>{customers.length === 0 ? 'Aún no hay clientes' : 'Sin resultados'}</h3>
          <p>{customers.length === 0 ? 'Crea el primero para poder registrar órdenes a su nombre.' : 'Prueba con otro nombre, código o ciudad.'}</p>
        </div>
      ) : (
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Cliente</th>
                <th className="hide-sm">Ciudad</th>
                <th className="hide-sm">Contacto</th>
                <th className="num">Tipo</th>
              </tr>
            </thead>
            <tbody>
              {visible.map((c) => (
                <tr key={c.id} className={c.active === false ? 'inactive' : ''} onClick={() => setSelected(c)}>
                  <td>
                    <div className="cell-name">
                      <button className="row-link" onClick={(e) => { e.stopPropagation(); setSelected(c) }}>{c.name}</button>
                      <span>{c.code}{c.active === false ? ' · inactivo' : ''}</span>
                    </div>
                  </td>
                  <td className="hide-sm">{c.city ?? '—'}</td>
                  <td className="hide-sm">{c.phone ?? c.email ?? '—'}</td>
                  <td className="num"><span className="pill">{c.customerType === 'MAYORISTA' ? 'Mayorista' : 'Minorista'}</span></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {selected && <CustomerPanel customer={selected === 'new' ? null : selected} onClose={() => setSelected(null)} onSaved={saved} />}
    </>
  )
}
