package Tetris;


import javafx.scene.paint.Color;

public class Constants {
    public static final int SQUARE_WIDTH = 30;
    public static final int SQUARE_WIDTH_ARRAY = 32;
    public static final int[][] I_PIECE_COORDS = new int[][]{{0, 0}, {0, 32}, {0, 64}, {0, 96}};
    public static final int[][] T_PIECE_COORDS = new int[][]{{-32, 0}, {-32, 32}, {-32, 64}, {0, 32}};
    public static final int[][] SQUARE_PIECE_COORDS = new int[][]{{0, 0}, {0, 32}, {-32, 0}, {-32, 32}};
    public static final int[][] L_RIGHT_PIECE_COORDS = new int[][]{{0, 0}, {32, 0}, {0, 32}, {0, 64}};
    public static final int[][] L_LEFT_PIECE_COORDS = new int[][]{{0, 0}, {-32, 0}, {0, 32}, {0, 64}};
    public static final int[][] Z_RIGHT_PIECE_COORDS = new int[][]{{0, 0}, {0, 32}, {-32, 32}, {-32, 64}};
    public static final int[][] Z_LEFT_PIECE_COORDS = new int[][]{{0, 0}, {0, 32}, {32, 32}, {32, 64}};
    public static final Color[] colors;
    public static final double APP_HEIGHT = 800.0;
    public static final double APP_WIDTH = 800.0;
    public static final int BOARD_ROWS = 25;
    public static final int BOARD_COLUMNS = 18;
    public static final double GAME_PAUSED_FONTSIZE = 16.0;
    public static final double QUIT_WIDTH = 100.0;
    public static final double QUIT_HEIGHT = 50.0;
    public static final double QUIT_XLOC = 625.0;
    public static final double QUIT_YLOC = 25.0;
    public static final double GAME_OVER_FONTSIZE = 23.0;
    public static final double GAP_WIDTH = 2.0;
    public static final double FIGURE_XLOC = 288.0;
    public static final double FIGURE_YLOC = 32.0;
    public static final double SMALL_FIGURE_XLOC = 680.0;
    public static final double SMALL_FIGURE_YLOC = 170.0;
    public static final double SCORING_FONTSIZE = 22.0;
    public static final double SPACING_VALUE = 350.0;

    public Constants() {
    }

    static {
        colors = new Color[]{Color.LIGHTSALMON, Color.GREEN, Color.GOLD, Color.RED, Color.ORANGE, Color.BLUE, Color.PURPLE};
    }
}

