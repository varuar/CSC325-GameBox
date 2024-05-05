package Chess;

import java.util.List;


public interface Model {
    
  
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
