package comp1110.ass2;

import comp1110.ass2.CatanStructure.Action;
import comp1110.ass2.CatanStructure.BoardStateTree;
import comp1110.ass2.CatanStructure.GameTree;
import comp1110.ass2.CatanStructure.Structure;

import java.util.*;


import static comp1110.ass2.CatanStructure.GameTree.createCatanGameTree;
import static java.lang.reflect.Array.setInt;
import static java.util.Arrays.asList;

public class CatanDice {

    /**
     * Check if the string encoding of a board state is well formed.
     * Note that this does not mean checking if the state is valid
     * (represents a state that the player could get to in game play),
     * only that the string representation is syntactically well formed.
     *
     * @param board_state: The string representation of the board state.
     * @return true iff the string is a well-formed representation of
     * a board state, false otherwise.
     */
    public static boolean isBoardStateWellFormed(String board_state) {
        String[] boardStateArr = board_state.split(",");
        int[] citiesPoints = {7, 12, 20, 30};
        int[] knightsPoints = {1, 2, 3, 4, 5, 6};
        int[] settlementsPoints = {3, 4, 5, 7, 9, 11};
        Boolean[] bucket = new Boolean[boardStateArr.length];

        if (board_state.equals("")) {
            return true;
        } else {
            for (int i = 0; i < boardStateArr.length; i++) {
                String structureLetter = boardStateArr[i].replaceAll("[0-9]", "");
                int structureInt = Integer.parseInt(0 + boardStateArr[i].replaceAll("[^0-9]", ""));
                Boolean real = boardStateArr[i].equals(structureLetter + "" + structureInt);
                if (real.equals(true)) {
                    switch (structureLetter) {
                        case "R" -> bucket[i] = structureInt >= 0 && structureInt <= 15;
                        case "C" -> bucket[i] = Arrays.stream(citiesPoints).anyMatch(a -> a == structureInt);
                        case "S" -> bucket[i] = Arrays.stream(settlementsPoints).anyMatch(a -> a == structureInt);
                        case "J", "K" -> bucket[i] = Arrays.stream(knightsPoints).anyMatch(a -> a == structureInt);
                        default -> bucket[i] = false;
                    }
                } else {
                    bucket[i] = false;
                }
            }
            return !(asList(bucket).contains(false));
            // FIXME: Task #3
        }
    }


    /**
     * Check if the string encoding of a player action is well formed.
     *
     * @param action: The string representation of the action.
     * @return true iff the string is a well-formed representation of
     * a board state, false otherwise.
     */
    // John Larkin
    public static boolean isActionWellFormed(String action) { // Task #4
        // The strings to check. Strings contain a space at the end.
        String build = "build ";
        String trade = "trade ";
        String swap = "swap ";
        // Check if it contains the specific string
        Boolean contains_build = action.indexOf(build) == 0;
        Boolean contains_trade = action.indexOf(trade) == 0;
        Boolean contains_swap = action.indexOf(swap) == 0;
        if (contains_build && action.length() < 10) {
            int type = action.charAt(6);
            // Check that type is R,S,C,J or K.
            // If the letter is R (a road) followed by a single digit
            if (type == 82 && action.length() == 8) {
                int num = action.charAt(7);
                return (47 < num && num < 54);
            }
            // If the letter is R (a road) followed by two digits
            if (type == 82 && action.length() == 9) {
                int num_1 = action.charAt(7);
                int num_2 = action.charAt(8);
                return (num_1 == 49 && 47 < num_2 && num_2 < 54);
            }
            // If the letter is S (a settlement)
            if (type == 83 && action.length() == 8) {
                int num = action.charAt(7);
                return (num == 51 || num == 52 || num == 53 || num == 55 || num == 57); //S3,S4,S5,S7,S9
            } else if (type == 83 && action.length() == 9) { //S11
                int num_1 = action.charAt(7);
                int num_2 = action.charAt(8);
                return (num_1 == 49 && num_2 == 49);
            }
            // If the letter is C (a city)
            if (type == 67 && action.length() == 8) { //C7
                int num_1 = action.charAt(7);
                return (num_1 == 55);
            } else if (type == 67 && action.length() == 9) { // Two digits after C
                int num_1 = action.charAt(7);
                int num_2 = action.charAt(8);
                if (num_1 == 49 && num_2 == 50) {
                    return true;
                } //C12
                if (num_1 == 50 && num_2 == 48) {
                    return true;
                } // C20
                if (num_1 == 51 && num_2 == 48) {
                    return true;
                } // C30
            }
            // If the letter is J or K (a knight)
            if (type == 74 || type == 85) {
                int num = action.charAt(7);
                return (48 < num && num < 55);
            }
        }
        // Check if it contains trade and is the correct length
        if (contains_trade && action.length() == 7) {
            int num = action.charAt(6);
            // Check if it contains the correct integers. In ASCII format
            if (47 < num && num < 54) {
                return true;
            }
        }
        // Check if it contains swap and is the correct length
        if (contains_swap && action.length() == 8) {
            int space_2 = action.charAt(6);
            // Check if contains the second space
            if (space_2 == 32) {
                int num_1 = action.charAt(5);
                int num_2 = action.charAt(7);
                // Check if numbers are between 0-5. Allows for num_1 = num_2. In ASCII format.
                return 47 < num_1 && num_1 < 54 && 47 < num_2 && num_2 < 54;
            }
        }
        return false;
    }

