// tailwind.config.js (en la raíz)
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    // ESTA LÍNEA ES CRUCIAL PARA ENCONTRAR LAS CLASES EN LOS COMPONENTES REACT
    "./src/**/*.{js,ts,jsx,tsx}", 
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
