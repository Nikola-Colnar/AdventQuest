import { initializeApp } from "firebase/app";
import { getAuth, GoogleAuthProvider } from "firebase/auth"

const firebaseConfig = {
    apiKey: "AIzaSyBAe6FxAR6f5kYesZzeKon358Fa129-ZLA",
    authDomain: "progiprojekt-be466.firebaseapp.com",
    projectId: "progiprojekt-be466",
    storageBucket: "progiprojekt-be466.firebasestorage.app",
    messagingSenderId: "741311302115",
    appId: "1:741311302115:web:5ee5dce00e86b95787b8d6"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
export const googleProvider = new GoogleAuthProvider();
