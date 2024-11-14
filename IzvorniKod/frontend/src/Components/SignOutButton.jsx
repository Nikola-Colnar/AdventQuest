import React from "react";
import { signOut } from "firebase/auth";
import { auth } from "../firebase/firebaseConfig.js";
import { useAuth } from "../firebase/AuthContext";
import {styled} from "@mui/system";
import {MenuItem} from "@mui/material";

const SignOutButton = ({onClose}) => {
    const { currentUser } = useAuth();

    const handleSignOut = () => {
        signOut(auth)
            .then(() => {
                alert("You have signed out successfully!");
                onClose();
            })
            .catch((error) => {
                console.error("Error signing out: ", error);
            });
    };

    const LogoutButton = styled(MenuItem)(() => ({
        backgroundColor: 'rgba(255, 24, 24, 0.27)',  // osnovna pozadina
        color: 'black',  // osnovna boja teksta
        textAlign: 'center',
        justifyContent: 'center',

        ':hover': {
            backgroundColor: 'red',  // red on hover
            color: 'white',
            textAlign: 'center',
            justifyContent:'center',
        },
    }));

    if (!currentUser) return null;

    return (
        <LogoutButton className="logout_button" onClick={handleSignOut}>Sign Out</LogoutButton>
    );
};

export default SignOutButton;