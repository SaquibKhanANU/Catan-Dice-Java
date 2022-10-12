package comp1110.ass2.gui.Scenes;

import comp1110.ass2.CatanGame.GameState;
import comp1110.ass2.gui.Game;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Menu {

    public Pane menuPane;

    public Menu() {
        this.createContent();
    }

    protected void createContent() {

        this.menuPane = new Pane();

        this.menuPane.setPrefSize(1200,700);

        ImageView img = new ImageView(new Image("comp1110/ass2/assets/CatanTitlePage.png"));
        img.setFitWidth(1205);
        img.setFitHeight(705);
        this.menuPane.getChildren().add(img);

        Title title = new Title("C A T A N" + " D I C E");
        title.setTranslateX(375);
        title.setTranslateY(200);

        MenuBox vbox = new MenuBox(
                new MenuItem("ONE PLAYER"),
                new MenuItem("TWO PLAYER"),
                new MenuItem("THREE PLAYER"),
                new MenuItem("FOUR PLAYER"),
                new MenuItem("INSTRUCTIONS"));
        vbox.setTranslateX(500);
        vbox.setTranslateY(300);



        this.menuPane.getChildren().addAll(title,vbox);
    }

    private static class Title extends StackPane {
        public Title(String name) {
            Rectangle bg = new Rectangle(450, 60);
            bg.setStroke(Color.SANDYBROWN);
            bg.setStrokeWidth(2);
            bg.setFill(null);

            Text text = new Text(name);
            text.setFill(Color.SANDYBROWN);
            text.setFont(Font.font("Times New Roman", FontWeight.SEMI_BOLD, 50));

            setAlignment(Pos.CENTER);
            getChildren().addAll(bg,text);
        }
    }

    private static class MenuBox extends VBox {
        public MenuBox(MenuItem... items) {
            getChildren().add(createSeperator());

            for (MenuItem item : items) {
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

    private class MenuItem extends StackPane {
        public MenuItem(String name) {
            LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, new Stop(0, Color.TAN),
                    new Stop(0.1, Color.BLACK),
                    new Stop(0.9, Color.BLACK),
                    new Stop(1, null));

            Rectangle bg = new Rectangle(200,30);
            bg.setOpacity(0.4);

            Text text = new Text(name);
            text.setFill(Color.SANDYBROWN);
            text.setFont(Font.font("Times New Roman", FontWeight.SEMI_BOLD,20));

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


            if ("ONE PLAYER".equals(name)) {
                setOnMousePressed(event -> {
                    Game.gameState = new GameState(1);
                    Game.oneBoard = new GameBoard(Game.playerOne);
                    Game.scenes.addScreen("PLAYER ONE", Game.oneBoard);
                    Game.scenes.activate("PLAYER ONE");
                });
            }
            if ("TWO PLAYER".equals(name)) {
                setOnMousePressed(event -> {
                    Game.gameState = new GameState(2);
                    Game.oneBoard = new GameBoard(Game.playerOne);
                    Game.twoBoard = new GameBoard(Game.playerTwo);
                    Game.scenes.addScreen("PLAYER ONE", Game.oneBoard);
                    Game.scenes.addScreen("PLAYER TWO", Game.twoBoard);
                    Game.scenes.activate("PLAYER ONE");
                });
            }
            if ("THREE PLAYER".equals(name)) {
                setOnMousePressed(event -> {
                    Game.gameState = new GameState(3);
                    Game.oneBoard = new GameBoard(Game.playerOne);
                    Game.twoBoard = new GameBoard(Game.playerTwo);
                    Game.threeBoard = new GameBoard(Game.playerThree);
                    Game.scenes.addScreen("PLAYER ONE", Game.oneBoard);
                    Game.scenes.addScreen("PLAYER TWO", Game.twoBoard);
                    Game.scenes.addScreen("PLAYER THREE", Game.threeBoard);
                    Game.scenes.activate("PLAYER ONE");
                });
            } if ("FOUR PLAYER".equals(name)) {
                setOnMousePressed(event -> {
                    Game.gameState = new GameState(4);
                    Game.oneBoard = new GameBoard(Game.playerOne);
                    Game.twoBoard = new GameBoard(Game.playerTwo);
                    Game.threeBoard = new GameBoard(Game.playerThree);
                    Game.fourBoard = new GameBoard(Game.playerFour);
                    Game.scenes.addScreen("PLAYER ONE", Game.oneBoard);
                    Game.scenes.addScreen("PLAYER TWO", Game.twoBoard);
                    Game.scenes.addScreen("PLAYER THREE", Game.threeBoard);
                    Game.scenes.addScreen("PLAYER FOUR", Game.fourBoard);
                    Game.scenes.activate("PLAYER ONE");
                });
            }
        }
    }
}