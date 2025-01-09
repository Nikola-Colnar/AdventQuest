import { styled } from "@mui/system";
import { MenuItem } from "@mui/material";
import PropTypes from "prop-types";

// styling za logout gumb
const LogoutButton = styled(MenuItem)(() => ({
  backgroundColor: "rgba(255, 24, 24, 0.27)",
  color: "black",
  textAlign: "center",
  justifyContent: "center",

  ":hover": {
    backgroundColor: "red",
    color: "white",
    textAlign: "center",
    justifyContent: "center",
  },
}));

const SignOutButton = ({ onClick, onClose }) => {
  // funkcija za sign out
  const handleSignOut = async () => {
    try {
      const response = await fetch("http://localhost:8080/logout", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include", // Include credentials in the request
      });
  
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
  
      // Resetiraj lokalno stanje i očisti localStorage
      localStorage.removeItem("username");
      onClick(); // Resetira stanje prijave (isLoggedIn -> false)
      onClose(); // Zatvori meni
    } catch (error) {
      console.error("There was a problem with the fetch operation:", error);
    }
  };

  return (
    <LogoutButton className="logout_button" onClick={handleSignOut}>
      Sign Out
    </LogoutButton>
  );
};

SignOutButton.propTypes = {
  onClick: PropTypes.func.isRequired,
  onClose: PropTypes.func.isRequired,
};

export default SignOutButton;