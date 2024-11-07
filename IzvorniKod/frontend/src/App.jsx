import { useState } from 'react'
import './App.css'
import ListaKorisnika from './ListaKorisnika'
import Form from './Form'
import Countdown from './Countdown'
import Snowfall from './Snowfall'
import LoginButton from './LoginButton'

function App() {
  const [isFormVisible, setIsFormVisible] = useState(false); //Stanje vidljivosti login forme

  const showForm = () => setIsFormVisible(true); // prikazuje formu
  const hideForm = () => setIsFormVisible(false); // Sakriva formu

  const [count, setCount] = useState(0)

  return (
    <>
      <LoginButton onClick={showForm}/>
      <Snowfall/>
      {isFormVisible && <Form onClick={hideForm} />}
      <Countdown targetDate="2024-12-25T00:00:00"/>
    </>
  )
}

export default App
