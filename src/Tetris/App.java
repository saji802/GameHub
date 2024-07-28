package Tetris;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class App extends Application {

    public void start(Stage stage) {
        PaneOrganizer organizer = new PaneOrganizer();
        Scene scene = new Scene(organizer.getRoot(), 800.0, 800.0);
        stage.setScene(scene);
        stage.setTitle("Tetris");
        stage.show();
        Button resetbutton = new Button("Reset");
        organizer.getRoot().getChildren().add(resetbutton);
        resetbutton.setPrefSize(150.0, 50.0);
        resetbutton.setLayoutY(740.0);
        resetbutton.setLayoutX(620.0);
        resetbutton.setOnAction((a) -> {
            stage.close();
            PaneOrganizer organizer1 = new PaneOrganizer();
            Scene scene1 = new Scene(organizer1.getRoot(), 800.0, 800.0);
            organizer1.getRoot().getChildren().add(resetbutton);
            stage.setScene(scene1);
            stage.setTitle("Tetris");
            stage.show();
        });
    }

    public static void main(String[] argv) {
        launch(argv);
    }
}

