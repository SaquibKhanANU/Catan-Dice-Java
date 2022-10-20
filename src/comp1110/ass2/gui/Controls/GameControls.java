package comp1110.ass2.gui.Controls;

import comp1110.ass2.CatanStructure.Action;
import comp1110.ass2.CatanEnum.ActionType;
import comp1110.ass2.CatanEnum.ResourceType;
import comp1110.ass2.CatanEnum.StructureType;
import comp1110.ass2.CatanGame.CatanBoard;
import comp1110.ass2.CatanGame.CatanPlayer;
import comp1110.ass2.CatanStructure.Structure;
import comp1110.ass2.gui.Game;
import comp1110.ass2.gui.Scenes.GameBoard;
import comp1110.ass2.gui.Scenes.Winner;
import gittest.C;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
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

// Author: Saquib Khan
public class GameControls {
    Group controls = new Group();
    Group instructions = new Group();
    Group chooseBoard = new Group();
    Group sidePanel = new Group();
    Group scoreBoard = new Group();
    Group diceRollGroup = new Group();
    Group currentTurnTextGroup = new Group();
    public Group warningTextGroup = new Group();
    Group resourceStateGroup = new Group();
    Pane swapResourcePane = new Pane();
    BorderPane scoreBoardPane = new BorderPane();
    GridPane diceRollGridPane;
    Scene swapResourceScene = new Scene(swapResourcePane);
    public Stage swapResourceStage = new Stage();
    public Group allControls = new Group(controls, chooseBoard, sidePanel, scoreBoard, diceRollGroup, warningTextGroup, resourceStateGroup, currentTurnTextGroup, instructions);
    public CatanPlayer catanPlayer;
    public CatanBoard catanBoard;

    public Action action = new Action(ActionType.NONE);
    // counts how many times a die has been rolled in a turn
    int diceRollCount;
    // counts how many times swap button has been pressed
    AtomicInteger countSwapPress = new AtomicInteger();
    // Stores the visual score texts of the player
    HashMap<Integer, Score> scores = new HashMap<>();
    // Stores the image of the dice that is being re-rolled.
    ArrayList<ResourceImage> reRollDice = new ArrayList<>();
    //Stores all the knights from GameBoard
    public ArrayList<GameBoard.KnightShape> knightsList = new ArrayList<>();
    //contains all the dice
    ArrayList<ImageView> diceArrayList = new ArrayList<>(6);
    // Stores the index of the dice chosen
    ArrayList<Integer> indexOfDice = new ArrayList<>(6);
    // stores the image that has been clicked
    public ArrayList<ResourceImage> clickedAlready = new ArrayList<>();
    // checks if dice has been rolled
    public boolean diceRolled = false;
    // checks if a trade has occurred
    boolean traded = false;
    // checks if a swap has occurred
    boolean swapped = false;
    // checks if a image hsa been click.
    public boolean imageClickOff;

    /**
     * Constructs an instance of the game controls (all the buttons and visuals assocciated with them)
     * for each catan player. GameControls creates the game controls.
     * @Author Saquib Khan
     * @param catanPlayer the catan player the game controls belong to
     */
    public GameControls(CatanPlayer catanPlayer) {
        this.catanPlayer = catanPlayer;
        this.catanBoard = new CatanBoard();
        makeSidePanel();
        createChooseBoard();
        createGameButtons();
        createInstructionsButton();
        makeScoreBoard();
        currentResourceState(catanPlayer.resource_state);
    }

    /**
     * simple dice roll producing number 1 - 6.
     * @return a random number 1 -6
     */
    private int diceRoll() {
        Random random  = new Random();
        return random.nextInt(6) + 1;
    }

