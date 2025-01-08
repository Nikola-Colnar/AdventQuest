import { useState, useEffect, useRef } from "react";
import "./form.css";
import { FaLock } from "react-icons/fa";
import { IoIosMail } from "react-icons/io";
import { Box, Alert } from "@mui/material";
import PropTypes from "prop-types";
import { FcGoogle } from "react-icons/fc";

// Endpoint za login i google login
const USERS_REST_API_URL = "http://localhost:8080/login";
const GOOGLE_LOGIN_API_URL = "http://localhost:8080/api/login/google";

function Form({ onClick, loggedIn }) {
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

  useEffect(() => {
    const handleClickOutside = (e) => {
      // Zatvaranje forme ako se klikne izvan nje
      if (overlayRef.current && overlayRef.current.contains(e.target) && !formRef.current.contains(e.target)) {
        onClick();  // Pozivanje funkcije za zatvaranje
      }
    };
    document.addEventListener("click", handleClickOutside); // Dodavanje event listenera

    return () => {
      document.removeEventListener("click", handleClickOutside); // Uklanjanje event listenera pri unmountanju komponente
    };
  }, [onClick]);

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
      });

      if (!response.ok) {
        // Ako odgovor nije ok
        setMessage("Network response was not ok");
        setSeverity("error");
      } else {
        const data = await response.json();  // Parsiranje JSON odgovora
        const username = data.username;
        localStorage.setItem("username", username);  // Spremanje username-a u localStorage
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
  const handleGoogleSignIn = () => {
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
            onClick={handleGoogleSignIn}
            className="google-button"
          >
            {<FcGoogle className="google-icon" />} Sign in with Google
          </button>
        </div>
      </form>
    </div>
  );
}

Form.propTypes = {
  onClick: PropTypes.func.isRequired,  // Funkcija koja se poziva za zatvaranje forme
  loggedIn: PropTypes.func.isRequired,  // Funkcija koja označava da je korisnik prijavljen
};

export default Form;