package Tetris;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Piece {
    private Pane root;
    private Rectangle[] figure;
    private Color color;
    private boolean rotate;

    public Piece(Pane root, int rand, int randcol) {
        this.root = root;
        this.figure = new Rectangle[4];
        this.createFigure(rand, randcol);
        this.showFigure();
        this.color = null;
    }

    public Piece(Pane root, int rand, int randcol, Piece pastpiece) {
        this.root = root;
        this.figure = new Rectangle[4];
        if (pastpiece != null) {
            this.removeFig(pastpiece);
        }

        this.createFigure(rand, randcol);
        this.showSmallFigure();
    }

    private void removeFig(Piece pastpiece) {
        Rectangle[] var2 = pastpiece.getFigure();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            Rectangle rect = var2[var4];
            this.root.getChildren().remove(rect);
        }

    }

    public void movePiece() {
        Rectangle[] var1 = this.figure;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            Rectangle rect = var1[var3];
            rect.setY(rect.getY() + 32.0);
        }

    }

    private void showFigure() {
        Rectangle[] var1 = this.figure;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            Rectangle rec = var1[var3];
            this.root.getChildren().add(rec);
            rec.setX(rec.getX() + 288.0);
            rec.setY(rec.getY() + 32.0);
        }

    }

    private void showSmallFigure() {
        Rectangle[] var1 = this.figure;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            Rectangle rec = var1[var3];
            this.root.getChildren().add(rec);
            rec.setX(rec.getX() + 680.0);
            rec.setY(rec.getY() + 170.0);
        }

    }

    private void createFigure(int randfig, int randcol) {
        int[][] piececoords = (int[][]) null;
        switch (randfig) {
            case 0:
                piececoords = Constants.T_PIECE_COORDS;
                this.rotate = true;
                this.color = Constants.colors[randcol];
                break;
            case 1:
                piececoords = Constants.I_PIECE_COORDS;
                this.rotate = true;
                this.color = Constants.colors[randcol];
                break;
            case 2:
                piececoords = Constants.SQUARE_PIECE_COORDS;
                this.rotate = false;
                this.color = Constants.colors[randcol];
                break;
            case 3:
                piececoords = Constants.L_RIGHT_PIECE_COORDS;
                this.rotate = true;
                this.color = Constants.colors[randcol];
                break;
            case 4:
                piececoords = Constants.L_LEFT_PIECE_COORDS;
                this.rotate = true;
                this.color = Constants.colors[randcol];
                break;
            case 5:
                piececoords = Constants.Z_RIGHT_PIECE_COORDS;
                this.rotate = true;
                this.color = Constants.colors[randcol];
                break;
            case 6:
                piececoords = Constants.Z_LEFT_PIECE_COORDS;
                this.rotate = true;
                this.color = Constants.colors[randcol];
        }

        int i;
        for (i = 0; i < 4; ++i) {
            this.figure[i] = new Rectangle(30.0, 30.0);
            this.figure[i].setFill(this.color);
        }

        for (i = 0; i < 4; ++i) {
            this.figure[i].setX((double) piececoords[i][0]);
            this.figure[i].setY((double) piececoords[i][1]);
        }

    }

    public Rectangle[] getFigure() {
        return this.figure;
    }

    public void moveRight() {
        Rectangle[] var1 = this.figure;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            Rectangle rect = var1[var3];
            rect.setX(rect.getX() + 32.0);
        }

    }

    public void moveLeft() {
        Rectangle[] var1 = this.figure;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            Rectangle rect = var1[var3];
            rect.setX(rect.getX() - 32.0);
        }

    }

    public void moveDown() {
        Rectangle[] var1 = this.figure;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            Rectangle rect = var1[var3];
            rect.setY(rect.getY() + 32.0);
        }

    }

    public void rotate() {
        if (this.rotate) {
            int centerOfRotationX = (int) this.figure[1].getX();
            int centerOfRotationY = (int) this.figure[1].getY();
            Rectangle[] var3 = this.figure;
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Rectangle rect = var3[var5];
                int oldXLocation = (int) rect.getX();
                int oldYLocation = (int) rect.getY();
                int newXLoc = centerOfRotationX - centerOfRotationY + oldYLocation;
                int newYLoc = centerOfRotationY + centerOfRotationX - oldXLocation;
                rect.setX((double) newXLoc);
                rect.setY((double) newYLoc);
            }
        }

    }
}
