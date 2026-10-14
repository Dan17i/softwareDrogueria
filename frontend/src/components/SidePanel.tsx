import { useEffect, useRef, type ReactNode } from 'react'

const FOCUSABLE = 'a[href], button:not([disabled]), input:not([disabled]), textarea:not([disabled]), select:not([disabled])'

interface Props {
  title: string
  onClose: () => void
  children: ReactNode
}

// Panel lateral accesible: el foco entra al abrir (a [data-autofocus] o al primer campo),
// queda atrapado mientras está abierto, Escape cierra y el foco vuelve al disparador.
export default function SidePanel({ title, onClose, children }: Props) {
  const ref = useRef<HTMLDivElement>(null)
  const closeRef = useRef(onClose)
  closeRef.current = onClose

  useEffect(() => {
    const opener = document.activeElement as HTMLElement | null
    const panel = ref.current
    const target =
      panel?.querySelector<HTMLElement>('[data-autofocus]') ??
      panel?.querySelector<HTMLElement>('input:not([disabled]), textarea:not([disabled]), select:not([disabled])')
    target?.focus()

    const onKey = (e: KeyboardEvent) => {
      if (e.key === 'Escape') return closeRef.current()
      if (e.key !== 'Tab' || !panel) return
      const items = Array.from(panel.querySelectorAll<HTMLElement>(FOCUSABLE))
      if (items.length === 0) return
      const first = items[0]
      const last = items[items.length - 1]
      if (e.shiftKey && document.activeElement === first) {
        e.preventDefault()
        last.focus()
      } else if (!e.shiftKey && document.activeElement === last) {
        e.preventDefault()
        first.focus()
      }
    }
    window.addEventListener('keydown', onKey)
    return () => {
      window.removeEventListener('keydown', onKey)
      opener?.focus?.()
    }
  }, [])

  return (
    <>
      <div className="scrim" onClick={onClose} />
      <div ref={ref} className="panel" role="dialog" aria-modal="true" aria-label={title}>
        <div className="panel-head">
          <h2>{title}</h2>
          <button className="btn btn-ghost" onClick={onClose}>
            Cerrar
          </button>
        </div>
        {children}
      </div>
    </>
  )
}
