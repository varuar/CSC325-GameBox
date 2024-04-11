package org.openjfx.gamebox;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button registerPageButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField emailField;

    @FXML
    void handleLoginAction(ActionEvent event) {
        signInUser();
    }

    @FXML
    protected void signInUser() {
        UserRecord.CreateRequest request1 = new UserRecord.CreateRequest()
                .setEmail(emailField.getText())
                .setPassword(passwordField.getText());
        UserRecord userRecord2;
        try {
            userRecord2 = LoginApp.fauth.getUserByEmail(emailField.getText());
            System.out.println(userRecord2);
            System.out.println("Sign-in successful.");
            switchtoSelect();
        } catch (IOException | FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
    }

    private void switchtoSelect() throws IOException {
        LoginApp.setRoot("game_selection");
    }
    @FXML
    private void switchToPrimary() throws IOException {
        LoginApp.setRoot("RegisterPage");
    }
}