package comp1110.ass2.gui.Scenes;

import comp1110.ass2.gui.Game;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

// Author: John Larkin and Saquib Khan
public class Instructions extends Pane {

    static String dice_roll_instruction = "some, text this is a placeholder for the text \n this is a test to see if it works";

    Group board = new Group();
    public Instructions() {
        goBack();
        InstructionsText instruction1 = new InstructionsText(dice_roll_instruction, "Dice Roll", 30, 60);
        InstructionsText instruction2 = new InstructionsText(dice_roll_instruction, "Dice Roll", 30+550+20, 60);
        InstructionsText instruction3 = new InstructionsText(dice_roll_instruction, "Dice Roll", 30, 60+350);
        InstructionsText instruction4 = new InstructionsText(dice_roll_instruction, "Dice Roll", 30+550+20, 60+350);
        this.setBackground(new Background(new BackgroundFill(Color.web("#439527"), CornerRadii.EMPTY, Insets.EMPTY)));
        getChildren().addAll(board, instruction1, instruction2, instruction3, instruction4);
    }

    // Author:
    class InstructionsBoard extends Rectangle {
        public InstructionsBoard(double x, double y) {
            setHeight(300);
            setWidth(550);
            setX(x);
            setY(y);
            setFill(Color.TAN);
            setStroke(Color.SADDLEBROWN);
            setStrokeWidth(3);


        }
    }

    // TOD0: FIX THIS
    private void goBack() {
        Button goBack = new Button();
        goBack.setText("GO BACK");
        goBack.setLayoutX(1200-68);
        goBack.setLayoutY(700-35);
        goBack.setOnMousePressed(e -> {
            Game.scenes.activate("Menu");
        });
        board.getChildren().add(goBack);
    }

    class InstructionsText extends Text{

        InstructionsText(String Instructions, String title, double x, double y){


            Text title1 = new Text();
            title1.setText(title);
            title1.setX(x);
            title1.setY(y-25);
            title1.setFill(Color.SADDLEBROWN);
            title1.setFont(Font.font("times new roman", FontWeight.BOLD, FontPosture.REGULAR, 35));


            setText(Instructions);
            setX(x+10);
            setY(y+10);
            setFill(Color.DARKGREEN);
            setFont(Font.font("times new roman", FontWeight.BOLD, FontPosture.REGULAR, 20));


            InstructionsBoard square = new InstructionsBoard(x-20, y-20);



            board.getChildren().addAll(this,square,title1);


        }


    }

}
