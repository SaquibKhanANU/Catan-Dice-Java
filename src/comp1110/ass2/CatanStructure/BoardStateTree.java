package comp1110.ass2.CatanStructure;

import java.util.*;

import static java.util.Collections.sort;

public class BoardStateTree extends GameTree {


    // A BoardStateTree encodes the ordering in which structures need to be built
    // Encode the current jokers, knights
    GameTree jokersAndKnights = new GameTree();
    // Encode the cities
    public ArrayList<String> cities = new ArrayList<>();
    // Encode the settlements
    public ArrayList<String> settlements = new ArrayList<>();
    // Encode the roads
    GameTree roads = new GameTree();

    public String board_state = new String();

    public static String entire_board = "RI,R0,R1,R2,R3,R4,R5,R6,R7,R8,R9,R10,R11,R12,R13,R14,R15,S3,S4,S5,S7,S9,S11,C7,C12,C20,C30,J1,J2,J3,J4,J5,J6,K1,K2,K3,K4,K5,K6";

    // This constructor will create four GameTrees given a string encoding of the board state
    public BoardStateTree(String board_state) {
        this.board_state = board_state;
        String[] board_state_array = board_state.split(",");
        ArrayList<String> jokers = new ArrayList<>();
        ArrayList<String> knights = new ArrayList<>();
        ArrayList<String> roads_list = new ArrayList<>();
        for (int i = 0; i < board_state_array.length; i++) {
            Structure current_structure = new Structure(board_state_array[i]);
            if (current_structure.type == 'R') {
                roads_list.add(board_state_array[i]);
            }
            if (current_structure.type == 'S') {
                settlements.add(board_state_array[i]);
            }
            if (current_structure.type == 'C') {
                cities.add(board_state_array[i]);
            }
            if (current_structure.type == 'J') {
                jokers.add(board_state_array[i]);
            }
            if (current_structure.type == 'K') {
                knights.add(board_state_array[i]);
            }
        }
        // Sort the cities and the settlements into build order
        sort(cities, Structure_Ordering);
        sort(settlements, Structure_Ordering);
        sort(jokers, Structure_Ordering);
        sort(knights, Structure_Ordering);
        sort(roads_list, Structure_Ordering);
        // Now put the jokers, knights and roads into GameTrees
        // Jokers and Knights are placed in the tree with knights down the branch of their respective joker
        // Roads are placed in a tree with the ordering in which they can be built.

        // Jokers and Knights
        if (jokers != null && jokers.size()!= 0){
            // Add the jokers to the tree first
            jokersAndKnights.node = jokers.get(0);
            for (int i = 1; i < jokers.toArray().length; i++){
            jokersAndKnights.add(jokers.get(i-1), null, jokers.get(i));
            }
            if (knights != null && knights.size()!= 0) {
                // Add the respective knights that have been activated
                for (int i = 0; i < knights.toArray().length; i++){
                    jokersAndKnights.add("J" + knights.get(i).substring(1), knights.get(i),null);
                }

            }
        }

        // Add roads to the game tree, roads_list always contains RI.
        //System.out.println("CONSTRUCTOR IS " + roads_list.stream().toList());
        roads.node = roads_list.get(0);
        for (int i = 1; i < roads_list.size(); i++){
            Structure current_road = new Structure(roads_list.get(i));
            if (current_road.value == 1){
                roads.add("R0", "R1", null);
            }
            else if (current_road.value == 4){
                roads.add("R3", "R4", null);
            }
            else if (current_road.value == 12){
                roads.add("R7", "R12", null);
            }
            else {
                roads.add(roads_list.get(i-1), null, roads_list.get(i));
            }
        }
    }




    public HashMap<String, String> makeProblems(){
        HashMap<String, String> problems = new HashMap();
        problems.put("R1", "C7");
        problems.put("R2", "S4");
        problems.put("R4", "C12");
        problems.put("R5", "S5");
        problems.put("R7", "S7");
        problems.put("R9", "S9");
        problems.put("R11", "S11");
        problems.put("R13", "C20");
        problems.put("R15", "C30");
        return problems;
    }



