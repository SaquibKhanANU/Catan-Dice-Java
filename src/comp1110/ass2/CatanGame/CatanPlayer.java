package comp1110.ass2.CatanGame;

import comp1110.ass2.CatanStructure.Structure;
import comp1110.ass2.gui.Game;

import java.util.ArrayList;
import java.util.List;

// Author: Saquib Khan and John Larkin (check this)
public class CatanPlayer {

    // The index distinguishes the player, either 0,1,2,3
    // The choice of 0,1,2,3 is so that in an array CatanPlayer[] players = new CatanPlayer[number of players].
    // The ith player can be referenced with players[i].
    // Then players[i].index = i.
    public int index;
    // The board state of the current player, an array of structures.
    // structures[0].id will return the id of position 0, for example R1.
    public ArrayList<Structure> structures;
    // The current resources available
    public int[] resource_state;
    // The current score of the player
    public int score;
    // The current number of completed turns of the player
    public int turn_num;
    //total score
    public ArrayList<Integer> scoreTotal;
    public int finalScore;
    public String name;
    public boolean currentTurn;

    // structures for current round, clears at end of each round.
    public ArrayList<Structure> structuresForRound;
    // Players board_state
    public String board_state;
    // Player action
    public String action;
    public CatanPlayer(int index){
        // Initialise the values associated to this player
        this.index = index;
        switch (index) {
            case 1 -> this.name = "PLAYER ONE";
            case 2 -> this.name = "PLAYER TWO";
            case 3 -> this.name = "PLAYER THREE";
            case 4 -> this.name = "PLAYER FOUR";
        }
        this.finalScore = 0;
        this.structures = new ArrayList<>();
        this.resource_state = new int[6];
        this.score = 0;
        this.turn_num = 0;
        this.scoreTotal = new ArrayList<>();
        this.structuresForRound = new ArrayList<>();
        this.board_state = "";
        this.action = "";
    }

    public void calculateFinalScore() {
        int sum = 0;
        for (int i : scoreTotal) {
            sum = sum + i;
        }
        finalScore = sum;
    }

    public void setCurrentTurn(boolean currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void changeResourceState(int index, int change) {
        resource_state[index] = resource_state[index] + change;
    }
}
