package org.openjfx.gamebox;

import java.util.ArrayList;
import java.util.List;


public class FlyingBishop extends Piece {

    public FlyingBishop(Color color) {
        super(color);
    }

    @Override
    public List<Position> getPossibleMoves(Position position, Board board) {
        List<Position> deplacement = new ArrayList();

        diagonalMove(board, position, Direction.NW, deplacement);
        diagonalMove(board, position, Direction.NE, deplacement);
        diagonalMove(board, position, Direction.SW, deplacement);
        diagonalMove(board, position, Direction.SE, deplacement);

        return deplacement;
    }

    private void diagonalMove(Board board, Position position, Direction dir, List<Position> deplacement) {
        Position pos = position;
        switch (dir) {
            case NE -> {
                while (true) {
                    pos = pos.next(Direction.NE);
                    if (board.contains(pos)) {
                        if (board.isFree(pos)) {
                            deplacement.add(pos);
                        }
                        else if (color != board.getPiece(pos).color){
                            deplacement.add(pos);
                        }
                    } else {
                        break;
                    }
                }
            }
            case NW -> {
                while (true) {
                    pos = pos.next(Direction.NW);
                    if (board.contains(pos)) {
                        if (board.isFree(pos)) {
                            deplacement.add(pos);
                        }
                        else if (color != board.getPiece(pos).color){
                            deplacement.add(pos);
                        }
                    } else {
                        break;
                    }
                }
            }
            case SE -> {
                while (true) {
                    pos = pos.next(Direction.SE);
                    if (board.contains(pos)) {
                        if (board.isFree(pos)) {
                            deplacement.add(pos);
                        }
                        else if (color != board.getPiece(pos).color){
                            deplacement.add(pos);
                        }
                    } else {
                        break;
                    }
                }
            }
            case SW -> {
                while (true) {
                    pos = pos.next(Direction.SW);
                    if (board.contains(pos)) {
                        if (board.isFree(pos)) {
                            deplacement.add(pos);
                        }
                        else if (color != board.getPiece(pos).color){
                            deplacement.add(pos);
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }

    public String getName(){
        return color + "FlyingBishop";
    }

}
