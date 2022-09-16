package comp1110.ass2.CatanStructure;

import comp1110.ass2.CatanEnum.StructureType;

public class CatanCity extends Structure{
    public CatanCity(String id, int[] coordinate, int point) {
        //(0) Ore, (1) Grain, (2) Wool, (3) Timber, (4) Bricks and (5) Gold
        super(id, coordinate, point, new int[]{3, 2, 0, 0, 0, 0});
        int x = coordinate[0];
        int y = coordinate[1];
        this.buildable_structure = new BuildableStructure(x, y, StructureType.CITY);
    }
}
