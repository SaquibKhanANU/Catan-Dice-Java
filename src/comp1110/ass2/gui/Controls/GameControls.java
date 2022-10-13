package comp1110.ass2.gui.Controls;

import comp1110.ass2.Action;
import comp1110.ass2.CatanEnum.ActionType;
import comp1110.ass2.CatanEnum.ResourceType;
import comp1110.ass2.CatanEnum.StructureType;
import comp1110.ass2.CatanGame.CatanBoard;
import comp1110.ass2.CatanGame.CatanPlayer;
import comp1110.ass2.CatanGame.GameState;
import comp1110.ass2.CatanStructure.Structure;
import comp1110.ass2.gui.Game;
import comp1110.ass2.gui.Scenes.GameBoard;
import comp1110.ass2.gui.Scenes.Winner;
import gittest.A;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.*;
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
import java.util.Arrays;
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
    public Group warningTextGroup = new Group();
    public Group resourceState = new Group();
    public Group allControls = new Group(controls, chooseBoard, sidePanel, scoreBoard, diceRollGroup, warningTextGroup, resourceState);
    BorderPane pane1 = new BorderPane();
    public CatanPlayer catanPlayer;
    public CatanBoard catanBoard;
    BorderPane scoreBoardPane = new BorderPane();
    Image scoreBoardImage = new Image("comp1110/ass2/assets/CatanScoreBoard.JPG");
    ImageView scoreBoardView = new ImageView();

    public Action action;
    public boolean diceRolled;
    public boolean traded = false;
    public boolean swapped = false;
    public int diceRollCount;
    public HashMap<Integer, Score> scores = new HashMap<>();
    public ArrayList<ResourceImage> reRollDice = new ArrayList<>();
    public ArrayList<GameBoard.KnightShape> knightsList = new ArrayList<>();
    AtomicInteger countSwapPress = new AtomicInteger();
    Pane swapResourcePane = new Pane();
    public Scene swapResourceScene = new Scene(swapResourcePane);
    public Stage swapResourceStage = new Stage();
    Random random  = new Random();
    int n = 6;
    public GameControls(CatanPlayer catanPlayer) {
        this.diceRolled = false;
        this.catanPlayer = catanPlayer;
        this.catanBoard = new CatanBoard();
        makeSidePanel();
        createChooseBoard();
        createGameButtons();
        makeScoreBoard();
        currentResourceState(catanPlayer.resource_state);
        action = new Action(ActionType.NONE);
    }

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
        ResourceType resourceType = null;

        rollDice(6, catanPlayer.resource_state);
        int index = 0;
        for (int i = 0; i < catanPlayer.resource_state.length; i++) {
            switch (i) {
                case 0 -> resourceType = ResourceType.ORE;
                case 1 -> resourceType = ResourceType.GRAIN;
                case 2 -> resourceType = ResourceType.WOOL;
                case 3 -> resourceType = ResourceType.TIMBER;
                case 4 -> resourceType = ResourceType.BRICKS;
                case 5 -> resourceType = ResourceType.GOLD;
            }
            for (int j = 0; j < catanPlayer.resource_state[i]; j++) {
                ResourceImage ri = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource" + (i + 1) + ".png", 10, 10, resourceType);
                diceArrayList.add(index, ri);
                gridPane.add(diceArrayList.get(index), index, 0);
                index++;
            }
        }

        /*for (int i = 0; i < n; i++) {
            ResourceImage ri = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource" + diceRoll() + ".png", 10, 10, ResourceType.ORE);
            diceArrayList.add(i, ri);
            gridPane.add(diceArrayList.get(i), i, 0);
        }*/

        diceRollGroup.toFront();
        gridPane.setLayoutX(900);
        gridPane.setLayoutY(200);
        diceRollGroup.getChildren().add(gridPane);
    }

    public void diceRollButtonSecond() {
        ResourceType resourceType;
        for (ResourceImage ri : reRollDice) {
            switch (ri.getResourceType()) {
                case ORE -> catanPlayer.resource_state[0]--;
                case GRAIN -> catanPlayer.resource_state[1]--;
                case WOOL -> catanPlayer.resource_state[2]--;
                case TIMBER -> catanPlayer.resource_state[3]--;
                case BRICKS -> catanPlayer.resource_state[4]--;
                case GOLD -> catanPlayer.resource_state[5]--;
            }
        }
        reRollDice.clear();
        for (Integer i : indexOfDice) {
            gridPane.getChildren().remove(diceArrayList.get(i));
            int diceRoll = diceRoll();
            switch (diceRoll) {
                case 1 -> {
                    catanPlayer.resource_state[0]++;
                    resourceType = ResourceType.ORE;
                }
                case 2 -> {
                    catanPlayer.resource_state[1]++;
                    resourceType = ResourceType.GRAIN;
                }
                case 3 -> {
                    catanPlayer.resource_state[2]++;
                    resourceType = ResourceType.WOOL;
                }
                case 4 -> {
                    catanPlayer.resource_state[3]++;
                    resourceType = ResourceType.TIMBER;
                }
                case 5 -> {
                    catanPlayer.resource_state[4]++;
                    resourceType = ResourceType.BRICKS;
                }
                case 6 -> {
                    catanPlayer.resource_state[5]++;
                    resourceType = ResourceType.GOLD;
                }
                default -> throw new IllegalStateException("Unexpected value: " + diceRoll);
            }
            ResourceImage new_image = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource" + diceRoll + ".png", 10, 10, resourceType);
            diceArrayList.set(i, new_image);
            diceArrayList.get(i).setEffect(null);
            gridPane.add(diceArrayList.get(i), i, 0);
        }
        indexOfDice.clear();
    }

    // Highlights all possible knights that can be swapped and sets them to swappable.
    public void swapButton() {
        String[] knightId = new String[]{"K1", "K2", "K3", "K4", "K5", "K6"};
        int index = 0;
        countSwapPress.getAndIncrement();
        if (countSwapPress.get() == 1) {
            System.out.println("SWAP");
            for (GameBoard.KnightShape knightShape : knightsList) {
                int x = catanBoard.getStructureBlocksMap().get(knightId[index]).getX();
                int y = catanBoard.getStructureBlocksMap().get(knightId[index]).getY();
                if (catanBoard.getBuildableStructure(x, y).getStructureType() == StructureType.JOKER) {
                    knightShape.setFill(Color.GOLD);
                    knightShape.setEffect(new DropShadow(30, Color.YELLOW));
                    knightShape.setSwappable(true);
                    action.setActionType(ActionType.SWAP);
                }
                index++;
            }
        }  else {
            for (GameBoard.KnightShape knightShape : knightsList) {
                countSwapPress.set(0);
                knightShape.setFill(Color.WHITE);
                knightShape.setEffect(null);
                knightShape.setSwappable(false);
                action.setActionType(ActionType.NONE);
                swapResourceStage.close();
            }
        }
    }

    public void tradeButton() {
        swapAndTradePopUp(null, ActionType.TRADE);
        swapResourceStage.show();
        System.out.println("TRADE");
    }

    public void endTurn() {
        swapped = false;
        traded = false;
        catanPlayer.setCurrentTurn(false);
        catanPlayer.resource_state = new int[]{0, 0, 0 , 0 ,0,0};
        currentResourceState(catanPlayer.resource_state);
        diceRolled = false;
        diceRollCount = 0;
        if (catanPlayer.structuresForRound.size() == 0 && Game.gameState.round < 15) {
            catanPlayer.score = catanPlayer.score - 2;
        }
        catanPlayer.scoreTotal.add(catanPlayer.score);
        catanPlayer.structuresForRound.clear();

        try {
            if (Game.gameState.round == 14) {
                catanPlayer.calculateFinalScore();
            }
            scores.clear();
            makeScores();
            scores.get(Game.gameState.round).setScore(catanPlayer.score);
            scoreBoardPane.getChildren().add(scores.get(Game.gameState.round));
            catanPlayer.score = 0;
            System.out.println(Game.playerOne.scoreTotal);

            // CHANGE ROUND
            catanPlayer.turn_num++;
        } catch (NullPointerException e) {
            System.out.println("GAME END");
        }
        // UPDATE STRUCTURES
        for (Structure c : catanPlayer.structures) {
            c.setRemovable(false);
        }

        if (Game.gameState.num == 1) {
            Game.scenes.activate("PLAYER ONE");
            catanPlayer.setCurrentTurn(true);
            Game.gameState.round++;
            if (Game.gameState.round == 16) {
                ArrayList<CatanPlayer> catanPlayers = new ArrayList<>();
                catanPlayers.add(Game.playerOne);
                Game.winner = new Winner(1, catanPlayers);
                Game.scenes.addScreen("WINNER", Game.winner);
                Game.scenes.activate("WINNER");
            }
        } else if (Game.gameState.num == 2) {
            if (Game.playerOne == catanPlayer) {
                Game.playerTwo.setCurrentTurn(true);
                Game.scenes.activate("PLAYER TWO");
            } else {
                Game.gameState.round++;
                Game.playerOne.setCurrentTurn(true);
                Game.scenes.activate("PLAYER ONE");
                if (Game.gameState.round == 16) {
                    ArrayList<CatanPlayer> catanPlayers = new ArrayList<>();
                    catanPlayers.add(Game.playerOne);
                    catanPlayers.add(Game.playerTwo);
                    catanPlayer.calculateFinalScore();
                    Game.winner = new Winner(2, catanPlayers);
                    Game.scenes.addScreen("WINNER", Game.winner);
                    Game.scenes.activate("WINNER");
                }
            }
        } else if (Game.gameState.num == 3) {
            if (Game.playerOne == catanPlayer) {
                Game.playerTwo.setCurrentTurn(true);
                Game.scenes.activate("PLAYER TWO");
            } else if (Game.playerTwo == catanPlayer) {
                Game.playerThree.setCurrentTurn(true);
                Game.scenes.activate("PLAYER THREE");
            } else {
                Game.gameState.round++;
                Game.playerOne.setCurrentTurn(true);
                Game.scenes.activate("PLAYER ONE");
                if (Game.gameState.round == 16) {
                    ArrayList<CatanPlayer> catanPlayers = new ArrayList<>();
                    catanPlayers.add(Game.playerOne);
                    catanPlayers.add(Game.playerTwo);
                    catanPlayers.add(Game.playerThree);
                    catanPlayer.calculateFinalScore();
                    Game.winner = new Winner(2, catanPlayers);
                    Game.scenes.addScreen("WINNER", Game.winner);
                    Game.scenes.activate("WINNER");
                }
            }
        } else {
            if (Game.playerOne == catanPlayer) {
                Game.playerTwo.setCurrentTurn(true);
                Game.scenes.activate("PLAYER TWO");
            } else if (Game.playerTwo == catanPlayer) {
                Game.playerThree.setCurrentTurn(true);
                Game.scenes.activate("PLAYER THREE");
            } else if (Game.playerThree == catanPlayer) {
                Game.playerFour.setCurrentTurn(true);
                Game.scenes.activate("PLAYER FOUR");
            } else {
                Game.gameState.round++;
                Game.playerOne.setCurrentTurn(true);
                Game.scenes.activate("PLAYER ONE");
                if (Game.gameState.round == 16) {
                    ArrayList<CatanPlayer> catanPlayers = new ArrayList<>();
                    catanPlayers.add(Game.playerOne);
                    catanPlayers.add(Game.playerTwo);
                    catanPlayers.add(Game.playerThree);
                    catanPlayers.add(Game.playerFour);
                    catanPlayer.calculateFinalScore();
                    Game.winner = new Winner(2, catanPlayers);
                    Game.scenes.addScreen("WINNER", Game.winner);
                    Game.scenes.activate("WINNER");
                }
            }
        }
    }


    public class GameButtonsBoard extends StackPane {
        public String name;
        public GameButtonsBoard(String name) {
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
                warningTextGroup.getChildren().clear();
            });
            setOnMousePressed(event -> {
                switch (name) {
                    case "ROLL DICE" -> {
                        if (catanPlayer.structuresForRound.size() == 0 && !swapped && !traded) {
                            if (catanPlayer.currentTurn){
                                if (diceRollCount < 3) {
                                    if (diceRollCount == 0) {
                                        diceRollCount++;
                                        diceRollButton();
                                        System.out.print(Arrays.toString(catanPlayer.resource_state));
                                    } else if (indexOfDice.size() == 0) {
                                        new Warning("CHOOSE WHICH DICE TO RE-ROLL");
                                    } else {
                                        diceRollCount++;
                                        diceRollButtonSecond();
                                        System.out.println(Arrays.toString(catanPlayer.resource_state));
                                    }
                                    off = true;
                                    currentResourceState(catanPlayer.resource_state);
                                    diceRolled = true;
                                    /*
                                    int[] resource_state = new int[]{0, 0, 0, 0, 0, 0};
                                    rollDice(6, resource_state); // The array should be the current resources
                                    for (int i = 0; i < resource_state.length; i++) {
                                        System.out.print(resource_state[i] + ", ");
                                        catanPlayer.resource_state[i] = resource_state[i]; */
                                } else new Warning("MAXIMUM DICE ROLLS IS 3");
                            } else new Warning("NOT YOUR TURN");
                        } else new Warning("ALREADY DID ACTION");
                    }
                    case "TRADE" -> {
                        if (diceRolled) {
                            if (catanPlayer.resource_state[5] >= 2) {
                                action.setActionType(ActionType.TRADE);
                                tradeButton();
                            } else {
                                new Warning("NOT ENOUGH GOLD");
                            }
                        } else {
                            new Warning("ROLL DICE FIRST");
                        }
                    }
                    case "SWAP" -> {
                        if(diceRolled) {
                            swapButton();
                        } else {
                            new Warning("ROLL DICE FIRST");
                        }
                    }
                    case "END TURN" -> {
                        if (catanPlayer.currentTurn) {
                            if (diceRolled) {
                                endTurn();
                            } else {
                                new Warning("ROLL DICE FIRST");
                            }
                        }
                    }
                    default -> Game.scenes.activate(name);
                }
            });
        }
    }
    public boolean off;
    public ArrayList<ResourceImage> clickedAlready = new ArrayList<>();

    // Creates the clickable images for trading and swapping.
    public class ResourceImage extends ImageView {
        public boolean clickable = true;
        ResourceType resourceType;
        AtomicInteger countRollClick = new AtomicInteger();

        public ResourceImage(String name, int x, int y, ResourceType resourceType) {
            this.resourceType = resourceType;
            Image c = new Image(name);
            setFitHeight(30);
            setFitWidth(30);
            setX(x);
            setY(y);
            setImage(c);
            setOnMouseEntered(e -> {
                if (clickable && off) setEffect(new Glow(1));
            });
            setOnMouseExited(e -> {
                if (clickable && off) setEffect(null);
            });
            setOnMousePressed(event -> {
                System.out.println(catanPlayer.board_state);
                System.out.println(Arrays.toString(catanPlayer.resource_state));
                if (clickable) {
                    if (action.getActionType() == ActionType.SWAP) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            switch (resourceType) {
                                case ORE -> catanPlayer.changeResourceState(0, -1);
                                case GRAIN -> catanPlayer.changeResourceState(1, -1);
                                case WOOL ->catanPlayer.changeResourceState(2, -1);
                                case TIMBER -> catanPlayer.changeResourceState(3, -1);
                                case BRICKS -> catanPlayer.changeResourceState(4, -1);
                            }
                            resourceState.getChildren().clear();
                            swapped = true;
                            int index = 0;
                            String[] knightId = new String[]{"K1", "K2", "K3", "K4", "K5", "K6"};
                            for (GameBoard.KnightShape knightShape : knightsList) {
                                int x2 = catanBoard.getStructureBlocksMap().get(knightId[index]).getX();
                                int y2 = catanBoard.getStructureBlocksMap().get(knightId[index]).getY();
                                StructureType checkType = catanBoard.getBuildableStructure(x2, y2).getStructureType();
                                if (checkType == StructureType.JOKER || checkType == StructureType.USED) {
                                    action.setActionType(ActionType.NONE);
                                    knightShape.setFill(Color.WHITE);
                                    knightShape.setEffect(null);
                                    knightShape.setSwappable(false);
                                    countSwapPress.set(0);
                                }
                                index++;
                            }
                            currentResourceState(catanPlayer.resource_state);
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
                            currentResourceState(catanPlayer.resource_state);
                            action.setActionType(ActionType.NONE);
                            traded = true;
                            swapResourceStage.close();
                        }
                    } else {
                        if (diceRollCount < 3) {
                            countRollClick.getAndIncrement();
                            if (countRollClick.get() == 1) {
                                off = false;
                                Blend blend = new Blend();
                                blend.setMode(BlendMode.ADD);
                                Glow glow = new Glow(0.9);
                                DropShadow dropShadow = new DropShadow(40, Color.RED);
                                blend.setBottomInput(glow);
                                blend.setTopInput(dropShadow);
                                setEffect(blend);
                                indexOfDice.add(diceArrayList.indexOf(this));
                                System.out.println(this);
                                reRollDice.add(this);
                                System.out.println("ADDED");
                            } else {
                                off = true;
                                setEffect(null);
                                countRollClick.set(0);
                                indexOfDice.remove((Object) diceArrayList.indexOf(this));
                                reRollDice.remove(this);
                            }
                        } else {
                            new Warning("MAXIMUM IS 3 DICE ROLLS");
                        }
                    }
                }
            });
        }
        public ResourceImage(String name, int x, int y, ResourceType resourceType, boolean swap) {
            this.resourceType = resourceType;
            Image c = new Image(name);
            setFitHeight(30);
            setFitWidth(30);
            setX(x);
            setY(y);
            setImage(c);
            setOnMouseEntered(e -> {
                if (clickable && off) setEffect(new Glow(1));
            });
            setOnMouseExited(e -> {
                if (clickable && off) setEffect(null);
            });
            setOnMousePressed(e -> {
                if (swap) {
                    if (clickedAlready.size() < 1) {
                        countRollClick.getAndIncrement();
                        if (countRollClick.get() == 1) {
                            setEffect(new Glow(0.9));
                            off = false;
                            clickedAlready.add(this);
                            switch (resourceType) {
                                case ORE -> catanPlayer.changeResourceState(0, -1);
                                case GRAIN -> catanPlayer.changeResourceState(1, -1);
                                case WOOL -> catanPlayer.changeResourceState(2, -1);
                                case TIMBER -> catanPlayer.changeResourceState(3, -1);
                                case BRICKS -> catanPlayer.changeResourceState(4, -1);
                                case GOLD -> catanPlayer.changeResourceState(5, -1);
                            }
                            System.out.println(clickedAlready);
                        } else {
                            off = true;
                            setEffect(null);
                            countRollClick.set(0);
                            clickedAlready.remove(this);
                        }
                    } else {
                        off = true;
                        setEffect(null);
                        countRollClick.set(0);
                        clickedAlready.remove(this);
                    }
                } else {
                    if (clickedAlready.size() == 1) {
                        switch (resourceType) {
                            case ORE -> catanPlayer.changeResourceState(0, +1);
                            case GRAIN -> catanPlayer.changeResourceState(1, +1);
                            case WOOL ->catanPlayer.changeResourceState(2, +1);
                            case TIMBER -> catanPlayer.changeResourceState(3, +1);
                            case BRICKS -> catanPlayer.changeResourceState(4, +1);
                            case GOLD -> catanPlayer.changeResourceState(5, +1);
                        }
                        System.out.println(this);
                        int index = 0;
                        String[] knightId = new String[]{"K1", "K2", "K3", "K4", "K5", "K6"};
                        for (GameBoard.KnightShape knightShape : knightsList) {
                            int x2 = catanBoard.getStructureBlocksMap().get(knightId[index]).getX();
                            int y2 = catanBoard.getStructureBlocksMap().get(knightId[index]).getY();
                            StructureType checkType = catanBoard.getBuildableStructure(x2, y2).getStructureType();
                            if (checkType == StructureType.JOKER || checkType == StructureType.USED) {
                                action.setActionType(ActionType.NONE);
                                knightShape.setFill(Color.WHITE);
                                knightShape.setEffect(null);
                                knightShape.setSwappable(false);
                                countSwapPress.set(0);
                            }
                            index++;
                        }
                        swapResourceStage.close();
                    }
                    currentResourceState(catanPlayer.resource_state);
                }

            });
        }
        public ResourceType getResourceType() {
            return resourceType;
        }
        public void setClickable(boolean clickable) {
            this.clickable = clickable;
        }

        @Override
        public String toString() {
            return "ResourceImage{" +
                    "clickable=" + clickable +
                    ", resourceType=" + resourceType +
                    ", countRollClick=" + countRollClick +
                    '}';
        }
    }

    // creates stage for clickable images for trading and swapping as a popup.
    public void swapAndTradePopUp(String id, ActionType type) {
        action.setActionType(type);
        ResourceImage ore = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource1.png", 10, 30, ResourceType.ORE);
        ResourceImage grain = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource2.png", 40, 30, ResourceType.GRAIN);
        ResourceImage wool = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource3.png", 70, 30, ResourceType.WOOL);
        ResourceImage timber = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource4.png", 100, 30, ResourceType.TIMBER);
        ResourceImage bricks = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource5.png", 130, 30, ResourceType.BRICKS);
        ResourceImage[] resourceImages = new ResourceImage[]{ore, grain, wool, timber, bricks};
        swapResourcePane.getChildren().clear();

        for (int i = 0; i < resourceImages.length; i++) {
            if (action.getActionType() == ActionType.SWAP) {
                if (catanPlayer.resource_state[i] > 0) {
                    swapResourcePane.getChildren().add(resourceImages[i]);
                }
            } else {
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
            }
        } else if (type == ActionType.UNDO_SWAP) {
            switch (id) {
                case "K1" -> catanPlayer.changeResourceState(0, -1);
                case "K2" -> catanPlayer.changeResourceState(1, -1);
                case "K3" -> catanPlayer.changeResourceState(2, -1);
                case "K4" -> catanPlayer.changeResourceState(3, -1);
                case "K5" -> catanPlayer.changeResourceState(4, -1);
            }
        }
        swapResourcePane.setPrefSize(170, 100);
        swapResourceStage.setResizable(true);
        swapResourceStage.setScene(swapResourceScene);
        swapResourceStage.show();

        if (action.getActionType() == ActionType.TRADE) {
            swapResourceStage.setOnCloseRequest(e -> {
                action.setActionType(ActionType.NONE);
            });
        }
    }

    Group t = new Group();
    Group a = new Group();
    Group c = new Group();
    public void WildCardPopUp(ActionType actionType) {
        action.setActionType(actionType);
        swapResourcePane.getChildren().clear();
        t.getChildren().clear();
        a.getChildren().clear();
        c.getChildren().clear();
        ResourceImage ore = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource1.png", 50, 5, ResourceType.ORE, true);
        ResourceImage grain = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource2.png", 100, 5, ResourceType.GRAIN, true);
        ResourceImage wool = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource3.png", 150, 5, ResourceType.WOOL, true);
        ResourceImage timber = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource4.png", 200, 5, ResourceType.TIMBER, true);
        ResourceImage bricks = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource5.png", 250, 5, ResourceType.BRICKS, true);
        ResourceImage gold = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource6.png", 300, 5, ResourceType.GOLD, true);
        ResourceImage[] resourceImages = new ResourceImage[]{ore, grain, wool, timber, bricks, gold};
        ResourceType[] resourceTypes = new ResourceType[]{ResourceType.ORE, ResourceType.GRAIN, ResourceType.WOOL, ResourceType.TIMBER, ResourceType.BRICKS, ResourceType.GOLD};
        for (int i = 0; i < resourceTypes.length; i++) {
            ResourceImage ri = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource" + (i+1) + ".png", 50 * i + 50, 50, resourceTypes[i], false);
            c.getChildren().add(ri);
        }

        Text swap = new Text();
        swap.setText("SWAP:");
        swap.setX(10);
        swap.setY(20);
        swap.setFill(Color.DARKGREEN);
        swap.setFont(Font.font("times new roman", FontWeight.BOLD, FontPosture.REGULAR, 14));

        Text swapFor = new Text();
        swapFor.setText("FOR:");
        swapFor.setX(10);
        swapFor.setY(70);
        swapFor.setFill(Color.DARKGREEN);
        swapFor.setFont(Font.font("times new roman", FontWeight.BOLD, FontPosture.REGULAR, 14));

        for (int i = 0; i < resourceImages.length; i++) {
            if (catanPlayer.resource_state[i] > 0) {
                t.getChildren().add(resourceImages[i]);
            }
        }

        if (actionType == ActionType.UNDO_SWAP) {

        }


        a.getChildren().addAll(swap, swapFor);
        swapResourcePane.setPrefSize(330, 100);
        swapResourceStage.setResizable(false);
        swapResourcePane.getChildren().addAll(a, t, c);
        swapResourceStage.setScene(swapResourceScene);
        swapResourceStage.show();

        swapResourceStage.setOnCloseRequest(e -> {
            action.setActionType(ActionType.NONE);
        });
    }

    public class CurrentResourceState extends GridPane {
        ArrayList<ResourceImage> resourceArrayList = new ArrayList<>();
        ArrayList<ResourceImage> resourceImageArrayList = new ArrayList<>();
        int index = 0;
        int[] resource_state;
        public CurrentResourceState(int[] resource_state) {
            this.resource_state = resource_state;
            setPadding(new Insets(10, 10, 10, 10));
            setVgap(30);
            setHgap(20);
            for (int i = 0; i < resource_state.length; i++) {
                ResourceImage ri = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource" + (i+1) + ".png", 10, 10, null);
                ri.setClickable(false);
                resourceArrayList.add(i, ri);
                add(resourceArrayList.get(i), i, 0);
                for (int j = 0; j < resource_state[i]; j++) {
                    ResourceImage ri2 = new ResourceImage("comp1110/ass2/assets/ResourceImages/Resource" + (i+1) + ".png", 10, 10, null);
                    ri2.setClickable(false);
                    resourceImageArrayList.add(index, ri2);
                    add(resourceImageArrayList.get(index), i, j+1);
                    index++;
                }
            }
        }
    }
    public void currentResourceState(int[] resource_state) {
        resourceState.getChildren().clear();
        Rectangle r = new Rectangle();
        r.setFill(Color.TAN);
        r.setWidth(296);
        r.setHeight(350);
        r.toBack();
        r.setStrokeWidth(4);
        r.setStroke(Color.BLACK);
        Rectangle line = new Rectangle();
        line.setWidth(296);
        line.setHeight(3);
        line.setY(40);
        new CurrentResourceState(resource_state);
        resourceState.getChildren().addAll(r, line, new CurrentResourceState(catanPlayer.resource_state));
        resourceState.setLayoutY(250);
        resourceState.setLayoutX(900);
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

    class Score extends Text {
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

    public void makeScores() {
        scores.put(0, new Score(12, 45, catanPlayer.score));
        scores.put(1, new Score(50, 45, catanPlayer.score));
        scores.put(2, new Score(50 + 38, 45, catanPlayer.score));
        scores.put(3, new Score(50 + 38 * 2, 45, catanPlayer.score));
        scores.put(4, new Score(50 + 38 * 3, 45, catanPlayer.score));
        scores.put(5, new Score(50 + 38 * 3, 45 + 39, catanPlayer.score));
        scores.put(10, new Score(12, 123, catanPlayer.score));
        scores.put(9, new Score(50, 123, catanPlayer.score));
        scores.put(8, new Score(50 + 38, 123, catanPlayer.score));
        scores.put(7, new Score(50 + 38 * 2, 123, catanPlayer.score));
        scores.put(6, new Score(50 + 38 * 3, 123, catanPlayer.score));
        scores.put(11, new Score(12, 123 + 39,catanPlayer.score));
        scores.put(12, new Score(12, 123 + 78, catanPlayer.score));
        scores.put(13, new Score(50, 123 + 78, catanPlayer.score));
        scores.put(14, new Score(50 + 38, 123 + 78, catanPlayer.score));
        scores.put(15, new Score(40 + 110, 123 + 78, catanPlayer.finalScore));
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

    protected void createGameButtons() {
        ChooseBoardBox gameButtons;
        GameButtonsBoard rollDice = new GameButtonsBoard("ROLL DICE");
        GameButtonsBoard swapButton = new GameButtonsBoard("SWAP");
        GameButtonsBoard tradeButton = new GameButtonsBoard("TRADE");
        GameButtonsBoard endTurn = new GameButtonsBoard("END TURN");
        gameButtons = new ChooseBoardBox(
                rollDice,
                swapButton,
                tradeButton,
                endTurn);
        gameButtons.setTranslateX(950);
        gameButtons.setTranslateY(50);

        controls.getChildren().addAll(gameButtons);
    }

    public void createChooseBoard() {
        ChooseBoardBox vbox;
        GameButtonsBoard playerOne = new GameButtonsBoard("PLAYER ONE");
        GameButtonsBoard playerTwo = new GameButtonsBoard("PLAYER TWO");
        GameButtonsBoard playerThree = new GameButtonsBoard("PLAYER THREE");
        GameButtonsBoard playerFour = new GameButtonsBoard("PLAYER FOUR");
        if (Game.gameState.num == 1) {
            vbox = new ChooseBoardBox(
                    playerOne);
        } else if (Game.gameState.num == 2) {
            vbox = new ChooseBoardBox(
                    playerOne,
                    playerTwo);
        } else if (Game.gameState.num == 3) {
            vbox = new ChooseBoardBox(
                    playerOne,
                    playerTwo,
                    playerThree
            );
        } else {
            vbox = new ChooseBoardBox(
                    playerOne,
                    playerTwo,
                    playerThree,
                    playerFour
            );
        }
        vbox.setTranslateX(-3);
        vbox.setTranslateY(270);
        this.chooseBoard.getChildren().addAll(vbox);
    }

    private class ChooseBoardBox extends VBox {
        public ChooseBoardBox(GameButtonsBoard... items) {
            getChildren().add(createSeperator());

            for (GameButtonsBoard item : items) {
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

    public class Warning extends Text {
        public Warning(String warning) {
            setText(warning);
            setText(warning);
            toFront();
            setFill(Color.RED);
            setFont(Font.font("times new roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
            setX(430);
            setY(40);
            warningTextGroup.getChildren().add(this);
        }
    }
}
