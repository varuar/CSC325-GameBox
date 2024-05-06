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
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import java.util.Objects;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.net.URL;
import javafx.scene.layout.VBox;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;




import java.net.URL;
import java.util.*;
import java.util.ArrayList;
import java.util.List;





class Bishop extends Piece {
    public static final String BLACK_BOLD = "\033[1;30m";


    public Bishop(Color color){
        super(color);
    }

    public String getName(){
        return color + "Bishop";
    }


    @Override
    public List<Position> getPossibleMoves(Position position, Board board) {
        List<Position> deplacement = new ArrayList();

        diagonal(board, position, Direction.NW, deplacement);
        diagonal(board, position, Direction.NE, deplacement);
        diagonal(board, position, Direction.SW, deplacement);
        diagonal(board, position, Direction.SE, deplacement);

        return deplacement;
    }

    @Override
    public String toString() {
        return color == Color.BLACK ? BLACK_BOLD+"♝" : "♝";
    }
}



class Board {

    private Square[][] squares;


    public Board() {
        squares = new Square[8][8];
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares.length; j++) {
                squares[i][j] = new Square(null);
            }
        }
    }


    public Square[][] getSquares() {
        return squares;
    }


    public boolean contains(Position pos) {
        Integer row = pos.getRow();
        Integer column = pos.getColumn();

        return !(row < 0 || row > squares.length-1 || column < 0 || column > squares.length-1);
    }


    public void setPiece(Piece piece, Position position) {
        if (!contains(position)) {
//            throw new IllegalArgumentException("The given position is not on the board.");
//                Alert a = new Alert(AlertType.NONE);
//                a.setAlertType(AlertType.INFORMATION);
//                a.setContentText("The given position is on 1 the board.");
//                a.show();

            throw new IllegalArgumentException("The given position is not on the board.");
        }
        Integer row = position.getRow();
        Integer column = position.getColumn();

        squares[column][row].setPiece(piece);
    }


    public Piece getPiece(Position pos) {
        if (!contains(pos)) {
//            throw new IllegalArgumentException("the given position is not on the board");
//                Alert a = new Alert(AlertType.NONE);
//                a.setAlertType(AlertType.INFORMATION);
//                a.setContentText("The given position is on 2 the board.");
//                a.show();

            throw new IllegalArgumentException("the given position is not on the board");
        }
        Integer row = pos.getRow();
        Integer column = pos.getColumn();

        return squares[column][row].getPiece();
    }


    public Integer getInitialPawnRow(Color color) {
        return color == Color.BLACK ? 6 : 1;
    }


    public void dropPiece(Position pos) {
        if (!contains(pos)) {
//            throw new IllegalArgumentException("The given position is not on the board");
//                Alert a = new Alert(AlertType.NONE);
//                a.setAlertType(AlertType.INFORMATION);
//                a.setContentText("The given position is on 3 the board.");
//                a.show();

            throw new IllegalArgumentException("The given position is not on the board");
        }
        setPiece(null, pos);
    }


    public boolean isFree(Position pos) {
        if (!contains(pos)) {
//            throw new IllegalArgumentException("The given position is not on the board");
//                Alert a = new Alert(AlertType.NONE);
//                a.setAlertType(AlertType.INFORMATION);
//                a.setContentText("The given position is on 4 the board.");
//                a.show();

            throw new IllegalArgumentException("The given position is not on the board");
        }
        Integer row = pos.getRow();
        Integer column = pos.getColumn();

        return squares[column][row].isFree();
    }


    public boolean containsOppositeColor(Position pos, Color col) {
        if (!contains(pos)) {
//            throw new IllegalArgumentException("The given position is not on the board");
//                Alert a = new Alert(AlertType.NONE);
//                a.setAlertType(AlertType.INFORMATION);
//                a.setContentText("The given position is on 5 the board");
//                a.show();

            throw new IllegalArgumentException("The given position is not on the board");
        }
        if (isFree(pos)) {
            return false;
        }
        return this.getPiece(pos).getColor() != col;
    }


    public List<Position> getPositionOccupiedBy(Player player) {
        List<Position> nbPieces = new ArrayList();

        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                Position pos = new Position(j,i);

                if (!isFree(pos)) {

                    if (player.getColor() == getPiece(pos).getColor()) {
                        nbPieces.add(new Position(j, i));
                    }
                }
            }
        }
        return nbPieces;
    }


    public Position getPiecePosition (Piece piece){
        for(int i = 0; i<squares.length; i++){
            for(int j = 0;j<squares[i].length;j++){
                Position pos = new Position(j,i);
                if(getPiece(pos) == piece){
                    return pos;
                }
            }
        }
        return null;
    }

}

class BoardGrid extends GridPane {

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

enum Color {
    WHITE, BLACK;


    public Color opposite(){
        return this == BLACK ? WHITE : BLACK;
    }
}

class Controller {

    private View view;
    private Model model;


    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }


