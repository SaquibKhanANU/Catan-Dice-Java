package comp1110.ass2.CatanGame;

import comp1110.ass2.CatanEnum.StructureType;
import comp1110.ass2.CatanStructure.*;
import comp1110.ass2.gui.Scenes.GameBoard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Author: Saquib Khan
public class CatanBoard {
    // the board array where structures can be placed
    public BuildableStructure[][] boardArray;

    // the height of catan board
    public final static int CATAN_HEIGHT = 21;
    // the width of catan board
    public final static int CATAN_WIDTH = 13;
    // Hashmap giving each structure a id as a key
    public final HashMap<String, Structure> structureBlocks = new HashMap<>();
    // Hashmap giving each buildable structure a id as a key
    public  final HashMap<String, BuildableStructure> structureBlocksMap = new HashMap<>();

    // Adds all the structures to a hashmap
    public void makeMap() {
        //ROADS
        structureBlocksMap.put("RI", new BuildableStructure(7, 3, StructureType.DBUILT_ROAD, "RI", 0));
        structureBlocksMap.put("R0", new BuildableStructure(7, 5, StructureType.ROAD, "R0", 1));
        structureBlocksMap.put("R1", new BuildableStructure(4, 6, StructureType.ROAD, "R1", 1));
        structureBlocksMap.put("R2", new BuildableStructure(7, 7, StructureType.ROAD, "R2", 1));
        structureBlocksMap.put("R3", new BuildableStructure(7, 9, StructureType.ROAD, "R3", 1));
        structureBlocksMap.put("R4", new BuildableStructure(4, 10, StructureType.ROAD, "R4", 1));
        structureBlocksMap.put("R5", new BuildableStructure(7, 11, StructureType.ROAD, "R5", 1));
        structureBlocksMap.put("R6", new BuildableStructure(10, 12, StructureType.ROAD, "R6", 1));
        structureBlocksMap.put("R7", new BuildableStructure(13, 11, StructureType.ROAD, "R7", 1));
        structureBlocksMap.put("R8", new BuildableStructure(13, 9, StructureType.ROAD, "R8", 1));
        structureBlocksMap.put("R9", new BuildableStructure(13, 7, StructureType.ROAD, "R9", 1));
        structureBlocksMap.put("R10", new BuildableStructure(13, 5, StructureType.ROAD, "R10", 1));
        structureBlocksMap.put("R11", new BuildableStructure(13, 3, StructureType.ROAD, "R11", 1));
        structureBlocksMap.put("R12", new BuildableStructure(16, 10, StructureType.ROAD, "R12", 1));
        structureBlocksMap.put("R13", new BuildableStructure(19, 9, StructureType.ROAD, "R13", 1));
        structureBlocksMap.put("R14", new BuildableStructure(19, 7, StructureType.ROAD, "R14", 1));
        structureBlocksMap.put("R15", new BuildableStructure(19, 5, StructureType.ROAD, "R15", 1));
        //CITIES
        structureBlocksMap.put("C7", new BuildableStructure(2, 6, StructureType.CITY, "C7", 7));
        structureBlocksMap.put("C12", new BuildableStructure(2, 10, StructureType.CITY, "C12", 12));
        structureBlocksMap.put("C20", new BuildableStructure(20, 8, StructureType.CITY, "C20", 20));
        structureBlocksMap.put("C30", new BuildableStructure(20, 4, StructureType.CITY, "C30", 30));
        //SETTLEMENT
        structureBlocksMap.put("S3", new BuildableStructure(8, 4, StructureType.SETTLEMENT, "S3", 3));
        structureBlocksMap.put("S4", new BuildableStructure(8, 8, StructureType.SETTLEMENT, "S4", 4));
        structureBlocksMap.put("S5", new BuildableStructure(8, 12, StructureType.SETTLEMENT, "S5", 5));
        structureBlocksMap.put("S7", new BuildableStructure(14, 10, StructureType.SETTLEMENT, "S7", 7));
        structureBlocksMap.put("S9", new BuildableStructure(14, 6, StructureType.SETTLEMENT, "S9", 9));
        structureBlocksMap.put("S11", new BuildableStructure(14, 2, StructureType.SETTLEMENT, "S11", 11));
        //KNIGHTS
        structureBlocksMap.put("J1", new BuildableStructure(4, 4, StructureType.JOKER, "J1", 1));
        structureBlocksMap.put("J2", new BuildableStructure(4, 8, StructureType.JOKER, "J2", 2));
        structureBlocksMap.put("J3", new BuildableStructure(10, 10, StructureType.JOKER, "J3", 3));
        structureBlocksMap.put("J4", new BuildableStructure(16, 8, StructureType.JOKER, "J4", 4));
        structureBlocksMap.put("J5", new BuildableStructure(16, 4, StructureType.JOKER, "J5", 5));
        structureBlocksMap.put("J6", new BuildableStructure(10, 2, StructureType.JOKER, "J6", 6));
    }
    // adds all the structures to a hashmap
    public void makeStructureBlocks() {
        structureBlocks.put("R", new Structure("R", new int[2]));
        structureBlocks.put("C", new Structure("C", new int[2]));
        structureBlocks.put("K", new Structure("K", new int[2]));
        structureBlocks.put("S", new Structure("S", new int[2]));
    }