    /**
     * Roll the specified number of dice and add the result to the
     * resource state.
     * <p>
     * The resource state on input is not necessarily empty. This
     * method should only _add_ the outcome of the dice rolled to
     * the resource state, not remove or clear the resources already
     * represented in it.
     *
     * @param n_dice: The number of dice to roll (>= 0).
     * @param resource_state: The available resources that the dice
     * roll will be added to.
     * <p>
     * This method does not return any value. It should update the given
     * resource_state.
     */

    // Create the DICE. 0 = Ore, 1 = Grain, 2 = Wool, 3 = Timber, 4 = Bricks, 5 = Gold.
    static final int[] DICE = {0, 1, 2, 3, 4, 5};

    public static void rollDice(int n_dice, int[] resource_state) {
        // For each of the n_dice dice, do the following
        for (int i = 0; i < n_dice; i++) {
            var rand = new Random();
            int roll_value = rand.nextInt(DICE.length);
            int resource_count = resource_state[roll_value];
            resource_count++; // Increment the number of that resource by 1
            resource_state[roll_value] = resource_count; // Store the value back into the resource_state array
        }
    }

    /**
     * Check if the specified structure can be built next, given the
     * current board state. This method should check that the build
     * meets the constraints described in section "Building Constraints"
     * of the README file.
     *
     * @param structure:   The string representation of the structure to
     *                     be built.
     * @param board_state: The string representation of the board state.
     * @return true iff the structure is a possible next build, false
     * otherwise.
     */

// John Larkin
    public static boolean checkBuildConstraints(String structure, String board_state) {
        String[] boardStateArray = board_state.split(",");
        List<String> boardStateList = asList(boardStateArray);
        // look at what structure is to be built,
        if (boardStateList.contains(structure)) {
            return false;
        }
        // structure has not already been built
        Structure current_structure = new Structure(structure);
        int value = current_structure.value;
        // structure is a road
        if (current_structure.type == 'R') {
            if (value == 0) {
                return true;
            }
            if (value == 2 || value == 5) {
                return boardStateList.contains("R" + (value - 2));
            }
            if (value == 12) {
                return boardStateList.contains("R" + (value - 5));
            } else {
                return boardStateList.contains("R" + (value - 1));
            }
        }
        // structure is a settlement
        if (current_structure.type == 'S') {
            if (value == 3) {
                return true;
            }
            if (value == 4) {
                return boardStateList.contains("R" + 2);
            } else {
                return boardStateList.contains("R" + value);
            }
        }
        // structure is a city
        if (current_structure.type == 'C') {
            if (value == 7) {
                return boardStateList.contains("R" + 1);
            }
            if (value == 12) {
                return boardStateList.contains("R" + 4);
            }
            if (value == 20) {
                return boardStateList.contains("R" + 13);
            }
            if (value == 30) {
                return boardStateList.contains("R" + 15);
            }
        }
        // structure is a joker
        if (current_structure.type == 'J') {
            if (value == 1) {
                return true;
            }
            Boolean truth_value = boardStateList.contains("J" + (value - 1)) || boardStateList.contains("K" + (value - 1));
            return truth_value;
        }
        // structure is a knight
        if (current_structure.type == 'K') {
            {
                return boardStateList.contains("J" + (value));
            }
        }
        return false; // Task #8
    }


