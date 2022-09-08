package comp1110.ass2.gui;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class StructureShapes {

    static Group road = new Group();

    public static class RoadShape extends Rectangle {

        double roadX;
        double roadY;

        RoadShape (double roadX, double roadY, int rotation) {
            this.roadX = roadX;
            this.roadY = roadY;
            setX(roadX);
            setY(roadX);
            setWidth(20);
            setHeight(40);
            setRotate(rotation);
            setFill(Color.WHITE);
            setStroke(Color.BLACK);
            setStrokeWidth(2);
        }
        RoadShape (String id) {

        }
    }

    public static void makeRoadShape() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {

            }
        }
    }
}
