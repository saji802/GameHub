package othello;

import javafx.animation.FillTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.util.Duration;

public class Piece {
    private Ellipse circle;
    private Color color;

    /**
     * This is the constructor of Piece class. It creates the
     * piece and assign the features to it.
     */
    public Piece(double squarex, double squarey){
        this.circle = new Ellipse(squarex+34,squarey+34, 30, 30);
    }

    /**
     * This method return the circle representing the piece.
     */
    public Ellipse getCircle() {
        return this.circle;
    }

    /**
     * This method allows other classes change the color of
     * the piece while flipping
     */
    public void setColor(Color piececolor, Boolean isTest) {
        if (isTest) {
            this.color = piececolor;
            this.circle.setFill(piececolor);
        }
        else {
            FillTransition ft = new FillTransition(Duration.millis(600), this.circle, this.color, piececolor);
            ft.setCycleCount(1);
            ft.play();
            this.color = piececolor;
        }
    }

    /**
     * This method returns the color of the piece.
     */
    public Color getColor() {
        return this.color;
    }
}
