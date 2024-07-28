package othello;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;



public class PaneOrganizer {
    private Pane root;
    private Pane gamepane;

    /**
     * This is PaneOrganizer's constructor. It creates the root pane and
     * the game pane, get the controls pane from Controls class, then adds game pane
     * and controls pane to root pane and takes care of their layout.
     */
    public PaneOrganizer(){
        this.root = new Pane();
        this.root.setStyle("-fx-background-color: grey");
        this.gamepane = new Pane();
        this.gamepane.setPrefSize(700, 700);
        this.gamepane.setStyle("-fx-background-color: BlACK");
        this.root.getChildren().add(this.gamepane);
        this.setIntroScreen();
    }

    private void setIntroScreen() {
        ImageView imageView = new ImageView(new Image("othello/images/background.jpg"));
        imageView.setX(0);
        imageView.setY(0);
        this.gamepane.getChildren().add(imageView);
        VBox vbox = new VBox();
        this.root.getChildren().add(vbox);
        vbox.setSpacing(10);
        vbox.setLayoutX(750);
        vbox.setLayoutY(300);
        Button play = new Button("Play");
        play.setPrefSize(150,50);
        Button instructions= new Button("Instructions");
        instructions.setPrefSize(150,50);
        play.setOnAction((ActionEvent e) -> {
            this.gamepane.getChildren().clear();
            this.root.getChildren().remove(vbox);
            SetupGame game = new SetupGame(this.gamepane);
            Controls controls = new Controls(game);
            this.root.getChildren().add(controls.getPane());
            controls.getPane().setLayoutX(700);
            controls.getPane().setLayoutY(125);
            controls.getPane().setStyle("-fx-background-color: grey");
        });
        instructions.setOnAction((ActionEvent e) -> {
            this.gamepane.getChildren().clear();
            vbox.getChildren().remove(instructions);
            this.setUpInstructions();
        });
        vbox.getChildren().addAll(play, instructions);
    }

    private void setUpInstructions() {
        ImageView background = new ImageView(new Image("othello/images/instructions background.png"));
        background.setX(0);
        background.setY(0);
        ImageView instructions = new ImageView(new Image("othello/images/instructions.png"));
        instructions.setX(0);
        instructions.setY(0);
        this.gamepane.getChildren().addAll(background, instructions);

    }

    /**
     * This method is an accessor method so that App can access the root pane
     */
    public Pane getRoot() {
        return this.root;
    }
}
