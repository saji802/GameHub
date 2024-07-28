package othello;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Board {
    private OthelloSquare[][] squares;
    private Pane root;
    private int blackscore;
    private int whitescore;
    private Color playerone;
    private Color playertwo;
    private Move prevmove;

    /**
     * This is Board's constructor. It initializes the values of
     * the instance variables then it calls a fillScreen method to implement
     * the 2D array board.
     */
    public Board(Pane root) {
        this.root = root;
        this.blackscore = 0;
        this.whitescore = 0;
        this.squares = new OthelloSquare[10][10];
        this.playerone = Color.WHITE;
        this.playertwo = Color.BLACK;
        this.fillScreen();
        this.prevmove = new Move(0, 0);
    }

    /**
     * This is Board's copy constructor. It copies the passed board parameter
     * so that computer can have a logical copy of the board to try out
     * some possible moves
     */
    public Board(Board board) {
        this.squares = new OthelloSquare[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.squares[i][j] = new OthelloSquare(j * 70, i * 70, 68, 68,
                        board.getSquare(i, j).getColor());
                if (!board.getSquare(i, j).getStatus()) {
                    this.squares[i][j].installPiece(board.getSquare(i, j).getPieceColor());
                }
            }
        }
    }

    /**
     * This method implements the 2D array board graphically and logically by filling
     * it with OthelloSquares and the initial four pieces in the middle adn then add to
     * the gamepane.
     */
    private void fillScreen() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (j * i == 0 || j == 9 || i == 9) {
                    this.squares[i][j] = new OthelloSquare(j * 70, i * 70,
                            68, 68, Color.CRIMSON);
                } else {
                    this.squares[i][j] = new OthelloSquare(j * 70, i * 70,
                            68, 68, Color.FORESTGREEN);
                    if ((i + j == 8 || i + j == 10) && i - j == 0) {
                        this.squares[i][j].installPiece(this.playerone);
                    }
                    if (i + j == 9 && (i - j == 1 || i - j == -1))
                        this.squares[i][j].installPiece(this.playertwo);
                }
                this.root.getChildren().add(this.squares[i][j].getSquarePane());
            }
        }
    }

    /**
     * This methods takes in two integers row and col, then
     * returns the OthelloSquare from the board that corresponds
     * to them.
     */
    public OthelloSquare getSquare(int row, int col) {
        return this.squares[row][col];
    }

    /**
     * This method checks for available moves for the current
     * player color their square differently (with grey)
     */
    public void showAvailable(Color player) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (this.checkValid(i, j, player, true) && this.squares[i][j].getStatus())
                    this.squares[i][j].setColor(Color.GREY);
                else
                    this.squares[i][j].setColor(Color.FORESTGREEN);
            }
        }
    }

    /**
     * This method takes in a row, col, player's colour. It returns
     * true if the player is allowed to play in the corresponding square
     * and false otherwise.
     */
    public boolean checkValid(int row, int col, Color player, Boolean isTest) {
        boolean valid = false;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0))
                    valid = valid || this.checkOneDirection(row, col, i, j,
                            player, false, false, isTest);
            }
        }
        return valid;
    }

    /**
     * This method checks if placing a piece in a certain place
     * will make a sandwich in a certain direction. It returns true if
     * so and false otherwise.
     */
    public boolean checkOneDirection(int row, int col, int rowdir, int coldir,
                                     Color color, boolean seenOpponent, boolean flippingMode, boolean isTest) {
        OthelloSquare testSquare = this.squares[row + rowdir][col + coldir];
        if (flippingMode) {
            if (testSquare.getPieceColor() == color)
                return true;
            else {
                testSquare.flipColor(color, isTest);
                this.checkOneDirection(row + rowdir, col + coldir,
                        rowdir, coldir, color, false, true, isTest);
            }
        }

        if (testSquare.getStatus() || testSquare.getColor() == Color.CRIMSON)
            return false;
        if (testSquare.getPieceColor() != color)
            return this.checkOneDirection(row + rowdir, col + coldir,
                    rowdir, coldir, color, true, false, isTest);
        if (testSquare.getPieceColor() == color && seenOpponent)
            return true;
        return false;
    }

    /**
     * This method checks if the game ended or if
     * there should be a turn skipping or the game
     * should simply continue
     */
    public int checkGameEnd(Color player) {
        Color otherplayer;
        if (player == this.playerone)
            otherplayer = this.playertwo;
        else
            otherplayer = this.playerone;
        boolean playable = false;
        boolean currentplayer = false;
        boolean nextplayer = false;
        boolean full = true;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (this.squares[i][j].getStatus()) {
                    full = false;
                    currentplayer = currentplayer || this.checkValid(i, j, player, true);
                    nextplayer = nextplayer || this.checkValid(i, j, otherplayer, true);
                    playable = playable || currentplayer || nextplayer;
                }
            }
        }
        if (!playable || full) {
            return 0;
        } else {
            if (!currentplayer) {
                return 1;
            }
        }
        return 2;
    }


    /**
     * This method updates the score label to keep them
     * up-to-date with our current board
     */
    public void UpdateScore() {
        int blackcount = 0;
        int whitecount = 0;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (!this.squares[i][j].getStatus()) {
                    if (this.squares[i][j].getPieceColor() == this.playertwo)
                        blackcount++;
                    else
                        whitecount++;
                }
            }
        }
        this.blackscore = blackcount;
        this.whitescore = whitecount;
    }

    /**
     * This method returns a string stating the
     * results of the game, the winner (if applicable)
     * or a tie.
     */
    public String getWinner() {
        if (this.blackscore > this.whitescore)
            return "GAMEOVER, " + this.getColorName(this.playertwo) + " PLAYER WINS";
        if (this.blackscore < this.whitescore)
            return "GAMEOVER, " + this.getColorName(this.playerone) + " PLAYER WINS";
        return "GAMEOVER, IT'S A TIE";
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
     * This method takes in a color and returns
     * the score of the corresponding player
     */
    public int getScore(Color player) {
        if (player == this.playertwo)
            return this.blackscore;
        else
            return this.whitescore;
    }

    /**
     * This method is the way the board knows about
     * the chosen colors of the players, so it functions
     * accordingly
     */
    public void setPlayers(Color playerone, Color playertwo) {
        this.editScreen(this.playerone, playerone, playertwo);
        this.playerone = playerone;
        this.playertwo = playertwo;
    }

    /**
     * This method edits the existing screen so that
     * colors of the already-played pieces change when
     * colors are changed in the middle of the game
     */
    private void editScreen(Color colorone, Color newcolorone, Color newcolortwo) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (!this.squares[i][j].getStatus()) {
                    if (this.squares[i][j].getPieceColor() == colorone)
                        this.squares[i][j].flipColor(newcolorone, true);
                    else
                        this.squares[i][j].flipColor(newcolortwo, true);
                }
            }
        }
    }

    /**
     * This method takes in the location of the most
     * recent played piece and turns its square to Brown
     * It also stores it in prevmove so that the color of
     * the Brown square returns to normal after another move
     * is played
     */
    public void arrangeLastMove(int row, int col) {
        if (this.prevmove.row != 0)
            this.squares[this.prevmove.row][this.prevmove.col].setColor(Color.GREEN);
        this.squares[row][col].setColor(Color.BROWN);
        this.prevmove.row = row;
        this.prevmove.col = col;

    }

    /**
     * This method return all the square to green after a Human
     * player has played
     */
    public void returnDefault() {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                this.squares[i][j].setColor(Color.FORESTGREEN);
            }
        }
    }
}
