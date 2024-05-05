package org.openjfx.gamebox;


public class Player {
    
    private Color color;
    
  
    public Player(Color color){
        this.color = color;
    }
    
  
    public Color getColor(){
        return color;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Player other = (Player) obj;
        if (this.color != other.color) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if(color == Color.BLACK){
            return "whites";
        }
        return "black";
    }
}