    public void play() {
        Model game = new Game();
        View view = new TextView(game);

        boolean gameIsOver = false;

        view.displayTitle();

        game.start();

        boolean goodPosition = false;

        Position départ = null;
        Position arrivé;

        while (!gameIsOver) {
            view.displayPlayer();
            view.displayState(game.getState());
            view.displayBoard();

            while (!goodPosition) {
                départ = view.askPosition();
                try {

                    if (game.isCurrentPlayerPosition(départ)) {

                        if (game.getPossibleMoves(départ).isEmpty()) {
                            view.displayError("Error.");
                        } else {
                            goodPosition = true;
                        }
                    } else {
                        view.displayError("Error.");
                    }
                } catch (Exception e) {
                    view.displayError(e.getMessage());
                }
            }
            goodPosition = false;
            while (!goodPosition) {
                view.displayMessage("Where do you want to go ?");
                arrivé = view.askPosition();
                try {

                    game.movePiecePosition(départ, arrivé);

                    goodPosition = true;
                } catch (Exception e) {
                    view.displayError(e.getMessage());
                    goodPosition = true;
                }
            }
            goodPosition = false;
            gameIsOver = game.getState() == GameState.CHECK_MATE || game.getState() == GameState.STALE_MATE;
        }
        view.displayWinner();
    }
}

enum Direction {
    NW(1,-1),N(1,0),NE(1,1),W(0,-1),E(0,1),SW(-1,-1),S(-1,0),SE(-1,1);

    private Integer deltaRow;
    private Integer deltaColumn;


    private Direction(Integer deltaR, Integer deltaC){
        deltaRow = deltaR;
        deltaColumn = deltaC;
    }


    public Integer getDeltaRow() {
        return deltaRow;
    }


    public Integer getDeltaColumn() {
        return deltaColumn;
    }
}


class FlyingBishop extends Piece {

    public FlyingBishop(Color color) {
        super(color);
    }

    @Override
    public List<Position> getPossibleMoves(Position position, Board board) {
        List<Position> deplacement = new ArrayList();

        diagonalMove(board, position, Direction.NW, deplacement);
        diagonalMove(board, position, Direction.NE, deplacement);
        diagonalMove(board, position, Direction.SW, deplacement);
        diagonalMove(board, position, Direction.SE, deplacement);

        return deplacement;
    }

