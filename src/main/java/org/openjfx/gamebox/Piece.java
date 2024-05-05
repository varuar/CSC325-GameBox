package org.openjfx.gamebox;

import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public abstract class Piece {

    Color color;

   
    public Piece(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

  
    public abstract List<Position> getPossibleMoves(Position position, Board board);

   
    public List<Position> getCapturePositions(Position position, Board board) {
        return  getPossibleMoves(position, board);
    }

  
    protected void peutAttaquer(Board board, Position position, Direction attaque, List<Position> deplacement) {
        Position pos = position.next(attaque);

        if (board.contains(pos)) {
            if (board.containsOppositeColor(pos, color)) {
                deplacement.add(pos);
            }
        }
    }

   
    protected boolean isEmptyPosition(Board board, Position pos) {
        if (board.contains(pos)) {
            return board.isFree(pos);
        }
        return false;
    }

   
    protected boolean dontContainsColor(Board board, Position pos, Color col) {
        if (!board.contains(pos)) {
//            
                        Alert a = new Alert(AlertType.NONE);
                a.setAlertType(AlertType.INFORMATION);
                a.setContentText("The given position is not on the board.");
                a.show();
                
                throw new IllegalArgumentException("The given position is not on the board.");
        }
        if (board.isFree(pos)) {
            return true;
        }
        return this.getColor() != col;
    }

    protected boolean containsColor(Board board, Position pos, Color col){
        if (!board.contains(pos)) {
//            throw new IllegalArgumentException("The given position is not on the board.");
                        Alert a = new Alert(AlertType.NONE);
                a.setAlertType(AlertType.INFORMATION);
                a.setContentText("The given position is not on the board.");
                a.show();
                
                
                throw new IllegalArgumentException("The given position is not on the board.");
        }
        if (board.isFree(pos)) {
            return false;
        }
        return this.color == col;
    }
    
    protected void line(Board board, Position position, Direction dir, List<Position> deplacement) {
        Position pos = position;
        try{
        switch (dir) {
            case E -> {
                pos = pos.next(Direction.E);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            deplacement.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        deplacement.add(pos);
                        pos = pos.next(Direction.E);
                        
                    }
                }
            case N -> {
                pos = pos.next(Direction.N);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            deplacement.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        deplacement.add(pos);
                        pos = pos.next(Direction.N);
                        
                    }
                    
                }
            case S -> {
                pos = pos.next(Direction.S);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            deplacement.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        deplacement.add(pos);
                        pos = pos.next(Direction.S);
                    }
                    
                }
            case W -> {
                pos = pos.next(Direction.W);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            deplacement.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        deplacement.add(pos);
                        pos = pos.next(Direction.W);
                    }
                } 
            } 
        } catch(Exception e){}
    }

    protected void diagonal(Board board, Position position, Direction dir, List<Position> deplacement) {
        Position pos = position;
        try {
            switch (dir) {
                case NE -> {
                    pos = pos.next(Direction.NE);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            deplacement.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        deplacement.add(pos);
                        pos = pos.next(Direction.NE);
                    }
                }
                case NW -> {
                    pos = pos.next(Direction.NW);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            deplacement.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        deplacement.add(pos);
                        pos = pos.next(Direction.NW);
                    }
                }
                case SE -> {
                    pos = pos.next(Direction.SE);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            deplacement.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        deplacement.add(pos);
                        pos = pos.next(Direction.SE);
                    }
                }
                case SW -> {
                    pos = pos.next(Direction.SW);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            deplacement.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        deplacement.add(pos);
                        pos = pos.next(Direction.SW);
                    }
                }
            }
        } catch (Exception e) {}
    }

    public abstract String getName();

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Piece other = (Piece) obj;
        if (this.color != other.color) {
            return false;
        }
        return true;
    }

}
