package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Game extends Application {

    private final Group root = new Group();
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;

    // ADDED
    private final Group board = new Group(); // The CatanDice map
    private final Group controls = new Group(); // Controls
    private final Group score_board = new Group(); // Score-sheet // Left of map
    private final Group dice = new Group(); // 6 dice (Roll on click the disappears and comes back after player turn is done)
    private final Group build_guide = new Group(); // Sheet showing resource_state to build structure (Pop up)
    private final Group road = new Group(); // Maybe could group these together? Structures of catan (fill/notFill)
    private final Group settlement = new Group();
    private final Group city = new Group();
    private final Group knight = new Group();


    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(this.root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }
}
