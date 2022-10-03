package comp1110.ass2.CatanGame;

import comp1110.ass2.CatanStructure.Structure;

import java.util.ArrayList;
import java.util.List;

// John Larkin
public class CatanPlayer {

    // The index distinguishes the player, either 0,1,2,3
    // The choice of 0,1,2,3 is so that in an array CatanPlayer[] players = new CatanPlayer[number of players].
    // The ith player can be referenced with players[i].
    // Then players[i].index = i.
    int index;
    // The board state of the current player, an array of structures.
    // structures[0].id will return the id of position 0, for example R1.
    ArrayList<Structure> structures;
    // The current resources available
    int[] resource_state;
    // The current score of the player
    int score;
    // The current number of completed turns of the player
    int turn_num;


    public CatanPlayer(int index){
        // Initialise the values associated to this player
        this.index = index;
        this.structures = new ArrayList<>();
        this.resource_state = new int[6];
        this.score = 0;
        this.turn_num = 0;
    }



}
