import { useEffect } from 'react';
import { useNavigate } from "react-router-dom";

const Logout = ({ setIsLoggedIn, loadUsername }) => {
  const navigate = useNavigate();

  useEffect(() => {
    const logoutUser = async () => {
      try {
        console.log("IZVRSAVAM LOGOUT")
        // Clear user-related data on logout
        localStorage.clear(); 
        setIsLoggedIn(false);  // Set logged-in status to false

        // send a logout request to the backend
        const response = await fetch("http://localhost:8080/logout", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: 'include', // Include credentials in the request
        });

        if (!response.ok) {
          throw new Error("Failed to log out");
        }
        console.log("User successfully logged out");
      } catch (error) {
        console.error("There was a problem with the logout operation:", error);
      }

      // After logging out, navigate to the login page
      navigate("/login");
    };

    logoutUser();
  }, [setIsLoggedIn, loadUsername, navigate]);

  return <div>Logging out...</div>;
};

export default Logout;