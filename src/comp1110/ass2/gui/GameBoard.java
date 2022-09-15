package comp1110.ass2.gui;

import comp1110.ass2.CatanEnum.StructureType;
import comp1110.ass2.CatanGame.CatanBoard;
import comp1110.ass2.CatanStructure.CatanRoad;
import comp1110.ass2.CatanStructure.Structure;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;


public class GameBoard { // Trying to make Board more efficient and clean with CatanBoard
    static Group gameBoard = new Group();

    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;
    private static final double HEX_HEIGHT = 100;
    private static final double HEX_WIDTH = (int) (HEX_HEIGHT * Math.sqrt(3) / 2);

    private static final int MARGIN_X = (int) (HEX_HEIGHT * 0.5);
    private static final int BOARD_X = (int) (HEX_HEIGHT * 0.75);
    private static final int MARGIN_Y = (int) (HEX_HEIGHT * 0.5);
    private static final int BOARD_Y = MARGIN_Y;
    private static final int BOARD_WIDTH = CatanBoard.CATAN_WIDTH * (int) HEX_WIDTH;
    private static final int BOARD_HEIGHT = (int) (CatanBoard.CATAN_HEIGHT * HEX_HEIGHT);

    static CatanBoard catanBoard;

    static class StructureShape extends Polygon {
        double startX, startY;
        StructureBlock structure_block;
        StructureShape(StructureType structure_type, double startX, double startY, StructureBlock structure_block) {
            if (structure_type == StructureType.ROAD){
                this.startX = startX;
                this.startY = startY;
                Rectangle road_shape = new Rectangle();
                road_shape.setX(startX);
                road_shape.setY(startY);
                road_shape.setWidth(20);
                road_shape.setHeight(70);
                road_shape.setFill(Color.LIGHTBLUE);
                road_shape.setStroke(Color.BLACK);
                road_shape.setStrokeWidth(2);
            }
        }
    }

    static class StructureBlock extends Group {
        Structure structure;
        int x;
        int y;

        StructureBlock(String id){
            char structureID = id.charAt(0);
            for (Structure structure : catanBoard.getStructureBlocks()) {
                if (id.equals(structure.getId())) {
                    switch (structureID) {
                        case 'R':
                            this.structure = structure;
                            int x = structure.getBuildableStructure().getX() * 100;
                            int y = structure.getBuildableStructure().getY() * 100;
                            StructureShape structureShapes = new StructureShape(structure.getBuildableStructure().getStructureType(), x, y, this);
                            gameBoard.getChildren().add(structureShapes);
                    }
                }
            }

        }
    }

    static class BuildableStructureBlock extends StructureBlock {
        BuildableStructureBlock(String id) {
            super(id);
        }
    }

    private void makeBlocks() {
        gameBoard.getChildren().clear();
        for (char id = 'A'; id < 'E'; id++) {
            BuildableStructureBlock draggableIceBlock = new BuildableStructureBlock("A");
            gameBoard.getChildren().add(draggableIceBlock);
        }
    }
}
