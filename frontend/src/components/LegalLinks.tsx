import { Link } from 'react-router-dom'

export default function LegalLinks() {
  return (
    <nav className="legal-links" aria-label="Legal">
      <Link to="/privacidad">Política de privacidad</Link>
      <Link to="/cookies">Cookies y almacenamiento</Link>
    </nav>
  )
}
