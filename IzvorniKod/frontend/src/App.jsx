import { useState } from 'react'
import './styles/App.css'
import Form from './components/form/Form.jsx'
import RegForm from './components/form/RegForm.jsx'
import Countdown from './components/countdown/Countdown.jsx'
import Snowfall from './components/snowfall/Snowfall.jsx'
import Header from './components/Header.jsx'


function App() {
  const [isFormVisible, setIsFormVisible] = useState(false); //stanje vidljivosti login forme

  const showForm = () => setIsFormVisible(true); // prikazuje formu
  const hideForm = () => setIsFormVisible(false); // Sakriva formu

  const [isRegFormVisible, setIsRegFormVisible] = useState(false); //Stanje vidljivosti login forme

  const showRegForm = () => setIsRegFormVisible(true); // prikazuje formu
  const hideRegForm = () => setIsRegFormVisible(false); // Sakriva formu


  // Definiranje stanja prijave i korisničkog imena
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [username, setUsername] = useState('Guest');

  const handleLoginStatusChange = (status) => {
    setIsLoggedIn(status)
    hideForm()    //skriva formu nakon logina
  };
  const handleSignInStatusChange = (status) => {
    setIsLoggedIn(status)
    hideRegForm() //skriva regformu nakon loginay
  };

  //funkcije za dobivanje Loginforme i signforme
  const handleLoginClick = () => {
    setUsername('M'); //promjena korisnika
    showForm();
  };

  const handleSignupClick = () => {
    setUsername('Sign'); //promjena korisnika
    showRegForm();
  };

  const handleLogoutClick = () => {
    setIsLoggedIn(false);     //logoutbutton
    setUsername('Guest');
  };
  return (
    <>
      <Header className="header"
        isLoggedIn={isLoggedIn}
        username={username}
        onLoginClick={handleLoginClick}
        onSignupClick={handleSignupClick}
        onLogoutClick={handleLogoutClick}
      />
      {isFormVisible && <Form onClick={hideForm} loggedIn={handleLoginStatusChange}/>}
      {isRegFormVisible && <RegForm onClick={hideRegForm} signIn={handleSignInStatusChange}/>}
      <Snowfall className="snowfall"/>
      
      <Countdown targetDate="2024-12-25T00:00:00"/>
    </>
  )
}

export default App
