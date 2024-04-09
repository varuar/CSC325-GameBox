package org.openjfx.gamebox;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void handleLoginAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if ("admin".equals(username) && "password".equals(password)) {
            System.out.println("Login successful!");
            // Switch to the game selection screen
            Stage stage = (Stage) usernameField.getScene().getWindow();
            LoginApp.switchToGameSelectionScreenOnLogin(stage);
        } else {
            System.out.println("Login failed!");
        }
    }

    @FXML
    protected void handleRegisterAction() {
        System.out.println("Register button clicked!");
    }
}
