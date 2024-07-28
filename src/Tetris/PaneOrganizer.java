package Tetris;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class PaneOrganizer {
    private Pane root = new Pane();

    public PaneOrganizer() {
        Pane pane = new Pane();
        Pane controlpane = new Pane();
        controlpane.setPrefSize(227.0, 800.0);
        controlpane.setStyle("-fx-background-color: DIMGREY");
        controlpane.setLayoutX(573.0);
        Pane displaypane = new Pane();
        displaypane.setPrefSize(150.0, 150.0);
        displaypane.setStyle("-fx-background-color: White");
        displaypane.setLayoutX(40.0);
        displaypane.setLayoutY(150.0);
        controlpane.getChildren().add(displaypane);
        this.root.getChildren().addAll(new Node[]{pane, controlpane});
        new Game(this.root, pane, controlpane);
    }

    public Pane getRoot() {
        return this.root;
    }
}
