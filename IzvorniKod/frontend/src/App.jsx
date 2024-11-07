import { useState } from 'react'
import './App.css'
import ListaKorisnika from './ListaKorisnika'
import Form from './Form'
import RegForm from './RegForm'
import Countdown from './Countdown'
import Snowfall from './Snowfall'
import LoginButton from './LoginButton'
import SignupButton from './SignupButton'


function App() {
  const [isFormVisible, setIsFormVisible] = useState(false); //Stanje vidljivosti login forme

  const showForm = () => setIsFormVisible(true); // prikazuje formu
  const hideForm = () => setIsFormVisible(false); // Sakriva formu

  const [isRegFormVisible, setIsRegFormVisible] = useState(false); //Stanje vidljivosti login forme

  const showRegForm = () => setIsRegFormVisible(true); // prikazuje formu
  const hideRegForm = () => setIsRegFormVisible(false); // Sakriva formu

  const [count, setCount] = useState(0)

  return (
    <>
      <LoginButton onClick={showForm}/>
      {isFormVisible && <Form onClick={hideForm} />}
      <SignupButton onClick={showRegForm}/>
      {isRegFormVisible && <RegForm onClick={hideRegForm} />}
      <Snowfall/>
      
      <Countdown targetDate="2024-12-25T00:00:00"/>
    </>
  )
}

export default App