    /**
     * Check if the available resources are sufficient to build the
     * specified structure, without considering trades or swaps.
     *
     * @param structure:      The string representation of the structure to
     *                        be built.
     * @param resource_state: The available resources.
     * @return true iff the structure can be built with the available
     * resources, false otherwise.
     */
    public static boolean checkResources(String structure, int[] resource_state) {
        char structureChar = structure.charAt(0);

        if (structureChar == 'C') {
            return resource_state[0] >= 3 && resource_state[1] >= 2;
        } else if (structureChar == 'S') {
            return resource_state[1] >= 1 && resource_state[2] >= 1 && resource_state[3] >= 1 && resource_state[4] >= 1;
        } else if (structureChar == 'R') {
            return resource_state[3] >= 1 && resource_state[4] >= 1;
        } else if (structureChar == 'K' || structureChar == 'J') {
            return resource_state[0] >= 1 && resource_state[1] >= 1 && resource_state[2] >= 1;
        } else {
            return false;
        }// FIXME: Task #7
    }


    /**
     * Check if the available resources are sufficient to build the
     * specified structure, considering also trades and/or swaps.
     * This method needs access to the current board state because the
     * board state encodes which Knights are available to perform swaps.
     *
     * @param structure:      The string representation of the structure to
     *                        be built.
     * @param board_state:    The string representation of the board state.
     * @param resource_state: The available resources.
     * @return true iff the structure can be built with the available
     * resources, false otherwise.
     */
    // John Larkin
    public static boolean checkResourcesWithTradeAndSwap(String structure,
                                                         String board_state,
                                                         int[] resource_state) {
        // If the structure can be built in the current resource state, then return true
        if (checkResources(structure, resource_state)) {
            return true;
        }
        // Now check if the structure can be built considering swaps and trades.
        else {
            // Find out what is needed to build the structure
            Structure current_structure = new Structure(structure);
            int[] cost = current_structure.getResourceCost();
            // Find out resource_state - cost, values that are negative need to be swapped or traded for
            int[] difference = subtractArray(resource_state, cost);
            String[] array = board_state.split(",");
            List<String> board_state_list = asList(array);
            // Try to make up the difference through jokers and knights
            for (int i = 0; i < difference.length; i++) {
                if (difference[i] < 0) {
                    // Check the board state has J(i+1) but not K(i+1)
                    if (board_state_list.contains("J" + (i + 1)) && !(board_state_list.contains("K" + (i + 1)))) {
                        // If so then add to difference
                        difference[i]++;
                    }
                }
            }
            // If difference is all positive then return true
            if (allNonNegative(difference)) {
                return true;
            }
            // Else a trade may need to be done
            // WLOG if resource_state has J6 but not K6 then swap an available resource
            // If there is no available resource then add to the gold
            if (board_state_list.contains("J6") && !(board_state_list.contains("K6"))) {
                // Add to one negative element in difference
                for (int k = 0; k < difference.length; k++) {
                    if (difference[k] < 0) {
                        for (int l = 0; l < difference.length; l++) {
                            if (difference[l] > 0) {
                                difference[l]--;
                                difference[k]++;
                                break;
                            }
                        }
                        break;
                    }
                    // If the loop has not reached a break statement then no
                    // excess resource was available to swap using J6.
                    // In this case k == 5.
                    else if (k == 5) {
                        difference[5]++;
                    }
                }
            }
            for (int j = 0; j < difference.length; j++) {
                // If there are at least 2 gold then can add to difference
                if (difference[j] < 0 && difference[5] >= 2) {
                    difference[j]++;
                    difference[5] -= 2;
                }
            }
            if (allNonNegative(difference)) {
                return true;
            }
        }
        return false; // Task #12
    }

