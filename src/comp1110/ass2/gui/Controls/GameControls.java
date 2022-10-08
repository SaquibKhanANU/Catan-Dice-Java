package comp1110.ass2.gui.Controls;

import comp1110.ass2.Action;
import comp1110.ass2.CatanEnum.ActionType;
import comp1110.ass2.CatanEnum.ResourceType;
import comp1110.ass2.CatanEnum.StructureType;
import comp1110.ass2.CatanGame.CatanPlayer;
import comp1110.ass2.CatanStructure.Structure;
import comp1110.ass2.gui.Game;
import comp1110.ass2.gui.Scenes.GameBoard;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
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
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static comp1110.ass2.CatanDice.rollDice;

public class GameControls {
    public Group controls = new Group();
    public Group chooseBoard = new Group();
    public Group sidePanel = new Group();
    public Group scoreBoard = new Group();
    public Group diceRollGroup = new Group();
    public Group allControls = new Group(controls, chooseBoard, sidePanel, scoreBoard, diceRollGroup);
    BorderPane pane1 = new BorderPane();
    CatanPlayer catanPlayer;
    BorderPane scoreBoardPane = new BorderPane();
    Image scoreBoardImage = new Image("comp1110/ass2/assets/CatanScoreBoard.JPG");
    ImageView scoreBoardView = new ImageView();

    public Action action;
    public boolean diceRolled;
    public int diceRollCount;
    public GameControls(CatanPlayer catanPlayer) {
        this.diceRolled = false;
        this.catanPlayer = catanPlayer;
        makeSidePanel();
        makeDiceRoll();
        createChooseBoard();
        gameButtons();
        makeScoreBoard();
        action = new Action(ActionType.NONE);
    }

    public void setDiceRolled(boolean diceRolled) {
        this.diceRolled = diceRolled;
    }

    public boolean isDiceRolled() {
        return diceRolled;
    }

    static class SidePanel extends Rectangle {
        double height;
        double width;

        SidePanel(double height, double width, double x, double y) {
            this.height = height;
            this.width = width;
            Color c = Color.web("#439527");
            setWidth(width);
            setHeight(height);
            setFill(c);
            setX(x);
            setY(y);
            setStroke(Color.BLACK);
            setStrokeWidth(3);
        }
    }

    private void makeSidePanel() {
        SidePanel rightPanel = new SidePanel(700, 300, 899, 0);
        SidePanel leftPanel = new SidePanel(700, 200, 1, 0);

        Image image = new Image("comp1110/ass2/assets/CatanTitle.JPG");
        ImageView iv1 = new ImageView();
        iv1.setFitWidth(196);
        iv1.setFitHeight(125);
        iv1.setX(2);
        iv1.setY(2);
        iv1.setImage(image);

        Image image2 = new Image("comp1110/ass2/assets/javaFx_catan_dice_costs.png");
        ImageView iv2 = new ImageView();
        iv2.setFitWidth(199);
        iv2.setFitHeight(125);
        iv2.setX(2);
        iv2.setY(124);
        iv2.setImage(image2);

        sidePanel.toBack();
        sidePanel.getChildren().addAll(rightPanel, leftPanel, iv1, iv2);
    }

    public void endTurn() {
        catanPlayer.setCurrentTurn(false);
        if (Game.gameState.num == 1) {
            if (Game.playerOne.equals(catanPlayer)) {
                // UPDATE SCORES
                catanPlayer.setCurrentTurn(true);
                catanPlayer.resource_state = new int[]{0, 0, 0 , 0 ,0,0};
                diceRolled = false;
                diceRollCount = 0;
                catanPlayer.scoreTotal.add(catanPlayer.score);
                if (Game.gameState.round == 15) {
                    catanPlayer.calculateFinalScore();
                }

                try {
                    scores.clear();
                    makeScores();
                    scores.get(Game.gameState.round).setScore(catanPlayer.score);
                    scoreBoardPane.getChildren().add(scores.get(Game.gameState.round));
                    catanPlayer.score = 0;
                    System.out.println(Game.playerOne.scoreTotal);

                    // CHANGE ROUND
                    Game.gameState.round++;
                    catanPlayer.turn_num++;
                } catch (NullPointerException e) {
                    System.out.println("GAME END");
                }


                // UPDATE STRUCTURES
                for (Structure c : catanPlayer.structures) {
                    c.setRemovable(false);
                }
            }
        }
            /*
            Game.two.setCurrentTurn(true);
            Game.playerOne.score = playerScore;
            Game.playerOne.scoreTotal.add(Game.playerOne.score);
            System.out.println(Game.playerOne.scoreTotal);
            makeScores();
            scores.get(0).setScore(playerScore + "");
            scoreBoardPane.getChildren().add(scores.get(0));
            playerScore = 0;

        } else if (Game.playerTwo.equals(catanPlayer)) {
            Game.three.setCurrentTurn(true);
        } else if (Game.playerThree.equals(catanPlayer)) {
            Game.four.setCurrentTurn(true);
        } else if (Game.playerFour.equals(catanPlayer)) {
            Game.one.setCurrentTurn(true);
        } */

    }

