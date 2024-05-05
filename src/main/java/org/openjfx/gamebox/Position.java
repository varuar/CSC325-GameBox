package org.openjfx.gamebox;

import java.util.Objects;


public class Position {
    private Integer row; 
    private Integer column; 
    
   
    public Position(Integer row, Integer Column){
        this.row = row;
        this.column = Column;
    }

   
    public Integer getRow() {
        return row;
    }
   
    public Integer getColumn() {
        return column;
    }
    
   
    public Position next(Direction dir){
        int aRow= this.row;
        int aColumn = this.column;
        int rowDir = dir.getDeltaRow();
        int columnDir = dir.getDeltaColumn();
        
        return new Position (aRow + rowDir, aColumn + columnDir);
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Position other = (Position) obj;
        if (!Objects.equals(this.row, other.row)) {
            return false;
        }
        if (!Objects.equals(this.column, other.column)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Position{" + "row=" + row + ", column=" + column + '}';
    }
}
