package comp1110.ass2.gui;

import comp1110.ass2.CatanGame.CatanBoard;
import comp1110.ass2.CatanStructure.BuildableStructure;
import comp1110.ass2.CatanStructure.Structure;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;



public class Game extends Application {

    private final Group root = new Group();
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;

    public static final double BOARD_WIDTH = 618;
    public static final double BOARD_HEIGHT = 670;
    public static final double BOARD_offsetX = (WINDOW_WIDTH * 0.45);
    public static final double BOARD_offsetY = (WINDOW_HEIGHT * 0.5);

    public static final double BLOCKS_OFFSETY = 600;
    public static final double BLOCKS_OFFSETX= 990;
    public static final double hexagonRadius = 135;

    public static int score = 0;
    public static int round = 0;

    Group hexagonBoard = new Group();
    Group roads = new Group();
    Group cities = new Group();
    Group settlements = new Group();
    Group knights = new Group();
    Group blocks = new Group();
    Group structuresBoard = new Group(hexagonBoard, roads, cities, settlements, knights);
    Group sidePanel = new Group();

    Group controls = new Group();

    Group scoreCounter = new Group();

    CatanBoard catanBoard;



    class Hexagon extends Polygon {

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
            double x = (Math.tan(radianStep) * radius) / 2;
            return x;
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
            if (structureBlock == null) {
                fillColor = Color.WHITE;
            } else {
                fillColor = Color.SADDLEBROWN;
            }
            strokeColor = Color.BLACK;
            setFill(fillColor);
            setStroke(strokeColor);
            setRotate(rotation);
            setStrokeWidth(2);

            if (structureBlock instanceof DraggableStructureBlock draggableStructureBlock) {
                this.setOnMousePressed(event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        this.mouseX = event.getSceneX();
                        this.mouseY = event.getSceneY();
                        if (draggableStructureBlock.structure.isBuilt()) {
                            catanBoard.removeStructureBlock(draggableStructureBlock.structure);
                        }
                    }
                    else if (event.getButton() == MouseButton.SECONDARY) {
                        if (draggableStructureBlock.structure.isBuilt()) {
                            catanBoard.removeStructureBlock(draggableStructureBlock.structure);
                        }
                        if (draggableStructureBlock.isOnBoard()) {
                            draggableStructureBlock.snapToHome();
                        }
                        event.consume();
                    }
                });

                this.setOnMouseDragged(event -> {
                    draggableStructureBlock.toFront();
                    double movementX = event.getSceneX() - this.mouseX;
                    double movementY = event.getSceneY() - this.mouseY;
                    draggableStructureBlock.drag(movementX, movementY);
                    this.mouseX = event.getSceneX();
                    this.mouseY = event.getSceneY();
                });
                
