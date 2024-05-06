package org.openjfx.gamebox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.net.URL;

public class GameSelectionController {

    @FXML
    private Button game1Button;

    @FXML
    private Button game2Button;

    @FXML
    private Button game3Button;

    @FXML
    private Button game4Button;

    @FXML
    protected void initialize() {
        game1Button.setText("Chess");
        game2Button.setText("Tile Game");
        game3Button.setText("Space Invaders");
        game4Button.setText("Swimming Mania");
    }

    @FXML
    protected void handleGameSelection(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();
        String gameName = clickedButton.getText();

        switch (gameName) {
            case "Chess":
                loadChessGame();
                break;
            case "Tile Game":
                loadGameScreen("tile_game.fxml", "Tile Game");
                break;
            case "Space Invaders":
                loadSpaceInvadersGame("Space Invaders Game");
                break;
            case "Swimming Mania":
                loadGameScreen("swimmingmania_game.fxml", "Swimming Mania Game");
                break;
            default:
                System.out.println("Invalid game selection");
        }
    }
    private void loadSpaceInvadersGame(String gameTitle) throws IOException {
        SpaceInvadersApplication spaceInvadersApp = new SpaceInvadersApplication();
        Stage stage = new Stage();
        spaceInvadersApp.start(stage);
        stage.setTitle(gameTitle);
        stage.show();
    }

    private void loadChessGame() throws IOException {

        Stage stage = new Stage();
        stage.setTitle("Chess");
        stage.setResizable(false);
        URL fxmlLocation = getClass().getResource("Menu.fxml");
        FXMLLoader root = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(root.load());
        stage.setScene(scene);
        stage.show();


    }

    private void loadGameScreen(String fxmlFileName, String gameTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(gameTitle);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}