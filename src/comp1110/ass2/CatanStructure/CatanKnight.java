package comp1110.ass2.CatanStructure;

import comp1110.ass2.CatanEnum.StructureType;

public class CatanKnight extends Structure {
    public CatanKnight(String id, int[] coordinate) {
        super(id, new int[]{1, 1, 1, 0, 0, 0}, coordinate);
        this.buildable_structure = new BuildableStructure(coordinate[0], coordinate[1], StructureType.JOKER);
    }
}
