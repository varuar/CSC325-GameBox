package org.openjfx.gamebox;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuController {
    @FXML
    private VBox root;

    public void play() {
        Stage stage = (Stage) root.getScene().getWindow();
        BoardGrid board = new BoardGrid(stage);
        Scene scene = new Scene(board);
        stage.setScene(scene);
    }

    public void getConsole() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    public void logout() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
        System.exit(0);
    }
}
