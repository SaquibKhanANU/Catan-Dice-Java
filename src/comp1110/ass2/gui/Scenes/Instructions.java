package comp1110.ass2.gui.Scenes;

import comp1110.ass2.gui.Game;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

// Author: John Larkin and Saquib Khan
public class Instructions extends Pane {
    static String dice_roll_instruction =
            "Roll dice to gain resources. \n" +
                    "You have six dice to roll on your turn. \n" +
                    "1. Select 'Roll Dice' in the top right to roll \n" +
                    "the dice.\n " +
                    "Your resources will appear in the resource bank. \n"+
                    "2. Click the resources in the top row to \n" +
                    "re-roll those dice.\n " +
                    "Select roll dice to re-roll. \n" +
                    "3. Re-roll the dice up to two times.";

    static String build_instruction = "Build Settlements, Cities and Knights on \n" +
            "your turn \n" +
            "1. Check the cost card for the cost of the item.\n " +
            "You may only build something if you have \n " +
            "sufficient resources. \n" +
            "2. Click and drag the structure and place \n" +
            "on the board \n"
            + "to build.\n " +
            "Right-click the item on the same turn \n" +
            "to undo \n " +
            "the build action \n" +
            "3. Items must be built in ascending point \n" +
            "order.\n" +
            "Knights correspond to round circles.";

    static String trade_instruction = "Trade gold for other resources.\n" +
            "1. Select 'Trade' in the top right to trade.\n " +
            "You may trade gold for any resource at a cost of 2:1\n"+
            "2. A popup will appear. Click on the resource you want\n " +
            "to trade for.\n " +
            "The item will be added to your resource bank.\n" +
            "3. You can continue to trade provided you have \n " +
            "sufficient gold.";

    static String swap_instruction = "Swap for other resources using the knight.\n" +
            "You can use each knight exactly once per game.\n"+
            "The knight must be built and unused.\n" +
            "1. Select 'Swap' in the top right to swap.\n " +
            "Usable knights are highlighted.\n" +
            "2. Select the knight corresponding to the resource you want.\n" +
            "3. Exchange one of your current dice for that resource.";


    Group board = new Group();
    Group button = new Group();
    public Instructions() {
        goBack();
        InstructionsText instruction1 = new InstructionsText(dice_roll_instruction, "Dice Roll", 30, 60);
        InstructionsText instruction2 = new InstructionsText(build_instruction, "Build", 30+550+20, 60);
        InstructionsText instruction3 = new InstructionsText(trade_instruction, "Trade", 30, 60+350);
        InstructionsText instruction4 = new InstructionsText(swap_instruction, "Swap", 30+550+20, 60+350);
        this.setBackground(new Background(new BackgroundFill(Color.web("#439527"), CornerRadii.EMPTY, Insets.EMPTY)));
        getChildren().addAll(board, instruction1, instruction2, instruction3, instruction4, button);
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
    private void goBack() {
        InstructionsButton goBack = new InstructionsButton("GO BACK");
        ButtonBox buttonBox = new ButtonBox(
                goBack
        );
        buttonBox.setTranslateX(20);
        buttonBox.setTranslateY(650);
        button.getChildren().add(buttonBox);
    }
    private static class ButtonBox extends VBox {
        public ButtonBox(InstructionsButton... items) {
            getChildren().add(createSeperator());

            for (InstructionsButton item : items) {
                getChildren().addAll(item, createSeperator());
            }
        }
        private Line createSeperator() {
            Line sep = new Line();
            sep.setEndX(210);
            sep.setStroke(Color.TAN);
            return sep;
        }
    }

    private class InstructionsButton extends StackPane {
        public InstructionsButton(String name) {
            LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, new Stop(0, Color.TAN),
                    new Stop(0.1, Color.BLACK),
                    new Stop(0.9, Color.BLACK),
                    new Stop(1, null));

            Rectangle bg = new Rectangle(200, 30);
            bg.setOpacity(0.4);

            Text text = new Text(name);
            text.setFill(Color.SANDYBROWN);
            text.setFont(Font.font("Times New Roman", FontWeight.SEMI_BOLD, 20));

            setAlignment(Pos.CENTER);
            getChildren().addAll(bg, text);

            setOnMouseEntered(event -> {
                bg.setFill(gradient);
                text.setFill(Color.WHITE);
            });

            setOnMouseExited(event -> {
                bg.setFill(Color.BLACK);
                text.setFill(Color.SANDYBROWN);
            });

            setOnMouseReleased(event -> {
                bg.setFill(gradient);
            });

            if (name.equals("GO BACK")) {
                setOnMousePressed(e -> {
                    if (Game.boardName.size() > 0) {
                        Game.scenes.activate(Game.boardName.get(0));
                        Game.boardName.clear();
                    } else {
                        Game.scenes.activate("Menu");
                    }
                });
            }
        }
    }
}

