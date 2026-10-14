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

export type OrderStatus = 'PENDING' | 'COMPLETED' | 'CANCELLED'

export interface OrderItem {
  id: number
  productId: number
  productCode: string
  productName: string
  unitPrice: number
  quantity: number
  subtotal: number
}

export interface Order {
  id: number
  orderNumber: string
  customerId: number
  customerCode: string
  customerName: string
  status: OrderStatus
  total: number
  items: OrderItem[]
  notes: string | null
  orderDate: string
}

export interface OrderInput {
  customerId: number
  items: { productId: number; quantity: number }[]
  notes: string | null
}

export type CustomerType = 'MINORISTA' | 'MAYORISTA'

export interface Customer {
  id: number
  code: string
  name: string
  email: string | null
  phone: string | null
  address: string | null
  city: string | null
  customerType: CustomerType
  creditLimit: number | null
  pendingBalance?: number
  active: boolean
}

export type CustomerInput = Omit<Customer, 'id' | 'active'> & { active?: boolean }

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
  orders: () => request<Order[]>('/orders'),
  createOrder: (o: OrderInput) => request<Order>('/orders', { method: 'POST', body: JSON.stringify(o) }),
  completeOrder: (id: number) => request<Order>(`/orders/${id}/complete`, { method: 'PATCH' }),
  cancelOrder: (id: number) => request<Order>(`/orders/${id}/cancel`, { method: 'PATCH' }),
  customers: () => request<Customer[]>('/customers'),
  createCustomer: (c: CustomerInput) =>
    request<Customer>('/customers', { method: 'POST', body: JSON.stringify(c) }),
  updateCustomer: (id: number, c: CustomerInput & { active: boolean }) =>
    request<Customer>(`/customers/${id}`, { method: 'PUT', body: JSON.stringify(c) }),
  toggleProduct: (id: number) => request<Product>(`/products/${id}/toggle-status`, { method: 'PATCH' }),
}

export const canEditInventory = (role: Role) => role === 'ADMIN' || role === 'WAREHOUSE'

export const canReadOrders = (role: Role) => role !== 'USER'
export const canWriteOrders = (role: Role) => role === 'ADMIN' || role === 'MANAGER' || role === 'SALES'
// Clientes: mismos roles que escriben órdenes (el backend los restringe igual)
export const canManageCustomers = canWriteOrders
