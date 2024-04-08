package org.openjfx.gamebox;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ChessGameController {

    @FXML
    private Label titleLabel;

    public void initialize() {
        // Set title text
        titleLabel.setText("Chess Game");
    }
}
