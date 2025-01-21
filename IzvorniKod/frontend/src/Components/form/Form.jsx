import { useState, useRef } from "react";
import "./form.css";
import { FaLock } from "react-icons/fa";
import { IoIosMail } from "react-icons/io";
import { Box, Alert } from "@mui/material";
import PropTypes from "prop-types";
import { FcGoogle } from "react-icons/fc";
import { Link } from "react-router-dom";

// Endpoint za login i google login
const USERS_REST_API_URL = "http://localhost:8080/login";
const GOOGLE_LOGIN_API_URL = "http://localhost:8080/api/login/google";

function Form({ loggedIn }) {
  // Stanice za formu
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });
  const [severity, setSeverity] = useState(""); // Za odabir vrste poruke (error/success)
  const [message, setMessage] = useState("");  // Poruka koju ćemo prikazati korisniku

  // Refovi za overlay i formu
  const overlayRef = useRef(null);
  const formRef = useRef(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });  // Ažuriranje stanja forme
  };

  // Funkcija za login
  const handleLogin = async (e) => {
    e.preventDefault(); // Sprečavanje default ponašanja forme (refresh stranice)
    try {
      const response = await fetch(USERS_REST_API_URL, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username: formData.username,  // username korisnika
          password: formData.password,  // Lozinka korisnika
        }),
        credentials: 'include',
      });

      if (!response.ok) {
        // Ako odgovor nije ok
        setMessage("Network response was not ok");
        setSeverity("error");
      } else {
        const data = await response.json();  // Parsiranje JSON odgovora
        console.log("user podaci: ", data);
        const username = data.username;
        localStorage.setItem("username", username);  // Spremanje username-a u localStorage
        
        // ID je potreban za usporedbu s ID pošiljatelja poruke
        const userID = data.userID;
        localStorage.setItem("userID", userID);

        loggedIn(true);  // Pozivanje funkcije koja označava da je korisnik prijavljen
        setMessage("User logged in successfully");
        setSeverity("success");  // Postavljanje poruke i statusa
      }
    } catch (error) {
      console.error("Error with login:", error);
      setMessage("Failed to login");  // Ako je došlo do greške u login procesu
      setSeverity("error");
    }

  };
  // signup korisnika sa google racunom
  const handleGoogleOAuth2Login = () => {
    console.log("Redirecting to Google OAuth2 login...");
    window.location.href = GOOGLE_LOGIN_API_URL;
  };
  return (
    <div ref={overlayRef} className="overlay">
      <form ref={formRef} className="Form" onSubmit={handleLogin}> {/* Forma za login */}
        <h2>Login</h2>
        <div className="maildiv">
          <input
            type="text"
            name="username"
            placeholder="Username"
            value={formData.username}
            onChange={handleChange}  // Pozivanje funkcije kad se mijenja input
            required
          />
          <IoIosMail className="mailicon" />
        </div>
        <div className="passdiv">
          <input
            type="password"
            name="password"
            placeholder="Password"
            value={formData.password}
            onChange={handleChange}  // Pozivanje funkcije kad se mijenja input
            required
          />
          <FaLock className="passicon" />
        </div>
        {message && (
          <Box spacing={2} className={"error-message"} paddingBottom={2}>
            {/* Prikazivanje poruka (error/success) */}
            <Alert severity={severity}>{message}</Alert>
          </Box>
        )}
        <div className="submitDiv">
          <button className="submit" type="submit">Submit</button>
          {/* Gumb za prijavu */}
        </div>
        <div className="google-signin">
          <button
            type="button"
            onClick={handleGoogleOAuth2Login}  // Funkcija za google login
            className="google-button"
          >
            {<FcGoogle className="google-icon" />} Sign in with Google
          </button>
        </div>
        <div className="register-link">
          <div className="line" />
          <Link to="/register" className="register-text">
            Don’t have an account? Register here
          </Link>
          <div className="line" />
        </div>
      </form>
    </div>
  );
}

Form.propTypes = {
  loggedIn: PropTypes.func.isRequired,  // funkcija koja oznacava da je korisnik prijavljen
};

export default Form;
