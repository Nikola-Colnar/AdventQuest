//import { styled } from "@mui/system";
//import { MenuItem } from "@mui/material";
import PropTypes from "prop-types";
import { Button } from "@mui/material"

//const LogoutButton = styled(MenuItem)(() => ({
//  backgroundColor: "rgba(255, 24, 24, 0.27)",
//  color: "black",
//  textAlign: "center",
//  justifyContent: "center",
//
//  ":hover": {
//    backgroundColor: "red",
//    color: "white",
//    textAlign: "center",
//    justifyContent: "center",
//  },
//}));

const SignOutButton = ({ onClick, onClose }) => {
  // funkcija za sign out
  const handleSignOut = async () => {
    try {
      const response = await fetch("http://localhost:8080/logout", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      // resetiraj lokalno stanje i ocisti localStorage
      localStorage.clear();
      onClick(); // resetira stanje prijave (isLoggedIn -> false)
      onClose(); // zatvori meni
    } catch (error) {
      console.error("There was a problem with the fetch operation:", error);
    }
  };

  return (
    <Button
      onClick={() => {
        handleSignOut()
      }}
      sx={{
        display: "flex",
        justifyContent: "center",
        textAlign: "center",
        width: "100%",
        alignItems: "center",
        backgroundColor: "rgba(255, 69, 58, 0.3)",
        color: "black",
        transition: "background-color 0.3s, color 0.3s",

        "&:hover": {
          backgroundColor: "rgba(255, 0, 0, 0.85)",
          color: "#ffffff",
        },
      }}
      disableRipple={true}
    >
      Sign Out
    </Button>
  );
};

SignOutButton.propTypes = {
  onClick: PropTypes.func.isRequired,
  onClose: PropTypes.func.isRequired,
};

export default SignOutButton;
