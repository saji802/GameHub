package Doodle_Jump;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class PaneOrganizer {
    private BorderPane root = new BorderPane();

    public PaneOrganizer() {
        ImageView introBackground = new ImageView(new Image("./Doodle_Jump/images/Intro.png"));
        introBackground.setFitHeight(800.0);
        introBackground.setFitWidth(600.0);
        Button playButton = new Button("Play");
        playButton.setPrefSize(175.0, 50.0);
        this.root.getChildren().add(introBackground);
        this.root.setCenter(playButton);
        playButton.setOnAction((e) -> {
            this.root.getChildren().clear();
            new Game(this.root);
        });
    }

    public Pane getRoot() {
        return this.root;
    }
}
