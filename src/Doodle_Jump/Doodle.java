package Doodle_Jump;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Doodle {
    private BorderPane root;
    private double velocity;
    private Rectangle body;
    private Rectangle bag;
    private Rectangle leg1;
    private Rectangle shoe1;
    private Rectangle shoe2;
    private Rectangle leg2;
    private Ellipse face;
    private Game game;
    private boolean stop;
    private boolean playing;

    public Doodle(BorderPane root, Game game) {
        this.root = root;
        this.playing = true;
        this.stop = false;
        this.velocity = -600.0;
        this.body = new Rectangle(25.0, 40.0);
        this.body.setFill(Color.RED);
        this.leg1 = new Rectangle(5.0, 7.5);
        this.leg1.setFill(Color.RED);
        this.shoe1 = new Rectangle(5.0, 2.5);
        this.leg2 = new Rectangle(5.0, 7.5);
        this.leg2.setFill(Color.RED);
        this.shoe2 = new Rectangle(5.0, 2.5);
        this.bag = new Rectangle(5.0, 25.0);
        this.face = new Ellipse(8.0, 5.0);
        this.setYLock(740.0);
        this.setXLock(300.0);
        this.face.setFill(Color.SKYBLUE);
        this.setUpGameElement();
        this.root.getChildren().addAll(new Node[]{this.body, this.leg1, this.leg2, this.bag, this.face, this.shoe1, this.shoe2});
        this.game = game;
    }

    public void stopControlDoodle() {
        this.body.setOnKeyPressed((EventHandler)null);
    }

    private void moveLeft() {
        if (this.body.getX() >= 30.0) {
            this.setXLock(this.body.getX() - 0.9);
        } else {
            this.setXLock(585.0);
        }

    }

    private void moveRight() {
        if (this.body.getX() <= 570.0) {
            this.setXLock(this.body.getX() + 0.9);
        } else {
            this.setXLock(-10.0);
        }

    }

    public void setXLock(double x) {
        this.body.setX(x);
        this.leg1.setX(x);
        this.leg2.setX(x + 20.0);
        this.shoe1.setX(x);
        this.shoe2.setX(x + 20.0);
        this.bag.setX(x + -5.0);
        this.face.setCenterX(x + 15.0);
    }

    public void setYLock(double y) {
        this.body.setY(y);
        this.leg1.setY(y + 40.0);
        this.leg2.setY(y + 40.0);
        this.shoe1.setY(y + 45.0);
        this.shoe2.setY(y + 45.0);
        this.bag.setY(y + 7.5);
        this.face.setCenterY(y + 10.0);
    }

    public void setUpGameElement() {
        this.body.setOnKeyPressed((e) -> {
            this.HandleKeyPressed(e);
        });
        this.body.setFocusTraversable(true);
    }

    public void jump() {
        this.velocity += 16.0;
        this.setYLock(this.body.getY() + this.velocity * 0.016);
        if (this.body.getY() < 390.0) {
            this.game.scrollPlatforms();
            this.game.cleanPlatforms();
            this.game.changeScore();
            this.fixDoodle();
        }

        this.game.checkGameOver();
    }

    public void reactToGrey() {
        if (this.velocity >= 0.0) {
            this.game.setUpGameOver();
            this.stopControlDoodle();
            this.stop = true;
        } else {
            this.velocity += 16.0;
            this.setYLock(this.body.getY() + this.velocity * 0.016);
        }

    }

    public void reactToOthers() {
        if (this.velocity >= 0.0) {
            this.velocity = -600.0;
        }

        this.setYLock(this.body.getY() + this.velocity * 0.016);
    }

    public void reactToRed(Platform platform) {
        if (this.velocity >= 0.0) {
            this.game.removeRed(platform);
            platform.removePane();
        }

        this.velocity = -600.0;
        this.setYLock(this.body.getY() + this.velocity * 0.016);
    }

    public void reactToGreen() {
        this.setYLock(this.body.getY() + this.velocity * 0.016);
        if (this.velocity >= 0.0) {
            this.velocity = -1200.0;
        } else {
            this.velocity += 16.0;
        }

    }

    private void fixDoodle() {
        this.velocity += 16.0;
        this.setYLock(this.body.getY() + this.velocity * 0.016);
    }

    public void HandleKeyPressed(KeyEvent e) {
        KeyCode key = e.getCode();
        KeyFrame kf1 = new KeyFrame(Duration.millis(3.0), (e1) -> {
            this.moveLeft();
        }, new KeyValue[0]);
        KeyFrame kf2 = new KeyFrame(Duration.millis(3.0), (e1) -> {
            this.moveRight();
        }, new KeyValue[0]);
        Timeline timeline1 = new Timeline(new KeyFrame[]{kf1});
        Timeline timeline2 = new Timeline(new KeyFrame[]{kf2});
        timeline1.setCycleCount(50);
        timeline2.setCycleCount(50);
        switch (key) {
            case RIGHT:
                if (!this.playing) {
                    return;
                }

                timeline2.play();
                break;
            case LEFT:
                if (!this.playing) {
                    return;
                }

                timeline1.play();
                break;
            case P:
                this.game.pauseReplay();
                this.playing = !this.playing;
                System.out.println(this.playing);
        }

        e.consume();
    }

    public Rectangle getBody() {
        return this.body;
    }

    public Rectangle getLeg1() {
        return this.leg1;
    }

    public Rectangle getLeg2() {
        return this.leg2;
    }

    public boolean getStop() {
        return this.stop;
    }

    public boolean getPaused() {
        return !this.playing;
    }
}