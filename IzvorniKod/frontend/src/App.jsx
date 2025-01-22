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
import AdminRoute from "./Components/AdminRoute.jsx";
import GroupDashboard from "./Components/GroupDashboard.jsx";

function App() {
  // definiranje stanja prijave, korisnickog imena i kalendara
  // * kod refreshanja stranice provjerava se localstorage
  const [isLoggedIn, setIsLoggedIn] = useState((localStorage.getItem("username") ? true : false));
  const [username, setUsername] = useState((localStorage.getItem("username")) || "Guest");
  const [userID, setUserID] = useState((localStorage.getItem("userID")));

  const handleLoginStatusChange = (status) => {
    setIsLoggedIn(status);
    setUsername(localStorage.getItem("username"));
    setUserID(localStorage.getItem("userID"));
  };

  const handleSignInStatusChange = (status) => {
    setIsLoggedIn(status);
    setUsername(localStorage.getItem("username"));
    setUserID(localStorage.getItem("userID"));
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
        path: "/dashboard",
        element: (
          <ProtectedRoute isLoggedIn={isLoggedIn}>
            <Snowfall className="snowfall" />
            <Box
              display="flex"
              flexDirection="column"
              height="100vh"
              width="100%"
              alignItems={"center"}
            >
              <Header
                className="header"
                isLoggedIn={isLoggedIn}
                handlelogin={handlelogin}
                username={username}
              />
              <GroupDashboard username={username} userID={userID} />
            </Box>
          </ProtectedRoute>
        ),
      },
      {
        path: "/admin",
        element: (
          <ProtectedRoute isLoggedIn={isLoggedIn}>
            <AdminRoute username={username}>
              <Box
                display="flex"
                alignItems="center"
                justifyContent="center"
                height="100vh"
                width="100vw"
              >
                <Snowfall className="snowfall" />
                <h1>Admin</h1>
              </Box>
            </AdminRoute>
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
