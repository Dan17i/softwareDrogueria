export type Role = 'ADMIN' | 'MANAGER' | 'SALES' | 'WAREHOUSE' | 'USER'

export interface Session {
  token: string
  username: string
  role: Role
}

export interface Product {
  id: number
  code: string
  name: string
  description: string | null
  price: number
  stock: number
  minStock: number
  category: string | null
  active: boolean
}

export type ProductInput = Omit<Product, 'id' | 'active'> & { active?: boolean }

const KEY = 'bellavista.session'

export function loadSession(): Session | null {
  try {
    const raw = localStorage.getItem(KEY)
    return raw ? (JSON.parse(raw) as Session) : null
  } catch {
    return null
  }
}

export function saveSession(s: Session | null) {
  try {
    if (s) localStorage.setItem(KEY, JSON.stringify(s))
    else localStorage.removeItem(KEY)
  } catch {
    /* almacenamiento bloqueado: la sesión vive solo en memoria */
  }
}

export class ApiError extends Error {
  status: number
  constructor(status: number, message: string) {
    super(message)
    this.status = status
  }
}

let onUnauthorized: () => void = () => {}
export function setUnauthorizedHandler(fn: () => void) {
  onUnauthorized = fn
}

async function request<T>(path: string, init: RequestInit = {}, auth = true): Promise<T> {
  const headers = new Headers(init.headers)
  if (init.body) headers.set('Content-Type', 'application/json')
  const session = loadSession()
  if (auth && session) headers.set('Authorization', `Bearer ${session.token}`)

  let res: Response
  try {
    res = await fetch(`/api${path}`, { ...init, headers })
  } catch {
    throw new ApiError(0, 'No hay conexión con el servidor.')
  }

  if (res.status === 401 && auth) {
    onUnauthorized()
    throw new ApiError(401, 'La sesión expiró. Ingresa de nuevo.')
  }
  if (!res.ok) {
    let msg = 'No se pudo completar la acción.'
    try {
      const body = await res.json()
      msg = body.message ?? body.error ?? msg
    } catch {
      /* respuesta sin cuerpo JSON */
    }
    if (res.status === 403) msg = 'Tu rol no tiene permiso para esta acción.'
    throw new ApiError(res.status, msg)
  }
  return res.status === 204 ? (undefined as T) : ((await res.json().catch(() => undefined)) as T)
}

export const api = {
  login: (username: string, password: string) =>
    request<{ token: string; username: string; role: Role }>(
      '/auth/login',
      { method: 'POST', body: JSON.stringify({ username, password }) },
      false,
    ),
  forgotPassword: (email: string) =>
    request<void>('/auth/forgot-password', { method: 'POST', body: JSON.stringify({ email }) }, false),
  resetPassword: (token: string, newPassword: string) =>
    request<void>('/auth/reset-password', { method: 'POST', body: JSON.stringify({ token, newPassword }) }, false),
  logout: () => request<void>('/auth/logout', { method: 'POST' }).catch(() => undefined),
  products: () => request<Product[]>('/products'),
  restockNeeded: () => request<Product[]>('/products/restock-needed'),
  createProduct: (p: ProductInput) =>
    request<Product>('/products', { method: 'POST', body: JSON.stringify(p) }),
  updateProduct: (id: number, p: ProductInput & { active: boolean }) =>
    request<Product>(`/products/${id}`, { method: 'PUT', body: JSON.stringify(p) }),
  adjustStock: (id: number, kind: 'increase' | 'reduce', quantity: number) =>
    request<Product>(`/products/${id}/${kind}-stock`, {
      method: 'POST',
      body: JSON.stringify({ quantity }),
    }),
  toggleProduct: (id: number) => request<Product>(`/products/${id}/toggle-status`, { method: 'PATCH' }),
}

export const canEditInventory = (role: Role) => role === 'ADMIN' || role === 'WAREHOUSE'
