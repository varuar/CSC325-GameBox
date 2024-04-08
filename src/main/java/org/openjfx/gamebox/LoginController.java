package org.openjfx.gamebox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField RegisterEmailField;

    @FXML
    private PasswordField RegisterPasswordField;

    @FXML
    private TextField RegisterUsernameField;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

    @FXML
    private TextField usernameField;

    @FXML
    protected void handleLoginAction(ActionEvent event) {
        // Simulate a login process
        String username = usernameField.getText();
        String password = passwordField.getText();

        if ("admin".equals(username) && "password".equals(password)) {
            System.out.println("Login successful!");
            try {
                // Load the second screen
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/openjfx/gamebox/game_selection.fxml"));
                Parent root = loader.load();

                // Get the current stage (window) from any control, e.g., the login button
                Stage stage = (Stage) loginButton.getScene().getWindow();

                // Set the scene with the second screen on the current stage
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Login failed!");
            // Here, you might want to show an error message to the user
        }
    }

    @FXML
    protected void handleRegisterAction(ActionEvent event) {
        // Your existing registration logic
    }
}