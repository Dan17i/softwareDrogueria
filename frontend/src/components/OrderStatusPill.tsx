import type { OrderStatus } from '../api'

const LABEL: Record<OrderStatus, string> = { PENDING: 'Pendiente', COMPLETED: 'Completada', CANCELLED: 'Cancelada' }
const CLASS: Record<OrderStatus, string> = { PENDING: 'pill pill-warn', COMPLETED: 'pill pill-ok', CANCELLED: 'pill' }

export const statusLabel = (s: OrderStatus) => LABEL[s] ?? s

export default function OrderStatusPill({ status }: { status: OrderStatus }) {
  return <span className={CLASS[status] ?? 'pill'}>{LABEL[status] ?? status}</span>
}
