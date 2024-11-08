package oop;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;

@SpringBootApplication
public class ProgiAppApplication {

	public static void main(String[] args) {

		try {
			ClassLoader classLoader = ProgiAppApplication.class.getClassLoader();

			File file = new File(classLoader.getResource("serviceAccountKey.json").getFile());

			FileInputStream serviceAccount = new FileInputStream(file.getAbsolutePath());
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.build();

			FirebaseApp.initializeApp(options);

			SpringApplication.run(ProgiAppApplication.class, args);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to initialize Firebase: " + e.getMessage());
		}


	}
}
