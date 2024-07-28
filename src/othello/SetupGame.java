package othello;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class SetupGame {
    private Pane gamepane;
    private Board board;
    private Referee referee;

    /**
     * This is the constructor of SetupGame class. It initializes the
     * instance variables gamepane, board, and referee.
     */
    public SetupGame(Pane gamepane) {
        this.gamepane = gamepane;
        this.board = new Board(this.gamepane);
        this.referee = new Referee(this.board);
    }

    /**
     * This method sets the players of the current game after Apply Settings
     * it tells the referee which players are playing, human or computer of level 1
     * , 2, or 3.
     */
    public void setPlayer(int whitePlayerMode, int blackPlayerMode, Color playerone, Color playertwo) {
        this.board.setPlayers(playerone, playertwo);
        switch (whitePlayerMode) {
            case 0:
                this.referee.addPlayer(new HumanPlayer(this.referee, this.board, playerone, this.gamepane, true));
                break;
            case 1:
                this.referee.addPlayer(new ComputerPlayer(this.referee, this.board, playerone, playertwo, 1, true));
                break;
            case 2:
                this.referee.addPlayer(new ComputerPlayer(this.referee, this.board, playerone, playertwo, 2, true));
                break;
            default:
                this.referee.addPlayer(new ComputerPlayer(this.referee, this.board, playerone, playertwo, 3, true));
                break;
        }
        switch (blackPlayerMode) {
            case 0:
                this.referee.addPlayer(new HumanPlayer(this.referee, this.board, playertwo, this.gamepane, false));
                break;
            case 1:
                this.referee.addPlayer(new ComputerPlayer(this.referee, this.board, playertwo, playerone,  1, false));
                break;
            case 2:
                this.referee.addPlayer(new ComputerPlayer(this.referee, this.board, playertwo, playerone,  2, false));
                break;
            default:
                this.referee.addPlayer(new ComputerPlayer(this.referee, this.board, playertwo, playerone, 3, false));
                break;
        }
    }

    /**
     * This method is an accessor so that Controls class can access
     * the score and turn labels that are in referee class.
     */
    public Label[] getStrings() {
        return this.referee.getStrings();
    }

    /**
     * This method is called by Controls class when
     * Reset button is clicked. It creates another starting board
     * and passes it to the referee.
     */
    public void resetBoard() {
        this.board = new Board(this.gamepane);
        this.referee.reset(this.board);
    }

    /**
     * This method makes sure that nothing will happen if reset
     * button was clicked without starting a game once (without giving
     * values to players) to avoid NullPointerException. It returns true
     * if both values are initialized and false otherwise.
     */
    public boolean isInitiated() {
        if (this.referee.hasPlayers())
            return true;
        else
            return false;
    }
}
