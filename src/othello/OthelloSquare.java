package othello;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class OthelloSquare {
    private boolean available;
    private Piece piece;
    private Rectangle square;
    private Pane squarepane;
    private Color color;

    /**
     * This is OthelloSquare constructor. It initializes the instance variables
     * values.
     */
    public OthelloSquare(double xloc, double yloc, double width, double hieght, Color color){
        this.squarepane = new Pane();
        this.square = new Rectangle(xloc, yloc, width, hieght);
        this.color = color;
        this.square.setFill(this.color);
        this.squarepane.getChildren().add(this.square);
        this.available = true;
    }

    /**
     * This is an accessor method so that other
     * classes can access the pane of the square
     * that contains the piece in it
     */
    public Pane getSquarePane() {
        return this.squarepane;
    }

    /**
     * This is an accessor method so that other
     * classes can access the color of the square
     */
    public Color getColor(){
        return this.color;
    }

    /**
     * This method takes in a color and installs
     * a piece of that color in this othello square
     * graphically and logically
     */
    public void installPiece(Color piececolor) {
        this.piece = new Piece(this.square.getX(), this.square.getY());
        this.piece.setColor(piececolor, true);
        this.squarepane.getChildren().add(this.piece.getCircle());
        this.available = false;
    }

    /**
     * This method returns true if the Square had
     * no pieces and false otherwise
     */
    public boolean getStatus() {
        return this.available;
    }

    /**
     * This is an accessor method so that other
     * classes can access the color of the square
     */
    public Color getPieceColor(){
        return this.piece.getColor();
    }

    /**
     * This method is a setter
     * so that other classes can change
     * the color of the OthelloSquare
     */
    public void setColor(Color color) {
        this.color = color;
        this.square.setFill(color);
    }

    /**
     * This method takes in a color and flips
     * the piece color to this color
     */
    public void flipColor(Color color, Boolean isTest) {
        this.piece.setColor(color, isTest);
    }

}
