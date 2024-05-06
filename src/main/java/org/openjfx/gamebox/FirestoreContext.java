package org.openjfx.gamebox;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

public class FirestoreContext {

    private static Firestore dbInstance = null;

    //Firebase connection
    public static Firestore firebase() {
        if (dbInstance == null) {
            synchronized (FirestoreContext.class) {
                if (dbInstance == null) {
                    try {
                        if (FirebaseApp.getApps().isEmpty()) {
                            FileInputStream serviceAccount = new FileInputStream("src/main/resources/org/openjfx/gamebox/key.json");
                            FirebaseOptions options = new FirebaseOptions.Builder()
                                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                                    .build();
                            FirebaseApp.initializeApp(options);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        System.out.println("Error initializing Firebase");
                        return null;
                    }
                    dbInstance = FirestoreClient.getFirestore();
                }
            }
        }
        return dbInstance;
    }
}