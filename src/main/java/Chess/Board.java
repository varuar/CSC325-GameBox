package Chess;

import java.util.ArrayList;
import java.util.List;

public class Board {

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
