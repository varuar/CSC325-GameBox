package org.openjfx.gamebox;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void handleLoginAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Implement your login logic here
        if ("admin".equals(username) && "password".equals(password)) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Login failed!");
        }
    }

    @FXML
    protected void handleRegisterAction() {
        // Implement your register logic here
        System.out.println("Register button clicked!");
    }
}