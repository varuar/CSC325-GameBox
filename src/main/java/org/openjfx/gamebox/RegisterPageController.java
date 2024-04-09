package org.openjfx.gamebox;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

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
        UserRecord userRecord;
        try {
            userRecord = LoginApp.fauth.createUser(request);
            System.out.println("Successfully created new user with Firebase Uid: " + userRecord.getUid()
                    + " check Firebase > Authentication > Users tab");
            System.out.println(userRecord.getUid());
            return true;

        } catch (FirebaseAuthException ex) {
            ex.printStackTrace();
            System.out.println("Error creating a new user in the firebase");
            System.out.println(registeremailTextField.getText());
            System.out.println(registerpasswordTextField.getText());
            System.out.println(registerphoneTextField.getText());
            System.out.println(registernameTextField.getText());
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
