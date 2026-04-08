# 🎨 Guía de Integración Frontend - Droguería Bellavista

## Información General

Esta guía proporciona toda la información necesaria para integrar un frontend con la API de Droguería Bellavista. La API sigue principios REST y utiliza JWT para autenticación.

## Configuración Base

### URL Base de la API
```javascript
const API_BASE_URL = 'https://drogueria-bellavista-api.onrender.com/api';
```

### Headers Comunes
```javascript
const getAuthHeaders = (token) => ({
  'Content-Type': 'application/json',
  'Authorization': token ? `Bearer ${token}` : undefined
});
```

## Autenticación

### 1. Registro de Usuario
```javascript
const register = async (userData) => {
  try {
    const response = await fetch(`${API_BASE_URL}/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        username: userData.username,
        email: userData.email,
        password: userData.password,
        firstName: userData.firstName,
        lastName: userData.lastName
      })
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Error en registro');
    }

    return await response.json();
  } catch (error) {
    console.error('Error en registro:', error);
    throw error;
  }
};
```

### 2. Inicio de Sesión
```javascript
const login = async (credentials) => {
  try {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        username: credentials.username,
        password: credentials.password
      })
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Credenciales inválidas');
    }

    const data = await response.json();
    // Guardar token en localStorage o contexto
    localStorage.setItem('token', data.token);
    localStorage.setItem('user', JSON.stringify(data.user));

    return data;
  } catch (error) {
    console.error('Error en login:', error);
    throw error;
  }
};
```

### 3. Recuperación de Contraseña
```javascript
const forgotPassword = async (email) => {
  try {
    const response = await fetch(`${API_BASE_URL}/auth/forgot-password`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email })
    });

    // Nota: La API siempre retorna éxito por seguridad
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error en recuperación:', error);
    throw error;
  }
};

