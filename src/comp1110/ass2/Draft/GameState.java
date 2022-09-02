package comp1110.ass2.Draft;

public class GameState {

        /**
         * Initialise the board, set points to 0, set resources to all empty, reset trades.
         */
        public void initialiseBoard(){}

        /**
         * Number of rounds played (0,...,14).
         */
        public void roundNumber(){}

        /**
         * Score at the end of a round. accumulative.
         */
        public void score(){}

        /**
         * Who wins
         */
        public void result(){}

        /**
         * Initialise the dice for the new turn
         */
        public void newTurn(){}

        /**
         * Keep track of whose turn it is
         */
        public void whoseTurn(){}

        /**
         * Can re roll the dice or not
         */
        public void canReRoll(){}

        /**
         * if player built that round
         */
        public void didBuild(){

        }
}



/**
 * Player input
 */
class PlayerInput extends GameState{

        }