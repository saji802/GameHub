package Doodle_Jump;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Controller {
    private Label labelscore;
    private Label labeldiff;
    private BorderPane root;
    private int score = 0;
    private HBox hbox;

    public Controller(BorderPane root) {
        this.labelscore = new Label("Score: " + this.score);
        this.labelscore.setTextFill(Color.BLACK);
        this.labelscore.setFont(Font.font(22.0));
        this.labeldiff = new Label("Difficulty: 0");
        this.labeldiff.setTextFill(Color.BLACK);
        this.labeldiff.setFont(Font.font(22.0));
        this.root = root;
        this.hbox = new HBox();
        this.root.setTop(this.hbox);
        this.showScore();
        this.showDiffculty();
    }

    private void showDiffculty() {
        this.hbox.getChildren().add(this.labelscore);
        this.hbox.setAlignment(Pos.BASELINE_LEFT);
        this.hbox.setSpacing(350.0);
    }

    private void showScore() {
        this.hbox.getChildren().add(this.labeldiff);
        this.hbox.setAlignment(Pos.BASELINE_RIGHT);
    }

    public void updateLabels() {
        this.score += 10;
        this.labelscore.setText("Score: " + this.score);
        this.labeldiff.setText("Difficulty: " + (this.score / 1000 + 1));
    }

    public int getDifficultty() {
        return this.score / 1000 + 1;
    }

    public Integer getScore() {
        return this.score;
    }
}