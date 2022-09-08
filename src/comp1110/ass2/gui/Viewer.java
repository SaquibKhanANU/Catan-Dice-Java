package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static comp1110.ass2.CatanGame.CatanDice.isBoardStateWellFormed;
import static comp1110.ass2.gui.Hexagon.*;
import static comp1110.ass2.gui.StructureShapes.*;

public class Viewer extends Application {

    private static final int VIEWER_WIDTH = 1200;
    private static final int VIEWER_HEIGHT = 700;

    private final Group root = new Group();
    private final Group controls = new Group();
    private TextField playerTextField;
    private TextField boardTextField;

    /**
     * Show the state of a (single player's) board in the window.
     *
     * @param board_state The string representation of the board state.
     */
    void displayState(String board_state) {
        road.getChildren().clear();
        if (isBoardStateWellFormed(board_state)) {
            String[] boardStateArr = board_state.split(",");
            for (String s : boardStateArr) {
                RoadShape piece = new RoadShape(s);
                piece.setFill(Color.BLACK);
                road.getChildren().add(piece);
            }
        } else {
            RoadShape piece = new RoadShape(100, 100, 0);
            road.getChildren().add(piece);
        }
        // FIXME Task 5: implement the state viewer
    }

    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label boardLabel = new Label("Board State:");
        boardTextField = new TextField();
        boardTextField.setPrefWidth(500);
        Button button = new Button("Show");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                displayState(boardTextField.getText());
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(boardLabel, boardTextField, button);
        hb.setSpacing(10);
        controls.getChildren().add(hb);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Board State Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);
        root.getChildren().add(board);
        root.getChildren().add(road);

        makeControls();
        Hexagon.makeBoard();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