    private void diagonalMove(Board board, Position position, Direction dir, List<Position> deplacement) {
        Position pos = position;
        switch (dir) {
            case NE -> {
                while (true) {
                    pos = pos.next(Direction.NE);
                    if (board.contains(pos)) {
                        if (board.isFree(pos)) {
                            deplacement.add(pos);
                        }
                        else if (color != board.getPiece(pos).color){
                            deplacement.add(pos);
                        }
                    } else {
                        break;
                    }
                }
            }
            case NW -> {
                while (true) {
                    pos = pos.next(Direction.NW);
                    if (board.contains(pos)) {
                        if (board.isFree(pos)) {
                            deplacement.add(pos);
                        }
                        else if (color != board.getPiece(pos).color){
                            deplacement.add(pos);
                        }
                    } else {
                        break;
                    }
                }
            }
            case SE -> {
                while (true) {
                    pos = pos.next(Direction.SE);
                    if (board.contains(pos)) {
                        if (board.isFree(pos)) {
                            deplacement.add(pos);
                        }
                        else if (color != board.getPiece(pos).color){
                            deplacement.add(pos);
                        }
                    } else {
                        break;
                    }
                }
            }
            case SW -> {
                while (true) {
                    pos = pos.next(Direction.SW);
                    if (board.contains(pos)) {
                        if (board.isFree(pos)) {
                            deplacement.add(pos);
                        }
                        else if (color != board.getPiece(pos).color){
                            deplacement.add(pos);
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }

    public String getName(){
        return color + "FlyingBishop";
    }

}


class Game implements Model {

    private Board board;
    private Player white;
    private Player black;
    private Player currentPlayer;
    private King whiteKing;
    private King blackKing;
    private GameState state;
    private Position pawnEnPassant;




    public Game() {
        white = new Player(Color.WHITE);
        black = new Player(Color.BLACK);
        board = new Board();
        whiteKing = new King(Color.WHITE);
        blackKing = new King(Color.BLACK);
        Alert a = new Alert(AlertType.NONE);
    }


    @Override
    public void start() {
        for (int i = 0; i < board.getSquares().length; i++) {
            board.setPiece(new Pawn(Color.WHITE), new Position(1, i));
            board.setPiece(new Pawn(Color.BLACK), new Position(6, i));
            switch (i) {
                case 0 -> {
                    board.setPiece(new Rook(Color.WHITE), new Position(0, i));
                    board.setPiece(new Rook(Color.BLACK), new Position(7, i));
                }
                case 1 -> {
                    board.setPiece(new Knight(Color.WHITE), new Position(0, i));
                    board.setPiece(new Knight(Color.BLACK), new Position(7, i));
                }
                case 2 -> {
                    board.setPiece(new Bishop(Color.WHITE), new Position(0, i));
                    board.setPiece(new Bishop(Color.BLACK), new Position(7, i));
                }
                case 3 -> {
                    board.setPiece(new Queen(Color.WHITE), new Position(0, i));
                    board.setPiece(new Queen(Color.BLACK), new Position(7, i));
                }
                case 4 -> {
                    board.setPiece(whiteKing, new Position(0, i));
                    board.setPiece(blackKing, new Position(7, i));
                }
                case 5 -> {
                    board.setPiece(new Bishop(Color.WHITE), new Position(0, i));
                    board.setPiece(new Bishop(Color.BLACK), new Position(7, i));
                }
                case 6 -> {
                    board.setPiece(new Knight(Color.WHITE), new Position(0, i));
                    board.setPiece(new Knight(Color.BLACK), new Position(7, i));
                }
                case 7 -> {
                    board.setPiece(new Rook(Color.WHITE), new Position(0, i));
                    board.setPiece(new Rook(Color.BLACK), new Position(7, i));
                }
            }
        }
        currentPlayer = white;
        state = GameState.PLAY;
    }


    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }


    @Override
    public Player getOppositePlayer() {
        return currentPlayer.getColor() == Color.BLACK ? white : black;
    }


    @Override
    public List<Position> getPossibleMoves(Position position) {
        return board.getPiece(position).getPossibleMoves(position, board);
    }


    @Override
    public Piece getPiece(Position pos) {
        if (!board.contains(pos)) {
//            throw new IllegalArgumentException("The given position is not in the table.");
            Alert a = new Alert(AlertType.NONE);
            a.setAlertType(AlertType.INFORMATION);
            a.setContentText("The given position is not in the table.");
            a.show();

            throw new IllegalArgumentException("The given position is not in the table.");
        }
        return board.getPiece(pos);
    }


    @Override
    public GameState getState() {
        return state;
    }


    @Override
    public boolean isCurrentPlayerPosition(Position pos) {
        if (!board.contains(pos)) {
//            throw new IllegalArgumentException("The given position is not in the table.");
            Alert a = new Alert(AlertType.NONE);
            a.setAlertType(AlertType.INFORMATION);
            a.setContentText("The given position is not in the table.");
            a.show();

            throw new IllegalArgumentException("The given position is not in the table.");
        }
        if (getPiece(pos) == null) {
//            throw new IllegalArgumentException("There is no piece at this position.");
            Alert a = new Alert(AlertType.NONE);
            a.setAlertType(AlertType.INFORMATION);
            a.setContentText("There is no piece at this position.");
            a.show();

            throw new IllegalArgumentException("There is no piece at this position.");
        }
        return getPiece(pos).getColor() == currentPlayer.getColor();
    }


    @Override
    public void movePiecePosition(Position oldPos, Position newPos) {
        if (!board.contains(oldPos)) {
            throw new IllegalArgumentException("The first position given does not fit in the table.");
        } else if (!board.contains(newPos)) {
            throw new IllegalArgumentException("The second position given does not fit into the table.");
        } else if (board.isFree(oldPos)) {
            throw new IllegalArgumentException("There is no piece at this position.");
        } else if (!this.isCurrentPlayerPosition(oldPos)) {
//            throw new IllegalArgumentException("This piece does not belong to you.");

            Alert a = new Alert(AlertType.NONE);
            a.setAlertType(AlertType.INFORMATION);
            a.setContentText("Enemy's Turn");
            a.show();

            throw new IllegalArgumentException("This piece does not belong to you.");

        } else if (!board.getPiece(oldPos).getPossibleMoves(oldPos, board).contains(newPos)) {
//            throw new IllegalArgumentException("You cannot make this move.");
            Alert a = new Alert(AlertType.NONE);
            a.setAlertType(AlertType.INFORMATION);
            a.setContentText("You cannot make this move.");
            a.show();

            throw new IllegalArgumentException("You cannot make this move.");
        } else if (!isValidMove(oldPos, newPos)) {
//            throw new IllegalArgumentException("this move is not valid.");
            Alert a = new Alert(AlertType.NONE);
            a.setAlertType(AlertType.INFORMATION);
            a.setContentText("this move is not valid.");
            a.show();

            throw new IllegalArgumentException("this move is not valid.");

        }
        Piece piece = getPiece(oldPos);
        board.setPiece(piece, newPos);
        board.dropPiece(oldPos);

        enPassant(piece,oldPos,newPos);
        promotion(newPos);
        roque(piece,oldPos,newPos);

        state = GameState.PLAY;

        if (échec()) {
            state = GameState.CHECK;
            if (échecEtMat()) {
                state = GameState.CHECK_MATE;
            }
        } else if (égalité()) {
            state = GameState.STALE_MATE;
        }
        currentPlayer = getOppositePlayer();
    }

    private void roque(Piece piece,Position oldPos, Position newPos){
        try {
            if(piece.getName().toLowerCase().contains("king")) {
                if (oldPos.next(Direction.E).next(Direction.E).equals(newPos)) {
                    Rook rook = (Rook) getPiece(newPos.next(Direction.E));
                    board.setPiece(rook, oldPos.next(Direction.E));
                    board.dropPiece(newPos.next(Direction.E));
                } else if (oldPos.next(Direction.W).next(Direction.W).equals(newPos)) {
                    Rook rook = (Rook) getPiece(newPos.next(Direction.W).next(Direction.W));
                    board.setPiece(rook, oldPos.next(Direction.W));
                    board.dropPiece(newPos.next(Direction.W).next(Direction.W));
                }
            }
        } catch (Exception e){}
    }

    private void promotion(Position newPos) {
        Piece piece = getPiece(newPos);
        if ("♟".equals(piece.toString())) {
            if (newPos.getRow() == 0) {
                board.setPiece(new Queen(Color.BLACK), newPos);
            }
        } else if ("♙".equals(piece.toString())) {
            if (newPos.getRow() == 7) {
                board.setPiece(new Queen(Color.WHITE), newPos);
            }
        }
    }

    private void enPassant(Piece piece,Position oldPos, Position newPos){
        try{
            Pawn p = (Pawn) board.getPiece(pawnEnPassant);
            p.setEnPassant(false);
        } catch (Exception e){}
        try{
            Pawn pawn = (Pawn) piece;
            if (pawn.getColor() == Color.WHITE && oldPos.next(Direction.N).next(Direction.N).equals(newPos)
                    || pawn.getColor() == Color.BLACK && oldPos.next(Direction.S).next(Direction.S).equals(newPos)){
                pawn.setEnPassant(true);
                pawnEnPassant = newPos;
            }
            if (pawn.getColor() == Color.WHITE && oldPos.next(Direction.N).next(Direction.E).equals(newPos)
                    || oldPos.next(Direction.N).next(Direction.W).equals(newPos)){
                board.dropPiece(newPos.next(Direction.S));
            } else if (pawn.getColor() == Color.BLACK && oldPos.next(Direction.S).next(Direction.E).equals(newPos)
                    || oldPos.next(Direction.S).next(Direction.W).equals(newPos)) {
                board.dropPiece(newPos.next(Direction.N));
            }
        } catch (Exception e) {}
    }

    protected Board getBoard() {
        return board;
    }

    protected void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }


    @Override
    public boolean isValidMove(Position oldPos, Position newPos) {
        if (board.isFree(oldPos)) {
//            throw new IllegalArgumentException("The given position does not contain a piece.");
            Alert a = new Alert(AlertType.NONE);
            a.setAlertType(AlertType.INFORMATION);
            a.setContentText("The given position does not contain a piece.");
            a.show();

            throw new IllegalArgumentException("The given position does not contain a piece.");
        } else if (!board.getPiece(oldPos).getPossibleMoves(oldPos, board).contains(newPos)) {
//            throw new IllegalArgumentException("moving is not possible.");
            Alert a = new Alert(AlertType.NONE);
            a.setAlertType(AlertType.INFORMATION);
            a.setContentText("moving is not possible.");
            a.show();

            throw new IllegalArgumentException("moving is not possible.");
        }
        Piece piece = board.getPiece(oldPos);
        Color myColor = piece.getColor();
        Piece piece2 = board.getPiece(newPos);
        Color color = piece.getColor().opposite();

        board.setPiece(piece, newPos);
        board.dropPiece(oldPos);

        Position positionDuRoi = board.getPiecePosition(getKing(new Player(myColor)));

        // si le joueur qui attaque est mis en echec après son coup.
        boolean valide = !getCapturePositions(new Player(color)).contains(positionDuRoi);

        board.setPiece(piece, oldPos);
        board.dropPiece(newPos);
        board.setPiece(piece2, newPos);

        return valide;
    }


    private List<Position> getCapturePositions(Player player) {
        List<Position> capturesPossible = new ArrayList();

        List<Position> pieces = board.getPositionOccupiedBy(player);

        for (int i = 0; i < pieces.size(); i++) {
            Position position = pieces.get(i);
            List<Position> capturesPiece = board.getPiece(position).getCapturePositions(position, board);
            capturesPossible.addAll(capturesPiece);
        }
        return capturesPossible;
    }


    private King getKing(Player player) {
        return player.getColor() == Color.WHITE ? whiteKing : blackKing;
    }

    private boolean échec() {
        return getCapturePositions(currentPlayer).contains(board.getPiecePosition(getKing(getOppositePlayer())));
    }


    private boolean échecEtMat() {
        List<Position> pieces = board.getPositionOccupiedBy(getOppositePlayer());
        boolean mat = false;
        for (int i = 0; i < pieces.size(); i++) {
            List<Position> deplacements = getPossibleMoves(pieces.get(i));
            for (int j = 0; j < deplacements.size(); j++) {
                Position oldPos = pieces.get(i);
                Position newPos = deplacements.get(j);

                if (isValidMove(oldPos, newPos)) {
                    Piece piece2 = getPiece(newPos);
                    board.setPiece(getPiece(oldPos), newPos);
                    board.dropPiece(oldPos);
                    if (!échec()) {
                        board.setPiece(getPiece(newPos), oldPos);
                        board.dropPiece(newPos);
                        board.setPiece(piece2, newPos);
                        return mat;
                    }
                    board.setPiece(getPiece(newPos), oldPos);
                    board.dropPiece(newPos);
                    board.setPiece(piece2, newPos);
                }
            }
        }
        mat = true;
        return mat;
    }


    private boolean égalité() {
        List<Position> pieces = board.getPositionOccupiedBy(getOppositePlayer());
        boolean égalité = false;
        for (int i = 0; i < pieces.size(); i++) {
            List<Position> deplacements = getPossibleMoves(pieces.get(i));
            Position oldPos = pieces.get(i);

            for (int j = 0; j < deplacements.size(); j++) {
                Position newPos = deplacements.get(j);
                if (isValidMove(oldPos, newPos)) {
                    return égalité;
                }
            }
        }
        égalité = true;
        return égalité;
    }
}

enum GameState {
    PLAY, CHECK, CHECK_MATE, STALE_MATE;
}

class King extends Piece {
    public static final String BLACK_BOLD = "\033[1;30m";


    public King(Color color) {
        super(color);
    }


    @Override
    public List<Position> getPossibleMoves(Position position, Board board) {
        List<Position> deplacement = new ArrayList();
        kingMove(board, position, Direction.E, deplacement);
        kingMove(board, position, Direction.N, deplacement);
        kingMove(board, position, Direction.NE, deplacement);
        kingMove(board, position, Direction.NW, deplacement);
        kingMove(board, position, Direction.S, deplacement);
        kingMove(board, position, Direction.SE, deplacement);
        kingMove(board, position, Direction.SW, deplacement);
        kingMove(board, position, Direction.W, deplacement);
        return deplacement;
    }

    public String getName(){
        return color + "King";
    }


    private void kingMove(Board board, Position position, Direction dir, List<Position> deplacement) {
        Position pos = position.next(dir);
        try {
            if (board.isFree(pos)) {
                deplacement.add(pos);
            } else if (this.color != board.getPiece(pos).color){
                deplacement.add(pos);
            }
            if(getInitialKingRow(color).equals(position.getRow()) &&
                    board.getPiece(pos) == null && board.getPiece(pos.next(dir)) == null){
                if(dir == Direction.E){
                    deplacement.add(pos.next(dir));
                } else if (dir == Direction.W && board.getPiece(pos.next(dir).next(dir)) == null) {
                    deplacement.add(pos.next(dir));
                }
            }
        } catch (Exception e) {}
    }


    public Integer getInitialKingRow(Color color) {
        return color == Color.BLACK ? 7 : 0;
    }

    @Override
    public String toString() {
        return color == Color.BLACK ? BLACK_BOLD+"♚" : "♚";
    }
}

class Knight extends Piece {
    public static final String BLACK_BOLD = "\033[1;30m";


    public Knight(Color color) {
        super(color);
    }


    @Override
    public List<Position> getPossibleMoves(Position position, Board board) {
        List<Position> deplacement = new ArrayList();
        knightMove(board, position, Direction.NE, deplacement);
        knightMove(board, position, Direction.NW, deplacement);
        knightMove(board, position, Direction.SE, deplacement);
        knightMove(board, position, Direction.SW, deplacement);
        return deplacement;
    }


    private void knightMove(Board board, Position position, Direction dir, List<Position> deplacement) {
        Position pos1 = position;
        Position pos2 = position;
        switch (dir) {
            case NE -> {
                pos1 = pos1.next(Direction.N).next(Direction.N).next(Direction.E);
                pos2 = pos2.next(Direction.E).next(Direction.E).next(Direction.N);
            }
            case NW -> {
                pos1 = pos1.next(Direction.N).next(Direction.N).next(Direction.W);
                pos2 = pos2.next(Direction.W).next(Direction.W).next(Direction.N);
            }
            case SE -> {
                pos1 = pos1.next(Direction.S).next(Direction.S).next(Direction.E);
                pos2 = pos2.next(Direction.E).next(Direction.E).next(Direction.S);
            }
            case SW -> {
                pos1 = pos1.next(Direction.S).next(Direction.S).next(Direction.W);
                pos2 = pos2.next(Direction.W).next(Direction.W).next(Direction.S);
            }
        }
        try {
            if (board.isFree(pos1)) {
                deplacement.add(pos1);
            }
            if (this.color != board.getPiece(pos1).color){
                deplacement.add(pos1);
            }
        } catch (Exception e) {}
        try {
            if (board.isFree(pos2)) {
                deplacement.add(pos2);
            }
            if (this.color != board.getPiece(pos2).color){
                deplacement.add(pos2);
            }
        } catch (Exception e) {}
    }

    public String getName(){
        return color + "Knight";
    }

    @Override
    public String toString() {
        return color == Color.BLACK ? BLACK_BOLD+"♞" : "♞";
    }
}


interface Model {


    public void start();


    public Player getCurrentPlayer();


    public Player getOppositePlayer();


    public List<Position> getPossibleMoves(Position position);


    public Piece getPiece(Position pos);


    public boolean isCurrentPlayerPosition(Position pos);


    public void movePiecePosition(Position oldPos, Position newPos);


    public GameState getState();


    public boolean isValidMove(Position oldPos, Position newPos);
}


class Pawn extends Piece {
    private static final String BLACK_BOLD = "\033[1;30m";
    private boolean enPassant;


    public Pawn(Color color) {
        super(color);
    }


    @Override
    public List<Position> getPossibleMoves(Position position, Board board) {
        List<Position> deplacement = new ArrayList();
        boolean isBlack = this.color == Color.BLACK;
        if (isBlack) {
            peutAvancer(board, position, Direction.S, deplacement);
            peutAttaquer(board, position, Direction.SW, deplacement);
            peutAttaquer(board, position, Direction.SE, deplacement);
        } else {
            peutAvancer(board, position, Direction.N, deplacement);
            peutAttaquer(board, position, Direction.NW, deplacement);
            peutAttaquer(board, position, Direction.NE, deplacement);
        }
        return deplacement;
    }


    private void peutCapturer(Board board, Position position, Direction direction, List<Position> capture) {
        Position pos = position.next(direction);
        if (board.contains(pos)) {
            if (board.getPiece(pos) != null) {
                if (board.getPiece(position).color != board.getPiece(pos).getColor()){
                    capture.add(pos);
                }
            } else {
                capture.add(pos);
            }

        }
    }


    private void peutAvancer(Board board, Position position, Direction mouvement, List<Position> deplacement) {
        Position pos = position.next(mouvement);
        // peut avancer d'une case ?
        if (isEmptyPosition(board, pos)) {
            deplacement.add(pos);
            // peut avancer de deux cases ?
            if (board.getInitialPawnRow(color).equals(position.getRow())) {
                pos = pos.next(mouvement);
                if (isEmptyPosition(board,pos)) {
                    deplacement.add(pos);
                }
            }
        }
        // en passant
        canEnPassant(board,position,deplacement,mouvement);
    }


    private void canEnPassant(Board board, Position position,List<Position> deplacement,Direction mouvement){
        String color = getColor() == Color.BLACK ? "WHITE": "BLACK";
        try {
            Pawn pawnPassedWest = (Pawn) board.getPiece(position.next(Direction.W));
            if (pawnPassedWest.getName().equals(color + "Pawn") && pawnPassedWest.enPassant
                    && board.isFree(position.next(Direction.W).next(mouvement))) {
                deplacement.add(position.next(Direction.W).next(mouvement));
            }
        } catch (Exception e) {}
        try {
            Pawn pawnPassedEast = (Pawn) board.getPiece(position.next(Direction.E));
            if (pawnPassedEast.getName().equals(color + "Pawn") && pawnPassedEast.enPassant
                    && board.isFree(position.next(Direction.E).next(mouvement))) {
                deplacement.add(position.next(Direction.E).next(mouvement));
            }
        } catch (Exception e) {}
    }

    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }

    public String getName(){
        return color + "Pawn";
    }


    @Override
    public List<Position> getCapturePositions(Position position, Board board) {
        if (board.getPiece(position) != this) {
//
            Alert a = new Alert(AlertType.NONE);
            a.setAlertType(AlertType.INFORMATION);
            a.setContentText("this position does not contain the piece");
            a.show();
            throw new IllegalArgumentException("this position does not contain the piece");
        }
        List<Position> capture = new ArrayList();
        boolean isBlack = this.color == Color.BLACK;
        if (isBlack) {
            peutCapturer(board, position, Direction.SE, capture);
            peutCapturer(board, position, Direction.SW, capture);
        } else {
            peutCapturer(board, position, Direction.NE, capture);
            peutCapturer(board, position, Direction.NW, capture);
        }
        return capture;
    }

    @Override
    public String toString() {
        return color == Color.BLACK ? BLACK_BOLD+"♙" : "♙";
    }
}



abstract class Piece {

    Color color;


    public Piece(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }


    public abstract List<Position> getPossibleMoves(Position position, Board board);


    public List<Position> getCapturePositions(Position position, Board board) {
        return  getPossibleMoves(position, board);
    }


    protected void peutAttaquer(Board board, Position position, Direction attaque, List<Position> deplacement) {
        Position pos = position.next(attaque);

        if (board.contains(pos)) {
            if (board.containsOppositeColor(pos, color)) {
                deplacement.add(pos);
            }
        }
    }


    protected boolean isEmptyPosition(Board board, Position pos) {
        if (board.contains(pos)) {
            return board.isFree(pos);
        }
        return false;
    }


    protected boolean dontContainsColor(Board board, Position pos, Color col) {
        if (!board.contains(pos)) {
//            
            Alert a = new Alert(AlertType.NONE);
            a.setAlertType(AlertType.INFORMATION);
            a.setContentText("The given position is not on the board.");
            a.show();

            throw new IllegalArgumentException("The given position is not on the board.");
        }
        if (board.isFree(pos)) {
            return true;
        }
        return this.getColor() != col;
    }

    protected boolean containsColor(Board board, Position pos, Color col){
        if (!board.contains(pos)) {
//            throw new IllegalArgumentException("The given position is not on the board.");
            Alert a = new Alert(AlertType.NONE);
            a.setAlertType(AlertType.INFORMATION);
            a.setContentText("The given position is not on the board.");
            a.show();


            throw new IllegalArgumentException("The given position is not on the board.");
        }
        if (board.isFree(pos)) {
            return false;
        }
        return this.color == col;
    }

    protected void line(Board board, Position position, Direction dir, List<Position> deplacement) {
        Position pos = position;
        try{
            switch (dir) {
                case E -> {
                    pos = pos.next(Direction.E);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            deplacement.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        deplacement.add(pos);
                        pos = pos.next(Direction.E);

                    }
                }
                case N -> {
                    pos = pos.next(Direction.N);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            deplacement.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        deplacement.add(pos);
                        pos = pos.next(Direction.N);

                    }

                }
                case S -> {
                    pos = pos.next(Direction.S);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            deplacement.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        deplacement.add(pos);
                        pos = pos.next(Direction.S);
                    }

                }
                case W -> {
                    pos = pos.next(Direction.W);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            deplacement.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        deplacement.add(pos);
                        pos = pos.next(Direction.W);
                    }
                }
            }
        } catch(Exception e){}
    }

    protected void diagonal(Board board, Position position, Direction dir, List<Position> deplacement) {
        Position pos = position;
        try {
            switch (dir) {
                case NE -> {
                    pos = pos.next(Direction.NE);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            deplacement.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        deplacement.add(pos);
                        pos = pos.next(Direction.NE);
                    }
                }
                case NW -> {
                    pos = pos.next(Direction.NW);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            deplacement.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        deplacement.add(pos);
                        pos = pos.next(Direction.NW);
                    }
                }
                case SE -> {
                    pos = pos.next(Direction.SE);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            deplacement.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        deplacement.add(pos);
                        pos = pos.next(Direction.SE);
                    }
                }
                case SW -> {
                    pos = pos.next(Direction.SW);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            deplacement.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        deplacement.add(pos);
                        pos = pos.next(Direction.SW);
                    }
                }
            }
        } catch (Exception e) {}
    }

    public abstract String getName();

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Piece other = (Piece) obj;
        if (this.color != other.color) {
            return false;
        }
        return true;
    }

}


