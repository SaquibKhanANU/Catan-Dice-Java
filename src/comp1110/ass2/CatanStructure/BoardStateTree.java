package comp1110.ass2.CatanStructure;

public class BoardStateTree extends GameTree{


    // The BoardStateTree needs four separate GameTrees,
    // Encode the current jokers, knights
    GameTree knights;
    // Encode the cities
    GameTree cities;
    // Encode the settlements
    GameTree settlements;
    // Encode the roads
    GameTree roads;


    // This constructor will create four GameTrees given a string encoding of the board state
    public BoardStateTree(String board_state){
        String[] board_state_array = board_state.split(",");
        for (int i = 0; i < board_state_array.length; i++){
            Structure current_structure = new Structure(board_state_array[i]);
//            if (current_structure.type == 'R'){}
//            if (current_structure.type == 'R'){}
//            if (current_structure.type == 'R'){}
//            if (current_structure.type == 'R'){}
        }
    }


}
