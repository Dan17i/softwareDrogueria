import NumberFlow from '@number-flow/react'
import { useCallback, useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api, canEditInventory, type Product } from '../api'
import { useAuth } from '../auth'
import ProductPanel from '../components/ProductPanel'
import StockStatus from '../components/StockStatus'

export default function Dashboard() {
  const { session } = useAuth()
  const editable = session ? canEditInventory(session.role) : false
  const [products, setProducts] = useState<Product[] | null>(null)
  const [restock, setRestock] = useState<Product[]>([])
  const [error, setError] = useState('')
  const [selected, setSelected] = useState<Product | null>(null)

  const load = useCallback(() => {
    Promise.all([api.products(), api.restockNeeded()])
      .then(([all, low]) => {
        setProducts(all)
        setRestock(low)
      })
      .catch((e: Error) => setError(e.message))
  }, [])
  useEffect(load, [load])

  if (error) return <p className="error-text" role="alert">{error}</p>

  const active = products?.filter((p) => p.active !== false).length ?? 0
  const value = products?.reduce((sum, p) => sum + p.price * p.stock, 0) ?? 0
  const showValue = session?.role === 'ADMIN' || session?.role === 'MANAGER'

  return (
    <>
      <div className="page-head">
        <div>
          <h1>Por reponer</h1>
          <p>
            {products === null
              ? 'Cargando…'
              : restock.length === 0
                ? 'Todo al día.'
                : `${restock.length} ${restock.length === 1 ? 'producto está' : 'productos están'} en o bajo su mínimo.`}
          </p>
        </div>
        <dl className="summary">
          <div>
            <dt>Activos</dt>
            <dd><NumberFlow value={active} /></dd>
          </div>
          {showValue && (
            <div>
              <dt>Valor en stock</dt>
              <dd>
                <NumberFlow value={value} format={{ style: 'currency', currency: 'COP', maximumFractionDigits: 0 }} locales="es-CO" />
              </dd>
            </div>
          )}
        </dl>
      </div>

      {!products ? (
        <div className="skeleton" style={{ height: 160 }} role="status" aria-label="Cargando" />
      ) : restock.length === 0 ? (
        <div className="empty">
          <h3>Todo al día</h3>
          <p>Ningún producto está por debajo de su stock mínimo.</p>
          <Link to="/productos" className="btn">Ver productos</Link>
        </div>
      ) : (
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Producto</th>
                <th className="num">Stock</th>
                <th className="num hide-sm">Mínimo</th>
                {editable && <th className="num hide-sm"><span className="sr-only">Acción</span></th>}
              </tr>
            </thead>
            <tbody className="stagger">
              {restock.map((p) => (
                <tr key={p.id} onClick={() => setSelected(p)}>
                  <td>
                    <div className="cell-name">
                      <button className="row-link" onClick={(e) => { e.stopPropagation(); setSelected(p) }}>{p.name}</button>
                      <span>{p.code} · mínimo {p.minStock}</span>
                    </div>
                  </td>
                  <td className="num"><StockStatus stock={p.stock} minStock={p.minStock} /></td>
                  <td className="num hide-sm">{p.minStock}</td>
                  {editable && (
                    <td className="num hide-sm">
                      <span className="row-action">Registrar entrada</span>
                    </td>
                  )}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {selected && (
        <ProductPanel
          product={selected}
          editable={editable}
          initialFocus="stock"
          onClose={() => setSelected(null)}
          onSaved={() => {
            setSelected(null)
            load()
          }}
        />
      )}
    </>
  )
}
