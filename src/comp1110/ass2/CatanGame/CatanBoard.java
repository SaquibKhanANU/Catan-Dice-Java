package comp1110.ass2.CatanGame;

import comp1110.ass2.CatanEnum.StructureType;
import comp1110.ass2.CatanStructure.*;



public class CatanBoard {
    public BuildableStructure[][] boardArray;
    public final static int CATAN_HEIGHT = 1;
    public final static int CATAN_WIDTH = 1;
    public CatanPlayer player;
// Updated Coordinates on Thursday, 15/09/2022.
    private final Structure[] structureBlocks = new Structure[] {
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
   public Structure[] getStructureBlocks() {
        return this.structureBlocks;
    }
}
