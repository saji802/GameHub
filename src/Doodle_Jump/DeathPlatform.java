package Doodle_Jump;

import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class DeathPlatform extends Platform {
    private Doodle doodle;

    public DeathPlatform(BorderPane root, double x, double y, Color color, Doodle doodle, Game game) {
        super(root, x, y, color, doodle);
        this.doodle = doodle;
    }

    public void intersection() {
        this.doodle.reactToGrey();
    }
}