    /**
     * Constructs the Catan Board
     * @Author Saquib khan
     */
    public CatanBoard() {
        this.initialiseBoard();
    }

    /**
     * adds a buildable structure to a part of the array
     */
    public void initialiseBoard() {
        makeMap();
        this.boardArray = new BuildableStructure[CATAN_HEIGHT][CATAN_WIDTH];
        for (BuildableStructure structure : structureBlocksMap.values()) {
            this.boardArray[structure.getX()][structure.getY()] = structure;
        }
    }

    public BuildableStructure getBuildableStructure(int x, int y){
        return this.boardArray[x][y];
    }

    public void setBuildableStructure(int x, int y, StructureType type) {
        this.boardArray[x][y].setStructureType(type);
    }

    public  HashMap<String, BuildableStructure> getStructureBlocksMap() {
        return this.structureBlocksMap;
    }

    public  HashMap<String, Structure> getStructureBlocks() {
        makeStructureBlocks();
        return this.structureBlocks;
    }


    /**
     * Check if the structure can be placed on the buildable structure at that point.
     * @param structure the structure being checked for valid placement
     * @return boolean of if the structure can be removed
     */
    public boolean isStructurePlacementValid(Structure structure) {
        int x = structure.getBuildableStructure().getX();
        int y = structure.getBuildableStructure().getY();
        StructureType type = getBuildableStructure(x, y).getStructureType();
        return switch (structure.getBuildableStructure().getStructureType()) {
            case YBUILT_CITY -> type == StructureType.CITY;
            case TBUILT_SETTLEMENT -> type == StructureType.SETTLEMENT;
            case DBUILT_ROAD -> type == StructureType.ROAD;
            case KNIGHT -> type == StructureType.JOKER;
            default ->
                    throw new IllegalStateException("Unexpected value: " + structure.getBuildableStructure().getStructureType());
        };
    }

    /**
     * place a structure at the specific point, replacing the current buildable structure with new structure.
     * @param structure The structure being placed
     */

    public void placeStructureBlock(Structure structure){
        BuildableStructure bStructure = structure.getBuildableStructure();
        setBuildableStructure(bStructure.getX(), bStructure.getY(), bStructure.getStructureType());
        structure.setIsBuilt(true);
    }

    /**
     * remove a structure at the specific point, replacing the current structure with previous buildable structure.
     * @param structure the structure being removed
     */
    public void removeStructureBlock(Structure structure){
        BuildableStructure bStructure = structure.getBuildableStructure();
        StructureType type = bStructure.getStructureType();
        switch (bStructure.getStructureType()) {
            case DBUILT_ROAD -> type = StructureType.ROAD;
            case YBUILT_CITY -> type = StructureType.CITY;
            case TBUILT_SETTLEMENT -> type = StructureType.SETTLEMENT;
            case KNIGHT -> type = StructureType.JOKER;
        }
        setBuildableStructure(bStructure.getX(), bStructure.getY(), type); // ID AND POINT WILL REVERT
        structure.setIsBuilt(false);
    }


    // Testing
    public CatanBoard(String initialState) {
        this.boardArray = new BuildableStructure[5][4];
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 5; x++) {
                StructureType type = StructureType.fromChar(initialState.charAt(5 * y + x));
                this.boardArray[x][y] = new BuildableStructure(x, y, type);
            }
        }
    }

    // TESTING, from assignment 1
    public String boardToString() {
        StringBuilder s = new StringBuilder();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 5; x++) {
                s.append(this.getBuildableStructure(x, y).getStructureType().toChar());
            }
            s.append("\n");
        }
        return s.toString();
    }
}
