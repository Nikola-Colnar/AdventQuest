// eslint-disable-next-line no-unused-vars
import React, { useState } from "react";
import { BrowserRouter as Router, useRoutes} from "react-router-dom";
import "./styles/App.css";
import Form from "./components/form/Form.jsx";
import RegForm from "./components/form/RegForm.jsx";
import Countdown from "./components/countdown/Countdown.jsx";
import Snowfall from "./components/snowfall/Snowfall.jsx";
import Header from "./components/Header.jsx";
import CalendarComponent from "./Components/calendar/CalendarComponent.jsx";
import { element } from "prop-types";
import UserInfo from "./Components/form/UserInfo.jsx";


function App() {
  // stanje vidljivosti login forme
  const [isFormVisible, setIsFormVisible] = useState(false);
  // prikaz i sakrivanje forme
  const showForm = () => setIsFormVisible(true);
  const hideForm = () => setIsFormVisible(false);

  // stanje vidljivosti registracijske forme
  const [isRegFormVisible, setIsRegFormVisible] = useState(false);
  // prikaz i sakrivanje forme
  const showRegForm = () => setIsRegFormVisible(true);
  const hideRegForm = () => setIsRegFormVisible(false);

  // definiranje stanja prijave, korisnickog imena i kalendara
  // * kod refreshanja stranice provjerava se localstorage
  const [isLoggedIn, setIsLoggedIn] = useState((localStorage.getItem("username") ? true : false));
  const [username, setUsername] = useState((localStorage.getItem("username")) || "Guest");
  const [showCalendar, setShowCalendar] = useState(false);

  const handleLoginStatusChange = (status) => {
    setIsLoggedIn(status);
    setUsername(localStorage.getItem("username"));
    setTimeout(hideForm, 1000);    // sakriva formu nakon logina
  };

  const handleSignInStatusChange = (status) => {
    setIsLoggedIn(status);
    setUsername(localStorage.getItem("username"));
    setTimeout(hideRegForm, 1000); // sakriva regformu nakon signin-a
  };

  // funkcije za prikaz loginforme i signinforme
  const handleLoginClick = () => {
    showForm();
  };

  const handleSignupClick = () => {
    showRegForm();
  };

  // funkcija za prikaz kalendara
  const handleCalendar = (status) => {
    setShowCalendar(status);
  };

  const handlelogin = (status) => {
    setIsLoggedIn(status);
  };

  const AppRoutes = () => {
    const routes = [
      {
        path: "/",
        element: (
          <>
            <Header
              className="header"
              isLoggedIn={isLoggedIn}
              handlelogin={handlelogin}
              username={username}
              onLoginClick={handleLoginClick}
              onSignupClick={handleSignupClick}
              calendarVisible={handleCalendar}
            />

            {isFormVisible && <Form onClick={hideForm} loggedIn={handleLoginStatusChange} />}
            {isRegFormVisible && <RegForm onClick={hideRegForm} signIn={handleSignInStatusChange} />}
            {showCalendar && <CalendarComponent hideCalendar={handleCalendar} />}

            <Snowfall className="snowfall" />
            <Countdown targetDate="2025-12-25T00:00:00" />
          </>
        ),
      },
      {
        path: "/userinfo",
        element: <UserInfo setIsLoggedIn={setIsLoggedIn} loadUsername={setUsername}/>
      }
    ];
    return useRoutes(routes);
  };

  return (
    <Router>
      <AppRoutes />
    </Router>
  );
}

export default App;