package othello;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class HumanPlayer implements Player {
    private Referee referee;
    private Color player;
    private Board board;
    private Pane gamepane;
    private boolean left;

    /**
     * This is the constructor of HumanPlayer class. It initializes the instance
     * variables of this class with the passes arguments
     */
    public HumanPlayer(Referee referee, Board board, Color color, Pane gamepane, boolean left) {
        this.referee = referee;
        this.player = color;
        this.board = board;
        this.gamepane = gamepane;
        this.left = left;
    }

    /**
     * This method sets teh gamepane responsive to mouse clicks.
     *if the clicked square it makes a move then tell referee to finish
     * the turn, otherwise it does nothing.
     */
    private void click(MouseEvent event) {
        int row = (int) event.getY() / 70;
        int col = (int) event.getX() / 70;
        OthelloSquare targeted = this.board.getSquare(row, col);
        if (targeted.getColor() == Color.GREY) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (!(i == 0 && j == 0)) {
                        if (this.board.checkOneDirection(row, col, i, j, this.getPlayerColor(),
                                false, false, false))
                            this.board.checkOneDirection(row, col, i, j, this.getPlayerColor(),
                                    false, true, false);
                    }
                }
            }
            OthelloSquare selected = this.board.getSquare(row, col);
            selected.installPiece(this.player);
            this.gamepane.setOnMouseClicked(null);
            this.board.returnDefault();
            this.referee.finishTurn(true);
            this.board.arrangeLastMove(row, col);
        }
    }

    /**
     * This method returns the player's color
     */
    @Override
    public Color getPlayerColor() {
        return this.player;
    }

    /**
     * This method returns true if the player
     * is on the left side of the menu (in controlspane)
     */
    @Override
    public boolean isLeft() {
        return this.left;
    }

    /**
     * This method calls a method that sets the gamepane
     * responsive to mouse clicks so that the player can
     * play
     */
    @Override
    public void makemove() {
        this.gamepane.setOnMouseClicked((MouseEvent mouse) -> this.click(mouse));
    }

}
