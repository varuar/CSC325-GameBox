package org.openjfx.gamebox;


public enum Color {
    WHITE, BLACK;
     
  
    public Color opposite(){
        return this == BLACK ? WHITE : BLACK;
    }
}
