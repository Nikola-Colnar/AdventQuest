import React, { useState } from 'react';
import { AppBar, Toolbar, Typography, Button, Menu, MenuItem, Avatar, Box } from '@mui/material';
import { styled } from '@mui/system';
import {useAuth} from "./Firebase/AuthContext.jsx";
import SignOutButton from "./Components/SignOutButton.jsx";

//styling for nav bar and components
const StyledAppBar = styled(AppBar)(() => ({
  position: 'relative',  //position relative jer (static position ne moze mijenjati z index)
  backgroundColor: 'rgba(255, 0, 0)',  // boja navigacijskog bara
  boxShadow: 'none',  
  zIndex: 11,
}));

const StyledToolbar = styled(Toolbar)(() => ({
  display: 'flex',   
  justifyContent: 'space-between', //split components
  alignItems: 'center',  // vertical centerin
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
    backgroundColor: 'lightgreen'
  },
}));


//////////////////
//function start//
//////////////////
function Header({ isLoggedIn, username, userAvatar, onLoginClick, onSignupClick, onLogoutClick }) {

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

  return (
    <StyledAppBar position="static" >
      <StyledToolbar>
        <Logo variant="h6" component="div">  {/*Logo on the left*/}
          AdventQuest
        </Logo>

        {/* Profile section (login, signup, avatar)*/}
        {currentUser? (
          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            {/* Avatar with user image or initials */}
            <Avatar 
              src={userAvatar} 
              alt={username} 
              sx={{ 
                //avatar styling
                marginRight: '0.3em', 
                width: 30,  
                height: 30, 
                ':hover': { 
                  transform: 'scale(1.2)',  // growing on Hover
                  transition: 'transform 0.3s ease',
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
                  textAlign: centered ? 'center' : 'initial', // Center the username on click
                }}
              >
                {username}
                
              </MenuItem>
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