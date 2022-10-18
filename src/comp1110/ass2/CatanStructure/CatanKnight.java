package comp1110.ass2.CatanStructure;

import comp1110.ass2.CatanEnum.StructureType;

public class CatanKnight extends Structure {
    public CatanKnight(String id, int[] coordinate) {
        super(id, coordinate);
        this.buildable_structure = new BuildableStructure(coordinate[0], coordinate[1], StructureType.KNIGHT);
    }
}
