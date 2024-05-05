package org.openjfx.gamebox;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class SwimmingManiaController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private ImageView carImage;

    @FXML
    private Label timerLabel;

    @FXML
    private Label leaderboardLabel;

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button restartButton;

    @FXML
    private Button resumeButton;

    @FXML
    private Button endGameButton;

    @FXML
    private Button leaderboardButton;

    private boolean gameStarted = false;
    private long lastBackgroundUpdateTime = 0L;
    private int timeSeconds = 0;
    private int timeMsec = 0;
    private int bestTimeSeconds = Integer.MAX_VALUE;
    private Image race1Image;
    private Image race2Image;
    private boolean toggleBackground = false;
    private Timeline gameLoop;
    private final String BEST_TIME_FILE = "best_time.txt";
    private ScoreCollectionController scoreCollector;
    private List<ImageView> obstacles = new ArrayList<>();

    public void initialize() {
        carImage.setImage(new Image(getClass().getResourceAsStream("/org/openjfx/gamebox/carimages/car_mask.png")));
        race1Image = new Image(getClass().getResourceAsStream("/org/openjfx/gamebox/carimages/race1.png"));
        race2Image = new Image(getClass().getResourceAsStream("/org/openjfx/gamebox/carimages/race2.png"));
        backgroundImage.setImage(race1Image);
        loadBestTime();
        scoreCollector = new ScoreCollectionController();
    }

    private void loadBestTime() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(BEST_TIME_FILE));
            String line = reader.readLine();
            if (line != null) {
                bestTimeSeconds = Integer.parseInt(line);
                updateLeaderboard();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBestTime() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(BEST_TIME_FILE));
            writer.write(Integer.toString(bestTimeSeconds));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startGameLoop() {
        gameLoop = new Timeline(
                new KeyFrame(Duration.seconds(0.1), event -> {
                    moveBackground();
                    updateTime();
                    if (Math.random() < 0.05) { // Adjust the probability as needed
                        generateRandomObstacle();
                    }
                    moveObstacles(); // Move the obstacles
                })
        );
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    private void moveBackground() {
        toggleBackground = !toggleBackground;
        backgroundImage.setImage(toggleBackground ? race2Image : race1Image);
    }

    private void updateTime() {
        long now = System.nanoTime();
        if (lastBackgroundUpdateTime != 0L) {
            long elapsedTime = now - lastBackgroundUpdateTime;
            timeMsec += elapsedTime / 1_000_000;
            if (timeMsec >= 1000) {
                timeSeconds++;
                timeMsec = 0;
            }
            timerLabel.setText(timeSeconds / 60 + ":" + String.format("%02d", timeSeconds % 60));
            updateBestTime();
        }
        lastBackgroundUpdateTime = now;
    }

    @FXML
    public void handleKeyPress(KeyEvent event) {
        if (gameStarted) {
            switch (event.getCode()) {
                case LEFT:
                case A:
                    moveCarLeft();
                    break;
                case RIGHT:
                case D:
                    moveCarRight();
                    break;
                case UP:
                case W:
                    moveCarUp();
                    break;
                case DOWN:
                case S:
                    moveCarDown();
                    break;
            }
        }
    }

    private void moveCarLeft() {
        if (carImage.getLayoutX() >= 185) {
            carImage.setLayoutX(carImage.getLayoutX() - 3);
        }
    }

    private void moveCarRight() {
        if (carImage.getLayoutX() <= 520) {
            carImage.setLayoutX(carImage.getLayoutX() + 3);
        }
    }

    private void moveCarUp() {
        if (carImage.getLayoutY() >= 0) {
            carImage.setLayoutY(carImage.getLayoutY() - 3);
        }
    }

    private void moveCarDown() {
        if (carImage.getLayoutY() <= 445) {
            carImage.setLayoutY(carImage.getLayoutY() + 3);
        }
    }

    private void moveObstacles() {
        for (ImageView obstacle : obstacles) {
            obstacle.setLayoutY(obstacle.getLayoutY() + 5); // Adjust speed as needed
            if (obstacle.getLayoutY() > 530) {
                // Remove obstacle if it goes beyond the game window
                rootPane.getChildren().remove(obstacle);
                obstacles.remove(obstacle);
                break;
            }
            // Check for collisions with the car
            if (obstacle.getBoundsInParent().intersects(carImage.getBoundsInParent())) {
                endGame();
                break;
            }
        }
    }

    private void generateRandomObstacle() {
        double minX = 180;
        double maxX = 450;
        double randomX = minX + Math.random() * (maxX - minX);
        double minY = -100; // Minimum Y-coordinate for spawning
        double maxY = -50;  // Maximum Y-coordinate for spawning

        // Check for collision with existing obstacles
        boolean overlap = true;
        while (overlap) {
            overlap = false;
            randomX = minX + Math.random() * (maxX - minX);
            for (ImageView obstacle : obstacles) {
                double obstacleX = obstacle.getLayoutX();
                double obstacleY = obstacle.getLayoutY();
                if (randomX >= obstacleX - 50 && randomX <= obstacleX + 50 &&
                        maxY >= obstacleY - 50 && maxY <= obstacleY + 50) {
                    overlap = true;
                    break;
                }
            }
        }

        ImageView obstacle = new ImageView(new Image(getClass().getResourceAsStream("/org/openjfx/gamebox/carimages/car_mask.png")));
        obstacle.setLayoutX(randomX);
        obstacle.setLayoutY(minY);
        rootPane.getChildren().add(obstacle);
        obstacles.add(obstacle);
    }
    private void endGame() {

        gameStarted = false;
        gameLoop.stop();
        int finalTime = timeSeconds;

        if (scoreCollector != null) {
            scoreCollector.updateGameScore("SwimmingScore", finalTime);
        } else {
            System.err.println("Score collector is not initialized.");
        }
        startButton.setVisible(true);
        stopButton.setVisible(false);
        restartButton.setVisible(false);
        resumeButton.setVisible(false);
        endGameButton.setVisible(false);
        timerLabel.setText("0:00");
        leaderboardLabel.setText("Best Time: " + (finalTime));
        loadBestTime();
    }
    @FXML
    public void handlePlayButtonClick() {
        if (!gameStarted) {
            gameStarted = true;
            carImage.setLayoutX(350);
            carImage.setLayoutY(400);
            timeSeconds = 0; // Reset time to 0 when starting the game
            timeMsec = 0;
            startGameLoop();
            startButton.setVisible(false);
            stopButton.setVisible(true);
            restartButton.setVisible(true);
        }
    }

    @FXML
    public void handleStopButtonClick() {
        if (gameStarted) {
            gameStarted = false;
            gameLoop.stop();
            startButton.setVisible(false);
            stopButton.setVisible(false);
            restartButton.setVisible(false);
            resumeButton.setVisible(true);
            endGameButton.setVisible(true);
            saveBestTime();
            // Reset to start screen
            timerLabel.setText("0:00");
            leaderboardLabel.setText("Best Time: " + bestTimeSeconds / 60 + ":" + String.format("%02d", bestTimeSeconds % 60));
        }
    }

    @FXML
    public void handleRestartButtonClick() {
        if (!gameStarted) {
            timeSeconds = 0;
            timeMsec = 0;
            timerLabel.setText("0:00");
            carImage.setLayoutX(350);
            carImage.setLayoutY(400);
            if (gameLoop != null) {
                gameLoop.stop(); // Stop the current game loop if it exists
            }
            startGameLoop(); // Start a new game loop
            resumeButton.setVisible(false);
            endGameButton.setVisible(false);
        }
    }

    @FXML
    public void handleLeaderboardButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/openjfx/demo5/leaderboard.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Leaderboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleResumeButtonClick() {
        startButton.setVisible(false);
        stopButton.setVisible(true);
        restartButton.setVisible(true);
        resumeButton.setVisible(false);
        endGameButton.setVisible(false);
        gameStarted = true;
        startGameLoop();
    }

    @FXML
    public void handleEndGameButtonClick() {
        startButton.setVisible(true);
        stopButton.setVisible(false);
        restartButton.setVisible(false);
        resumeButton.setVisible(false);
        endGameButton.setVisible(false);
        gameStarted = false;
        saveBestTime();
        // Record the current timer value as the best time
        bestTimeSeconds = timeSeconds;
        updateLeaderboard();
        // Reset to start screen
        timerLabel.setText("0:00");
        leaderboardLabel.setText("Best Time: " + bestTimeSeconds / 60 + ":" + String.format("%02d", bestTimeSeconds % 60));
    }

    @FXML
    public void handleCloseButtonClick() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    private void updateBestTime() {
        if (timeSeconds < bestTimeSeconds) {
            bestTimeSeconds = timeSeconds;
            updateLeaderboard();
        }
    }

    private void updateLeaderboard() {
        leaderboardLabel.setText("Best Time: " + bestTimeSeconds / 60 + ":" + String.format("%02d", bestTimeSeconds % 60));
    }
}
    