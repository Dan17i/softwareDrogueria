import { lazy, Suspense } from 'react'
import { Navigate, Outlet, Route, Routes } from 'react-router-dom'
import { useAuth } from './auth'
import Layout from './components/Layout'
import Dashboard from './pages/Dashboard'
import Login from './pages/Login'
import Customers from './pages/Customers'
import Orders from './pages/Orders'
import Products from './pages/Products'

const Recover = lazy(() => import('./pages/Recover').then((m) => ({ default: m.Recover })))
const ResetPassword = lazy(() => import('./pages/Recover').then((m) => ({ default: m.ResetPassword })))

function Protected() {
  const { session } = useAuth()
  return session ? <Layout><Outlet /></Layout> : <Navigate to="/login" replace />
}

export default function App() {
  const { session } = useAuth()
  return (
    <Suspense fallback={null}>
    <Routes>
      <Route path="/login" element={session ? <Navigate to="/" replace /> : <Login />} />
      <Route path="/recuperar" element={<Recover />} />
      <Route path="/reset-password" element={<ResetPassword />} />
      <Route element={<Protected />}>
        <Route index element={<Dashboard />} />
        <Route path="productos" element={<Products />} />
        <Route path="ordenes" element={<Orders />} />
        <Route path="clientes" element={<Customers />} />
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
    </Suspense>
  )
}
