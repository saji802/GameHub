package Doodle_Jump;

import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class DisappearingPlatform extends Platform {
    private Doodle doodle;

    public DisappearingPlatform(BorderPane root, double x, double y, Color color, Doodle doodle) {
        super(root, x, y, color, doodle);
        this.doodle = doodle;
    }

    public void intersection() {
        this.doodle.reactToRed(this);
    }
}
