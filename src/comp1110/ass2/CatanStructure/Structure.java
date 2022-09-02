package comp1110.ass2.CatanStructure;

public abstract class Structure {

    // The list of buildable blocks making up this structure type, e.g. {R1, R2, ... , R15} (MAYBE NOT NEEDED)
    protected BuildableStructure[] buildable_structures;

    // The buildable structure of a specific type (VARIABLE)
    protected  BuildableStructure buildable_structure;

    // The id of the block e.g. R1, all cities have different id. (VARIABLE)
    protected final String id;

    // The point associated with the structure type, all cities have different point (VARIABLE)
    protected int point;

    // resource_state required to build the structure, all cities have same resource state (FIXED)
    protected int[] resource_cost;

    // The coordinate of the structure
    protected  int[] coordinate;

    // Check if the structure is built (VARIABLE)
    protected boolean isBuilt;

    public Structure(String id, int[] coordinate, int point, int[] resource_cost){
        this.id = id;
        this.coordinate = coordinate;
        this.point = point;
        this.resource_cost = resource_cost;
        this.isBuilt = false;
    }

    public BuildableStructure getBuildableStructure(){
        return this.buildable_structure;
    }
    public void setBuildableStructure(BuildableStructure buildable_structure){
        this.buildable_structure = buildable_structure;
    }
    public int getPoint(){
        return this.point;
    }
    public void setPoint(int point){
        this.point = point;
    }
    public int[] getResourceCost(){
        return this.resource_cost;
    }
    public void setResourceCost(int[] resource_cost) {
        this.resource_cost = resource_cost;
    }
    public int[] getCoordinate(){
        return this.coordinate;
    }
    public void setCoordinate(int[] coordinate) {
        this.coordinate = coordinate;
    }
    public boolean isBuilt(){
        return this.isBuilt;
    }
    public void setIsBuilt(boolean isBuilt){
        this.isBuilt = isBuilt;
    }
    public String getId(){
        return this.id;
    }

    public String toString() {
        return super.toString();
    }
}
