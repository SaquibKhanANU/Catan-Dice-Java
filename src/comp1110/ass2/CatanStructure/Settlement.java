package comp1110.ass2.CatanStructure;

import comp1110.ass2.CatanEnum.StructureType;

public class Settlement extends Structure{
    public Settlement(String id, int[] coordinate, int point) {
        super(id, coordinate, point, new int[]{1, 1, 1, 1, 1, 1});
        int x = coordinate[0];
        int y = coordinate[1];
        this.buildable_structure = new BuildableStructure(x, y, StructureType.SETTLEMENT);
    }
}
