export const money = new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 })

const dateFmt = new Intl.DateTimeFormat('es-CO', { dateStyle: 'medium', timeStyle: 'short' })
export const formatDate = (iso: string | null | undefined) => (iso ? dateFmt.format(new Date(iso)) : '—')
