package org.openjfx.gamebox;


public class Square {
    
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
