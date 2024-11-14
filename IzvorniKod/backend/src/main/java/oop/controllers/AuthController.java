package oop.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

public class AuthController {

    public boolean isValidToken(String idToken) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();

            // Dodaj logiku za korisnika (npr. kreiranje sesije, provjera prava korisnika)
            System.out.println("Verified user ID: " + uid);
            return true;

        } catch (Exception e) {
            System.out.println("Invalid or expired token: " + e.getMessage());
            return false;
        }
    }
}