    /**
     * This method removes the given structure from the BoardStateTree.
     * A structure can only be removed iff it is at the end of a tree or list.
     * Only Roads, Settlements and Cities can be removed.
     * If the id is not on the board then return false
     * @param id : The id to be removed, for example J6 or R12.
     * @return true iff the structure can be removed
     */
    public boolean canRemove(String id){
        List<String> board_state_array = Arrays.stream(this.board_state.split(",")).toList();
        if (!board_state_array.contains(id)){return false;}
        Structure remove_structure = new Structure(id);
        if (remove_structure.type == 'R'){
            if (this.roads == null){return false;}
            else if (this.roads.node == id){return false;} // Cannot remove "RI"
            else {
                HashMap<String,String> problems = makeProblems();
                String problem_structure = problems.get(id);
                if (this.roads.canPrune(id) && !(board_state_array.contains(problem_structure))){
                    // Remove any null branches from this move
                    GameTree new_tree = new GameTree();
                    new_tree.node = "RI";
                    this.roads = this.roads.removeNulls(new_tree);
                    return true;
                };
            }
        };
        if (remove_structure.type == 'S'){
            if (this.settlements == null){return false;}
            if (this.settlements.size() == 1){
                this.settlements.remove(0);
                return true;
            }
            else if (this.settlements.get(this.settlements.size()-1).equals(id)){
                // Remove the last settlement
                this.settlements.remove(this.settlements.size()-1);
                return true;
            }
        };
        if (remove_structure.type == 'C'){
            if (this.cities == null){return false;}
            if (this.cities.size() == 1){
                this.cities.remove(0);
                return true;
            }
            else if (this.cities.get(this.cities.size()-1).equals(id)){
                System.out.println("CODE IS HERE");
                System.out.println(this.cities);
                // Remove the last city
                this.cities.remove(this.cities.size()-1);
                return true;
            }
        };
        if (remove_structure.type == 'J'){
            // A Joker can be removed if the corresponding knight has not been built
            // That is if we canPrune the GameTree from jokers and knights
            if (this.jokersAndKnights.canPrune(id)){
                GameTree new_tree = new GameTree();
                new_tree.node = "J1";
                this.jokersAndKnights = this.jokersAndKnights.removeNulls(new_tree);
                return true;
                }
            }
        return false;
    }



    /**
     * The below Comparator imposes a new ordering on Strings which can be used to sort fields in BoardTreeState
     * The sorting order is a numerical ordering grouped by structure types
     */
    static String[] ORDERED_LIST = new String[]{"RI", "R0", "R2", "R3", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R1", "R4", "R12", "R13", "R14", "R15", "S3", "S4", "S5", "S7", "S9", "S11", "C7", "C12", "C20", "C30", "J1", "J2", "J3", "J4", "J5", "J6", "K1", "K2", "K3", "K4", "K5", "K6"};

    static final Comparator<String> Structure_Ordering = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            int res = 0;
            for (int i = 0; i < ORDERED_LIST.length; i++) {
                for (int j = 0; j < ORDERED_LIST.length; j++) {
                    if (ORDERED_LIST[i].equals(o1) && ORDERED_LIST[j].equals(o2)) {
                        res = i - j;
                    }
                }
            }
            return res;
        }
    };

    public static void main(String[] args) {
//        ArrayList<String> test = new ArrayList<>();
//        test.add("R5");
//        test.add("J1");
//        test.add("K6");
//        test.add("K1");
//        test.add("J1");
//        test.add("R7");
//        test.add("R1");
//        System.out.println(test);
//        sort(test, Structure_Ordering);
//        System.out.println(test);


        BoardStateTree board = new BoardStateTree(entire_board);
//        ArrayList<Object> res = new ArrayList<>();
//        System.out.println(board.jokersAndKnights.fold(res));

//        String test2 = "RI,R0,R1,R2,R3,R4,R5,R6,R7,R9,R10,R11,R12,R13,S3,S4,S5,S7,C7,C12,J1,J2,J3,J4,K1,K2";
//        BoardStateTree board2 = new BoardStateTree(test2);
//        ArrayList<Object> res3 = new ArrayList<>();
//        //System.out.println(board2.roads.fold(res3));
//        System.out.println(board2.jokersAndKnights.fold(res3));
//        System.out.println(board2.cities);
//        System.out.println(board2.settlements);

        ArrayList<Object> res1 = new ArrayList<>();
        System.out.println(board.roads.fold(res1));
        System.out.println(board.canRemove("R11"));
        ArrayList<Object> res2 = new ArrayList<>();
        System.out.println(board.roads.fold(res2));
    }
}



