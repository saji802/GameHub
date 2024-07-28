
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public void start(Stage stage) {
        LandingScreen organizer = new LandingScreen();
        Scene scene = new Scene(organizer.getRoot(), 800.0, 800.0);
        stage.setScene(scene);
        stage.setTitle("Game Hub");
        stage.show();
    }

    public static void main(String[] argv) {
        launch(argv);
    }
}
