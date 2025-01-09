import React, { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";

const UserInfo = () => {
  const [username, setUsername] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/userinfo", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include", // Include credentials in the request
        });

        if (!response.ok) {
          throw new Error("Failed to fetch user info");
        }

        const userInfo = await response.json();
        setUsername(userInfo.username);
        localStorage.setItem("username", userInfo.username);
        console.log("User info received and username saved to localStorage:", userInfo);
        navigate("/");
      } catch (error) {
        console.error("There was a problem with the fetch operation:", error);
      }
    };

    fetchUserInfo();
  }, []);

  return (
    <div>
      {username ? <p>Welcome, {username}!</p> : <p>Loading user info...</p>}
    </div>
  );
};

export default UserInfo;