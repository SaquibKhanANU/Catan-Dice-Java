package comp1110.ass2.gui;

import comp1110.ass2.CatanGame.CatanBoard;
import comp1110.ass2.CatanGame.CatanPlayer;
import comp1110.ass2.CatanGame.GameState;
import comp1110.ass2.gui.Scenes.GameBoard;
import comp1110.ass2.gui.Scenes.Menu;
import comp1110.ass2.gui.Controls.ScreenController;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Collection;


public class Game extends Application {
    private static final Group root = new Group();
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 700;

    public static Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    public static ScreenController n = new ScreenController(scene);
    Menu menu;
    public static CatanPlayer playerOne = new CatanPlayer(1);
    public static CatanPlayer playerTwo = new CatanPlayer(2);
    public static Pane playerOnePane;
    public static Pane playerTwoPane;
    public static GameBoard one;
    public static GameBoard two;
    public static GameState gameState;

    @Override
    public void start(Stage stage) throws Exception {
        scene.setFill(Color.NAVAJOWHITE);
        stage.setResizable(false);

        this.menu = new Menu();
        n.addScreen("Menu", menu.menuPane);
        n.activate("Menu");

        stage.setScene(scene);
        stage.show();
    }
}