const resetPassword = async (token, newPassword) => {
  try {
    const response = await fetch(`${API_BASE_URL}/auth/reset-password`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ token, newPassword })
  });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Error al restablecer contraseña');
    }

    return await response.json();
  } catch (error) {
    console.error('Error al restablecer:', error);
    throw error;
  }
};
```

## Gestión de Productos

### Listar Productos
```javascript
const getProducts = async (filters = {}) => {
  try {
    const params = new URLSearchParams();

    if (filters.active !== undefined) {
      params.append('active', filters.active);
    }

    const response = await fetch(`${API_BASE_URL}/products?${params}`, {
      headers: getAuthHeaders(localStorage.getItem('token'))
    });

    if (!response.ok) {
      throw new Error('Error al obtener productos');
    }

    return await response.json();
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};
```

### Buscar Productos
```javascript
const searchProducts = async (name) => {
  try {
    const response = await fetch(`${API_BASE_URL}/products/search?name=${encodeURIComponent(name)}`, {
      headers: getAuthHeaders(localStorage.getItem('token'))
    });

    if (!response.ok) {
      throw new Error('Error en búsqueda');
    }

    return await response.json();
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};
```

### Crear Producto
```javascript
const createProduct = async (productData) => {
  try {
    const response = await fetch(`${API_BASE_URL}/products`, {
      method: 'POST',
      headers: getAuthHeaders(localStorage.getItem('token')),
      body: JSON.stringify({
        code: productData.code,
        name: productData.name,
        description: productData.description,
        price: productData.price,
        stock: productData.stock,
        minStock: productData.minStock,
        category: productData.category
      })
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Error al crear producto');
    }

    return await response.json();
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};
```

### Gestionar Stock
```javascript
const reduceStock = async (productId, quantity) => {
  try {
    const response = await fetch(`${API_BASE_URL}/products/${productId}/reduce-stock`, {
      method: 'POST',
      headers: getAuthHeaders(localStorage.getItem('token')),
      body: JSON.stringify({ quantity })
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Error al reducir stock');
    }

    return await response.json();
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};

const increaseStock = async (productId, quantity) => {
  try {
    const response = await fetch(`${API_BASE_URL}/products/${productId}/increase-stock`, {
      method: 'POST',
      headers: getAuthHeaders(localStorage.getItem('token')),
      body: JSON.stringify({ quantity })
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Error al aumentar stock');
    }

    return await response.json();
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};
```

## Gestión de Clientes

### Listar Clientes
```javascript
const getCustomers = async () => {
  try {
    const response = await fetch(`${API_BASE_URL}/customers`, {
      headers: getAuthHeaders(localStorage.getItem('token'))
    });

    if (!response.ok) {
      throw new Error('Error al obtener clientes');
    }

    return await response.json();
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};
```

### Crear Cliente
```javascript
const createCustomer = async (customerData) => {
  try {
    const response = await fetch(`${API_BASE_URL}/customers`, {
      method: 'POST',
      headers: getAuthHeaders(localStorage.getItem('token')),
      body: JSON.stringify({
        code: customerData.code,
        name: customerData.name,
        email: customerData.email,
        phone: customerData.phone,
        address: customerData.address,
        city: customerData.city,
        postalCode: customerData.postalCode,
        documentNumber: customerData.documentNumber,
        documentType: customerData.documentType,
        customerType: customerData.customerType,
        creditLimit: customerData.creditLimit
      })
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Error al crear cliente');
    }

    return await response.json();
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};
```

## Gestión de Órdenes

### Crear Orden
```javascript
const createOrder = async (orderData) => {
  try {
    const response = await fetch(`${API_BASE_URL}/orders`, {
      method: 'POST',
      headers: getAuthHeaders(localStorage.getItem('token')),
      body: JSON.stringify({
        customerId: orderData.customerId,
        items: orderData.items.map(item => ({
          productId: item.productId,
          quantity: item.quantity
        })),
        notes: orderData.notes
      })
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Error al crear orden');
    }

    return await response.json();
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};
```

### Listar Órdenes por Cliente
```javascript
const getCustomerOrders = async (customerId, includePending = false) => {
  try {
    let url = `${API_BASE_URL}/orders/customer/${customerId}`;
    if (includePending) {
      url += '/pending';
    }

    const response = await fetch(url, {
      headers: getAuthHeaders(localStorage.getItem('token'))
    });

    if (!response.ok) {
      throw new Error('Error al obtener órdenes');
    }

    return await response.json();
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};
```

## Gestión de Usuarios (Solo ADMIN)

### Listar Usuarios
```javascript
const getUsers = async () => {
  try {
    const response = await fetch(`${API_BASE_URL}/users`, {
      headers: getAuthHeaders(localStorage.getItem('token'))
    });

    if (!response.ok) {
      throw new Error('Error al obtener usuarios');
    }

    return await response.json();
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};
```

### Cambiar Rol de Usuario
```javascript
const changeUserRole = async (userId, newRole) => {
  try {
    const response = await fetch(`${API_BASE_URL}/users/${userId}/role`, {
      method: 'PATCH',
      headers: getAuthHeaders(localStorage.getItem('token')),
      body: JSON.stringify({ role: newRole })
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Error al cambiar rol');
    }

    return await response.json();
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
};
```

## Manejo de Errores

### Interceptor de Errores Global
```javascript
const handleApiError = (error) => {
  if (error.response) {
    // Error de respuesta del servidor
    const status = error.response.status;
    const message = error.response.data?.message || 'Error desconocido';

    switch (status) {
      case 400:
        // Error de validación
        showValidationErrors(message);
        break;
      case 401:
        // Token expirado o inválido
        logout();
        redirectToLogin();
        break;
      case 403:
        // Permisos insuficientes
        showPermissionError();
        break;
      case 404:
        // Recurso no encontrado
        showNotFoundError();
        break;
      case 500:
        // Error del servidor
        showServerError();
        break;
      default:
        showGenericError(message);
    }
  } else if (error.request) {
    // Error de red
    showNetworkError();
  } else {
    // Error desconocido
    showGenericError(error.message);
  }
};
```

## Interceptores de Axios (Recomendado)

### Configuración de Axios
```javascript
import axios from 'axios';

// Crear instancia de axios
const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
});

// Interceptor de request para agregar token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Interceptor de response para manejar errores
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expirado
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;
```

### Uso Simplificado con Axios
```javascript
import api from './api';

// Ejemplos de uso
export const productService = {
  getAll: () => api.get('/products'),
  create: (data) => api.post('/products', data),
  update: (id, data) => api.put(`/products/${id}`, data),
  delete: (id) => api.delete(`/products/${id}`),
  reduceStock: (id, quantity) => api.post(`/products/${id}/reduce-stock`, { quantity }),
  increaseStock: (id, quantity) => api.post(`/products/${id}/increase-stock`, { quantity }),
};

export const orderService = {
  create: (data) => api.post('/orders', data),
  getById: (id) => api.get(`/orders/${id}`),
  getByCustomer: (customerId) => api.get(`/orders/customer/${customerId}`),
  complete: (id) => api.patch(`/orders/${id}/complete`),
  cancel: (id) => api.patch(`/orders/${id}/cancel`),
};
```

## Estados de Carga y UX

### Hook de React para API Calls
```javascript
import { useState, useEffect } from 'react';

const useApi = (apiCall, dependencies = []) => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        setError(null);
        const result = await apiCall();
        setData(result.data);
      } catch (err) {
        setError(err.response?.data?.message || err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, dependencies);

  return { data, loading, error, refetch: () => fetchData() };
};

// Uso
const ProductList = () => {
  const { data: products, loading, error } = useApi(() => api.get('/products'));

  if (loading) return <div>Cargando productos...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div>
      {products.map(product => (
        <div key={product.id}>{product.name}</div>
      ))}
    </div>
  );
};
```

## Validaciones Frontend

### Validación de Formularios
```javascript
const validateProduct = (product) => {
  const errors = {};

  if (!product.code?.trim()) {
    errors.code = 'El código es obligatorio';
  } else if (product.code.length > 50) {
    errors.code = 'El código no puede tener más de 50 caracteres';
  }

  if (!product.name?.trim()) {
    errors.name = 'El nombre es obligatorio';
  } else if (product.name.length > 200) {
    errors.name = 'El nombre no puede tener más de 200 caracteres';
  }

  if (product.price <= 0) {
    errors.price = 'El precio debe ser mayor a 0';
  }

  if (product.stock < 0) {
    errors.stock = 'El stock no puede ser negativo';
  }

  if (product.minStock < 0) {
    errors.minStock = 'El stock mínimo no puede ser negativo';
  }

  return errors;
};

const validateOrder = (order) => {
  const errors = {};

  if (!order.customerId) {
    errors.customerId = 'Debe seleccionar un cliente';
  }

  if (!order.items || order.items.length === 0) {
    errors.items = 'La orden debe tener al menos un producto';
  } else {
    order.items.forEach((item, index) => {
      if (!item.productId) {
        errors[`items.${index}.productId`] = 'Producto requerido';
      }
      if (!item.quantity || item.quantity <= 0) {
        errors[`items.${index}.quantity`] = 'Cantidad debe ser mayor a 0';
      }
    });
  }

  return errors;
};
```

## Manejo de Roles y Permisos

### Verificación de Roles
```javascript
const useAuth = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const userData = localStorage.getItem('user');

    if (token && userData) {
      setUser(JSON.parse(userData));
    }
    setLoading(false);
  }, []);

  const hasRole = (role) => {
    return user?.role === role;
  };

  const hasAnyRole = (roles) => {
    return roles.includes(user?.role);
  };

  const isAdmin = () => hasRole('ADMIN');
  const canManageUsers = () => hasRole('ADMIN');
  const canManageInventory = () => hasAnyRole(['ADMIN', 'WAREHOUSE']);
  const canCreateOrders = () => hasAnyRole(['ADMIN', 'SALES']);

  return {
    user,
    loading,
    hasRole,
    hasAnyRole,
    isAdmin,
    canManageUsers,
    canManageInventory,
    canCreateOrders,
    logout: () => {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      setUser(null);
    }
  };
};

// Uso en componentes
const AdminPanel = () => {
  const { canManageUsers, isAdmin } = useAuth();

  if (!isAdmin()) {
    return <div>No tienes permisos para acceder</div>;
  }

  return (
    <div>
      {canManageUsers() && <UserManagement />}
      <OtherAdminFeatures />
    </div>
  );
};
```

## Paginación y Filtros

### Implementar Paginación
```javascript
const usePagination = (apiEndpoint, itemsPerPage = 20) => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [filters, setFilters] = useState({});

  const fetchData = async () => {
    setLoading(true);
    try {
      const params = new URLSearchParams({
        page: page.toString(),
        size: itemsPerPage.toString(),
        ...filters
      });

      const response = await api.get(`${apiEndpoint}?${params}`);
      setData(response.data.content || response.data);
      setTotalPages(response.data.totalPages || 1);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [page, filters]);

  return {
    data,
    loading,
    page,
    totalPages,
    setPage,
    setFilters,
    refetch: fetchData
  };
};

// Uso
const ProductList = () => {
  const {
    data: products,
    loading,
    page,
    totalPages,
    setPage,
    setFilters
  } = usePagination('/products', 10);

  return (
    <div>
      <input
        type="text"
        placeholder="Buscar productos..."
        onChange={(e) => setFilters({ name: e.target.value })}
      />

      {loading ? (
        <div>Cargando...</div>
      ) : (
        <div>
          {products.map(product => (
            <div key={product.id}>{product.name}</div>
          ))}

          <div>
            <button
              onClick={() => setPage(page - 1)}
              disabled={page === 0}
            >
              Anterior
            </button>
            <span>Página {page + 1} de {totalPages}</span>
            <button
              onClick={() => setPage(page + 1)}
              disabled={page === totalPages - 1}
            >
              Siguiente
            </button>
          </div>
        </div>
      )}
    </div>
  );
};
```

## Consideraciones de Producción

### Variables de Entorno
```javascript
// .env.production
VITE_API_BASE_URL=https://drogueria-bellavista-api.onrender.com/api
VITE_APP_NAME=Droguería Bellavista
VITE_APP_VERSION=1.0.0
```

### Configuración de Build
```javascript
// vite.config.js
export default defineConfig({
  plugins: [react()],
  define: {
    __APP_VERSION__: JSON.stringify(process.env.npm_package_version),
  },
  build: {
    sourcemap: false, // Deshabilitar en producción
    minify: 'terser',
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
});
```

### Error Boundaries
```javascript
class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error) {
    return { hasError: true, error };
  }

  componentDidCatch(error, errorInfo) {
    console.error('Error caught by boundary:', error, errorInfo);
    // Reportar error a servicio de monitoreo
  }

  render() {
    if (this.state.hasError) {
      return (
        <div className="error-boundary">
          <h2>Algo salió mal</h2>
          <p>Por favor, recarga la página o contacta soporte</p>
          <button onClick={() => window.location.reload()}>
            Recargar Página
          </button>
        </div>
      );
    }

    return this.props.children;
  }
}

// Uso
<ErrorBoundary>
  <App />
</ErrorBoundary>
```

## Testing

### Tests de API Calls
```javascript
import { render, screen, waitFor } from '@testing-library/react';
import { rest } from 'msw';
import { setupServer } from 'msw/node';
import ProductList from './ProductList';

// Mock API
const server = setupServer(
  rest.get('/api/products', (req, res, ctx) => {
    return res(ctx.json([
      { id: 1, name: 'Producto 1', price: 100 },
      { id: 2, name: 'Producto 2', price: 200 },
    ]));
  })
);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

test('loads and displays products', async () => {
  render(<ProductList />);

  await waitFor(() => {
    expect(screen.getByText('Producto 1')).toBeInTheDocument();
    expect(screen.getByText('Producto 2')).toBeInTheDocument();
  });
});
```

---

**Última actualización:** Marzo 2026
**Versión:** 1.0
**Framework de ejemplo:** React + TypeScript</content>
<parameter name="filePath">C:\Users\DANIEL-PC\Documents\software 3\softwareDrogueria\docs\FRONTEND_INTEGRATION.md
