package comp1110.ass2.gui;

import comp1110.ass2.CatanEnum.StructureType;
import comp1110.ass2.CatanGame.CatanBoard;
import comp1110.ass2.CatanStructure.CatanRoad;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    static Group hexBoard = new Group();
    static Group roads = new Group();

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
            System.out.println(x + "");
            return x;
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
    }

    static class RoadShape extends Rectangle {

        private static final CatanRoad[] catanRoadBlocks = new CatanRoad[]{ // WILL MAKE THIS A FOR LOOP IF POSSIBLE
                new CatanRoad("RI", new int[]{2, 0}, 1),
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
        static boolean built;


        // WILL REMOVE Color just using it for testing.
        RoadShape(double roadX, double roadY, double rotation, String id) {
            this.roadX = roadX;
            this.roadY = roadY;
            this.rotation = rotation;

            setX(roadX);
            setY(roadY);
            setWidth(20);
            setHeight(70);
            if (built) {
                setFill(Color.BLUE);
            } else {
                setFill(Color.RED);
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
                        this.roadX = x + ((i % 13 == 0) ? -30 : 30);
                        this.roadY = y + 36;
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
            for (int i = 0; i < catanRoadBlocks.length; i++) {
                String id = catanRoadBlocks[i].getId();
                RoadShape road_shape = new RoadShape(id);
                roads.getChildren().add(road_shape);
            }
            roads.setTranslateX(260);
            roads.setTranslateY(140);
        }

        static void setBuilt(boolean built) {
            RoadShape.built = built;
        }
    }
}