class Player {

    private Color color;


    public Player(Color color){
        this.color = color;
    }


    public Color getColor(){
        return color;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Player other = (Player) obj;
        if (this.color != other.color) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if(color == Color.BLACK){
            return "whites";
        }
        return "black";
    }
}


class Position {
    private Integer row;
    private Integer column;


    public Position(Integer row, Integer Column){
        this.row = row;
        this.column = Column;
    }


    public Integer getRow() {
        return row;
    }

    public Integer getColumn() {
        return column;
    }


    public Position next(Direction dir){
        int aRow= this.row;
        int aColumn = this.column;
        int rowDir = dir.getDeltaRow();
        int columnDir = dir.getDeltaColumn();

        return new Position (aRow + rowDir, aColumn + columnDir);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Position other = (Position) obj;
        if (!Objects.equals(this.row, other.row)) {
            return false;
        }
        if (!Objects.equals(this.column, other.column)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Position{" + "row=" + row + ", column=" + column + '}';
    }
}


class Queen extends Piece {
    public static final String BLACK_BOLD = "\033[1;30m";


    public Queen (Color color){
        super(color);
    }

    public String getName(){
        return color + "Queen";
    }


    @Override
    public List<Position> getPossibleMoves(Position position, Board board) {
        List<Position> deplacement = new ArrayList();
        line(board, position, Direction.E, deplacement);
        line(board, position, Direction.N, deplacement);
        line(board, position, Direction.S, deplacement);
        line(board, position, Direction.W, deplacement);
        diagonal(board, position, Direction.NW, deplacement);
        diagonal(board, position, Direction.NE, deplacement);
        diagonal(board, position, Direction.SW, deplacement);
        diagonal(board, position, Direction.SE, deplacement);

        return deplacement;
    }

    @Override
    public String toString() {
        return color == Color.BLACK ? BLACK_BOLD+"♛" : "♛";
    }
}


class Rook extends Piece {
    public static final String BLACK_BOLD = "\033[1;30m";


    public Rook(Color color){
        super(color);
    }

    public String getName(){
        return color + "Rook";
    }


    @Override
    public List<Position> getPossibleMoves(Position position, Board board) {
        List<Position> deplacement = new ArrayList();
        line(board, position, Direction.E, deplacement);
        line(board, position, Direction.N, deplacement);
        line(board, position, Direction.S, deplacement);
        line(board, position, Direction.W, deplacement);

        return deplacement;
    }

    @Override
    public String toString() {
        return color == Color.BLACK ? BLACK_BOLD+"♜" : "♜";
    }
}


class Square {

    private Piece piece;


    public Square(Piece piece){
        this.piece = piece;
    }


    public Piece getPiece(){
        return piece;
    }


    public void setPiece(Piece piece) {
        this.piece = piece;
    }


    public boolean isFree(){
        return piece == null;
    }
}


class TextView implements View {

    private Model model;
    private Scanner clavier = new Scanner(System.in);
    private final String ANSI_RESET = "\u001B[0m";
    public static final String WHITE_BACKGROUND_BRIGHT  = "\033[0;107m";
    public static final String BLACK_BRIGHT = "\033[0;90m";

    public TextView(Model model) {
        this.model = model;
    }


    @Override
    public void displayTitle() {
        System.out.println("Chess :");
        System.out.println("Welcome to this game");
    }


    @Override
    public void displayBoard() {
        try{
            for (int i = 7; i >= 0; i--) {
                System.setOut(new PrintStream(System.out, true, "UTF8")); // Pour les consoles
                System.out.print(i + 1);
                for (int j = 0; j < 8; j++) {
                    displaySquare(i,j);
                }
                System.out.println("");
            }
            System.out.println("  a   b   c   d   e   f   g   h");
        } catch(UnsupportedEncodingException e){
            System.out.println("An UnsupportedEncodingException is detected : "+e.getMessage());
        }
    }


    private void displaySquare(int i, int j){
        Position pos = new Position(i, j);
        boolean isPieceNull = model.getPiece(pos) == null;
        boolean isEvenRow = i % 2 == 0;
        boolean isEvenColumn = j % 2 == 0;
        if (isEvenRow) {
            if (isPieceNull && isEvenColumn) {
                System.out.print(WHITE_BACKGROUND_BRIGHT +"    "+ ANSI_RESET);
            } else if (isPieceNull) {
                System.out.print(BLACK_BRIGHT +"    "+ ANSI_RESET);
            } else if (isEvenColumn) {
                System.out.print(WHITE_BACKGROUND_BRIGHT +" "+ model.getPiece(pos) +"  "+ ANSI_RESET);
            } else {
                System.out.print(BLACK_BRIGHT +" "+ model.getPiece(pos) +"  "+ ANSI_RESET);
            }
        } else {
            if (model.getPiece(pos) == null && isEvenColumn) {
                System.out.print(BLACK_BRIGHT +"    "+ ANSI_RESET);
            } else if (model.getPiece(pos) == null) {
                System.out.print(WHITE_BACKGROUND_BRIGHT +"    "+ ANSI_RESET);
            } else if (isEvenColumn) {
                System.out.print(BLACK_BRIGHT +" "+ model.getPiece(pos)+"  "+ ANSI_RESET);
            } else {
                System.out.print(WHITE_BACKGROUND_BRIGHT +" "+ model.getPiece(pos)+"  "+ ANSI_RESET);
            }
        }
    }


    @Override
    public void displayWinner() {
        if (model.getCurrentPlayer().getColor() == Color.BLACK) {
            System.out.println("Black won.");
        } else {
            System.out.println("White won.");
        }
    }


    @Override
    public void displayPlayer() {
        if (model.getCurrentPlayer().getColor() == Color.BLACK) {
            System.out.println("Black's turn");
        } else {
            System.out.println("White's turn");
        }
        System.out.println("Which pawn do you want to move? (Column then row)");
    }


    @Override
    public Position askPosition() {
        Position pos = null;
        boolean estCorrect;

        do {
            try {
                estCorrect = true;
                System.out.println("Please enter a position.");
                String coordonnée = clavier.nextLine();
                pos = changeInPosition(coordonnée);
            } catch (Exception e) {
                System.out.println("this position does not exist.");
                estCorrect = false;
            }
        } while (!estCorrect);

        return pos;
    }


    @Override
    public void displayError(String message) {
        System.out.println(message);
    }


    private Position changeInPosition(String move) {
        char PremièreLettre = move.toLowerCase().charAt(0);
        char DeuxièmeLettre = move.toLowerCase().charAt(1);

        Integer column;
        column = switch (PremièreLettre) {
            case 'a' ->
                    0;
            case 'b' ->
                    1;
            case 'c' ->
                    2;
            case 'd' ->
                    3;
            case 'e' ->
                    4;
            case 'f' ->
                    5;
            case 'g' ->
                    6;
            case 'h' ->
                    7;
            default ->
                    throw new IllegalArgumentException();
        };
        Integer row = switch (DeuxièmeLettre) {
            case '1' ->
                    0;
            case '2' ->
                    1;
            case '3' ->
                    2;
            case '4' ->
                    3;
            case '5' ->
                    4;
            case '6' ->
                    5;
            case '7' ->
                    6;
            case '8' ->
                    7;
            default ->
                    throw new IllegalArgumentException();
        };
        return new Position(row, column);
    }


    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }


    @Override
    public void displayState(GameState state) {
        switch (state) {
            case CHECK -> System.out.println("You are in Failure.");
            case CHECK_MATE -> System.out.println("Checkmate");
            case STALE_MATE -> System.out.println("Equality");
            default -> { System.out.println(state);
            }
        }
    }
}


interface View {

    public void displayTitle();


    public void displayBoard();


    public void displayWinner();

    public void displayPlayer();


    public Position askPosition();


    public void displayError(String message);


    public void displayMessage(String message);


    public void displayState(GameState state);
}