    static class Score extends Text {
        int test;
        Score(double x, double y, int test) {
            setText(test + "");
            setFont(Font.font("cambria", FontWeight.BOLD, FontPosture.REGULAR, 20));
            setFill(Color.DARKGREEN);
            setTranslateX(x);
            setTranslateY(y);
            toFront();
        }
        public void setScore(int test) {
            this.test = test;
        }
    }

    public HashMap<Integer, Score> scores = new HashMap<>();

    public void makeScores() {
        scores.put(0, new Score(17, 45, catanPlayer.score));
        scores.put(1, new Score(55, 45, Game.playerOne.score));
        scores.put(2, new Score(55 + 38, 45, Game.playerOne.score));
        scores.put(3, new Score(55 + 38 * 2, 45, Game.playerOne.score));
        scores.put(4, new Score(55 + 38 * 3, 45, Game.playerOne.score));
        scores.put(5, new Score(55 + 38 * 3, 45 + 39, Game.playerOne.score));
        scores.put(10, new Score(17, 123, Game.playerOne.score));
        scores.put(9, new Score(55, 123, Game.playerOne.score));
        scores.put(8, new Score(55 + 38, 123, Game.playerOne.score));
        scores.put(7, new Score(55 + 38 * 2, 123, Game.playerOne.score));
        scores.put(6, new Score(55 + 38 * 3, 123, Game.playerOne.score));
        scores.put(11, new Score(17, 123 + 39, Game.playerOne.score));
        scores.put(12, new Score(17, 123 + 78, Game.playerOne.score));
        scores.put(13, new Score(55, 123 + 78, Game.playerOne.score));
        scores.put(14, new Score(55 + 38, 123 + 78, Game.playerOne.score));
        scores.put(15, new Score(55 + 110, 123 + 78, catanPlayer.finalScore));
    }


    private void makeScoreBoard() {
        scoreBoardView.setFitHeight(200);
        scoreBoardView.setFitWidth(195);
        scoreBoardView.setX(4);
        scoreBoardView.setY(400);
        scoreBoardView.setImage(scoreBoardImage);

        Text text = new Text();
        text.setText(catanPlayer.name + "");
        text.setFont(Font.font("cambria", FontWeight.BOLD, FontPosture.REGULAR, 14));
        text.setFill(Color.WHITE);
        text.setX(0);
        text.setY(0);

        scoreBoardPane.setCenter(scoreBoardView);
        scoreBoardPane.setTop(text);
        scoreBoardPane.setLayoutX(4);
        scoreBoardPane.setLayoutY(400);
        scoreBoardPane.setBackground(new Background(new BackgroundFill(Color.web("#439527"), CornerRadii.EMPTY, Insets.EMPTY)));

        scoreBoard.getChildren().add(scoreBoardPane);
    }

