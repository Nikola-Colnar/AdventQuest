import { useState } from 'react'
import './App.css'
import ListaKorisnika from './ListaKorisnika'
import Form from './Form'
import Login from "./Login.jsx";
import {AuthProvider} from "./AuthContext.jsx";

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
    <AuthProvider>
      <ListaKorisnika/>
        <Form/>
        <Login/>
    </AuthProvider>
    </>
  )
}

export default App
