// src/SignOutButton.js
import React from "react";
import { signOut } from "firebase/auth";
import { auth } from "../firebaseConfig";
import { useAuth } from "./AuthContext";

const SignOutButton = () => {
    const { currentUser } = useAuth();

    const handleSignOut = () => {
        signOut(auth)
            .then(() => {
                alert("You have signed out successfully!");
            })
            .catch((error) => {
                console.error("Error signing out: ", error);
            });
    };

    if (!currentUser) return null;

    return (
        <button onClick={handleSignOut}>Sign Out</button>
    );
};

export default SignOutButton;
