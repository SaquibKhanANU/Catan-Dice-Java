package comp1110.ass2.CatanStructure;
import comp1110.ass2.CatanDice;
import comp1110.ass2.CatanEnum.ActionType;

// Author: John Larkin and Saquib Khan? (check this)
public class Action {
    public ActionType actionType;
    public String type;
    public char structure;
    public char first;
    public String id;
    public int in;
    public int out;

    // Assumes the action is well-formed
    // Author: John Larkin
    public Action(String action_string){
        this.first = action_string.charAt(0);
        if (first == 'b'){
            this.type = "build";
            this.structure = action_string.charAt(6);
            this.id = action_string.substring(6);
        }
        if (first == 't'){
            this.type = "trade";
            this.in = Integer.parseInt(action_string.substring(6));
        }
        if (first == 's'){
            this.type = "swap";
            this.in = Integer.parseInt(action_string.substring(7));
            this.out = Integer.parseInt(action_string.substring(5,6));
        }
    }

    public Action(ActionType actionType) {
        this.actionType = actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public ActionType getActionType() {
        return this.actionType;
    }
}
