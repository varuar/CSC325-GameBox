package org.openjfx.gamebox;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Tile_result {
    @FXML
    private Label titleLabel;
    @FXML
    private Button playAgain;
    @FXML
    private Button close;
    @FXML
    private BorderPane rootPane;

    public Tile_result() {
    }

    private void addButtonsEventListener() {
        Button[] btns = new Button[]{this.playAgain, this.close};

        for (int j = 0; j < 2; j++) {
            final int finalJ = j; // effectively final variable
            btns[j].setOnMouseEntered((e) -> {
                btns[finalJ].setStyle("-fx-background-color: #1e1e94; -fx-border-radius: 10; -fx-border-color: #1e1e94; -fx-background-radius: 10");
            });
            btns[j].setOnMouseExited((e) -> {
                btns[finalJ].setStyle("-fx-background-color: white; -fx-border-radius: 10; -fx-border-color: #1e1e94; -fx-background-radius: 10");
            });
        }
    }

    public void initialize() {
        this.addButtonsEventListener();
    }

    public void setTitle(String message) {
        this.titleLabel.setText(message);
    }

    public void showPlayGame() {
        try {
            FXMLLoader loader = new FXMLLoader(TileGameApp.class.getResource("tile_game.fxml"));
            Parent summaryRoot = (Parent)loader.load();
            Stage stage = (Stage)this.rootPane.getScene().getWindow();
            Scene scene = new Scene(summaryRoot, 1080.0, 620.0);
            stage.setScene(scene);
            stage.show();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    public void exit() {
        Stage stage = (Stage)this.rootPane.getScene().getWindow();
        stage.close();
    }
}