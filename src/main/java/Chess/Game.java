package Chess;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class Game implements Model {

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
