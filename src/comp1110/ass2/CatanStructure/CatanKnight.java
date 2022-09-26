package comp1110.ass2.CatanStructure;

import comp1110.ass2.CatanEnum.StructureType;

public class CatanKnight extends Structure {
    public CatanKnight(String id) {
        super(id, new int[]{1, 1, 1, 0, 0, 0});
        this.buildable_structure = new BuildableStructure(0, 0, StructureType.KNIGHT, "K", 0);
    }
}
