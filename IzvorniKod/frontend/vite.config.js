import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import fs from 'fs';
// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    https: {
      key: fs.readFileSync('./certs/frontend-key.pem'),
      cert: fs.readFileSync('./certs/frontend-cert.pem')
    },
    proxy: {
      "/api/": {
        target: "http://localhost:8080",
        changeOrigin: true
      },
    },
  }
})
