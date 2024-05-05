package Chess;


public class Controller {

    private View view;
    private Model model;

   
    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

   
    public void play() {
        Model game = new Game();
        View view = new TextView(game);

        boolean gameIsOver = false;

        view.displayTitle();

        game.start();

        boolean goodPosition = false;

        Position départ = null;
        Position arrivé;

        while (!gameIsOver) { 
            view.displayPlayer();
            view.displayState(game.getState());
            view.displayBoard();
           
            while (!goodPosition) {
                départ = view.askPosition();
                try {
                    
                    if (game.isCurrentPlayerPosition(départ)) { 
                       
                        if (game.getPossibleMoves(départ).isEmpty()) { 
                            view.displayError("Error.");
                        } else { 
                            goodPosition = true;
                        }
                    } else { 
                        view.displayError("Error.");
                    }
                } catch (Exception e) { 
                    view.displayError(e.getMessage());
                }
            }
            goodPosition = false;
            while (!goodPosition) {
                view.displayMessage("Where do you want to go ?");
                arrivé = view.askPosition();
                try {
                   
                    game.movePiecePosition(départ, arrivé);
                   
                    goodPosition = true;
                } catch (Exception e) { 
                    view.displayError(e.getMessage());
                    goodPosition = true;
                }
            }
            goodPosition = false;
            gameIsOver = game.getState() == GameState.CHECK_MATE || game.getState() == GameState.STALE_MATE;
        }
        view.displayWinner();
    }
}
