# Cumplimiento legal y accesibilidad (Colombia)

Revisión 2026-10-15. La app es un sistema INTERNO (login de personal), no una tienda pública.

| Tema | Estado | Detalle |
|---|---|---|
| Privacidad / habeas data (Ley 1581/2012, Dto 1377/2013) | Hecho | `/privacidad` en el front, enlazada en login y pie. **Completar `frontend/src/legal.ts`** (razón social, NIT, dirección, correo) |
| Datos mínimos | Hecho | Cliente: obligatorios código, nombre, cupo; correo/teléfono/ciudad/dirección marcados "opcional". Sin datos sensibles ni de menores |
| Registro de bases de datos (RNBD, SIC) | **Pendiente (negocio)** | Aplica si los activos de la empresa superan 100.000 UVT; verificar con contador/abogado |
| Autorización del titular | **Pendiente (negocio)** | Definir cómo se obtiene (formato físico o aceptación al crear cuenta/cliente) y guardarla |
| Cookies | Hecho | No hay cookies. Solo `localStorage` (`bellavista.session`), estrictamente necesario → **no requiere banner**. `/cookies` lo explica. Si se añade analítica, pedir consentimiento antes |
| Terceros | Hecho | Google Fonts eliminado (fuentes autoalojadas, `@fontsource`). Quedan: AWS SES (correo) y hosting. Stripe aún sin UI |
| CSP | Hecho | `nginx.conf` solo permite recursos propios |
| Reseñas falsas | N/A | No existen reseñas ni testimonios en la app. Si se agregan: Ley 1480/2011 prohíbe publicidad engañosa (SIC) |
| Reembolsos / retracto (Ley 1480/2011 arts. 47 y 51) | N/A hoy | Sin ventas en línea. Si se habilitan pagos Stripe a consumidores: política de retracto (5 días hábiles) y reversión de pago; ojo con excepciones y medicamentos con control sanitario (Invima/Res. 1403/2007) |
| Teclado | Hecho | Enlace "Saltar al contenido", foco visible, paneles con foco atrapado, formularios con `label` |
| Textos alternativos | N/A | No hay `<img>`. Wordmark decorativo `aria-hidden`; iconos con `aria-label`; favicon sin alt |
| Contraste | Hecho (previo) | axe-core 0 violaciones en `/audit` |
