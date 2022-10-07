package comp1110.ass2.gui.Scenes;

import comp1110.ass2.CatanDice;
import comp1110.ass2.CatanGame.CatanBoard;
import comp1110.ass2.CatanGame.CatanPlayer;
import comp1110.ass2.CatanStructure.BuildableStructure;
import comp1110.ass2.CatanStructure.Structure;
import comp1110.ass2.gui.Controls.GameControls;
import comp1110.ass2.gui.Game;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
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

import java.util.Arrays;

public class GameBoard extends Pane {
    public static final double BOARD_WIDTH = 618;
    public static final double BOARD_HEIGHT = 670;
    public static final double BOARD_offsetX = (Game.WINDOW_WIDTH * 0.45);
    public static final double BOARD_offsetY = (Game.WINDOW_HEIGHT * 0.5);
    public static final double BLOCKS_OFFSETY = 600;
    public static final double BLOCKS_OFFSETX = 970;
    public static final double hexagonRadius = 135;
    public Text warningText;
    String board_state = "";
    String action = "";
    Group hexagonBoard = new Group();
    Group roads = new Group();
    Group cities = new Group();
    Group settlements = new Group();
    Group knights = new Group();
    Group blocks = new Group();
    Group resources = new Group();
    Group structuresBoard = new Group(hexagonBoard, roads, cities, settlements, knights);
    Group scoreCounter = new Group();
    Group warningTextGroup = new Group();
    CatanBoard catanBoard;
    public CatanPlayer catanPlayer;
    public GameControls gameControls;
    public GameBoard(CatanPlayer catanPlayer) {
        this.catanPlayer = catanPlayer;
        catanPlayer.currentTurn = catanPlayer == Game.playerOne;
        newGame();
        makeBoard();
        makeStructures();
        makeBlocks();
        gameControls = new GameControls(catanPlayer);
        gameControls.sidePanel.toBack();
        blocks.toFront();
        getChildren().addAll(hexagonBoard, structuresBoard, resources, scoreCounter, gameControls.allControls, blocks, warningTextGroup);
    }

