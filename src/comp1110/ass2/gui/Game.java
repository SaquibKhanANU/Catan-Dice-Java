package comp1110.ass2.gui;

import comp1110.ass2.CatanDice;
import comp1110.ass2.CatanGame.CatanBoard;
import comp1110.ass2.CatanGame.CatanPlayer;
import comp1110.ass2.CatanGame.GameState;
import comp1110.ass2.gui.Scenes.GameBoard;
import comp1110.ass2.gui.Scenes.Instructions;
import comp1110.ass2.gui.Scenes.Menu;
import comp1110.ass2.gui.Controls.ScreenController;
import comp1110.ass2.gui.Scenes.Winner;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;

// Author: Saquib Khan
public class Game extends Application {
    private static final Group root = new Group();
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 700;

    public static Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    public static ScreenController scenes = new ScreenController(scene);
    Menu menu;
    public static CatanPlayer playerOne;
    public static CatanPlayer playerTwo;
    public static CatanPlayer playerThree;
    public static CatanPlayer playerFour;
    public static GameBoard oneBoard;
    public static GameBoard twoBoard;
    public static GameBoard threeBoard;
    public static GameBoard fourBoard;
    public static GameState gameState;
    public static Winner winner;
    public static Instructions instructions;
    public static ArrayList<String> boardName = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);

        this.menu = new Menu();
        scenes.addScreen("Menu", menu.menuPane);
        scenes.activate("Menu");

        stage.setScene(scene);
        stage.show();
    }
}
