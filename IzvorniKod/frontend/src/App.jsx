import { useState } from 'react'
import './App.css'
import ListaKorisnika from './ListaKorisnika'
import Form from './Form'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <ListaKorisnika/> <Form/>
    </>
  )
}

export default App
