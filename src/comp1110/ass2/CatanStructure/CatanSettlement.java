package comp1110.ass2.CatanStructure;

import comp1110.ass2.CatanEnum.StructureType;

public class CatanSettlement extends Structure{
    public CatanSettlement(String id, int[] coordinate) {
        //(0) Ore, (1) Grain, (2) Wool, (3) Timber, (4) Bricks and (5) Gold
        super(id, new int[]{0, 1, 1, 1, 1, 0}, coordinate);
        this.buildable_structure = new BuildableStructure(coordinate[0], coordinate[1], StructureType.TBUILT_SETTLEMENT);
    }
}
