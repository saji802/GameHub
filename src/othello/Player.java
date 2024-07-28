package othello;

import javafx.scene.paint.Color;

public interface Player {

    void makemove();

    Color getPlayerColor();

    boolean isLeft();
}
