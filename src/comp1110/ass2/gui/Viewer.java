package comp1110.ass2.gui;

import comp1110.ass2.CatanEnum.StructureType;
import comp1110.ass2.CatanGame.CatanBoard;
import comp1110.ass2.CatanGame.CatanPlayer;
import comp1110.ass2.CatanGame.GameState;
import comp1110.ass2.CatanStructure.Structure;
import comp1110.ass2.gui.Scenes.GameBoard;
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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

import static comp1110.ass2.CatanDice.checkBuildConstraints;
import static comp1110.ass2.CatanDice.isBoardStateWellFormed;

public class Viewer extends Application  {

    private static final int VIEWER_WIDTH = 1200;
    private static final int VIEWER_HEIGHT = 700;

    private final Group root = new Group();
    private final Group controls = new Group();
    private TextField playerTextField;
    private TextField boardTextField;
    Group instructions = new Group();

    CatanPlayer playerViewer;
    GameBoard viewerBoard;

    /**
     * Show the state of a (single player's) board in the window.
     *
     * @param board_state The string representation of the board state.
     */

    void displayState(String board_state) { // STARTING WITH ROADS THEN WILL CHANGE IT FOR OTHER 3 Structures.
        String[] boardStateArr = board_state.split(",");
        List<String> boardStateArrList = Arrays.asList(boardStateArr);
        if (isBoardStateWellFormed(board_state) && board_state.length() > 0) {
            instructions.getChildren().clear();
            viewerBoard.makeStructuresViewer(boardStateArrList);
        } else {
            instructions.getChildren().clear();
            instructions();
            Text text = new Text();
            text.setText("Invalid board state string");
            text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
            text.setFill(Color.RED);
            text.setX(720);
            text.setY(50);
            instructions.getChildren().add(text);
            viewerBoard.getChildren().add(instructions);
        }
        // FIXME Task 5: implement the state viewer
    }
    void instructions() {
        Text text = new Text();
        text.setText("INSTRUCTIONS: Type a string containing the following characters: \n" +
                "R + (1-15), C + (7,12,20,30), S + (3,4,5,7,9,11), K/J + (1-6)\n" +
                "e.g. R0,R1,R2,S3,S4 (note: no space between comma)");
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
        text.setX(250);
        text.setY(50);
        instructions.getChildren().add(text);
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
        viewerBoard.getChildren().add(hb);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Board State Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);
        playerViewer = new CatanPlayer(1);
        Game.gameState = new GameState(1);
        viewerBoard = new GameBoard(playerViewer);
        scene.setRoot(viewerBoard);
        root.getChildren().add(controls);
        makeControls();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
