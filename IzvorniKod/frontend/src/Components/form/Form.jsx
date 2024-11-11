import {useState, useEffect, useRef} from 'react';
import "./form.css"
import {FaLock} from "react-icons/fa";
import {FcGoogle} from "react-icons/fc";
import {IoIosMail} from "react-icons/io";
import {signInWithEmailAndPassword} from "firebase/auth";
import {auth, googleProvider} from "../../firebase/firebaseConfig.js";
import {signInWithPopup} from "firebase/auth";
import {Box, Alert} from "@mui/material";
import PropTypes from "prop-types";

const USERS_REST_API_URL = 'http://localhost:8080/api/users/login';
const USERS_REST_API_URL1 = 'http://localhost:8080/api/users/signup';

function Form({onClick, loggedIn}) {
  //State za pracenje podataka u formi
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    email: ''
  });
  const [severity, setSeverity] = useState('');
  const [message, setMessage] = useState('');

  const overlayRef = useRef(null); // referenca na overlay div
  const formRef = useRef(null);

  useEffect(() => {
    // Funkcija koja detektira klik izvan forme (na overlay)
    const handleClickOutside = (e) => {
      if (overlayRef.current && overlayRef.current.contains(e.target) && !formRef.current.contains(e.target)) {
        onClick(); // poziva onClick (hideForm) kad je kliknut overlay
      }
    };
    // Dodajemo event listener za klikove na dokument
    document.addEventListener("click", handleClickOutside);

    // Čistimo event listener kad se komponenta unmounta
    return () => {
      document.removeEventListener("click", handleClickOutside);
    };
  }, [onClick]); // useEffect će se ponovo pozvati samo ako se onClick promijeni

  // Funkcija za rukovanje promjenama u input poljima
  const handleChange = (e) => {
    const {name, value} = e.target;
    setFormData({...formData, [name]: value});
  };

  //login korisnika na firebase sa mailom i lozinkom
  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const Credentials = await signInWithEmailAndPassword(auth, formData.email, formData.password);
      const user = Credentials.user
      const uid = user.uid
      try {
        const response = await fetch(USERS_REST_API_URL, {  //saljemo podatke
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            uid: uid
          })
        });

        if (!response.ok) {
          setMessage('Network response was not ok: ' + response.statusText);
          setSeverity('error');
          return;
        }
        const data = await response.json(); // Dohvaćamo JSON odgovor
        const username = data.username;     // Pretpostavljamo da odgovor sadrži 'username'
        console.log(username)
        localStorage.setItem('username', username);

        setMessage('User logged in successfully');
        setSeverity('success');
        loggedIn(true, username); // prosljeđujemo username u funkciju loggedIn
        console.log(loggedIn)

      } catch (error) {
        console.error('Database: Error with login: ', error);
        setMessage('Failed to login');
        setSeverity('error');
      }

    } catch (error) {
      console.error('Firebase: Error with login: ', error);
      setMessage('Failed to login');
      setSeverity('error');
    }
  };

  // login korisnika na firebase sa google racunom
  const handleGoogleSignIn = async () => {
    try {
      const result = await signInWithPopup(auth, googleProvider);
      // result.user sadrzi info o useru
      localStorage.setItem('username',result.user.displayName)
      console.log("User info:", result.user);
      alert(`Welcome ${result.user.displayName}`);
      loggedIn(true,localStorage.getItem('username'));

      //BAZA
      try {
        const response = await fetch(USERS_REST_API_URL1, {  //saljemo podatke
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            uid: localStorage.getItem('uid'),
            username: localStorage.getItem('username'),
            vrstaUser: 'korisnik'
          })
        });

        if (!response.ok) {
          console.log("user already created")
          try {
            const response = await fetch(USERS_REST_API_URL, {  //saljemo podatke
              method: 'POST',
              headers: {
                'Content-Type': 'application/json'
              },
              body: JSON.stringify({
                uid: localStorage.getItem('uid'),
              })
            });
            const data = await response.json(); // Dohvaćamo JSON odgovor
            localStorage.setItem('username',data.username)    // Dohvacamo username iz baze
            //Nakon uspjesnog slanja forma se resetira
            setFormData({username: '', password: '', email: '', vrstaUser: 'korisnik'});
            loggedIn(true, data.username);
          }catch {
            setMessage('Network response was not ok: ' + response.statusText);

            setSeverity('error');
            return;
          }
        }

      } catch (error) {
        console.error('Database: Error creating user: ', error);
        setMessage('Failed to create user');
        setSeverity('error');
      }
      setMessage('User logged in successfully');
      setSeverity('success');


    } catch (error) {
      console.error("Error during Google sign-in:", error);
      alert("Failed to sign in with Google");
    }
  };

  return (
    <div ref={overlayRef} className='overlay'>
      <form ref={formRef} className="Form" onSubmit={handleLogin}>
        <h2>Login</h2>
        <div className='maildiv'>
          <input
            type="email"
            name="email"
            placeholder='Email'
            value={formData.email}
            onChange={handleChange}
            required  // sprjecava submit dok polje nije ispravno
          />
          <IoIosMail className='mailicon'></IoIosMail>
        </div>
        <div className='passdiv'>
          <input
            type="password"
            name="password"
            placeholder='Password'
            value={formData.password}
            onChange={handleChange}
            required  // sprjecava submit dok polje nije ispravno
          />
          <FaLock className='passicon'></FaLock>
        </div>
        {message &&
          <Box spacing={2} className={'error-message'} paddingBottom={2}>
            <Alert severity={severity}>
              {message}
            </Alert>
          </Box>
        }
        <div className='submitDiv'>
          <button className="submit" type="submit">Submit</button>
        </div>
        <div className="google-signin">
          <button type="button" onClick={handleGoogleSignIn} className="google-button">{<FcGoogle
            className='google-icon'/>} Sign in with Google
          </button>
        </div>
      </form>
    </div>
  );
}

Form.propTypes = {
  onClick: PropTypes.func.isRequired,
  loggedIn: PropTypes.func.isRequired,
}

export default Form;