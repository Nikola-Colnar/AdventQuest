import {useState, useEffect, useRef} from 'react';
import "./form.css"
import {createUserWithEmailAndPassword} from "firebase/auth";
import {auth} from "../../firebase/firebaseConfig.js";
import {FaUser, FaLock,} from "react-icons/fa";
import {FcGoogle} from "react-icons/fc";
import {IoIosMail} from "react-icons/io";
import {Stack, Alert} from "@mui/material";
import PropTypes from "prop-types";

const USERS_REST_API_URL = 'http://localhost:8080/api/users/signup';

function RegForm({onClick}) {
  // state za pracenje podataka u formi
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    email: '',
    vrstaUser: 'korisnik'
  });
  const [message, setMessage] = useState('');

  const overlayRef = useRef(null); // referenca na overlay div
  const formRef = useRef(null);

  useEffect(() => {
    // funkcija koja detektira klik izvan forme (na overlay)
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

  const handleUserRoleClick = (role) => {
    setFormData((prevData) => ({
      ...prevData,
      vrstaUser: role
    }));
  };

  //registracija korisnika na firebase
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const Credentials = await createUserWithEmailAndPassword(auth, formData.email, formData.password);
      const user = Credentials.user
      const uid = user.uid
      console.log(JSON.stringify(uid, formData.username, formData.vrstaUser))

      e.preventDefault(); //sprjecava ponovno ucitavanje stranice
      try {
        const response = await fetch(USERS_REST_API_URL, {  //saljemo podatke
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            uid: uid,
            username: formData.username,
            vrstaUser: formData.vrstaUser
          })
        });

        if (!response.ok) {
          setMessage('Network response was not ok: ' + response.statusText);
          return;
        }

        //Nakon uspjesnog slanja forma se resetira
        setFormData({username: '', password: '', email: '', vrstaUser: 'korisnik'});
      } catch (error) {
        console.error('Database: Error creating user: ', error);
        setMessage('Failed to create user');
      }
      setMessage('User created successfully');
    } catch (error) {
      console.error('Firebase: Error creating user: ', error);
      setMessage('Failed to create user');
    }
  };

  return (
    <div ref={overlayRef} className='overlay'>
      <form ref={formRef} className="Form" onSubmit={handleSubmit}>
        <h2>Sign in</h2>
        <div className='userdiv'>
          <input
            type="text"
            name="username"
            placeholder='Username'
            value={formData.username}
            onChange={handleChange} //svaka promjena se handlea
            required  //sprjecava submit dok polje nije ispravno
          />
          <FaUser className='usericon'></FaUser>
        </div>
        <div className='passdiv'>
          <input
            type="password"
            name="password"
            placeholder='Password'
            value={formData.password}
            onChange={handleChange}
            required  //sprjecava submit dok polje nije ispravno
          />
          <FaLock className='passicon'></FaLock>
        </div>
        <div className='maildiv'>
          <input
            type="email"
            name="email"
            placeholder='Email'
            value={formData.email}
            onChange={handleChange}
            required  //sprjecava submit dok polje nije ispravno
          />
          <IoIosMail className='mailicon'></IoIosMail>
        </div>
        <div className='vrstaUser-select'>
          <div className='divKorisnik'>
            <button
              type="button"
              onClick={() => handleUserRoleClick('korisnik')}
              className={formData.vrstaUser === 'korisnik' ? 'vrstaUser-button green' : 'vrstaUser-button red'}
            >
              Korisnik 🎅
            </button>
          </div>
          <div className='divPredstavnik'>
            <button
              type="button"
              onClick={() => handleUserRoleClick('predstavnik')}
              className={formData.vrstaUser === 'predstavnik' ? 'vrstaUser-button green' : 'vrstaUser-button red'}
            >
              Predstavnik 🎄
            </button>
          </div>
        </div>
        {message &&
          <Stack spacing={2} className={'error-message'} paddingBottom={2}>
            <Alert severity='error'>
              {message}
            </Alert>
          </Stack>
        }
        <div className='submitDiv'>
          <button className="submit" type="submit">Submit</button>
        </div>
        <div className="google-signin">
          <button type="button" className="google-button">{<FcGoogle className='google-icon'/>} Sign in with Google
          </button>
        </div>
      </form>
    </div>
  );
}

RegForm.propTypes = {
  onClick: PropTypes.func.isRequired,
}

export default RegForm;