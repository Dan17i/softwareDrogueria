import { useState, type FormEvent } from 'react'
import { toast } from 'sonner'
import { api, type Customer, type CustomerType } from '../api'
import SidePanel from './SidePanel'

interface Props {
  customer: Customer | null
  onClose: () => void
  onSaved: (c: Customer) => void
}

interface Form {
  code: string
  name: string
  customerType: CustomerType
  email: string
  phone: string
  city: string
  address: string
  creditLimit: string
  active: boolean
}

const toForm = (c: Customer | null): Form => ({
  code: c?.code ?? '',
  name: c?.name ?? '',
  customerType: c?.customerType ?? 'MINORISTA',
  email: c?.email ?? '',
  phone: c?.phone ?? '',
  city: c?.city ?? '',
  address: c?.address ?? '',
  creditLimit: c?.creditLimit ? String(c.creditLimit) : '',
  active: c?.active ?? true,
})

export default function CustomerPanel({ customer, onClose, onSaved }: Props) {
  const [form, setForm] = useState<Form>(() => toForm(customer))
  const [busy, setBusy] = useState(false)
  const set = (k: keyof Form) => (e: { target: { value: string } }) => setForm((f) => ({ ...f, [k]: e.target.value }))

  async function submit(e: FormEvent) {
    e.preventDefault()
    setBusy(true)
    const body = {
      code: form.code.trim(),
      name: form.name.trim(),
      customerType: form.customerType,
      email: form.email.trim() || null,
      phone: form.phone.trim() || null,
      city: form.city.trim() || null,
      address: form.address.trim() || null,
      creditLimit: form.creditLimit ? Number(form.creditLimit) : null,
    }
    try {
      const saved = customer ? await api.updateCustomer(customer.id, { ...body, active: form.active }) : await api.createCustomer(body)
      toast.success(customer ? 'Cliente actualizado' : 'Cliente creado')
      onSaved(saved)
    } catch (err) {
      toast.error((err as Error).message)
    } finally {
      setBusy(false)
    }
  }

  return (
    <SidePanel title={customer ? customer.name : 'Nuevo cliente'} onClose={onClose}>
      <form onSubmit={submit}>
        <div className="row-2">
          <div className="field">
            <label htmlFor="c-code">Código</label>
            <input id="c-code" className="input" value={form.code} onChange={set('code')} required maxLength={50} />
          </div>
          <div className="field">
            <label htmlFor="c-type">Tipo</label>
            <select id="c-type" className="input" value={form.customerType} onChange={set('customerType')}>
              <option value="MINORISTA">Minorista</option>
              <option value="MAYORISTA">Mayorista</option>
            </select>
          </div>
        </div>
        <div className="field">
          <label htmlFor="c-name">Nombre</label>
          <input id="c-name" className="input" value={form.name} onChange={set('name')} required minLength={3} maxLength={100} />
          <span className="hint">Mínimo 3 caracteres.</span>
        </div>
        <div className="row-2">
          <div className="field">
            <label htmlFor="c-email">Correo</label>
            <input id="c-email" className="input" type="email" value={form.email} onChange={set('email')} />
          </div>
          <div className="field">
            <label htmlFor="c-phone">Teléfono</label>
            <input id="c-phone" className="input" type="tel" value={form.phone} onChange={set('phone')} maxLength={20} />
          </div>
        </div>
        <div className="row-2">
          <div className="field">
            <label htmlFor="c-city">Ciudad</label>
            <input id="c-city" className="input" value={form.city} onChange={set('city')} maxLength={100} />
          </div>
          <div className="field">
            <label htmlFor="c-credit">Cupo de crédito</label>
            <input id="c-credit" className="input" type="number" inputMode="decimal" min="0.01" step="0.01" value={form.creditLimit} onChange={set('creditLimit')} required aria-describedby="c-credit-hint" />
            <span id="c-credit-hint" className="hint">Sin cupo no se le pueden registrar órdenes.</span>
          </div>
        </div>
        <div className="field">
          <label htmlFor="c-address">Dirección</label>
          <input id="c-address" className="input" value={form.address} onChange={set('address')} maxLength={255} />
        </div>
        {customer && (
          <label className="check">
            <input type="checkbox" checked={form.active} onChange={(e) => setForm((f) => ({ ...f, active: e.target.checked }))} />
            Cliente activo
          </label>
        )}
        <div className="form-actions">
          <button type="button" className="btn btn-ghost" onClick={onClose}>
            Cancelar
          </button>
          <button className="btn btn-primary" disabled={busy}>
            {customer ? 'Guardar cambios' : 'Crear cliente'}
          </button>
        </div>
      </form>
    </SidePanel>
  )
}
