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
        Board.cities.getChildren().clear();
        Board.settlements.getChildren().clear();

        String[] roadIds = new String[]{"RI","R0","R1", "R2","R3", "R4","R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15",
                "C7", "C12", "C20", "C30", "S3", "S4", "S5", "S7", "S9", "S11"};
        String[] boardStateArr = board_state.split(",");
        List<String> boardStateArrList = Arrays.asList(boardStateArr);

        if (isBoardStateWellFormed(board_state)) {
            for (String s : roadIds) {
                Board.setBuilt(boardStateArrList.contains(s));
                if (s.charAt(0) == 'R'){
                    Board.RoadShape piece = new Board.RoadShape(s);
                    Board.roads.getChildren().add(piece);
                } else if (s.charAt(0) == 'C') {
                    Board.CityShape piece = new Board.CityShape(s);
                    Board.cities.getChildren().add(piece);
                } else if (s.charAt(0) == 'S') {
                    Board.SettlementShape piece = new Board.SettlementShape(s);
                    Board.settlements.getChildren().add(piece);
                }
            }
        } else {
            Board.setBuilt(false);
            Board.RoadShape.makeRoads();
            Board.CityShape.makeCities();
            Board.SettlementShape.makeSettlements();
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
        root.getChildren().add(Board.cities);
        root.getChildren().add(Board.roads);
        root.getChildren().add(Board.settlements);


        makeControls();
        Board.Hexagon.makeBoard();
        Board.RoadShape.makeRoads();
        Board.CityShape.makeCities();
        Board.SettlementShape.makeSettlements();


        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
