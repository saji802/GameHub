package Tetris;


import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class Controller {
    private int score;
    private Label labelscore;
    private Label labelcount;
    private Label labeldiff;
    private Pane root;
    private int linecount;
    private int diff;
    private Game game;
    private Button diffup;
    private Button diffdown;
    private boolean respond;

    public Controller(Pane pane, Game game) {
        this.root = pane;
        this.score = 0;
        this.linecount = 0;
        this.diff = 1;
        this.game = game;
        this.diffup = new Button("Diff UP");
        this.diffdown = new Button("Diff Down");
        this.labelscore = new Label("Score: " + this.score);
        this.labelcount = new Label("Lines Cleared: " + this.linecount);
        this.labeldiff = new Label("Difficulty: " + this.diff);
        Label labelnextpiece = new Label("NEXT PIECE:");
        this.makeLabels(this.labelscore, Color.WHITE, Font.font(22.0), 400, 20);
        this.makeLabels(this.labelcount, Color.WHITE, Font.font(22.0), 350, 20);
        this.makeLabels(this.labeldiff, Color.BLACK, Font.font(22.0), 500, 20);
        this.makeLabels(labelnextpiece, Color.BLACK, Font.font(22.0), 100, 50);
        this.makeButtons(this.diffup, 125, 650, true, 100, 50);
        this.makeButtons(this.diffdown, 15, 650, false, 100, 50);
        this.root.getChildren().addAll(new Node[]{this.diffup, this.diffdown, this.labelscore, this.labelcount, this.labeldiff, labelnextpiece});
        this.respond = true;
    }

    private void makeButtons(Button button, int xloc, int yloc, boolean isup, int width, int height) {
        if (isup) {
            button.setOnAction((a) -> {
                this.IncreaseDiff();
            });
        } else {
            button.setOnAction((b) -> {
                this.DecreaseDiff();
            });
        }

        button.setFocusTraversable(false);
        button.setLayoutY((double)yloc);
        button.setLayoutX((double)xloc);
        button.setPrefSize((double)width, (double)height);
    }

    private void DecreaseDiff() {
        if (this.diff != 1 && this.respond) {
            --this.diff;
        }

        this.game.alterDiff();
        this.labeldiff.setText("Difficulty: " + this.diff);
    }

    private void IncreaseDiff() {
        if (this.respond) {
            ++this.diff;
            this.game.alterDiff();
            this.labeldiff.setText("Difficulty: " + this.diff);
        }

    }

    private void makeLabels(Label label, Color color, Font font, int yloc, int xloc) {
        label.setTextFill(color);
        label.setFont(font);
        label.setLayoutY((double)yloc);
        label.setLayoutX((double)xloc);
    }

    public void addToScore(int count) {
        int oldlinecount = this.linecount;
        this.linecount += count;
        this.labelcount.setText("Lines Cleared: " + this.linecount);
        switch (count) {
            case 0:
                break;
            case 1:
                this.score += 100 * this.diff;
                break;
            case 2:
                this.score += 300 * this.diff;
                break;
            case 3:
                this.score += 500 * this.diff;
                break;
            default:
                this.score += 800 * this.diff;
                this.showTetris();
        }

        this.labelscore.setText("Score: " + this.score);
        this.diff += this.linecount - oldlinecount;
        this.labeldiff.setText("Difficulty: " + this.diff);
    }

    private void showTetris() {
        Label label = new Label("TETRIS!! SLAAY QUEEN");
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font(18.0));
        this.root.getChildren().add(label);
        label.setLayoutY(600.0);
        label.setLayoutX(20.0);
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(2.0));
        visiblePause.setOnFinished((event) -> {
            label.setVisible(false);
        });
        visiblePause.play();
    }

    public int getDiff() {
        return this.diff;
    }

    public void stopButtons() {
        this.diffup.setOnAction((EventHandler)null);
        this.diffdown.setOnAction((EventHandler)null);
    }

    public void setResponse(boolean res) {
        this.respond = res;
    }

    public int getScore() {
        return this.score;
    }
}
