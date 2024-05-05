package Chess;


public enum Color {
    WHITE, BLACK;
     
  
    public Color opposite(){
        return this == BLACK ? WHITE : BLACK;
    }
}
