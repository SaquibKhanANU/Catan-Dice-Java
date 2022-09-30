package comp1110.ass2.CatanStructure;

public abstract class Structure {

    // The buildable structure of a specific type (VARIABLE)
    protected  BuildableStructure buildable_structure;

    // The id of the block e.g. R1, all cities have different id. (VARIABLE)
    protected final String id;

    protected int[] coordinate;

    // resource_state required to build the structure, all cities have same resource state (FIXED)
    protected int[] resource_cost;

    // Check if the structure is built (VARIABLE)
    protected boolean isBuilt;

    public Structure(String id, int[] coordinate, int[] resource_cost){
        this.id = id;
        this.resource_cost = resource_cost;
        this.coordinate = coordinate;
        this.isBuilt = false;
    }

    public BuildableStructure getBuildableStructure(){
        return this.buildable_structure;
    }
    public void setBuildableStructure(BuildableStructure buildable_structure){
        this.buildable_structure = buildable_structure;
    }

    public String getId(){
        return this.id;
    }
    public int[] getResourceCost(){
        return this.resource_cost;
    }
    public void setResourceCost(int[] resource_cost) {
        this.resource_cost = resource_cost;
    }
    public boolean isBuilt(){
        return this.isBuilt;
    }
    public void setIsBuilt(boolean isBuilt){
        this.isBuilt = isBuilt;
    }

    public int[] getCoordinate(){
        return this.coordinate;
    }
    public void setCoordinate(int x, int y) {
        this.coordinate = new int[]{x, y};
    }


    public String toString() {
        return super.toString();
    }
}
