package org.openjfx.gamebox;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterPageController {

    @FXML
    private Button goBackbtn;

    @FXML
    private TextField registeremailTextField;

    @FXML
    private TextField registernameTextField;

    @FXML
    private TextField registerpasswordTextField;

    @FXML
    private TextField registerphoneTextField;

    @FXML
    private Button registrationButton;

    @FXML
    void registerButtonClicked(ActionEvent event) {
        registerUser();
    }

    public boolean registerUser() {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(registeremailTextField.getText())
                .setEmailVerified(false)
                .setPassword(registerpasswordTextField.getText())
                .setPhoneNumber(registerphoneTextField.getText())
                .setDisplayName(registernameTextField.getText())
                .setDisabled(false);

        try {
            // Create user with Firebase Authentication
            UserRecord userRecord = LoginApp.fauth.createUser(request);
            System.out.println("Successfully created new user with Firebase Uid: " + userRecord.getUid());

            // Save user data to Firestore
            Firestore db = FirestoreClient.getFirestore();  // Get Firestore instance
            Map<String, Object> userData = new HashMap<>();
            userData.put("email", registeremailTextField.getText());
            userData.put("password", registerpasswordTextField.getText()); // Note: Storing passwords in plain text is not recommended
            userData.put("phone", registerphoneTextField.getText());
            userData.put("displayName", registernameTextField.getText());
            userData.put("SpaceInvadersScore", "0");
            userData.put("SwimmingScore", "0");
            userData.put("TileGameScore", "0");
            userData.put("ChessScore", "0");


            // Add a new document with the user UID as the document ID
            db.collection("users2").document(userRecord.getUid()).set(userData);

            return true;
        } catch (FirebaseAuthException ex) {
            ex.printStackTrace();
            System.out.println("Error creating a new user in Firebase");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error accessing Firestore");
            return false;
        }
    }

    @FXML
    void goBackToLoginPage(ActionEvent event) throws IOException {
        switchToLoginPage();
    }

    private void switchToLoginPage() throws IOException {
        LoginApp.setRoot("login_page");
    }
}