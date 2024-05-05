package org.openjfx.gamebox;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class LoginController {

    @FXML
    private Button registerPageButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField emailField;

    @FXML
    private Button leaderboardButton;

    @FXML
    private void showLeaderboard(ActionEvent event) throws IOException {
        switchToLeaderboard();
    }

    private void switchToLeaderboard() throws IOException {
        LoginApp.setRoot("LeaderboardPage");
    }


    @FXML
    protected void signInUser() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        Firestore db = LoginApp.fstore;

        // Query Firestore to find a user document by email
        ApiFuture<QuerySnapshot> future = db.collection("users")
                .whereEqualTo("email", email)
                .get();

        new Thread(() -> {
            try {
                QuerySnapshot querySnapshot = future.get();
                if (!querySnapshot.isEmpty()) {
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                    Platform.runLater(() -> {
                        String storedPassword = document.getString("password");
                        if (password.equals(storedPassword)) {
                            System.out.println("Sign-in successful.");
                            UserSession.getInstance().setUserEmail(email); // Set user email in session
                            try {
                                switchtoSelect();
                            } catch (IOException e) {
                                System.err.println("Failed to switch to select: " + e.getMessage());
                                showAlert("Error", "Failed to navigate.");
                            }
                        } else {
                            System.out.println("Incorrect password.");
                            showAlert("Login Failed", "Incorrect password.");
                        }
                    });
                } else {
                    Platform.runLater(() -> {
                        System.out.println("No such user.");
                        showAlert("Login Failed", "No such user.");
                    });
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Platform.runLater(() -> showAlert("Error", "Operation was interrupted."));
            } catch (ExecutionException e) {
                Platform.runLater(() -> showAlert("Error", "Failed to complete the operation: " + e.getCause().getMessage()));
            }
        }).start();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void switchtoSelect() throws IOException {
        LoginApp.setRoot("game_selection");
    }

    @FXML
    private void switchToPrimary() throws IOException {
        LoginApp.setRoot("RegisterPage");
    }
}