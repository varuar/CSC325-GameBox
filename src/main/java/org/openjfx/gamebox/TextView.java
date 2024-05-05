package org.openjfx.gamebox;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;


public class TextView implements View {

    private Model model;
    private Scanner clavier = new Scanner(System.in);
    private final String ANSI_RESET = "\u001B[0m";
    public static final String WHITE_BACKGROUND_BRIGHT  = "\033[0;107m";
    public static final String BLACK_BRIGHT = "\033[0;90m";

    public TextView(Model model) {
        this.model = model;
    }

    
    @Override
    public void displayTitle() {
        System.out.println("Chess :");
        System.out.println("Welcome to this game");
    }

    
    @Override
    public void displayBoard() {
        try{
        for (int i = 7; i >= 0; i--) {
            System.setOut(new PrintStream(System.out, true, "UTF8")); // Pour les consoles
            System.out.print(i + 1);
            for (int j = 0; j < 8; j++) {
                displaySquare(i,j);
            }
            System.out.println("");
        }
        System.out.println("  a   b   c   d   e   f   g   h");
    } catch(UnsupportedEncodingException e){
            System.out.println("An UnsupportedEncodingException is detected : "+e.getMessage());
        }
    }

    
    private void displaySquare(int i, int j){
        Position pos = new Position(i, j);
        boolean isPieceNull = model.getPiece(pos) == null;
        boolean isEvenRow = i % 2 == 0;
        boolean isEvenColumn = j % 2 == 0;
        if (isEvenRow) {
            if (isPieceNull && isEvenColumn) {
                System.out.print(WHITE_BACKGROUND_BRIGHT +"    "+ ANSI_RESET);
            } else if (isPieceNull) {
                System.out.print(BLACK_BRIGHT +"    "+ ANSI_RESET);
            } else if (isEvenColumn) {
                System.out.print(WHITE_BACKGROUND_BRIGHT +" "+ model.getPiece(pos) +"  "+ ANSI_RESET);
            } else {
                System.out.print(BLACK_BRIGHT +" "+ model.getPiece(pos) +"  "+ ANSI_RESET);
            }
        } else {
            if (model.getPiece(pos) == null && isEvenColumn) {
                System.out.print(BLACK_BRIGHT +"    "+ ANSI_RESET);
            } else if (model.getPiece(pos) == null) {
                System.out.print(WHITE_BACKGROUND_BRIGHT +"    "+ ANSI_RESET);
            } else if (isEvenColumn) {
                System.out.print(BLACK_BRIGHT +" "+ model.getPiece(pos)+"  "+ ANSI_RESET);
            } else {
                System.out.print(WHITE_BACKGROUND_BRIGHT +" "+ model.getPiece(pos)+"  "+ ANSI_RESET);
            }
        }
    }

  
    @Override
    public void displayWinner() {
        if (model.getCurrentPlayer().getColor() == Color.BLACK) {
            System.out.println("Black won.");
        } else {
            System.out.println("White won.");
        }
    }

    
    @Override
    public void displayPlayer() {
        if (model.getCurrentPlayer().getColor() == Color.BLACK) {
            System.out.println("Black's turn");
        } else {
            System.out.println("White's turn");
        }
        System.out.println("Which pawn do you want to move? (Column then row)");
    }

    
    @Override
    public Position askPosition() {
        Position pos = null;
        boolean estCorrect;

        do {
            try {
                estCorrect = true;
                System.out.println("Please enter a position.");
                String coordonnée = clavier.nextLine();
                pos = changeInPosition(coordonnée);
            } catch (Exception e) {
                System.out.println("this position does not exist.");
                estCorrect = false;
            }
        } while (!estCorrect);

        return pos;
    }

   
    @Override
    public void displayError(String message) {
        System.out.println(message);
    }

   
    private Position changeInPosition(String move) {
        char PremièreLettre = move.toLowerCase().charAt(0);
        char DeuxièmeLettre = move.toLowerCase().charAt(1);

        Integer column;
        column = switch (PremièreLettre) {
            case 'a' ->
                0;
            case 'b' ->
                1;
            case 'c' ->
                2;
            case 'd' ->
                3;
            case 'e' ->
                4;
            case 'f' ->
                5;
            case 'g' ->
                6;
            case 'h' ->
                7;
            default ->
                throw new IllegalArgumentException();
        };
        Integer row = switch (DeuxièmeLettre) {
            case '1' ->
                0;
            case '2' ->
                1;
            case '3' ->
                2;
            case '4' ->
                3;
            case '5' ->
                4;
            case '6' ->
                5;
            case '7' ->
                6;
            case '8' ->
                7;
            default ->
                throw new IllegalArgumentException();
        };
        return new Position(row, column);
    }

   
    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    } 

    
    @Override
    public void displayState(GameState state) {
        switch (state) {
            case CHECK -> System.out.println("You are in Failure.");
            case CHECK_MATE -> System.out.println("Checkmate");
            case STALE_MATE -> System.out.println("Equality");
            default -> { System.out.println(state);
            }
        }
    }
}
