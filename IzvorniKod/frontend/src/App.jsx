import { useState } from "react";
import { Navigate, BrowserRouter as Router, useRoutes } from "react-router-dom";
import "./styles/App.css";
import Form from "./Components/form/Form.jsx";
import RegForm from "./Components/form/RegForm.jsx";
import Countdown from "./Components/countdown/Countdown.jsx";
import Snowfall from "./Components/snowfall/Snowfall.jsx";
import Header from "./Components/Header.jsx";
import UserInfo from "./Components/form/UserInfo.jsx";
import { Box } from "@mui/system";
import ProtectedRoute from "./Components/ProtectedRoute.jsx";

function App() {
  // definiranje stanja prijave, korisnickog imena i kalendara
  // * kod refreshanja stranice provjerava se localstorage
  const [isLoggedIn, setIsLoggedIn] = useState(
    localStorage.getItem("username") ? true : false
  );
  const [username, setUsername] = useState(
    localStorage.getItem("username") || "Guest"
  );

  const handleLoginStatusChange = (status) => {
    setIsLoggedIn(status);
    setUsername(localStorage.getItem("username"));
  };

  const handleSignInStatusChange = (status) => {
    setIsLoggedIn(status);
    setUsername(localStorage.getItem("username"));
  };

  const handlelogin = (status) => {
    setIsLoggedIn(status);
  };

  const AppRoutes = () => {
    const routes = [
      {
        path: "/",
        element: (
          <ProtectedRoute isLoggedIn={isLoggedIn}>
            <>
              <Snowfall className="snowfall" />
              <Header
                className="header"
                isLoggedIn={isLoggedIn}
                handlelogin={handlelogin}
                username={username}
              />
              <Countdown targetDate="2025-12-25T00:00:00" />
            </>
          </ProtectedRoute>
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
            <Form loggedIn={handleLoginStatusChange} />
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
            <RegForm signIn={handleSignInStatusChange} />
          </Box>
        ) : (
          <Navigate to="/" />
        ),
      },
      {
        path: "/groups",
        element: (
          <ProtectedRoute isLoggedIn={isLoggedIn}>
            <Box
              display="flex"
              alignItems="center"
              justifyContent="center"
              height="100vh"
              width="100vw"
            >
              <Snowfall className="snowfall" />
              <h1>Groups</h1>
            </Box>
          </ProtectedRoute>
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
