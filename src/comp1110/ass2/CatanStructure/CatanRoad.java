package comp1110.ass2.CatanStructure;

import comp1110.ass2.CatanEnum.StructureType;

public class CatanRoad extends Structure{
    public CatanRoad(String id, int[] coordinate) {
        //(0) Ore, (1) Grain, (2) Wool, (3) Timber, (4) Bricks and (5) Gold
        super(id, coordinate);
        this.buildable_structure = new BuildableStructure(coordinate[0], coordinate[1], StructureType.DBUILT_ROAD);
    }
}
