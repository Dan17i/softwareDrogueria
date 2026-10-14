import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// El backend sirve bajo context-path /api (puerto 8080); el proxy evita CORS en dev.
export default defineConfig({
  plugins: [react()],
  server: { proxy: { '/api': 'http://localhost:8080' } },
})
