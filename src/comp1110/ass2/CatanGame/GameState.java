package comp1110.ass2.CatanGame;


// This class contains all the information for a game of CatanDice.
// When a GameState is constructed the number of players in the game is
// specified

/**
 * This class contains information to specify a game of Catan Dice.
 * When a GameState is constructed the number of players needs to be specified.
 * Then it creates an array of CatanPlayer objects which each specify a player
 * in the game. The GameState also keeps track of the round number,
 * turn number (between 0 and num-1 inclusive).
 */

public class GameState {
    // The number of players, between 2 and 4.
    public int num;

    // The index of the current players turn
    public int turn_num;

    // The number of completed rounds (for example if each player
    // has had one turn then it is has been one round.
    public int round;

    public GameState(int num){
        // Initialise the values
        this.num = num;
        this.turn_num = 0;
        this.round = 0;

        // Use the CatanPlayer constructor to construct num players.
        // Each player can be references using players[i]
        CatanPlayer[] players = new CatanPlayer[num];
        for (int i = 0; i < players.length; i ++){
            players[i] = new CatanPlayer(i);
        }

    }
}
