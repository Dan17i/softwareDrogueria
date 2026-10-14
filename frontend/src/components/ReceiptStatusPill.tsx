import type { ReceiptStatus } from '../api'

const LABEL: Record<ReceiptStatus, string> = {
  PENDING: 'Pendiente',
  RECEIVED: 'Recibida',
  PARTIALLY_RECEIVED: 'Recibida parcial',
  REJECTED: 'Rechazada',
}
const CLASS: Record<ReceiptStatus, string> = {
  PENDING: 'pill pill-warn',
  RECEIVED: 'pill pill-ok',
  PARTIALLY_RECEIVED: 'pill pill-warn',
  REJECTED: 'pill',
}

export default function ReceiptStatusPill({ status }: { status: ReceiptStatus }) {
  return <span className={CLASS[status] ?? 'pill'}>{LABEL[status] ?? status}</span>
}
