import React, { useState } from 'react';
import { AppBar, Toolbar, Typography, Button, Menu, MenuItem, Avatar, Box } from '@mui/material';
import { styled } from '@mui/system';
import {useAuth} from "../firebase/AuthContext.jsx";
import SignOutButton from "./SignOutButton.jsx";

//styling for nav bar and components
const StyledAppBar = styled(AppBar)(() => ({
  position: "relative",  //position relative jer (static position ne moze mijenjati z index)
  backgroundColor: "rgba(255, 0, 0)",  // boja navigacijskog bara
  boxShadow: "none",
  zIndex: 11,
}));

const StyledToolbar = styled(Toolbar)(() => ({
  display: "flex",
  justifyContent: "space-between", //split components
  alignItems: "center",  // vertical centerin
}));

const Logo = styled(Typography)(() => ({
  fontSize: '1.5rem',
  marginLeft: '2em',
  ':hover': {
    transform: 'scale(1.2)', // Logo enlargement x1.2
    transition: 'transform 0.3s ease',
  },
}));

const StyledMenu = styled(Menu)(() => ({
  ' .MuiPaper-root': {
    minWidth: '200px',  // default menu size
    padding: '10px',
  },
}));


//////////////////
//function start//
//////////////////
function Header({ isLoggedIn, handlelogin, username, userAvatar, onLoginClick, onSignupClick, calendarVisible }) {

  //currentUser ---> null ako nitko nije logiran :: currentUser ako je netko logiran
  const { currentUser } = useAuth();

  const [anchorEl, setAnchorEl] = useState(null);
  const [centered, setCentered] = useState(false); // State to control the centering of the username

  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleUsernameClick = () => {
    setCentered(!centered); // Toggle centering the username
  };
  const handleCalendarToggle = () => {
    calendarVisible(true)// Toggle the calendar visibility
    setAnchorEl(null);  // Close the menu when calendar is opened
  };

  return (
    <StyledAppBar position="static" >
      <StyledToolbar>
        <Logo variant="h6" component="div">  {/*Logo on the left*/}
          AdventQuest
        </Logo>

        {/* Profile section (login, signup, avatar)*/}
        {(currentUser && isLoggedIn)? (
          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            {/* Avatar with user image or initials */}
            <Avatar
              src={userAvatar}
              alt={username}
              sx={{
                //avatar styling
                marginRight: "0.3em",
                width: 30,
                height: 30,
                ":hover": {
                  transform: "scale(1.2)",  // growing on Hover
                  transition: "transform 0.3s ease",
                },
              }}
              onClick={handleMenuOpen}  //Menu opening on the click of the avatar
            />

            {/* Username displayed next to the profile icon */}
            <Typography
              variant="h6"
              sx={{ marginRight: '2em', cursor: 'default'}}
              onClick={handleUsernameClick}
            >
              {username}
            </Typography>

            {/* Dropdown Menu */}
            <StyledMenu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={handleMenuClose}>
              <MenuItem
                disabled
                onClick={handleUsernameClick}
                sx={{
                  display: 'flex',
                  justifyContent: 'center',
                  alignItems: 'center',
                  textAlign: 'center',
                }}
              >
                {username}

              </MenuItem>
              <Button sx={{
                display: 'flex',
                justifyContent: 'center',
                textAlign: 'center',
                width: '100%',
                alignItems: 'center',
                backgroundColor: 'rgba(84, 221, 52, 0.24)',
                color: 'black',
                transition: 'background-color 0.3s, color 0.3s',

                '&:hover': {
                  backgroundColor: 'rgb(16, 165, 16)',
                  color: '#ffffff',
                },
              }} disableRipple={true} onClick={handleCalendarToggle}>Activity</Button>
              <SignOutButton onClose={handleMenuClose}/>

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

export default Header;