package comp1110.ass2;



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
        return false;
        // FIXME: Task #3
    }

    /**
     * Check if the string encoding of a player action is well formed.
     *
     * @param action: The string representation of the action.
     * @return true iff the string is a well-formed representation of
     *         a board state, false otherwise.
     */
    public static boolean isActionWellFormed(String action) { // Task #4
        // The strings to check. Strings contains a space at the end.
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
    public static void rollDice(int n_dice, int[] resource_state) {
	// FIXME: Task #6
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
    public static boolean checkBuildConstraints(String structure,
						String board_state) {
	 return false; // FIXME: Task #8
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
    public static boolean checkResources(String structure,
					 int[] resource_state) {
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
	 return false; // FIXME: Task #9
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
