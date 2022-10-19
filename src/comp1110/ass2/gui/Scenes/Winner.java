package comp1110.ass2.gui.Scenes;

import comp1110.ass2.CatanGame.CatanBoard;
import comp1110.ass2.CatanGame.CatanPlayer;
import comp1110.ass2.gui.Controls.GameControls;
import comp1110.ass2.gui.Game;
import gittest.B;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
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

import javax.print.DocFlavor;
import javax.swing.text.html.ImageView;
import java.util.ArrayList;

// Author: Saquib Khan
public class Winner extends Pane {
    ArrayList<CatanPlayer> catanPlayers;
    int players;
    Group board = new Group();
    public Winner(int players, ArrayList<CatanPlayer> catanPlayers) {
        this.players = players;
        this.catanPlayers = catanPlayers;
        goBack();
        this.setBackground(new Background(new BackgroundFill(Color.web("#439527"), CornerRadii.EMPTY, Insets.EMPTY)));
        getChildren().addAll(new WinnerBoard(catanPlayers), board);
    }
    // Author: Saquib Khan
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

    private void goBack() {
        Button goBack = new Button();
        goBack.setText("GO BACK");
        goBack.setLayoutX(100);
        goBack.setLayoutY(500);
        goBack.setOnMousePressed(e -> {
            Game.scenes.activate("PLAYER ONE");
        });
        board.getChildren().add(goBack);
    }
}
