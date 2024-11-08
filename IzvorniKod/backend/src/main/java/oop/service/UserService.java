package oop.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import oop.model.User;
import oop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    // Metoda za kreiranje novog korisnika
//    public User createUser(User user) {
//
//        return userRepository.save(user);
//    }
//
//    public User getUserByEmail(String email){
//        return userRepository.findByEmail(email);
//    }
//
//    // Metoda za dohvaćanje svih korisnika
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    // Metoda za dohvaćanje korisnika prema ID-u
//    public Optional<User> getUserById(int id) {
//        return userRepository.findById(id);
//    }
//
//    // Metoda za ažuriranje korisnika
//    public User updateUser(int id, User userDetails) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
//
//        user.setUsername(userDetails.getUsername());
//        user.setPassword(userDetails.getPassword());
//        user.setEmail(userDetails.getEmail());
//
//        return userRepository.save(user);
//    }
//
//    // Metoda za brisanje korisnika
//    public void deleteUser(int id) {
//        userRepository.deleteById(id);
//    }

    public String createUser(User user) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference docRef = dbFirestore.collection("users").document();
        user.setId(docRef.getId());
        ApiFuture<WriteResult> result = docRef.set(user);
        return docRef.getId();
    }

    public User getUserById(String userId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference docRef = dbFirestore.collection("users").document(userId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            return document.toObject(User.class);
        } else {
            System.out.println("No such document!");
            return null;
        }
    }

    public String updateUser(User updatedUser) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference docRef = dbFirestore.collection("users").document(String.valueOf(updatedUser.getId()));
        ApiFuture<WriteResult> future = docRef.set(updatedUser);
        return "User updated at: " + future.get().getUpdateTime().toString();
    }

    public String deleteUser(String userId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> future = dbFirestore.collection("users").document(userId).delete();
        return "User deleted at: " + future.get().getUpdateTime().toString();
    }





}