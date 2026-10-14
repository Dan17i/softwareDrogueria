import { useCallback, useEffect, useMemo, useRef, useState } from 'react'
import { toast } from 'sonner'
import { api, canEditInventory, type Product } from '../api'
import { useAuth } from '../auth'
import ProductPanel from '../components/ProductPanel'
import StockStatus from '../components/StockStatus'

const money = new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 })

type SortKey = 'name' | 'price' | 'stock'

export default function Products() {
  const { session } = useAuth()
  const editable = session ? canEditInventory(session.role) : false
  const [products, setProducts] = useState<Product[] | null>(null)
  const [error, setError] = useState('')
  const [query, setQuery] = useState('')
  const [onlyLow, setOnlyLow] = useState(false)
  const [sort, setSort] = useState<{ key: SortKey; dir: 1 | -1 } | null>(null)
  const [selected, setSelected] = useState<Product | 'new' | null>(null)
  const searchRef = useRef<HTMLInputElement>(null)

  const load = useCallback(() => {
    api.products().then(setProducts).catch((e: Error) => setError(e.message))
  }, [])
  useEffect(load, [load])

  // "/" lleva al buscador, como en cualquier herramienta de uso diario.
  useEffect(() => {
    const onKey = (e: KeyboardEvent) => {
      const t = e.target as HTMLElement
      if (e.key === '/' && !['INPUT', 'TEXTAREA', 'SELECT'].includes(t.tagName) && !selected) {
        e.preventDefault()
        searchRef.current?.focus()
      }
    }
    window.addEventListener('keydown', onKey)
    return () => window.removeEventListener('keydown', onKey)
  }, [selected])

  const visible = useMemo(() => {
    const q = query.trim().toLowerCase()
    const rows = (products ?? []).filter(
      (p) =>
        (!onlyLow || p.stock <= p.minStock) &&
        (!q || p.name.toLowerCase().includes(q) || p.code.toLowerCase().includes(q) || (p.category ?? '').toLowerCase().includes(q)),
    )
    if (!sort) return rows
    const { key, dir } = sort
    return [...rows].sort((a, b) =>
      key === 'name' ? dir * a.name.localeCompare(b.name, 'es') : dir * (a[key] - b[key]),
    )
  }, [products, query, onlyLow, sort])

  function toggleSort(key: SortKey) {
    setSort((s) => (s?.key === key ? (s.dir === 1 ? { key, dir: -1 } : null) : { key, dir: 1 }))
  }

  const upsert = (p: Product) =>
    setProducts((prev) => (prev?.some((x) => x.id === p.id) ? prev.map((x) => (x.id === p.id ? p : x)) : [p, ...(prev ?? [])]))

  function saved(p: Product, message: string) {
    upsert(p)
    setSelected(p)
    if (message) toast.success(message)
  }

  if (error) return <p className="error-text" role="alert">{error}</p>

  const th = (key: SortKey, label: string, cls = '') => (
    <th className={cls} aria-sort={sort?.key === key ? (sort.dir === 1 ? 'ascending' : 'descending') : 'none'}>
      <button className="sort" onClick={() => toggleSort(key)}>
        {label}
        <span aria-hidden="true">{sort?.key === key ? (sort.dir === 1 ? ' ↑' : ' ↓') : ''}</span>
      </button>
    </th>
  )

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
          ref={searchRef}
          className="input"
          type="search"
          placeholder="Buscar por nombre, código o categoría  ( / )"
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
        <div className="skeleton" style={{ height: 280 }} role="status" aria-label="Cargando" />
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
                {th('name', 'Producto')}
                <th className="hide-sm">Categoría</th>
                {th('price', 'Precio', 'num')}
                {th('stock', 'Stock', 'num')}
              </tr>
            </thead>
            <tbody>
              {visible.map((p) => (
                <tr
                  key={p.id}
                  className={p.active === false ? 'inactive' : ''}
                  onClick={() => setSelected(p)}
                >
                  <td>
                    <div className="cell-name">
                      <button className="row-link" onClick={(e) => { e.stopPropagation(); setSelected(p) }}>{p.name}</button>
                      <span>{p.code}{p.active === false ? ' · inactivo' : ''}</span>
                    </div>
                  </td>
                  <td className="hide-sm">{p.category ?? '—'}</td>
                  <td className="num">{money.format(p.price)}</td>
                  <td className="num" data-label="Stock">
                    <StockStatus stock={p.stock} minStock={p.minStock} />
                  </td>
                </tr>
              ))}
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
          onUndone={upsert}
        />
      )}
    </>
  )
}
