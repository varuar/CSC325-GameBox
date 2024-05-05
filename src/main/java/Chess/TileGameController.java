package Chess;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TileGameController {

    @FXML
    private Label titleLabel;

    public void initialize() {
        // Set title text
        titleLabel.setText("Tile Game");
    }
}