    private void makeDiceRoll() {
        Button button = new Button();
        button.setText("Roll Dice");
        button.setLayoutX(800);
        button.setLayoutY(20);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int[] resource_state = new int[]{1, 1, 1, 1, 1, 1};
                rollDice(4, resource_state); // The array should be the current resources
                for (var a : resource_state) {
                    System.out.print(a + ", ");
                }
            }
        });

        this.controls.getChildren().add(button);
        pane1.setCenter(button);
        controls.getChildren().add(button);
    }

    public void createChooseBoard() {
        ChooseBoardBox vbox;
        ChooseBoard playerOne = new ChooseBoard("PLAYER ONE");
        ChooseBoard playerTwo = new ChooseBoard("PLAYER TWO");
        if (Game.gameState.num == 1) {
            vbox = new ChooseBoardBox(
                    playerOne);
        } else if (Game.gameState.num == 2) {
            vbox = new ChooseBoardBox(
                    playerOne,
                    playerTwo);
        } else {
            vbox = new ChooseBoardBox(
                    playerOne,
                    playerTwo
            );
        }
        vbox.setTranslateX(-3);
        vbox.setTranslateY(270);
        this.chooseBoard.getChildren().addAll(vbox);
    }

    protected void gameButtons() {
        ChooseBoardBox gameButtons;
        ChooseBoard rollDice = new ChooseBoard("ROLL DICE");
        ChooseBoard swapButton = new ChooseBoard("SWAP");
        ChooseBoard tradeButton = new ChooseBoard("TRADE");
        ChooseBoard endTurn = new ChooseBoard("END TURN");
        gameButtons = new ChooseBoardBox(
                rollDice,
                swapButton,
                tradeButton,
                endTurn);
        gameButtons.setTranslateX(950);
        gameButtons.setTranslateY(50);

        controls.getChildren().addAll(gameButtons);
    }


    private static class ChooseBoardBox extends VBox {
        public ChooseBoardBox(ChooseBoard... items) {
            getChildren().add(createSeperator());

            for (ChooseBoard item : items) {
                getChildren().addAll(item, createSeperator());
            }
        }

        private Line createSeperator() {
            Line sep = new Line();
            sep.setEndX(205);
            sep.setStroke(Color.TAN);
            return sep;
        }
    }

    public class ChooseBoard extends StackPane {
        public static String name;

        public ChooseBoard(String name) {
            ChooseBoard.name = name;
            Rectangle bg;
            bg = new Rectangle(200, 30);
            if (catanPlayer.name.equals(name)) {
                bg.setFill(Color.RED);
            } else {
                bg.setFill(Color.BLACK);
            }
            LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, new Stop(0, Color.TAN),
                    new Stop(0.1, Color.BLACK),
                    new Stop(0.9, Color.BLACK),
                    new Stop(1, null));

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
                if (catanPlayer.name.equals(name)) {
                    bg.setFill(Color.RED);
                } else {
                    bg.setFill(Color.BLACK);
                }
                text.setFill(Color.SANDYBROWN);
                GameBoard.warningTextGroup.getChildren().clear();
            });

            setOnMousePressed(event -> {
                switch (name) {
                    case "ROLL DICE" -> {
                        if (diceRollCount < 3) {
                            if (diceRollCount == 0) {
                                diceRollCount++;
                                diceRollButton();
                            } else if (indexOfDice.size() == 0) {
                               new GameBoard.Warning("CHOOSE WHICH DICE TO RE-ROLL");
                            } else {
                                diceRollCount++;
                                diceRollButtonSecond();
                            }

                            int[] resource_state = new int[]{0, 0, 0, 0, 0, 0};
                            rollDice(6, resource_state); // The array should be the current resources
                            for (int i = 0; i < resource_state.length; i++) {
                                System.out.print(resource_state[i] + ", ");
                                catanPlayer.resource_state[i] = resource_state[i];
                                diceRolled = true;
                            }
                        } else {
                            new GameBoard.Warning("MAXIMUM DICE ROLLS IS 3");
                        }
                    }
                    case "TRADE" -> tradeButton();
                    case "SWAP" -> swapButton();
                    case "END TURN" -> {
                        if (diceRolled) {
                            endTurn();
                        } else {
                            new GameBoard.Warning("ROLL DICE FIRST");
                        }
                    }
                    default -> Game.n.activate(name);
                }
            });
        }
    }

    AtomicInteger count = new AtomicInteger();
    Pane swapResourcePane = new Pane();
    public Scene swapResourceScene = new Scene(swapResourcePane);
    public Stage swapResourceStage = new Stage();
    Random random  = new Random();
    int n = 6;
    public int diceRoll() {
        return random.nextInt(6) + 1;
    }
    ArrayList<ImageView> diceArrayList = new ArrayList<>(6);
    ArrayList<Integer> indexOfDice = new ArrayList<>(6);
    GridPane gridPane;
    public void diceRollButton() {
        diceRollGroup.getChildren().clear();
        action.setActionType(ActionType.ROLL);

        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(30);
        gridPane.setHgap(20);

        for (int i = 0; i < n; i++) {
            ResourceImage ri = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource" + diceRoll() + ".png", 10, 10, ResourceType.ORE);
            diceArrayList.add(i, ri);
            gridPane.add(diceArrayList.get(i), i, 0);
        }

        diceRollGroup.toFront();
        gridPane.setLayoutX(900);
        gridPane.setLayoutY(200);
        diceRollGroup.getChildren().add(gridPane);
    }

    public void diceRollButtonSecond() {
        for (Integer i : indexOfDice) {
            gridPane.getChildren().remove(diceArrayList.get(i));
            diceArrayList.get(i).setImage(new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource" + diceRoll() + ".png", 10, 10, ResourceType.ORE).getImage());
            diceArrayList.get(i).setEffect(null);
            gridPane.add(diceArrayList.get(i), i, 0);
        }
        indexOfDice.clear();
    }


    // Highlights all possible knights that can be swapped and sets them to swappable.
    public void swapButton() {
        String[] knightId = new String[]{"K1", "K2", "K3", "K4", "K5", "K6"};
        int index = 0;
        count.getAndIncrement();
        if (diceRolled) {
            if (count.get() == 1) {
                for (GameBoard.KnightShape knightShape : GameBoard.knightsList) {
                    int x = GameBoard.catanBoard.getStructureBlocksMap().get(knightId[index]).getX();
                    int y = GameBoard.catanBoard.getStructureBlocksMap().get(knightId[index]).getY();
                    if (GameBoard.catanBoard.getBuildableStructure(x, y).getStructureType() == StructureType.JOKER) {
                        knightShape.setFill(Color.GOLD);
                        knightShape.setEffect(new DropShadow(30, Color.YELLOW));
                        knightShape.setSwappable(true);
                        action.setActionType(ActionType.SWAP);
                    }
                    index++;
                }
            } else {
                for (GameBoard.KnightShape knightShape : GameBoard.knightsList) {
                    count.set(0);
                    knightShape.setFill(Color.WHITE);
                    knightShape.setEffect(null);
                    knightShape.setSwappable(false);
                    action.setActionType(ActionType.NONE);
                    swapResourceStage.close();
                }
            }
        } else {
            new GameBoard.Warning("ROLL DICE FIRST");
        }
        System.out.println("SWAP");
    }

    public void tradeButton() {
        if (diceRolled) {
            if (catanPlayer.resource_state[5] >= 2)  {
                swapAndTradePopUp(null, ActionType.TRADE);
                action.setActionType(ActionType.TRADE);
                swapResourceStage.show();
                System.out.println("TRADE");
            } else {
                new GameBoard.Warning("NOT ENOUGH GOLD");
            }
        } else {
            new GameBoard.Warning("ROLL DICE FIRST");
        }
    }

    // Creates the clickable images for trading and swapping.
    public class ResourceImage extends ImageView {
        int countRollClick = 0;
        public ResourceImage(String name, int x, int y, ResourceType resourceType) {
            Image c = new Image(name);
            setFitHeight(30);
            setFitWidth(30);
            setX(x);
            setY(y);
            setImage(c);
            setOnMousePressed(event -> {
                if (action.getActionType() == ActionType.SWAP) {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        switch (resourceType) {
                            case ORE -> catanPlayer.changeResourceState(0, -1);
                            case GRAIN -> catanPlayer.changeResourceState(1, -1);
                            case WOOL ->catanPlayer.changeResourceState(2, -1);
                            case TIMBER -> catanPlayer.changeResourceState(3, -1);
                            case BRICKS -> catanPlayer.changeResourceState(4, -1);
                        }
                        System.out.println(catanPlayer.resource_state[1]);
                        int index = 0;
                        String[] knightId = new String[]{"K1", "K2", "K3", "K4", "K5", "K6"};
                        for (GameBoard.KnightShape knightShape : GameBoard.knightsList) {
                            int x2 = GameBoard.catanBoard.getStructureBlocksMap().get(knightId[index]).getX();
                            int y2 = GameBoard.catanBoard.getStructureBlocksMap().get(knightId[index]).getY();
                            StructureType checkType = GameBoard.catanBoard.getBuildableStructure(x2, y2).getStructureType();
                            if (checkType == StructureType.JOKER || checkType == StructureType.USED) {
                                action.setActionType(ActionType.NONE);
                                knightShape.setFill(Color.WHITE);
                                knightShape.setEffect(null);
                                knightShape.setSwappable(false);
                                count.set(0);
                            }
                            index++;
                        }
                        swapResourceStage.close();
                    }
                } else if (action.getActionType() == ActionType.TRADE) {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        switch (resourceType) {
                            case ORE -> catanPlayer.changeResourceState(0, 1);
                            case GRAIN -> catanPlayer.changeResourceState(1, 1);
                            case WOOL ->catanPlayer.changeResourceState(2, 1);
                            case TIMBER -> catanPlayer.changeResourceState(3, 1);
                            case BRICKS -> catanPlayer.changeResourceState(4, 1);
                        }
                        catanPlayer.changeResourceState(5, -2);
                        swapResourceStage.close();
                    }
                } else if (action.getActionType() == ActionType.ROLL) {
                    countRollClick++;
                    if (diceRollCount < 3) {
                        if (countRollClick == 1) {
                            setEffect(new DropShadow(40, Color.BLACK));
                            indexOfDice.add(diceArrayList.indexOf(this));
                            countRollClick++;
                        } else {
                            setEffect(null);
                            countRollClick = 0;
                            indexOfDice.remove((Object) diceArrayList.indexOf(this));
                        }
                    } else {
                        new GameBoard.Warning("MAXIMUM IS 3 DICE ROLLS");
                    }
                }
            });
        }
    }
    // creates stage for clickable images for trading and swapping as a popup.
    public void swapAndTradePopUp(String id, ActionType type) {
        ResourceImage ore = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource1.png", 10, 30, ResourceType.ORE);
        ResourceImage grain = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource2.png", 40, 30, ResourceType.GRAIN);
        ResourceImage wool = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource3.png", 70, 30, ResourceType.WOOL);
        ResourceImage timber = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource4.png", 100, 30, ResourceType.TIMBER);
        ResourceImage bricks = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource5.png", 130, 30, ResourceType.BRICKS);
        ResourceImage[] resourceImages = new ResourceImage[]{ore, grain, wool, timber, bricks};
        for (int i = 0; i < resourceImages.length; i++) {
            if (catanPlayer.resource_state[i] > 0) {
                swapResourcePane.getChildren().add(resourceImages[i]);
            }
        }
        if (type == ActionType.SWAP) {
            switch (id) {
                case "K1" -> catanPlayer.changeResourceState(0, 1);
                case "K2" -> catanPlayer.changeResourceState(1, 1);
                case "K3" -> catanPlayer.changeResourceState(2, 1);
                case "K4" -> catanPlayer.changeResourceState(3, 1);
                case "K5" -> catanPlayer.changeResourceState(4, 1);
                case "K6" -> catanPlayer.changeResourceState(5, 1);
            }
        } else if (type == ActionType.UNDO_SWAP) {
            switch (id) {
                case "K1" -> catanPlayer.changeResourceState(0, -1);
                case "K2" -> catanPlayer.changeResourceState(1, -1);
                case "K3" -> catanPlayer.changeResourceState(2, -1);
                case "K4" -> catanPlayer.changeResourceState(3, -1);
                case "K5" -> catanPlayer.changeResourceState(4, -1);
                case "K6" -> catanPlayer.changeResourceState(5, -1);
            }
        }

        swapResourcePane.setPrefSize(170, 100);
        swapResourceStage.setResizable(true);
        swapResourceStage.setScene(swapResourceScene);
    }
}
