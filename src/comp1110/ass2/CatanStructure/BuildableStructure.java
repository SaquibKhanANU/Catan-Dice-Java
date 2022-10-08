package comp1110.ass2.CatanStructure;

import comp1110.ass2.CatanEnum.ResourceType;
import comp1110.ass2.CatanEnum.StructureType;

public class BuildableStructure {
    // The x-cord of the BuildableStructure
    private int x;

    // The y-cord of the BuildableStructure
    private int y;

    // The type of this structure
    private StructureType structure_type;

    // The id of this structure
    private String id;

    // The point of this structure
    private int point;

    // For knights.
    private ResourceType resourceType;

    /**
     * Constructor for the BuildableStructure class that allows for specifying the structure's
     * coordinates and its type on a specific players board.
     *
     * @param x int
     * @param y int
     * @param structure_type StructureType
     */
    public BuildableStructure(int x, int y, StructureType structure_type, String id, int point) {
        this.x = x;
        this.y = y;
        this.structure_type = structure_type;
        this.id = id;
        this.point = point;
        switch (id) {
                case "K1" -> this.resourceType = ResourceType.ORE;
                case "K2" -> this.resourceType = ResourceType.GRAIN;
                case "K3" -> this.resourceType = ResourceType.WOOL;
                case "K4" -> this.resourceType = ResourceType.TIMBER;
                case "K5" -> this.resourceType = ResourceType.BRICKS;
                case "K6" -> this.resourceType = ResourceType.ANY;
                default -> this.resourceType = null;
        }
    }

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
    public StructureType getStructureType(){
        return this.structure_type;
    }

    public String getId(){
        return this.id;
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
    public void setStructureType(StructureType structure_type){
        this.structure_type = structure_type;
    }


    public void setId(String id){
        this.id = id;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    /**
     *
     * @return String
     */
    @Override
    public String toString() {
        return structure_type + "";
    }


}