    // Return true if every element is non-negative
    public static boolean allNonNegative(int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a player action (build, trade or swap) is executable in the
     * given board and resource state.
     *
     * @param action:         String representatiion of the action to check.
     * @param board_state:    The string representation of the board state.
     * @param resource_state: The available resources.
     * @return true iff the action is applicable, false otherwise.
     */
    public static boolean canDoAction(String action,
                                      String board_state,
                                      int[] resource_state) {
        Action current_action = new Action(action);
        if (current_action.first == 'b') {
            // Needs to be a valid build and have the available resources
            String structure = action.substring(6);
            boolean result = checkBuildConstraints(structure, board_state) && checkResources(structure, resource_state);
            return result;
        }
        if (current_action.first == 't') {
            // Need 2 gold for a trade action
            return (resource_state[5] >= 2);
        }
        if (current_action.first == 's') {
            int out = current_action.out;
            int in = current_action.in;
            // The resource state needs to contain the out resource
            if (resource_state[out] >= 1) {
                // The board_state needs to contain J + in but not contain K + in (otherwise it has been used)
                String[] boardStateArray = board_state.split(",");
                List<String> boardStateList = asList(boardStateArray);
                String cannot_contain = "K" + (in + 1);
                boolean case_1 = boardStateList.contains("J" + (in + 1)) && !(boardStateList.contains(cannot_contain));
                // If the wildcard joker is available then action can be done
                boolean case_2 = boardStateList.contains("J6") && !(boardStateList.contains("K6"));
                return case_1 || case_2;
            } else {
                return false;
            }
        }
        return false; //Task #9
    }

    /**
     * Check if the specified sequence of player actions is executable
     * from the given board and resource state.
     *
     * @param actions:        The sequence of (string representatins of) actions.
     * @param board_state:    The string representation of the board state.
     * @param resource_state: The available resources.
     * @return true iff the action sequence is executable, false otherwise.
     */
    public static boolean canDoSequence(String[] actions,
                                        String board_state,
                                        int[] resource_state) {
        String[] updated_actions = actions;
        int[] updated_resource_state = new int[6];
        String updated_board_state = board_state;
        System.arraycopy(resource_state, 0, updated_resource_state, 0, 6);
//        System.out.println("Actions is " + Arrays.stream(actions).toList().toString());
//        System.out.println("Board State is  " + board_state);
//        System.out.println("Resources are  " + resource_state[0]+", " + resource_state[1]+", " + resource_state[2]+", " + resource_state[3]+", " + resource_state[4]+", "+ resource_state[5]+", "   );
        // Assume sequence can be done unless we find an action that cannot be done.
        boolean res = true;
        for (int i = 0; i < actions.length; i++) {
            if (canDoAction(updated_actions[i], updated_board_state, updated_resource_state)) {
                // NOW UPDATE THE ACTIONS
                // Otherwise the action can be done and the actions, board_state and resource state
                // need to be updated.
                Action current_action = new Action(updated_actions[i]);
                // Split cases depending upon the current_action.
                if (current_action.first == 'b') {
//                    System.out.println("ACTION IS A BUILD");
                    // Add the structure to the updated_board_state
                    updated_board_state = updated_board_state + "," + current_action.id;
                    Structure structure = new Structure(current_action.id);
                    // Remove the resources
                    updated_resource_state = subtractArray(updated_resource_state, structure.getResourceCost());
                } else if (current_action.first == 't') {
//                    System.out.println("ACTION IS A TRADE");
                    // Remove resources from updated_resource_state
                    updated_resource_state[5] = updated_resource_state[5] - 2;
                    // Add resource that has been traded for
                    int in = current_action.in;
                    updated_resource_state[in]++;
                } else if (current_action.first == 's') {
//                    System.out.println("ACTION IS A SWAP");
                    int out = current_action.out;
                    int in = current_action.in;
                    // Add in resource to resource state
                    int new_in = updated_resource_state[in] + 1;
                    setInt(updated_resource_state, in, new_in);
                    // Remove out resource from resource state
                    int new_out = updated_resource_state[out] - 1;
                    setInt(updated_resource_state, out, new_out);
                    // Update board_copy depending on whether the wildcard joker was used.
                    String[] boardCopyArray = updated_board_state.split(",");
                    List<String> boardCopyList = asList(boardCopyArray);
                    if (boardCopyList.contains("K" + (in + 1))) {
                        updated_board_state = updated_board_state + ",K6";
                    } else {// Wildcard was not used
                        updated_board_state = updated_board_state + (",K" + (in + 1));
                    }
                }
            }
            // This is the only location in the code that can update the result.
            else {
//                System.out.println("Action cannot be done return false");
                res = false;
            }
//            System.out.println(res);
        }
        return res;
    }


    // Given array1 and array 2 assumed to be of the same length and not empty
    // Return an array that is a componentwise subtraction of array1 - array2
    public static int[] subtractArray(int[] array1, int[] array2) {
        int[] res = new int[array1.length];
        for (int i = 0; i < array1.length; i++) {
            res[i] = array1[i] - array2[i];
        }
        return res;
    }

    /**
     * Find the path of roads that need to be built to reach a specified
     * (unbuilt) structure in the current board state. The roads should
     * be returned as an array of their string representation, in the
     * order in which they have to be built. The array should _not_ include
     * the target structure (even if it is a road). If the target structure
     * is reachable via the already built roads, the method should return
     * an empty array.
     * <p>
     * Note that on the Island One map, there is a unique path to every
     * structure.
     *
     * @param target_structure: The string representation of the structure
     *                          to reach.
     * @param board_state:      The string representation of the board state.
     * @return An array of string representations of the roads along the
     * path.
     */
    public static String[] pathTo(String target_structure,
                                  String board_state) {
        // Create the GameTree
        GameTree path = createCatanGameTree();
        ArrayList<Object> path_array = new ArrayList<>();
        // Find the path from RI to this target structure, includes settlements and cities
        ArrayList<Object> paths_list = path.findPath(target_structure, path_array);
        paths_list.remove("RI");
        // If the target structure is a road then do not include it in paths_list
        if (target_structure.charAt(0) == 'R') {
            paths_list.remove(target_structure);
        }
        Object[] paths = paths_list.toArray();
        // Remove occurrences of cities and settlements
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < paths.length; i++) {
            if (paths[i].toString().charAt(0) == 'R') {
                res.add((String) paths[i]);
            }
        }
        // Remove occurrences in paths of any already built roads
        String[] board_state_array = board_state.split(",");
        for (int j = 0; j < paths.length; j++) {
            for (int k = 0; k < board_state_array.length; k++) {
                if (paths[j].equals(board_state_array[k])) {
                    res.remove(paths[j]);
                }
            }
        }
        Object[] res_array = res.toArray();
        String[] result = new String[res_array.length];
        for (int l = 0; l < res_array.length; l++) {
            result[l] = res_array[l].toString();
        }
        return result; // FIXME: Task #13
    }

