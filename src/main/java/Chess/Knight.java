package Chess;

import java.util.ArrayList;
import java.util.List;


public class Knight extends Piece {
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
