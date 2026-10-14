import { useCallback, useEffect, useMemo, useState } from 'react'
import { toast } from 'sonner'
import { api, canEditInventory, type Product } from '../api'
import { useAuth } from '../auth'
import ProductPanel from '../components/ProductPanel'

const money = new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 })

export default function Products() {
  const { session } = useAuth()
  const editable = session ? canEditInventory(session.role) : false
  const [products, setProducts] = useState<Product[] | null>(null)
  const [error, setError] = useState('')
  const [query, setQuery] = useState('')
  const [onlyLow, setOnlyLow] = useState(false)
  const [selected, setSelected] = useState<Product | 'new' | null>(null)

  const load = useCallback(() => {
    api.products().then(setProducts).catch((e: Error) => setError(e.message))
  }, [])
  useEffect(load, [load])

  const visible = useMemo(() => {
    const q = query.trim().toLowerCase()
    return (products ?? []).filter(
      (p) =>
        (!onlyLow || p.stock <= p.minStock) &&
        (!q || p.name.toLowerCase().includes(q) || p.code.toLowerCase().includes(q) || (p.category ?? '').toLowerCase().includes(q)),
    )
  }, [products, query, onlyLow])

  function saved(p: Product, message: string) {
    setProducts((prev) => (prev?.some((x) => x.id === p.id) ? prev.map((x) => (x.id === p.id ? p : x)) : [p, ...(prev ?? [])]))
    setSelected(p)
    toast.success(message)
  }

  if (error) return <p className="error-text" role="alert">{error}</p>

  return (
    <>
      <div className="page-head">
        <div>
          <h1>Productos</h1>
          <p>{products ? `${products.length} en catálogo` : 'Cargando…'}</p>
        </div>
        {editable && (
          <button className="btn btn-primary" onClick={() => setSelected('new')}>
            Nuevo producto
          </button>
        )}
      </div>

      <div className="toolbar">
        <input
          className="input"
          type="search"
          placeholder="Buscar por nombre, código o categoría"
          aria-label="Buscar productos"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
        <label className="check">
          <input type="checkbox" checked={onlyLow} onChange={(e) => setOnlyLow(e.target.checked)} />
          Solo stock bajo
        </label>
      </div>

      {!products ? (
        <div className="skeleton" style={{ height: 280 }} aria-label="Cargando" />
      ) : visible.length === 0 ? (
        <div className="empty">
          <h3>{products.length === 0 ? 'Aún no hay productos' : 'Sin resultados'}</h3>
          <p>
            {products.length === 0
              ? 'Crea el primero para empezar a controlar el stock.'
              : 'Prueba con otro nombre o quita el filtro de stock bajo.'}
          </p>
        </div>
      ) : (
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Producto</th>
                <th className="hide-sm">Categoría</th>
                <th className="num">Precio</th>
                <th className="num">Stock</th>
              </tr>
            </thead>
            <tbody>
              {visible.map((p) => {
                const low = p.stock <= p.minStock
                return (
                  <tr
                    key={p.id}
                    className={p.active === false ? 'inactive' : ''}
                    tabIndex={0}
                    onClick={() => setSelected(p)}
                    onKeyDown={(e) => e.key === 'Enter' && setSelected(p)}
                  >
                    <td>
                      <div className="cell-name">
                        <strong>{p.name}</strong>
                        <span>{p.code}{p.active === false ? ' · inactivo' : ''}</span>
                      </div>
                    </td>
                    <td className="hide-sm">{p.category ?? '—'}</td>
                    <td className="num">{money.format(p.price)}</td>
                    <td className="num" data-label="Stock">
                      <span className={low ? 'pill pill-warn' : 'pill pill-ok'}>{p.stock}</span>
                    </td>
                  </tr>
                )
              })}
            </tbody>
          </table>
        </div>
      )}

      {selected && (
        <ProductPanel
          product={selected === 'new' ? null : selected}
          editable={editable}
          onClose={() => setSelected(null)}
          onSaved={saved}
        />
      )}
    </>
  )
}
