import React, { useState, useEffect, useRef } from 'react';
import "./form.css"
import { FaUser, FaLock,} from "react-icons/fa";
import { FcGoogle } from "react-icons/fc";
import { IoIosMail } from "react-icons/io";


const USERS_REST_API_URL = 'http://localhost:8080/api/users/login';

function Form({onClick, loggedIn  }) {
  //State za pracenje podataka u formi
  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });
  const overlayRef = useRef(null); // referenca na overlay div
  const formRef = useRef(null);

  // Funkcija koja detektira klik izvan forme (na overlay)
  const handleClickOutside = (e) => {
    if (overlayRef.current && overlayRef.current.contains(e.target) && !formRef.current.contains(e.target)) {
      onClick(); // poziva onClick (hideForm) kad je kliknut overlay
    }
  };

  useEffect(() => {
    // Dodajemo event listener za klikove na dokument
    document.addEventListener("click", handleClickOutside);

    // Čistimo event listener kad se komponenta unmounta
    return () => {
      document.removeEventListener("click", handleClickOutside);
    };
  }, [onClick]); // useEffect će se ponovo pozvati samo ako se onClick promijeni


  // Funkcija za rukovanje promjenama u input poljima
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  //slanje podataka na server
  const handleSubmit = async (e) => {
    console.log(formData)
    console.log(JSON.stringify(formData))
    e.preventDefault(); //sprjecava ponovno ucitavanje stranice
    try {
      const response = await fetch(USERS_REST_API_URL, {  //saljemo podatke 
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData) 
      });
      console.log(response.json);
      if (!response.ok) {
        throw new Error('Network response was not ok ' + response.statusText);
      }
      //ako je response dobar login je uspjesan
      const identified = await response.json();
      console.log('User Successfully logged in', identified);
      alert('Welcome back! Login successfully completed!');
      loggedIn(true); //skrivamo formu ako smo se prijavili
      //Nakon uspjesnog slanja forma se resetira
      setFormData({ username: '', password: ''});
    } catch (error) {
      console.error('Error logging in', error);
      alert('Login failed');
    }
  };

  return (
    <div ref={overlayRef} className='overlay'>
      <form ref={formRef}className="Form" onSubmit={handleSubmit} >
        <h2>Login</h2>
        <div className='userdiv'>
          <input
            type="text"
            name="username"
            placeholder='username/email'
            value={formData.username}
            onChange={handleChange} //svaka promjena se handlea
            required  //sprjecava submit dok polje nije ispravno
          />
          <FaUser className='usericon'></FaUser>
          
        </div>
        <br />
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
        <br />
        {/*
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
        */}
        <br />
        <div className='submitDiv'>
        <button className="submit" type="submit">Submit</button>
        </div>
        <div className="google-signin">
          <button type="button" className="google-button">{<FcGoogle className='google-icon'/>}  Sign in with Google</button>
          
        </div>
      </form>
    </div>
  );
}

export default Form;