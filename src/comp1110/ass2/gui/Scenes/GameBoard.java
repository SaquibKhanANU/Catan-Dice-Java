package comp1110.ass2.gui.Scenes;

import comp1110.ass2.CatanDice;
import comp1110.ass2.CatanEnum.ActionType;
import comp1110.ass2.CatanEnum.ResourceType;
import comp1110.ass2.CatanEnum.StructureType;
import comp1110.ass2.CatanGame.CatanPlayer;
import comp1110.ass2.CatanStructure.BoardStateTree;
import comp1110.ass2.CatanStructure.BuildableStructure;
import comp1110.ass2.CatanStructure.Structure;
import comp1110.ass2.gui.Controls.GameControls;
import comp1110.ass2.gui.Game;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public class GameBoard extends Pane {
    public static final double BOARD_WIDTH = 618;
    public static final double BOARD_HEIGHT = 670;
    public static final double BOARD_offsetX = (Game.WINDOW_WIDTH * 0.45);
    public static final double BOARD_offsetY = (Game.WINDOW_HEIGHT * 0.5);
    public static final double BLOCKS_OFFSETY = 620;
    public static final double BLOCKS_OFFSETX = 960;
    public static final double hexagonRadius = 135;
    Group hexagonBoard = new Group();
    Group roads = new Group();
    Group cities = new Group();
    Group settlements = new Group();
    Group knights = new Group();
    Group blocks = new Group();
    Group resources = new Group();
    Group structuresBoard = new Group(roads, cities, settlements, knights);
    Group scoreCounter = new Group();
    public GameControls gameControls;
    public BoardStateTree boardStateTree;

    /**
     * Constructs a game board pane based on a player, makes all the properties of a game board including
     * game controls.
     * @Author Saquib Khan
     * @param catanPlayer which player the game board belongs to.
     */
    public GameBoard(CatanPlayer catanPlayer) {
        gameControls = new GameControls(catanPlayer);
        setBackground(new Background(new BackgroundFill(Color.web("#3399ff"), CornerRadii.EMPTY, Insets.EMPTY)));
        this.gameControls.catanPlayer = catanPlayer;
        gameControls.catanPlayer.currentTurn = gameControls.catanPlayer == Game.playerOne;
        makeBoard();
        makeStructures();
        makeBlocks();
        gameControls.swapResourceStage.initModality(Modality.APPLICATION_MODAL);
        blocks.toFront();
        getChildren().addAll(hexagonBoard, structuresBoard, resources, scoreCounter, gameControls.allControls, blocks);
    }

    /**
     * Used only for Task 5 viewer, does same thing as first constructor but removes visuals not required.
     * @Author Saquib Khan
     */
    public GameBoard() {
        setBackground(new Background(new BackgroundFill(Color.web("#3399ff"), CornerRadii.EMPTY, Insets.EMPTY)));
        this.gameControls = new GameControls(new CatanPlayer(1));
        makeBoard();
        makeStructures();
        getChildren().addAll(hexagonBoard, structuresBoard);
    }

    static class Hexagon extends Polygon {
        private final double radius;
        private final double radianStep = (2 * Math.PI) / 6;
        private final double offsetY;
        private final double offsetX;

        /**
         * The hexagon shape for the board layout
         * @Author Saquib Khan
         * @param radius radius of the hexagon
         * @param color color of the hexagon
         */
        Hexagon(double radius, Paint color) {
            this.radius = radius;
            setFill(color);
            setStroke(Color.SANDYBROWN);
            setEffect(new DropShadow(40, Color.SADDLEBROWN));
            setStrokeWidth(1);
            setStrokeType(StrokeType.INSIDE);
            offsetY = calculateApothem() * 0.92;
            offsetX = radius * 1.35;

            for (int i = 0; i < 6; i++) {
                double angle = radianStep * i;
                getPoints().add(Math.cos(angle) * radius / 1.1);
                getPoints().add(Math.sin(angle) * radius / 1.1);
            }
        }

        public double getOffsetY() {
            return offsetY;
        }

        public double getOffsetX() {
            return offsetX;
        }

        private double calculateApothem() {
            return (Math.tan(radianStep) * radius) / 2;
        }
    }
    class ImageHexagon extends ImageView {
        private ImageHexagon(String name, double x, double y) {
            Image c = new Image("comp1110/ass2/assets/HexagonImages/" + name + ".png");
            if (name.equals("AnyHex")) {
                setFitHeight(220);
                setFitWidth(240);
            } else {
                setFitHeight(300);
                setFitWidth(300);
            }

            setTranslateX(x);
            setTranslateY(y);
            setImage(c);
        }
    }
    // Author: Saquib Khan
    class RoadShape extends Rectangle {
        double mouseX, mouseY;

        /**
         * Constructs the road shape for the roaed structure.
         * @Author Saquib khan
         * @param roadX x coordintae on a pane
         * @param roadY y coordiante on a pane
         * @param rotation rotation of the shape
         * @param structureBlock if road shape is a structure block or not
         */
        RoadShape(double roadX, double roadY, double rotation, StructureBlock structureBlock) {
            setX(roadX);
            setY(roadY);
            setWidth(20);
            setHeight(60);
            Color fillColor;
            Color strokeColor;
            int strokeWidth;
            if (structureBlock == null) {
                fillColor = Color.WHITE;
            } else {
                fillColor = Color.SADDLEBROWN;
            }
            strokeWidth = 2;
            strokeColor = Color.BLACK;
            setFill(fillColor);
            setStroke(strokeColor);
            setRotate(rotation);
            setStrokeWidth(strokeWidth);
            if (structureBlock instanceof DraggableStructureBlock draggableStructureBlock) {
                this.setOnMouseEntered(event -> {
                    if (gameControls.catanPlayer.currentTurn && draggableStructureBlock.structure.getRemovable()) {
                        gameControls.warningTextGroup.getChildren().clear();
                        setFill(Color.TAN);
                        setEffect(new DropShadow(10, Color.SADDLEBROWN));
                    }
                });
                this.setOnMouseExited(event -> {
                    setFill(Color.SADDLEBROWN);
                    setEffect(new DropShadow(10, Color.SADDLEBROWN));
                });

                this.setOnMousePressed(event -> {
                    if (gameControls.diceRolled) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            this.mouseX = event.getSceneX();
                            this.mouseY = event.getSceneY();
                            if (draggableStructureBlock.structure.isBuilt()) {
                                new Warning("RIGHT CLICK TO REMOVE");
                            }
                        } else if (event.getButton() == MouseButton.SECONDARY) {
                            if (gameControls.catanPlayer.currentTurn) {
                                if (draggableStructureBlock.structure.getRemovable()) {
                                    if (draggableStructureBlock.structure.isBuilt()) {
                                        boardStateTree = new BoardStateTree(gameControls.catanPlayer.board_state);
                                        // Update the board state for the remove
                                        if (boardStateTree.canRemove(draggableStructureBlock.getStructureId())) {
                                            draggableStructureBlock.snapToHome();
                                            // Remove was possible so update the board_state
                                            // gameControls.catanPlayer.board_state = boardStateTree.board_state;
                                            draggableStructureBlock.removePoint();
                                            draggableStructureBlock.removeBoardState();
                                            gameControls.catanBoard.removeStructureBlock(draggableStructureBlock.structure);
                                            gameControls.catanPlayer.structures.remove(draggableStructureBlock.structure);
                                            gameControls.catanPlayer.structuresForRound.remove(draggableStructureBlock.structure);
                                            draggableStructureBlock.increaseResourceState();
                                            gameControls.currentResourceState(gameControls.catanPlayer.resource_state);
                                            blocks.getChildren().remove(this);
                                        } else {
                                            new Warning("CANNOT REMOVE THIS STRUCTURE GIVEN CURRENT BOARD STATE");
                                        }
                                    }  else{
                                        new Warning("STRUCTURE IS NOT BUILT");
                                    }
                                } else {
                                    gameControls.warningTextGroup.getChildren().clear();
                                    new Warning("NOT REMOVABLE");
                                }
                            } else {
                                new Warning("NOT YOUR TURN");
                            }
                            event.consume();
                        }
                    } else {
                        new Warning("ROLL THE DICE");
                    }
                });

                this.setOnMouseDragged(event -> {
                    if (gameControls.diceRolled) {
                        if (!draggableStructureBlock.structure.isBuilt()) {
                            draggableStructureBlock.toFront();
                            double movementX = event.getSceneX() - this.mouseX;
                            double movementY = event.getSceneY() - this.mouseY;
                            draggableStructureBlock.drag(movementX, movementY);
                            this.mouseX = event.getSceneX();
                            this.mouseY = event.getSceneY();
                        }
                    }
                });

                this.setOnMouseReleased(event -> {
                    if (gameControls.diceRolled) {
                        if (!draggableStructureBlock.structure.isBuilt()) {
                            if (draggableStructureBlock.isOnBoard()) {
                                draggableStructureBlock.setPosition();
                                try {
                                    if (gameControls.catanBoard.isStructurePlacementValid(draggableStructureBlock.structure)) {
                                        if (gameControls.catanPlayer.currentTurn) {
                                            draggableStructureBlock.updateAction();
                                            if (CatanDice.canDoAction(gameControls.catanPlayer.action, gameControls.catanPlayer.board_state, gameControls.catanPlayer.resource_state)) { // GET ACTION, GET BOARD_STATE, GET RESOURCE.
                                                draggableStructureBlock.updateBoardState();
                                                draggableStructureBlock.snapToGrid();
                                                draggableStructureBlock.accessPoints();
                                                gameControls.catanBoard.placeStructureBlock(draggableStructureBlock.structure);
                                                gameControls.catanPlayer.structures.add(draggableStructureBlock.structure);
                                                gameControls.catanPlayer.structuresForRound.add(draggableStructureBlock.structure);
                                                draggableStructureBlock.decreaseResourceState();
                                                gameControls.currentResourceState(gameControls.catanPlayer.resource_state);
                                                draggableStructureBlock.newBlock();
                                            } else {
                                                new Warning("NOT ENOUGH RESOURCE/NOT ENOUGH STRUCTURES");
                                                draggableStructureBlock.snapToHome();
                                            }
                                        } else {
                                            new Warning("NOT YOUR TURN");
                                            draggableStructureBlock.snapToHome();
                                        }
                                    } else {
                                        new Warning("INVALID STRUCTURE PLACEMENT");
                                        draggableStructureBlock.snapToHome();
                                    }
                                } catch (NullPointerException e) {
                                    new Warning("INVALID STRUCTURE PLACEMENT");
                                    draggableStructureBlock.snapToHome();
                                }
                            } else {
                                draggableStructureBlock.snapToHome();
                            }
                        }
                    }
                });
            }
        }
    }
    // Author: Saquib Khan
    class SettlementShape extends Polygon {
        double mouseX, mouseY;
        /**
         * Constructs the settlement shape for the road structure.
         * @Author Saquib Khan
         * @param x x coordintae on a pane
         * @param y y coordiante on a pane
         * @param structureBlock if settlement shape is a structure block or not
         */
        SettlementShape(double x, double y, StructureBlock structureBlock) {
            getPoints().addAll(
                    -20.0, 25.0,
                    -20.0, 0.0,
                    -22.0, 0.0,
                    -5.0, -10.0,
                    12.0, 0.0,
                    10.0, 0.0,
                    10.0, 25.0);
            setLayoutX(x);
            setLayoutY(y);
            Color fillColor;
            Color strokeColor;
            if (structureBlock == null) {
                fillColor = Color.WHITE;
            } else {
                fillColor = Color.SADDLEBROWN;
            }
            strokeColor = Color.BLACK;
            setFill(fillColor);
            setStroke(strokeColor);
            setStrokeWidth(2);
            if (structureBlock instanceof DraggableStructureBlock draggableStructureBlock) {
                this.setOnMouseEntered(event -> {
                    if (gameControls.catanPlayer.currentTurn && draggableStructureBlock.structure.getRemovable()) {
                        gameControls.warningTextGroup.getChildren().clear();
                        setFill(Color.TAN);
                        setEffect(new DropShadow(10, Color.SADDLEBROWN));
                    }
                });
                this.setOnMouseExited(event -> {
                    setFill(Color.SADDLEBROWN);
                    setEffect(new DropShadow(10, Color.SADDLEBROWN));
                });

                this.setOnMousePressed(event -> {
                    if (gameControls.diceRolled) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            this.mouseX = event.getSceneX();
                            this.mouseY = event.getSceneY();
                            if (draggableStructureBlock.structure.isBuilt()) {
                                new Warning("RIGHT CLICK TO REMOVE");
                            }
                        } else if (event.getButton() == MouseButton.SECONDARY) {
                            if (gameControls.catanPlayer.currentTurn) {
                                if (draggableStructureBlock.structure.getRemovable()) {
                                    if (draggableStructureBlock.structure.isBuilt()) {
                                        boardStateTree = new BoardStateTree(gameControls.catanPlayer.board_state);
                                        // Update the board state for the remove
                                        if (boardStateTree.canRemove(draggableStructureBlock.getStructureId())) {
                                            draggableStructureBlock.snapToHome();
                                            // Remove was possible so update the board_state
                                            // gameControls.catanPlayer.board_state = boardStateTree.board_state;
                                            draggableStructureBlock.removePoint();
                                            draggableStructureBlock.removeBoardState();
                                            gameControls.catanBoard.removeStructureBlock(draggableStructureBlock.structure);
                                            gameControls.catanPlayer.structures.remove(draggableStructureBlock.structure);
                                            gameControls.catanPlayer.structuresForRound.remove(draggableStructureBlock.structure);
                                            draggableStructureBlock.increaseResourceState();
                                            gameControls.currentResourceState(gameControls.catanPlayer.resource_state);
                                            blocks.getChildren().remove(this);
                                        } else {
                                            new Warning("CANNOT REMOVE THIS STRUCTURE GIVEN CURRENT BOARD STATE");
                                        }
                                    }  else{
                                        new Warning("STRUCTURE IS NOT BUILT");
                                    }
                                } else {
                                    gameControls.warningTextGroup.getChildren().clear();
                                    new Warning("NOT REMOVABLE");
                                }
                            } else {
                                new Warning("NOT YOUR TURN");
                            }
                            event.consume();
                        }
                    } else {
                        new Warning("ROLL THE DICE");
                    }
                });

                this.setOnMouseDragged(event -> {
                    if (gameControls.diceRolled) {
                        if (!draggableStructureBlock.structure.isBuilt()) {
                            draggableStructureBlock.toFront();
                            double movementX = event.getSceneX() - this.mouseX;
                            double movementY = event.getSceneY() - this.mouseY;
                            draggableStructureBlock.drag(movementX, movementY);
                            this.mouseX = event.getSceneX();
                            this.mouseY = event.getSceneY();
                        }
                    }
                });

                this.setOnMouseReleased(event -> {
                    if (gameControls.diceRolled) {
                        if (!draggableStructureBlock.structure.isBuilt()) {
                            if (draggableStructureBlock.isOnBoard()) {
                                draggableStructureBlock.setPosition();
                                try {
                                    if (gameControls.catanBoard.isStructurePlacementValid(draggableStructureBlock.structure)) {
                                        if (gameControls.catanPlayer.currentTurn) {
                                            draggableStructureBlock.updateAction();
                                            if (CatanDice.canDoAction(gameControls.catanPlayer.action, gameControls.catanPlayer.board_state, gameControls.catanPlayer.resource_state)) { // GET ACTION, GET BOARD_STATE, GET RESOURCE.
                                                draggableStructureBlock.updateBoardState();
                                                draggableStructureBlock.snapToGrid();
                                                draggableStructureBlock.accessPoints();
                                                gameControls.catanBoard.placeStructureBlock(draggableStructureBlock.structure);
                                                gameControls.catanPlayer.structures.add(draggableStructureBlock.structure);
                                                gameControls.catanPlayer.structuresForRound.add(draggableStructureBlock.structure);
                                                draggableStructureBlock.decreaseResourceState();
                                                gameControls.currentResourceState(gameControls.catanPlayer.resource_state);
                                                draggableStructureBlock.newBlock();
                                            } else {
                                                new Warning("NOT ENOUGH RESOURCE/NOT ENOUGH STRUCTURES");
                                                draggableStructureBlock.snapToHome();
                                            }
                                        } else {
                                            new Warning("NOT YOUR TURN");
                                            draggableStructureBlock.snapToHome();
                                        }
                                    } else {
                                        new Warning("INVALID STRUCTURE PLACEMENT");
                                        draggableStructureBlock.snapToHome();
                                    }
                                } catch (NullPointerException e) {
                                    new Warning("INVALID STRUCTURE PLACEMENT");
                                    draggableStructureBlock.snapToHome();
                                }
                            } else {
                                draggableStructureBlock.snapToHome();
                            }
                        }
                    }
                });
            }
        }
    }

    // Author: Saquib Khan
    public class KnightShape extends Circle {
        double mouseX, mouseY;
        // if the knight is currently swappable or not
        public static boolean swappable;
        /**
         * Constructs the knight shape for the knight structure.
         * @Author Saquib Khan
         * @param x x coordintae on a pane
         * @param y y coordiante on a pane
         * @param structureBlock if knight shape is a structure block or not
         */
        KnightShape(double x, double y, StructureBlock structureBlock) {
            swappable = false;
            Circle c = new Circle();
            c.setRadius(15);
            c.toFront();
            c.setCenterX(x);
            c.setCenterY(y - 25);
            setCenterX(x);
            setCenterY(y);
            setRadius(25);
            Color fillColor;
            Color strokeColor;
            if (structureBlock == null) {
                knights.getChildren().add(c);
                fillColor = Color.WHITE;
            } else {
                fillColor = Color.SADDLEBROWN;
            }
            strokeColor = Color.BLACK;
            setFill(fillColor);
            setStroke(strokeColor);
            setStrokeWidth(2);
            if (structureBlock instanceof DraggableStructureBlock draggableStructureBlock) {
                this.setOnMouseEntered(event -> {
                    gameControls.warningTextGroup.getChildren().clear();
                    if (gameControls.catanPlayer.currentTurn && draggableStructureBlock.structure.getRemovable() &&
                            draggableStructureBlock.structure.getBuildableStructure().getStructureType() != StructureType.USED) {
                        setFill(Color.TAN);
                        setEffect(new DropShadow(10, Color.SADDLEBROWN));
                    }
                });
                this.setOnMouseExited(event -> {
                    setFill(Color.SADDLEBROWN);
                    setEffect(new DropShadow(10, Color.SADDLEBROWN));
                });

                this.setOnMousePressed(event -> {
                    if (gameControls.diceRolled) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            if (swappable && draggableStructureBlock.isOnBoard() && !draggableStructureBlock.structure.isUsed()) {
                                if (gameControls.swapResourceStage.isShowing()) {
                                    gameControls.swapResourceStage.toFront();
                                } else {
                                    if (draggableStructureBlock.getStructureId().equals("J6")) {
                                        gameControls.WildCardPopUp(ActionType.SWAP);
                                        setOpacity(1);
                                        draggableStructureBlock.setUsed();
                                        gameControls.swapResourceStage.setOnCloseRequest(e2 -> {
                                            gameControls.swapResourceStage.close();
                                            draggableStructureBlock.setUnused();
                                            gameControls.action.setActionType(ActionType.NONE);
                                            setOpacity(0.3);
                                            if (gameControls.clickedAlready.size() == 1) {
                                                switch (gameControls.clickedAlready.get(0).getResourceType()) {
                                                    case ORE -> gameControls.catanPlayer.changeResourceState(0, 1);
                                                    case GRAIN -> gameControls.catanPlayer.changeResourceState(1, 1);
                                                    case WOOL -> gameControls.catanPlayer.changeResourceState(2, 1);
                                                    case TIMBER -> gameControls.catanPlayer.changeResourceState(3, 1);
                                                    case BRICKS -> gameControls.catanPlayer.changeResourceState(4, 1);
                                                    case GOLD -> gameControls.catanPlayer.changeResourceState(5, 1);
                                                }
                                                gameControls.clickedAlready.clear();
                                            }
                                        });
                                    } else {
                                        gameControls.swapAndTradePopUp(draggableStructureBlock.getStructureId(), ActionType.SWAP);
                                        setOpacity(1);
                                        draggableStructureBlock.setUsed();
                                        gameControls.swapResourceStage.setOnCloseRequest(
                                                e -> {
                                                    e.consume();
                                                    gameControls.swapAndTradePopUp(draggableStructureBlock.getStructureId(), ActionType.UNDO_SWAP);
                                                    gameControls.swapResourceStage.close();
                                                    draggableStructureBlock.setUnused();
                                                    gameControls.action.setActionType(ActionType.NONE);
                                                    setOpacity(0.3);
                                                });
                                    }
                                }

                            } else {
                                this.mouseX = event.getSceneX();
                                this.mouseY = event.getSceneY();
                                if (draggableStructureBlock.structure.isBuilt()) {
                                    gameControls.warningTextGroup.getChildren().clear();
                                    new Warning("RIGHT CLICK TO REMOVE");
                                }
                            }
                        } else if (event.getButton() == MouseButton.SECONDARY) {
                            if (gameControls.catanPlayer.currentTurn) {
                                if (!swappable && draggableStructureBlock.structure.getRemovable() && draggableStructureBlock.structure.getBuildableStructure().getStructureType() == StructureType.KNIGHT) {
                                    if (draggableStructureBlock.structure.isBuilt()) {
                                        boardStateTree = new BoardStateTree(gameControls.catanPlayer.board_state);
                                        if (boardStateTree.canRemove(draggableStructureBlock.getStructureId())) {
                                            draggableStructureBlock.snapToHome();
                                            draggableStructureBlock.removePoint();
                                            draggableStructureBlock.removeBoardState();
                                            gameControls.catanBoard.removeStructureBlock(draggableStructureBlock.structure);
                                            gameControls.catanPlayer.structures.remove(draggableStructureBlock.structure);
                                            gameControls.catanPlayer.structuresForRound.remove(draggableStructureBlock.structure);
                                            draggableStructureBlock.increaseResourceState();
                                            gameControls.currentResourceState(gameControls.catanPlayer.resource_state);
                                            blocks.getChildren().remove(this);
                                        } else {
                                            new Warning("CANNOT REMOVE DUE TO CURRENT BOARD STATE");
                                        }
                                    }
                                } else {
                                    new Warning("NOT REMOVABLE");
                                }
                            } else {
                                new Warning("NOT YOUR TURN");
                            }
                            event.consume();
                        }
                    } else {
                        new Warning("ROLL THE DICE");
                    }
                });

                this.setOnMouseDragged(event -> {
                    if (gameControls.diceRolled) {
                        if (!draggableStructureBlock.structure.isBuilt()) {
                            draggableStructureBlock.toFront();
                            double movementX = event.getSceneX() - this.mouseX;
                            double movementY = event.getSceneY() - this.mouseY;
                            draggableStructureBlock.drag(movementX, movementY);
                            this.mouseX = event.getSceneX();
                            this.mouseY = event.getSceneY();
                        }
                    }
                });

                this.setOnMouseReleased(event -> {
                    if (gameControls.diceRolled) {
                        if (!draggableStructureBlock.structure.isBuilt()) {
                            if (draggableStructureBlock.isOnBoard()) {
                                draggableStructureBlock.setPosition();
                                try {
                                    if (gameControls.catanBoard.isStructurePlacementValid(draggableStructureBlock.structure)) {
                                        if (gameControls.catanPlayer.currentTurn) {
                                            draggableStructureBlock.updateAction();
                                            if (CatanDice.canDoAction(gameControls.catanPlayer.action, gameControls.catanPlayer.board_state, gameControls.catanPlayer.resource_state)) { // GET ACTION, GET BOARD_STATE, GET RESOURCE.
                                                draggableStructureBlock.updateBoardState();
                                                setOpacity(0.3);
                                                draggableStructureBlock.snapToGrid();
                                                draggableStructureBlock.accessPoints();
                                                gameControls.catanBoard.placeStructureBlock(draggableStructureBlock.structure);
                                                gameControls.catanPlayer.structures.add(draggableStructureBlock.structure);
                                                gameControls.catanPlayer.structuresForRound.add(draggableStructureBlock.structure);
                                                draggableStructureBlock.decreaseResourceState();
                                                gameControls.currentResourceState(gameControls.catanPlayer.resource_state);
                                                draggableStructureBlock.newBlock();
                                            } else {
                                                new Warning("NOT ENOUGH RESOURCE/NOT ENOUGH STRUCTURES");
                                                draggableStructureBlock.snapToHome();
                                            }
                                        } else {
                                            new Warning("NOT YOUR TURN");
                                            draggableStructureBlock.snapToHome();
                                        }
                                    } else {
                                        new Warning("INVALID STRUCTURE PLACEMENT");
                                        draggableStructureBlock.snapToHome();
                                    }
                                } catch (NullPointerException e) {
                                    new Warning("INVALID STRUCTURE PLACEMENT");
                                    draggableStructureBlock.snapToHome();
                                }
                            } else {
                                draggableStructureBlock.snapToHome();
                            }
                        }
                    } else {
                        new Warning("ROLL DICE FIRST");
                    }
                });
            }
        }
        public void setSwappable(boolean swappable) {
            KnightShape.swappable = swappable;
        }
    }

    // Author: Saquib Khan
    class CityShape extends Polygon {
        double mouseX, mouseY;
        /**
         * Constructs the city shape for the city structure.
         * @Author Saquib Khan
         * @param x x coordintae on a pane
         * @param y y coordiante on a pane
         * @param structureBlock if city shape is a structure block or not
         */
        CityShape(double x, double y, StructureBlock structureBlock) {
            getPoints().addAll(
                    -35.0, 15.0,
                    -35.0, -5.0,
                    -12.0, -5.0,
                    -12.0, -10.0,
                    -14.0, -10.0,
                    0.0, -30.0,
                    12.0, -10.0,
                    10.0, -10.0,
                    10.0, -10.0,
                    10.0, -5.0,
                    10.0, -5.0,
                    10.0, 15.0);
            setLayoutX(x);
            setLayoutY(y);
            Color fillColor;
            Color strokeColor;
            if (structureBlock == null) {
                fillColor = Color.WHITE;
            } else {
                fillColor = Color.SADDLEBROWN;
            }
            strokeColor = Color.BLACK;
            setFill(fillColor);
            setStroke(strokeColor);
            setStrokeWidth(2);
            if (structureBlock instanceof DraggableStructureBlock draggableStructureBlock) {
                this.setOnMouseEntered(event -> {
                    if (gameControls.catanPlayer.currentTurn && draggableStructureBlock.structure.getRemovable()) {
                        gameControls.warningTextGroup.getChildren().clear();
                        setFill(Color.TAN);
                        setEffect(new DropShadow(10, Color.SADDLEBROWN));
                    }
                });
                this.setOnMouseExited(event -> {
                    setFill(Color.SADDLEBROWN);
                    setEffect(new DropShadow(10, Color.SADDLEBROWN));
                });

                this.setOnMousePressed(event -> {
                    if (gameControls.diceRolled) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            this.mouseX = event.getSceneX();
                            this.mouseY = event.getSceneY();
                            if (draggableStructureBlock.structure.isBuilt()) {
                                new Warning("RIGHT CLICK TO REMOVE");
                            }
                        } else if (event.getButton() == MouseButton.SECONDARY) {
                            if (gameControls.catanPlayer.currentTurn) {
                                if (draggableStructureBlock.structure.getRemovable()) {
                                    if (draggableStructureBlock.structure.isBuilt()) {
                                        boardStateTree = new BoardStateTree(gameControls.catanPlayer.board_state);
                                        // Update the board state for the remove;
                                        if (boardStateTree.canRemove(draggableStructureBlock.getStructureId())) {
                                            draggableStructureBlock.snapToHome();
                                            // Remove was possible so update the board_state
                                            // gameControls.catanPlayer.board_state = boardStateTree.board_state;
                                            draggableStructureBlock.removePoint();
                                            draggableStructureBlock.removeBoardState();
                                            gameControls.catanBoard.removeStructureBlock(draggableStructureBlock.structure);
                                            gameControls.catanPlayer.structures.remove(draggableStructureBlock.structure);
                                            gameControls.catanPlayer.structuresForRound.remove(draggableStructureBlock.structure);
                                            draggableStructureBlock.increaseResourceState();
                                            gameControls.currentResourceState(gameControls.catanPlayer.resource_state);
                                            blocks.getChildren().remove(this);
                                        } else {
                                            new Warning("CANNOT REMOVE THIS STRUCTURE GIVEN CURRENT BOARD STATE");
                                        }
                                    }  else{
                                        new Warning("STRUCTURE IS NOT BUILT");
                                    }
                                } else {
                                    gameControls.warningTextGroup.getChildren().clear();
                                    new Warning("NOT REMOVABLE");
                                }
                            } else {
                                new Warning("NOT YOUR TURN");
                            }
                            event.consume();
                        }
                    } else {
                        new Warning("ROLL THE DICE");
                    }
                });

                this.setOnMouseDragged(event -> {
                    if (gameControls.diceRolled) {
                        if (!draggableStructureBlock.structure.isBuilt()) {
                            draggableStructureBlock.toFront();
                            double movementX = event.getSceneX() - this.mouseX;
                            double movementY = event.getSceneY() - this.mouseY;
                            draggableStructureBlock.drag(movementX, movementY);
                            this.mouseX = event.getSceneX();
                            this.mouseY = event.getSceneY();
                        }
                    }
                });

                this.setOnMouseReleased(event -> {
                    if (gameControls.diceRolled) {
                        if (!draggableStructureBlock.structure.isBuilt()) {
                            if (draggableStructureBlock.isOnBoard()) {
                                draggableStructureBlock.setPosition();
                                try {
                                    if (gameControls.catanBoard.isStructurePlacementValid(draggableStructureBlock.structure)) {
                                        if (gameControls.catanPlayer.currentTurn) {
                                            draggableStructureBlock.updateAction();
                                            if (CatanDice.canDoAction(gameControls.catanPlayer.action, gameControls.catanPlayer.board_state, gameControls.catanPlayer.resource_state)) { // GET ACTION, GET BOARD_STATE, GET RESOURCE.
                                                draggableStructureBlock.updateBoardState();
                                                draggableStructureBlock.snapToGrid();
                                                draggableStructureBlock.accessPoints();
                                                gameControls.catanBoard.placeStructureBlock(draggableStructureBlock.structure);
                                                gameControls.catanPlayer.structures.add(draggableStructureBlock.structure);
                                                gameControls.catanPlayer.structuresForRound.add(draggableStructureBlock.structure);
                                                draggableStructureBlock.decreaseResourceState();
                                                gameControls.currentResourceState(gameControls.catanPlayer.resource_state);
                                                draggableStructureBlock.newBlock();
                                            } else {
                                                new Warning("NOT ENOUGH RESOURCE/NOT ENOUGH STRUCTURES");
                                                draggableStructureBlock.snapToHome();
                                            }
                                        } else {
                                            new Warning("NOT YOUR TURN");
                                            draggableStructureBlock.snapToHome();
                                        }
                                    } else {
                                        new Warning("INVALID STRUCTURE PLACEMENT");
                                        draggableStructureBlock.snapToHome();
                                    }
                                } catch (NullPointerException e) {
                                    new Warning("INVALID STRUCTURE PLACEMENT");
                                    draggableStructureBlock.snapToHome();
                                }
                            } else {
                                draggableStructureBlock.snapToHome();
                            }
                        }
                    }
                });
            }
        }
    }
    // Author: Saquib Khan
    class StructureBlock extends Group {
        final Structure structure;
        int x;
        int y;
        RoadShape roadShape;
        CityShape cityShape;
        KnightShape knightShape;
        SettlementShape settlementShape;
        Text pointCounter = new Text();

        /**
         * Constructs a structure block which makes the structure shapes have extra properties required
         * for it to be buildable.
         * @Author Saquib khan
         * @param id String id of structure
         */
        StructureBlock(String id) {
            this.structure = gameControls.catanBoard.getStructureBlocks().get(id);
            int x = this.structure.getBuildableStructure().getX();
            int y = this.structure.getBuildableStructure().getY();
            this.roadShape = new RoadShape(x, y, 90, this);
            this.cityShape = new CityShape(x, y, this);
            this.knightShape = new KnightShape(x, y, this);
            this.settlementShape = new SettlementShape(x, y, this);
            switch (structure.getId()) {
                case "R" -> blocks.getChildren().add(roadShape);
                case "C" -> blocks.getChildren().add(cityShape);
                case "K" -> blocks.getChildren().add(knightShape);
                case "S" -> blocks.getChildren().add(settlementShape);
            }
        }

        /**
         * Snaps the structure block to the board.
         */
        protected void snapToGrid() {
            switch (this.structure.getId()) {
                case "R" -> {
                    this.setLayoutX(220 + (this.x * (BOARD_WIDTH / 20)));
                    this.setLayoutY((BOARD_offsetY - (hexagonRadius * 2.1 + 116.91 - 30)) + ((this.y * (BOARD_HEIGHT / 12))));
                    updateRotation();
                    roadShape.setTranslateX(this.getLayoutX());
                    roadShape.setTranslateY(this.getLayoutY());
                }
                case "C" -> {
                    this.setLayoutX(225 + (this.x * (BOARD_WIDTH / 20)));
                    this.setLayoutY((BOARD_offsetY - (hexagonRadius * 2.1 + 116.91 - 60)) + ((this.y * (BOARD_HEIGHT / 12))));
                    cityShape.setTranslateX(this.getLayoutX());
                    cityShape.setTranslateY(this.getLayoutY());
                }
                case "K" -> {
                    this.setLayoutX(230 + (this.x * (BOARD_WIDTH / 20)));
                    this.setLayoutY((BOARD_offsetY - (hexagonRadius * 2.1 + 116.91 - 60)) + ((this.y * (BOARD_HEIGHT / 12))));
                    knightShape.setTranslateX(this.getLayoutX());
                    knightShape.setTranslateY(this.getLayoutY());
                }
                case "S" -> {
                    this.setLayoutX(220 + 10 + (this.x * (BOARD_WIDTH / 20)));
                    this.setLayoutY((BOARD_offsetY - (hexagonRadius * 2.1 + 116.91 - 50)) + ((this.y * (BOARD_HEIGHT / 12))));
                    settlementShape.setTranslateX(this.getLayoutX());
                    settlementShape.setTranslateY(this.getLayoutY());
                }
            }
        }

        /**
         * Decreases resource state depending on structure block shape
         * used when building a structure.
         */
        protected void decreaseResourceState() {
            switch (this.structure.getId()) {
                case "R" -> {
                    gameControls.catanPlayer.resource_state[4]--;
                    gameControls.catanPlayer.resource_state[3]--;
                }
                case "C" -> {
                    gameControls.catanPlayer.resource_state[0] -= 3;
                    gameControls.catanPlayer.resource_state[1] -= 2;
                }
                case "K" -> {
                    gameControls.catanPlayer.resource_state[0]--;
                    gameControls.catanPlayer.resource_state[1]--;
                    gameControls.catanPlayer.resource_state[2]--;
                }
                case "S" -> {
                    gameControls.catanPlayer.resource_state[4]--;
                    gameControls.catanPlayer.resource_state[3]--;
                    gameControls.catanPlayer.resource_state[2]--;
                    gameControls.catanPlayer.resource_state[1]--;
                }
            }
        }

        /**
         * Increases the resource state depending on the structure block shape
         * used when removing a structure
         */
        protected void increaseResourceState() {
            switch (this.structure.getId()) {
                case "R" -> {
                    gameControls.catanPlayer.resource_state[4]++;
                    gameControls.catanPlayer.resource_state[3]++;
                }
                case "C" -> {
                    gameControls.catanPlayer.resource_state[0] += 3;
                    gameControls.catanPlayer.resource_state[1] += 2;
                }
                case "K" -> {
                    gameControls.catanPlayer.resource_state[0]++;
                    gameControls.catanPlayer.resource_state[2]++;
                    gameControls.catanPlayer.resource_state[1]++;
                }
                case "S" -> {
                    gameControls.catanPlayer.resource_state[4]++;
                    gameControls.catanPlayer.resource_state[3]++;
                    gameControls.catanPlayer.resource_state[2]++;
                    gameControls.catanPlayer.resource_state[1]++;
                }
            }
        }

        /**
         * Changes the rotation of the structure block shape
         */
        protected void updateRotation() {
            setRotate(30);
            BuildableStructure test = gameControls.catanBoard.getBuildableStructure(this.x, this.y);
            String[] thirtyDegrees = new String[]{"R0", "R3", "R7", "R9", "R11", "R15", "R13"};
            String[] negThirtyDegrees = new String[]{"R1", "R4", "R6", "R12"};
            if (asList(negThirtyDegrees).contains(test.getId())) {
                this.roadShape.setRotate(90);
            } else if (asList(thirtyDegrees).contains(test.getId())) {
                this.roadShape.setRotate(30);
            } else {
                this.roadShape.setRotate(-30);
            }
        }
        /**
         * updates a point to text that displays current score.
         */
        protected void accessPoints() {
            BuildableStructure buildableStructure = gameControls.catanBoard.getBuildableStructure(this.x, this.y);
            int points = buildableStructure.getPoint();
            gameControls.catanPlayer.score = gameControls.catanPlayer.score + points;
            scoreCounter.getChildren().clear();
            pointCounter.setText("Points for round " + (gameControls.catanPlayer.turn_num + 1) + ": " + gameControls.catanPlayer.score);
            pointCounter.setFill(Color.WHITE);
            pointCounter.setFont(Font.font("times new roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
            pointCounter.setX(250);
            pointCounter.setY(40);
            pointCounter.toFront();
            scoreCounter.toFront();
            scoreCounter.getChildren().add(pointCounter);
        }

        /**
         * Removes a point from text that displays current score.
         */
        protected void removePoint() {
            BuildableStructure buildableStructure = gameControls.catanBoard.getBuildableStructure(this.x, this.y);
            int points = buildableStructure.getPoint();
            gameControls.catanPlayer.score = gameControls.catanPlayer.score - points;
            scoreCounter.getChildren().clear();
            pointCounter.setText("Points for round " + (gameControls.catanPlayer.turn_num + 1) + ": " + gameControls.catanPlayer.score);
            pointCounter.setFill(Color.WHITE);
            pointCounter.setFont(Font.font("times new roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
            pointCounter.setX(250);
            pointCounter.setY(40);
            pointCounter.toFront();
            scoreCounter.toFront();
            scoreCounter.getChildren().add(pointCounter);
        }
        /**
         * Updates the structure blocks id to the board state.
         */
        protected void updateBoardState() {
            if (gameControls.catanPlayer.board_state.equals("")) {
                gameControls.catanPlayer.board_state = gameControls.catanPlayer.board_state + gameControls.catanBoard.getBuildableStructure(this.x, this.y).getId();
            } else {
                gameControls.catanPlayer.board_state = gameControls.catanPlayer.board_state + "," + gameControls.catanBoard.getBuildableStructure(this.x, this.y).getId();
            }
        }

        /**
         * Removes the structure blocks id from the board state.
         */
        // Author: John Larkin
        protected void removeBoardState() {
            String[] board_state = gameControls.catanPlayer.board_state.split(",");
            ArrayList<String> board_state_list = new ArrayList<>();
            Collections.addAll(board_state_list, board_state);
            String res = "";
            board_state_list.remove(gameControls.catanBoard.getBuildableStructure(this.x, this.y).getId());
            if (board_state_list != null) {
                for (int i = 0; i < board_state_list.size(); i++) {
                    if (i == 0) {
                        res = board_state_list.get(i);
                    } else {
                        res += "," + board_state_list.get(i);
                    }
                }

            }
            gameControls.catanPlayer.board_state = res;
        }

        /**
         * Updates the build action with id of the structure block about to be built.
         */
        protected void updateAction() {
            gameControls.catanPlayer.action = "build" + " " + gameControls.catanBoard.getBuildableStructure(this.x, this.y).getId();
        }

        /**
         * Sets the Knight to a used knight.
         */
        protected void setUsed() {
            structure.setUsed(true);
            gameControls.catanBoard.getBuildableStructure(this.x, this.y).setStructureType(StructureType.USED);
            structure.getBuildableStructure().setStructureType(StructureType.USED);
        }

        /**
         * For a Knight, sets the used knight back to Knight (A built joker)
         */
        protected void setUnused() {
            structure.setUsed(false);
            gameControls.catanBoard.getBuildableStructure(this.x, this.y).setStructureType(StructureType.KNIGHT);
            structure.getBuildableStructure().setStructureType(StructureType.KNIGHT);
        }

        /**
         * used to get the id of the structure block
         */
        protected String getStructureId() {
            return gameControls.catanBoard.getBuildableStructure(this.x, this.y).getId();
        }
    }

    // Author: Saquib Khan
    class DraggableStructureBlock extends StructureBlock {
        double homeX, homeY;

        /**
         * Constructs a structure block and giving it all the properties for it to be draggable.
         * @Author Saquib khan
         * @param id String id of the structure block
         */
        DraggableStructureBlock(String id) {
            super(id);

            this.homeX = BLOCKS_OFFSETX;
            this.homeY = BLOCKS_OFFSETY;

            switch (structure.getId()) {
                case "R" -> {
                    this.homeX = BLOCKS_OFFSETX;
                    this.homeY = BLOCKS_OFFSETY;
                }
                case "C" -> {
                    this.homeX = BLOCKS_OFFSETX + 130;
                    this.homeY = BLOCKS_OFFSETY + 27;
                }
                case "K" -> {
                    this.homeX = BLOCKS_OFFSETX + 110 + 70;
                    this.homeY = BLOCKS_OFFSETY + 20;
                }
                case "S" -> {
                    this.homeX = BLOCKS_OFFSETX + 75;
                    this.homeY = BLOCKS_OFFSETY + 17;
                }
            }
            this.snapToHome();
        }

        /**
         * Snaps the draggable structure back to home depending on its shape.
         */
        private void snapToHome() {
            this.setLayoutX(this.homeX);
            this.setLayoutY(this.homeY);
            switch (structure.getId()) {
                case "R" -> {
                    this.roadShape.setRotate(90);
                    this.roadShape.setTranslateX(this.getLayoutX());
                    this.roadShape.setTranslateY(this.getLayoutY());
                }
                case "C" -> {
                    this.cityShape.setTranslateX(this.getLayoutX());
                    this.cityShape.setTranslateY(this.getLayoutY());
                }
                case "K" -> {
                    this.knightShape.setTranslateX(this.getLayoutX());
                    this.knightShape.setTranslateY(this.getLayoutY());
                }
                case "S" -> {
                    this.settlementShape.setTranslateX(this.getLayoutX());
                    this.settlementShape.setTranslateY(this.getLayoutY());
                }
            }
        }

        /**
         * Drag the block a new x and y location depending on its shape
         * @param movementX movement in the x direction
         * @param movementY movement in the y direction
         */
        protected void drag(double movementX, double movementY) {
            this.setLayoutX(this.getLayoutX() + movementX);
            this.setLayoutY(this.getLayoutY() + movementY);
            switch (structure.getId()) {
                case "R" -> {
                    this.roadShape.setTranslateX(this.getLayoutX());
                    this.roadShape.setTranslateY(this.getLayoutY());
                }
                case "C" -> {
                    this.cityShape.setTranslateX(this.getLayoutX());
                    this.cityShape.setTranslateY(this.getLayoutY());
                }
                case "K" -> {
                    this.knightShape.setTranslateX(this.getLayoutX());
                    this.knightShape.setTranslateY(this.getLayoutY());
                }
                case "S" -> {
                    this.settlementShape.setTranslateX(this.getLayoutX());
                    this.settlementShape.setTranslateY(this.getLayoutY());
                }
            }
        }

        /**
         * changes the position of the draggable structure blocked
         */
        private void setPosition() {
            this.x = (int) ((getLayoutX() - (BOARD_offsetX - 330 + 10)) / (630 / 21)); // 439.0,147.0
            this.y = (int) ((getLayoutY() - (BOARD_offsetY - (hexagonRadius * 2.1 + 116.91 - 30))) / (670 / 12));
            this.structure.getBuildableStructure().setX(this.x);
            this.structure.getBuildableStructure().setY(this.y);
            System.out.println(gameControls.catanBoard.getBuildableStructure(this.x, this.y));
            System.out.println(this.x + "," + this.y);
        }

        /**
         * checks if draggableStructureBlock is on the board
         * @return if structure block is on the board
         */
        private boolean isOnBoard() {
            return getLayoutX() > (250) && getLayoutX() < (850) &&
                    getLayoutY() > (50) && getLayoutY() < (700);
        }

        /**
         * creates a new draggable structure block
         */
        protected void newBlock() {
            blocks.getChildren().add(new DraggableStructureBlock(this.structure.getId()));
        }
    }


    // Author: Saquib Khan
    static class KnightImage extends ImageView {
        /**
         * Constructs the resource image for knights and their respective resource
         * @Author Saquib Khan
         * @param resourceType the resource type
         * @param x x position on pane
         * @param y y postion on pane
         */
        KnightImage(ResourceType resourceType, double x, double y) {
            String name;
            switch (resourceType) {
                case ORE -> name = "comp1110/ass2/assets/ResourceImages/Resource1.png";
                case GRAIN -> name = "comp1110/ass2/assets/ResourceImages/Resource2.png";
                case TIMBER -> name = "comp1110/ass2/assets/ResourceImages/Resource4.png";
                case WOOL -> name = "comp1110/ass2/assets/ResourceImages/Resource3.png";
                case BRICKS -> name = "comp1110/ass2/assets/ResourceImages/Resource5.png";
                case ANY -> name = "comp1110/ass2/assets/ResourceImages/Resource7.png";
                default -> throw new IllegalStateException("Unexpected value: " + resourceType);
            }
            Image c = new Image(name);
            setFitHeight(30);
            setFitWidth(30);
            setX(x);
            setY(y);
            setImage(c);
        }
    }
    // Author: Saquib Khan
    class Warning extends Text {
        /**
         * A warning text used to inform the user if a invalid action has taken place
         * @Author Saquib khan
         * @param warning a text of a specific warning
         */
        public Warning(String warning) {
            setText(warning);
            setText(warning);
            toFront();
            setFill(Color.RED);
            setFont(Font.font("times new roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
            setX(430);
            setY(40);
            gameControls.warningTextGroup.getChildren().add(this);
        }
    }

    /**
     * makes all the buildable structures spread out across the board
     */
    private void makeStructures() {
        gameControls.catanBoard.makeMap();
        var keySet = gameControls.catanBoard.getStructureBlocksMap().keySet();
        for (var id : keySet) {
            BuildableStructure structure = gameControls.catanBoard.getStructureBlocksMap().get(id);
            double x = structure.getX() * (BOARD_WIDTH / 20);
            double y = structure.getY() * (BOARD_HEIGHT / 12);
            Label label = new Label();
            label.toFront();
            label.setTextFill(Color.web("#439527"));
            label.setText(id);
            label.setFont(Font.font("times new roman", FontWeight.BOLD, FontPosture.REGULAR, 10));
            if (id.charAt(0) == 'R') {
                label.setLayoutX(x + 3);
                label.setLayoutY(y + 24);
                int rotation;
                String[] thirtyDegrees = new String[]{"R0", "R3", "R7", "R9", "R11", "R15", "R13"};
                String[] ninetyDegrees = new String[]{"R1", "R4", "R6", "R12"};
                if (asList(ninetyDegrees).contains(id)) {
                    rotation = 90;
                } else if (asList(thirtyDegrees).contains(id)) {
                    rotation = 30;
                } else {
                    rotation = -30;
                }
                RoadShape roadShape = new RoadShape(x, y, rotation, null);
                if (id.equals("RI")) {
                    roadShape.setFill(Color.PURPLE);
                }
                roads.getChildren().add(roadShape);
            } else if (id.charAt(0) == 'C') {
                label.setLayoutX(x - 3);
                label.setLayoutY(y + 24);
                CityShape cityShape = new CityShape(x + 5, y + 30, null);
                cities.getChildren().add(cityShape);
            } else if (id.charAt(0) == 'S') {
                label.setLayoutX(x + 3);
                label.setLayoutY(y + 24);
                SettlementShape settlementShape = new SettlementShape(x + 10, y + 20, null);
                settlements.getChildren().add(settlementShape);
            } else if (id.charAt(0) == 'J') {
                KnightImage resource = new KnightImage(gameControls.catanBoard.getStructureBlocksMap().get(id).getResourceType(), x - 5, y + 15);
                resource.toFront();
                label.setLayoutX(x + 3);
                label.setLayoutY(y - 7);
                KnightShape knightShape = new KnightShape(x + 10, y + 30, null);
                knights.getChildren().addAll(knightShape, resource);
                gameControls.knightsList.add(knightShape);
            }
            structuresBoard.getChildren().add(label);
        }
        structuresBoard.setLayoutX(BOARD_offsetX - 330 + 10);
        structuresBoard.setLayoutY(BOARD_offsetY - (hexagonRadius * 2.1 + 116.91 - 30));
    }

    /**
     * Makes the 6 hexagons displayed for the board
     */
    private void makeBoard() {
        int[] translationY = {2, -2, -1, -1, 1, 1};
        int[] translationX = {0, 0, 1, -1, -1, 1};
        Color[] hexagonColors = {Color.web("#b2985c"), Color.web("#b2985c"), Color.web("#b2985c"), Color.web("#b2985c"), Color.web("#b2985c"), Color.web("#b2985c")};
        String[] hexNames = {"WoolHex", "AnyHex", "BricksHex", "OreHex", "GrainHex", "TimberHex"};
        Hexagon hexagon1 = new Hexagon(hexagonRadius, Color.WHITE);
        hexagonBoard.setTranslateX(BOARD_offsetX);
        hexagonBoard.setTranslateY(BOARD_offsetY);
        for (int i = 0; i <= 5; i++) {
            Hexagon hexagon = new Hexagon(hexagonRadius, hexagonColors[i]);
            hexagon.setTranslateX(hexagon1.getOffsetX() * translationX[i]);
            hexagon.setLayoutY(hexagon1.getOffsetY() * translationY[i]);
            double translateX = !hexNames[i].equals("AnyHex") ? (hexagon1.getOffsetX() * translationX[i]) - 150 : (hexagon1.getOffsetX() * translationX[i]) - 118;
            double translateY = !hexNames[i].equals("AnyHex") ? (hexagon1.getOffsetY() * translationY[i]) - 150 : (hexagon1.getOffsetY() * translationY[i]) - 110;
            ImageHexagon a = new ImageHexagon(hexNames[i], translateX , translateY);
            hexagonBoard.getChildren().addAll(hexagon, a);
        }
    }

    /**
     * Makes the 4 draggable structure blocks
     */
    private void makeBlocks() {
        this.blocks.getChildren().clear();
        var keySet = gameControls.catanBoard.getStructureBlocks().keySet();
        for (String id : keySet) {
            DraggableStructureBlock d = new DraggableStructureBlock(id);
            blocks.getChildren().add(d);
        }
    }

    // VIEWER

    /**
     * (UPDATED VIEWER) For task 5, constructs all the structures, highlights all the structures in b_array depending
     * on its id.
     * @param b_array list containing string of structure ids.
     */
    public void makeStructuresViewer(List<String> b_array) {
        List<String> checked = new ArrayList<>();
        roads.getChildren().clear();
        settlements.getChildren().clear();
        knights.getChildren().clear();
        cities.getChildren().clear();
        makeStructures();
        gameControls.catanBoard.makeMap();
        for (var id : b_array) {
            if (!checked.contains(id)) {
                checked.add(id);
                String id_copy = id;
                double x;
                double y;
                if (id.charAt(0) == 'K') {
                    switch (id) {
                        case "K1" -> id = "J1";
                        case "K2" -> id = "J2";
                        case "K3" -> id = "J3";
                        case "K4" -> id = "J4";
                        case "K5" -> id = "J5";
                        case "K6" -> id = "J6";
                    }
                }
                BuildableStructure structure = gameControls.catanBoard.getStructureBlocksMap().get(id);
                x = structure.getX() * (BOARD_WIDTH / 20);
                y = structure.getY() * (BOARD_HEIGHT / 12);
                Label label = new Label();
                label.toFront();
                label.setTextFill(Color.web("#439527"));
                label.setText(id);
                label.setFont(Font.font("times new roman", FontWeight.BOLD, FontPosture.REGULAR, 10));
                if (id.charAt(0) == 'R') {
                    label.setLayoutX(x + 3);
                    label.setLayoutY(y + 24);
                    int rotation;
                    String[] thirtyDegrees = new String[]{"R0", "R3", "R7", "R9", "R11", "R15", "R13"};
                    String[] ninetyDegrees = new String[]{"R1", "R4", "R6", "R12"};
                    if (Arrays.asList(ninetyDegrees).contains(id)) {
                        rotation = 90;
                    } else if (Arrays.asList(thirtyDegrees).contains(id)) {
                        rotation = 30;
                    } else {
                        rotation = -30;
                    }
                    RoadShape roadShape = new RoadShape(x, y, rotation, null);
                    roadShape.setFill(Color.SADDLEBROWN);
                    if (id.equals("RI")) {
                        roadShape.setFill(Color.PURPLE);
                    }
                    roads.getChildren().add(roadShape);
                } else if (id.charAt(0) == 'C') {
                    label.setLayoutX(x - 3);
                    label.setLayoutY(y + 24);
                    CityShape cityShape = new CityShape(x + 5, y + 30, null);
                    cityShape.setFill(Color.SADDLEBROWN);
                    cities.getChildren().add(cityShape);
                } else if (id.charAt(0) == 'S') {
                    label.setLayoutX(x + 3);
                    label.setLayoutY(y + 24);
                    SettlementShape settlementShape = new SettlementShape(x + 10, y + 20, null);
                    settlementShape.setFill(Color.SADDLEBROWN);
                    settlements.getChildren().add(settlementShape);
                } else if (id.charAt(0) == 'J' || id.charAt(0) == 'K') {
                    KnightImage resource = new KnightImage(gameControls.catanBoard.getStructureBlocksMap().get(id).getResourceType(), x - 5, y + 15);
                    resource.toFront();
                    label.setLayoutX(x + 3);
                    label.setLayoutY(y - 7);
                    KnightShape knightShape = new KnightShape(x + 10, y + 30, null);
                    knightShape.setFill(Color.SADDLEBROWN);
                    knightShape.setOpacity(0.7);
                    if (id_copy.charAt(0) == 'K') {
                        knightShape.setFill(Color.BLACK);
                        knightShape.setOpacity(1);
                    }
                    knights.getChildren().addAll(knightShape, resource);
                    gameControls.knightsList.add(knightShape);
                }
            }
            structuresBoard.setLayoutX(BOARD_offsetX - 330 + 10);
            structuresBoard.setLayoutY(BOARD_offsetY - (hexagonRadius * 2.1 + 116.91 - 30));
        }
    }
}
