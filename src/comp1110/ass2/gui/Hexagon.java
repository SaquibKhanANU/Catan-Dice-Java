package comp1110.ass2.gui;

import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.shape.*;

 public class Hexagon extends Polygon {

        static Group board = new Group();
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
            board.setTranslateX(hexagon1.getOffsetX() + 400);
            board.setTranslateY(hexagon1.getOffsetY() + 250);
            for (int i = 0; i <= 5; i++) {
                Hexagon hexagon = new Hexagon(HexagonRadius, hexagonColors[i]);
                hexagon.setTranslateX(hexagon1.getOffsetX() * translationX[i]);
                hexagon.setLayoutY(hexagon1.getOffsetY() * translationY[i]);
                board.getChildren().add(hexagon);
            }
        }
    }

