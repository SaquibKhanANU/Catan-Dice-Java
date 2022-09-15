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
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

import static comp1110.ass2.CatanDice.isBoardStateWellFormed;

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
    Group instructions = new Group();
    void displayState(String board_state) { // STARTING WITH ROADS THEN WILL CHANGE IT FOR OTHER 3 Structures.
        instructions.getChildren().clear();
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
            Text text = new Text();
            text.setText("Invalid board state string");
            text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
            text.setFill(Color.RED);
            text.setX(520);
            text.setY(50);
            instructions.getChildren().add(text);
            Board.setBuilt(false);
            Board.RoadShape.makeRoads();
            Board.CityShape.makeCities();
            Board.SettlementShape.makeSettlements();
        }

        // FIXME Task 5: implement the state viewer
    }
    void instructions() {
        Text text = new Text();
        text.setText("INSTRUCTIONS: Type a string containing the following characters: \n" +
                "R + (1-15), C + (7,12,20,30), S + (3,4,5,7,9,11), K + (1-6)\n" +
                "e.g. R0,R1,R2,S3,S4 (note: no space between comma)");
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
        text.setX(50);
        text.setY(50);
        root.getChildren().add(text);
    }

    int points() {
        return 1;
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
        root.getChildren().add(instructions);


        makeControls();
        Board.Hexagon.makeBoard();
        Board.RoadShape.makeRoads();
        Board.CityShape.makeCities();
        Board.SettlementShape.makeSettlements();
        instructions();


        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
