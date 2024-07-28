import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


public class LandingScreen {
    private Pane root;

    public LandingScreen() {
        this.root = new Pane();
        this.setUpScreen();
    }

    private void setUpScreen() {
        ImageView introBackground = new ImageView(new Image("./images/Landing Page.png"));
        introBackground.setFitHeight(800);
        introBackground.setFitWidth(800);
        this.root.getChildren().add(introBackground);
        Button tetris = new Button("Play Tetris!");
        tetris.setPrefSize(150, 50);
        tetris.setLayoutX(50);
        tetris.setLayoutY(360);
        tetris.setOnAction((ActionEvent a) -> this.callAnother("Tetris"));
        Button doodle = new Button("Play Doodle Jump!");
        doodle.setPrefSize(150, 50);
        doodle.setLayoutX(310);
        doodle.setLayoutY(360);
        doodle.setOnAction((ActionEvent a) -> this.callAnother("Doodle_Jump"));
        Button othello = new Button("Play Othello!");
        othello.setPrefSize(150, 50);
        othello.setLayoutX(590);
        othello.setLayoutY(360);
        othello.setOnAction((ActionEvent a) -> this.callAnother("othello"));
        Button leader = new Button("View Leaderboard!");
        leader.setOnAction((ActionEvent a) -> {
            try {
                this.viewLeaderBoard();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        leader.setPrefSize(250, 50);
        leader.setLayoutX(270);
        leader.setLayoutY(450);
        this.root.getChildren().addAll(tetris, othello, doodle, leader);
    }

    private void viewLeaderBoard() throws IOException {

        TableView<String[]> table1 = new TableView<>();
        File file1 = new File("src/Doodle_Jump/Scores.csv");
        List<String> lines1 = Files.readAllLines(Paths.get(file1.toURI()));
        TableView<String[]> table2 = new TableView<>();
        File file2 = new File("src/Tetris/Scores.csv");
        List<String> lines2 = Files.readAllLines(Paths.get(file2.toURI()));

        List<String[]> data1 = lines1.stream()
                .map(line -> line.split(","))
                .collect(Collectors.toList());

        List<String[]> data2 = lines2.stream()
                .map(line -> line.split(","))
                .collect(Collectors.toList());

        String[] headers1 = data1.get(0);
        for (int columnIndex = 0; columnIndex < headers1.length; columnIndex++) {
            final int index = columnIndex;
            TableColumn<String[], String> column = new TableColumn<>(headers1[columnIndex]);
            column.setCellValueFactory(cellDataFeatures -> {
                String[] rowValues = cellDataFeatures.getValue();
                return new SimpleStringProperty(rowValues[index].replaceAll("\"", ""));
            });
            table1.getColumns().add(column);
        }

        String[] headers2 = data2.get(0);
        for (int columnIndex = 0; columnIndex < headers2.length; columnIndex++) {
            final int index = columnIndex;
            TableColumn<String[], String> column = new TableColumn<>(headers2[columnIndex]);
            column.setCellValueFactory(cellDataFeatures -> {
                String[] rowValues = cellDataFeatures.getValue();
                return new SimpleStringProperty(rowValues[index].replaceAll("\"", ""));
            });
            table2.getColumns().add(column);
        }

        data1.remove(0);
        table1.setItems(FXCollections.observableArrayList(data1));
        data2.remove(0);
        table2.setItems(FXCollections.observableArrayList(data2));

        this.root.getChildren().clear();
        ImageView introBackground = new ImageView(new Image("./images/LeaderBoard.png"));
        introBackground.setFitHeight(800);
        introBackground.setFitWidth(800);
        this.root.getChildren().addAll(introBackground, table1);
        table1.setLayoutX(80);
        table1.setLayoutY(220);
        this.root.getChildren().add(table2);
        table2.setLayoutX(500);
        table2.setLayoutY(220);

        Button back = new Button("Back to homepage");
        this.root.getChildren().add(back);
        back.setOnAction((ActionEvent a) -> {this.root.getChildren().clear();
        this.setUpScreen();});
        back.setLayoutX(30);
        back.setLayoutY(30);

    }

    private void callAnother(String proj) {
        try {

            String sourceDirPath = "src/" + proj;
            String outputDirPath = "./out/production/Game_Center";

            File sourceDir = new File(sourceDirPath);
            String[] javaFiles = sourceDir.list((dir, name) -> name.endsWith(".java"));

            if (javaFiles == null || javaFiles.length == 0) {
                System.out.println("No Java source files found in directory: " + sourceDirPath);
                return;
            }

            String[] compileCommand = new String[javaFiles.length + 5];
            compileCommand[0] = "javac";
            compileCommand[1] = "-cp";
            compileCommand[2] = "lib/*";
            compileCommand[3] = "-d";
            compileCommand[4] = outputDirPath;

            for (int i = 0; i < javaFiles.length; i++) {
                compileCommand[i + 5] = sourceDirPath + "/" + javaFiles[i];
            }

            Process compileProcess = Runtime.getRuntime().exec(compileCommand);

            BufferedReader compileError = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
            String compileErrorLine;
            while ((compileErrorLine = compileError.readLine()) != null) {
                if (!compileErrorLine.startsWith("Note"))
                    System.out.println("Compilation Error: " + compileErrorLine);
            }

            int compileExitCode = compileProcess.waitFor();
            if (compileExitCode != 0) {
                System.out.println("Compilation failed with exit code: " + compileExitCode);
                return;
            }
            System.out.println("Compilation successful.");

            String[] runCommand = {
                    "java",
                    "-cp",
                    "out/production/Game_Center:lib/opencsv-5.7.1.jar:lib/commons-csv-1.9.0.jar",
                    proj + ".App"
            };

            Process runProcess = Runtime.getRuntime().exec(runCommand);

            BufferedReader runOutput = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            String runOutputLine;
            while ((runOutputLine = runOutput.readLine()) != null) {
                System.out.println(runOutputLine);
            }

            BufferedReader runError = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
            String runErrorLine;
            while ((runErrorLine = runError.readLine()) != null) {
                System.out.println("Runtime Error: " + runErrorLine);
            }

            int runExitCode = runProcess.waitFor();
            System.out.println("Process exited with code: " + runExitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Parent getRoot() {
        return this.root;
    }
}
