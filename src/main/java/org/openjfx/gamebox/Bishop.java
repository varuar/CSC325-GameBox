package org.openjfx.gamebox;

import java.util.ArrayList;
import java.util.List;


public class Bishop extends Piece {
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
