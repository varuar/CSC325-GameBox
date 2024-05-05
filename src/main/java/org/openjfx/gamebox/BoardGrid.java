package org.openjfx.gamebox;

import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class BoardGrid extends GridPane {

    private ImageView backgroundImageView;
    private Model model;
    private double startX;
    private double startY;
    private final double  SQUARE_SIZE = 64;
    Stage stage;

    public BoardGrid(Stage stage) {
        this.stage = stage;
        initModel();
        displayBackground();
    }

    private void displayBackground() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i % 2 == 0 && j % 2 != 0 || i % 2 != 0 && j % 2 == 0) {
                    backgroundImageView = new ImageView(getImage("bgGreen2"));
                } else {
                    backgroundImageView = new ImageView(getImage("bgGreen1"));
                }
                Piece piece = model.getPiece(new Position(j, i));
                if (piece == null) {
                    this.add(backgroundImageView, i, 7 - j);
                } else {
                    StackPane stackPane = new StackPane();
                    ImageView pieceImageView = new ImageView(getImage(piece.getName()));
                    stackPane.getChildren().addAll(backgroundImageView, pieceImageView);
                    makeDraggable(pieceImageView, new Position(j, i));
                    this.add(stackPane, i, 7 - j);
                }
            }
        }
    }

    private Image getImage(String str) {
        switch (str) {
            case "bgGreen1" -> str = "img/boardGreen1.png";
            case "bgGreen2" -> str = "img/boardGreen2.png";
            case "BLACKRook" -> str = "img/blackRook.png";
            case "WHITERook" -> str = "img/whiteRook.png";
            case "BLACKBishop" -> str = "img/blackBishop.png";
            case "WHITEBishop" -> str = "img/whiteBishop.png";
            case "BLACKKing" -> str = "img/blackKing.png";
            case "WHITEKing" -> str = "img/whiteKing.png";
            case "BLACKQueen" -> str = "img/blackQueen.png";
            case "WHITEQueen" -> str = "img/whiteQueen.png";
            case "BLACKPawn" -> str = "img/blackPawn.png";
            case "WHITEPawn" -> str = "img/whitePawn.png";
            case "BLACKKnight" -> str = "img/blackKnight.png";
            case "WHITEKnight"-> str = "img/whiteKnight.png";
        }
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(str)),SQUARE_SIZE,SQUARE_SIZE,true,true);
    }

    private void initModel() {
        model = new Game();
        model.start();
    }

    private void makeDraggable(Node node, Position oldPos){
        if(model.getState() != GameState.CHECK_MATE && model.getState() != GameState.STALE_MATE) {
            node.setOnMouseEntered(e -> {
                node.setCursor(Cursor.CLOSED_HAND);
            });
            node.setOnMousePressed(e -> {
                startX = e.getSceneX() - node.getTranslateX();
                startY = e.getSceneY() - node.getTranslateY();
                if (node.getParent() != null) {
                    node.getParent().toFront();
                }
            });
            node.setOnMouseDragged(e -> {
                node.setTranslateX(e.getSceneX() - startX);
                node.setTranslateY(e.getSceneY() - startY);
                stage.getScene().setCursor(Cursor.CLOSED_HAND);
            });
            node.setOnMouseReleased(e -> {
                stage.getScene().setCursor(Cursor.DEFAULT);
                if (hasMoved(-node.getTranslateY(), node.getTranslateX())) {
                    Position newPos = oldPos;
                    double transalationY = -node.getTranslateY();
                    double transalationX = node.getTranslateX();
                    if (transalationY >= SQUARE_SIZE / 2.0645) { // UP
                        while (transalationY >= SQUARE_SIZE / 2.0645) {
                            newPos = newPos.next(Direction.N);
                            transalationY = transalationY - SQUARE_SIZE;
                        }
                    } else if (transalationY <= -SQUARE_SIZE / 2.37037) { // DOWN
                        while (transalationY <= -SQUARE_SIZE / 2.37037) {
                            newPos = newPos.next(Direction.S);
                            transalationY = transalationY + SQUARE_SIZE;
                        }
                    }
                    if (transalationX >= SQUARE_SIZE / 2.112) { // RIGHT
                        while (transalationX >= SQUARE_SIZE / 2.112) {
                            newPos = newPos.next(Direction.E);
                            transalationX = transalationX - SQUARE_SIZE;
                        }
                    } else if (transalationX <= -SQUARE_SIZE / 1.4545) { // LEFT
                        while (transalationX <= -SQUARE_SIZE / 1.4545) {
                            newPos = newPos.next(Direction.W);
                            transalationX = transalationX + SQUARE_SIZE;
                        }
                    }
                    try {
                        model.movePiecePosition(oldPos, newPos);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                displayBackground();
                if (model.getState() == GameState.CHECK_MATE || model.getState() == GameState.STALE_MATE) {
                    URL FxmlLocation = getClass().getResource("Result.fxml");
                    FXMLLoader loader = new FXMLLoader(FxmlLocation);
                    try {
                        Stage secondaryStage = new Stage();
                        stage.setResizable(false);
                        Scene scene = new Scene(loader.load());
                        secondaryStage.setScene(scene);
                        secondaryStage.show();
                        ResultController controller = loader.getController();
                        controller.getPrimaryStage(stage, model.getCurrentPlayer().toString());
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            });
        }
    }

    private boolean hasMoved(double y,double x){
        return y <= -SQUARE_SIZE/2.37037 || y >= SQUARE_SIZE/2.0645 || x >= SQUARE_SIZE/2.112 || x <= -SQUARE_SIZE/2.112;
    }
}
