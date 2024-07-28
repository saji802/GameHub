package Doodle_Jump;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public App() {
    }

    public void start(Stage stage) {
        PaneOrganizer organizer = new PaneOrganizer();
        Scene scene = new Scene(organizer.getRoot(), 600.0, 800.0);
        stage.setScene(scene);
        stage.setTitle("DoodleJump");
        stage.show();
    }

    public static void main(String[] argv) {
        launch(argv);
    }
}