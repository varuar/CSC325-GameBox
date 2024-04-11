package org.openjfx.gamebox;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TileGameApp extends LoginApp{

    private static final int SIZE = 4;
    private Button[][] tiles = new Button[SIZE][SIZE];

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Button tile = new Button(" ");
                tile.setPrefSize(100, 100);
                int finalI = i;
                int finalJ = j;
                tile.setOnAction(e -> tileClicked(finalI, finalJ));
                tiles[i][j] = tile;
                grid.add(tile, j, i);
            }
        }

        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setTitle("4x4 Tile Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void tileClicked(int row, int col) {
        System.out.println("Tile clicked: " + row + "," + col);
        // Implement your move logic here
    }
}
