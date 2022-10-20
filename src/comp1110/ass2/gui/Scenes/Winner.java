package comp1110.ass2.gui.Scenes;

import comp1110.ass2.CatanGame.CatanBoard;
import comp1110.ass2.CatanGame.CatanPlayer;
import comp1110.ass2.gui.Controls.GameControls;
import comp1110.ass2.gui.Game;
import gittest.B;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
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

import javax.print.DocFlavor;
import javax.swing.text.html.ImageView;
import java.util.ArrayList;

// Author: Saquib Khan
public class Winner extends Pane {
    ArrayList<CatanPlayer> catanPlayers;
    int players;
    Group board = new Group();

    /**
     * Creates the winner board, showing each player's points
     * @Author Saquib Khan
     * @param players the number of players
     * @param catanPlayers the players playing.
     */
    public Winner(int players, ArrayList<CatanPlayer> catanPlayers) {
        this.players = players;
        this.catanPlayers = catanPlayers;
        Buttons();
        this.setBackground(new Background(new BackgroundFill(Color.web("#439527"), CornerRadii.EMPTY, Insets.EMPTY)));
        getChildren().addAll(new WinnerBoard(catanPlayers), board);
    }
    // Author: Saquib Khan

    /**
     * Creates the winner board, showing each player's points
     * @Author Saquib Khan
     */
    class WinnerBoard extends Rectangle {
        WinnerBoard(ArrayList<CatanPlayer> catanPlayers) {
            setHeight(120 + (20 * players));
            setWidth(300);
            setX(450);
            setY(150);
            setFill(Color.TAN);
            setStroke(Color.SADDLEBROWN);
            setStrokeWidth(3);
            for (CatanPlayer catanPlayer : catanPlayers) {
                catanPlayer.calculateFinalScore();
                Text swap = new Text();
                swap.setText(catanPlayer.name + ": " + catanPlayer.finalScore + "");
                swap.setX(460);
                swap.setY(140 + (50 * catanPlayer.index));
                swap.setFill(Color.DARKGREEN);
                swap.setFont(Font.font("times new roman", FontWeight.BOLD, FontPosture.REGULAR, 30));
                board.getChildren().add(swap);
            }
        }
    }

    private void Buttons() {
        WinnerPageButton goBack = new WinnerPageButton("GO BACK");
        WinnerPageButton restart = new WinnerPageButton("RESTART");
        ButtonBox buttonBox = new ButtonBox(
                goBack,
                restart
        );
        buttonBox.setTranslateX(100);
        buttonBox.setTranslateY(600);
        board.getChildren().add(buttonBox);
    }


    private static class ButtonBox extends VBox {
        public ButtonBox(WinnerPageButton... items) {
            getChildren().add(createSeperator());

            for (WinnerPageButton item : items) {
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

    private class WinnerPageButton extends StackPane {
        public WinnerPageButton(String name) {
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
            if (name.equals("RESTART")) {
                setOnMousePressed(e -> {
                    Game.scenes.activate("Menu");
                });
            } else {
                setOnMousePressed(e->{
                    Game.scenes.activate("PLAYER ONE");
                });
            }
        }
    }
}
