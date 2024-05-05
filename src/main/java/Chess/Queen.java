package Chess;

import java.util.ArrayList;
import java.util.List;


public class Queen extends Piece {
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
