import NumberFlow from '@number-flow/react'
import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api, type Product } from '../api'

export default function Dashboard() {
  const [products, setProducts] = useState<Product[] | null>(null)
  const [restock, setRestock] = useState<Product[]>([])
  const [error, setError] = useState('')

  useEffect(() => {
    Promise.all([api.products(), api.restockNeeded()])
      .then(([all, low]) => {
        setProducts(all)
        setRestock(low)
      })
      .catch((e: Error) => setError(e.message))
  }, [])

  if (error) return <p className="error-text" role="alert">{error}</p>

  const active = products?.filter((p) => p.active !== false).length ?? 0
  const value = products?.reduce((sum, p) => sum + p.price * p.stock, 0) ?? 0

  return (
    <>
      <div className="page-head">
        <div>
          <h1>Resumen</h1>
          <p>Estado del inventario ahora mismo.</p>
        </div>
      </div>

      <dl className="summary">
        <div>
          <dt>Por reponer</dt>
          <dd className={restock.length ? 'alert' : ''}><NumberFlow value={restock.length} /></dd>
        </div>
        <div>
          <dt>Productos activos</dt>
          <dd><NumberFlow value={active} /></dd>
        </div>
        <div>
          <dt>Valor en stock</dt>
          <dd>
            <NumberFlow value={value} format={{ style: 'currency', currency: 'COP', maximumFractionDigits: 0 }} locales="es-CO" />
          </dd>
        </div>
      </dl>

      <h2 style={{ marginBottom: 'var(--space-md)' }}>Por reponer</h2>
      {!products ? (
        <div className="skeleton" style={{ height: 160 }} aria-label="Cargando" />
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
                <th className="num">Mínimo</th>
              </tr>
            </thead>
            <tbody className="stagger">
              {restock.map((p) => (
                <tr key={p.id}>
                  <td>
                    <div className="cell-name">
                      <strong>{p.name}</strong>
                      <span>{p.code}</span>
                    </div>
                  </td>
                  <td className="num" data-label="Stock"><span className="pill pill-warn">{p.stock}</span></td>
                  <td className="num" data-label="Mínimo">{p.minStock}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </>
  )
}
