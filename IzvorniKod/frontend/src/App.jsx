import { useState } from 'react'
import './styles/App.css'
import Form from './components/form/Form.jsx'
import RegForm from './components/form/RegForm.jsx'
import Countdown from './components/countdown/Countdown.jsx'
import Snowfall from './components/snowfall/Snowfall.jsx'
import Header from './components/Header.jsx'
import CalendarComponent from "./Components/calendar/CalendarComponent.jsx";


function App() {
  const [isFormVisible, setIsFormVisible] = useState(false); //stanje vidljivosti login forme

  const showForm = () => setIsFormVisible(true); // prikazuje formu
  const hideForm = () => {

    setIsFormVisible(false)}; // Sakriva formu

  const [isRegFormVisible, setIsRegFormVisible] = useState(false); //Stanje vidljivosti login forme

  const showRegForm = () => setIsRegFormVisible(true); // prikazuje formu
  const hideRegForm = () => setIsRegFormVisible(false); // Sakriva formu


  // Definiranje stanja prijave i korisničkog imena
  ////kod refreshanja stranice provjerava localstorage
  const [isLoggedIn, setIsLoggedIn] = useState((localStorage.getItem('username') ? true : false));

  const [username, setUsername] = useState((localStorage.getItem('username')) || 'Guest');
  const [showCalendar, setShowCalendar] = useState(false);

  const handleLoginStatusChange = (status) => {
    setIsLoggedIn(status)
    setUsername(localStorage.getItem('username'))
    setTimeout(hideForm, 1000)    //skriva formu nakon logina
  };
  const handleSignInStatusChange = (status) => {
    setIsLoggedIn(status)
    setUsername(localStorage.getItem('username'))

    setTimeout(hideRegForm, 1000); //skriva regformu nakon loginay
  };

  //funkcije za dobivanje Loginforme i signforme
  const handleLoginClick = () => {
    showForm();
  };

  const handleSignupClick = () => {
    showRegForm();
  };
  const handleCalendar = (status) => {
    setShowCalendar(status)
  };

  return (
    <>
      <Header className="header"
        isLoggedIn={isLoggedIn}
        username={username}
        onLoginClick={handleLoginClick}
        onSignupClick={handleSignupClick}
        calendarVisible={handleCalendar}
      />
      {isFormVisible && <Form onClick={hideForm} loggedIn={handleLoginStatusChange}/>}
      {isRegFormVisible && <RegForm onClick={hideRegForm} signIn={handleSignInStatusChange}/>}
      <Snowfall className="snowfall"/>
      
      <Countdown targetDate="2024-12-25T00:00:00"/>
      {showCalendar && <CalendarComponent/>}
    </>
  )
}

export default App