    /**
     * The dice roll button, rolls the dice using rollDice from catanDice and produced images based on what has
     * been rolled.
     */
    private void diceRollButton() {
        diceRollGroup.getChildren().clear();
        reRollDice.clear();
        indexOfDice.clear();
        diceRollGridPane = new GridPane();
        diceRollGridPane.setPadding(new Insets(10, 10, 10, 10));
        diceRollGridPane.setVgap(30);
        diceRollGridPane.setHgap(20);
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
                diceRollGridPane.add(diceArrayList.get(index), index, 0);
                index++;
            }
        }

        diceRollGroup.toFront();
        diceRollGridPane.setLayoutX(900);
        diceRollGridPane.setLayoutY(200);
        diceRollGroup.getChildren().add(diceRollGridPane);
    }

    /**
     * dice roll button second, computes the roll after the first dice roll, changes any dice in reRollDice
     * (rolls the dice selected by user).
     */
    private void diceRollButtonSecond() {
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
            diceRollGridPane.getChildren().remove(diceArrayList.get(i));
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
            diceRollGridPane.add(diceArrayList.get(i), i, 0);
        }
        indexOfDice.clear();
    }


    /**
     * Highlights all possible knights that can be swapped and sets them to swappable.
     * If clicked twice it reverts.
     */
    private void swapButton() {
        String[] knightId = new String[]{"J1", "J2", "J3", "J4", "J5", "J6"};
        int index = 0;
        countSwapPress.getAndIncrement();
        if (countSwapPress.get() == 1) {
            for (GameBoard.KnightShape knightShape : knightsList) {
                int x = catanBoard.getStructureBlocksMap().get(knightId[index]).getX();
                int y = catanBoard.getStructureBlocksMap().get(knightId[index]).getY();
                if (catanBoard.getBuildableStructure(x, y).getStructureType() == StructureType.KNIGHT) {
                    knightShape.setFill(Color.GOLD);
                    knightShape.setEffect(new DropShadow(30, Color.YELLOW));
                    knightShape.setSwappable(true);
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

    /**
     * Trade button pops up the trade stage, where a trade can be done.
     */
    private void tradeButton() {
        swapAndTradePopUp(null, ActionType.TRADE);
        swapResourceStage.show();
        System.out.println("TRADE");
    }

    /**
     * End turn button, ends the turn and resets every back to default, switches player turn if more than 1 player
     * and ends the game at round 15 to activate winner page.
     */
    private void endTurn() {
        swapped = false;
        traded = false;
        catanPlayer.setCurrentTurn(false);
        catanPlayer.resource_state = new int[]{0, 0, 0 , 0 ,0, 0};
        currentResourceState(catanPlayer.resource_state);
        diceRolled = false;
        diceRollCount = 0;
        if (catanPlayer.structuresForRound.size() == 0 && Game.gameState.round < 15) {
            catanPlayer.score = catanPlayer.score - 2;
        }
        catanPlayer.scoreTotal.add(catanPlayer.score);
        catanPlayer.structuresForRound.clear();

        try {
            if (catanPlayer.turn_num >= 14) {
                catanPlayer.calculateFinalScore();
                scores.clear();
                makeScores();
                scores.get(15).setScore(catanPlayer.finalScore);
                scoreBoardPane.getChildren().add(scores.get(15));
            }
            scores.clear();
            makeScores();
            scores.get(Game.gameState.round).setScore(catanPlayer.score);
            scoreBoardPane.getChildren().add(scores.get(Game.gameState.round));
            catanPlayer.score = 0;


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
            if (Game.gameState.round >= 15) {
                catanPlayer.setCurrentTurn(false);
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
                if (Game.gameState.round >= 15) {
                    catanPlayer.setCurrentTurn(false);
                    ArrayList<CatanPlayer> catanPlayers = new ArrayList<>();
                    catanPlayers.add(Game.playerOne);
                    catanPlayers.add(Game.playerTwo);
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
                if (Game.gameState.round >= 15) {
                    catanPlayer.setCurrentTurn(false);
                    ArrayList<CatanPlayer> catanPlayers = new ArrayList<>();
                    catanPlayers.add(Game.playerOne);
                    catanPlayers.add(Game.playerTwo);
                    catanPlayers.add(Game.playerThree);
                    Game.winner = new Winner(3, catanPlayers);
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
                Game.gameState.round+=20;
                Game.playerOne.setCurrentTurn(true);
                Game.scenes.activate("PLAYER ONE");
                if (Game.gameState.round >= 15) {
                    catanPlayer.setCurrentTurn(false);
                    ArrayList<CatanPlayer> catanPlayers = new ArrayList<>();
                    catanPlayers.add(Game.playerOne);
                    catanPlayers.add(Game.playerTwo);
                    catanPlayers.add(Game.playerThree);
                    catanPlayers.add(Game.playerFour);
                    Game.winner = new Winner(4, catanPlayers);
                    Game.scenes.addScreen("WINNER", Game.winner);
                    Game.scenes.activate("WINNER");
                }
            }
        }
    }
    // Author: Saquib Khan, visuals influenced by third party.
    private class GameButtonsBoard extends StackPane {
        /**
         * Constructs a button based on a name. The button also performs an action based on its name.
         * This constructor contains the visuals and checks if action can be performed before performing it.
         * @Author Saquib Khan
         * @param name the string name of the button
         */
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
                                    imageClickOff = true;
                                    currentResourceState(catanPlayer.resource_state);
                                    diceRolled = true;
                                } else new Warning("MAXIMUM DICE ROLLS IS 3");
                            } else new Warning("NOT YOUR TURN");
                        } else new Warning("ALREADY DID ACTION");
                    }
                    case "TRADE" -> {
                        if (diceRolled) {
                            if (catanPlayer.resource_state[5] >= 2) {
                                tradeButton();
                            } else {
                                new Warning("NOT ENOUGH GOLD");
                            }
                        } else {
                            new Warning("ROLL DICE FIRST");
                        }
                    }
                    case "SWAP" -> {
                        if (catanPlayer.currentTurn) {
                            if (diceRolled) {
                                swapButton();
                            } else {
                                new Warning("ROLL DICE FIRST");
                            }
                        } else {
                            new Warning("NOT YOUR TURN");
                        }
                    }
                    case "END TURN" -> {
                        if (catanPlayer.currentTurn) {
                            if (diceRolled) {
                                endTurn();
                            } else {
                                new Warning("ROLL DICE FIRST");
                            }
                        } else {
                            new Warning("NOT YOUR TURN");
                        }
                    }
                    case "INSTRUCTIONS" -> {
                        Game.boardName.add(catanPlayer.name);
                        Game.scenes.activate("INSTRUCTIONS");
                    }
                    default -> Game.scenes.activate(name);
                }
            });
        }
    }

    // Author: Saquib Khan
    // Creates the clickable images for trading and swapping.
    public class ResourceImage extends ImageView {
        // Some resources aren't clickable, e.g. the ones displayed in current resource state.
        public boolean clickable = true;
        // Resource type of the image.
        ResourceType resourceType;
        // Counts how many times a image is clicked (0 is off) (1 is on)
        AtomicInteger countClick = new AtomicInteger();

        /**
         * Resource image is a clickable image used to perform an action done through the image
         * depending on which type of action it is. Undos an action when an image is clicked again,
         * A clcik will highlight or unhighlight an image.
         *
         * @Author Saquib Khan
         * @param name path from source root
         * @param x the x position of the image on a pane
         * @param y the y position of the image on a pane
         * @param resourceType the type of resource this resource image represents.
         */
        private ResourceImage(String name, int x, int y, ResourceType resourceType) {
            this.resourceType = resourceType;
            Image c = new Image(name);
            setFitHeight(30);
            setFitWidth(30);
            setX(x);
            setY(y);
            setImage(c);
            setOnMouseEntered(e -> {
                if (clickable && imageClickOff) setEffect(new Glow(1));
            });
            setOnMouseExited(e -> {
                if (clickable && imageClickOff) setEffect(null);
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
                            resourceStateGroup.getChildren().clear();
                            swapped = true;
                            int index = 0;
                            String[] knightId = new String[]{"J1", "J2", "J3", "J4", "J5", "J6"};
                            for (GameBoard.KnightShape knightShape : knightsList) {
                                int x2 = catanBoard.getStructureBlocksMap().get(knightId[index]).getX();
                                int y2 = catanBoard.getStructureBlocksMap().get(knightId[index]).getY();
                                StructureType checkType = catanBoard.getBuildableStructure(x2, y2).getStructureType();
                                if (checkType == StructureType.KNIGHT || checkType == StructureType.USED) {
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
                            countClick.getAndIncrement();
                            if (countClick.get() == 1) {
                                imageClickOff = false;
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
                                imageClickOff = true;
                                setEffect(null);
                                countClick.set(0);
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

        /**
         * Same as above, used for wild card. Wildcard will contain two resource image sets,
         * one of which is being swapped in and one which is swapped out. Constructor differentiates them
         * through boolean swap.
         *
         * @param name path from source root
         * @param x the x position of the image on a pane
         * @param y the y position of the image on a pane
         * @param resourceType the type of resource this resource image represents.
         * @param swap boolean for checking it is a swap image
         */
        private ResourceImage(String name, int x, int y, ResourceType resourceType, boolean swap) {
            this.resourceType = resourceType;
            Image c = new Image(name);
            setFitHeight(30);
            setFitWidth(30);
            setX(x);
            setY(y);
            setImage(c);
            setOnMouseEntered(e -> {
                if (clickable && imageClickOff) setEffect(new Glow(1));
            });
            setOnMouseExited(e -> {
                if (clickable && imageClickOff) setEffect(null);
            });
            setOnMousePressed(e -> {
                if (swap) {
                    if (clickedAlready.size() < 1) {
                        countClick.getAndIncrement();
                        if (countClick.get() == 1) {
                            setEffect(new Glow(0.9));
                            imageClickOff = false;
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
                            imageClickOff = true;
                            setEffect(null);
                            countClick.set(0);
                            clickedAlready.remove(this);
                        }
                    } else {
                        imageClickOff = true;
                        setEffect(null);
                        countClick.set(0);
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
                        String[] knightId = new String[]{"J1", "J2", "J3", "J4", "J5", "J6"};
                        for (GameBoard.KnightShape knightShape : knightsList) {
                            int x2 = catanBoard.getStructureBlocksMap().get(knightId[index]).getX();
                            int y2 = catanBoard.getStructureBlocksMap().get(knightId[index]).getY();
                            StructureType checkType = catanBoard.getBuildableStructure(x2, y2).getStructureType();
                            if (checkType == StructureType.KNIGHT || checkType == StructureType.USED) {
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
        /**
         * gets the resource type of the image
         */
        public ResourceType getResourceType() {
            return resourceType;
        }
        /**
         * sets if the imgae is clickable or not
         */
        public void setClickable(boolean clickable) {
            this.clickable = clickable;
        }

        @Override
        public String toString() {
            return "ResourceImage{" +
                    "clickable=" + clickable +
                    ", resourceType=" + resourceType +
                    ", countRollClick=" + countClick +
                    '}';
        }
    }

    /**
     * Swap and trade pop up shows the visual pop for user interaction to swap or trade.
     * Contains the resources that can be swapped, and all resources are displayed for trade.
     * Sets the action type to the type provided.
     *
     * @param id The id of the knight being swapped.
     * @param type The type of action being perforemd (undo_swap, swap or trade)
     */
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
                case "J1" -> catanPlayer.changeResourceState(0, 1);
                case "J2" -> catanPlayer.changeResourceState(1, 1);
                case "J3" -> catanPlayer.changeResourceState(2, 1);
                case "J4" -> catanPlayer.changeResourceState(3, 1);
                case "J5" -> catanPlayer.changeResourceState(4, 1);
            }
        } else if (type == ActionType.UNDO_SWAP) {
            switch (id) {
                case "J1" -> catanPlayer.changeResourceState(0, -1);
                case "J2" -> catanPlayer.changeResourceState(1, -1);
                case "J3" -> catanPlayer.changeResourceState(2, -1);
                case "J4" -> catanPlayer.changeResourceState(3, -1);
                case "J5" -> catanPlayer.changeResourceState(4, -1);
            }
        }
        swapResourcePane.setPrefSize(170, 100);
        swapResourceStage.setResizable(true);
        swapResourceStage.setScene(swapResourceScene);
        swapResourceStage.show();

        if (action.getActionType() == ActionType.TRADE) {
            swapResourceStage.setOnCloseRequest(e -> action.setActionType(ActionType.NONE));
        }
    }

    /**
     * Makes the pop-up for J6, wildcard gives the option to swap anyting available including gold for anything.
     * This shows the popup for player interaction.
     * @param actionType the action type being perfomed to set action to this type.
     */
    public void WildCardPopUp(ActionType actionType) {
        // Group to store resource images player currently has
        Group resourceImagesGroup = new Group();
        // text storing instructions for wildcard
        Group wildCardText = new Group();
        // Group storing all resources images
        Group resourceImagesGroup2 = new Group();

        action.setActionType(actionType);
        swapResourcePane.getChildren().clear();
        resourceImagesGroup.getChildren().clear();
        wildCardText.getChildren().clear();
        resourceImagesGroup2.getChildren().clear();
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
            resourceImagesGroup2.getChildren().add(ri);
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
                resourceImagesGroup.getChildren().add(resourceImages[i]);
            }
        }

        wildCardText.getChildren().addAll(swap, swapFor);
        swapResourcePane.setPrefSize(330, 100);
        swapResourceStage.setResizable(false);
        swapResourcePane.getChildren().addAll(wildCardText, resourceImagesGroup, resourceImagesGroup2);
        swapResourceStage.setScene(swapResourceScene);
        swapResourceStage.show();
    }

    // Author: Saquib Khan
    public class CurrentResourceState extends GridPane {
        // All the resources possible in a list.
        ArrayList<ResourceImage> resourceArrayList = new ArrayList<>();
        // All the resources obtained from a die roll, including duplicates.
        ArrayList<ResourceImage> resourceImageArrayList = new ArrayList<>();
        // Index counter
        int index = 0;

        /**
         * Constructs the grid that the resource images of the resources in current resource_state of the player will
         * be displayed in and adds the images.
         *
         * @Author Saquib Khan
         * @param resource_state the current resource state that is going to be displayed.
         */
        public CurrentResourceState(int[] resource_state) {
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

    /**
     * Current resource state shows the visuals of the current resource state.
     * Used to change resource visuals when dice roll, build, swap or trade takes place.
     *
     * @param resource_state the current resource state that is going to be displayed.
     */
    public void currentResourceState(int[] resource_state) {
        resourceStateGroup.getChildren().clear();
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
        resourceStateGroup.getChildren().addAll(r, line, new CurrentResourceState(catanPlayer.resource_state));
        resourceStateGroup.setLayoutY(250);
        resourceStateGroup.setLayoutX(900);
    }

    // Author: Saquib Khan
    static class SidePanel extends Rectangle {
        double height;
        double width;
        /**
         * Constructs a side panel with rectangle for visuals
         * @Author Saquib Khan
         */
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
    // Author: Saquib Khan
    class Score extends Text {
        // the score being displayed
        int score;

        /**
         * Constructs a score text according to a given score.
         *
         * @Author Saquib Khan
         * @param x x-cooridnate position
         * @param y y-coordinate postion
         * @param score the score value being represented by the score text.
         */
        Score(double x, double y, int score) {
            setText(score + "");
            setFont(Font.font("cambria", FontWeight.BOLD, FontPosture.REGULAR, 20));
            setFill(Color.DARKGREEN);
            setTranslateX(x);
            setTranslateY(y);
            toFront();
        }
        public void setScore(int score) {
            this.score = score;
        }
    }
    /**
     * makes all the scores a player can get for a game of Catan Dice and
     * positions them on the score board pane.
     */
    private void makeScores() {
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

    /**
     * Makes two green side panels containing two images for visuals
     */
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

    /**
     * Makes a score board with a image for visuals.
     */
    private void makeScoreBoard() {
        Image scoreBoardImage = new Image("comp1110/ass2/assets/CatanScoreBoard.JPG");
        ImageView scoreBoardView = new ImageView();

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

    // Author: Saquib Khan
    public class Warning extends Text {
        /**
         * Constructs a warning text according to a given warning.
         *
         * @Author Saquib Khan
         * @param warning A string of text representing the warning.
         */
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

    /**
     * Creates the visuals for the clickable buttons of catan dice
     */
    private void createGameButtons() {
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

    /**
     * Creates the buttons to switch between player boards
     */
    private void createChooseBoard() {
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
    public void createInstructionsButton() {
        GameButtonsBoard insturctionsButton = new GameButtonsBoard("INSTRUCTIONS");
        ChooseBoardBox vbox = new ChooseBoardBox(
                insturctionsButton
        );
        vbox.setTranslateX(-3);
        vbox.setTranslateY(630);
        instructions.getChildren().add(vbox);
    }
    // Author: Saquib Khan, Heavily influenced by third party.
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
}
