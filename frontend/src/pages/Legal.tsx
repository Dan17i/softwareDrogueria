import { Link } from 'react-router-dom'
import { LEGAL } from '../legal'

function Page({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <main className="legal" id="contenido">
      <p><Link to="/" className="text-link">← Volver</Link></p>
      <h1>{title}</h1>
      <p className="updated">Última actualización: {LEGAL.actualizado}</p>
      {children}
    </main>
  )
}

export function Privacy() {
  return (
    <Page title="Política de tratamiento de datos personales">
      <p>
        Esta política se rige por la Ley 1581 de 2012, el Decreto 1377 de 2013 (compilado en el Decreto 1074 de 2015)
        y demás normas de protección de datos de Colombia (habeas data, art. 15 de la Constitución).
      </p>

      <h2>Responsable del tratamiento</h2>
      <p>
        {LEGAL.razonSocial}, NIT {LEGAL.nit}. Domicilio: {LEGAL.direccion}. Contacto para consultas y reclamos: {LEGAL.correo}.
      </p>

      <h2>Qué datos tratamos y para qué</h2>
      <p>Solo recolectamos lo necesario para operar el inventario de la droguería:</p>
      <table>
        <thead><tr><th>Titular</th><th>Datos</th><th>Finalidad</th></tr></thead>
        <tbody>
          <tr>
            <td>Personal con cuenta</td>
            <td>Usuario, correo, contraseña (cifrada), rol, IP en los intentos de acceso</td>
            <td>Autenticar, controlar permisos, seguridad y recuperación de clave</td>
          </tr>
          <tr>
            <td>Clientes</td>
            <td>Código, nombre, tipo, cupo de crédito; opcionales: correo, teléfono, ciudad, dirección</td>
            <td>Registrar órdenes y cupo de crédito</td>
          </tr>
          <tr>
            <td>Proveedores</td>
            <td>Datos de contacto comerciales</td>
            <td>Órdenes y recepción de mercancía</td>
          </tr>
        </tbody>
      </table>
      <p>
        No tratamos datos sensibles (salud, biométricos, etc.) ni datos de menores. No registramos historias clínicas ni
        fórmulas médicas. No usamos los datos para publicidad ni perfilamiento.
      </p>

      <h2>Autorización</h2>
      <p>
        El tratamiento requiere autorización previa, expresa e informada del titular, obtenida al momento de crear su
        cuenta o registro como cliente o proveedor. Puede solicitar copia de ella.
      </p>

      <h2>Terceros que intervienen</h2>
      <ul>
        <li>Amazon Web Services (SES): envío del correo de recuperación de contraseña; puede implicar transferencia a Estados Unidos.</li>
        <li>Proveedor de alojamiento del servidor y la base de datos.</li>
        <li>Stripe (pagos): aún no está activo en esta aplicación; si se habilita, se actualizará esta política.</li>
      </ul>
      <p>
        Las fuentes tipográficas se sirven desde nuestro propio servidor: no se envía su IP a Google. No usamos
        analítica, publicidad ni redes sociales.
      </p>

      <h2>Sus derechos (art. 8 Ley 1581)</h2>
      <ul>
        <li>Conocer, actualizar y rectificar sus datos.</li>
        <li>Solicitar prueba de la autorización y ser informado del uso que se les da.</li>
        <li>Presentar quejas ante la Superintendencia de Industria y Comercio (SIC).</li>
        <li>Revocar la autorización y/o solicitar la supresión, salvo deber legal o contractual de conservarlos.</li>
        <li>Acceder gratuitamente a sus datos.</li>
      </ul>
      <p>
        Las consultas se responden en máximo 10 días hábiles y los reclamos en máximo 15 días hábiles (arts. 14 y 15).
        Escriba a {LEGAL.correo}.
      </p>

      <h2>Seguridad y conservación</h2>
      <p>
        Contraseñas cifradas, comunicación por HTTPS en producción, acceso por roles y límite de intentos de ingreso.
        Conservamos los datos mientras exista la relación comercial y el tiempo que exijan las normas contables y tributarias.
      </p>

      <h2>Cambios</h2>
      <p>Publicaremos aquí cualquier cambio sustancial de esta política.</p>
    </Page>
  )
}

export function Cookies() {
  return (
    <Page title="Cookies y almacenamiento local">
      <p>
        Esta aplicación <strong>no usa cookies</strong> de seguimiento, analítica ni publicidad, y no carga recursos de terceros.
      </p>
      <table>
        <thead><tr><th>Elemento</th><th>Uso</th><th>Duración</th></tr></thead>
        <tbody>
          <tr>
            <td>localStorage <code>bellavista.session</code></td>
            <td>Mantener su sesión iniciada (token, usuario y rol)</td>
            <td>Hasta que pulse «Salir» o el token expire</td>
          </tr>
        </tbody>
      </table>
      <p>
        Es un almacenamiento estrictamente necesario para que el servicio funcione, por lo que no requiere un banner de
        consentimiento. Si más adelante se agregan cookies no esenciales (analítica, por ejemplo), se pedirá su
        consentimiento antes de activarlas.
      </p>
      <p>Puede borrarlo en cualquier momento desde la configuración de su navegador o cerrando sesión.</p>
      <p>Más información en la <Link to="/privacidad" className="text-link">política de privacidad</Link>.</p>
    </Page>
  )
}
