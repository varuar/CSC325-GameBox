package org.openjfx.gamebox;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Pawn extends Piece {
    private static final String BLACK_BOLD = "\033[1;30m";
    private boolean enPassant;

   
    public Pawn(Color color) {
        super(color);
    }

   
    @Override
    public List<Position> getPossibleMoves(Position position, Board board) {
        List<Position> deplacement = new ArrayList();
        boolean isBlack = this.color == Color.BLACK;
        if (isBlack) {
            peutAvancer(board, position, Direction.S, deplacement);
            peutAttaquer(board, position, Direction.SW, deplacement);
            peutAttaquer(board, position, Direction.SE, deplacement);
        } else {
            peutAvancer(board, position, Direction.N, deplacement);
            peutAttaquer(board, position, Direction.NW, deplacement);
            peutAttaquer(board, position, Direction.NE, deplacement);
        }
        return deplacement;
    }

   
    private void peutCapturer(Board board, Position position, Direction direction, List<Position> capture) {
        Position pos = position.next(direction);
        if (board.contains(pos)) {
            if (board.getPiece(pos) != null) {
                if (board.getPiece(position).color != board.getPiece(pos).getColor()){
               capture.add(pos); 
            }
            } else {
                capture.add(pos);
            }
            
        }
    }

    
    private void peutAvancer(Board board, Position position, Direction mouvement, List<Position> deplacement) {
        Position pos = position.next(mouvement);
        // peut avancer d'une case ?
        if (isEmptyPosition(board, pos)) {
            deplacement.add(pos);
            // peut avancer de deux cases ?
            if (board.getInitialPawnRow(color).equals(position.getRow())) {
                pos = pos.next(mouvement);
                if (isEmptyPosition(board,pos)) {
                    deplacement.add(pos);
                }
            }
        }
        // en passant
        canEnPassant(board,position,deplacement,mouvement);
    }

   
    private void canEnPassant(Board board, Position position,List<Position> deplacement,Direction mouvement){
        String color = getColor() == Color.BLACK ? "WHITE": "BLACK";
        try {
            Pawn pawnPassedWest = (Pawn) board.getPiece(position.next(Direction.W));
            if (pawnPassedWest.getName().equals(color + "Pawn") && pawnPassedWest.enPassant
                    && board.isFree(position.next(Direction.W).next(mouvement))) {
                deplacement.add(position.next(Direction.W).next(mouvement));
            }
        } catch (Exception e) {}
        try {
            Pawn pawnPassedEast = (Pawn) board.getPiece(position.next(Direction.E));
            if (pawnPassedEast.getName().equals(color + "Pawn") && pawnPassedEast.enPassant
                    && board.isFree(position.next(Direction.E).next(mouvement))) {
                deplacement.add(position.next(Direction.E).next(mouvement));
            }
        } catch (Exception e) {}
    }

    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }

    public String getName(){
        return color + "Pawn";
    }

   
    @Override
    public List<Position> getCapturePositions(Position position, Board board) {
        if (board.getPiece(position) != this) {
//            
            Alert a = new Alert(AlertType.NONE);
                a.setAlertType(AlertType.INFORMATION);
                a.setContentText("this position does not contain the piece");
                a.show();
            throw new IllegalArgumentException("this position does not contain the piece");
        }
        List<Position> capture = new ArrayList();
        boolean isBlack = this.color == Color.BLACK;
        if (isBlack) {
            peutCapturer(board, position, Direction.SE, capture);
            peutCapturer(board, position, Direction.SW, capture);
        } else {
            peutCapturer(board, position, Direction.NE, capture);
            peutCapturer(board, position, Direction.NW, capture);
        }
        return capture;
    }

    @Override
    public String toString() {
        return color == Color.BLACK ? BLACK_BOLD+"♙" : "♙";
    }
}
