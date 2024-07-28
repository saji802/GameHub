package othello;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;


public class Referee {

    private Board board;
    private Timeline switchturn;
    private Player currPlayer;
    private Player nextPlayer;
    private Label scorelabel;
    private Label turnslabel;
    private Color colorone;
    private Color colortwo;
    private int count;

    /**
     * This is Referee's constructor. It initializes the values of
     * the instance variables.
     */
    public Referee(Board board) {
        this.board = board;
        this.scorelabel = new Label("WHITE:2    BLACK:2");
        this.turnslabel = new Label();
        this.count = 0;
    }

    /**
     * This method adds the player recieved from SetupGame class
     * to refree Class and stores them as current and next player
     */
    public void addPlayer(Player player) {
        if (this.currPlayer == null) {
            this.currPlayer = player;
            this.colorone = this.currPlayer.getPlayerColor();
            this.board.showAvailable(this.currPlayer.getPlayerColor());
            this.turnslabel.setText(this.getColorName(this.currPlayer.getPlayerColor()) + " PLAYER'S TURN");
        } else if (this.nextPlayer == null) {
            this.nextPlayer = player;
            this.colortwo = this.nextPlayer.getPlayerColor();
            this.alternate();
            if (this.board.getScore(this.colorone) == 0)
                this.scorelabel.setText(this.getColorName(this.currPlayer.getPlayerColor()) + ": 2"
                        + "    " + this.getColorName(this.nextPlayer.getPlayerColor()) + ": 2");
        } else {
            if (this.currPlayer.isLeft() == player.isLeft()) {
                this.currPlayer = player;
                this.colorone = this.currPlayer.getPlayerColor();
                this.count++;
            } else {
                this.nextPlayer = player;
                this.colortwo = this.nextPlayer.getPlayerColor();
                this.count++;
            }
            if (this.board.getScore(this.colorone) == 0)
                this.scorelabel.setText(this.getColorName(this.currPlayer.getPlayerColor()) + ": 2"
                        + "    " + this.getColorName(this.nextPlayer.getPlayerColor()) + ": 2");
            else
                this.scorelabel.setText(this.getColorName(this.currPlayer.getPlayerColor()) + ": " + this.board.getScore(this.currPlayer.getPlayerColor())
                    + "    " + this.getColorName(this.nextPlayer.getPlayerColor()) + ": " + this.board.getScore(this.nextPlayer.getPlayerColor()));
            this.turnslabel.setText(this.getColorName(this.currPlayer.getPlayerColor()) + " PLAYER'S TURN");
            if (this.count == 2) {
                this.currPlayer.makemove();
                this.count = 0;
            }
        }

    }

    /**
     * This method uses a timeline to handle alternating turns
     * between players
     */
    private void alternate() {
        KeyFrame kf = new KeyFrame(Duration.millis(20), (ActionEvent a) -> this.askToMove());
        this.switchturn = new Timeline(kf);
        this.switchturn.setCycleCount(Animation.INDEFINITE);
        this.switchturn.play();
    }

    /**
     * This method tells the current player that it's allowed to move
     */
    private void askToMove() {
        this.switchturn.stop();
        if (this.currPlayer != null)
            this.currPlayer.makemove();
    }

    /**
     * This method handles the logic that should be done
     * when we switch turns. Updating scores, switching players
     * change turns label, replay the timeline, check if the last
     * move ended the game.
     */
    public void finishTurn(boolean human) {
        Player temp = this.currPlayer;
        this.currPlayer = this.nextPlayer;
        this.nextPlayer = temp;
        this.board.UpdateScore();
        this.setUpScore();
        int status = this.board.checkGameEnd(this.currPlayer.getPlayerColor());
        switch (status) {
            case 0:
                this.setUpGameOver();
                return;
            case 1:
                this.finishTurn(human);
                return;
            default:
                break;
        }
        if (human)
            this.board.showAvailable(this.currPlayer.getPlayerColor());
        if (this.currPlayer.getPlayerColor() == this.colorone) {
            this.setUpTurns(this.getColorName(this.colorone) + " PLAYER'S TURN");
        } else
            this.setUpTurns(this.getColorName(this.colortwo) + " PLAYER'S TURN");
        this.switchturn.play();
    }

    /**
     * This method takes in a color value and returns it
     * as a string
     */
    private String getColorName(Color color) {
        if (color == Color.RED)
            return "RED";
        if (color == Color.BLACK)
            return "BLACK";
        if (color == Color.WHITE)
            return "WHITE";
        if (color == Color.YELLOW)
            return "YELLOW";
        if (color == Color.BLUE)
            return "BLUE";
        return null;
    }

    /**
     * This method is called when the game is over.
     * It changes the labels turn to display
     * that the game has ended with the results
     */
    private void setUpGameOver() {
        String results = this.board.getWinner();
        this.turnslabel.setText(results);
    }

    /**
     * This method changes the scores label to display
     * the current score
     */
    private void setUpScore() {
        this.scorelabel.setText(this.getColorName(this.colorone) + ": " + this.board.getScore(this.colorone) + " " +
                "   " + this.getColorName(this.colortwo) + ": " + this.board.getScore(this.colortwo));
    }

    /**
     * This method changes the turn label to
     * display which player is to go now
     */
    private void setUpTurns(String scores) {
        this.turnslabel.setText(scores);
    }

    /**
     * This method returns the labels
     */
    public Label[] getStrings() {
        return new Label[]{this.scorelabel, this.turnslabel};
    }

    /**
     * This is called to reset refree settings when
     * Reset button is clicked. It resets all
     * the labels to default and sets the players
     * to null
     */
    public void reset(Board board) {
        this.scorelabel.setText("WHITE: 2       BLACK: 2");
        String color = this.getColorName(this.currPlayer.getPlayerColor());
        this.turnslabel.setText(color + " PLAYER'S TURN");
        this.currPlayer = null;
        this.nextPlayer = null;
        this.board = board;
    }

    /**
     * This method returns true if players are initialized and
     * false otherwise
     */
    public boolean hasPlayers() {
        if (this.currPlayer != null && this.nextPlayer != null)
            return true;
        else
            return false;
    }
}
