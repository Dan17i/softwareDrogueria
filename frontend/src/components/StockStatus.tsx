// El estado se dice con texto, no solo con color (daltonismo, impresión en B/N).
export default function StockStatus({ stock, minStock }: { stock: number; minStock: number }) {
  if (stock <= 0) return <span className="pill pill-danger">Agotado</span>
  if (stock <= minStock) return <span className="pill pill-warn">{stock} · Bajo</span>
  return <span className="pill pill-ok">{stock}</span>
}
