// eslint-disable-next-line no-unused-vars
import React, { useState } from "react";
import { Navigate, BrowserRouter as Router, useRoutes } from "react-router-dom";
import "./styles/App.css";
import Form from "./Components/form/Form.jsx";
import RegForm from "./Components/form/RegForm.jsx";
import Countdown from "./Components/countdown/Countdown.jsx";
import Snowfall from "./Components/snowfall/Snowfall.jsx";
import Header from "./Components/Header.jsx";
// import CalendarComponent from "./Components/calendar/CalendarComponent.jsx";
import UserInfo from "./Components/form/UserInfo.jsx";
import { Box } from "@mui/system";
// import CreateGroupButton from "./Components/CreateGroupButton.jsx";
// import AddUserToGroupButton from "./Components/AddUserToGroupButton.jsx";
// import SelectGroupForUserButton from "./Components/SelectGroupForUserButton.jsx";
// import DeleteUserFromGroup from "./Components/DeleteUserFromGroup.jsx";
// import ShowAllUsersFromGroup from "./Components/ShowAllUsersFromGroup.jsx";
// import CalendarLeader from "./Components/calendar/CalendarLeader.jsx";
// import CalendarUser from "./Components/calendar/CalendarUser.jsx";
// import ShowAllEventsFromGroup from "./Components/ShowAllEventsFromGroup.jsx";
// import AddEvent from "./Components/AddEvent.jsx";
// import EventProposals from "./Components/EventProposals.jsx";
// import PastEventList from "./Components/PastEventsList.jsx";
// import AddToCalendar from "./Components/AddToCalendar.jsx";
// import AdventCalendar from "./Components/AdventCalendar/AdventCalendar.jsx";

function App() {
  // // stanje vidljivosti login forme
  // const [isFormVisible, setIsFormVisible] = useState(false);
  // // prikaz i sakrivanje forme
  // const showForm = () => setIsFormVisible(true);
  // const hideForm = () => setIsFormVisible(false);

  // // stanje vidljivosti registracijske forme
  // const [isRegFormVisible, setIsRegFormVisible] = useState(false);
  // // prikaz i sakrivanje forme
  // const showRegForm = () => setIsRegFormVisible(true);
  // const hideRegForm = () => setIsRegFormVisible(false);

  // definiranje stanja prijave, korisnickog imena i kalendara
  // * kod refreshanja stranice provjerava se localstorage
  const [isLoggedIn, setIsLoggedIn] = useState(
    localStorage.getItem("username") ? true : false
  );
  const [username, setUsername] = useState(
    localStorage.getItem("username") || "Guest"
  );
  // const [showCalendar, setShowCalendar] = useState(false);

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

  // // funkcije za prikaz loginforme i signinforme
  // const handleLoginClick = () => {
  //   showForm();
  // };

  // const handleSignupClick = () => {
  //   showRegForm();
  // };

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
        element: isLoggedIn ? (
          <>
            <Snowfall className="snowfall" />
            <Header
              className="header"
              isLoggedIn={isLoggedIn}
              handlelogin={handlelogin}
              username={username}
              calendarVisible={handleCalendar}
            />
            <Countdown targetDate="2025-12-25T00:00:00" />
          </>
        ) : (
          <Navigate to="/login" />
        ),
      },
      {
        path: "/login",
        element: !isLoggedIn ? (
          <Box
            display="flex"
            alignItems="center"
            justifyContent="center"
            height="100vh"
            width="100vw"
          >
            <Snowfall className="snowfall" />
            <Form loggedIn={handleLoginStatusChange}/>
          </Box>
        ) : (
          <Navigate to="/" />
        ),
      },
      {
        path: "/register",
        element: !isLoggedIn ? (
          <Box
            display="flex"
            alignItems="center"
            justifyContent="center"
            height="100vh"
            width="100vw"
          >
            <Snowfall className="snowfall" />
            <RegForm signIn={handleSignInStatusChange}/>
          </Box>
        ) : (
          <Navigate to="/" />
        ),
      },
      {
        path: "/userinfo",
        element: (
          <UserInfo setIsLoggedIn={setIsLoggedIn} loadUsername={setUsername} />
        ),
      },
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
