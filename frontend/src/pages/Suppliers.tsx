import { useCallback, useEffect, useMemo, useState } from 'react'
import { api, canReadSuppliers, type Supplier } from '../api'
import { useAuth } from '../auth'
import SupplierPanel from '../components/SupplierPanel'

export default function Suppliers() {
  const { session } = useAuth()
  const editable = session ? canReadSuppliers(session.role) : false
  const [suppliers, setSuppliers] = useState<Supplier[] | null>(null)
  const [error, setError] = useState('')
  const [query, setQuery] = useState('')
  const [selected, setSelected] = useState<Supplier | 'new' | null>(null)

  const load = useCallback(() => {
    api.suppliers().then(setSuppliers).catch((e: Error) => setError(e.message))
  }, [])
  useEffect(load, [load])

  const visible = useMemo(() => {
    const q = query.trim().toLowerCase()
    return (suppliers ?? []).filter(
      (s) => !q || s.name.toLowerCase().includes(q) || s.code.toLowerCase().includes(q) || (s.city ?? '').toLowerCase().includes(q),
    )
  }, [suppliers, query])

  function saved(s: Supplier) {
    setSuppliers((prev) => (prev?.some((x) => x.id === s.id) ? prev.map((x) => (x.id === s.id ? s : x)) : [s, ...(prev ?? [])]))
    setSelected(null)
  }

  if (error) return <p className="error-text" role="alert">{error}</p>

  return (
    <>
      <div className="page-head">
        <div>
          <h1>Proveedores</h1>
          <p>{suppliers ? `${suppliers.length} registrados` : 'Cargando…'}</p>
        </div>
        {editable && (
          <button className="btn btn-primary" onClick={() => setSelected('new')}>
            Nuevo proveedor
          </button>
        )}
      </div>

      <div className="toolbar">
        <input
          className="input"
          type="search"
          placeholder="Buscar por nombre, código o ciudad"
          aria-label="Buscar proveedores"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </div>

      {!suppliers ? (
        <div className="skeleton" style={{ height: 240 }} role="status" aria-label="Cargando" />
      ) : visible.length === 0 ? (
        <div className="empty">
          <h3>{suppliers.length === 0 ? 'Aún no hay proveedores' : 'Sin resultados'}</h3>
          <p>
            {suppliers.length === 0
              ? editable
                ? 'Crea el primero para asignarlo a las órdenes y registrar la mercancía que llega.'
                : 'Cuando se registren proveedores aparecerán aquí.'
              : 'Prueba con otro nombre, código o ciudad.'}
          </p>
        </div>
      ) : (
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Proveedor</th>
                <th className="hide-sm">Ciudad</th>
                <th className="hide-sm">Contacto</th>
                <th className="num">Entrega</th>
              </tr>
            </thead>
            <tbody>
              {visible.map((s) => (
                <tr key={s.id} className={s.active === false ? 'inactive' : ''} onClick={() => setSelected(s)}>
                  <td>
                    <div className="cell-name">
                      <button className="row-link" onClick={(e) => { e.stopPropagation(); setSelected(s) }}>{s.name}</button>
                      <span>{s.code}{s.active === false ? ' · inactivo' : ''}</span>
                    </div>
                  </td>
                  <td className="hide-sm">{s.city ?? '—'}</td>
                  <td className="hide-sm">{s.phone ?? s.email ?? '—'}</td>
                  <td className="num">{s.leadTimeDays != null ? `${s.leadTimeDays} ${s.leadTimeDays === 1 ? 'día' : 'días'}` : '—'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {selected && <SupplierPanel supplier={selected === 'new' ? null : selected} onClose={() => setSelected(null)} onSaved={saved} />}
    </>
  )
}
