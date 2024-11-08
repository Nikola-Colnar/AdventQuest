import React, { useState } from 'react';
import { createUserWithEmailAndPassword } from "firebase/auth";
import { auth } from "../firebaseConfig";
import {useAuth} from "./AuthContext.jsx";
import SignOutButton from "./SignOutButton.jsx";


const USERS_REST_API_URL = 'http://localhost:8080/api/users';


function Form() {
  // State za praćenje unosa u formu

    const { currentUser } = useAuth();

  const [formData, setFormData] = useState({
    username: '',
    password: '',
    email: ''
  });

  // Funkcija za rukovanje promjenama u input poljima
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  // Funkcija za slanje podataka na backend
  // const handleSubmit = async (e) => {
  //   e.preventDefault(); // Sprječava ponovno učitavanje stranice
  //   try {
  //     const response = await fetch(USERS_REST_API_URL, {
  //       method: 'POST',
  //       headers: {
  //         'Content-Type': 'application/json'
  //       },
  //       body: JSON.stringify(formData)
  //     });
  //
  //     if (!response.ok) {
  //       throw new Error('Network response was not ok ' + response.statusText);
  //     }
  //
  //     const result = await response.json();
  //     console.log('User created successfully:', result);
  //     alert('User created successfully!');
  //
  //     // Resetiranje forme nakon uspješnog slanja
  //     setFormData({ username: '', password: '', email: '' });
  //   } catch (error) {
  //     console.error('Error creating user:', error);
  //     alert('Failed to create user.');
  //   }
  // };


    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await createUserWithEmailAndPassword(auth, formData.email, formData.password);
            alert("User registered successfully");
        } catch (error) {
            setError(error.message);
        }
    };


  return (
      <div>
          <div>
              <h1>Welcome to Firebase Auth with React</h1>
              {currentUser ? (
                  <div>
                      <p>Logged in as: {currentUser.email}</p>
                      <SignOutButton/>
                  </div>
              ) : (
                  <div>
                      <p>Click here to signup</p>
                      <p>Click here to login</p>
                  </div>
              )}
          </div>
          <form onSubmit={handleSubmit} style={{
              padding: '20px',
              border: '1px solid #ccc',
              borderRadius: '8px',
              width: '300px',
              margin: '20px auto'
          }}>
              <h2>Create User</h2>
              <label>
                  Username:
                  <input
                      type="text"
                      name="username"
                      value={formData.username}
                      onChange={handleChange}
                      required
                  />
              </label>
              <br/>
              <label>
                  Password:
                  <input
                      type="password"
                      name="password"
                      value={formData.password}
                      onChange={handleChange}
                      required
                  />
              </label>
              <br/>
              <label>
                  Email:
                  <input
                      type="email"
                      name="email"
                      value={formData.email}
                      onChange={handleChange}
                      required
                  />
              </label>
              <br/>
              <button type="submit">Submit</button>
          </form>
      </div>

  );
}

export default Form;