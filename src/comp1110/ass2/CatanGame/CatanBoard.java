package comp1110.ass2.CatanGame;

import comp1110.ass2.CatanEnum.StructureType;
import comp1110.ass2.CatanStructure.*;

import java.util.HashMap;


public class CatanBoard {
    public BuildableStructure[][] boardArray;
    public final static int CATAN_HEIGHT = 1;
    public final static int CATAN_WIDTH = 1;
    public CatanPlayer player;
// Updated Coordinates on Thursday, 15/09/2022.
    private static final Structure[] structureBlocks = new Structure[] {
            new CatanRoad("RI", new int[]{7, 3}, 0),
            new CatanRoad("R0", new int[]{7, 5}, 1),
            new CatanRoad("R1", new int[]{4, 6}, 1),
            new CatanRoad("R2", new int[]{7, 7}, 1),
            new CatanRoad("R3", new int[]{7, 9}, 1),
            new CatanRoad("R4", new int[]{4, 10}, 1),
            new CatanRoad("R5", new int[]{7, 11}, 1),
            new CatanRoad("R6", new int[]{10, 12}, 1),
            new CatanRoad("R7", new int[]{13, 11}, 1),
            new CatanRoad("R8", new int[]{13, 9}, 1),
            new CatanRoad("R9", new int[]{13, 7}, 1),
            new CatanRoad("R10", new int[]{13, 5}, 1),
            new CatanRoad("R11", new int[]{13, 3}, 1),
            new CatanRoad("R12", new int[]{16, 10}, 1),
            new CatanRoad("R13", new int[]{19, 9}, 1),
            new CatanRoad("R14", new int[]{19, 7}, 1),
            new CatanRoad("R15", new int[]{19, 5}, 1)
    };

    private static final HashMap<String, Structure> structureBlocksMap = new HashMap<>();

    public static void makeMap() {
        structureBlocksMap.put("RI", new CatanRoad("RI", new int[]{7, 3}, 0));
        structureBlocksMap.put("R0", new CatanRoad("R0", new int[]{7, 5}, 1));
        structureBlocksMap.put("R1", new CatanRoad("R1", new int[]{4, 6}, 1));
        structureBlocksMap.put("R2", new CatanRoad("R2", new int[]{7, 7}, 1));
        structureBlocksMap.put("R3",new CatanRoad("R3", new int[]{7, 9}, 1));
        structureBlocksMap.put("R4", new CatanRoad("R4", new int[]{4, 10}, 1));
        structureBlocksMap.put("R5",  new CatanRoad("R5", new int[]{7, 11}, 1));
        structureBlocksMap.put("R6", new CatanRoad("R6", new int[]{10, 12}, 1));
        structureBlocksMap.put("R7", new CatanRoad("R7", new int[]{13, 11}, 1));
        structureBlocksMap.put("R8",  new CatanRoad("R8", new int[]{13, 9}, 1));
        structureBlocksMap.put("R9", new CatanRoad("R9", new int[]{13, 7}, 1));
        structureBlocksMap.put("R10", new CatanRoad("R10", new int[]{13, 5}, 1));
        structureBlocksMap.put("R11", new CatanRoad("R11", new int[]{13, 3}, 1));
        structureBlocksMap.put("R12",  new CatanRoad("R12", new int[]{16, 10}, 1));
        structureBlocksMap.put("R13", new CatanRoad("R13", new int[]{19, 9}, 1));
        structureBlocksMap.put("R14", new CatanRoad("R14", new int[]{19, 7}, 1));
        structureBlocksMap.put("R15", new CatanRoad("R15", new int[]{19, 5}, 1));
        structureBlocksMap.put("C7", new CatanRoad("C7", new int[]{2, 6}, 7));
        structureBlocksMap.put("C12", new CatanRoad("C12", new int[]{2, 10}, 12));
        structureBlocksMap.put("C20", new CatanRoad("C20", new int[]{20, 8}, 20));
        structureBlocksMap.put("C30", new CatanRoad("R30", new int[]{20, 4}, 30));
        structureBlocksMap.put("S3", new CatanRoad("S3", new int[]{8, 4}, 3));
        structureBlocksMap.put("S4", new CatanRoad("S4", new int[]{8, 8}, 4));
        structureBlocksMap.put("S5", new CatanRoad("S5", new int[]{8, 12}, 5));
        structureBlocksMap.put("S7", new CatanRoad("S7", new int[]{14, 10}, 7));
        structureBlocksMap.put("S9", new CatanRoad("S9", new int[]{14, 6}, 9));
        structureBlocksMap.put("S11", new CatanRoad("S11", new int[]{14, 2}, 11));
        structureBlocksMap.put("K1", new CatanRoad("K1", new int[]{4, 4}, 1));
        structureBlocksMap.put("K2", new CatanRoad("K2", new int[]{4, 8}, 2));
        structureBlocksMap.put("K3", new CatanRoad("K3", new int[]{10, 10}, 3));
        structureBlocksMap.put("K4", new CatanRoad("K4", new int[]{16, 8}, 4));
        structureBlocksMap.put("K5", new CatanRoad("K5", new int[]{16, 4}, 5));
        structureBlocksMap.put("K6", new CatanRoad("K6", new int[]{10, 2}, 6));
    }


    public CatanBoard(CatanPlayer player) {
        this.player = player;
        this.initialiseBoard(player);
    }
    private void initialiseBoard(CatanPlayer player) {
        this.boardArray = new BuildableStructure[5][4];
        for (int y = 0; y < CATAN_HEIGHT; y++) {
            for (int x = 0; x < CATAN_WIDTH; x++) {
                this.boardArray[x][y] = new BuildableStructure(x, y, StructureType.EMPTY) {
                };
            }
        }
    }
    public BuildableStructure getBuildableStructure(int x, int y){
        return this.boardArray[x][y];
    }
    public void setBuildableStructure(int x, int y, StructureType type) {
        this.boardArray[x][y].setStructureType(type);
    }
   public static Structure[] getStructureBlocks() {
        return CatanBoard.structureBlocks;
    }
    public static HashMap<String, Structure> getStructureBlocksTwo() {
        return CatanBoard.structureBlocksMap;
    }
}
