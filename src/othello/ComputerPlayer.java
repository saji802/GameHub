package othello;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ComputerPlayer implements Player {
    private Referee referee;
    private Color player;
    private Board board;
    private int level;
    private boolean left;
    private Color colorcurr;
    private Color colorother;

    /**
     * This is the constructor of ComputerPlayer. It initializes the instance
     * variables of this class with the passes values
     */
    public ComputerPlayer(Referee referee, Board board, Color color, Color colorother, int level, boolean left) {
        this.referee = referee;
        this.player = color;
        this.board = board;
        this.level = level;
        this.left = left;
        this.colorcurr = color;
        this.colorother = colorother;
    }

    /**
     * This method makes a move by calling getBestMove method.
     * Then, it flips pieces and install the piece on the chosen
     * square from the original board
     */
    @Override
    public void makemove() {
        Move move = this.getBestMove(this.level, this.player, new Board(this.board), -10000000, 10000000);
        OthelloSquare selected = this.board.getSquare(move.row, move.col);
        if (this.board.checkValid(move.row, move.col, this.player, true) && selected.getStatus()) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (!(i == 0 && j == 0)) {
                        if (this.board.checkOneDirection(move.row, move.col, i, j,
                                this.getPlayerColor(), false, false, true))
                            this.board.checkOneDirection(move.row, move.col, i, j,
                                    this.getPlayerColor(), false, true, true);
                    }
                }
            }
            selected.installPiece(this.player);
            this.referee.finishTurn(true);
            this.board.arrangeLastMove(move.row, move.col);
        }
    }

    /**
     * This method uses MiniMax algorithem to get the computer
     * player to choose the best move available.
     */
    private Move getBestMove(int mode, Color player, Board betaboard, int alpha, int beta) {
        Color otherplayer;
        if (player == this.colorcurr)
            otherplayer = this.colorother;
        else
            otherplayer = this.colorcurr;
        if (betaboard.checkGameEnd(player) == 0) {
            if (betaboard.getScore(player) > betaboard.getScore(otherplayer)){
                Move imaginarymove = new Move(0, 0);
                imaginarymove.value = 3000;
                return imaginarymove;
            }
            if (betaboard.getScore(player) < betaboard.getScore(otherplayer)){
                Move imaginarymove = new Move(0, 0);
                imaginarymove.value = -3000;
                return imaginarymove;
            }
            if (betaboard.getScore(player) == betaboard.getScore(otherplayer)){
                Move imaginarymove = new Move(0, 0);
                imaginarymove.value = 0;
                return imaginarymove;
            }
        }

        ArrayList<Move> legalmoves = new ArrayList<Move>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (betaboard.checkValid(i, j, player, true) && betaboard.getSquare(i, j).getStatus()) {
                    Move legalmove = new Move(i, j);
                    legalmoves.add(legalmove);
                }
            }
        }
        if (legalmoves.isEmpty()) {
            if (mode == 1) {
                Move imaginarymove = new Move(0, 0);
                imaginarymove.value = -3000;
                return imaginarymove;
            } else {
                Move bestothermove = this.getBestMove(mode - 1, otherplayer, betaboard,alpha,beta);
                bestothermove.value *= -1;
                return bestothermove;
            }
        } else {
            Move bestmove = new Move(0, 0);
            bestmove.value = -3000;
            for (Move move : legalmoves) {
                Board testboard= new Board(betaboard);
                OthelloSquare selected = testboard.getSquare(move.row, move.col);
                selected.installPiece(player);
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (!(i == 0 && j == 0)) {
                            if (testboard.checkOneDirection(move.row, move.col, i, j, player, false, false, true))
                                testboard.checkOneDirection(move.row, move.col, i, j, player, false, true,true);
                        }
                    }
                }
                if (mode == 1) {
                    move.value = this.evaluateBoard(testboard, player);
                } else {
                    move.value = -1 * this.getBestMove(mode - 1, otherplayer, testboard,-beta,-alpha).value;
                    if (player == this.player) {
                        alpha = Math.max(alpha, move.value);
                        if (beta <= alpha)
                            break;
                    }
                    else {
                        beta = Math.min(beta, move.value);
                        if (beta <= alpha)
                            break;
                    }
                }
                if (bestmove.value <= move.value)
                    bestmove = move;
            }
            return bestmove;

        }
    }

    /**
     * This method evaluates the value of the board from the
     * perspective of the passed player color
     */
    private int evaluateBoard(Board dummyboard, Color player) {
        int value = 0;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (!dummyboard.getSquare(i, j).getStatus()) {
                    if (dummyboard.getSquare(i, j).getPieceColor() == player)
                        value += Constants.weights[i - 1][j - 1];
                    else
                        value -= Constants.weights[i - 1][j - 1];
                }
            }
        }
        return value;
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
}
