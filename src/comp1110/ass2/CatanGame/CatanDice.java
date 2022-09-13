package comp1110.ass2.CatanGame;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
     *         a board state, false otherwise.
     */
    public static boolean isBoardStateWellFormed(String board_state) {
        String[] boardStateArr = board_state.split(",");
        int[] citiesPoints = {7, 12, 20, 30};
        int[] knightsPoints = {1, 2, 3, 4, 5, 6};
        int[] settlementsPoints = {3, 4, 5, 7, 9, 11};
        Boolean[] bucket = new Boolean[boardStateArr.length];

        if (board_state == "") {
            return true;
        } else {
            for (int i = 0; i < boardStateArr.length; i++) {
                String structureLetter = boardStateArr[i].replaceAll("[0-9]", "");
                int structureInt = Integer.parseInt(0 + boardStateArr[i].replaceAll("[^0-9]", ""));
                Boolean real = boardStateArr[i].equals(structureLetter + "" + structureInt);
                if (real.equals(true)) {
                    if (structureLetter.equals("R")) {
                        if (structureInt >= 0 && structureInt <= 15) {
                            bucket[i] = true;
                            System.out.println(structureLetter);
                        } else {
                            bucket[i] = false;
                        }
                    } else if (structureLetter.equals("C")) {
                        if (Arrays.stream(citiesPoints).anyMatch(a -> a == structureInt)) {
                            bucket[i] = true;
                            System.out.println(structureLetter);
                        } else {
                            bucket[i] = false;
                        }
                    } else if (structureLetter.equals("S"))
                        if (Arrays.stream(settlementsPoints).anyMatch(a -> a == structureInt)) {
                            bucket[i] = true;
                            System.out.println(structureLetter);
                        } else {
                            bucket[i] = false;
                        }
                    else if (structureLetter.equals("J")) {
                        if (Arrays.stream(knightsPoints).anyMatch(a -> a == structureInt)) {
                            bucket[i] = true;
                            System.out.println(structureLetter);
                        } else {
                            bucket[i] = false;
                        }
                    } else if (structureLetter.equals("K")) {
                        if (Arrays.stream(knightsPoints).anyMatch(a -> a == structureInt)) {
                            bucket[i] = true;
                            System.out.println(structureLetter);
                        } else {
                            bucket[i] = false;
                        }
                    } else {
                        bucket[i] = false;
                    }
                }
                else {
                    bucket[i] = false;
                }
            }
            return  !(asList(bucket).contains(false));
            // FIXME: Task #3
        }
    }



    /**
     * Check if the string encoding of a player action is well formed.
     *
     * @param action: The string representation of the action.
     * @return true iff the string is a well-formed representation of
     *         a board state, false otherwise.
     */
    public static boolean isActionWellFormed(String action) { // Task #4
        // The strings to check. Strings contain a space at the end.
        String build = "build ";
        String trade = "trade ";
        String swap = "swap ";
        // Check if it contains the specific string
        Boolean contains_build = action.indexOf(build) == 0;
        Boolean contains_trade = action.indexOf(trade) == 0;
        Boolean contains_swap = action.indexOf(swap) == 0;
        if (contains_build && action.length() < 10){
            int type = (int) action.charAt(6);
            // Check that type is R,S,C,J or K.
            // If the letter is R (a road) followed by a single digit
            if(type == 82 && action.length() == 8){
                int num = (int) action.charAt(7);
                return (47 < num && num < 54);
            }
            // If the letter is R (a road) followed by two digits
            if(type == 82 && action.length() == 9){
                int num_1 = (int) action.charAt(7);
                int num_2 = (int) action.charAt(8);
                return (num_1 == 49 && 47 < num_2 && num_2 < 54);
            }
            // If the letter is S (a settlement)
            if(type == 83 && action.length()==8){
                int num = (int) action.charAt(7);
                return(num == 51 ||num == 52 ||num == 53 ||num == 55 ||num == 57 ); //S3,S4,S5,S7,S9
            } else if(type == 83 && action.length() == 9){ //S11
                int num_1 = (int) action.charAt(7);
                int num_2 = (int) action.charAt(8);
                return (num_1 == 49 && num_2 == 49);
            }
            // If the letter is C (a city)
            if(type == 67 && action.length() == 8){ //C7
                int num_1 = (int) action.charAt(7);
                return (num_1 == 55);
            } else if(type == 67 && action.length() == 9){ // Two digits after C
                int num_1 = (int) action.charAt(7);
                int num_2 = (int) action.charAt(8);
                if (num_1 ==49 && num_2 ==50 ){return true;} //C12
                if (num_1 ==50 && num_2 == 48 ){return true;} // C20
                if (num_1 ==51 && num_2 == 48 ){return true;} // C30
            }
            // If the letter is J or K (a knight)
            if(type == 74 || type == 85){
                int num = (int) action.charAt(7);
                return (48 < num && num < 55);
            }
        }
        // Check if it contains trade and is the correct length
        if (contains_trade && action.length() == 7){
            int num = (int) action.charAt(6);
            // Check if it contains the correct integers. In ASCII format
            if (47 < num && num < 54){
                return true;}
        }
        // Check if it contains swap and is the correct length
        if (contains_swap && action.length() == 8){
            int space_2 = (int) action.charAt(6);
            // Check if contains the second space
            if (space_2 == 32){
                int num_1 = (int) action.charAt(5);
                int num_2 = (int) action.charAt(7);
                // Check if numbers are between 0-5. Allows for num_1 = num_2. In ASCII format.
                if (47 < num_1 && num_1 < 54 && 47 < num_2 && num_2 < 54){return true;}
            }
        }
        return false;
    }

    /**
     * Roll the specified number of dice and add the result to the
     * resource state.
     *
     * The resource state on input is not necessarily empty. This
     * method should only _add_ the outcome of the dice rolled to
     * the resource state, not remove or clear the resources already
     * represented in it.
     *
     * @param n_dice: The number of dice to roll (>= 0).
     * @param resource_state: The available resources that the dice
     *        roll will be added to.
     *
     * This method does not return any value. It should update the given
     * resource_state.
     */

    // Create the DICE. 0 = Ore, 1 = Grain, 2 = Wool, 3 = Timber, 4 = Bricks, 5 = Gold.
    static final int[] DICE = {0,1,2,3,4,5};

    public static void rollDice(int n_dice, int[] resource_state) {
        // For each of the n_dice dice, do the following
	for (int i = 0; i < n_dice; i ++){
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
     * @param structure: The string representation of the structure to
     *        be built.
     * @param board_state: The string representation of the board state.
     * @return true iff the structure is a possible next build, false
     *         otherwise.
     */


    public static boolean checkBuildConstraints(String structure, String board_state) {
        String[] boardStateArray = board_state.split(",");
        List<String> boardStateList = asList(boardStateArray);
        // look at what structure is to be built,
        if (boardStateList.contains(structure)){return false;}
        // structure has not already been built
        String structure_type = structure.substring(0,1);
        int structure_int = Integer.parseInt(structure.substring(1));
        // structure is a road
        if (structure_type.equals("R")){
          if (structure_int == 0){return true;}
          if (structure_int == 2 || structure_int ==5){
              return boardStateList.contains("R" + (structure_int-2));
          }
          if (structure_int == 12){
              return boardStateList.contains("R" + (structure_int-5));
          }
          else {return boardStateList.contains("R" + (structure_int-1));}
        }
        // structure is a settlement
        if (structure_type.equals("S")){
            if (structure_int == 3){return true;}
            if (structure_int ==4 ){return boardStateList.contains("R" + 2);}
            else {return boardStateList.contains("R" + structure_int);}
        };
        // structure is a city
        if (structure_type.equals("C")){
            if (structure_int == 7){return boardStateList.contains("R" + 1);}
            if (structure_int == 12){return boardStateList.contains("R" + 4);}
            if (structure_int == 20){return boardStateList.contains("R" + 13);}
            if (structure_int == 30){return boardStateList.contains("R" + 15);}
        };
        // structure is a joker
        if (structure_type.equals("J")){
            if (structure_int == 1){return true;}
            Boolean value = boardStateList.contains("J" + (structure_int-1)) || boardStateList.contains("K" + (structure_int-1));
            return value;
        };
        // structure is a knight
        if (structure_type.equals("K")){
            {return boardStateList.contains("J" + (structure_int));}
        }
        return false; // Task #8
    }



    /**
     * Check if the available resources are sufficient to build the
     * specified structure, without considering trades or swaps.
     *
     * @param structure: The string representation of the structure to
     *        be built.
     * @param resource_state: The available resources.
     * @return true iff the structure can be built with the available
     *         resources, false otherwise.
     */
    public static boolean checkResources(String structure, int[] resource_state) {
        char structureChar = structure.charAt(0);

        if (structureChar == 'C') {
            if (resource_state[0] >= 3 && resource_state[1] >= 2) {
                return true;
            }
        } else if (structureChar == 'S') {
            if (resource_state[1] >= 1 && resource_state[2] >= 1 && resource_state[3] >= 1 && resource_state[4] >= 1) {
                return true;
            }
        } else if (structureChar == 'R') {
            if (resource_state[3] >= 1 && resource_state[4] >= 1) {
                return true;
            }
        } else if (structureChar == 'K') {
            if (resource_state[0] >= 1 && resource_state[1] >= 1 && resource_state[2] >= 1) {
                return true;
            }
        }
        else if (structureChar == 'J') {
            if (resource_state[0] >= 1 && resource_state[1] >= 1 && resource_state[2] >= 1) {
                return true;
            }
        } else {
            return false;
        }
        return false; // FIXME: Task #7
    }


    /**
     * Check if the available resources are sufficient to build the
     * specified structure, considering also trades and/or swaps.
     * This method needs access to the current board state because the
     * board state encodes which Knights are available to perform swaps.
     *
     * @param structure: The string representation of the structure to
     *        be built.
     * @param board_state: The string representation of the board state.
     * @param resource_state: The available resources.
     * @return true iff the structure can be built with the available
     *         resources, false otherwise.
     */
    public static boolean checkResourcesWithTradeAndSwap(String structure,
							 String board_state,
							 int[] resource_state) {
	return false; // FIXME: Task #12
    }

    /**
     * Check if a player action (build, trade or swap) is executable in the
     * given board and resource state.
     *
     * @param action: String representatiion of the action to check.
     * @param board_state: The string representation of the board state.
     * @param resource_state: The available resources.
     * @return true iff the action is applicable, false otherwise.
     */
    public static boolean canDoAction(String action,
				      String board_state,
				      int[] resource_state) {
        if (action.substring(0,5).equals("build")){
            // Needs to be a valid build and have the available resources
            String structure = action.substring(6);
            boolean result = checkBuildConstraints(structure, board_state) && checkResources(structure, resource_state);
            return result;
        }
        if (action.substring(0,5).equals("trade")){
            // Need 2 gold for a trade action
            return (resource_state[5] >= 2);
        }
        if (action.substring(0,4).equals("swap")){
            int out = Integer.parseInt(action.substring(5,6));
            int in = Integer.parseInt(action.substring(7));
            // The resource state needs to contain the out resource
            if (resource_state[out] >= 1){
                // The board_state needs to contain J + in but not contain K + in (otherwise it has been used)
                String[] boardStateArray = board_state.split(",");
                List<String> boardStateList = asList(boardStateArray);
                boolean result = boardStateList.contains("J" + (in + 1)) && !(boardStateList.contains("K" + (in+1)));
                return result;
            }
            return false;
        }
	 return false; //Task #9
    }

    /**
     * Check if the specified sequence of player actions is executable
     * from the given board and resource state.
     *
     * @param actions: The sequence of (string representatins of) actions.
     * @param board_state: The string representation of the board state.
     * @param resource_state: The available resources.
     * @return true iff the action sequence is executable, false otherwise.
     */
    public static boolean canDoSequence(String[] actions,
					String board_state,
					int[] resource_state) {
	 return false; // FIXME: Task #11
    }

    /**
     * Find the path of roads that need to be built to reach a specified
     * (unbuilt) structure in the current board state. The roads should
     * be returned as an array of their string representation, in the
     * order in which they have to be built. The array should _not_ include
     * the target structure (even if it is a road). If the target structure
     * is reachable via the already built roads, the method should return
     * an empty array.
     * 
     * Note that on the Island One map, there is a unique path to every
     * structure. 
     *
     * @param target_structure: The string representation of the structure
     *        to reach.
     * @param board_state: The string representation of the board state.
     * @return An array of string representations of the roads along the
     *         path.
     */
    public static String[] pathTo(String target_structure,
				  String board_state) {
	String[] result = {};
	return result; // FIXME: Task #13
    }

    /**
     * Generate a plan (sequence of player actions) to build the target
     * structure from the given board and resource state. The plan may
     * include trades and swaps, as well as bulding other structures if
     * needed to reach the target structure or to satisfy the build order
     * constraints.
     *
     * However, the plan must not have redundant actions. This means it
     * must not build any other structure that is not necessary to meet
     * the building constraints for the target structure, and it must not
     * trade or swap for resources if those resources are not needed.
     *
     * If there is no valid build plan for the target structure from the
     * specified state, return null.
     *
     * @param target_structure: The string representation of the structure
     *        to be built.
     * @param board_state: The string representation of the board state.
     * @param resource_state: The available resources.
     * @return An array of string representations of player actions. If
     *         there exists no valid build plan for the target structure,
     *         the method should return null.
     */
    public static String[] buildPlan(String target_structure,
				     String board_state,
				     int[] resource_state) {
	 return null; // FIXME: Task #14
    }

}