    static class Hexagon extends Polygon {
        private final double radius;
        private final double radianStep = (2 * Math.PI) / 6;
        private final double offsetY;
        private final double offsetX;
        Hexagon(double radius, Paint color) {

            this.radius = radius;
            setFill(color);
            setStroke(Color.SANDYBROWN);
            setEffect(new DropShadow(10, Color.SADDLEBROWN));
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

    class RoadShape extends Rectangle {
        double mouseX, mouseY;
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
                strokeWidth = 2;
            } else {
                fillColor = Color.SADDLEBROWN;
                strokeWidth = 1;
            }
            strokeColor = Color.BLACK;
            setFill(fillColor);
            setStroke(strokeColor);
            setRotate(rotation);
            setStrokeWidth(strokeWidth);
            if (structureBlock instanceof DraggableStructureBlock draggableStructureBlock) {
                    this.setOnMouseEntered(event -> {
                        if (catanPlayer.currentTurn && draggableStructureBlock.structure.getRemovable() && catanBoard.canDoRemove()) {
                            setFill(Color.TAN);
                            setEffect(new DropShadow(10, Color.SADDLEBROWN));
                        }
                    });
                    this.setOnMouseExited(event -> {
                        warningTextGroup.getChildren().clear();
                        setFill(Color.SADDLEBROWN);
                        setEffect(new DropShadow(10, Color.SADDLEBROWN));
                    });

                    this.setOnMousePressed(event -> {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            this.mouseX = event.getSceneX();
                            this.mouseY = event.getSceneY();
                            if (draggableStructureBlock.structure.isBuilt()) {
                                warningTextGroup.getChildren().clear();
                                makeWarning("RIGHT CLICK TO REMOVE");
                                System.out.println("RIGHT CLICK TO REMOVE");
                            }
                        } else if (event.getButton() == MouseButton.SECONDARY) {
                            if (draggableStructureBlock.structure.getRemovable()) {
                                if (draggableStructureBlock.structure.isBuilt()) {
                                    if (catanBoard.canDoRemove()) {
                                        if (catanPlayer.currentTurn) {
                                            draggableStructureBlock.removePoint();
                                            draggableStructureBlock.removeBoardState();
                                            catanBoard.removeStructureBlock(draggableStructureBlock.structure);
                                            catanPlayer.structures.remove(draggableStructureBlock.structure);
                                            blocks.getChildren().remove(event.getTarget());
                                        } else {
                                            warningTextGroup.getChildren().clear();
                                            makeWarning("NOT YOUR TURN");
                                            System.out.println("NOT YOUR TURN");
                                        }
                                    } else {
                                        System.out.println("REMOVE STRUCTURES FURTHER OF HIGHER POINT VALUE");
                                    }
                                }
                                if (draggableStructureBlock.isOnBoard() && catanPlayer.currentTurn) {
                                    draggableStructureBlock.snapToHome();
                                }
                            } else {
                                warningTextGroup.getChildren().clear();
                                makeWarning("NOT REMOVABLE");
                                System.out.println("NOT REMOVABLE");
                            }
                            event.consume();
                        }
                    });

                    this.setOnMouseDragged(event -> {
                        if (!draggableStructureBlock.structure.isBuilt()) {
                            draggableStructureBlock.toFront();
                            double movementX = event.getSceneX() - this.mouseX;
                            double movementY = event.getSceneY() - this.mouseY;
                            draggableStructureBlock.drag(movementX, movementY);
                            this.mouseX = event.getSceneX();
                            this.mouseY = event.getSceneY();
                        }
                    });

                    this.setOnMouseReleased(event -> {
                        if (!draggableStructureBlock.structure.isBuilt()) {
                            if (draggableStructureBlock.isOnBoard()) {
                                draggableStructureBlock.setPosition();
                                System.out.println(draggableStructureBlock.structure);
                                try {
                                if (catanBoard.isStructurePlacementValid(draggableStructureBlock.structure)) {
                                    if (catanPlayer.currentTurn) {
                                        draggableStructureBlock.updateAction();
                                        if (CatanDice.canDoAction(action, board_state, new int[]{6, 6, 6, 6, 6, 6})) { // GET ACTION, GET BOARD_STATE, GET RESOURCE.
                                            draggableStructureBlock.updateBoardState();
                                            System.out.println(board_state);
                                            System.out.println(action);
                                            draggableStructureBlock.snapToGrid();
                                            draggableStructureBlock.accessPoints();
                                            catanBoard.placeStructureBlock(draggableStructureBlock.structure);
                                            catanPlayer.structures.add(draggableStructureBlock.structure);
                                            draggableStructureBlock.newBlock();
                                        } else {
                                            System.out.println("BUILD REQUIRED STRUCTURES TO BUILD THIS");
                                            draggableStructureBlock.snapToHome();
                                        }
                                    } else {
                                        System.out.println("NOT YOUR TURN");
                                        draggableStructureBlock.snapToHome();
                                    }
                                }
                                else {
                                    System.out.println("INVALID STRUCTURE PLACEMENT");
                                    draggableStructureBlock.snapToHome();
                                }
                                } catch (NullPointerException e){
                                    System.out.println("CANNOT PLACE STRUCTURE HERE");
                                    draggableStructureBlock.snapToHome();
                                }
                            } else {
                                draggableStructureBlock.snapToHome();
                            }
                        }
                    });
            }
        }
    }

    class SettlementShape extends Polygon {
        double mouseX, mouseY;

        SettlementShape(double x, double y, StructureBlock structureBlock) {
            double a = 20;
            double b = 20;
            getPoints().addAll(0.0, -a,
                    b, a,
                    -b, a);
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
        }
    }

    class KnightShape extends Circle {
        double mouseX, mouseY;

        KnightShape(double x, double y, StructureBlock structureBlock) {
            Circle c = new Circle();
            c.setCenterX(x);
            c.setCenterY(y - 25);
            c.setRadius(15);
            setCenterX(x);
            setCenterY(y);
            setRadius(25);
            Color fillColor;
            Color strokeColor;
            if (structureBlock == null) {
                knights.getChildren().add(c);
                fillColor = Color.WHITE;
            } else {
                blocks.getChildren().add(c);
                fillColor = Color.SADDLEBROWN;
            }
            strokeColor = Color.BLACK;
            setFill(fillColor);
            setStroke(strokeColor);
            setStrokeWidth(2);
        }
    }

     class CityShape extends Circle {
        double mouseX, mouseY;
        CityShape(double cityX, double cityY, StructureBlock structureBlock) {
            setCenterX(cityX);
            setCenterY(cityY);
            setRadius(25);
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
        }
    }

    class StructureBlock extends Group {
        final Structure structure;
        int x;
        int y;
        RoadShape roadShape;
        CityShape cityShape;
        KnightShape knightShape;
        SettlementShape settlementShape;
        Text pointCounter = new Text();

        StructureBlock(String id) {
            this.structure = catanBoard.getStructureBlocks().get(id);
            int x = this.structure.getBuildableStructure().getX();
            int y = this.structure.getBuildableStructure().getY();
            this.roadShape = new RoadShape(x, y, 90, this);
            this.cityShape = new CityShape(x, y,  this);
            this.knightShape = new KnightShape(x, y, this);
            this.settlementShape = new SettlementShape(x, y, this);
            switch (structure.getId()) {
                case "R" -> blocks.getChildren().add(roadShape);
                case "C" -> blocks.getChildren().add(cityShape);
                case "K" -> blocks.getChildren().add(knightShape);
                case "S" -> blocks.getChildren().add(settlementShape);
            }
        }

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
                    this.setLayoutX(225 + (this.x * (BOARD_WIDTH / 20)));
                    this.setLayoutY((BOARD_offsetY - (hexagonRadius * 2.1 + 116.91 - 60)) + ((this.y * (BOARD_HEIGHT / 12))));
                    knightShape.setTranslateX(this.getLayoutX());
                    knightShape.setTranslateY(this.getLayoutY());
                }
                case "S" -> {
                    this.setLayoutX(220 + 10  + (this.x * (BOARD_WIDTH / 20)));
                    this.setLayoutY((BOARD_offsetY - (hexagonRadius * 2.1 + 116.91 - 50)) + ((this.y * (BOARD_HEIGHT / 12))));
                    settlementShape.setTranslateX(this.getLayoutX());
                    settlementShape.setTranslateY(this.getLayoutY());
                }
            }
        }

        protected void updateRotation() {
            setRotate(30);
            BuildableStructure test = catanBoard.getBuildableStructure(this.x, this.y);
            String[] thirtyDegrees = new String[]{"R0", "R3", "R7", "R9", "R11", "R15", "R13"};
            String[] negThirtyDegrees = new String[]{"R1", "R4", "R6", "R12"};
            if (Arrays.asList(negThirtyDegrees).contains(test.getId())) {
                this.roadShape.setRotate(90);
            } else if (Arrays.asList(thirtyDegrees).contains(test.getId())) {
                this.roadShape.setRotate(30);
            } else {
                this.roadShape.setRotate(-30);
            }
        }

        protected void accessPoints() {
            BuildableStructure buildableStructure = catanBoard.getBuildableStructure(this.x, this.y);
            int points = buildableStructure.getPoint();
            catanPlayer.score = catanPlayer.score  + points;
            scoreCounter.getChildren().clear();
            pointCounter.setText("Points for " + catanPlayer.turn_num + ": " + catanPlayer.score);
            pointCounter.setFill(Color.web("#439527"));
            pointCounter.setFont(Font.font("times new roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
            pointCounter.setX(300);
            pointCounter.setY(20);
            pointCounter.toFront();
            scoreCounter.getChildren().add(pointCounter);
        }

        protected void removePoint() {
            BuildableStructure buildableStructure = catanBoard.getBuildableStructure(this.x, this.y);
            int points = buildableStructure.getPoint();
            catanPlayer.score  = catanPlayer.score  - points;
            scoreCounter.getChildren().clear();
            pointCounter.setText("Points for " + catanPlayer.turn_num + ": " + catanPlayer.score);
            pointCounter.toFront();
            pointCounter.setFill(Color.web("#439527"));
            pointCounter.setFont(Font.font("times new roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
            pointCounter.setX(300);
            pointCounter.setY(20);
            scoreCounter.getChildren().add(pointCounter);
        }

        // WILL SIMPLIFY CODE BELOW, CURRENTLY TESTING IF IT WILL WORK

        // CATAN BOARD
        protected void updateBoardState() {
            if (catanBoard.getBuildableStructure(this.x, this.y).getId().equals("R0")) {
                board_state = board_state + catanBoard.getBuildableStructure(this.x, this.y).getId();
            } else {
                board_state = board_state + "," + catanBoard.getBuildableStructure(this.x, this.y).getId();
            }
        }
        protected void removeBoardState() {
            if (catanBoard.getBuildableStructure(this.x, this.y).getId().equals("R0")) {
                board_state = board_state.replace(catanBoard.getBuildableStructure(this.x, this.y).getId(), "");
            }
            board_state = board_state.replace("," + catanBoard.getBuildableStructure(this.x, this.y).getId(), "");
        }

        protected void updateAction() {
            action = "build" + " " + catanBoard.getBuildableStructure(this.x, this.y).getId();
        }
    }

    class DraggableStructureBlock extends StructureBlock {
        double homeX, homeY;
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
                    this.homeX = BLOCKS_OFFSETX + 110;
                    this.homeY = BLOCKS_OFFSETY + 25;
                }
                case "K" -> {
                    this.homeX = BLOCKS_OFFSETX + 110 + 55;
                    this.homeY = BLOCKS_OFFSETY + 25;
                }
                case "S" -> {
                    this.homeX = BLOCKS_OFFSETX + 65;
                    this.homeY = BLOCKS_OFFSETY + 25;
                }
            }
            this.snapToHome();
        }

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

        private void setPosition() {
            this.x = (int) ((getLayoutX() - (BOARD_offsetX - 330 + 10)) / (630  / 21)); // 439.0,147.0
            this.y = (int) ((getLayoutY() - (BOARD_offsetY - (hexagonRadius * 2.1 + 116.91 - 30))) / (670 / 12));
            this.structure.getBuildableStructure().setX(this.x);
            this.structure.getBuildableStructure().setY(this.y);
            System.out.println(catanBoard.getBuildableStructure(this.x, this.y));
            System.out.println("(" + getLayoutX() + "," + getLayoutY() + ")");
            System.out.println(this.x + "," + this.y);
            BuildableStructure destHex = catanBoard.getBuildableStructure(this.x, this.y);
        }

        private boolean isOnBoard() {
            return getLayoutX() > (250) && getLayoutX() < (850) &&
                    getLayoutY() > (50) && getLayoutY() < (650);
        }

        protected void newBlock(){
            blocks.getChildren().add(new DraggableStructureBlock(this.structure.getId()));
        }
    }

    private void makeStructures() {
        catanBoard.makeMap();
        var keySet = catanBoard.getStructureBlocksMap().keySet();
        for (var id : keySet) {
            BuildableStructure structure = catanBoard.getStructureBlocksMap().get(id);
            double x = structure.getX() * (BOARD_WIDTH / 20);
            double y = structure.getY() * (BOARD_HEIGHT / 12);
            Label label = new Label();
            label.toFront();
            label.setTextFill(Color.web("#439527"));
            label.setText(id);
            label.setFont(Font.font("times new roman", FontWeight.BOLD, FontPosture.REGULAR, 10));
            label.setLayoutX(x+3);
            label.setLayoutY(y+24);
            if (id.charAt(0) == 'R') {
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
                if (id.equals("RI")){
                    roadShape.setFill(Color.PURPLE);
                }
                roads.getChildren().add(roadShape);
            }
            else if (id.charAt(0) == 'C') {
                CityShape cityShape = new CityShape(x + 5, y + 30, null);
                cities.getChildren().add(cityShape);
            }
            else if (id.charAt(0) == 'S') {
                SettlementShape settlementShape = new SettlementShape(x + 10, y + 20, null);
                settlements.getChildren().add(settlementShape);
            }
            else if (id.charAt(0) == 'K') {
                KnightShape knightShape = new KnightShape(x + 10, y + 30, null);
                knights.getChildren().add(knightShape);
            }
            structuresBoard.getChildren().add(label);
        }
        structuresBoard.setLayoutX(BOARD_offsetX - 330 + 10);
        structuresBoard.setLayoutY(BOARD_offsetY - (hexagonRadius * 2.1 + 116.91 - 30));
    }

    private void makeBoard() {
        int[] translationY = {2, -2, -1, -1, 1, 1};
        int[] translationX = {0, 0, 1, -1, -1, 1};
        Color[] hexagonColors = {Color.TAN, Color.TAN, Color.TAN, Color.TAN, Color.TAN, Color.TAN};
        Hexagon hexagon1 = new Hexagon(hexagonRadius, Color.WHITE);
        hexagonBoard.setTranslateX(BOARD_offsetX);
        hexagonBoard.setTranslateY(BOARD_offsetY);
        for (int i = 0; i <= 5; i++) {
            Hexagon hexagon = new Hexagon(hexagonRadius, hexagonColors[i]);
            hexagon.setTranslateX(hexagon1.getOffsetX() * translationX[i]);
            hexagon.setLayoutY(hexagon1.getOffsetY() * translationY[i]);
            hexagonBoard.getChildren().add(hexagon);
        }
    }

    private void makeBlocks() {
        this.blocks.getChildren().clear();
        var keySet = catanBoard.getStructureBlocks().keySet();
        for (String id : keySet) {
            DraggableStructureBlock d = new DraggableStructureBlock(id);
            blocks.getChildren().add(d);
        }
    }

    public void newGame(){
        this.catanBoard = new CatanBoard();
    }

    public void makeWarning(String warning) {
        warningText = new Text();
        warningText.setText(warning);
        warningText.toFront();
        warningText.setFill(Color.RED);
        warningText.setFont(Font.font("times new roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
        warningText.setX(350);
        warningText.setY(30);
        warningTextGroup.getChildren().add(warningText);
    }
}
