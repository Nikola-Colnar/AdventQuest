import { signOut } from "firebase/auth";
import { auth } from "../firebase/firebaseConfig.js";
import { useAuth } from "../firebase/AuthContext";
import { styled } from "@mui/system";
import { MenuItem } from "@mui/material";
import PropTypes from "prop-types";


const SignOutButton = ({ onClick, onClose }) => {
  const { currentUser } = useAuth();

  // funkcija za sign out
  const handleSignOut = () => {
    signOut(auth)
      .then(() => {
        alert("You have signed out successfully!");
        localStorage.clear();
        onClick();
        onClose();
      })
      .catch((error) => {
        console.error("Error signing out: ", error);
      });
  };

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

  if (!currentUser) return null;

  return (
    <LogoutButton className="logout_button" onClick={handleSignOut}>Sign Out</LogoutButton>
  );
};

SignOutButton.propTypes = {
  onClick: PropTypes.func.isRequired,
  onClose: PropTypes.func.isRequired,
};

export default SignOutButton;