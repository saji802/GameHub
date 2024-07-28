package Doodle_Jump;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class MovingPlatform extends Platform {
    private Controller controller;
    private boolean moveleft;
    private Timeline timeline;
    private Doodle doodle;

    public MovingPlatform(BorderPane root, double x, double y, Color color, Controller controller, Doodle doodle) {
        super(root, x, y, color, doodle);
        this.doodle = doodle;
        this.setUpTimeline();
        this.controller = controller;
        int ran = (int)(Math.random() * 1.0);
        if (ran == 1) {
            this.moveleft = true;
        } else {
            this.moveleft = false;
        }

        this.move(1);
    }

    private void setUpTimeline() {
        KeyFrame kf = new KeyFrame(Duration.seconds(0.016), (e) -> {
            this.move(1 + this.controller.getDifficultty() / 2);
        }, new KeyValue[0]);
        this.timeline = new Timeline(new KeyFrame[]{kf});
        this.timeline.setCycleCount(-1);
        this.timeline.play();
    }

    public void move(int multiplier) {
        if (!this.doodle.getPaused()) {
            if (this.moveleft) {
                this.moveLeft(multiplier);
            } else {
                this.moveRight(multiplier);
            }

            if (this.doodle.getBody().getY() > 800.0 || this.doodle.getStop()) {
                this.timeline.stop();
            }

        }
    }

    private void moveLeft(int multiplier) {
        if (this.getX() <= 0.0) {
            this.moveleft = false;
        }

        this.setX((int)(this.getX() - (double)multiplier));
    }

    private void moveRight(int multiplier) {
        if (this.getX() >= 530.0) {
            this.moveleft = true;
        }

        this.setX((int)(this.getX() + (double)multiplier));
    }
}