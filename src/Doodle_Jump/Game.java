package Doodle_Jump;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import com.opencsv.CSVWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

public class Game {
    private static final char DELIMITER = ',';
    private BorderPane root;
    private Doodle doodle;
    private ArrayList<Platform> platforms;
    private Timeline timeLine;
    private Platform currentPlat;
    private Controller controller;
    private boolean playing;
    private ArrayList<ArrayList<String>> pq;

    public Game(BorderPane ourroot) {
        this.root = ourroot;
        this.restoreDate();
        ImageView imageView = new ImageView(new Image("Doodle_Jump/images/InstructBackground.png"));
        imageView.setFitHeight(800.0);
        imageView.setFitWidth(600.0);
        this.root.getChildren().add(imageView);
        this.setInstructionsPage();
        Button startGame = new Button("Start Game");
        startGame.setPrefSize(200.0, 50.0);
        startGame.setOnAction((e) -> {
            this.root.getChildren().clear();
            this.startGame();
        });
        HBox bottomCenterBox = new HBox();
        bottomCenterBox.setAlignment(Pos.CENTER);
        bottomCenterBox.getChildren().add(startGame);
        this.root.setBottom(bottomCenterBox);
        this.playing = true;
    }

    private void restoreDate() {
        CSVFormat format = CSVFormat.RFC4180.withDelimiter(DELIMITER).withHeader();
        CSVParser parser;

        try {
            parser = new CSVParser(new FileReader("src/Doodle_Jump/Scores.csv"), format);
        } catch (IOException e) {
            this.pq = new ArrayList<ArrayList<String>>();
            return;
        }
        this.pq = new ArrayList<ArrayList<String>>();
        for (CSVRecord record : parser) {
            ArrayList<String> row = new ArrayList<>();
            row.add(String.valueOf(record.get("Score")));
            row.add(String.valueOf(record.get("Date")));
            row.add(String.valueOf(record.get("Time")));
            this.pq.add(row);
        }
    }

    private void setInstructionsPage() {
        VBox centerBox = new VBox();
        Text intro = new Text("\n \n \n \n \n Welcome to my modified version of DoodleJump! As you can see, \nI borrowed doodle from among us. Here's what you need to know before playing:\n");
        intro.setFont(Font.font("Arial", FontPosture.ITALIC, 16.0));
        intro.setFill(Color.BLACK);
        Text controls = new Text("Controls:\n");
        controls.setFont(Font.font("Tahoma", FontWeight.BOLD, 18.0));
        controls.setFill(Color.ORANGE);
        Text movement = new Text("Use the left and right arrow keys to move the character. You can wrap around the screen \n from both sides");
        movement.setFont(Font.font("Arial", 14.0));
        movement.setFill(Color.BLACK);
        Text platforms = new Text("Platforms:\n");
        platforms.setFont(Font.font("Tahoma", FontWeight.BOLD, 18.0));
        platforms.setFill(Color.ORANGE);
        Text black = new Text("Black Platform (Standard): This is the standard platform. You can bounce\non it as much as you want.You will not see a lot of it when difficulty increases.\n \n \n");
        black.setFont(Font.font("Arial", FontPosture.ITALIC, 14.0));
        black.setFill(Color.BLACK);
        Text blue = new Text("Blue Platform (Moving): This is the moving platform. when difficulty increases \nit will start moving faster proportionally with the difficulty level\n \n \n");
        blue.setFont(Font.font("Arial", FontPosture.ITALIC, 14.0));
        blue.setFill(Color.BLUE);
        Text red = new Text("Red Platform (Disappearing): This is the disappearing platform. You can bounce\non it only for onceYou will see many of it when difficulty increases.\n \n \n");
        red.setFont(Font.font("Arial", FontPosture.ITALIC, 14.0));
        red.setFill(Color.RED);
        Text green = new Text("Green Platform (Extra Bounce): This is the extra bounce platform. When you bounce on it\nit will provide youwith a jump double the normal one.\n \n \n");
        green.setFont(Font.font("Arial", FontPosture.ITALIC, 14.0));
        green.setFill(Color.GREEN);
        Text grey = new Text("Grey Platform (Death): This is the death platform. if you bounce on it\nyou will lose the game.You will start seeing it after level 5 of difficulty.\n \n");
        grey.setFont(Font.font("Arial", FontPosture.ITALIC, 14.0));
        grey.setFill(Color.GREY);
        Text quitRestart = new Text("Quit / Restart / Pause:\n");
        quitRestart.setFont(Font.font("Tahoma", FontWeight.BOLD, 18.0));
        quitRestart.setFill(Color.ORANGE);
        Text content = new Text("After the game is over, click the restart button to play again.\nClick on the quit button to exit the game. Press (P) to pause/continue playing");
        content.setFont(Font.font("Arial", FontPosture.ITALIC, 14.0));
        content.setFill(Color.BLACK);
        centerBox.getChildren().addAll(new Node[]{intro, controls, movement, platforms, black, green, red, blue, grey, quitRestart, content});
        centerBox.setAlignment(Pos.CENTER);
        this.root.setCenter(centerBox);
    }

