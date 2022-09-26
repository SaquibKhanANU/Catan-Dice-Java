package comp1110.ass2.CatanGame;

import comp1110.ass2.CatanEnum.StructureType;
import comp1110.ass2.CatanStructure.*;

import java.util.HashMap;
import java.util.Map;


public class CatanBoard {
    public BuildableStructure[][] boardArray;
    public final static int CATAN_HEIGHT = 20;
    public final static int CATAN_WIDTH = 12;
    public CatanPlayer player;
// Updated Coordinates on Thursday, 15/09/2022.

    private static final HashMap<String, BuildableStructure> structureBlocksMap = new HashMap<>();
    public static void makeMap() {
        //ROADS
        structureBlocksMap.put("RI", new BuildableStructure(7, 3, StructureType.ROAD, "RI", 1));
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
        structureBlocksMap.put("K1", new BuildableStructure(4, 4, StructureType.KNIGHT, "K1", 1));
        structureBlocksMap.put("K2", new BuildableStructure(4, 8, StructureType.KNIGHT, "K2", 2));
        structureBlocksMap.put("K3", new BuildableStructure(10, 10, StructureType.KNIGHT, "K3", 3));
        structureBlocksMap.put("K4", new BuildableStructure(16, 8, StructureType.KNIGHT, "K4", 4));
        structureBlocksMap.put("K5", new BuildableStructure(16, 4, StructureType.KNIGHT, "K5", 5));
        structureBlocksMap.put("K6", new BuildableStructure(10, 2, StructureType.KNIGHT, "K6", 6));
    }

    private static final HashMap<String, Structure> structureBlocks = new HashMap<>();
    public static void makeStructureBlocks() {
        structureBlocks.put("C", new CatanCity("C", new int[]{0,0}));
        structureBlocks.put("R", new CatanRoad("R", new int[]{0,0}));
        structureBlocks.put("S", new CatanSettlement("S", new int[]{0,0}));
        structureBlocks.put("K", new CatanKnight("K", new int[]{0,0}));
    }

    public CatanBoard(CatanPlayer player) {
        this.player = player;
        this.initialiseBoard();
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

    private void initialiseBoard() {
        makeMap();
        this.boardArray = new BuildableStructure[CATAN_HEIGHT][CATAN_HEIGHT];
        for (BuildableStructure structure : structureBlocksMap.values()) {
            this.boardArray[structure.getX()][structure.getY()] = structure;
        }
    }

    // TESTING
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

    public BuildableStructure getBuildableStructure(int x, int y){
        return this.boardArray[x][y];
    }

    public void setBuildableStructure(int x, int y, StructureType type) {
        this.boardArray[x][y].setStructureType(type);
    }

    public static HashMap<String, BuildableStructure> getStructureBlocksMap() {
        return CatanBoard.structureBlocksMap;
    }

    public static HashMap<String, Structure> getStructureBlocks() {
        makeStructureBlocks();
        return CatanBoard.structureBlocks;
    }

    /**
     * Check if the structure can be placed on the buildable structure at that point.
     * @param structure
     * @return
     */
    public boolean isStructurePlacementValid(Structure structure) {
        BuildableStructure structure1 = structure.getBuildableStructure();
        int x = structure1.getX();
        int y = structure1.getY();
        StructureType type = structure1.getStructureType();
        switch (type) {
            case DBUILT_ROAD -> type = StructureType.ROAD;
            case YBUILT_CITY -> type = StructureType.CITY;
            case TBUILT_SETTLEMENT -> type = StructureType.SETTLEMENT;
            case JOKER -> type = StructureType.KNIGHT;
        }
        return (!(x < 0) && !(x > 20) && !(y < 0) && !(y > 12)) && this.boardArray[x][y].getStructureType() == type;
    }

    /**
     * place a structure at the specific point, replacing the current buildable structure with new structure.
     * @param structure
     */

    public void placeStructureBlock(Structure structure){
        if (isStructurePlacementValid(structure)) {
            BuildableStructure bStructure = structure.getBuildableStructure();
            setBuildableStructure(bStructure.getX(), bStructure.getY(), bStructure.getStructureType());
            structure.setIsBuilt(true);
        }
    }

    /**
     * remove a structure at the specific point, replacing the current structure with previous buildable structure.
     * @param structure
     */
    public void removeStructureBlock(Structure structure){
        BuildableStructure bStructure = structure.getBuildableStructure();
        StructureType type = structure.getBuildableStructure().getStructureType();
        switch (bStructure.getStructureType()) {
            case DBUILT_ROAD -> type = StructureType.ROAD;
            case YBUILT_CITY -> type = StructureType.CITY;
            case TBUILT_SETTLEMENT -> type = StructureType.SETTLEMENT;
            case JOKER -> type = StructureType.KNIGHT;
        }
        setBuildableStructure(bStructure.getX(), bStructure.getY(), type); // ID AND POINT WILL REVERT
        structure.setIsBuilt(false);
    }
}
