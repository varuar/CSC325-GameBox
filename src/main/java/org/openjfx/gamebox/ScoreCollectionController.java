package org.openjfx.gamebox;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

public class ScoreCollectionController {

    private Firestore db;

    public ScoreCollectionController() {
        FirestoreContext context = new FirestoreContext();
        db = context.firebase(); // Use existing FirestoreContext to get Firestore instance
    }

    public void updateGameScore(String gameName, int newScore) {
        String email = UserSession.getInstance().getUserEmail(); // Get email from user session
        if (email == null || email.isEmpty()) {
            System.out.println("User email not set. Cannot update score.");
            return;
        }

        ApiFuture<QuerySnapshot> future = db.collection("users2")
                .whereEqualTo("email", email)
                .get();

        future.addListener(() -> {
            try {
                QuerySnapshot querySnapshot = future.get();
                if (!querySnapshot.isEmpty()) {
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                    document.getReference().update(gameName + "Score", newScore);
                    System.out.println("Score updated successfully for " + gameName);
                } else {
                    System.out.println("No such user found with email: " + email);
                }
            } catch (Exception e) {
                System.out.println("Failed to update the score: " + e.getMessage());
            }
        }, Runnable::run);
    }
}
