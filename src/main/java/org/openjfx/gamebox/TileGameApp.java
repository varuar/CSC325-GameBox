package org.openjfx.gamebox;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TileGameApp extends LoginApp {
    // Label to display timer countdown
    @FXML
    private Label timerLabel;

    private int remainingTime = 10;  // Timer for game countdown
    private ScoreCollectionController scoreCollector;
    private Timeline gameTimer;  // Timer for managing game countdown

    // Buttons for the game tiles, organized by rows and columns
    @FXML
    private Button r1c1, r1c2, r1c3, r1c4;
    @FXML
    private Button r2c1, r2c2, r2c3, r2c4;
    @FXML
    private Button r3c1, r3c2, r3c3, r3c4;
    @FXML
    private Button r4c1, r4c2, r4c3, r4c4;
    @FXML
    private Button r5c1, r5c2, r5c3, r5c4;

    @FXML
    private BorderPane rootPane;

    // Grid to track visibility of tiles
    private Boolean[][] tileVisibility;
    private int SelectedTileRow = -1;
    private int SelectedTileCol = -1;
    private boolean gameEnded = false;

    // Array of buttons for easier access
    private Button[][] buttons;

    // Array representing valid paths in the game grid
    private int[][] pathGrid;


    public TileGameApp() {
    }

    private void setupTimer() {
        gameTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (remainingTime == 0) {
                gameTimer.stop();
                showResult("Time Out! Play Again");
            } else {
                remainingTime--;
                timerLabel.setText(remainingTime + " Sec");
            }
        }));
        gameTimer.setCycleCount(Timeline.INDEFINITE);
        gameTimer.play();
    }

    private void showResult(String message) {
        gameTimer.stop();

        // Update the score in Firestore only once per game end
        if (!gameEnded) {
            int finalTime = 10 - remainingTime;
            scoreCollector.updateGameScore("TileGameBestTime", finalTime);
            gameEnded = true;
        }


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tile_finish.fxml"));
            Parent summaryRoot = loader.load();
            Tile_result controller = (Tile_result) loader.getController();
            controller.setTitle(message);

            Scene currentScene = rootPane.getScene();
            if (currentScene != null) {
                Stage stage = (Stage) currentScene.getWindow();
                if (stage != null) {
                    stage.setScene(new Scene(summaryRoot, 1080.0, 620.0));
                    stage.show();
                } else {
                    System.err.println("Stage is null");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Handle mouse clicks on game tiles
    private void handleMouseClick(MouseEvent e, int finalI, int finalJ) {
        // Checking if the tile clicked is adjacent to the last selected tile
        if ((this.SelectedTileRow + 1 == finalI || this.SelectedTileRow - 1 == finalI) && this.SelectedTileCol == finalJ ||
                this.SelectedTileRow == finalI && (this.SelectedTileCol + 1 == finalJ || this.SelectedTileCol - 1 == finalJ) ||
                this.SelectedTileRow == -1 && finalI == 4) {

            if (finalI == 0) {
                this.showResult("Congratulations! You Won the Game");
            }

            int prevSelectedRow = this.SelectedTileRow;
            int prevSelectedCol = this.SelectedTileCol;
            this.SelectedTileRow = finalI;
            this.SelectedTileCol = finalJ;

            // Check for tile visibility to determine game over conditions
            if (!this.tileVisibility[this.SelectedTileRow][this.SelectedTileCol]) {
                this.showResult("Oh No, You Fell Down. Play Again");
            }

            // Handle tile style reset when moving from one tile to another
            if (prevSelectedRow != -1) {
                this.buttons[prevSelectedRow][prevSelectedCol].fireEvent(new MouseEvent(MouseEvent.MOUSE_EXITED, 0.0, 0.0, 0.0, 0.0, MouseButton.NONE, 0, false, false, false, false, false, false, false, false, false, false, (PickResult)null));
            }
        }
    }

    // Method to check if an array contains a specific number
    private boolean containsNumber(int[] array, int target) {
        for(int num : array) {
            if (num == target) {
                return true;
            }
        }
        return false;
    }

    // Add mouse event listeners to all buttons (game tiles)
    private void addButtonsEventListener() {
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 4; j++) {
                final int fi = i;
                final int fj = j;
                this.buttons[fi][fj].setOnMouseEntered((e) -> {
                    // Set tile style when mouse enters
                    if (fi == 0) {
                        this.buttons[fi][fj].setStyle("-fx-background-color: #1e1e94; -fx-border-radius: 10; -fx-border-color: #1e1e94; -fx-background-radius: 10");
                    } else {
                        this.buttons[fi][fj].setStyle("-fx-background-color: #ec9703; -fx-border-radius: 10; -fx-border-color: #ec9703; -fx-background-radius: 10");
                    }
                });
                this.buttons[fi][fj].setOnMouseExited((e) -> {
                    // Reset tile style when mouse exits, unless it's the selected tile
                    if (fi != this.SelectedTileRow || fj != this.SelectedTileCol) {
                        if (fi == 0) {
                            this.buttons[fi][fj].setStyle("-fx-background-color: white; -fx-border-radius: 10; -fx-border-color: #1e1e94; -fx-background-radius: 10");
                        } else {
                            this.buttons[fi][fj].setStyle("-fx-background-color: white; -fx-border-radius: 10; -fx-border-color: #ec9703; -fx-background-radius: 10");
                        }
                    }
                });
                this.buttons[fi][fj].setOnMouseClicked((event) -> {
                    this.handleMouseClick(event, fi, fj);
                });

                // Setup disappearing tiles with a FadeTransition
                if (fi > 0 && !this.containsNumber(this.pathGrid[fi], fj)) {
                    final FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1.0), this.buttons[fi][fj]);
                    fadeOutTransition.setFromValue(1.0);
                    fadeOutTransition.setToValue(0.0);
                    Timeline t = new Timeline(new KeyFrame(Duration.seconds((Math.random() + 0.2) * 10.0), (event) -> {
                        TileGameApp.this.tileVisibility[fi][fj] = false;
                        fadeOutTransition.play();
                        if (TileGameApp.this.SelectedTileRow == fi && TileGameApp.this.SelectedTileCol == fj) {
                            TileGameApp.this.showResult("You Stepped On Disappearing Tile! Play Again");
                        }
                    }));
                    t.play();
                }
            }
        }
    }

    // Initialize the game components and start the game timer
    public void initialize() {
        this.buttons = new Button[][]{{this.r1c1, this.r1c2, this.r1c3, this.r1c4}, {this.r2c1, this.r2c2, this.r2c3, this.r2c4}, {this.r3c1, this.r3c2, this.r3c3, this.r3c4}, {this.r4c1, this.r4c2, this.r4c3, this.r4c4}, {this.r5c1, this.r5c2, this.r5c3, this.r5c4}};
        this.tileVisibility = new Boolean[][]{{true, true, true, true}, {true, true, true, true}, {true, true, true, true}, {true, true, true, true}, {true, true, true, true}};
        this.pathGrid = new int[][]{{1}, {1}, {1}, {1, 2}, {2}};
        this.setupTimer();
        this.addButtonsEventListener();
        scoreCollector = new ScoreCollectionController();

    }
}