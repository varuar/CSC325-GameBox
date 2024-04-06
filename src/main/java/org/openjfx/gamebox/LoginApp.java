package org.openjfx.gamebox;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FirestoreContext firestoreContext = new FirestoreContext();
        Parent root = FXMLLoader.load(getClass().getResource("/org/openjfx/gamebox/login_page.fxml"));
        primaryStage.setTitle("Login Page");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
