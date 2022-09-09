package comp1110.ass2.gui;

import comp1110.ass2.CatanGame.CatanBoard;
import comp1110.ass2.CatanStructure.CatanRoad;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

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
            offsetY = calculateApothem();
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
            return (Math.tan(radianStep) * radius) / 2.2;
        }

        public static void makeBoard() {
            int[] translationY = {2, -2, -1, -1, 1, 1};
            int[] translationX = {0, 0, 1, -1, -1, 1};
            Color[] hexagonColors = {Color.WHITE, Color.BLUE, Color.BLACK, Color.RED, Color.SADDLEBROWN, Color.ROYALBLUE};
            double HexagonRadius = 135;
            Hexagon hexagon1 = new Hexagon(HexagonRadius, Color.WHITE);
            hexBoard.setTranslateX(hexagon1.getOffsetX() + 400);
            hexBoard.setTranslateY(hexagon1.getOffsetY() + 250);
            for (int i = 0; i <= 5; i++) {
                Hexagon hexagon = new Hexagon(HexagonRadius, hexagonColors[i]);
                hexagon.setTranslateX(hexagon1.getOffsetX() * translationX[i]);
                hexagon.setLayoutY(hexagon1.getOffsetY() * translationY[i]);
                hexBoard.getChildren().add(hexagon);
            }
        }
    }

    static class RoadShape extends Rectangle {

        private CatanRoad[] catanRoadBlocks;
        double roadX;
        double roadY;

        // WILL REMOVE Color color just using it for testing.
        RoadShape(int roadX, int roadY, double rotation, String id, Color color) {
            this.roadX = roadX;
            this.roadY = roadY;
            setX(roadX);
            setY(roadY);
            setWidth(20);
            setHeight(40);
            setRotate(rotation);
            setFill(color);
            setStroke(Color.BLACK);
            setStrokeWidth(2);
        }

        RoadShape(String id) {

            this.catanRoadBlocks = new CatanRoad[]{ // WILL MAKE THIS A FOR LOOP IF POSSIBLE
                    new CatanRoad("R1", new int[]{0, 0}, 1),
                    new CatanRoad("R2", new int[]{1, 1}, 1),
                    new CatanRoad("R3", new int[]{2, 2}, 1),
                    new CatanRoad("R4", new int[]{3, 3}, 1),
                    new CatanRoad("R5", new int[]{0, 0}, 1),
                    new CatanRoad("R6", new int[]{1, 1}, 1),
                    new CatanRoad("R7", new int[]{2, 2}, 1),
                    new CatanRoad("R8", new int[]{3, 3}, 1),
                    new CatanRoad("R9", new int[]{0, 0}, 1),
                    new CatanRoad("R10", new int[]{1, 1}, 1),
                    new CatanRoad("R11", new int[]{2, 2}, 1),
                    new CatanRoad("R12", new int[]{3, 3}, 1),
                    new CatanRoad("R13", new int[]{0, 0}, 1),
                    new CatanRoad("R14", new int[]{1, 1}, 1),
                    new CatanRoad("R15", new int[]{2, 2}, 1),
            };

            for (int i = 0; i < catanRoadBlocks.length; i++) {
                int x = catanRoadBlocks[i].getBuildableStructure().getX() * 100;
                int y = catanRoadBlocks[i].getBuildableStructure().getY() * 100;
                double r = 0;

                if (id.equals(catanRoadBlocks[i].getId())) {
                    RoadShape road_shape = new RoadShape(x, y, r, id, Color.RED);
                    roads.getChildren().add(road_shape);
                }
            }
        }
        void makeRoads() {
        }
    }
}
