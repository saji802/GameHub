package othello;

public class Move {
    public int value;
    public int col;
    public int row;

    /**
     * This is the constructor of Move class. It initializes the values
     * of instance variables using the passed parameters.
     */
    public Move(int row, int col){
        this.row = row;
        this.col = col;
    }

}
