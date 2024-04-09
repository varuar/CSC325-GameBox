package org.openjfx.gamebox;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/org/openjfx/gamebox/login_page.fxml"));
        primaryStage.setTitle("Login Page");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void switchToGameSelectionScreen(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(LoginApp.class.getResource("/org/openjfx/gamebox/game_selection.fxml"));
            primaryStage.setScene(new Scene(root, 600, 600));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void switchToGameSelectionScreenOnLogin(Stage primaryStage) {
        switchToGameSelectionScreen(primaryStage);
    }
}
