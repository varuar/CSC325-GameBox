package org.openjfx.gamebox;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;


public class FxView extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Chess");
        stage.setResizable(false);
        URL fxmlLocation = getClass().getResource("Menu.fxml");
        FXMLLoader root = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(root.load());
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
          System.exit(0);
        });
    }

}
