package comp1110.ass2.CatanStructure;

import comp1110.ass2.CatanEnum.ResourceType;
import comp1110.ass2.CatanEnum.StructureType;

// Author: Saquib Khan
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
     * Constructor for the Buildable Structure class that allows for specifying the structure's
     * coordinates and its type on a specific players board.
     *
     * @Author Saquib Khan
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
                case "J1" -> this.resourceType = ResourceType.ORE;
                case "J2" -> this.resourceType = ResourceType.GRAIN;
                case "J3" -> this.resourceType = ResourceType.WOOL;
                case "J4" -> this.resourceType = ResourceType.TIMBER;
                case "J5" -> this.resourceType = ResourceType.BRICKS;
                case "J6" -> this.resourceType = ResourceType.ANY;
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
     * @return x gets the x-coordiante of buildable structure
     */
    public int getX(){
        return this.x;
    }

    /**
     * @return y gets the y-coordiante of buildable structure
     */
    public int getY(){
        return this.y;
    }

    /**
     * @return structure_type gets the type of buildable structure
     */
    public StructureType getStructureType(){
        return this.structure_type;
    }

    /**
     *
     * @return gets the id of the buildable structure
     */
    public String getId(){
        return this.id;
    }

    /**
     * sets the x to new coordinate
     * @param x int
     */
    public void setX(int x){
        this.x = x;
    }

    /**
     * sets the y to new coordinate
     * @param y int
     */
    public void setY(int y){
        this.y = y;
    }

    /**
     * sets the structure type to new structure type
     * @param structure_type StructureType
     */
    public void setStructureType(StructureType structure_type){
        this.structure_type = structure_type;
    }

    /**
     * @return returns the point value of the buildable structure.
     */
    public int getPoint() {
        return point;
    }

    public ResourceType getResourceType() {
        return resourceType;
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
