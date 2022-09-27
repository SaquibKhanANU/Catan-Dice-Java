package comp1110.ass2.gui;

import comp1110.ass2.CatanEnum.StructureType;
import comp1110.ass2.CatanGame.CatanBoard;
import comp1110.ass2.CatanStructure.BuildableStructure;
import comp1110.ass2.CatanStructure.Structure;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
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

    Group hexagonBoard = new Group();
    Group roads = new Group();
    Group cities = new Group();
    Group settlements = new Group();
    Group knights = new Group();
    Group blocks = new Group();
    Group structuresBoard = new Group(hexagonBoard, roads, cities, settlements, knights);
    Group sidePanel = new Group();

    CatanBoard catanboard;



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

    static class RoadShape extends Rectangle {
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
                    draggableStructureBlock.setPosition();
                    draggableStructureBlock.snapToGrid();
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
            this.structure = CatanBoard.getStructureBlocks().get(id);
            double x = this.structure.getBuildableStructure().getX();
            double y = this.structure.getBuildableStructure().getY();
            this.roadShape = new RoadShape(x, y, 90, this);
            blocks.getChildren().add(roadShape);
        }

        protected void snapToGrid() {
            this.setLayoutX(BOARD_offsetX + this.x * 10);
            this.setLayoutY(BOARD_offsetY + ((this.y)) * 10);
            System.out.println(this.x +"" + this.y);
            roadShape.setTranslateX(this.getLayoutX());
            roadShape.setTranslateY(this.getLayoutY());
        }
    }

    class DraggableStructureBlock extends StructureBlock {
        double homeX, homeY;
        DraggableStructureBlock(String id) {
            super(id);

            this.homeX = 990;
            this.homeY = 600;

            this.snapToHome();
        }
        private void snapToHome() {
            this.setLayoutX(this.homeX);
            this.setLayoutY(this.homeY);

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
            this.x = (int) ((this.getLayoutX()) + BLOCKS_OFFSETX);
            this.y = (int) (this.getLayoutY() + BLOCKS_OFFSETY);
            System.out.println(this.x + "," + this.y);
            BuildableStructure hex = catanboard.getBuildableStructure(this.x, 0);
            BuildableStructure destHex = catanboard.getBuildableStructure(this.x, this.y);
        }

    }

    private void makeStructures() {
        CatanBoard.makeMap();
        var keySet = CatanBoard.getStructureBlocksMap().keySet();
        for (var id : keySet) {
            BuildableStructure structure = CatanBoard.getStructureBlocksMap().get(id);
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
        var keySet = CatanBoard.getStructureBlocks().keySet();
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
        leftPanel.setWidth(200);
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
        this.catanboard = new CatanBoard();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(this.root, WINDOW_WIDTH, WINDOW_HEIGHT);

        scene.setFill(Color.NAVAJOWHITE);

        root.getChildren().add(hexagonBoard);
        root.getChildren().add(structuresBoard);
        root.getChildren().add(blocks);
        root.getChildren().add(sidePanel);

        makeBoard();
        makeStructures();
        makeBlocks();
        makeSidePanel();
        this.newGame();

        stage.setScene(scene);
        stage.show();
    }
}
