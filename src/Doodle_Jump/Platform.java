package Doodle_Jump;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Platform {
    private Rectangle platform;
    private BorderPane root;
    private Color color;
    private Doodle doodle;

    public Platform(BorderPane root, double x, double y, Color color, Doodle doodle) {
        this.root = root;
        this.platform = new Rectangle(x, y, 75.0, 10.0);
        this.root.getChildren().add(this.platform);
        this.color = color;
        this.platform.setFill(this.color);
        this.doodle = doodle;
    }

    public void intersection() {
        this.doodle.reactToOthers();
    }

    public double getX() {
        return this.platform.getX();
    }

    public void setX(int x) {
        this.platform.setX((double)x);
    }

    public double getY() {
        return this.platform.getY();
    }

    public void setYloc(double diff) {
        this.platform.setY(this.platform.getY() + diff);
    }

    public void removePane() {
        this.root.getChildren().remove(this.platform);
    }

    public Color getColor() {
        return this.color;
    }

    public boolean check() {
        return this.doodle.getLeg1().intersects(this.platform.getX(), this.platform.getY(), 75.0, 10.0) || this.doodle.getLeg2().intersects(this.platform.getX(), this.platform.getY(), 75.0, 10.0);
    }
}
