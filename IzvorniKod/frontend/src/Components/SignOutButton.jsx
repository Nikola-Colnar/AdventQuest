import React from "react";
import { signOut } from "firebase/auth";
import { auth } from "../Firebase/firebaseConfig.js";
import { useAuth } from "../Firebase/AuthContext";
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
        ':hover': {
            backgroundColor: 'red',  // red on hover
            color: 'white',
        },
    }));

    if (!currentUser) return null;

    return (
        <LogoutButton onClick={handleSignOut}>Sign Out</LogoutButton>
    );
};

export default SignOutButton;