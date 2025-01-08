import { useState } from "react";
import { AppBar, Toolbar, Typography, Button, Menu, MenuItem, Avatar, Box } from "@mui/material";
import { styled } from "@mui/system";
import SignOutButton from "./SignOutButton.jsx";
import PropTypes from "prop-types";


// styling za navigacijsku traku
const StyledAppBar = styled(AppBar)(() => ({
  position: "relative",  // position relative jer static position ne moze mijenjati z index
  backgroundColor: "rgba(255, 0, 0)",  // boja navigacijskog bara
  boxShadow: "none",
  zIndex: 11,
}));

const StyledToolbar = styled(Toolbar)(() => ({
  display: "flex",
  justifyContent: "space-between",
  alignItems: "center",
}));

const Logo = styled(Typography)(() => ({
  fontSize: "1.5rem",
  marginLeft: "2em",
  ":hover": {
    transform: "scale(1.2)",
    transition: "transform 0.3s ease",
  },
}));

const StyledMenu = styled(Menu)(() => ({
  " .MuiPaper-root": {
    minWidth: "200px",
    padding: "10px",
  },
}));

function Header({ isLoggedIn, handlelogin, username, userAvatar, onLoginClick, onSignupClick, calendarVisible }) {


  const [anchorEl, setAnchorEl] = useState(null);

  // funkcije za dropdown menu
  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  // funkcija za prikaz stavki nakon prijave
  const handleLogin = () => {
    handlelogin(false);
  };

  const handleCalendarToggle = () => {
    calendarVisible(true);    // toggle za kalendar
    setAnchorEl(null);  // zatvori dropdown menu kada je kalendar otvoren
  };

  return (
    <StyledAppBar position="static">
      <StyledToolbar>
        <Logo variant="h6" component="div">
          AdventQuest
        </Logo>

        {/* Conditional rendering ovisno je li korisnik prijavljen ili nije */}
        {(isLoggedIn) ? (
          <Box sx={{ display: "flex", alignItems: "center" }}>
            <Avatar
              src={userAvatar}
              alt={username}
              sx={{
                // avatar styling
                marginRight: "0.3em",
                width: 30,
                height: 30,
                ":hover": {
                  transform: "scale(1.2)",
                  transition: "transform 0.3s ease",
                },
              }}
              onClick={handleMenuOpen}
            />

            {/* username */}
            <Typography
              variant="h6"
              sx={{ marginRight: "2em", cursor: "default" }}
            >
              {username}
            </Typography>

            {/* dropdown menu */}
            <StyledMenu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={handleMenuClose}>
              <MenuItem
                disabled
                sx={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                  textAlign: "center",
                }}
              >
                {username}

              </MenuItem>
              <Button sx={{
                display: "flex",
                justifyContent: "center",
                textAlign: "center",
                width: "100%",
                alignItems: "center",
                backgroundColor: "rgba(84, 221, 52, 0.24)",
                color: "black",
                transition: "background-color 0.3s, color 0.3s",

                "&:hover": {
                  backgroundColor: "rgb(16, 165, 16)",
                  color: "#ffffff",
                },
              }} disableRipple={true} onClick={handleCalendarToggle}>Activity</Button>
              <SignOutButton onClick={handleLogin} onClose={handleMenuClose} />
              {/* Stari logout gumb, mozda cemo ga ponovno iskoristiti */}
              {/*<LogoutButton onClick={() => { handleMenuClose(); onLogoutClick(); }}>*/}
              {/*  Logout*/}
              {/*</LogoutButton>*/}
            </StyledMenu>
          </Box>
        ) : (
          <div>
            <Button color="inherit" onClick={onLoginClick}>Login</Button>
            <Button color="inherit" onClick={onSignupClick}>Signup</Button>
          </div>
        )}
      </StyledToolbar>
    </StyledAppBar>
  );
}

Header.propTypes = {
  isLoggedIn: PropTypes.bool.isRequired,
  handlelogin: PropTypes.func.isRequired,
  username: PropTypes.string.isRequired,
  userAvatar: PropTypes.string,
  onLoginClick: PropTypes.func.isRequired,
  onSignupClick: PropTypes.func.isRequired,
  calendarVisible: PropTypes.func.isRequired,
};

export default Header;