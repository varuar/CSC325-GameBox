package Chess;


public interface View {
    
    public void displayTitle();
    
   
    public void displayBoard();
    
    
    public void displayWinner();
    
    public void displayPlayer();
    
  
    public Position askPosition();
    
    
    public void displayError(String message);
    
    
    public void displayMessage(String message);
    
    
    public void displayState(GameState state);
}
