import { useState, type FormEvent } from 'react'
import { toast } from 'sonner'
import { api, type Supplier } from '../api'
import SidePanel from './SidePanel'

interface Props {
  supplier: Supplier | null
  onClose: () => void
  onSaved: (s: Supplier) => void
}

interface Form {
  code: string
  name: string
  email: string
  phone: string
  city: string
  address: string
  leadTimeDays: string
  active: boolean
}

const toForm = (s: Supplier | null): Form => ({
  code: s?.code ?? '',
  name: s?.name ?? '',
  email: s?.email ?? '',
  phone: s?.phone ?? '',
  city: s?.city ?? '',
  address: s?.address ?? '',
  leadTimeDays: s?.leadTimeDays != null ? String(s.leadTimeDays) : '',
  active: s?.active ?? true,
})

export default function SupplierPanel({ supplier, onClose, onSaved }: Props) {
  const [form, setForm] = useState<Form>(() => toForm(supplier))
  const [busy, setBusy] = useState(false)
  const set = (k: keyof Form) => (e: { target: { value: string } }) => setForm((f) => ({ ...f, [k]: e.target.value }))

  async function submit(e: FormEvent) {
    e.preventDefault()
    setBusy(true)
    const body = {
      code: form.code.trim(),
      name: form.name.trim(),
      email: form.email.trim() || null,
      phone: form.phone.trim() || null,
      city: form.city.trim() || null,
      address: form.address.trim() || null,
      leadTimeDays: form.leadTimeDays ? Number(form.leadTimeDays) : null,
      averagePaymentDelay: supplier?.averagePaymentDelay ?? null,
    }
    try {
      const saved = supplier ? await api.updateSupplier(supplier.id, { ...body, active: form.active }) : await api.createSupplier(body)
      toast.success(supplier ? 'Proveedor actualizado' : 'Proveedor creado')
      onSaved(saved)
    } catch (err) {
      toast.error((err as Error).message)
    } finally {
      setBusy(false)
    }
  }

  return (
    <SidePanel title={supplier ? supplier.name : 'Nuevo proveedor'} onClose={onClose}>
      <form onSubmit={submit}>
        <div className="row-2">
          <div className="field">
            <label htmlFor="s-code">Código</label>
            <input id="s-code" className="input" value={form.code} onChange={set('code')} required maxLength={50} />
          </div>
          <div className="field">
            <label htmlFor="s-lead">Días de entrega</label>
            <input id="s-lead" className="input" type="number" inputMode="numeric" min="0" step="1" value={form.leadTimeDays} onChange={set('leadTimeDays')} aria-describedby="s-lead-hint" />
            <span id="s-lead-hint" className="hint">Cuánto tarda en surtir un pedido.</span>
          </div>
        </div>
        <div className="field">
          <label htmlFor="s-name">Nombre</label>
          <input id="s-name" className="input" value={form.name} onChange={set('name')} required minLength={3} maxLength={100} />
          <span className="hint">Mínimo 3 caracteres.</span>
        </div>
        <div className="row-2">
          <div className="field">
            <label htmlFor="s-email">Correo</label>
            <input id="s-email" className="input" type="email" value={form.email} onChange={set('email')} />
          </div>
          <div className="field">
            <label htmlFor="s-phone">Teléfono</label>
            <input id="s-phone" className="input" type="tel" value={form.phone} onChange={set('phone')} maxLength={20} />
          </div>
        </div>
        <div className="row-2">
          <div className="field">
            <label htmlFor="s-city">Ciudad</label>
            <input id="s-city" className="input" value={form.city} onChange={set('city')} maxLength={100} />
          </div>
          <div className="field">
            <label htmlFor="s-address">Dirección</label>
            <input id="s-address" className="input" value={form.address} onChange={set('address')} maxLength={255} />
          </div>
        </div>
        {supplier && (
          <label className="check">
            <input type="checkbox" checked={form.active} onChange={(e) => setForm((f) => ({ ...f, active: e.target.checked }))} />
            Proveedor activo
          </label>
        )}
        <div className="form-actions">
          <button type="button" className="btn btn-ghost" onClick={onClose}>
            Cancelar
          </button>
          <button className="btn btn-primary" disabled={busy}>
            {supplier ? 'Guardar cambios' : 'Crear proveedor'}
          </button>
        </div>
      </form>
    </SidePanel>
  )
}
