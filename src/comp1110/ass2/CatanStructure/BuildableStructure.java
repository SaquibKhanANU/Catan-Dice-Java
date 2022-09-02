package comp1110.ass2.CatanStructure;

import comp1110.ass2.CatanEnum.StructureType;

public class BuildableStructure {
    // The x-cord of the BuildableStructure
    private int x;

    // The y-cord of the BuildableStructure
    private int y;

    // The type of this structure
    private StructureType structure_type;

    /**
     * Constructor for the BuildableStructure class that allows for specifying the structure's
     * coordinates and its type on a specific players board.
     *
     * @param x int
     * @param y int
     * @param structure_type StructureType
     */
    public BuildableStructure(int x, int y, StructureType structure_type) {
        this.x = x;
        this.y = y;
        this.structure_type = structure_type;
    }

    /**
     *
     * @return x
     */
    public int getX(){
        return this.x;
    }

    /**
     *
     * @return y
     */
    public int getY(){
        return this.y;
    }

    /**
     *
     * @return structure_type
     */
    public StructureType getStructure_type(){
        return this.structure_type;
    }

    /**
     *
     * @param x int
     */
    public void setX(int x){
        this.x = x;
    }

    /**
     *
     * @param y int
     */
    public void setY(int y){
        this.y = y;
    }

    /**
     *
     * @param structure_type StructureType
     */
    public void setStructure_type(StructureType structure_type){
        this.structure_type = structure_type;
    }

    /**
     *
     * @return String
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
