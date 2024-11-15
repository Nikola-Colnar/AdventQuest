import { useState, useEffect, useRef } from "react";
import "./form.css";
import { createUserWithEmailAndPassword, signInWithPopup } from "firebase/auth";
import { auth, googleProvider } from "../../firebase/firebaseConfig.js";
import { FaUser, FaLock } from "react-icons/fa";
import { FcGoogle } from "react-icons/fc";
import { IoIosMail } from "react-icons/io";
import { Box, Alert } from "@mui/material";
import PropTypes from "prop-types";


const USERS_REST_API_URL = "https://localhost:8443/api/users/signup";
const USERS_REST_API_URL1 = "https://localhost:8443/api/users/login";


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
      if (overlayRef.current && overlayRef.current.contains(e.target) && !formRef.current.contains(e.target)) {
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
      const Credentials = await createUserWithEmailAndPassword(auth, formData.email, formData.password);
      const user = Credentials.user;
      const idToken = await user.getIdToken();
      console.log("email login ID Token:", idToken);

      e.preventDefault(); // sprjecava ponovno ucitavanje stranice

      try {
        // saljemo podatke
        const response = await fetch(USERS_REST_API_URL, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${idToken}`,
          },
          body: JSON.stringify({
            username: formData.username,
            vrstaUser: formData.vrstaUser,
          }),
        });
        console.log(response);
        // provjeravamo statusni kod
        if (!response.ok) {
          setMessage("Network response was not ok");
          setSeverity("error");
        } else {
          // dohvacamo JSON odgovor
          const data = await response.json();
          console.log(data);
          // pohranjujemo u localStorage dobiveni username
          localStorage.setItem("username", data.username);
          signIn(true); // makne formu i postavlja username na stranici
          // nakon uspjesnog slanja forma se resetira
          setFormData({ username: "", password: "", email: "", vrstaUser: "korisnik" });
          setMessage("User created successfully");
          setSeverity("success");
        }
      } catch (error) {
        console.error("Database: Error creating user: ", error);
        setMessage("Ups! Something went wrong :(");
        setSeverity("error");
      }
    } catch (error) {
      console.error("Firebase: Error creating user: ", error);
      setMessage("User already exist!");
      setSeverity("error");
    }
  };

  // signup korisnika sa google racunom
  const handleGoogleSignIn = async () => {
    try {
      const result = await signInWithPopup(auth, googleProvider);
      const user = result.user;
      const idToken = await user.getIdToken();
      console.log("Google login ID Token:", idToken);
      // result.user sadrzi info o useru
      localStorage.setItem("username", user.displayName);
      console.log("User info:", result.user);
      console.log(localStorage.getItem("username"));

      try {
        // saljemo podatke
        const response = await fetch(USERS_REST_API_URL, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${idToken}`,
          },
          body: JSON.stringify({
            username: localStorage.getItem("username"),
            vrstaUser: formData.vrstaUser,
          }),
        });
        // provjeravamo statusni kod
        if (!response.ok) {
          console.log("user already created");

          try {
            // saljemo podatke
            const response = await fetch(USERS_REST_API_URL1, {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${idToken}`,
              },
            });
            // provjeravamo statusni kod
            if (!response.ok) {
              setMessage("Network response was not ok");
              setSeverity("error");
            } else {
              // dohvacamo JSON odgovor
              const data = await response.json();
              // pretpostavljamo da odgovor sadrzi username
              const username = data.username;
              console.log(username);
              localStorage.setItem("username", username);
              // prosljedujemo username u funkciju loggedIn
              signIn(true);
              setMessage("User logged in successfully");
              setSeverity("success");
              console.log(signIn);
            }
          } catch {
            setMessage("Network response was not ok");
            setSeverity("error");
          }

        } else {
          // dohvacamo JSON odgovor
          const data = await response.json();
          // pretpostavljamo da odgovor sadrzi username
          const username = data.username;
          console.log(username);
          localStorage.setItem("username", username);
          // prosljedujemo username u funkciju loggedIn
          signIn(true);
          setMessage("User Signed in successfully");
          setSeverity("success");
          console.log(signIn);
        }

      } catch (error) {
        console.error("Database: Error creating user: ", error);
        setMessage("Ups! Something went wrong :(");
        setSeverity("error");
      }

    } catch (error) {
      console.error("Error during Google sign-in:", error);
      alert("Failed to sign in with Google");
    }
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
            required  // sprjecava submit dok polje nije ispravno
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
            required  // sprjecava submit dok polje nije ispravno
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
            required  // sprjecava submit dok polje nije ispravno
          />
          <IoIosMail className="mailicon"></IoIosMail>
        </div>
        <div className="vrstaUser-select">
          <div className="divKorisnik">
            <button
              type="button"
              onClick={() => handleUserRoleClick("korisnik")}
              className={formData.vrstaUser === "korisnik" ? "vrstaUser-button green" : "vrstaUser-button red"}
            >
              User
              <div className="selector-icon">🎅</div>
            </button>
          </div>
          <div className="divPredstavnik">
            <button
              type="button"
              onClick={() => handleUserRoleClick("predstavnik")}
              className={formData.vrstaUser === "predstavnik" ? "vrstaUser-button green" : "vrstaUser-button red"}
            >
              Leader
              <div className="selector-icon">🎅</div>
            </button>
          </div>
        </div>
        {message &&
          <Box spacing={2} className={"error-message"} paddingBottom={2}>
            <Alert severity={severity}>
              {message}
            </Alert>
          </Box>
        }
        <div className="submitDiv">
          <button className="submit" type="submit">Submit</button>
        </div>
        <div className="google-signin">
          <button type="button"
                  onClick={handleGoogleSignIn}
                  className="google-button">{<FcGoogle
            className="google-icon" />} Sign in with Google
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