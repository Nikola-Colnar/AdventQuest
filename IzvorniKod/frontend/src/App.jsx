import React, { useState } from "react";
import { Navigate, BrowserRouter as Router, useRoutes } from "react-router-dom";
import { ThemeProvider, createTheme } from "@mui/material/styles";
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
import backgroundImage from './assets/Winter.jpg';
import backgroundImage2 from './assets/Background2.jpg';
import backgroundLogin from './assets/LoginPic.jpg';
import { Paper, Typography } from "@mui/material";
import AddUserToGroupButton from "./Components/AddUserToGroupButton.jsx";
import DeleteUserFromGroup from "./Components/DeleteUserFromGroup.jsx";
import AddToCalendar from "./Components/AddToCalendar.jsx";
import GroupEventPresident from "./Components/GroupEventPresident.jsx";
import AddEvent from "./Components/AddEvent.jsx";
import EventProposal from "./Components/EventProposals.jsx";
import GetAllGroupsAdmin from "./Components/GetAllGroupsAdmin.jsx";
import DeleteUser from "./Components/DeleteUser.jsx";

const christmasTheme = createTheme({
  palette: {
    primary: {
      main: "#2E8B57",
      contrastText: "#FFFFFF",
    },
    secondary: {
      main: "#800020",
      contrastText: "#FFFFFF",
    },
    info: {
      main: "#DAA520",
      contrastText: "#FFFFFF",
    },
    background: {
      default: "#F5F5F5",
      paper: "#FFFFFF",
    },
    text: {
      primary: "#000000",
      secondary: "#800020",
    },
  },
  typography: {
    h1: {
      fontWeight: 700,
    },
    h2: {
      fontWeight: 600,
    },
    body1: {
      fontWeight: 400,
    },
  },
});

function App() {
  // definiranje stanja prijave, korisnickog imena i kalendara
  // * kod refreshanja stranice provjerava se localstorage
  const [isLoggedIn, setIsLoggedIn] = useState((localStorage.getItem("username") ? true : false));
  const [username, setUsername] = useState((localStorage.getItem("username")) || "Guest");
  const [userID, setUserID] = useState((localStorage.getItem("userID")));
  const [refreshTrigger, setRefreshTrigger] = useState(0); // dodano radi prikaza odabrane grupe

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

  const refreshHeader = () => {
    setRefreshTrigger((prev) => prev + 1); // Povećanje refreshTrigger stanja
  };

  const AppRoutes = () => {
    const routes = [
      {
        path: "/",
        element: (
          <ProtectedRoute isLoggedIn={isLoggedIn}>
            <>
              <Box
                display="flex"
                flexDirection="column"
                alignItems="center"
                width="100%"
                maxWidth="100vw"
                sx={{
                  backgroundImage: `url(${backgroundImage2})`,
                  backgroundSize: 'cover',
                  backgroundPosition: 'center',
                  backgroundAttachment: 'fixed',
                  height: '100%',
                  minHeight: '100vh',
                  width: '100%',
                  paddingTop: '10px',


                }}

              >
              <Snowfall className="snowfall" />
              <Header

                className="header"
                isLoggedIn={isLoggedIn}
                handlelogin={handlelogin}
                username={username}
                refreshTrigger={refreshTrigger}
              />
              <Countdown targetDate="2025-12-25T00:00:00" />
                </Box>
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
            sx={{
                backgroundImage: `url(${backgroundLogin})`,
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                backgroundAttachment: 'fixed',
              }}

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
            sx={{
              backgroundImage: `url(${backgroundLogin})`,
              backgroundSize: 'cover',
              backgroundPosition: 'center',
              backgroundAttachment: 'fixed',
            }}

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
            <Box
              display="flex"
              flexDirection="column"
              alignItems="center"
              width="100%"
              maxWidth="100vw"
              sx={{
                backgroundImage: `url(${backgroundImage})`,
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                backgroundAttachment: 'fixed',
                height: '100%',
                minHeight: '100vh',
                width: '100%',
                paddingTop: '10px',


              }}
            >
              <Snowfall className="snowfall" />
              <Header
                className="header"
                isLoggedIn={isLoggedIn}
                handlelogin={handlelogin}
                username={username}
                refreshTrigger={refreshTrigger}
              />
              <GroupDashboard username={username} userID={userID} refreshHeader={refreshHeader} />
            </Box>
          </ProtectedRoute>
        ),
      },
      {
        path: "/admin",
        element: (
          <ProtectedRoute isLoggedIn={isLoggedIn}>
            <Box
              display="flex"
              flexDirection="column"
              alignItems="center"
              width="100%"
              maxWidth="100vw"
              sx={{
                backgroundImage: `url(${backgroundImage})`,
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                backgroundAttachment: 'fixed',
                height: '100%',
                minHeight: '100vh',
                width: '100%',
                paddingTop: '10px',


              }}
            >
              <Snowfall className="snowfall" />
              <Header
                className="header"
                isLoggedIn={isLoggedIn}
                handlelogin={handlelogin}
                username={username}
                refreshTrigger={refreshTrigger}
              />
            <AdminRoute username={username} userID={userID}/>
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
    <ThemeProvider theme={christmasTheme}>
      <Router>
        <AppRoutes />
      </Router>
    </ThemeProvider>
  );
}

export default App;