    private void startGame() {
        this.platforms = new ArrayList();
        this.background();
        this.controller = new Controller(this.root);
        this.doodle = new Doodle(this.root, this);
        this.platforms.add(new Platform(this.root, 300.0, 740.0, Color.BLACK, this.doodle));
        this.setUpPlatforms(720.0);
        this.currentPlat = (Platform) this.platforms.get(0);
        this.setUpTimeline();
    }

    public void setUpButtons() {
        Button exit = new Button("Quit");
        exit.setOnAction((e) -> {
            this.storeData();
            System.exit(0);
        });
        Button restart = new Button("Restart");
        restart.setOnAction((e) -> {
            this.root.getChildren().clear();
            this.startGame();
        });
        HBox hbox = new HBox();
        hbox.setSpacing(20.0);
        this.root.setBottom(hbox);
        hbox.getChildren().addAll(new Node[]{exit, restart});
        hbox.setAlignment(Pos.CENTER);
    }

    private void storeData() {
        int count = 0;
        try {
            CSVWriter writer = new CSVWriter(new FileWriter("src/Doodle_Jump/Scores.csv"));
            String[] header = {"Score", "Date", "Time"};
            writer.writeNext(header);
            while (count < this.pq.size() && count < 15) {
                String[] row = {String.valueOf(this.pq.get(count).get(0)),
                        String.valueOf(this.pq.get(count).get(1)), String.valueOf(this.pq.get(count).get(2))};
                writer.writeNext(row);
                count++;
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("error while writing to the file");
        }
    }

    public void setUpPlatforms(double lastY) {
        double y = lastY;

        while (y > 100.0) {
            double x = (double) ((int) (525.0 * Math.random()));
            y -= 50.0 + (double) ((int) (40.0 * Math.random()));
            int ran;
            if (this.controller.getDifficultty() < 10) {
                ran = (int) (Math.random() * (double) (10 - this.controller.getDifficultty() / 2));
            } else {
                ran = (int) (Math.random() * 4.0) - 1;
            }

            if (this.currentPlat == null) {
                this.currentPlat = new Platform(this.root, x, y, Color.BLACK, this.doodle);
                this.platforms.add(this.currentPlat);
            } else {
                if (this.currentPlat.getColor() == Color.GREY) {
                    ran = (int) (Math.random() * 4.0);
                }

                switch (ran) {
                    case -1:
                        this.currentPlat = new DeathPlatform(this.root, x, y, Color.GREY, this.doodle, this);
                        this.platforms.add(this.currentPlat);
                        break;
                    case 0:
                        this.currentPlat = new DisappearingPlatform(this.root, x, y, Color.RED, this.doodle);
                        this.platforms.add(this.currentPlat);
                        break;
                    case 1:
                        this.currentPlat = new MovingPlatform(this.root, x, y, Color.BLUE, this.controller, this.doodle);
                        this.platforms.add(this.currentPlat);
                        break;
                    case 2:
                        this.currentPlat = new ExtraBouncePlatform(this.root, x, y, Color.GREEN, this.doodle);
                        this.platforms.add(this.currentPlat);
                        break;
                    default:
                        this.currentPlat = new Platform(this.root, x, y, Color.BLACK, this.doodle);
                        this.platforms.add(this.currentPlat);
                }
            }
        }

    }

    public void setUpTimeline() {
        KeyFrame kf = new KeyFrame(Duration.seconds(0.016), (e) -> {
            this.doodle.jump();
            this.checkIntersection();
            this.fillSpace();
        }, new KeyValue[0]);
        this.timeLine = new Timeline(new KeyFrame[]{kf});
        this.timeLine.setCycleCount(-1);
        this.timeLine.play();
    }

    private void fillSpace() {
        if (((Platform) this.platforms.get(this.platforms.size() - 1)).getY() >= 100.0) {
            double startingY = ((Platform) this.platforms.get(this.platforms.size() - 1)).getY();
            this.setUpPlatforms(startingY);
        }

    }

    private void checkIntersection() {
        for (int i = 0; i < this.platforms.size(); ++i) {
            if (((Platform) this.platforms.get(i)).check()) {
                if (((Platform) this.platforms.get(i)).getColor() == Color.RED) {
                    ((Platform) this.platforms.get(i)).intersection();
                    --i;
                } else {
                    ((Platform) this.platforms.get(i)).intersection();
                }
            }
        }

    }

    public void setUpGameOver() {
        Label gameOverLabel = new Label("Game Over");
        this.root.setCenter(gameOverLabel);
        gameOverLabel.setFont(Font.font(60.0));
        gameOverLabel.setTextFill(Color.RED);
        this.timeLine.stop();
        this.doodle.stopControlDoodle();
        this.setUpButtons();
        this.manageLeaderBoard();
    }

    public void manageLeaderBoard() {
        for (int i = 0; i < this.pq.size(); i++) {
            int num = Integer.parseInt(this.pq.get(i).get(0));
            if (this.controller.getScore() > num) {
                ArrayList<String> row = new ArrayList<>();
                row.add(String.valueOf(this.controller.getScore()));
                row.add(String.valueOf(LocalDate.now()));
                row.add(String.valueOf(LocalTime.now()));
                this.pq.add(i, row);
                return;
            }
        }
        ArrayList<String> row = new ArrayList<>();
        row.add(String.valueOf(this.controller.getScore()));
        row.add(String.valueOf(LocalDate.now()));
        row.add(String.valueOf(LocalTime.now()));
        this.pq.add(row);
    }

    public void background() {
        ImageView imageView = new ImageView(new Image("Doodle_Jump/images/Background.png"));
        imageView.setFitHeight(800.0);
        imageView.setFitWidth(600.0);
        this.root.getChildren().add(imageView);
    }

    public void cleanPlatforms() {
        for (int i = 0; i < this.platforms.size(); ++i) {
            if (((Platform) this.platforms.get(i)).getY() > 760.0) {
                Platform erased = (Platform) this.platforms.remove(0);
                erased.removePane();
                --i;
            }
        }

    }

    public void checkGameOver() {
        if (this.doodle.getBody().getY() > 800.0) {
            this.setUpGameOver();
        }

    }

    public void changeScore() {
        this.controller.updateLabels();
    }

    public void scrollPlatforms() {
        double diff = 390.0 - this.doodle.getBody().getY();
        this.doodle.setYLock(390.0);
        Iterator var3 = this.platforms.iterator();

        while (var3.hasNext()) {
            Platform platform = (Platform) var3.next();
            platform.setYloc(diff);
        }

    }

    public void removeRed(Platform platform) {
        this.platforms.remove(platform);
    }

    public void pauseReplay() {
        if (this.playing) {
            this.timeLine.stop();
        } else {
            this.timeLine.play();
        }

        this.playing = !this.playing;
    }
}
