import { useState } from 'react'
import './App.css'
import ListaKorisnika from './ListaKorisnika'
import Form from './Form'
import Countdown from './Countdown'
import Snowfall from './Snowfall'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <Snowfall/>
      <ListaKorisnika/> <Form/>
      <Countdown targetDate="2024-12-25T00:00:00"/>
    </>
  )
}

export default App
