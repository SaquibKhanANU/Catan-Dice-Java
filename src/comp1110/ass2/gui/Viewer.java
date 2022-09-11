package comp1110.ass2.gui;

import comp1110.ass2.CatanEnum.StructureType;
import comp1110.ass2.CatanStructure.CatanRoad;
import comp1110.ass2.CatanStructure.Structure;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static comp1110.ass2.CatanGame.CatanDice.isBoardStateWellFormed;

public class Viewer extends Application  {

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
    void displayState(String board_state) { // STARTING WITH ROADS THEN WILL CHANGE IT FOR OTHER 3 Structures.
        Board.roads.getChildren().clear();

        String[] roadIds = new String[]{"RI", "R0","R1", "R2","R3", "R4","R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15"};
        String[] boardStateArr = board_state.split(",");
        List<String> boardStateArrList = Arrays.asList(boardStateArr);

        if (isBoardStateWellFormed(board_state)) {
            for (String s : roadIds) {
                if (boardStateArrList.contains(s)) {
                    Board.RoadShape.setBuilt(true);
                } else {
                    Board.RoadShape.setBuilt(false);
                }
                Board.RoadShape piece = new Board.RoadShape(s);
                Board.roads.getChildren().add(piece);
            }
        } else {
            Board.RoadShape.setBuilt(false);
            Board.RoadShape.makeRoads();
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
        root.getChildren().add(Board.hexBoard);
        root.getChildren().add(Board.roads);

        makeControls();
        Board.Hexagon.makeBoard();
        Board.RoadShape.makeRoads();


        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
