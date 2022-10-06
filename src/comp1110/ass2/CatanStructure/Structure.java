package comp1110.ass2.CatanStructure;

public class Structure {

    // The buildable structure of a specific type (VARIABLE)
    protected  BuildableStructure buildable_structure;

    // The id of the block e.g. R1, all cities have different id. (VARIABLE)
    protected final String id;

    protected int[] coordinate;

    // resource_state required to build the structure, all cities have same resource state (FIXED)
    protected int[] resource_cost;

    // Check if the structure is built (VARIABLE)
    protected boolean isBuilt;

    protected boolean removable;


    public Structure(String id, int[] coordinate){
        this.id = id;
        char structure_type = id.charAt(0);
        if (structure_type == 'R'){
            this.resource_cost = new int[]{0, 0, 0, 1, 1, 0};
        } else if (structure_type == 'S'){
            this.resource_cost = new int[]{0, 1, 1, 1, 1, 0};
        } else if (structure_type == 'C'){
            this.resource_cost = new int[]{3, 2, 0, 0, 0, 0};
        } else if (structure_type == 'J'){
            this.resource_cost = new int[]{1, 1, 1, 0, 0, 0};
        }
        this.coordinate = coordinate;
        this.isBuilt = false;
        this.removable = true;
    }

    public Structure(String id){
        this.id = id;
        char structure_type = id.charAt(0);
        if (structure_type == 'R'){
            this.resource_cost = new int[]{0, 0, 0, 1, 1, 0};
        } else if (structure_type == 'S'){
            this.resource_cost = new int[]{0, 1, 1, 1, 1, 0};
        } else if (structure_type == 'C'){
            this.resource_cost = new int[]{3, 2, 0, 0, 0, 0};
        } else if (structure_type == 'J'){
            this.resource_cost = new int[]{1, 1, 1, 0, 0, 0};
        }
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

    public void setRemovable(boolean removable) {
        this.removable = removable;
    }
    public boolean getRemovable() {
        return this.removable;
    }

    public String toString() {
        return super.toString();
    }
}
