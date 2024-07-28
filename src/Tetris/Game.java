package Tetris;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import com.opencsv.CSVWriter;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.Pair;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Game {
    private Pane root;
    private static final char DELIMITER = ',';
    private Rectangle[][] board;
    private Piece currpiece;
    private Piece piece;
    private Pane piecespane;
    private Timeline timeline;
    private Label label;
    private Controller controller;
    private ArrayList<Integer> randfig;
    private ArrayList<Integer> randcol;
    private ArrayList<ArrayList<String>> pq;

    public Game(Pane root, Pane piecepane, Pane controlpane) {
        this.piecespane = piecepane;
        this.randfig = new ArrayList();
        this.randcol = new ArrayList();
        this.restoreDate();
        int ran1 = (int)(Math.random() * 7.0);
        int ran2 = (int)(Math.random() * 7.0);
        this.randfig.add(ran1);
        this.randfig.add(ran2);
        this.randfig.remove(0);
        int ran3 = (int)(Math.random() * 7.0);
        int ran4 = (int)(Math.random() * 7.0);
        this.randcol.add(ran3);
        this.randcol.add(ran4);
        this.currpiece = new Piece(this.piecespane, (Integer)this.randfig.get(0), (Integer)this.randcol.get(0));
        this.randcol.remove(0);
        this.piecespane.setOnKeyPressed((e) -> {
            this.handleKeyPressed(e);
        });
        this.piecespane.setFocusTraversable(true);
        this.root = root;
        this.board = new Rectangle[25][18];
        this.controller = new Controller(controlpane, this);
        this.setBoarders();
        this.setUpTimeline();
        this.setGamePausedLabel();
        this.piece = null;
        this.showNextFigure(ran2, ran4);
        this.setUpQuit();
    }

    private void restoreDate() {
        CSVFormat format = CSVFormat.RFC4180.withDelimiter(DELIMITER).withHeader();
        CSVParser parser;

        try {
            parser = new CSVParser(new FileReader("src/Tetris/Scores.csv"), format);
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

    private void setGamePausedLabel() {
        this.label = new Label("Game Paused!! Press P to continue");
        this.label.setLayoutY(0.0);
        this.label.setLayoutX(0.0);
        this.label.setFont(Font.font(16.0));
        this.label.setTextFill(Color.WHITE);
    }

    private void showNextFigure(int ran, int randcol) {
        Piece piece = new Piece(this.root, ran, randcol, this.piece);
        this.piece = piece;
    }

    private void handleKeyPressed(KeyEvent e) {
        KeyCode keypressed = e.getCode();
        boolean running = this.timeline.getStatus() == Status.RUNNING;
        switch (keypressed) {
            case RIGHT:
                if (running && this.eligibleMoveRight()) {
                    this.currpiece.moveRight();
                }
                break;
            case LEFT:
                if (running && this.eligibleMoveLeft()) {
                    this.currpiece.moveLeft();
                }
                break;
            case DOWN:
                if (running && this.eligibleMoveDown()) {
                    this.currpiece.moveDown();
                }
                break;
            case SPACE:
                if (running) {
                    while(this.eligibleMoveDown()) {
                        this.currpiece.moveDown();
                    }
                }
                break;
            case UP:
                if (running && this.eligibleRotate()) {
                    this.currpiece.rotate();
                }
                break;
            case P:
                if (this.timeline.getStatus() == Status.PAUSED) {
                    this.root.getChildren().remove(this.label);
                    this.timeline.play();
                    this.controller.setResponse(true);
                } else {
                    this.timeline.pause();
                    this.root.getChildren().add(this.label);
                    this.controller.setResponse(false);
                }
        }

        e.consume();
    }

    private boolean eligibleRotate() {
        int centerOfRotationX = (int)this.currpiece.getFigure()[1].getX() / 32;
        int centerOfRotationY = (int)this.currpiece.getFigure()[1].getY() / 32;
        Rectangle[] var3 = this.currpiece.getFigure();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Rectangle rect = var3[var5];
            int oldXLocation = (int)rect.getX() / 32;
            int oldYLocation = (int)rect.getY() / 32;
            int newXLoc = centerOfRotationX - centerOfRotationY + oldYLocation;
            int newYLoc = centerOfRotationY + centerOfRotationX - oldXLocation;
            if (this.board[newYLoc][newXLoc] != null) {
                return false;
            }
        }

        return true;
    }

    private boolean eligibleMoveDown() {
        Rectangle[] shape = this.currpiece.getFigure();
        boolean eligible = true;
        Rectangle[] var3 = shape;
        int ran1 = shape.length;

        label30:
        for(int var5 = 0; var5 < ran1; ++var5) {
            Rectangle rect = var3[var5];
            int index1 = (int)rect.getY() / 32 + 1;
            int index2 = (int)rect.getX() / 32;
            if (this.board[index1][index2] != null) {
                eligible = false;
                Rectangle[] var9 = shape;
                int var10 = shape.length;
                int var11 = 0;

                while(true) {
                    if (var11 >= var10) {
                        break label30;
                    }

                    Rectangle rect1 = var9[var11];
                    int indexa = (int)rect1.getY() / 32;
                    int indexb = (int)rect1.getX() / 32;
                    this.board[indexa][indexb] = rect1;
                    ++var11;
                }
            }
        }

        if (!eligible) {
            if (this.checkGameOver()) {
                this.timeline.stop();
            } else {
                int ran = (int)(Math.random() * 7.0);
                this.randfig.add(ran);
                ran1 = (int)(Math.random() * 7.0);
                this.randcol.add(ran1);
                this.currpiece = new Piece(this.piecespane, (Integer)this.randfig.get(0), (Integer)this.randcol.get(0));
                this.randfig.remove(0);
                this.randcol.remove(0);
                this.checkRow();
                this.showNextFigure(ran, ran1);
            }
        }

        return eligible;
    }

    private boolean checkGameOver() {
        for(int j = 1; j < 17; ++j) {
            if (this.board[1][j] != null) {
                this.showGameOver();
                this.piecespane.setOnKeyPressed(null);
                this.controller.stopButtons();
                return true;
            }
        }

        return false;
    }

    public void setUpQuit() {
        Button quit = new Button("Quit");
        quit.setOnAction((e) -> {
            this.storeData();
            System.exit(0);
        });
        this.root.getChildren().add(quit);
        quit.setPrefSize(100.0, 50.0);
        quit.setLayoutX(625.0);
        quit.setLayoutY(25.0);
        quit.setFocusTraversable(false);
    }

    private void storeData() {
        int count = 0;
        try {
            CSVWriter writer = new CSVWriter(new FileWriter("src/Tetris/Scores.csv"));
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

    private void showGameOver() {
        Label label = new Label("Game Over !!");
        this.root.getChildren().add(label);
        label.setLayoutY(0.0);
        label.setLayoutX(0.0);
        label.setFont(Font.font(23.0));
        label.setTextFill(Color.WHITE);
        this.manageLeaderBoard();
    }

    private void manageLeaderBoard() {
        for (int i = 0; i < this.pq.size(); i++) {
            int num = Integer.parseInt(this.pq.get(i).get(0));
            if (this.controller.getScore() > num) {
                ArrayList<String> row = new ArrayList<>();
                row.add(String.valueOf(this.controller.getScore()));
                row.add(String.valueOf(LocalDate.now()));
                row.add(String.valueOf(LocalTime.now()));
                this.pq.add(i, row);
                System.out.println(this.pq);
                return;
            }
        }
        ArrayList<String> row = new ArrayList<>();
        row.add(String.valueOf(this.controller.getScore()));
        row.add(String.valueOf(LocalDate.now()));
        row.add(String.valueOf(LocalTime.now()));
        this.pq.add(row);
    }

    private void checkRow() {
        int count = 0;

        for(int i = 1; i < 24; ++i) {
            for(int j = 1; j < 17 && this.board[i][j] != null; ++j) {
                if (j == 16) {
                    this.removeRow(i);
                    this.moveRowsDown(i - 1);
                    ++count;
                }
            }
        }

        this.controller.addToScore(count);
        double diff = 0.67 + (double)this.controller.getDiff() / 3.0;
        this.timeline.setRate(diff);
    }

    private void moveRowsDown(int startingrow) {
        for(int i = startingrow; i >= 1; --i) {
            for(int j = 1; j < 17; ++j) {
                if (this.board[i][j] != null) {
                    this.board[i][j].setY(this.board[i][j].getY() + 32.0);
                    this.board[i + 1][j] = this.board[i][j];
                    this.board[i][j] = null;
                }
            }
        }

    }

    private void removeRow(int currentrow) {
        for(int j = 1; j < 17; ++j) {
            this.piecespane.getChildren().remove(this.board[currentrow][j]);
            this.board[currentrow][j] = null;
        }

    }

    private boolean eligibleMoveLeft() {
        Rectangle[] var1 = this.currpiece.getFigure();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Rectangle rect = var1[var3];
            int index1 = (int)rect.getY() / 32;
            int index2 = (int)rect.getX() / 32 - 1;
            if (this.board[index1][index2] != null) {
                return false;
            }
        }

        return true;
    }

    private boolean eligibleMoveRight() {
        Rectangle[] var1 = this.currpiece.getFigure();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Rectangle rect = var1[var3];
            int index1 = (int)rect.getY() / 32;
            int index2 = (int)rect.getX() / 32 + 1;
            if (this.board[index1][index2] != null) {
                return false;
            }
        }

        return true;
    }

    private void setUpTimeline() {
        KeyFrame keyframe = new KeyFrame(Duration.seconds(1.0), (a) -> {
            if (this.eligibleMoveDown()) {
                this.currpiece.movePiece();
            }

        }, new KeyValue[0]);
        this.timeline = new Timeline(new KeyFrame[]{keyframe});
        this.timeline.setCycleCount(-1);
        this.timeline.play();
    }

    private void setBoarders() {
        for(int i = 0; i < 25; ++i) {
            for(int j = 0; j < 18; ++j) {
                Rectangle rect1;
                if (j * i == 0 || j == 17 || i == 24) {
                    rect1 = new Rectangle((double)(j * 32), (double)(i * 32), 30.0, 30.0);
                    rect1.setFill(Color.BLACK);
                    this.root.getChildren().add(rect1);
                    this.board[i][j] = rect1;
                }

                rect1 = new Rectangle((double)(j * 32 + 30), (double)(i * 32), 2.0, 30.0);
                rect1.setFill(Color.GREY);
                Rectangle rect2 = new Rectangle((double)(j * 32), (double)(i * 32 + 30), 32.0, 2.0);
                rect2.setFill(Color.GREY);
                this.root.getChildren().addAll(new Node[]{rect1, rect2});
            }
        }

    }

    public void alterDiff() {
        double diff = 0.67 + (double)this.controller.getDiff() / 3.0;
        this.timeline.setRate(diff);
    }
}

