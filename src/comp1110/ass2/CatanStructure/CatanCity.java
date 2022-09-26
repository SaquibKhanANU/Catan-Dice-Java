package comp1110.ass2.CatanStructure;

import comp1110.ass2.CatanEnum.StructureType;

public class CatanCity extends Structure{
    public CatanCity(String id) {
        //(0) Ore, (1) Grain, (2) Wool, (3) Timber, (4) Bricks and (5) Gold
        super(id, new int[]{3, 2, 0, 0, 0, 0});
        this.buildable_structure = new BuildableStructure(0, 0, StructureType.CITY, "C", 0);
    }
}
