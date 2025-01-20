import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/api/": {
        target: "http://localhost:8080",
        changeOrigin: true,
      },
      "/socket.io/": {
        target: "http://localhost:8085",
        changeOrigin: true,
        ws: true,
        configure: (proxy, _options) => {
          proxy.on('error', (err, _req, _res) => {
            console.log('proxy error', err);
          });
          proxy.on("proxyReq", (proxyReq, req, _res) => {
            console.log(
              "Send request:",
              req.method,
              req.url,
              " -> ",
              proxyReq.method,
              proxyReq.host,
              proxyReq.path,
              "with cookie header:",
              JSON.stringify(proxyReq.getHeader("cookie")),
            );
          });
          proxy.on('proxyRes', (proxyRes, req, _res) => {
            console.log('Received:', proxyRes.statusCode, req.url);
          });
        }
      },
    },
  }
})
