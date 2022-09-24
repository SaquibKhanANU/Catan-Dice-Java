package comp1110.ass2.gui;

import comp1110.ass2.CatanStructure.CatanCity;
import comp1110.ass2.CatanStructure.CatanKnight;
import comp1110.ass2.CatanStructure.CatanRoad;
import comp1110.ass2.CatanStructure.CatanSettlement;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Board {

    static Group hexBoard = new Group();
    static Group roads = new Group();
    static Group cities = new Group();
    static Group settlements = new Group();

    static Group knights = new Group();
    static Group jokers = new Group();

    static boolean built;

    static void setBuilt(boolean built) {
        Board.built = built;
    }

    public static class Hexagon extends Polygon {

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

    public static void makeBoard() {
        int[] translationY = {2, -2, -1, -1, 1, 1};
        int[] translationX = {0, 0, 1, -1, -1, 1};
        Color[] hexagonColors = {Color.TAN, Color.TAN, Color.TAN, Color.TAN, Color.TAN, Color.TAN};
        double HexagonRadius = 135;
        Hexagon hexagon1 = new Hexagon(HexagonRadius, Color.WHITE);
        hexBoard.setTranslateX(600);
        hexBoard.setTranslateY(360);
        for (int i = 0; i <= 5; i++) {
            Hexagon hexagon = new Hexagon(HexagonRadius, hexagonColors[i]);
            hexagon.setTranslateX(hexagon1.getOffsetX() * translationX[i]);
            hexagon.setLayoutY(hexagon1.getOffsetY() * translationY[i]);
            hexBoard.getChildren().add(hexagon);
        }
    }

    static class RoadShape extends Rectangle {

        private static final CatanRoad[] catanRoadBlocks = new CatanRoad[]{ // WILL MAKE THIS A FOR LOOP IF POSSIBLE
                new CatanRoad("RI", new int[]{2, 0}, 0),
                new CatanRoad("R0", new int[]{2, 2}, 1),
                new CatanRoad("R1", new int[]{1, 2}, 1),
                new CatanRoad("R2", new int[]{2, 3}, 1),
                new CatanRoad("R3", new int[]{2, 5}, 1),
                new CatanRoad("R4", new int[]{1, 5}, 1),
                new CatanRoad("R5", new int[]{2, 6}, 1),
                new CatanRoad("R6", new int[]{3, 7}, 1),
                new CatanRoad("R7", new int[]{4, 6}, 1),
                new CatanRoad("R8", new int[]{4, 5}, 1),
                new CatanRoad("R9", new int[]{4, 3}, 1),
                new CatanRoad("R10", new int[]{4, 2}, 1),
                new CatanRoad("R11", new int[]{4, 0}, 1),
                new CatanRoad("R12", new int[]{5, 5}, 1),
                new CatanRoad("R13", new int[]{6, 5}, 1),
                new CatanRoad("R14", new int[]{6, 3}, 1),
                new CatanRoad("R15", new int[]{6, 2}, 1)
        };
        double roadX;
        double roadY;
        double rotation;
        
        String id;

        RoadShape(double roadX, double roadY, double rotation, String id) {
            this.id = id;
            this.roadX = roadX;
            this.roadY = roadY;
            this.rotation = rotation;
            setX(roadX);
            setY(roadY);
            setWidth(20);
            setHeight(70);
            if (built) {
                setFill(Color.SADDLEBROWN);
            } else if (id.equals("RI")) {
                setFill(Color.PURPLE);
            } else {
                setFill(Color.LIGHTBLUE);
            }
            setRotate(rotation);
            setStroke(Color.BLACK);
            setStrokeWidth(2);
        }

        RoadShape(String id) {
            for (int i = 0; i < catanRoadBlocks.length; i++) {
                double x = catanRoadBlocks[i].getBuildableStructure().getX() * 110;
                double y = catanRoadBlocks[i].getBuildableStructure().getY() * 73.06;
                if (i >= 8 && i <= 12) {
                    this.rotation = (i % 2 == 0) ? 30 : -30;
                    double xOffset = -30;
                    double yOffset = (i % 2 == 0) ? 36 : -36;
                    this.roadX = x + xOffset;
                    this.roadY = y + yOffset;
                } else if (i == 2 || i == 5 || i == 7 || i == 13) {
                    this.rotation = -90;
                    if ( i == 7) {
                        this.roadX = x;
                        this.roadY = y;
                    } else {
                        this.roadX = x + ((i % 13 == 0) ? -30 : 40);
                        this.roadY = y + 34;
                    }
                } else if (i >= 14) {
                    this.rotation = (i % 3 == 0) ? -30 : 30;
                    this.roadX = x - 70;
                    this.roadY = y;
                } else {
                    this.rotation = (i % 3 == 0) ? -30 : 30;
                    this.roadX = x;
                    this.roadY = y;
                }

                if (id.equals(catanRoadBlocks[i].getId())) {
                    RoadShape road_shape = new RoadShape(roadX, roadY, rotation, id);
                    roads.getChildren().add(road_shape);
                }
            }
            roads.setTranslateX(260);
            roads.setTranslateY(140);
        }
        public static void makeRoads() {
            for (CatanRoad catanRoadBlock : catanRoadBlocks) {
                String id = catanRoadBlock.getId();
                RoadShape road_shape = new RoadShape(id);
                roads.getChildren().add(road_shape);
            }
        }
    }

    static class CityShape extends Circle {
        private static final CatanCity[] catanCityBlocks = new CatanCity[]{
                new CatanCity("C7", new int[]{0,2}, 7),
                new CatanCity("C12", new int[]{0,5}, 12),
                new CatanCity("C20", new int[]{6,4}, 20),
                new CatanCity("C30", new int[]{6,1}, 30)
        };
        double cityX;
        double cityY;

        CityShape(double cityX, double cityY) {
            this.cityX = cityX;
            this.cityY = cityY;
            setCenterX(cityX);
            setCenterY(cityY);
            setRadius(25);
            if (built) {
                setFill(Color.SADDLEBROWN);
            } else {
                setFill(Color.LIGHTBLUE);
            }
            setStroke(Color.BLACK);
            setStrokeWidth(2);
        }

        CityShape(String id) {
            for (CatanCity catanCityBlock : catanCityBlocks) {
                if (id.equals(catanCityBlock.getId())) {
                double x = catanCityBlock.getBuildableStructure().getX() * 90;
                double y = catanCityBlock.getBuildableStructure().getY();
                if (x != 0) {
                    this.cityY = (y % 2 != 0) ? y * 105 : (y - 1) * 105;
                } else {
                    this.cityY = (y % 2 == 0) ? y * 105 : (y - 1) * 105;
                }
                this.cityX = x;
                    CityShape road_shape = new CityShape(cityX, cityY);
                    cities.getChildren().add(road_shape);
                }
                cities.setTranslateX(350);
                cities.setTranslateY(150);
            }
        }
        public static void makeCities() {
            for (CatanCity catanCityBlock : catanCityBlocks) {
                String id = catanCityBlock.getId();
                CityShape city_shape = new CityShape(id);
                cities.getChildren().add(city_shape);
            }
        }
    }

    static class SettlementShape extends Polygon {
        private static final CatanSettlement[] catanSettlementBlocks = new CatanSettlement[]{
                new CatanSettlement("S3",new int[]{2,1}, 3),
                new CatanSettlement("S4",new int[]{2,4}, 4),
                new CatanSettlement("S5",new int[]{2,7}, 5),
                new CatanSettlement("S7",new int[]{4,6}, 7),
                new CatanSettlement("S9",new int[]{4,3}, 9),
                new CatanSettlement("S11",new int[]{4,0}, 11)
        };

        SettlementShape (double x, double y) {
            double a = 20;
            double b = 20;
            getPoints().addAll(0.0, -a,
                    b, a,
                    -b, a);
            setLayoutX(x);
            setLayoutY(y);
            if (built) {
                setFill(Color.SADDLEBROWN);
            } else {
                setFill(Color.LIGHTBLUE);
            }
            setStroke(Color.BLACK);
            setStrokeWidth(2);
        }

        SettlementShape(String id){
            for (int i = 0; i < catanSettlementBlocks.length; i++) {
                double x = catanSettlementBlocks[i].getBuildableStructure().getX();
                double y = catanSettlementBlocks[i].getBuildableStructure().getY();
                double xs = x * 93;
                double ys = (i > 2) ? y * 70 : y * 70 + 40;

                if (id.equals(catanSettlementBlocks[i].getId())) {
                    SettlementShape road_shape = new SettlementShape(xs, ys);
                    settlements.getChildren().add(road_shape);
                }
                settlements.setTranslateX(350);
                settlements.setTranslateY(140);
            }
        }
        public static void makeSettlements() {
            for (CatanSettlement catanSettlementBlocks : catanSettlementBlocks) {
                String id = catanSettlementBlocks.getId();
                SettlementShape settlement_shape = new SettlementShape(id);
                settlements.getChildren().add(settlement_shape);
            }
        }
    }
    static class KnightShape extends Circle {
        static Color color;
        String[] knightId = new String[]{"K1", "K2", "K3", "K4", "K5", "K6"};
        List<String> knightsList = Arrays.asList(knightId);
        private static final CatanKnight[] catanKnightBlocks = new CatanKnight[]{
                new CatanKnight("J1", new int[]{4,4}, 0),
                new CatanKnight("J2", new int[]{4,8}, 0),
                new CatanKnight("J5", new int[]{16,4}, 0),
                new CatanKnight("J4", new int[]{16,8}, 0),
                new CatanKnight("J3", new int[]{10,2}, 0),
                new CatanKnight("J6", new int[]{10,10}, 0),
        };

        KnightShape(double x, double y) {
            Circle c = new Circle();
            c.setCenterX(x);
            c.setCenterY(y-25);
            c.setRadius(15);
            setCenterX(x);
            setCenterY(y);
            setRadius(25);
            if (built) {
                setFill(color);
            } else {
                setFill(Color.LIGHTBLUE);
            }
            setStroke(Color.BLACK);
            setStrokeWidth(2);
            knights.getChildren().add(c);
        }
        static void setUsed(Color color){
            Board.KnightShape.color = color;
        }

        KnightShape(String id) {
            for (CatanKnight catanKnight: catanKnightBlocks) {
                if (id.equals(catanKnight.getId())) {
                    double x = catanKnight.getBuildableStructure().getX() * 30 + 90;
                    double y = catanKnight.getBuildableStructure().getY()* 60;
                    KnightShape road_shape = new KnightShape(x, y);
                    knights.getChildren().add(road_shape);
                }
                knights.setTranslateX(210);
                knights.setTranslateY(20);
            }
        }
        public static void makeKnights() {
            for (CatanKnight catanKnight : catanKnightBlocks) {
                String id = catanKnight.getId();
                KnightShape city_shape = new KnightShape(id);
                knights.getChildren().add(city_shape);
            }
        }
    }
}

/*
    class StructureShape extends Group {
        final Structure structure;
        int x;
        int y;
        StructureShape(String id) {
            CatanBoard.makeMap();
            this.structure = CatanBoard.getStructureBlocksTwo().get(id);
            double x = structure.getCoordinate()[0] * (BOARD_WIDTH / 20);
            double y = structure.getCoordinate()[1] * (BOARD_HEIGHT / 12);
            if (id.charAt(0) == 'R') {
                int rotation;
                String[] thirtyDegrees = new String[]{"R0", "R3", "R7", "R9", "R11", "R15", "R13"};
                String[] ninetyDegrees = new String[]{"R1", "R4", "R6", "R12"};
                if (Arrays.stream(ninetyDegrees).anyMatch(a -> a.equals(id))) {
                    rotation = 90;
                } else if (Arrays.stream(thirtyDegrees).anyMatch(a -> a.equals(id))) {
                    rotation = 30;
                } else {
                    rotation = -30;
                }
                RoadShape roadShape = new RoadShape(x, y, rotation);
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
                KnightShape knightShape = new KnightShape(x + 10, y + 30);
                knights.getChildren().add(knightShape);
            }
        }
    }

    package comp1110.ass2.gui;

import comp1110.ass2.CatanEnum.StructureType;
import comp1110.ass2.CatanGame.CatanBoard;
import comp1110.ass2.CatanStructure.Structure;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.w3c.dom.css.Rect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game extends Application {

    private final Group root = new Group();
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;

    public static final double BOARD_WIDTH = 618;
    public static final double BOARD_HEIGHT = 670;
    public static final double BOARD_offsetX =  (WINDOW_WIDTH * 0.45);
    public static final double BOARD_offsetY =  (WINDOW_HEIGHT * 0.5);
    public static final double hexagonRadius = 135;

    Group hexagonBoard = new Group();
    Group roads = new Group();
    Group cities = new Group();
    Group settlements = new Group();
    Group knights = new Group();
    Group blocks = new Group();
    Group structuresBoard = new Group(hexagonBoard, roads, cities, settlements, knights);


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
        RoadShape(double roadX, double roadY, double rotation) {
            setX(roadX);
            setY(roadY);
            setWidth(20);
            setHeight(60);
            setFill(Color.WHITE);
            setRotate(rotation);
            setStroke(Color.BLACK);
            setStrokeWidth(2);
        }
    }

    static class SettlementShape extends Polygon {
        StructureBlock structureBlock;
        double mouseX, mouseY;
        SettlementShape (double x, double y, StructureBlock structureBlock) {
            this.structureBlock = structureBlock;

            double a = 20;
            double b = 20;
            getPoints().addAll(0.0, -a,
                    b, a,
                    -b, a);
            setLayoutX(x);
            setLayoutY(y);
            if (structureBlock == null) {
                setFill(Color.WHITE);
            } else {
                if (structureBlock instanceof DraggableStructureBlock)
                    setFill(Color.ALICEBLUE);
            }
            setStroke(Color.BLACK);
            setStrokeWidth(2);
            if (structureBlock instanceof DraggableStructureBlock draggableStructureBlock) {
                this.setOnMousePressed(event -> {
                    mouseX = event.getSceneX();
                    mouseY = event.getSceneY();
                    draggableStructureBlock.toFront();
                    event.consume();
                });
                this.setOnMouseDragged(event -> {
                    draggableStructureBlock.toFront();
                    this.mouseX = event.getSceneX();
                    this.mouseY = event.getSceneY();
                });

            }
        }
    }

     class KnightShape extends Circle {
        KnightShape(double x, double y) {
            Circle c = new Circle();
            c.setCenterX(x);
            c.setCenterY(y-25);
            c.setRadius(15);
            setCenterX(x);
            setCenterY(y);
            setRadius(25);
            setFill(Color.WHITE);
            setStroke(Color.BLACK);
            setStrokeWidth(2);
            knights.getChildren().add(c);
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

    class StructureShape1 extends Group {
        int x;
        int y;
        StructureBlock structureBlock;
        StructureType structureType;
        StructureShape1(StructureType type, int x, int y, StructureBlock structureBlock) {
            this.x = x;
            this.y = y;
            this.structureType = type;
            this.structureBlock = structureBlock;
            if (type == StructureType.CITY) {
                CityShape cityBlock = new CityShape(x, y);
                blocks.getChildren().add(cityBlock);
            }
            else if (type == StructureType.KNIGHT) {
                KnightShape k = new KnightShape(x, y);
                blocks.getChildren().add(k);
            }
            else if (type == StructureType.SETTLEMENT) {
                SettlementShape s = new SettlementShape(x, y, null);
                blocks.getChildren().add(s);
            }
            else if (type == StructureType.ROAD) {
                int rotation;
                RoadShape r = new RoadShape(x  , x, 90);
                blocks.getChildren().add(r);
            }
        }
    }

    class StructureShape extends Group {
        final Structure structure;
        int x;
        int y;
        StructureShape(String id) {
            CatanBoard.makeMap();
            this.structure = CatanBoard.getStructureBlocksTwo().get(id);
            double x = structure.getCoordinate()[0] * (BOARD_WIDTH / 20);
            double y = structure.getCoordinate()[1] * (BOARD_HEIGHT / 12);
            if (id.charAt(0) == 'R') {
                int rotation;
                String[] thirtyDegrees = new String[]{"R0", "R3", "R7", "R9", "R11", "R15", "R13"};
                String[] ninetyDegrees = new String[]{"R1", "R4", "R6", "R12"};
                if (Arrays.stream(ninetyDegrees).anyMatch(a -> a.equals(id))) {
                    rotation = 90;
                } else if (Arrays.stream(thirtyDegrees).anyMatch(a -> a.equals(id))) {
                    rotation = 30;
                } else {
                    rotation = -30;
                }
                RoadShape roadShape = new RoadShape(x, y, rotation);
                roads.getChildren().add(roadShape);
            }
            else if (id.charAt(0) == 'C') {
                CityShape cityShape = new CityShape(x + 5, y + 30);
                cities.getChildren().add(cityShape);
            }
            else if (id.charAt(0) == 'S') {
                SettlementShape settlementShape = new SettlementShape(x + 10, y + 20, null);
                settlements.getChildren().add(settlementShape);
            }
            else if (id.charAt(0) == 'K') {
                KnightShape knightShape = new KnightShape(x + 10, y + 30);
                knights.getChildren().add(knightShape);
            }
        }
    }

    class StructureBlock extends Group {
        int x;
        int y;
        StructureBlock (StructureType type) {
            if (type == StructureType.CITY) {
                CityShape cityBlock = new CityShape(100, 100);
                blocks.getChildren().add(cityBlock);
            }
            else if (type == StructureType.KNIGHT) {
                KnightShape k = new KnightShape(200, 100);
                blocks.getChildren().add(k);
            }
            else if (type == StructureType.SETTLEMENT) {
                SettlementShape s = new SettlementShape(150, 100, this);
                blocks.getChildren().add(s);
            }
            else if (type == StructureType.ROAD) {
                RoadShape r = new RoadShape(25  , 70, 90);
                blocks.getChildren().add(r);
            }
        }
    }

    class DraggableStructureBlock extends StructureBlock {
        double homeX, homeY;
        DraggableStructureBlock(StructureType type) {
            super(type);
        }
    }

    private void makeStructures() {
        CatanBoard.makeMap();
        var keySet = CatanBoard.getStructureBlocksTwo().keySet();
        for (var entry : keySet) {
            StructureShape c = new StructureShape(entry);
            roads.getChildren().add(c);
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
        StructureType[] types = new StructureType[]{StructureType.ROAD, StructureType.CITY, StructureType.SETTLEMENT, StructureType.KNIGHT};
        for (StructureType type : types) {
            DraggableStructureBlock draggableStructureBlock = new DraggableStructureBlock(type);
            this.blocks.getChildren().add(draggableStructureBlock);
        }
        this.blocks.setLayoutY(500);
        this.blocks.setLayoutX(925);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(this.root, WINDOW_WIDTH, WINDOW_HEIGHT);

        root.getChildren().add(hexagonBoard);
        root.getChildren().add(structuresBoard);
        root.getChildren().add(blocks);

        makeBoard();
        makeStructures();
        makeBlocks();

        stage.setScene(scene);
        stage.show();
    }
}

 */