                this.setOnMouseReleased(event -> {
                    if (draggableStructureBlock.isOnBoard()) {
                        draggableStructureBlock.setPosition();
                        System.out.println(draggableStructureBlock.structure);
                        if (catanBoard.isStructurePlacementValid(draggableStructureBlock.structure)) {
                            draggableStructureBlock.snapToGrid();
                            draggableStructureBlock.accessPoints();
                            catanBoard.placeStructureBlock(draggableStructureBlock.structure);
                        }
                        else {
                            draggableStructureBlock.snapToHome();
                        }
                    }
                    else {
                        draggableStructureBlock.snapToHome();
                    }
                });
            }
        }
    }

    static class SettlementShape extends Polygon {

        SettlementShape(double x, double y) {
            double a = 20;
            double b = 20;
            getPoints().addAll(0.0, -a,
                    b, a,
                    -b, a);
            setLayoutX(x);
            setLayoutY(y);
            setFill(Color.WHITE);
            setStroke(Color.BLACK);
            setStrokeWidth(2);
        }
    }

    class KnightShape extends Circle {
        KnightShape(double x, double y, StructureBlock structureBlock) {
            Circle c = new Circle();
            c.setCenterX(x);
            c.setCenterY(y - 25);
            c.setRadius(15);
            setCenterX(x);
            setCenterY(y);
            setRadius(25);
            setFill(Color.WHITE);
            setStroke(Color.BLACK);
            setStrokeWidth(2);
            if (structureBlock == null) {
                knights.getChildren().add(c);
            } else {
                blocks.getChildren().add(c);
            }
        }
    }

    static class CityShape extends Circle {
        CityShape(double cityX, double cityY) {
            setCenterX(cityX);
            setCenterY(cityY);
            setRadius(25);
            setFill(Color.WHITE);
            setStroke(Color.BLACK);
            setStrokeWidth(2);
        }
    }

    class StructureBlock extends Group {
        final Structure structure;
        int x;
        int y;
        RoadShape roadShape;


        StructureBlock(String id) {
            this.structure = catanBoard.getStructureBlocks().get(id);
            int x = this.structure.getBuildableStructure().getX();
            int y = this.structure.getBuildableStructure().getY();
            this.roadShape = new RoadShape(x, y, 90, this);
            blocks.getChildren().add(roadShape);
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

        protected void snapToGrid() {
            updateRotation();
            this.setLayoutX(220 + (this.x * (BOARD_WIDTH / 20)));
            this.setLayoutY((BOARD_offsetY - (hexagonRadius * 2.1 + 116.91 - 30)) + ((this.y * (BOARD_HEIGHT / 12))));
            roadShape.setTranslateX(this.getLayoutX());
            roadShape.setTranslateY(this.getLayoutY());
        }

        protected void accessPoints() {
            BuildableStructure buildableStructure = catanBoard.getBuildableStructure(this.x, this.y);
            int points = buildableStructure.getPoint();
            score = score + points;

            scoreCounter.getChildren().clear();
            Text text = new Text();
            text.setText("Points for " + round + ": " +score);
            text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
            text.setFill(Color.RED);
            text.setX(220);
            text.setY(60);
            scoreCounter.getChildren().add(text);
        }
}

class DraggableStructureBlock extends Game.StructureBlock {
    double homeX, homeY;

    DraggableStructureBlock(String id) {
        super(id);

        this.homeX = BLOCKS_OFFSETX;
        this.homeY = BLOCKS_OFFSETY;

        this.snapToHome();
    }

    private void snapToHome() {
        this.setLayoutX(this.homeX);
        this.setLayoutY(this.homeY);
        this.roadShape.setRotate(90);
        this.roadShape.setTranslateX(this.getLayoutX());
        this.roadShape.setTranslateY(this.getLayoutY());
    }

    protected void drag(double movementX, double movementY) {
        this.setLayoutX(this.getLayoutX() + movementX);
        this.setLayoutY(this.getLayoutY() + movementY);
        this.roadShape.setTranslateX(this.getLayoutX());
        this.roadShape.setTranslateY(this.getLayoutY());
    }

    private void setPosition() {
        this.x = (int) ((getLayoutX() - (BOARD_offsetX - 330 + 10)) / (BOARD_WIDTH / 21)); // 439.0,147.0
        this.y = (int) ((getLayoutY() - (BOARD_offsetY - (hexagonRadius * 2.1 + 116.91 - 30))) / (BOARD_HEIGHT / 13));
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
}

    private void makeStructures() {
        catanBoard.makeMap();
        var keySet = catanBoard.getStructureBlocksMap().keySet();
        for (var id : keySet) {
            BuildableStructure structure = catanBoard.getStructureBlocksMap().get(id);
            double x = structure.getX() * (BOARD_WIDTH / 20);
            double y = structure.getY() * (BOARD_HEIGHT / 12);
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
                roads.getChildren().add(roadShape);
            }
            else if (id.charAt(0) == 'C') {
                CityShape cityShape = new CityShape(x + 5, y + 30);
                cities.getChildren().add(cityShape);
            }
            else if (id.charAt(0) == 'S') {
                SettlementShape settlementShape = new SettlementShape(x + 10, y + 20);
                settlements.getChildren().add(settlementShape);
            }
            else if (id.charAt(0) == 'K') {
                KnightShape knightShape = new KnightShape(x + 10, y + 30, null);
                knights.getChildren().add(knightShape);
            }
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
            DraggableStructureBlock c = new DraggableStructureBlock(id);
            blocks.getChildren().add(c);
        }
    }

    private void makeSidePanel(){
        Rectangle rightPanel = new Rectangle();
        rightPanel.toBack();
        rightPanel.setHeight(700);
        rightPanel.setWidth(300);
        Color c = Color.web("#439527");;
        rightPanel.setFill(c);
        rightPanel.setX(899);
        rightPanel.setY(0);
        rightPanel.setStroke(Color.BLACK);
        rightPanel.setStrokeWidth(3);
        Rectangle leftPanel = new Rectangle();
        leftPanel.toBack();
        leftPanel.setHeight(700);
        leftPanel.setWidth(200); // 230
        leftPanel.setFill(c);
        leftPanel.setX(1);
        leftPanel.setY(0);
        leftPanel.setStroke(Color.BLACK);
        leftPanel.setStrokeWidth(3);
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

    public void newGame(){
        this.catanBoard = new CatanBoard();
    }

    BorderPane pane1 = new BorderPane();
    Scene scene1 = new Scene(pane1);
    Stage stage1 = new Stage();

    private void makeControls() {
        Button btn = new Button();

        btn.setText("Score Board");
        btn.setTextFill(Color.DARKGREEN);
        btn.setOnAction(e -> {
            if(stage1.isShowing()) {
                stage1.toFront();
            } else {
                displayScoreBoard();
            }
        });
        btn.setTranslateX(220);
        btn.setTranslateY(20);

        Button button = new Button();
        button.setText("End Turn");
        button.setLayoutX(60);
        button.setLayoutY(260);
        button.setOnAction(e -> {
            endTurn();
        });
        this.controls.getChildren().add(button);

        pane1.setCenter(btn);

        controls.getChildren().addAll(btn);
    }

    private void endTurn()  {
        round = round + 1;
        score = 0;
    }

    public void displayScoreBoard(){
        Image image2;
        ImageView iv2;

        image2 = new Image("comp1110/ass2/assets/CatanScoreBoard.JPG");
        iv2 = new ImageView();
        iv2.setFitWidth(200);
        iv2.setFitHeight(200);
        iv2.setX(2);
        iv2.setY(124);
        iv2.setImage(image2);

        Text text = new Text();
        text.setText("Points for round " + round + ": " +score);
        text.setFont(Font.font("cambria", FontWeight.BOLD, FontPosture.REGULAR, 14));
        text.setFill(Color.DARKGREEN);
        text.setX(0);
        text.setY(0);

        pane1.setCenter(iv2);
        pane1.setTop(text);

        stage1.setResizable(false);

        stage1.setScene(scene1);
        // Without this, the audio won't stop!
        stage1.setOnCloseRequest(
                e -> {
                    e.consume();
                    stage1.close();
                }
        );
        stage1.showAndWait();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(this.root, WINDOW_WIDTH, WINDOW_HEIGHT);

        scene.setFill(Color.NAVAJOWHITE);
        stage.setResizable(false);

        root.getChildren().add(hexagonBoard);
        root.getChildren().add(structuresBoard);
        root.getChildren().add(blocks);
        root.getChildren().add(sidePanel);
        root.getChildren().add(controls);
        root.getChildren().add(scoreCounter);

        makeControls();
        this.newGame();
        makeBoard();
        makeStructures();
        makeBlocks();
        makeSidePanel();

        stage.setScene(scene);
        stage.show();
    }
}
