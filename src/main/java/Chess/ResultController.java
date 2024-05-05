package Chess;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

public class ResultController {
    @FXML
    private VBox root;
    @FXML
    private Label victoryLabel;
    private Stage primaryStage;

    public void getPrimaryStage(Stage stage,String winnerName){
        this.primaryStage = stage;
        victoryLabel.setText("THE "+winnerName+" have won !");
    }

    public void rematch() {
        BoardGrid board = new BoardGrid(primaryStage);
        Scene scene = new Scene(board);
        primaryStage.setScene(scene);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    public void back() {
        Stage stage = (Stage) root.getScene().getWindow();
        URL FxmlLocation = getClass().getResource("Menu.fxml");
        FXMLLoader loader = new FXMLLoader(FxmlLocation);
        try {
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            stage.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
