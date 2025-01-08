import { useState, useEffect, useRef } from "react";
import "./form.css";
import { FaUser, FaLock } from "react-icons/fa";
import { FcGoogle } from "react-icons/fc";
import { IoIosMail } from "react-icons/io";
import { Box, Alert } from "@mui/material";
import PropTypes from "prop-types";

const REGISTER_API_URL = "http://localhost:8080/register";
const GOOGLE_LOGIN_API_URL = "http://localhost:8080/api/login/google";

function RegForm({ onClick, signIn }) {
  // state za pracenje podataka u formi
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    email: "",
    vrstaUser: "korisnik",
  });
  const [severity, setSeverity] = useState("");
  const [message, setMessage] = useState("");

  // referenca na overlay div
  const overlayRef = useRef(null);
  const formRef = useRef(null);

  useEffect(() => {
    // funkcija koja detektira klik izvan forme (na overlay)
    const handleClickOutside = (e) => {
      if (
        overlayRef.current &&
        overlayRef.current.contains(e.target) &&
        !formRef.current.contains(e.target)
      ) {
        onClick(); // poziva onClick (hideForm) kad je kliknut overlay
      }
    };
    // dodajemo event listener za klikove na dokument
    document.addEventListener("click", handleClickOutside);

    // cistimo event listener kad se komponenta unmounta
    return () => {
      document.removeEventListener("click", handleClickOutside);
    };
  }, [onClick]); // useEffect ce se ponovo pozvati samo ako se onClick promijeni

  // funkcija za rukovanje promjenama u input poljima
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  // funkcija za postavljanje tipa korisnika
  const handleUserRoleClick = (role) => {
    setFormData((prevData) => ({
      ...prevData,
      vrstaUser: role,
    }));
  };

  // registracija korisnika na firebase
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch(REGISTER_API_URL, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username: formData.username,  // username korisnika
          password: formData.password,  // lozinka korisnika
          email: formData.email // mail korisnika
        }),
      });

      if (response.ok) {
        setMessage("User registered successfully");
        setSeverity("success");
        const data = await response.json();  // Parsiranje JSON odgovora
        const username = data.username;
        localStorage.setItem("username", username);
        signIn(true);
      } else {
        setMessage("Failed to register user");
        setSeverity("error");
      }
    } catch (error) {
      setMessage("Error registering user: " + error.message);
      setSeverity("error");
    }
  };

  // signup korisnika sa google racunom
  const handleGoogleSignIn = () => {
    window.location.href = GOOGLE_LOGIN_API_URL;
  };

  return (
    <div ref={overlayRef} className="overlay">
      <form ref={formRef} className="Form" onSubmit={handleSubmit}>
        <h2>Sign in</h2>
        <div className="userdiv">
          <input
            type="text"
            name="username"
            placeholder="Username"
            value={formData.username}
            onChange={handleChange} // svaka promjena se handlea
            required // sprjecava submit dok polje nije ispravno
          />
          <FaUser className="usericon"></FaUser>
        </div>
        <div className="passdiv">
          <input
            type="password"
            name="password"
            placeholder="Password"
            value={formData.password}
            onChange={handleChange}
            required // sprjecava submit dok polje nije ispravno
          />
          <FaLock className="passicon"></FaLock>
        </div>
        <div className="maildiv">
          <input
            type="email"
            name="email"
            placeholder="Email"
            value={formData.email}
            onChange={handleChange}
            required // sprjecava submit dok polje nije ispravno
          />
          <IoIosMail className="mailicon"></IoIosMail>
        </div>
        <div className="vrstaUser-select">
          <div className="divKorisnik">
            <button
              type="button"
              onClick={() => handleUserRoleClick("korisnik")}
              className={
                formData.vrstaUser === "korisnik"
                  ? "vrstaUser-button green"
                  : "vrstaUser-button red"
              }
            >
              User
              <div className="selector-icon">🎅</div>
            </button>
          </div>
          <div className="divPredstavnik">
            <button
              type="button"
              onClick={() => handleUserRoleClick("predstavnik")}
              className={
                formData.vrstaUser === "predstavnik"
                  ? "vrstaUser-button green"
                  : "vrstaUser-button red"
              }
            >
              Leader
              <div className="selector-icon">🎅</div>
            </button>
          </div>
        </div>
        {message && (
          <Box
            spacing={2}
            className={"error-message"}
            paddingBottom={2}
            sx={{
              width: '100%',
              maxWidth: '400px',
              wordWrap: 'break-word',
              overflowWrap: 'break-word',
              whiteSpace: 'normal',
              display: 'flex',
              justifyContent: 'center',
            }}
          >
            <Alert
              severity={severity}
              sx={{
                width: '100%',
                wordWrap: 'break-word',
                overflowWrap: 'break-word',
                whiteSpace: 'normal',
              }}
            >
              {message}
            </Alert>
          </Box>
        )}
        <div className="submitDiv">
          <button className="submit" type="submit">
            Submit
          </button>
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

RegForm.propTypes = {
  onClick: PropTypes.func.isRequired,
  signIn: PropTypes.func.isRequired,
};

export default RegForm;