    /**
     * Generate a plan (sequence of player actions) to build the target
     * structure from the given board and resource state. The plan may
     * include trades and swaps, as well as bulding other structures if
     * needed to reach the target structure or to satisfy the build order
     * constraints.
     * <p>
     * However, the plan must not have redundant actions. This means it
     * must not build any other structure that is not necessary to meet
     * the building constraints for the target structure, and it must not
     * trade or swap for resources if those resources are not needed.
     * <p>
     * If there is no valid build plan for the target structure from the
     * specified state, return null.
     *
     * @param target_structure: The string representation of the structure
     *                          to be built.
     * @param board_state:      The string representation of the board state.
     * @param resource_state:   The available resources.
     * @return An array of string representations of player actions. If
     * there exists no valid build plan for the target structure,
     * the method should return null.
     */
    public static String[] buildPlan(String target_structure,
                                     String board_state,
                                     int[] resource_state) {
        ArrayList<String> board_state_list = requiredStructures(target_structure, board_state);
        board_state_list.add(target_structure);
        // Now the board_state_list contains all the structures that need to be built including the target structure
        int[] total_cost = buildCost(board_state_list);
        // Check resource_state against the cost, need to consider trades and swaps


        return null; // FIXME: Task #14
    }


    /**
     * Return the necessary structures needed to reach the target structure,
     * Assumes the board state is valid.
     * @param target_structure
     * @param board_state
     * @return An arraylist with only the necessary structures to build the target structure.
     * The result does not include the target structure.
     * The result only contains the roads, settlements and cities needed
     */
    public static ArrayList<String> requiredStructures(String target_structure,
                                                       String board_state) {
        ArrayList<String> res = new ArrayList<>();
        // Generate the entire board in a gametree
        BoardStateTree entire_tree = new BoardStateTree(BoardStateTree.entire_board);
        String[] current_state = board_state.split(",");
        List<String> current_state_list = asList(current_state);

        // To build the target structure, you need the roads to get there.
        // This is given in pathTo
        String[] roads = pathTo(target_structure, board_state);
        Collections.addAll(res, roads);
        // Now we need to add any required structures according to the building constraints
        Structure target = new Structure(target_structure);
        if (target.type == 'S') {
            for (int i = 0; i < entire_tree.settlements.size(); i++) {
                Structure current = new Structure(entire_tree.settlements.get(i));
                if (current.value == target.value) {
                    break;
                } else {
                    if (!(current_state_list.contains(entire_tree.settlements.get(i)))){res.add(entire_tree.settlements.get(i));}
                }
            }
        }
        if (target.type == 'C') {
            for (int i = 0; i < entire_tree.cities.size(); i++) {
                Structure current = new Structure(entire_tree.cities.get(i));
                // R1 must be built for C7
                if (current.value == 7){
                    if (!(current_state_list.contains("R1"))){res.add("R1");}
                }
                if (current.value == 12){
                    if (!(current_state_list.contains("R4"))){res.add("R4");}
                }
                if (current.value == target.value) {
                    break;
                } else {
                    if (!(current_state_list.contains(entire_tree.cities.get(i)))){res.add(entire_tree.cities.get(i));}
                }
            }
        }
            return res;}


    /**
     * Find the cost of building the specified structures in the ArrayList
     * @param structures: The structures to build
     * @return the resource cost of building all of these structures in an integer array
     */
    public static int[] buildCost(ArrayList<String> structures){
        int[] res = new int[6];
        for (int i = 0; i < structures.size(); i++){
            Structure current = new Structure(structures.get(i));
            int[] current_cost = current.getResourceCost();
            for (int j = 0; j < res.length; j++){
                res[j] += current_cost[j];
            }
        }
        return res;
    }

    public static void main(String[] args) {
        ArrayList<String> res = requiredStructures("C20","RI,R0,S3,R2,S4,R1,C7");
        System.out.println(res);

        ArrayList<String> test_cost = new ArrayList<>();
        test_cost.add("RI");
        test_cost.add("S7");

        int[] out = buildCost(test_cost);
        System.out.println(out[0] + " " + out[1] + " " + out[2] + " " +out[3] + " " + out[4] + " " + out[5]);
    }
}