//https://www.youtube.com/watch?v=FVo1fm52hz0&ab_channel=AlmasBaimagambetov
package org.openjfx.gamebox;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpaceInvadersApplication extends LoginApp{

    private Pane root = new Pane();
    private Pane ui = new Pane();

    private Pane stackPane = new Pane(root, ui);

    private double t = 0;

    private boolean moveLeft = false;
    private boolean moveRight = false;

    private boolean gamePaused = false;

    private boolean gameOver = false;

    private boolean darkMode = true;

    private static final long FIRE_RATE = 200;

    private long lastShotTime = 0;

    private Sprite player = new Sprite(300, 750, 40, 40, "player", Color.BLUE);

    private int score = 0;

    private int enemiesRemaining = 5;

    Label darkLightLabel = new Label("Dark Mode / Light Mode: Press r");
    Label gameStatus = new Label("Score: " + score);

    private Parent createContent(){
        root.setPrefSize(600, 800);
        ui.setPrefSize(600, 800);
        root.getChildren().add(player);
        ui.getChildren().add(darkLightLabel);
        ui.getChildren().add(gameStatus);
        darkLightLabel.setLayoutX(10);
        darkLightLabel.setLayoutY(10);
        gameStatus.setLayoutX(250);
        gameStatus.setLayoutY(10);
        /*if (darkMode) {
            BackgroundFill darkBackgroundFill = new BackgroundFill(Color.BLACK, null, null);
            Background darkBackground = new Background(darkBackgroundFill);
            root.setBackground(darkBackground);
        }
        else{
            BackgroundFill darkBackgroundFill = new BackgroundFill(Color.WHITE, null, null);
            Background darkBackground = new Background(darkBackgroundFill);
            root.setBackground(darkBackground);
        }*/

        AnimationTimer timer = new AnimationTimer(){
            @Override
            public void handle(long now){
                update();
            }
        };

        timer.start();

        firstLevel();

        return stackPane;
    }

    private void firstLevel(){
        for (int i = 0; i < 5; i++){
            Sprite s = new Sprite(90 + i*100, 25, 30, 30, "enemy", Color.RED);


            root.getChildren().add(s);
        }
    }

    private List<Sprite> sprites(){
        return root.getChildren().stream().map(n -> (Sprite)n).collect(Collectors.toList());
    }

    private void update(){
        if (darkMode) {
            BackgroundFill darkBackgroundFill = new BackgroundFill(Color.BLACK, null, null);
            Background darkBackground = new Background(darkBackgroundFill);
            root.setBackground(darkBackground);
        }
        else{
            BackgroundFill darkBackgroundFill = new BackgroundFill(Color.WHITE, null, null);
            Background darkBackground = new Background(darkBackgroundFill);
            root.setBackground(darkBackground);
        }
        if(!gamePaused){

            t += 0.016;

            if (!gameOver){gameStatus.setText("Score: " + score);}

            if (moveLeft){
                player.moveLeft();
                moveLeft = false;
            }
            if (moveRight){
                player.moveRight();
                moveRight = false;
            }

            sprites().forEach(s -> {
                switch (s.type) {
                    case "enemybullet":
                        s.moveDown();

                        if (s.getBoundsInParent().intersects(player.getBoundsInParent())){
                            player.dead = true;
                            gameOver(false);
                            s.dead = true;
                        }
                        if(gameOver){
                            s.dead = true;
                        }
                        break;
                    case "playerbullet":
                        s.moveUp();

                        sprites().stream().filter(e -> e.type.equals("enemy")).forEach(enemy -> {
                            if (s.getBoundsInParent().intersects(enemy.getBoundsInParent())){
                                enemy.dead = true;
                                s.dead = true;
                                enemiesRemaining -= 1;
                                score += 100;
                                if(enemiesRemaining == 0){gameOver(true);}
                            }
                        });
                        sprites().stream().filter(e -> e.type.equals("enemybullet")).forEach(enemy -> {
                            if (s.getBoundsInParent().intersects(enemy.getBoundsInParent())){
                                enemy.dead = true;
                                s.dead = true;
                                score += 50;
                            }
                        });
                        if(gameOver){
                            s.dead = true;
                        }

                        break;
                    case "enemy":
                        if (t > 2){
                            if (Math.random() < 0.3){
                                shoot(s);
                            }
                        }
                        if (t > 0 && t < 0.1){
                            s.moveRight();
                        }
                        if (t > 0.2 && t < 0.3){
                            s.moveDown();
                        }
                        if (t > 0.7 && t < 0.8){
                            s.moveLeft();
                        }
                        if (t > 0.9 && t < 1){
                            s.moveDown();
                        }
                        if (s.getTranslateY() > 770){
                            gameOver(false);
                            s.dead = true;
                        }
                        if(gameOver){
                            s.dead = true;
                        }
                        break;


                }
            });

            root.getChildren().removeIf(n -> {
                Sprite s = (Sprite) n;
                return s.dead;
            });

            if (t > 2){
                t = 0;
            }

        }
    }

    private void pauseGame(){
        if (gamePaused){
            //root.getChildren().add(pauseImageView);
        }
        else{
            //root.getChildren().remove(pauseImageView);
        }
        gamePaused = !gamePaused;
    }

    private void restartGame(){
        gameOver = false;
        player.dead = false;
        score = 0;
        enemiesRemaining = 5;
        root.getChildren().remove(player);
        firstLevel();
        root.getChildren().add(player);
    }

    private void gameOver(boolean win){
        if (!win){
            gameStatus.setText("You Lose! Score: " + score + " Try again: press ESC");
            System.out.println("Loser");
            //gamePaused = true;
            gameOver = true;
            //gameStatus.setText("You Lose! Score: " + score + " Try again: press ESC");
        }
        else{
            gameStatus.setText("You Win! Score: " + score + " Try again: press ESC");
            System.out.println("Winner");
            //gamePaused = true;
            gameOver = true;
            //gameStatus.setText("You Win! Score: " + score + " Try again: press ESC");
        }
    }

    private void shoot(Sprite who){
        /*if (darkMode){
            Sprite s = new Sprite((int)who.getTranslateX() + 20,(int) who.getTranslateY(), 5, 20,who.type + "bullet", Color.WHITE);
            root.getChildren().add(s);
        }
        else{
            Sprite t = new Sprite((int)who.getTranslateX() + 20,(int) who.getTranslateY(), 5, 20,who.type + "bullet", Color.BLACK);
            root.getChildren().add(t);
        }*/
        Sprite s = new Sprite((int)who.getTranslateX() + 20,(int) who.getTranslateY(), 5, 20,who.type + "bullet", Color.DARKGREY);
        root.getChildren().add(s);
    }

    private boolean canShoot() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastShotTime) >= FIRE_RATE;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SpaceInvadersApplication.class.getResource("/org/openjfx/demo5/spaceinvaders_game.fxml"));
        Scene scene = new Scene(createContent());//fxmlLoader.load(), 320, 240);

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()){
                case A:
                    //player.moveLeft();
                    moveLeft = true;
                    break;
                case D:
                    //player.moveRight();
                    moveRight = true;
                    break;
                case R:
                    darkMode = !darkMode;
                    break;
                case ESCAPE:
                    if (!gameOver){pauseGame();}
                    else{restartGame();}
                    break;
                case SPACE:
                    if (!player.dead && canShoot() && !gamePaused){
                        shoot(player);
                        lastShotTime = System.currentTimeMillis();
                    }
                    break;
            }
        });

        stage.setTitle("Space Invaders!");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    private static class Sprite extends Rectangle {
        boolean dead = false;
        final String type;

        Sprite(int x, int y, int w, int h, String type, Color color){
            super(w, h, color);
            this.type = type;
            setTranslateX(x);
            setTranslateY(y);
        }

        void moveLeft(){
            setTranslateX(getTranslateX() - 5);
        }

        void moveRight(){
            setTranslateX(getTranslateX() + 5);
        }

        void moveUp(){
            setTranslateY(getTranslateY() - 5);
        }

        void moveDown(){
            setTranslateY(getTranslateY() + 5);
        }
    }

    public Pane getRootPane() {
        return root; // This assumes that your root pane is properly initialized
    }
    public static void main(String[] args) {
        launch();
    }
}
