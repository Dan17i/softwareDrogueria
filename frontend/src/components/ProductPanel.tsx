import { useEffect, useState, type FormEvent } from 'react'
import { toast } from 'sonner'
import { api, type Product } from '../api'

interface Props {
  product: Product | null
  editable: boolean
  onClose: () => void
  onSaved: (p: Product, message: string) => void
}

interface Form {
  code: string
  name: string
  description: string
  price: string
  stock: string
  minStock: string
  category: string
  active: boolean
}

const toForm = (p: Product | null): Form => ({
  code: p?.code ?? '',
  name: p?.name ?? '',
  description: p?.description ?? '',
  price: p ? String(p.price) : '',
  stock: p ? String(p.stock) : '0',
  minStock: p ? String(p.minStock) : '0',
  category: p?.category ?? '',
  active: p?.active ?? true,
})

export default function ProductPanel({ product, editable, onClose, onSaved }: Props) {
  const [form, setForm] = useState<Form>(() => toForm(product))
  const [qty, setQty] = useState('1')
  const [busy, setBusy] = useState(false)

  useEffect(() => setForm(toForm(product)), [product])
  useEffect(() => {
    const onKey = (e: KeyboardEvent) => e.key === 'Escape' && onClose()
    window.addEventListener('keydown', onKey)
    return () => window.removeEventListener('keydown', onKey)
  }, [onClose])

  const set = (k: keyof Form) => (e: { target: { value: string } }) => setForm((f) => ({ ...f, [k]: e.target.value }))

  async function run<T>(fn: () => Promise<T>, onOk: (r: T) => void) {
    setBusy(true)
    try {
      onOk(await fn())
    } catch (err) {
      toast.error((err as Error).message)
    } finally {
      setBusy(false)
    }
  }

  function submit(e: FormEvent) {
    e.preventDefault()
    const body = {
      code: form.code.trim(),
      name: form.name.trim(),
      description: form.description.trim() || null,
      price: Number(form.price),
      stock: Number(form.stock),
      minStock: Number(form.minStock),
      category: form.category.trim() || null,
    }
    if (product) {
      void run(
        () => api.updateProduct(product.id, { ...body, active: form.active }),
        (p) => onSaved(p, 'Producto actualizado'),
      )
    } else {
      void run(() => api.createProduct(body), (p) => onSaved(p, 'Producto creado'))
    }
  }

  function adjust(kind: 'increase' | 'reduce') {
    if (!product) return
    const quantity = Number(qty)
    void run(
      () => api.adjustStock(product.id, kind, quantity),
      (p) => onSaved(p, kind === 'increase' ? `Entraron ${quantity} unidades` : `Salieron ${quantity} unidades`),
    )
  }

  const dis = !editable || busy

  return (
    <>
      <div className="scrim" onClick={onClose} />
      <aside className="panel" role="dialog" aria-modal="true" aria-label={product ? product.name : 'Nuevo producto'}>
        <div className="panel-head">
          <h2>{product ? product.name : 'Nuevo producto'}</h2>
          <button className="btn btn-ghost" onClick={onClose} aria-label="Cerrar">
            Cerrar
          </button>
        </div>

        {!editable && <p className="hint">Tu rol puede consultar, pero no modificar el inventario.</p>}

        <form onSubmit={submit}>
          <div className="row-2">
            <div className="field">
              <label htmlFor="code">Código</label>
              <input id="code" className="input" value={form.code} onChange={set('code')} required maxLength={50} disabled={dis} />
            </div>
            <div className="field">
              <label htmlFor="category">Categoría</label>
              <input id="category" className="input" value={form.category} onChange={set('category')} maxLength={100} disabled={dis} />
            </div>
          </div>
          <div className="field">
            <label htmlFor="name">Nombre</label>
            <input id="name" className="input" value={form.name} onChange={set('name')} required maxLength={200} disabled={dis} />
          </div>
          <div className="field">
            <label htmlFor="description">Descripción</label>
            <textarea id="description" className="input" value={form.description} onChange={set('description')} maxLength={500} disabled={dis} />
          </div>
          <div className="row-2">
            <div className="field">
              <label htmlFor="price">Precio</label>
              <input id="price" className="input" type="number" inputMode="decimal" min="0.01" step="0.01" value={form.price} onChange={set('price')} required disabled={dis} />
            </div>
            <div className="field">
              <label htmlFor="minStock">Stock mínimo</label>
              <input id="minStock" className="input" type="number" inputMode="numeric" min="0" step="1" value={form.minStock} onChange={set('minStock')} required disabled={dis} />
            </div>
          </div>
          {!product && (
            <div className="field">
              <label htmlFor="stock">Stock inicial</label>
              <input id="stock" className="input" type="number" inputMode="numeric" min="0" step="1" value={form.stock} onChange={set('stock')} required disabled={dis} />
            </div>
          )}
          {product && (
            <label className="check">
              <input type="checkbox" checked={form.active} onChange={(e) => setForm((f) => ({ ...f, active: e.target.checked }))} disabled={dis} />
              Producto activo
            </label>
          )}
          {editable && (
            <div className="form-actions">
              <button type="button" className="btn btn-ghost" onClick={onClose}>
                Cancelar
              </button>
              <button className="btn btn-primary" disabled={busy}>
                {product ? 'Guardar cambios' : 'Crear producto'}
              </button>
            </div>
          )}
        </form>

        {product && editable && (
          <section className="stock-adjust" aria-label="Ajustar stock">
            <h3>
              Stock actual: {product.stock}
            </h3>
            <div className="actions">
              <input className="input" type="number" inputMode="numeric" min="1" step="1" value={qty} onChange={(e) => setQty(e.target.value)} aria-label="Cantidad" />
              <button className="btn" disabled={busy || Number(qty) < 1} onClick={() => adjust('increase')}>
                Entrada
              </button>
              <button className="btn" disabled={busy || Number(qty) < 1 || Number(qty) > product.stock} onClick={() => adjust('reduce')}>
                Salida
              </button>
            </div>
          </section>
        )}
      </aside>
    </>
  )
}
