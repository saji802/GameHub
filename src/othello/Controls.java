package othello;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/*
 * Controls sets up the GUI for the game menu, allowing the user to pick the
 * game modes and to start and track games. Controls holds a one-way reference
 * to the Game, so it can control the Game's player settings.
 */
public class Controls {

    private VBox controlsPane;
    private SetupGame othello;
    private Color playerone;
    private Color playertwo;
    private ComboBox<String> comboBox1;
    private ComboBox<String> comboBox2;

    // Arrays for player buttons. Each button is checked to see if it is
    // selected when the user starts each game.
    private RadioButton[][] playerButtons;

    /**
     * This is Controls constructor. It calls methods that
     * are responsible for setting up the controls panel
     */
    public Controls(SetupGame game) {
        this.othello = game;
        this.controlsPane = new VBox();
        this.controlsPane.setPadding(new Insets(15));
        this.controlsPane.setSpacing(20);
        this.controlsPane.setAlignment(Pos.CENTER);
        this.setupInstructions();
        this.addLabels();
        this.setUpDropMenus();
        this.setupMenu();
        this.setupGameButtons();
        this.playerone = Color.WHITE;
        this.playertwo = Color.BLACK;
        this.controlsPane.setOnKeyPressed((KeyEvent e) -> this.handleKeys(e));
        this.controlsPane.setFocusTraversable(true);
    }

    private void handleKeys(KeyEvent e) {
        KeyCode pressed = e.getCode();
        switch (pressed) {
            case R:
                this.resetHandler(new ActionEvent());
                break;
            case Q:
                Platform.exit();
                break;
            case A:
                this.applySettings(new ActionEvent());
                break;
        }
        e.consume();
    }

    private void setUpDropMenus() {
        HBox hbox = new HBox();
        hbox.setSpacing(35);
        ObservableList<String> options1 = FXCollections.observableArrayList(
                "Black",
                "White",
                "Blue",
                "Red",
                "Yellow"
        );
        this.comboBox1 = new ComboBox<>(options1);
        this.comboBox1.setValue("White");
        ObservableList<String> options2 = FXCollections.observableArrayList(
                "Black",
                "White",
                "Blue",
                "Red",
                "Yellow"
        );
        this.comboBox2 = new ComboBox<>(options2);
        this.comboBox2.setValue("Black");
        hbox.getChildren().addAll(this.comboBox1, this.comboBox2);
        this.controlsPane.getChildren().addAll(hbox);
        this.comboBox1.setOnAction((ActionEvent a) -> {
            String selectedValue = this.comboBox1.getValue();
            this.playerone = Color.valueOf(selectedValue);
        });
        this.comboBox2.setOnAction((ActionEvent a) -> {
            String selectedValue = this.comboBox2.getValue();
            this.playertwo = Color.valueOf(selectedValue);
        });
    }

    /**
     * This method add the labels recieved from Referee
     * class (Score and turns) to the controlspane
     */
    private void addLabels() {
        Label[] labels = this.othello.getStrings();
        for (Label label : labels) {
            this.controlsPane.getChildren().add(label);
        }
    }

    /**
     * This method is an accessor so that PaneOrganizer class
     * can get the controls pane after everything is added
     */
    public Pane getPane() {
        return this.controlsPane;
    }

    /**
     * This method sets up the label of instructions and
     * then add it to control pane
     */
    private void setupInstructions() {
        Label instructionsLabel = new Label(
                "Select options, then press Apply Settings");
        this.controlsPane.getChildren().add(instructionsLabel);
    }

    /**
     * This method sets up the two halves of the player mode menu.
     */
    private void setupMenu() {
        this.playerButtons = new RadioButton[2][4];
        HBox playersMenu = new HBox();
        playersMenu.setSpacing(10);
        playersMenu.setAlignment(Pos.CENTER);
        playersMenu.getChildren().addAll(this.playerMenu(Constants.WHITE),
                this.playerMenu(Constants.BLACK));

        this.controlsPane.getChildren().add(playersMenu);
    }

    /**
     * This method provides the menu for each player mode.
     */
    private VBox playerMenu(int player) {
        VBox playerMenu = new VBox();
        playerMenu.setPrefWidth(Constants.CONTROLS_PANE_WIDTH / 2);
        playerMenu.setSpacing(10);
        playerMenu.setAlignment(Pos.CENTER);

        // Radio button group for player mode.
        ToggleGroup toggleGroup = new ToggleGroup();

        // Human player.
        RadioButton humanButton = new RadioButton("Human         ");
        humanButton.setToggleGroup(toggleGroup);
        humanButton.setSelected(true);
        this.playerButtons[player][0] = humanButton;

        // Computer Players.
        RadioButton computerButton1 = new RadioButton("Easy");
        computerButton1.setToggleGroup(toggleGroup);
        this.playerButtons[player][1] = computerButton1;
        RadioButton computerButton2 = new RadioButton("Medium");
        computerButton2.setToggleGroup(toggleGroup);
        this.playerButtons[player][2] = computerButton2;
        RadioButton computerButton3 = new RadioButton("Hard");
        computerButton3.setToggleGroup(toggleGroup);
        this.playerButtons[player][3] = computerButton3;
        // Enables deterministic button when Computer player selected.

        // Checkbox for deterministic play. Only enabled when computer player
        // selected. This is ONLY for Bells&Whistles

        // Visually add the player mode menu.
        for (RadioButton rb : this.playerButtons[player]) {
            playerMenu.getChildren().add(rb);
        }

        return playerMenu;
    }

    /**
     * This method sets up the game buttons
     * and then add them to the controls pane
     */
    private void setupGameButtons() {
        Button applySettingsButton = new Button("Apply Settings");
        applySettingsButton.setOnAction((ActionEvent e) -> this.applySettings(e));
        applySettingsButton.setFocusTraversable(false);
        Button resetButton = new Button("Reset");
        resetButton.setOnAction((ActionEvent e) -> this.resetHandler(e));
        resetButton.setFocusTraversable(false);

        Button quitButton = new Button("Quit");
        quitButton.setOnAction((ActionEvent e) -> Platform.exit());
// Platform.exit(
        quitButton.setFocusTraversable(false);

        this.controlsPane.getChildren().addAll(applySettingsButton, resetButton,
                quitButton);
    }


    /**
     * This method is a handler for Apply Settings button.
     */

    public void applySettings(ActionEvent e) {

        // Determine game play mode for each player.
        int whitePlayerMode = 0;
        int blackPlayerMode = 0;
        for (int player = 0; player < 2; player++) {
            for (int mode = 0; mode < 4; mode++) {
                if (this.playerButtons[player][mode].isSelected()) {
                    if (player == Constants.WHITE) {
                        whitePlayerMode = mode;
                    } else {
                        blackPlayerMode = mode;
                    }
                }
            }

        }
        if (this.playerone != this.playertwo)
            this.othello.setPlayer(whitePlayerMode, blackPlayerMode, this.playerone, this.playertwo);
        else
            System.out.println("Choose different colors, you know how this works");
    }

    /**
     * This method handles resetting the game
     * when Reset button is clicked
     */
    public void resetHandler(ActionEvent e) {
        if (this.othello.isInitiated()) {
            this.othello.resetBoard();
            this.comboBox1.setValue("White");
            this.comboBox2.setValue("Black");
        }
    }
}
